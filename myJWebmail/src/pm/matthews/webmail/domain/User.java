package pm.matthews.webmail.domain;

import java.io.Serializable;

public class User implements Serializable{
    private String username;
    public void setUsername(String u){username = u;}
    public String getUsername(){return username;}

    private String language;
    public void setLanguage(String l){language = l;}
    public String getLanguage(){return language;}

    private int sortOrder;
    public void setSortOrder(int so){sortOrder = so;}
    public int getSortOrder(){return sortOrder;}

    private boolean autoEmpty;
    public void setAutoEmpty(boolean ae){autoEmpty = ae;}
    public boolean getAutoEmpty(){return autoEmpty;}

    private boolean autoQuote;
    public void setAutoQuote(boolean aq){autoQuote = aq;}
    public boolean getAutoQuote(){return autoQuote;}

    private boolean autoSign;
    public void setAutoSign(boolean as){autoSign = as;}
    public boolean getAutoSign(){return autoSign;}

    private String sent;
    public void setSent(String s){sent = s;}
    public String getSent(){return sent;}

    private String draft;
    public void setDraft(String d){draft = d;}
    public String getDraft(){return draft;}
    
    private String mailId;
    public void setMailId(String mid){mailId = mid;}
    public String getMailId(){return mailId;}

    private int processor;
    public void setProcessor(int p){processor = p;}
    public int getProcessor(){return processor;}
    
    private Contact contact;
    public void setContact(Contact c){contact = c;}
    public Contact getContact(){return contact;}
}