package pm.matthews.webmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;
import pm.matthews.webmail.domain.User;

public class SerialPref implements Preference{
    private static Logger log = Logger.getLogger(Preference.class);
    private String username, path, language;
    private User user;
    
    public SerialPref(String p, String u, String l){
        username = u;
        path = p + File.separator + "data" + File.separator + username + ".conf";
        File f = new File(path);
        if(!f.exists()){
            language = l;
            writeDefaultUserPref();
        }
    }    
    public SerialPref(String p, String u){
        username = u;
        this.path = p + File.separator + "data" + File.separator + username + ".conf";
        user = readUserPrefs(path);
    }
    
    @Override
    public void setUpdates(String l,boolean as,boolean aq,boolean ae,String s,String d,String m,int p){
        user.setLanguage(l);
        user.setAutoSign(as);
        user.setAutoQuote(aq);
        user.setAutoEmpty(ae);
        user.setSent(s);
        user.setDraft(d);
        user.setMailId(m);
        user.setProcessor(p);
    }
    @Override
    public void setSortOrder(int order){
        user.setSortOrder(order);
    }
    @Override
    public String[] getUserPref(){
        String[] prefs = new String[10];
        user = readUserPrefs(path);
        prefs[0] = user.getUsername();
        prefs[1] = user.getLanguage();
        prefs[2] = ""+user.getSortOrder();
        prefs[3] = ""+user.getAutoEmpty();
        prefs[4] = ""+user.getAutoQuote();
        prefs[5] = ""+user.getAutoSign();
        prefs[6] = user.getSent();
        prefs[7] = user.getDraft();
        prefs[8] = user.getMailId();
        prefs[9] = ""+user.getProcessor();
        return prefs;
    }
    @Override
    public void writeUserPref(){
        try{
            FileOutputStream fout =new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(user);
            out.close();
            fout.close();         
        }
        catch(Exception ex){log.debug(ex);}
    }
    @Override  
    public void shutdown(){}
    
    private void writeDefaultUserPref(){
        log.info("TRYING to write default preferences for " + username);
        user = new User();
        user.setUsername(username);
        user.setLanguage(language);
        user.setSortOrder(0);
        user.setAutoEmpty(true);
        user.setAutoQuote(false);       
        user.setAutoSign(false);
        user.setSent("");
        user.setDraft("");
        	if(username.contains("@")){
		user.setMailId("JWMA User<" + username + ">");
	}
        else{
		user.setMailId("JWMA User<" + username + "@localhost>");
	}
        user.setProcessor(1);
        writeUserPref();
    }
    
    private User readUserPrefs(String path){
        user = null;
        try{
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fin);
            user = (User)in.readObject();
            in.close();
            fin.close();
        }
        catch(Exception ex){log.debug(ex);}
        return user;
    }
}
