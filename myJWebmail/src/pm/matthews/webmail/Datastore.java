package pm.matthews.webmail;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.log4j.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import pm.matthews.webmail.domain.User;
import pm.matthews.webmail.domain.Contact;

public class Datastore  implements Preference, ContactManager{
    private static Logger log = Logger.getLogger(Datastore.class);
    private User user;
    private Contact contact;
    private ArrayList<String[]> cList;
    private Connection connection;
    private Statement statement;
    private PreparedStatement update;
    private String query;
    private String database;
    
    public Datastore(String database, String name, String lang){
            this.database = database;
            user = findUser(name);
            if(user == null){
                createUser(name, lang);
            }
            contact = user.getContact();
            cList = contact.getContactList();
    }
    
    @Override
    public String[] getUserPref(){
        String[] prefs = new String[10];
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
    public void setUpdates(String l,boolean aq,boolean as,boolean ae,String s,String d,String mid,int p){
        user.setLanguage(l);
        user.setAutoEmpty(ae);
        user.setAutoQuote(aq);
        user.setAutoSign(as);
        user.setSent(s);
        user.setDraft(d);
        user.setMailId(mid);
        user.setProcessor(p);
        persistUser(user);      
    }   
    @Override    
    public void setSortOrder(int so){
        user.setSortOrder(so);
        getConnection();
        try{
           statement = connection.createStatement();
           query = "update JWMA_USER set SORT_ORDER = " + so + " where USERNAME = '" + user.getUsername() +"'";
           statement.executeUpdate(query);
           statement.close();
           connection.close();
        }
        catch(Exception ex){}
    }   
    @Override
    public void writeUserPref(){
        persistUser(user);
    }
    @Override    
    public void deleteContacts(List<String> emailDel){
        getContacts();
        Iterator<String[]> it;
        for(String s:emailDel){
            it = cList.iterator();
            while (it.hasNext()){
                if(it.next()[2].equals(s)){
                    it.remove();
                }
            }
        }
        persistContacts();
    }
    @Override
    public void createContact(String[] cnt){
        cList.add(cnt);
        Collections.sort(cList, new SortContacts());
        contact.setContactList(cList);
        persistContacts();
    }
    @Override    
    public ArrayList<String[]> getContacts(){
        return cList;
    }
    @Override
    public void shutdown(){
        try{connection.close();}
        catch(Exception ex){}
    }
    
    private void createUser(String name, String lang){
        log.info("CREATING entry for new user " + name);
        user= new User();
        user.setUsername(name);
        user.setLanguage(lang);
        user.setSortOrder(0);
        user.setAutoEmpty(true);
        user.setAutoQuote(false);
        user.setAutoSign(false);
        user.setSent("");
        user.setDraft("");
	if(name.contains("@")){
		user.setMailId("JWMA User<" + name + ">");
	}
        else{
		user.setMailId("JWMA User<" + name + "@localhost>");
	}
        contact = new Contact();
        cList = new ArrayList<String[]>();
        contact.setContactList(cList);
        user.setContact(contact);
        user.setProcessor(1);
        getConnection();
        try{
            update = connection.prepareStatement("insert into JWMA_USER("+
                    "USERNAME,AUTO_EMPTY,AUTO_QUOTE,AUTO_SIGN,DRAFT_FOLDER,"+
                    "LANGUAGE,MAIL_ID,SENT_FOLDER,SORT_ORDER,PROCESSOR) "+
                    "values(?,?,?,?,?,?,?,?,?,?)");
            update.setString(1, name);
            update.setBoolean(2, true);
            update.setBoolean(3, false);
            update.setBoolean(4, false);
            update.setString(5, "");
            update.setString(6, "en");
            update.setString(7, "JWMA User<" + name + "@localhost>");
            update.setString(8, "");
            update.setInt(9, 0);
            update.setInt(10, 0);
            update.executeUpdate();
            update.close(); 
            persistContacts();
        }
        catch(Exception ex){System.out.println(ex);}
        finally{shutdown();}
    }
    
    private void persistContacts(){
        getConnection();
        try{
           update = connection.prepareStatement("update JWMA_USER set CONTACT_LIST = ? where USERNAME = ?");
           Object o = (Object)cList;
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           ObjectOutputStream oos = new ObjectOutputStream(bos); 
           oos.writeObject(o);
           oos.flush(); 
           oos.close(); 
           bos.close();
           byte[] bytes = bos.toByteArray ();
           update.setBytes(1, bytes);
           update.setString(2, user.getUsername());
           update.executeUpdate();
           update.close();
        }
        catch(Exception ex){System.out.println(ex);}
        finally{shutdown();}
    }
    
    private void persistUser(User user){
        getConnection();
        try{
            update = connection.prepareStatement("update JWMA_USER set "+
                    "AUTO_EMPTY = ?, AUTO_QUOTE = ?, AUTO_SIGN = ?, DRAFT_FOLDER = ?,"+
                    "LANGUAGE = ?, MAIL_ID = ?, PROCESSOR = ?, SENT_FOLDER = ?"+ 
                    "where USERNAME = '" + user.getUsername() + "'");
            update.setBoolean(1, user.getAutoEmpty());
            update.setBoolean(2, user.getAutoQuote());
            update.setBoolean(3, user.getAutoSign());
            update.setString(4, user.getDraft());
            update.setString(5, user.getLanguage());
            update.setString(6, user.getMailId());
            update.setInt(7, user.getProcessor());
            update.setString(8, user.getSent());
            update.executeUpdate();
            update.close(); 
        }
        catch(Exception ex){System.out.println(ex);} 
        finally{try{connection.close();}catch(Exception ex){};}
    }
    
    private User findUser(String name){
        getConnection();
        user = null;
        try{
           statement = connection.createStatement();
           query = "select * from JWMA_USER where USERNAME = '" + name +"'";
           ResultSet res = statement.executeQuery(query);
           while(res.next()){
             user= new User();
             user.setUsername(res.getString("USERNAME"));
             user.setAutoEmpty(res.getBoolean("AUTO_EMPTY"));
             user.setAutoQuote(res.getBoolean("AUTO_QUOTE"));
             user.setAutoSign(res.getBoolean("AUTO_SIGN"));
             ByteArrayInputStream bis = new ByteArrayInputStream(res.getBytes("CONTACT_LIST"));
             ObjectInputStream ois = new ObjectInputStream(bis);
             cList = (ArrayList<String[]>)ois.readObject();
             contact = new Contact();
             contact.setContactList(cList);
             user.setContact(contact);
             user.setDraft(res.getString("DRAFT_FOLDER"));
             user.setLanguage(res.getString("LANGUAGE"));
             user.setMailId(res.getString("MAIL_ID"));
             user.setProcessor(res.getInt("PROCESSOR"));
             user.setSent(res.getString("SENT_FOLDER"));
             user.setSortOrder(res.getInt("SORT_ORDER"));
           }
           statement.close();
        }
        catch(Exception ex){log.debug(ex);}
        finally{
            shutdown();
            return user;
        }
    }
    
    private void getConnection(){
        try{
            if(database.equals("postgres")){
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jwma", "jwma", "webmail");
            }
            else if(database.equals("mysql")){
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jwma", "jwma", "webmail");
            }
        }
        catch(Exception ex){log.debug("cannot connect to " + database + ex);}
    }
}
