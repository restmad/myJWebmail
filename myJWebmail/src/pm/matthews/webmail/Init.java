package pm.matthews.webmail;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Init {
    private static Logger log = Logger.getLogger(Init.class);
    private static String[] conf = {
        "############\n#jwma.config\n############\n",
        "#mail_host default is localhost\nmail_host = localhost\n",
        "#access_protocol - the mailbox access, either imap or imaps\naccess_protocol = imap\n",
        "#access port, probably either 143 for imap or 993 for imaps\nport = 143\n",
        "#smtp_port, the port on which the MTA listens, probably 25\nsmtp_port = 25\n",
        "#available interface languages\nlanguages = de,en,es,fr,it\n",
        "#the login screen language and the default choice for users\nlanguage = en\n",
        "#users config & contacts persistence - one of serialize, postgres or mysql\npersistence = serialize\n"
    };
    private static String path, DIR;
    private static File file;  
    private static String message;
    
    public Init(String path){
        message = "";
        this.path = path;
        DIR = ".jwma";
        if(System.getProperty("os.name").contains("Win")){
            DIR = "jwma";
        }
        /*try to construct a file path relative to real localtion of WEB-INF
         *see InitAction.java*/
        if(!path.equals("")){
           String p = path;
           for(int i = 0;i < 3;i++){
               int j = p.lastIndexOf(File.separator);
               p = p.substring(0, j);
           }
           DIR = p + File.separator + DIR;
        }
        else{
           /*fallback to this if running from an unexploded war file*/            
           DIR = ".."+File.separator+ DIR;
        }
        
        file = new File(DIR);
        if(!file.isDirectory()){
            file.mkdirs();
            file = new File(DIR +File.separator+ "data");
            file.mkdirs();
            writeDefaultConfig();
        }
    }
    
   private static void writeDefaultConfig(){     
        try{
            file = new File(DIR +"/jwma.config");
            FileWriter fw = new FileWriter(file);
            PrintWriter out = new PrintWriter(fw);
            for(String s:conf){
                out.println(s);
            }
            out.flush(); fw.flush();
            out.close(); fw.close();  
            message = "WROTE a default config at " + file.getAbsolutePath() + ":";
            System.out.println(message);
        }      
        catch(IOException ex){}
    }
    
    public String[] readConfig(){
        file = new File(DIR + "/jwma.config");
        String msg = "Looking for CONFIG at " + file.getAbsolutePath() + ":";
        System.out.println(msg);
        message = message + msg;
        String configs = "";
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            br.mark(500);
            String key, value = "";
            while(br.readLine() != null){
                br.reset();
                String s = br.readLine();
                if(!s.startsWith("#") && !s.equals("")){
                    String[] item = s.split("=");
                    key = item[0].replaceAll(" ","");
                    value = item[1].replaceAll(" ","");
                    message = message + "key " + key + " value " + value + ":";
                    configs = configs +  key + ":" + value + ":";
                }
                br.mark(500);
            }
            br.close();fr.close();log.debug(configs);
            if(configs.contains("serialize")){
                configs = configs + "home:" + DIR;
                message = message + "key home value "+ DIR + ":";
            }
        }
        catch(Exception ex){}
        String[] res = configs.split(":");
        return res;
    }
    /*we try to put the log file in our webmail home folder
     *but we won't be able to manipulate the log4j property file
     * if running from an unexploded war file
     */
    public void prepareLogging(){
        try{
            Properties props = new Properties();
            props.load(new FileInputStream(path + "/classes/log4j.properties"));
            for(Enumeration en = props.propertyNames(); en.hasMoreElements();){
              String property = (String)en.nextElement();
              if(property.indexOf(".File") > 0){
                file = new File(DIR);
                props.setProperty(property, file.getAbsolutePath() + "/jwma.log");
              }
            }
            PropertyConfigurator.configure(props);           
        }
        catch(Exception ex){}
        finally{
            String[] messages = message.split(":");
            for(String m:messages){
                log.info(m);
            }
        }
    }
}
