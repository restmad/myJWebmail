 
package pm.matthews.webmail.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Contact implements Serializable{
    private ArrayList<String[]> contactList;
    public void setContactList(ArrayList<String[]> cl){
        contactList = cl;
    }
    public ArrayList<String[]> getContactList(){
        return contactList;
    }
}
