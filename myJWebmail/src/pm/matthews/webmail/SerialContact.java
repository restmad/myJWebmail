
package pm.matthews.webmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import pm.matthews.webmail.domain.Contact;

public class SerialContact implements ContactManager{
    private static Logger log = Logger.getLogger(ContactManager.class);
    private String path, username;
    private Contact contact;
    private ArrayList<String[]> contacts;
    
    public SerialContact(String p, String u){
        username = u;
        path = p + File.separator + "data" + File.separator + username + ".contact";
        File f = new File(path);
        if(!f.exists()){
            contact = new Contact();
            contact.setContactList(new ArrayList<String[]>());
            writeContacts();
        }
        contact = readContacts(); 
    }
    
    @Override
    public ArrayList<String[]> getContacts(){
        contacts = contact.getContactList();
        return contacts;
    }
    @Override
    public void createContact(String[] c){
        contacts = getContacts();
        contacts.add(c); 
        log.info("added CONTACT " + contacts.size()+" with email " + c[2]);
        Collections.sort(contacts, new SortContacts());
        writeContacts();
    }
    
    /*search for deletes by matching email since
     * multiple deletes via collection posistion is fraught
     */
    @Override
    public void deleteContacts(List<String> emailDel){
        contacts = getContacts();
        String email;
        Iterator<String[]> it;
        for(String s:emailDel){
            it = contacts.iterator();
            while (it.hasNext()){
                email = it.next()[2];
                if(email.equals(s)){
                    it.remove();
                }
            }
        }
        writeContacts();
    }
    
    private Contact readContacts(){
        contact = null;
        File f = new File(path);
        try{
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fin);
            contact = (Contact)in.readObject();
            in.close();
            fin.close();
        }
        catch(Exception ex){log.debug(ex);};
        return contact;
    }
    
    private void writeContacts(){ 
        try{
            FileOutputStream fout =new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(contact);
            out.close();
            fout.close();         
        }
        catch(Exception ex){log.debug(ex);}
    }
}
