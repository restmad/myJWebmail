package pm.matthews.webmail;


import org.apache.log4j.Logger;
import java.util.Properties;
import java.security.Security;
import javax.mail.Session;
import javax.mail.Store;

public class JwmaAuthenticator{
    private static Logger log = Logger.getLogger(JwmaAuthenticator.class);   
    private String username, host, protocol, pass;
    private int port;
    private Session mailSession;
    private Store store;
    
    public JwmaAuthenticator(String u, String p, String h, String pcol, String prt){
        username = u;
        pass = p;
        host = h;
        protocol = pcol;
        port = Integer.parseInt(prt);
    }
    
    public boolean connectServer(){
        boolean auth = false;
        try{
            if(protocol.equals("imap")){
                mailSession = Session.getInstance(System.getProperties());
                store = mailSession.getStore(protocol);
                store.connect(host, port, username, pass);
            }
            else if(protocol.equals("imaps")){
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                Properties props = new java.util.Properties();
                props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.imap.socketFactory.fallback", "false");
                props.setProperty("mail.imap.socketFactory.port", ""+port);
                mailSession = javax.mail.Session.getInstance(props);
                store = mailSession.getStore("imaps");
                store.connect(host, port, username, pass);
            }
            auth = true;
            log.info("OPENED " + protocol + " mailbox at "+ host + ":" + port);
            log.info("login for " + username + " SUCCESS");
        }
        catch(Exception ex){
            mailSession = null;
            log.warn("login FAILED for " + username);
        }
        return auth;
    }
    
    public JwmaSession createUserSession(Preference pref){
        return new JwmaSession(mailSession, store, pref);
    }
}

