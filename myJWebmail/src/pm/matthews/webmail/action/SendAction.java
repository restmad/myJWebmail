package pm.matthews.webmail.action;

import java.util.List;
import javax.mail.Multipart;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import pm.matthews.webmail.SendMessage;

public class SendAction  extends BaseAction{
    private static Logger log = Logger.getLogger(SendAction.class);
    private String from, to, cc, bcc, subject, body;
    private boolean autoSign;
    private List<FileBean> attachment;
    private Multipart forward;
    private SendMessage sm;
    private boolean msgStatus, richText;
    
    public Resolution sendMail(){
        jession = getSessionValue("jession");
        if(body == null){body = "";}
        messageFrom();
        sm = new SendMessage(jession);
        msgStatus = false;
        msgStatus = writeMessage();
        if(msgStatus){msgStatus = sm.send();}
        if(msgStatus){
            log.info("message SENT to "+to+" cc'd to "+cc+" bcc'd to "+bcc);
        }
        else{
            log.info("FAILED to send message");
            LocalizableError err = new LocalizableError("sendmessage.failed");
            getContext().getMessages().add(err);
        }
        if(!jession.sent.equals("")){
            sm.archiveMail(jession.sent);
        }
        String view = "/jsp/mobile/folder.jsp";
        if(!jession.mobile){
            updateMailSession();
            view = "/jsp/folder.jsp";
        }
        return new ForwardResolution(view);
    }
    
    @DefaultHandler
    public Resolution saveDraft(){
        jession = getSessionValue("jession");
        String view = "/jsp/folder.jsp";
        //FIXME:see mobile/sendmail.jsp
        if(jession.mobile){           
            sendMail();
            view = "/jsp/mobile/folder.jsp";
        }
        else{
            updateMailSession();
            if(jession.draft.equals("")){
                log.debug("no Draft folder configured - can't SAVE");
                LocalizableError err = new LocalizableError("savemessage.nodraftfolder");
                getContext().getMessages().add(err);
            }
            else{
                String[] message = new String[6];
                if(body == null){body = "";}
                if(to == null){to = "";}
                sm = new SendMessage(jession);
                writeMessage();
                sm.archiveMail(jession.draft);        
            }
        }
        return new ForwardResolution(view);
    }
    
    private boolean writeMessage(){
        msgStatus = sm.createMessage(from, to, cc, bcc, subject);
        forward = getSessionValue("forward");
        if(msgStatus){
            if(attachment == null && forward == null){
                if(!richText){
                    sm.writeSinglepart(body);
                }
                else{
                    sm.writeHtml(body);
                }
            }
            else if(attachment != null){
                sm.writeAttachment(body, attachment, richText);
            }
            else{
                sm.writeForward(body, forward, richText);
                removeSessionValue("forward");
            }
        }
        return msgStatus;
    }
    
    private void messageFrom(){
        String cr = "\n";
        String ob = "&lt;";
        String cb = "&gt;";
        int sub = 4;
        if(jession.mobile){
            ob = "<";
            cb = ">";
            sub = 1;
        }
        from = from.replace(cb, "");
        if(autoSign){
            String line = "\n\n--\n";
            if(richText){
                cr = "<br>";
                line = "<br><br>--<br>";
            }
            body =  body + line + from.replace(ob, cr);
        }
        from = from.substring(from.indexOf(ob) + sub);
    }

    
    public List<FileBean> getAttachment(){return attachment;}
    public void setFrom(String f){from = f.trim();}
    @Validate(required=true, on={"sendMail", "saveDraft"})
    public void setTo(String t){to = t.trim();}
    public void setCc(String c){cc = c.trim();} 
    public void setBcc(String b){bcc = b.trim();}
    @Validate(required=true)
    public void setSubject(String s){subject = s;} 
    public void setBody(String b){body = b;}
    public void setAttachment(List<FileBean> att){attachment = att;}
    public void setForward(Multipart mp){forward = mp;}
    public void setRichText(boolean rt){richText = rt;}
    public void setAutoSign(boolean autoSign){
        jession = getSessionValue("jession");
        this.autoSign = Boolean.valueOf(jession.autoSign) != autoSign;
    } 
}
