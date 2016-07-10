package pm.matthews.webmail;

public interface Preference{
    public String[] getUserPref();
    public void setUpdates(String l,boolean as,boolean aq,boolean ae,String s,String d,String m,int p);
    public void writeUserPref();
    public void setSortOrder(int order);
    public void shutdown();
}