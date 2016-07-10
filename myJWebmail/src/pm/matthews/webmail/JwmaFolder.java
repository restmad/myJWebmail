
package pm.matthews.webmail;

import com.sun.mail.imap.protocol.FLAGS;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.BodyTerm;
import javax.mail.search.SearchTerm;
import org.apache.log4j.Logger;

public class JwmaFolder {
    private static Logger log = Logger.getLogger(JwmaFolder.class);
    private Folder folder;
    private String[][] messageList;
    private Store store;
    
    public JwmaFolder(Store s, String f){
        store = s;
        try{
            folder = store.getFolder(f);
        }
        catch(MessagingException mex){log.debug("kwaaaaaaaaaa");}
    }
    
    public void emptyTrash(){
        try{
            folder.open(Folder.READ_WRITE);
            Message[] msg = folder.getMessages();
            for(Message m:msg){
                m.setFlag(FLAGS.Flag.DELETED, true);
                m.isExpunged(); 
            }
            folder.close(true);
        }
        catch(Exception ex){log.debug("failed to EMPTY trash " + ex);}
    }
    
    /*this is a real delete 
     *it's only called on messages in the trash folder
     */
    public void deleteMessage(List<String> delete){
        try{
            folder.open(Folder.READ_WRITE);
            int i;
            //FIXME: delete backwards through the list is safer?
            for(String s:delete){
                i = Integer.parseInt(s);
                Message m = folder.getMessage(i);
                m.setFlag(FLAGS.Flag.DELETED, true);
                log.info("DELETING message " + i);
            }
            folder.close(true);
        }
        catch(Exception ex ){log.debug("FAIL in deleteMessage() "+ex);}
    }
    
    public void moveMessage(List<String> move, String destination){
        Message[] msg = new Message[move.size()];
        try{
            Folder target = store.getFolder(destination);
            folder.open(Folder.READ_WRITE);
            target.open(Folder.READ_WRITE);
            int i = 0 ; int j;
            //FIXME: should we work backwards through the list?
            for(String s:move){
                j = Integer.parseInt(s);
                msg[i] = folder.getMessage(j);
                log.info("MOVING message "+s+" from "+folder+" to "+target);
                i++;
            }
            folder.copyMessages(msg, target);
            target.close(true);
            for(Message m:msg){
               m.setFlag(FLAGS.Flag.DELETED, true); 
            }
            folder.close(true);    
        }
        catch(Exception ex){log.debug("FAIL in moveMessage() "+ex);}
    }
    
    public String[][] messageList(boolean writeLog, boolean sentFolder){
       try{
            folder.open(Folder.READ_ONLY);
            Message[] mList = folder.getMessages();
            messageList = new String[mList.length][7];
            if(writeLog){
                log.info("listing " + mList.length + " MESSAGES");
            }
            int i = 0;
            for(Message m:mList){
                messageList[i][0] = ""+ m.getMessageNumber();
                Flags f = m.getFlags();
                Flags.Flag[] sf = f.getSystemFlags();
                String flag="";
                String seen ="N";
                for(Flags.Flag ff:sf){
                    if(ff == Flags.Flag.ANSWERED){
                        flag = flag + "A";
                    }
                    if(ff == Flags.Flag.SEEN){
                        seen = "R";
                    }
                }
                messageList[i][1] = flag + seen;
                messageList[i][2] = checkForAttachment(m);              
                messageList[i][3] = "";
                String head = "From";
                if(sentFolder){head = "To";}
                try{
                    String[]header = m.getHeader(head);
                    String from = header[0].replace("<", "&lt;").replace(">", "&gt;").replaceAll("\"", "&quot;");
                    if(from.contains("utf-8")){
                        from = from.replace("=?","").replace("?=","");
                        from = from.substring(from.lastIndexOf("?") + 1);
                        from = from.replaceAll("=20", " ");
                        if(from.contains("&quot;")){from = "&quot;" + from;}
                    }
                    messageList[i][3] = from;      
                }
                catch(NullPointerException ex){}
                String subject = m.getSubject();
                if(subject!=null && !subject.equals("")){
                    subject = subject.replaceAll("\\p{Cc}", "");//remove any control characters
                    messageList[i][4] = subject;
                }
                else{messageList[i][4] = "no subject";}
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh.mm");
                messageList[i][5] = formatter.format(m.getReceivedDate());
                int s = m.getSize();
                messageList[i][6] = (s < 1024)?""+s+" bytes":""+(s/1024)+"."+(s%1024)+" kB";
                i++;
            }
            folder.close(false);
        }
        catch(MessagingException mex){log.debug("kwaaaaaaaaaa");}
        return messageList;
    }
    
    public String filteredList(String search){
        SearchTerm term = new BodyTerm(search);
        StringBuilder sb = new StringBuilder(":");
        String s;
        try{
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.search(term);
            for(Message m:messages){
                s = m.getMessageNumber()+":";
                sb.append(s);
                log.info("FOUND "+search+" in "+m.getMessageNumber()+" "+InternetAddress.toString(m.getFrom())+" "+m.getSubject()+" "+ m.getSentDate());
            }
            folder.close(false);
        }
        catch(Exception ex){log.error("kwaaa" + ex);}
        search = sb.toString();
        return search;
    }
    
    public void moveFolder(Folder old){
        try{         
            folder.create(Folder.HOLDS_MESSAGES);
            folder.open(Folder.READ_WRITE);
            old.open(Folder.READ_ONLY);
            Message[] msg = old.getMessages();
            old.copyMessages(msg, folder);
            old.close(false);
            old.delete(true);
            folder.close(false);      
        }
        catch(Exception ex){log.debug("kwaaaa"+ex);} 
    }
    
    private String checkForAttachment(Message m){
        String attachment = ""; 
        try{       
            if(m.getContentType().toLowerCase().contains("multipart")){
                Object o = m.getContent();
                Multipart mp = (Multipart)o;
                for (int i = 0; i < mp.getCount(); i++) {
                    if(mp.getBodyPart(i).getFileName() != null){                             
                        attachment = " *";
                        break;
                    }
                    else if(mp.getBodyPart(i).getContentType().toLowerCase().contains("multipart")){
                        Multipart mp2 = (Multipart)mp.getBodyPart(i).getContent();
                        for (int j = 0; j < mp2.getCount(); j++) {
                            if(mp2.getBodyPart(j).getFileName() != null){                             
                                attachment = " *";
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch(Exception ex){}
        return attachment;
     }
}
