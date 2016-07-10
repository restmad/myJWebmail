package pm.matthews.webmail.action;

import javax.mail.Store;
import javax.servlet.http.Cookie;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.log4j.Logger;
import pm.matthews.webmail.JwmaAuthenticator;
import pm.matthews.webmail.Preference;
import pm.matthews.webmail.SerialPref;
import pm.matthews.webmail.Datastore;
import pm.matthews.webmail.JwmaFolder;

public class LoginAction extends BaseAction{
    private String password, username, view;
    private JwmaAuthenticator auth;
    private static Logger log = Logger.getLogger(LoginAction.class);
    private boolean remember, mobile;
    
    @DefaultHandler
    public Resolution login(){
        String  host = getSessionValue("mail_host");
        String protocol = getSessionValue("access_protocol");
        String port = getSessionValue("port");     
        auth = new JwmaAuthenticator(username, password, host, protocol, port);
        boolean authenticated = auth.connectServer();
        String param = "";
        String value = "";
        if(authenticated){
            Preference pref = null;
            String language = getSessionValue("language");
            String persistence = getSessionValue("persistence");
            if(persistence.equals("serialize")){
                String home = getSessionValue("home");
                pref = new SerialPref(home, username, language);
            }
            else{
                pref = new Datastore(persistence, username, language);
                setSessionValue("datastore", pref);
            }
            jession = auth.createUserSession(pref);
            jession.mobile = mobile;
            setSessionValue("jession", jession);
            if(mobile){
                setCookie();
                param = "folder";
                value = "INBOX";
                jession.processor = 1;
                jession.sortOrder = 1;
                view = "Folder.jwma";
            }
            else{
                if(remember){
                    setCookie();
                }
                view = "/jsp/folder.jsp";
            }
        }
        else{
           log.warn("FAILED login from " + getContext().getRequest().getRemoteHost());
           view = "index.jsp";  
        }
	return new ForwardResolution(view).addParameter(param, value);
    }
    
    private void setCookie(){
        log.info("trying to set COOKIE for " + username );
        Cookie userCookie = new Cookie("USER", username);
        getContext().getResponse().addCookie(userCookie);
    }
          
    public void setUsername(String u){username = u;}
    public void setPassword(String p){password = p;}
    public void setRemember(boolean r){remember = r;}
    public void setMobile(boolean m){mobile = m;};
}
