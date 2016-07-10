package pm.matthews.webmail;
import net.htmlparser.jericho.Source;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

public class DisplayMessage {

    private static Logger log = Logger.getLogger(DisplayMessage.class);
    private Message message;
    private Store store;
    private Folder folder;
    private String[] msg;
    private String folderName;
    private int messageNumber, style;
    private boolean reply, doesExist, removeAttachment;
    private StringBuilder body;
    private boolean text;
    
    public DisplayMessage(Store s, String f, int m, int st) {
        store = s;
        folderName = f;
        messageNumber = m;
        style = st;
        text = false;
        try {
            folder = store.getFolder(f);
            folder.open(Folder.READ_WRITE);
            message = folder.getMessage(messageNumber);
            doesExist = true;
        } 
        catch (Exception ex) {
            doesExist = false;
        }
        removeAttachment = false;
    }

    public boolean doesExist() {
        return doesExist;
    }

    public String[] replyMessage(boolean all) {
        reply = true;
        log.info("Replying to " + messageNumber + " in " + folderName);
        msg = new String[5];
        msg[1] = "";
        msg[2] = "";
        try {
            messageBody();
            msg[4] = ">" + body.toString().replaceAll("<br>", "\n>").replaceAll("&gt;", ">").replaceAll("&lt;", ">").replaceAll( "&quot;", "\"");
            message = message.reply(all);
            msg[0] = getTo();
            msg[3] = getSubject();
            
        } catch (Exception ex) {
            log.debug("reply subject " + ex);
        }
        return msg;
    }

    public String[] forwardMessage(String forward, boolean a) {
        removeAttachment = !a;
        body = new StringBuilder();
        body.append("\n\nOriginal message attached below:\n\n\n");
        log.info("Forwarding " + messageNumber + " in " + folderName + " to " + forward);
        msg = new String[5];
        msg[0] = forward;
        msg[1] = "";
        msg[2] = "";
        String subject = "";
        try {
            subject = getSubject();
        } catch (Exception ex) {
            log.debug("foward subject " + ex);
        }
        msg[3] = "Fwd: " + subject;
        msg[4] = body.toString();
        return msg;
    }

    /*forwarding mail - prepare the original for attaching to a new Message*/
    public Multipart originalMessage() {
        Multipart mp = null;
        String disposition;
        Part p;
        try {
            String ct = message.getContentType();
            log.info("content TYPE is " + ct);
            if (ct.toLowerCase().startsWith("text/")) {
                mp = new MimeMultipart();
                BodyPart text = new MimeBodyPart();
                text.setContent(message.getContent(), message.getContentType());
                mp.addBodyPart(text);
            }
            else {
                Object bd = message.getContent();
                mp = (Multipart) bd;
                if(removeAttachment){
                    for (int i = 0; i < mp.getCount(); i++) {
                        p = mp.getBodyPart(i);
                        disposition = p.getDisposition();
                        if (disposition != null && disposition.toLowerCase().contains("attachment")) {                          
                            log.info("removing ATTACHMENT");
                            mp.removeBodyPart(i);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("tried to strip attachment" + ex);
        }
        return mp;
    }

    public String[] messageDetails() {
        log.info("READING message " + messageNumber + " in " + folderName);
        try {
            msg = new String[7];
            msg[0] = getMessageNumber();
            msg[1] = getDate();
            msg[2] = getFrom();
            msg[3] = getTo();
            msg[4] = getSubject();
            messageBody();
            msg[5] = body.toString();
            Enumeration en = message.getAllHeaders();
            StringBuilder header = new StringBuilder();
            Header h;
            String s;
            while (en.hasMoreElements()) {
                h = (Header) en.nextElement();
                String name = h.getName().replace("<", "&lt;").replace(">", "&gt;").replaceAll("\"", "&quot;");
                String value = h.getValue().replace("<", "&lt;").replace(">", "&gt;").replaceAll("\"", "&quot;");
                s = "<b>" + name + "</b> " + value + "<br>";
                header.append(s);
            }
            msg[6] = header.toString();
            if (folderName.equals("INBOX")) {
                message.setFlag(Flags.Flag.SEEN, true);
            }
            folder.close(false);
        } catch (Exception ex) {
            log.debug(ex);
        }
        return msg;
    }

    private String getMessageNumber() {
        return "" + message.getMessageNumber();
    }

    private String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = "";
        try {
            date = formatter.format(message.getReceivedDate());
        } catch (MessagingException mex) {
        }
        return date;
    }

    private String getFrom() throws MessagingException {
        String from = "";
        try {
            String[] header = message.getHeader("From");
            from = header[0].replace("<", "&lt;").replace(">", "&gt;").replaceAll("\"", "&quot;");
            if (from.contains("utf-8")) {
                from = utf8(from);
            }
        } catch (NullPointerException ex) {
        }
        return from;
    }

    private String getTo() throws MessagingException {
        String to = "";
        try {
            String[] header = message.getHeader("To");
            for (String t : header) {
                if (reply) {
                    to = to + t.replace("&lt;", "<").replace("&gt;", ">").replaceAll("&quot;", "");
                    if (to.contains("utf-8")) {
                        to = utf8(to);
                    }           
                } 
                else {
                    to = to + t.replace("<", "&lt;").replace(">", "&gt;").replaceAll("\"", "&quot;") + " ";
                }
            }
        } 
        catch (NullPointerException ex) {}
        return to;
    }
    
    private String getSubject() throws MessagingException{
        String subject = "";
        if(message.getSubject() == null){
            subject = "";
        }
        else{
            //remove any spurious control characters (eg =00=00=00=27 as utf-8 aposthrope;
            subject = message.getSubject().replaceAll("\\p{Cc}", "");
        }
        return subject;
    }
    
    private String utf8(String header){
        header = header.replace("=?", "").replace("?=", "");
        header = header.substring(header.lastIndexOf("?") + 1);
        header = header.replaceAll("=20", " ");
        header = header.replaceAll("\\p{Cc}", "");
        if (header.contains("&quot;")) {
            header = "&quot;" + header;
        }
        return header;
    }
            
    private void messageBody() {
        body = new StringBuilder();
        try {
            Object bd = message.getContent();
            if (bd instanceof Multipart) {
                processMultipart((Multipart) bd);
            } else {
                processPart(message);
            }
        } catch (Exception ex) {
            log.debug("messagebody " + ex);
        }
    }

    private void processMultipart(Multipart mp) throws MessagingException {
        for (int i = 0; i < mp.getCount(); i++) {
            log.info("processing MULTIPART " + i);
            if (style == 0) {
                String s = "<br><b>Part </b>#<b>" + i + " (" + mp.getBodyPart(i).getContentType() + ")</b><br>";
                body.append(s);
            }
            processPart(mp.getBodyPart(i));
        }
    }

    private void processPart(Part p) {
        String st;
        try {
            String contentType = p.getContentType();
            if (contentType.toLowerCase().startsWith("text/plain")) {
                text = true;//we may want to remove the text/plain part
                st = p.getContent().toString();
                st = st.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
                st = st.replaceAll("\n", "<br>") + "<br>";
                body.append(st);
            } 
            else if(contentType.toLowerCase().startsWith("text/html") && p.getFileName()==null){
                /*we will try and display html if it's not an attached file*/
                if(style > 1){
                    displayHtml(p);
                }
                /*we don't want to show html, but there was no text/plain part*/
                else if(!text){
                    if (style == 0) {
                        body.append("<br><b>parsing html - no text/plain part</b><br><br>");
                    }   
                    st = p.getContent().toString();
                    Source source = new Source(st);
                    body.append(source.getRenderer().toString().replaceAll("\n", "<br>"));
                }
            } 
            else if(contentType.toLowerCase().startsWith("message/rfc822")) {
                displayRFC822(p);
            } 
            else{
                if(contentType.toLowerCase().startsWith("multipart/")){
                    processMultipart((Multipart)p.getContent());
                }
                /* looking for an in line image
                 * but we don't look for inline disposition since
                 * an attached pdf can be marked as inline
                 * and an  inline image may have attachment disposition*/
                else if(p.getContentType()!=null && p.getContentType().contains("image")){
                    if (style < 3) {
                        hiddenInlineImage(p);
                    }
                    /*try to display the in line image*/
                    else{
                       displayInlineImage(p); 
                    }
                    if(p.getFileName() != null){
                       displayDownloadLink(p); 
                    }
                } 
                /*non-image file attached - yet it may have been marked as inline (sigh)*/ 
                else if(p.getFileName() != null){
                    displayDownloadLink(p);
                }
                else{
                    log.debug("kwwwwaaaaaaaaaaaaaaaaaaa dunno what to do with this part");
                    if(style == 0){
                        String s = "<br>problem dealing with " + p.getDescription();
                        body.append(s);
                    }
                }
            }
        } 
        catch(Exception ex){
            log.debug(ex);
        }
    }
    
    private void displayHtml(Part p){
        try{
            String st = p.getContent().toString();
            st = cleanHtml(st);
            /*we will block the remote images and stylesheets unless we are crazy*/
            if(style != 4){
                String regex = "(?s)(?i)(<img|<link)(.*?)(src=.http://|src=.https://)(.*?)(/.*?>)";
                Pattern pattern = Pattern.compile(regex);
                Matcher m = pattern.matcher(st);
                if (m.find()) {
                    String replace = "" + m.group(1) + m.group(2) + m.group(3) + "localhost" + m.group(5);
                    st = st.replaceAll(regex, replace);
                }

            }                 
            /*discard any text/plain part*/
            if(text){
                body = new StringBuilder();
                text = false;
            }
            body.append(st);
        }
        catch(Exception ex){log.debug("problem displaying HTML" + ex);}
    }
    
    private void displayRFC822(Part p){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            p.writeTo(baos);
            String st = baos.toString();
            baos.close();
            st = st.substring(st.indexOf("Encoding: quoted-printable") + 26);
            st = st.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
            st = st.replaceAll("\n", "<br>") + "<br>";
            body.append(st);        
        }
        catch(Exception ex){log.debug("problem displaying RFC8222" + ex);}
    }
    
    private void hiddenInlineImage(Part p){
        try{
            String st = "Display image: ";
            st = st+"<a href='Message.jwma?showInlineImage=&folder="+folder+"&number="+messageNumber+"'>";
            st = st + p.getFileName() + "</a>&nbsp;&nbsp;";
            body.append(st);
        }
        catch(Exception ex){}
    }
    
    private void displayInlineImage(Part p){
        try{
            String image = "<img src='data:";
            String temp = p.getContentType().toString();
            int index = temp.indexOf(";");
            temp = temp.substring(0, index + 1);
            image = image + temp;
            temp = p.getContent().toString();
            index = temp.indexOf("Decoder");
            temp = temp.substring(18, index);
            image = image + temp.toLowerCase() + ",";
            log.info("starting to assemble inline IMAGE tag - " + image);
            int header = 0;
            Enumeration en = p.getAllHeaders();
            while(en.hasMoreElements()){
                log.info("need to remove "+ en.nextElement()+" from image data");
                header++;
            }       
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            p.writeTo(baos);                    
            String data = "";
            BufferedReader br = new BufferedReader(new StringReader(baos.toString()));
            baos.close();
            int remove = 0;
            while((temp = br.readLine()) != null) {
               if(remove < header){
                   if(temp.toLowerCase().startsWith("content")){
                       remove++;
                   }
                }
                else{
                    data = data + temp;
                }
            }
            image = image + data;
            image = image + "'/><br>";
            body.append(image);
        }
        catch(Exception ex){log.debug("problems displaying inline IMAGE" + ex);}
    }
    
    private void displayDownloadLink(Part p){
        try{
            int s = p.getSize();
            String st = (s < 1024) ? "" + s + " bytes" : "" + (s / 1024) + "." + (s % 1024) + " kB";
            String fileLink = "Download: ";
            fileLink = fileLink + "<a href='Message.jwma?downloadAttachment=&folder=" + folder + "&number=" + messageNumber + "&filename=" + p.getFileName() + "'>";
            fileLink = fileLink + p.getFileName() + "</a>(" + st + ")<br>";
            body.append(fileLink);
        }
        catch(Exception ex){log.debug("problems with download LINK for " + ex);}
    }

    public Part download(String filename) {
        Part p = null;
        boolean file = false;
        try{
            Multipart outer = (Multipart)message.getContent();
                for(int i = 0; i < outer.getCount(); i++){
                    p = outer.getBodyPart(i);
                    if (p.getContentType().toLowerCase().startsWith("multipart/")){
                        Multipart inner = (Multipart) p.getContent();
                        for (int j = 0; j < inner.getCount(); j++) {
                            p = inner.getBodyPart(j);
                            if (p.getFileName() != null && p.getFileName().equals(filename)) {
                                log.info("DOWNLOADING attachment from multipart " + p.getFileName());
                                file = true;
                                break;
                            }
                        }//end inner
                        if(file){break;}
                    }
                    if (p.getFileName() != null && p.getFileName().equals(filename)){
                        log.info("DOWNLOADING attachment " + p.getFileName());
                        file = true;
                        break;
                    }
                }//end outer
        } 
        catch (Exception ex) {
            log.debug("download fail :-( " + ex);
        }
        return p;
    }
    
    /*filter out annoying stuff that mungs our appearance*/
    private String cleanHtml(String html){
        //any line-height stuff must go
        html = html.replaceAll("line-height","");  
        html = html.replaceAll("width","");
        //remove any style code that will alter appearance of links
        String regex = "(?i)(a\\s*)(:|\\{)(?s)(.*?)(})";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(html);
        if(m.find()){
             html = html.replaceAll(regex, "");
         }
        //remove any css table styles
        regex = "^table|[\\s]table";
        pattern = Pattern.compile(regex);
        m = pattern.matcher(html);
        if(m.find()){
             html = html.replaceAll(regex, " t_able");
         }
        //don't allow the body tag to be hijacked
        html = html.replace("body", "bo_dy");
        //don't screw our internal page links
        html = html.replace("<base", "<b_ase");
        //don't realign everything
        html = html.replace("-align:", "-a_lign:");
        return html;
    }
}
