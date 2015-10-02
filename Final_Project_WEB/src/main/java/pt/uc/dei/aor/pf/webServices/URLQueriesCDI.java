//package pt.uc.dei.aor.pf.webServices;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.ejb.EJB;
//import javax.enterprise.context.RequestScoped;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import pt.uc.dei.aor.pf.constants.Constants;
//import pt.uc.dei.aor.pf.urlQueriesManagement.URLQueriesManagementInterface;
//
//@RequestScoped
//public class URLQueriesCDI {
//
//	@EJB
//	private URLQueriesManagementInterface urlQueriesEJB;
//
//	private List<String>processedQueryResult;
//
//	private String subject;
//
//	private static final Logger log = LoggerFactory.getLogger(URLQueriesCDI.class);
//
//
//	// Vem do UserSessionManagement.checkForUserAndServices()
//	public boolean processQuery(){
//		
//		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//
//		// Verifica se há query
//		if(request.getQueryString()!=null){
//			// Vai buscar o "assunto a tratar" da query
//			this.subject=request.getParameter(Constants.QUERY_SUBJECT);
//
//			// Verifica se há algo para fazer
//			if(this.subject!=null){
//
//				// Processa a query e devolve o resultado
//
//				// Autenticação de candidato por email (paramater Constants.QUERY_EMAIL)
//				if(this.subject.equals(Constants.QUERY_SUBJECT)) this.processedQueryResult=this.urlQueriesEJB.authenticateCandidate(request.getParameter(Constants.SERVICE_EMAIL));
//
//				
//				// O servicesEJB devolve uma lista de strings
//				// this.processedQueryResult.get(0) -> Mensagem
//				// this.processedQueryResult.get(1) -> Caminho para reencaminhar
//				// Se vem nulo a query não era válida
//				if(this.processedQueryResult!=null){
//
//					// Se não é preciso redireccionar, a Lista só tem um elemento
//					if(this.processedQueryResult.size()!=1){
//						// Redirect
//						HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//						try {
//							log.info("Query processed with web redirect: "+processedQueryResult.get(0));
//							response.sendRedirect(request.getContextPath()+processedQueryResult.get(1));
//						} catch (IOException e) {
//							log.error("Error redirecting service");
//						}
//					}else{
//						// Se só tem um elemento não redirecciona mas mostra a mensagem aqui
//						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(processedQueryResult.get(0)));
//						log.info("Query processed without web redirect.");
//					}
//					return true;
//
//				}else{
//					// A query não era válida
//					log.info("Invalid query");
//					return false;
//				}
//			}else{
//				// Não havia nada para fazer
//				log.info("No subject atribute from query");
//				return false;
//			}
//			
//		}
//		// Não há query
//		log.info("No query to process");
//		return false;
//
//	}
//
//}
