package pt.uc.dei.aor.pf.DGeRS.credentials;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.security.CacheableManager;

@WebListener
public class FlushCredentialsListener implements HttpSessionListener {

    @Resource(name = "java:jboss/jaas/ProjetoFinal/authenticationMgr")
    private CacheableManager<?, Principal> authenticationManager;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }
 
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        Principal principal = (Principal) httpSessionEvent.getSession().getAttribute("principal");
        if (principal != null)
            authenticationManager.flushCache(principal);
    }

}

