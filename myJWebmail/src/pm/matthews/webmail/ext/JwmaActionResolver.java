
package pm.matthews.webmail.ext;

import net.sourceforge.stripes.controller.NameBasedActionResolver;

public class JwmaActionResolver extends NameBasedActionResolver{
    
    @Override
    protected String getBindingSuffix(){
        return ".jwma";
    }
}
