package pm.matthews.webmail.action;

import javax.mail.Folder;
import javax.servlet.http.Cookie;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import javax.servlet.http.HttpSession;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import pm.matthews.webmail.JwmaSession;

public class BaseAction implements ActionBean{
    protected ActionBeanContext context;
    JwmaSession jession;
    protected String[] mailId, msg;
    protected String lastLogin;
    
    @DefaultHandler
    public Resolution main(){
        jession = getSessionValue("jession");
        String view = "/jsp/mobile/folder.jsp";
        if(!jession.mobile){        
            updateMailSession();
            view = "/jsp/folder.jsp";
        }
        return new ForwardResolution(view);       
    }
    
    //this acts as a keep alive for desktop interface
    public Resolution updateInbox(){
        jession = getSessionValue("jession");
        updateMailSession();
        String data = "" + getUnread() + "|" + getMessages();
        return new JavaScriptResolution(data);
    }
    
    public Resolution compose(){
        jession = getSessionValue("jession");
        updateMailSession();
        String view = "/jsp/mobile/sendmessage.jsp";
        if(!jession.mobile){
            updateMailSession();
            view = "/jsp/sendmessage.jsp";
        }
        msg = new String[5];
        return new ForwardResolution(view);       
    }
    
    protected void setSessionValue(String key, Object value){
        HttpSession session = getContext().getRequest().getSession();
        session.setAttribute(key, value);
    }
    
    protected <T> T getSessionValue(String key) {
        T  value = null;
        try{
            value = (T)context.getRequest().getSession().getAttribute(key);
        }
        catch(NullPointerException nex){}
        return value;
    }
    
    protected void removeSessionValue(String key){
        HttpSession session = getContext().getRequest().getSession();
        session.removeAttribute(key);
    }
    
    protected void updateMailSession(){
        jession.updateInbox();
        jession.updateTrash();
    }
    
    public boolean type2(){return false;}
    
    public String getUserLanguage(){
        jession = getSessionValue("jession");
        return jession.language;
    }
    public int getMessages(){return jession.messages;}
    public int getUnread(){return jession.unread;}
    public boolean getTrashEmpty(){return jession.trashEmpty;}
    public String getTrashName(){return jession.trashName;}
    public Folder[] getMixedStoreList(){return jession.mixedStoreList;}
    public String[] getMsg(){return msg;}
    public String getParent(){return null;}
    public String[] getMailId(){
         mailId = jession.mailId.split(",");
         return mailId;
     }
    
    public ActionBeanContext getContext(){return context;}
    public void setContext(ActionBeanContext c){context = c;}  
    
    public String getLastLogin(){
        return lastLogin;
    }   
    protected String setLoginName(){
        lastLogin = "";       
        try{
            Cookie[] jar = getContext().getRequest().getCookies();
            for(Cookie c:jar){
                if(c.getName().equals("USER")){
                    lastLogin = c.getValue();
                    //log.info("found login COOKIE for " + lastLogin);
                }
            }
        }
        catch(NullPointerException nex){}
        return lastLogin;
    }
}
