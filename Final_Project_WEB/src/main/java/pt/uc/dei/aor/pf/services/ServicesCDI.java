package pt.uc.dei.aor.pf.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.servicesManagement.ServicesManagementInterface;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

@Named
@RequestScoped
public class ServicesCDI {
	
	@EJB
	private ServicesManagementInterface servicesEJB;
	
	@Inject
	private UserSessionManagement userSession;
	
	private List<String>processedQuery;
	
	private String subject;
	
	private static final Logger log = LoggerFactory.getLogger(ServicesCDI.class);
	
	public void processQuery(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		this.subject=request.getParameter("subject");
		
		// Verifica se há algo para fazer
		if(this.subject!=null){
			
			// Autenticação de candidato
			if(this.subject.equals(ServicesManagementInterface.AUTHENTICATE_CANDIDATE))this.processedQuery=this.servicesEJB.authenticateCandidate(request.getParameter("email"));
			
			// this.processdeQuery.get(0) -> Mensagem
			// this.processdeQuery.get(1) -> Caminho para reencaminhar
			// Se vem nulo a query não era válida
			if(this.processedQuery!=null){
				
				// Coloca a mesnagem na sessão corrente
				// A mensagem é despoletada pelo listener no Layout.xhtml
				this.userSession.setServiceMessage(this.processedQuery.get(0));
				
				// Redirect
				HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				try {
					response.sendRedirect(request.getContextPath()+processedQuery.get(1));
				} catch (IOException e) {
					log.error("Error redirecting service");
				}
				
			}
			
		}
		
	}

}
