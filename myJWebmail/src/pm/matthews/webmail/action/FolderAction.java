
package pm.matthews.webmail.action;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.mail.Folder;
import javax.mail.Store;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.HtmlUtil;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.apache.log4j.Logger;
import pm.matthews.webmail.JwmaFolder;
import pm.matthews.webmail.Preference;
import pm.matthews.webmail.SerialPref;

public class FolderAction extends BaseAction implements ValidationErrorHandler{
    private static Logger log = Logger.getLogger(FolderAction.class);
    private String folder,targetFolder,storename,sort,searchkey,destination, parent;
    private String[][] messageList;
    private JwmaFolder jFolder;
    private int newStore, sortOrder;
    private String[] options;
    private boolean sent;
    public String  moveto;
    private List<String> selection, selected;
    private Preference preference;
    
    public Resolution handleValidationErrors(ValidationErrors errors){
        jession = getSessionValue("jession");
        if(jession.mobile){
            return new RedirectResolution(BaseAction.class);
        }
        else{
            if(errors.containsKey("searchkey")||errors.containsKey("selected")){
                Store store = jession.store;
                log.info("OPENING folder " + folder);
                jFolder = new JwmaFolder(store, folder);
                sent = folder.equals(jession.sent);
                prepareMessageList(true, sent);
            }
            return null;
        }
    } 
    
    public Resolution logout(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        if(Boolean.valueOf(jession.autoEmpty)){
           JwmaFolder trash = new JwmaFolder(store, jession.trashName);
           trash.emptyTrash();
        }
        if(getSessionValue("datastore") != null){
            /*close database connection*/
            Preference p = getPreference();
            p.shutdown();
        }
        setSessionValue("jession", null);  
        getContext().getRequest().getSession().invalidate();
        return new ForwardResolution("/jsp/logout.jsp");
    }
    
    @DefaultHandler
    public Resolution listMessages(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        jFolder = new JwmaFolder(store, folder);
        boolean writeLog = true;
        String header = getContext().getRequest().getHeader("X-Requested-With" );
        if (header != null && header.equalsIgnoreCase("XMLHttpRequest" )){
            writeLog = false;
        }
        else{
            log.info("OPENING folder " + folder);        
        }
        sent = folder.equals(jession.sent);
        prepareMessageList(writeLog, sent);
        String view = "/jsp/mobile/messagelist.jsp";
        if(!jession.mobile){
            updateMailSession();
            view = "/jsp/messagelist.jsp";
        } 
        return new ForwardResolution(view);
    }
    
    public Resolution deleteFolder(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        for(String s:selection){
            try{
                Folder f = store.getFolder(s);
                if(!f.getName().equals(jession.draft)&&!f.getName().equals(jession.sent)&&!f.getName().equals(jession.trash)){
                    f.delete(true);
                    log.info("folder " + s + " DELETED");
                }
                else{log.error(s + " is a SYSTEM folder");}
            }
            catch(Exception ex ){log.debug("kwaaa" +ex);}
        }
        jession.getMixedStoreList();
        return new RedirectResolution(BaseAction.class);
    }
    
    public Resolution moveFolder(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        try{
          for(String s:selection){
            char sep = store.getDefaultFolder().getSeparator();
            String newName = targetFolder + sep + s;
            jFolder = new JwmaFolder(store, newName);
            Folder old = store.getFolder(s);
            LocalizableError err;
            //must be a mailbox as we are not going to nest a folder
            if(old.getType() == 1){
                Folder f = store.getFolder(newName);
                if(f.exists()){//the nested mailbox must not exist
                    err = new LocalizableError("storename.invalid");
                    getContext().getMessages().add(err);
                }
                else{jFolder.moveFolder(old);}
            }
            else{
                err = new LocalizableError("folder.embedded");
                getContext().getMessages().add(err);
            }
          }
        }
        catch(Exception ex){log.debug("kwaaaa"+ex);}           
        jession.getMixedStoreList();
        return new RedirectResolution(BaseAction.class);
    }
    
    public Resolution createFolder(){
        jession = getSessionValue("jession");
        if(storename.toLowerCase().equals("trash")||storename.toUpperCase().equals("INBOX")){
            LocalizableError err = new LocalizableError("storename.invalid");
            getContext().getMessages().add(err);
        }  
        else{
	    Store store = jession.store;
            try{
                LocalizableError err;
                Folder f = store.getFolder(storename);
                if(f.exists()){
                    err = new LocalizableError("storename.invalid");
                    getContext().getMessages().add(err);
                }
                 else if(storename.contains("/")){
                    //a new folder must not have the same name as a top level mailbox
                    Folder fldr = store.getFolder(storename.substring(0, storename.indexOf("/")));
                    if(fldr.exists() && fldr.getType() == 1){
                        err = new LocalizableError("storename.invalid");
                        getContext().getMessages().add(err);
                    }
                    //don't create an embedded folder
                    if(newStore == 2){
                        err = new LocalizableError("folder.embedded");
                        getContext().getMessages().add(err);
                    }   
                    else{
                        f.create(newStore);
                        log.info("CREATED new nested mailbox " + storename);
                    }
                }
                else{
                    f.create(newStore);
                    log.info("CREATED new type " + newStore + " mailbox " + storename);
                }
            }
            catch(Exception ex){log.debug(""+newStore + ex);}
        }
	jession.getMixedStoreList();
        return new RedirectResolution(BaseAction.class);
    }
    
    public Resolution searchWhat(){
        jession = getSessionValue("jession");
        updateMailSession();
        Store store = jession.store;
        log.info("SEARCHING folder " + folder + " for  " + searchkey);
        jFolder = new JwmaFolder(store, folder);
        sent = folder.equals(jession.sent);
        prepareMessageList(true, sent);
        
        return new ForwardResolution("/jsp/messagelist.jsp");
    }
    
    public Resolution deleteMessage(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        jFolder = new JwmaFolder(store, folder);
        sent = folder.equals(jession.sent);
        prepareMessageList(true, sent);
        if(!folder.equals(jession.trashName)){
           jFolder.moveMessage(selected, jession.trashName);
        }
        else{
           jFolder.deleteMessage(selected); 
        }
        return new RedirectResolution(FolderAction.class).addParameter("folder", folder).addParameter("parent", parent);
    }
    
    public Resolution moveMessage(){
        jession = getSessionValue("jession");
        Store store = jession.store;
        jFolder = new JwmaFolder(store, folder);
        jFolder.moveMessage(selected, destination);
        prepareMessageList(true, false);
        return new RedirectResolution(FolderAction.class).addParameter("folder", folder).addParameter("parent", parent);
    }
       
    private Preference getPreference(){
        if(getSessionValue("datastore") == null){
            preference = new SerialPref((String)getSessionValue("home"), jession.username);
        }
        else{
            preference = getSessionValue("datastore");
        }
        return preference;
    }
    
    public void setFolder(String f){folder = f;}
    @Validate(required=true, on="moveFolder")
    public void setTargetFolder(String tf){targetFolder = tf;}
    @Validate(required=true, on="createFolder", label="mail store")
    public void setStorename(String sn){storename = HtmlUtil.encode(sn);}
    @Validate(required=true, on="searchWhat")
    public void setSearchkey(String sk){searchkey = HtmlUtil.encode(sk);}
    public void setDestination(String d){destination = d;}   
    @Validate(required=true, on={"moveFolder","deleteFolder"})
    public void setSelection(List<String> s){selection = s;}
    @Validate(required=true, on={"moveMessage","deleteMessage"})
    public void setSelected(List<String> s){selected = s;}
    public void setNewStore(int ns){newStore = ns + 1;} 
    public void setParent(String p){parent = p;}
    
    public void setSortOrder(int sortOrder){
        this.sortOrder = sortOrder;
        log.info("SET order to " + sortOrder);
        jession = getSessionValue("jession");
        jession.sortOrder = sortOrder;
        setSessionValue("jession", jession);
        Preference p = getPreference();
        p.setSortOrder(sortOrder);
        p.writeUserPref();
    }
    
    public String getFolder(){return folder;}
    public String[][] getMessageList(){return messageList;}
    public String getParent(){return parent;}
    public String getSort(){
        sort = "" + jession.sortOrder;
        return sort;
    }
    
    private String[][] prepareMessageList(boolean log, boolean sent){
        messageList = jFolder.messageList(log, sent);
        if(searchkey != null){
           String filter = jFolder.filteredList(searchkey);
           ArrayList<String[]> filteredMessages = new ArrayList<String[]>();
           for(String[] s:messageList){
               if(filter.contains(":"+s[0]+":")){               
                   filteredMessages.add(s);
               }
           }
           messageList = new String[filteredMessages.size()][7];
           int i = 0;
           for(String[] s:filteredMessages){
               messageList[i] = s;
               i++;
           }
        }
        sortOrder = jession.sortOrder;
        if(sortOrder != 0){
            Arrays.sort(messageList, new Comparator<String[]>() {
                @Override
                public int compare(final String[] first, final String[] second){
                    String a = ""; String b = "";
                    if(sortOrder == 1){//recent first
                        b = first[0].length()==1?"0"+first[0]:first[0];
                        b = b.length()==2?"0"+b:b;
                        a = second[0].length()==1?"0"+second[0]:second[0];
                        a = a.length()==2?"0"+a:a;
                    }
                    else{//lexographical
                       a = first[3];
                       b = second[3];
                    }
                    return a.compareTo(b);
                }
            });
        }
        return messageList;
    }
    
    public String[] getOptions(){
        String o = "messages.sort.oldestfirst";
        String r = "messages.sort.recentfirst";
        String l = "messages.sort.wholexographical";
        options = new String[]{o, r, l};       
        return options;
    }
}
