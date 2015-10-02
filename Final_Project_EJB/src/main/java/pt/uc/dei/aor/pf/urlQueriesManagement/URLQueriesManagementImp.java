package pt.uc.dei.aor.pf.urlQueriesManagement;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class URLQueriesManagementImp implements URLQueriesManagementInterface {
	
	private static final Logger log = LoggerFactory.getLogger(URLQueriesManagementImp.class);
	
	@EJB
	private UserEJBInterface userEJB;

	@Override
	public List<String> authenticateCandidate(String email) {
		
		UserEntity user=this.userEJB.findUserByEmail(email);
		user.setAuthenticated(true);
		this.userEJB.update(user);
		
		List<String>responseList=new ArrayList<>();
		responseList.add("Email "+user.getEmail()+" autenticado, registo conluído. Pode efectuar o seu login.");
		
		log.info("User with email "+email+" authenticated");
		
		return responseList;
	}

}
