package pm.matthews.webmail;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import org.apache.log4j.Logger;

public final class JwmaSession {
    private static Logger log = Logger.getLogger(JwmaSession.class);
    public Session mailSession;
    public Store store;
    public Folder inbox, trash;
    public String username,trashName,language,mailId,autoEmpty,autoSign,autoQuote,sent,draft;
    public int messages, unread, sortOrder, processor;
    public boolean trashEmpty, mobile;
    public Folder[] mixedStoreList;
    
    JwmaSession(Session ms, Store st, Preference p){
        mailSession = ms;
        store = st;
        getPreferences(p);
        try{
            inbox = store.getFolder("INBOX");
        }
        catch(Exception ex){log.debug("kwaaaaaa");}
        updateInbox();   
        createMailbox(sent);
        createMailbox(draft);
        getMixedStoreList();
        trashName = trash.getName();
        updateTrash();
    }
            
    private void createMailbox(String s){
        if(s.equals("")){return;}
        try{
            Folder f = store.getFolder(s);
            if(!f.exists()){
                f.create(1);
            }
        }
        catch(Exception ex){}
    }
   
    public void updateInbox(){
        try{
            inbox.open(Folder.READ_ONLY);
            messages = inbox.getMessageCount();
            unread = inbox.getUnreadMessageCount();          
            inbox.close(false);
        }
        catch(Exception mex){}
    }
    public void updateTrash(){
        try{
            trash.open(Folder.READ_ONLY);
            trashEmpty = trash.getMessageCount() == 0;
            trash.close(false);
        }
        catch(Exception mex){}    
    }
    
    public void getMixedStoreList(){
      createMailbox("Trash");//in case it does not exist already
      Folder[] folders;
      try{
        mixedStoreList = store.getDefaultFolder().list();
	folders = new Folder[mixedStoreList.length - 2];
        int j = 0;
        for(Folder f:mixedStoreList){
            if(f.getName().equals("Trash")){
                trash = f;
            }
            else if(!f.getName().equals("INBOX")){
                folders[j] = f;
                j++;
            }
        }
        mixedStoreList = folders;
      }
      catch(Exception ex){
            mailSession = null;
            log.debug(ex);
      }
    }
    
    public void getPreferences(Preference p){
        String[] prefs = p.getUserPref();
        username = prefs[0];
        language = prefs[1];
        /*the order in which messages are displayed
         * 0 = oldest first, 1 reverses this, 2 = lexographical
         */
        sortOrder = Integer.parseInt(prefs[2]);
        autoEmpty = prefs[3];
        autoSign = prefs[4];
        autoQuote = prefs[5];
        sent = prefs[6];
        draft = prefs[7];
        mailId = prefs[8];
        /* 0-4 represents message display style - geek, plain text etc*/
        processor = Integer.parseInt(prefs[9]);
    }
}

