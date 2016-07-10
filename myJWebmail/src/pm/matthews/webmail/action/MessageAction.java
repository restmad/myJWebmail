
package pm.matthews.webmail.action;

import java.io.InputStream;
import java.io.OutputStream;
import javax.mail.Multipart;
import org.apache.log4j.Logger;
import javax.mail.Store;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import pm.matthews.webmail.DisplayMessage;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

public class MessageAction extends BaseAction implements ValidationErrorHandler{
    private static Logger log = Logger.getLogger(MessageAction.class);
    private String folder, next, fwdaddress, filename, parent;
    private String[] msg;
    private int number, messageNumber, processor;
    private boolean all, quote, attachment;
    private Multipart original;
    
    public Resolution handleValidationErrors(ValidationErrors errors){
        jession = getSessionValue("jession");
        Store store = jession.store;
        processor = jession.processor;
        DisplayMessage dm = new DisplayMessage(store, folder, number, processor);
        msg = dm.messageDetails();
        return null;
    }
    public Resolution cycleMessages(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        processor = jession.processor;
        DisplayMessage dm = new DisplayMessage(store, folder, messageNumber, processor);
        if(dm == null || !dm.doesExist()){
            return new RedirectResolution(FolderAction.class).addParameter("folder", folder).addParameter("parent", parent);
        }
        msg = dm.messageDetails();
        return new ForwardResolution("/jsp/readmessage.jsp");        
    }
    
    @DefaultHandler
    public Resolution displayMessage(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        processor = jession.processor;
        DisplayMessage dm = new DisplayMessage(store, folder, number, processor);
        String view = "/jsp/mobile/readmessage.jsp";
        msg = dm.messageDetails(); 
        if(!jession.mobile){
            updateMailSession(); 
            if(folder.equals(jession.draft)){
                String[] temp = new String[5];
                temp[0] = msg[3].replace("&lt;","<").replace("&gt;",">").replaceAll("&quot;","\"" );
                temp[1] = temp[2] = "";
                temp[3] = msg[4];
                temp[4] = msg[5].replaceAll("<br>", "\n").replaceAll("&gt;",">");
                msg = temp;
                view = "/jsp/sendmessage.jsp";
            }
            else{view = "/jsp/readmessage.jsp";}
        }
        return new ForwardResolution(view); 
    }
    
    public Resolution switchToHtml(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        processor = -1;
        DisplayMessage dm = new DisplayMessage(store, folder, number, 2);
        msg = dm.messageDetails();
        String view = "/jsp/readmessage.jsp";
        if(jession.mobile){view = "/jsp/mobile/readmessage.jsp";}
        return new ForwardResolution(view);
    }
    
    public Resolution showInlineImage(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        DisplayMessage dm = new DisplayMessage(store, folder, number, 4);
        msg = dm.messageDetails();
        if(folder.contains("/")){
            int i = folder.indexOf("/");
            parent = folder.substring(0, i);
        }
        String view = "/jsp/readmessage.jsp";
        if(jession.mobile){view = "/jsp/mobile/readmessage.jsp";}
        return new ForwardResolution(view);
    }
    
    public Resolution printMessage(){
        jession = getSessionValue("jession");
        Store store = jession.store;  
        msg = new DisplayMessage(store, folder, number, 1).messageDetails();
        return new ForwardResolution("/jsp/printmessage.jsp");
    }
    
    public Resolution reply(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        processor = jession.processor;
        msg = new DisplayMessage(store, folder, number, 1).replyMessage(all);
	if(!quote){
	     msg[4] = "";
	}
        String view = "/jsp/mobile/sendmessage.jsp";
        if(!jession.mobile){
            updateMailSession();
            view = "/jsp/sendmessage.jsp";
        }
        return new ForwardResolution(view);
    }
    
    public Resolution forward(){
        jession = getSessionValue("jession");
        Store store = jession.store; 
        processor = jession.processor;
        DisplayMessage dm = new DisplayMessage(store, folder, number, processor);
        msg = dm.forwardMessage(fwdaddress, attachment);
        setSessionValue("forward", dm.originalMessage());
        log.info("ready to FORWARD message");
        String view = "/jsp/sendmessage.jsp";
        if(jession.mobile){view = "/jsp/mobile/sendmessage.jsp";}
        return new ForwardResolution(view);
    }
    
    public Resolution downloadAttachment(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        processor = jession.processor;
        DisplayMessage dm = new DisplayMessage(store, folder, number, processor);
        final Part p = dm.download(filename);
        String type ="";
        try{
            type = new ContentType(p.getContentType()).getBaseType();
        }
        catch(Exception ex){}        
        return new StreamingResolution(type){
            protected void stream(HttpServletResponse resp) throws Exception{
                OutputStream output = resp.getOutputStream();
                InputStream input = p.getInputStream();
                byte[] buffer = new byte[4096];
                int byteRead;
                while((byteRead = input.read(buffer)) != -1){
                    output.write(buffer, 0, byteRead);
                }
            }

        }.setFilename(filename);  
    } 
        
    public void setFolder(String f){folder = f;}
    public void setParent(String p){parent = p;}
    public void setNext(String n){next = n;}
    public void setAll(boolean a){all = a;}
    @Validate(required=true, on="forward")
    public void setFwdaddress(String fwd){fwdaddress = fwd;}
    public void setFilename(String fn){filename = fn;}
    public void setAttachment(boolean att){attachment = att;} 
    public void setNumber(int nm){number = nm;}
    
    public void setMessageNumber(int mn){
        if(next != null && next.equals("next")){
            mn++;
        }
        else if(next != null && next.equals("prev")){
            mn--;
        }
        messageNumber  = mn;   
    }

    public void setQuote(boolean quote){
        jession = getSessionValue("jession");
        this.quote = Boolean.valueOf(jession.autoQuote) != quote;
    }
    
    public String getFolder(){return folder;}
    public String getParent(){return parent;}
    public String[] getMsg(){return msg;}
    public Multipart getOriginal(){return original;}
    public int getProcessor(){return processor;}
}
