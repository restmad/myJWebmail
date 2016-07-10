package pm.matthews.webmail.ext;

import org.apache.log4j.Logger;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.exception.ExceptionHandler;
import pm.matthews.webmail.JwmaSession;

public class JwmaExceptionHandler implements ExceptionHandler{
    private static Logger log = Logger.getLogger(JwmaExceptionHandler.class);
    
    public void handle(Throwable ex, HttpServletRequest req, HttpServletResponse resp)
                       throws ServletException, IOException{
        log.error(ex);
        if(ex.toString().equals("java.lang.NullPointerException")){
            //resp.sendRedirect(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath());
            //maybe javascript does this better? Don't seem to have to worry about the port so >>*/
            req.getRequestDispatcher("/jsp/logout.jsp").forward(req, resp);
        }
        else{
            HttpSession session = req.getSession();
            JwmaSession jession =  (JwmaSession)session.getAttribute("jession");
            String view = "/jsp/error.jsp";
            if(jession.mobile){
                view = "/jsp/mobile/error.jsp";
            }
            req.getRequestDispatcher(view).forward(req, resp);
        }
    }
    
    public void init(Configuration config){ }
}
