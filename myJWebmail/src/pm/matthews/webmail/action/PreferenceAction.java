package pm.matthews.webmail.action;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Folder;
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
import pm.matthews.webmail.Preference;
import pm.matthews.webmail.SerialPref;

public class PreferenceAction extends BaseAction implements ValidationErrorHandler{
    private static Logger log = Logger.getLogger(PreferenceAction.class);
    private Preference preference;
    private String[] pref,lang,sent,draft,mbox,processorOptions;
    private String language, sentMail, draftMail, newName, newFrom;
    private boolean autoSign, autoQuote, autoEmpty, newDef;
    private String[][] longMailId;
    private List<String> defaultID, selected;
    private int processor;

    public Resolution handleValidationErrors(ValidationErrors errors){
        jession = getSessionValue("jession");
        preference = getPreference();
        pref = preference.getUserPref();
        longMailId();
        language();
        Folder[] f = jession.mixedStoreList;
        mailboxOnly(f);
        sent = mailboxOrder(pref[6]);
        draft = mailboxOrder(pref[7]);
        return null;
    }

    @DefaultHandler
    public Resolution displayPreference(){
        jession = getSessionValue("jession");
        preference = getPreference();
        pref = preference.getUserPref();
        longMailId();
        language();
        getProcessorOptions();
        Folder[] f = jession.mixedStoreList;
        mailboxOnly(f);
        sent = mailboxOrder(pref[6]);
        draft = mailboxOrder(pref[7]);
        String view = "/jsp/preference.jsp";
        if(jession.mobile){view = "/jsp/mobile/preference.jsp";}
        return new ForwardResolution(view);
    }

    public Resolution updatePreference(){
       updateSettings();
       preference = getPreference();
       int processor = jession.processor;
       String view = "";
       if(jession.mobile){
           /*we leave desktop view setting for this unchanged*/
           processor = Integer.parseInt(preference.getUserPref()[9]);
           view = "/jsp/mobile/folder.jsp";
       }
       else{
           updateMailSession();
           view = "/jsp/folder.jsp";
       }
       preference.setUpdates(language,autoSign,autoQuote,autoEmpty,sentMail,draftMail,jession.mailId,processor);
       preference.writeUserPref();
       return new ForwardResolution(view);
    }

    public Resolution deleteMailId(){
        jession = getSessionValue("jession");
	if(getMailId().length <= selected.size()){
            LocalizableError err = new LocalizableError("deleteMailId.zero");
            getContext().getMessages().add(err);
        }
        else{
           for(String mid:selected){
               jession.mailId = removeMailId(mid);
               log.info("DELETING mail id with email " + mid);
               updateSettings();
           }
        }
        updateMailSession();
        return new RedirectResolution(PreferenceAction.class, "displayPreference");
    }

    public Resolution addMailId(){
        jession = getSessionValue("jession");
        Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(newFrom);
        boolean match = m.matches();
        if(!match){
            log.warn(newFrom + " is an INVALID email address");
        }
        else{
            log.info("ADDING mail id " + newName + " " + newFrom);
            if(jession.mobile){
                jession.mailId = newName + "<" + newFrom + ">," + jession.mailId;
            }
            else{
                jession.mailId = addMailId(newName, newFrom);
                defaultMailId();
            }
            updateSettings();
        }
        updateMailSession();
        return new RedirectResolution(PreferenceAction.class, "displayPreference");
    }

    public void language(){
         String[] temp = getSessionValue("languages").toString().split(",");
         lang = new String[temp.length];
         lang[0] = pref[1];
         int i = 1;
         for(String l:temp){
            if(!l.equals(lang[0])){
                lang[i] = l;
                i++;
            }
         }
    }

    private void defaultMailId(){
        if(defaultID == null){
            return;
        }
        else{
            String mid = defaultID.get(0);
            if(mid.equals("checked")){
                mid = newFrom;
            }
            log.info("mail id with email " + mid + " is DEFAULT");
            String mailId = "";
            String[] temp = getMailId();
            for(String s:temp){
                if(s.contains(mid)){
                    mailId = mailId + s +",";
                    break;
                }
            }
            for(String s:temp){
               if(!s.contains(mid)){
                    mailId = mailId + s +",";
                }
            }
            jession.mailId = mailId;
        }
    }

    private String removeMailId(String del){
        String[] temp = getMailId();
        String mailId = "";
        for(int i=0;i<temp.length;i++){
            if(!temp[i].contains(del)){
                mailId = mailId + temp[i] +",";
            }
        }
        return mailId;
    }
    private String addMailId(String name, String from){
        String[] temp = getMailId();
        String mailId = "";
        for (String s:temp){
            mailId = mailId + s + ",";
        }
        if(newDef){
            mailId = name + "<" + from + ">," + mailId;
        }
        else{
            mailId = mailId + name + "<" + from + ">,";
        }
        return mailId;
    }


    private String[] mailboxOnly(Folder[] fld){
        ArrayList<String> mb = new ArrayList();
        for(Folder f:fld){
            try{
                if(f.getType() == 1){
                    mb.add(f.getName());
                }
            }
            catch(Exception ex){}
        }
        mbox = mb.toArray(new String[mb.size()]);
        return mbox;
    }
    private String[] mailboxOrder(String s){
        String[] temp = new String[mbox.length+1];
        temp[0] = s;
        int i = 1;
        for(String m:mbox){
            if(!m.equals(temp[0])){
                temp[i] = m;
                i++;
            }
        }
        return temp;
    }
    private void updateSettings(){
       jession = getSessionValue("jession");
       jession.language = language;
       jession.autoSign = ""+autoSign;
       jession.autoQuote = ""+autoQuote;
       jession.autoEmpty = ""+autoEmpty;
       jession.sent = sentMail;
       jession.draft = draftMail;
       if(!jession.mobile){
            jession.processor = processor;
            defaultMailId();
       }
       setSessionValue("jession", jession);
    }
    
    private Preference getPreference(){
        if(getSessionValue("persistence").equals("serialize")){
            preference = new SerialPref((String)getSessionValue("home"), jession.username);
        }
        else{
            preference = getSessionValue("datastore");
        }
        return preference;
    }
    
    public void longMailId(){
        String[] temp = getMailId();
        longMailId = new String[temp.length][2];
        int i = 0;
        String[] t = new String[2];
        for(String m:temp){
           // so we can upgrade from < 2.3
           //FIXME: we should remove the try/catch in a later version
           try{
                m = m.substring(0, m.indexOf(">"));
                t = m.split("<");
           }
           catch(Exception ex){
               t = m.split(":");
           }
           longMailId[i] = t;
           i++;
        }
    }
    public String[] getPref(){return pref;}
    public String[][] getLongMailId(){return longMailId;}
    public String[] getLang(){return lang;}
    public String[] getSent(){return sent;}
    public String[] getDraft(){return draft;}
    public String getProcess(){return pref[9];}
    public String[] getProcessorOptions(){
        String one = "preference.processor.1";
        String two = "preference.processor.2";
        String three = "preference.processor.3";
        String four = "preference.processor.4";
        String five = "preference.processor.5";
        processorOptions = new String[]{one,two,three,four,five};
        return processorOptions;
    }

    public void setMailId(String[] mi){mailId = mi;}
    public void setLanguage(String l){language = l;}
    public void setAutoSign(boolean as){autoSign = as;}
    public void setAutoQuote(boolean aq){autoQuote = aq;}
    public void setAutoEmpty(boolean ae){autoEmpty = ae;}
    @Validate(required=true, on="deleteMailId")
    public void setSelected(List<String> s){selected = s;}
    public void setDefaultID (List<String> def){defaultID = def;};
    @Validate(required=true, on="addMailId")
    public void setNewName(String name){newName = HtmlUtil.encode(name);}
    @Validate(required=true, on="addMailId", mask=".+@.+\\.[a-z]+")
    public void setNewFrom(String from){newFrom = from;}
    public void setNewDef(boolean df){newDef = df;}
    public void setProcessor(int p){
        processor = p;
        log.info("setting PROCESSOR to option " + processor);
    }
    public void setDraftMail(String d){
        draftMail = "";
        if(d != null){
            draftMail = d;
        }
    }
    public void setSentMail(String s){
        sentMail = "";
        if(s != null){
            sentMail = s;
        }
    }
}
