package pm.matthews.webmail.action;

import javax.mail.Folder;
import javax.mail.Store;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.log4j.Logger;

public class SubFolderAction  extends FolderAction{
    private static Logger log = Logger.getLogger(SubFolderAction.class);
    private String folder, parent;
    private Folder[] mixedStoreList;
    private Store store;
    
    @DefaultHandler
    public Resolution list(){
        jession = getSessionValue("jession");
        updateMailSession();
        parent = folder;
        store = jession.store;
        return new ForwardResolution("/jsp/folder.jsp");
    }
    
    @Override
    public Folder[] getMixedStoreList(){
      mixedStoreList = null;
      try{
          Folder f = store.getFolder(folder);
          mixedStoreList = f.list();
          log.info("OPENING type 2 folder " + f.getName());
      }
      catch(Exception ex){log.debug(ex);}
      return mixedStoreList;
    }
    
    public void setFolder(String f){folder = f;}
    public String getFolder(){return folder;}
    public String getParent(){return parent;}
    @Override
    public boolean type2(){return true;}   
}
