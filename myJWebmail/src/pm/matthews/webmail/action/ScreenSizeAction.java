package pm.matthews.webmail.action;

import javax.servlet.http.Cookie;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

public class ScreenSizeAction extends BaseAction{
    
    public Resolution desktop(){
        lastLogin = setLoginName();
        return new ForwardResolution("/jsp/login.jsp");
    }
    
    public Resolution mobile(){
        lastLogin = setLoginName();
        return new ForwardResolution("/jsp/mobile/login.jsp");
    }
}
