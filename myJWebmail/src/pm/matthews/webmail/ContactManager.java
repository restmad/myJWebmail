package pm.matthews.webmail;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface ContactManager{
     public ArrayList<String[]> getContacts();
     public void createContact(String[] c);
     public void deleteContacts(List<String> emailDel);
         
    class SortContacts implements Comparator<String[]>{
        public int compare(String[] a, String[] b){
            int result = a[1].toUpperCase().compareTo(b[1].toUpperCase());
            return result;
        }
    }
}