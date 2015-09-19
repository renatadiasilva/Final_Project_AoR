package pt.uc.dei.aor.pf.dao.servicesManagement;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class ServicesManagementImp implements ServicesManagementInterface {
	
	private static final Logger log = LoggerFactory.getLogger(ServicesManagementImp.class);
	
	@EJB
	private UserEJBInterface userEJB;

	@Override
	public List<String> authenticateCandidate(String email) {
		
		UserEntity user=this.userEJB.findUserByEmail(email);
		user.setAuthenticated(true);
		this.userEJB.update(user);
		
		List<String>response=new ArrayList<>();
		response.add("Email "+user.getEmail()+" autenticado, registo conluido. Pode efectuar o seu login.");
		response.add("/Home.xhtml");
		
		log.info("User with email "+email+" authenticated");
		
		return response;
	}

}
