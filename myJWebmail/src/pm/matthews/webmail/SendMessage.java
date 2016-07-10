package pm.matthews.webmail;

import net.htmlparser.jericho.*;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;
import net.sourceforge.stripes.action.FileBean;

public class SendMessage {
    private static Logger log = Logger.getLogger(SendMessage.class);
    private MimeMessage mime;
    private Transport tpt;
    private Address[] recipients;
    private boolean success;
    private JwmaSession jession;
    
    public SendMessage(JwmaSession j){
        success = false;
        jession = j;
        mime = new MimeMessage(jession.mailSession);
        //FIXME: add smtps?
        //FIXME: might we want to auth to a non-local smtp host?
        try{
            tpt = jession.mailSession.getTransport("smtp");
        }
        catch(javax.mail.NoSuchProviderException ex){}
    }
    
    public boolean createMessage(String from, String to, String cc, String bcc, String subject){
        try{
            mime.setHeader("Reply-To", from); 
            mime.setHeader("From", from);
            Address[] TO = getRecipients(to);
            mime.addRecipients(RecipientType.TO, TO);
            final int T = TO.length;
            recipients = new Address[T];
            System.arraycopy(TO, 0, recipients, 0, TO.length);
            
            int C = 0;
            if(cc !=null && !cc.equals("")){
                Address[] CC = getRecipients(cc);
                mime.addRecipients(RecipientType.CC, CC);
                C = CC.length;
                recipients = new Address[T+C];
                System.arraycopy(TO, 0, recipients, 0, T);
                System.arraycopy(CC, 0, recipients, T, C);
            }

            int B = 0;
            if(bcc !=null && !bcc.equals("")){
                Address[] BCC = getRecipients(bcc);
                mime.addRecipients(RecipientType.BCC, BCC);
                B = BCC.length;
                Address[] temp = recipients;
                recipients = new Address[T+C+B];
                System.arraycopy(temp, 0, recipients, 0, T+C);
                System.arraycopy(BCC, 0, recipients, T+C , B);
            }
            mime.setSubject(subject);
            mime.setHeader("X-Mailer", "jwma");
            success = true;
        }
        catch(Exception ex){}
        return success;
    }
    
    public void writeSinglepart(String body){
        try{
            mime.setContent(body, "text/plain");
            mime.saveChanges();
        }
        catch(Exception ex){}
    }
    
    public void writeHtml(String body){
        try{   
            mime.setContent(constructMultipart(body));
            mime.saveChanges();
        }
        catch(Exception ex){}
    }
    
    public void writeAttachment(String body, List<FileBean> list, boolean richText){
        try{
            Multipart multipart = null;
            if(richText){
                multipart = constructMultipart(body);
            }
            else{
                BodyPart text = new MimeBodyPart();
                text.setContent(body, "text/plain");
                multipart = new MimeMultipart();
                multipart.addBodyPart(text);
            }  
            for(FileBean fb:list){
                if(fb != null){
                    String fileName = fb.getFileName();
                    fb.save(new File(fileName));
                    MimeBodyPart attachment = new MimeBodyPart();  
                    DataSource source = new FileDataSource(fileName);  
                    attachment.setDataHandler(new DataHandler(source));  
                    attachment.setFileName(fileName);  
                    multipart.addBodyPart(attachment);             
                    mime.setContent(multipart);
                    mime.saveChanges();
                }
            }
        }
        catch(Exception ex){log.debug("multipart"+ex);}
    }
    
    public void writeForward(String body, Multipart forward, boolean richText){
        try{
            if(richText){  
                Source source = new Source(body);         
                BodyPart text = new MimeBodyPart();
                text.setContent(source.getRenderer().toString(), "text/plain");
                BodyPart html = new MimeBodyPart();
                html.setContent(body, "text/html");
                forward.addBodyPart(text, 0);
                forward.addBodyPart(html, 1);
            }
            else{
                BodyPart plain = new MimeBodyPart();  
                plain.setText(body);
                forward.addBodyPart(plain, 0);
            }
            mime.setContent(forward);
            mime.saveChanges();
        }
        catch(Exception ex){log.debug("forward"+ex);}
    }
    
     public boolean send(){
        //FIXME: this assumes a local smtp server, we might want to authenticate elsewhere?
        success = false;
        try{
            tpt.connect();
            tpt.sendMessage(mime, recipients);          
            tpt.close();
            success = true;
        }
        catch(Exception ex){log.debug("send message FAILED " + ex);}
        return success;
     }
     
    public void archiveMail(String s){
        try{
            Folder f = jession.store.getFolder(s);
            f.open(Folder.READ_WRITE);
            Message[] m = new Message[1];
            m[0] = mime;
            f.appendMessages(m);
            f.close(false);
        }
        catch(Exception ex){}
    }
    
    private Address[] getRecipients(String s){
        /*first deal with the likes of this - "Bloggs, Freddie F"<fred@bloggs.co.uk>
         *we need to temporarily replace that comma inside the preamble*/
        Pattern p = Pattern.compile("(\\\".*)(,)(.*\\\"<.*>)");
        Matcher m = p.matcher(s);
        while(m.find()){
            s = m.group(1) + "|" + m.group(3);
        }
        /*because we also have to deal with this - fred@bloggs.co.uk, jane@bloggs.co.uk*/ 
        String[]rcp =s.split(",");
        Address a;
        int i = 0;
        Address[] adrs = new Address[rcp.length];
        for(String st:rcp){
            try{
                /*put any replaced comma back*/
                st = st.replace("|", ",");
                a = new InternetAddress(st);
                adrs[i] = a;
                i++;
            }
            catch(Exception ex){log.debug(st + " " + ex);}               
        }
        return adrs;
    }
    
    private Multipart constructMultipart(String body){        
        Multipart multipart = null;
        try{
            Source source = new Source(body);
            BodyPart text = new MimeBodyPart();
            text.setContent(source.getRenderer().toString(), "text/plain");
            BodyPart html = new MimeBodyPart();
            html.setContent(body, "text/html");
            multipart = new MimeMultipart();
            multipart.addBodyPart(text);  
            multipart.addBodyPart(html);
        }
        catch(Exception ex){}
        return multipart;
    }

}
