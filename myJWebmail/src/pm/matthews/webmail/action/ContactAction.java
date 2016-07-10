
package pm.matthews.webmail.action;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.HtmlUtil;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.apache.log4j.Logger;
import pm.matthews.webmail.ContactManager;
import pm.matthews.webmail.SerialContact;

public class ContactAction extends BaseAction implements ValidationErrorHandler{
    private static Logger log = Logger.getLogger(ContactAction.class);
    private ContactManager cmanager;
    private List<String[]> contacts;
    private String firstname, surname, email; 
    private List<String> selected;
    
    public Resolution handleValidationErrors(ValidationErrors errors){
        jession = getSessionValue("jession");
        cmanager = getContactManager();
        contacts = cmanager.getContacts();
        log.info("Found "+ contacts.size()+" contacts");
        return null;
    }

    public Resolution displayContacts(){
        jession = getSessionValue("jession");
        cmanager = getContactManager();
        contacts = cmanager.getContacts();
        log.info("Found "+ contacts.size()+" contacts");
        String view = "/jsp/mobile/contact.jsp";
        if(!jession.mobile){
            updateMailSession();
            view = "/jsp/contact.jsp";
        }
        return new ForwardResolution(view);       
    }
    
    public Resolution createContact(){
        jession = getSessionValue("jession");
        Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(email);
        boolean match = m.matches();
        if(!match){
            log.warn(email + " is an INVALID email address");
        }
        else{
            cmanager = getContactManager();
            String[] cnt = {firstname, surname, email};
            log.info("creating new CONTACT " + firstname +" "+surname);
            cmanager.createContact(cnt);
        }
        return new RedirectResolution(ContactAction.class, "displayContacts");
    }
    
    @DefaultHandler
    public Resolution deleteContact(){
        jession = getSessionValue("jession");
        cmanager = getContactManager();
        cmanager.deleteContacts(selected);
        return new RedirectResolution(ContactAction.class, "displayContacts");
    }
    
    private ContactManager getContactManager(){
        if(getSessionValue("persistence").equals("serialize")){
            cmanager = new SerialContact((String)getSessionValue("home"), jession.username);
        }
        else{
            cmanager = getSessionValue("datastore");
        }
        return cmanager;
    }
    
    public List<String[]> getContacts(){return contacts;}  
    public String getFirstname(){return firstname;}
    public String getSurname(){return surname;}
    public String getEmail(){return email;}
    
    @Validate(required=true, on="createContact")
    public void setFirstname(String f){firstname = HtmlUtil.encode(f);}
    @Validate(required=true, on="createContact")
    public void setSurname(String s){surname = HtmlUtil.encode(s);}
    @Validate(required=true, on="createContact", mask=".+@.+\\.[a-z]+")
    public void setEmail(String e){email = e;}
    @Validate(required=true, on="deleteContact")
    public void setSelected(List<String> s){selected = s;}
}
