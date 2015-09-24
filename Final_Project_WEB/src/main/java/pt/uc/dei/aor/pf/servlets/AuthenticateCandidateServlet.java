package pt.uc.dei.aor.pf.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.MailManagementInterface;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementInterface;

@WebServlet(name="/"+Constants.SERVLET_AUTH_CANDIDATE, urlPatterns="/services/*")
public class AuthenticateCandidateServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -632986421050762469L;

	private static final Logger log = LoggerFactory.getLogger(AuthenticateCandidateServlet.class);

	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private SecureMailManagementInterface mailEJB;

	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		
		if(request.getParameter(Constants.SERVLET_EMAIL)!=null){
			// Autentica o utilizador
			log.info("Authenticating "+request.getParameter(Constants.SERVLET_EMAIL));
			UserEntity user=this.userEJB.findUserByEmail(request.getParameter(Constants.SERVLET_EMAIL));

			if(user!=null){
				user.setAuthenticated(true);
				this.userEJB.update(user);
			} else log.info("Email not found: "+request.getParameter(Constants.SERVLET_EMAIL));
			
			
			try {
				// Reencaminha para a página inicial
				this.mailEJB.authenticatedEmail(user);
				response.sendRedirect(request.getContextPath()+"/Home.xhtml");
			} catch (IOException e) {
				log.error("Error redirecting");
			}
			
		}

	}

}
