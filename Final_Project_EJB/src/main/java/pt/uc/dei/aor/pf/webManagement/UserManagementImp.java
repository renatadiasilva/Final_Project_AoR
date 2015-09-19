package pt.uc.dei.aor.pf.webManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementInterface;


@Stateful
public class UserManagementImp implements UserManagementInterface {

	private static final Logger log = LoggerFactory.getLogger(UserManagementImp.class);

	@EJB
	UserEJBInterface userBean;

	@EJB
	UserInfoEJBInterface userInfoBean;
	
	@EJB
	SecureMailManagementInterface mail;

	private UserEntity currentUser;

	private boolean admin, manager, interviewer, candidate;

	private boolean newPasswordCheck;

	public UserManagementImp() {		
		this.currentUser=new UserEntity();
		this.admin=this.manager=this.interviewer=this.candidate=false;

		this.newPasswordCheck=true;
	}

	@Override
	public void login(String email, String password){
		log.info("Loggin In "+email);

		// Se o servidor consegue logar o utilizador não cria a excepção 
		// (vem do UserSessionManagement) e chega aqui: logo a password está correcta
		this.currentUser = this.userBean.findUserByEmail(email);
		
		// Se houver um pirata que consiga chegar aqui com a 
		// password errada o user vai para null e eventualmente
		// a aplicação vai rebentar de propósito
		if(!this.userBean.checkPassword(this.currentUser, password)){
			this.currentUser=null;
			// Pode ser gerada por algum problema de autenticação no servidor, mas...
			log.warn("Possible security breach, heads up!");
		}
		
		// Roles para mostrar na web
		this.setAvailableRoles();
	}

	@Override
	public void logout(){
		log.info("Loggin Out "+this.currentUser.getEmail());
		
		this.admin=this.manager=this.interviewer=this.candidate=false;
		this.currentUser=new UserEntity();
	}

	// Para exibição do menu de navegação (rendered)
	private void setAvailableRoles() {
		for (String s: this.currentUser.getRoles()){
			if(s.equals(Constants.ROLE_ADMIN)) this.admin=true;
			if(s.equals(Constants.ROLE_MANAGER)) this.manager=true;
			if(s.equals(Constants.ROLE_INTERVIEWER)) this.interviewer=true;
			if(s.equals(Constants.ROLE_CANDIDATE)) this.candidate=true;
		}
	}

	@Override
	public void defaultRole(String role){

		if(role.equals(Constants.ROLE_ADMIN)) this.currentUser.setDefaultRole(role);
		if(role.equals(Constants.ROLE_MANAGER)) this.currentUser.setDefaultRole(role);
		if(role.equals(Constants.ROLE_INTERVIEWER)) this.currentUser.setDefaultRole(role);
		if(role.equals(Constants.ROLE_CANDIDATE)) this.currentUser.setDefaultRole(role);

		this.userBean.update(this.currentUser);	
	}

	@Override
	public boolean checkDefault(String role){
		if(this.currentUser.getDefaultRole().equals(role)) return true;
		return false;
	}

	@Override
	public boolean changePassword (String password, String newPassword){
		log.info("Changing password");
		log.debug("User: "+ currentUser.getEmail());

		// Verifica e altera se estiver tudo certo
		if(this.userBean.checkPassword(this.currentUser, password)){
			this.currentUser.setPassword(newPassword);
			this.userBean.updatePassword(currentUser);
			return true;
		}else return false;
	}

	// Novo utilizador com ROLE_CANDIDATE. Se vem do signup: boolean createdByAdmin=false.
	// Se é criado por admin: boolean createdByAdmin=true
	@Override
	public boolean newUser(String email, String password, String firstName, String lastName,
			Date birthday, String address, String city,
			String homePhone, String mobilePhone, String country, String course, String school, 
			String linkedin, boolean createdByAdmin,
			boolean admin, boolean manager, boolean interviewer){

		log.info("Creating new user (candidate)");

		// Verifica primeiro se o email já está a uso
		if(this.userBean.findUserByEmail(email)==null){
			List<String> roles = new ArrayList<String>();
			roles.add(Constants.ROLE_CANDIDATE);

			if(admin&&createdByAdmin)roles.add(Constants.ROLE_ADMIN);
			if(manager&&createdByAdmin)roles.add(Constants.ROLE_MANAGER);
			if(interviewer&&createdByAdmin)roles.add(Constants.ROLE_INTERVIEWER);

			// Atributos do UserEntity
			UserEntity newUser=new UserEntity(email, password, firstName, lastName, roles);
			newUser.setDefaultRole(Constants.ROLE_CANDIDATE);	

			// Atributos do UserInfoEntity do respectivo UserEntity
			UserInfoEntity newUserInfo= new UserInfoEntity(birthday, address, city, homePhone, mobilePhone, 
					country, course, school, null, newUser);

			newUser.setUserInfo(newUserInfo);

			// Se for criado por um admin, esse admin é o currentUser e a temporaryPassword=true
			if(createdByAdmin){
				newUser.setCreatedBy(this.currentUser);
				newUser.setTemporaryPassword(true);
				newUser.setAuthenticated(true);
			}else{
				// Se vem da página do signup, vai ter de ser autenticado por email
				newUser.setAuthenticated(false);
			}
			
			// Grava o UserEntity
			this.userBean.save(newUser);

			log.info("Successfully created new user (candidate)");
			if(createdByAdmin){
				log.info("New user: "+ newUser.getEmail()+" with temporary password "+password);
			}else{
				// Envia o email para o processo de autenticação
				this.mail.candidateToAuthenticate(newUser);
			}
			return true;

		}else {
			log.info("Registration failure: the email already exists");
			return false;
		}
	}

	// Novo utilizador criado por um admin sem o ROLE_CANDIDATE
	@Override
	public boolean newUserNC (String email, String password, String firstName,
			String lastName, boolean admin, boolean manager, boolean interviewer) {

		log.info("Creating new user (internal)");

		// Verifica primeiro se o email já está a uso
		if(this.userBean.findUserByEmail(email)==null){

			UserEntity newUser=new UserEntity();
			List<String> roles = new ArrayList<String>();

			if(admin)roles.add(Constants.ROLE_ADMIN);
			if(manager)roles.add(Constants.ROLE_MANAGER);
			if(interviewer)roles.add(Constants.ROLE_INTERVIEWER);

			newUser=new UserEntity(email, password, firstName, lastName, roles);

			// Role por default
			if(interviewer)newUser.setDefaultRole(Constants.ROLE_INTERVIEWER);
			if(manager)newUser.setDefaultRole(Constants.ROLE_MANAGER);
			if(admin)newUser.setDefaultRole(Constants.ROLE_ADMIN);

			// Foi criado por um admin, esse admin é o currentUser, e a temporaryPassword=true
			// Se foi um administrador que criou é fiável, não precisa de autenticação
			newUser.setCreatedBy(this.currentUser);
			newUser.setTemporaryPassword(true);
			newUser.setAuthenticated(true);

			this.userBean.save(newUser);

			return true;
			
		}else return false;
	}

	@Override
	public void updateUserInfo(String firstName, String lastName, String address, 
			String city, String homePhone, String mobilePhone, String country, 
			String course, String school, String linkedin) {

		log.info("Updating user info");
		log.debug("New user: "+ currentUser.getEmail());

		this.currentUser.setFirstName(firstName);
		this.currentUser.setLastName(lastName);

		if(this.currentUser.getUserInfo()==null){
			this.currentUser.setUserInfo(new UserInfoEntity());
			this.currentUser.getUserInfo().setOwner(this.currentUser);
		}

		this.currentUser.getUserInfo().setAddress(address);
		this.currentUser.getUserInfo().setCity(city);
		this.currentUser.getUserInfo().setHomePhone(homePhone);
		this.currentUser.getUserInfo().setMobilePhone(mobilePhone);
		this.currentUser.getUserInfo().setCountry(country);
		this.currentUser.getUserInfo().setCourse(course);
		this.currentUser.getUserInfo().setSchool(school);
		this.currentUser.getUserInfo().setLinkedin(linkedin);

		this.userBean.update(this.currentUser);
	}
	
	@Override
	public boolean recoverPassword (String email, String temporaryPassword){
		log.info("Password recovery: "+email);
		
		UserEntity userToRecover=userBean.findUserByEmail(email);
		
		// Verifica se existe algum utilizador com o email na BD
		if(userToRecover!=null){
			
			// Muda a password para uma password temporária
			userToRecover.setPassword(temporaryPassword);
			this.userBean.updatePassword(userToRecover);
			
			// Persiste com o atributo de password temporária marcado a true
			userToRecover.setTemporaryPassword(true);
			this.userBean.update(userToRecover);
			
			// Envia um email ao user com a password temporária
			this.mail.passwordRecovery(userToRecover, temporaryPassword);
			
			return true;
			
		}else{
			log.info(email+" not found in DB - no email was sent");
			return false;
		}
	}

	@Override
	public boolean isTemporaryPassword(){
		// Verifica se o utilizador tem uma password temporária
		if(this.currentUser.isTemporaryPassword()&&this.newPasswordCheck){
			// Só chateia a primeira vez, não volta a chatear durante o resto da sessão
			this.newPasswordCheck=false;
			return true;
		}
		return false;
	}

	@Override
	public boolean isAdmin() {
		return admin;
	}

	@Override
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public boolean isManager() {
		return manager;
	}

	@Override
	public void setManager(boolean manager) {
		this.manager = manager;
	}

	@Override
	public boolean isInterviewer() {
		return interviewer;
	}

	@Override
	public void setInterviewer(boolean interviewer) {
		this.interviewer = interviewer;
	}

	@Override
	public boolean isCandidate() {
		return candidate;
	}

	@Override
	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}

	@Override
	public String getUserEmail(){
		return this.currentUser.getEmail();
	}

	@Override
	public String getUserDefaultRole(){
		return this.currentUser.getDefaultRole();
	}

	@Override
	public boolean isUserLogged(){
		if(this.currentUser.getEmail()!=null) return true;
		return false;
	}

	@Override
	public String getUserFullName(){
		return this.currentUser.getFirstName()+" "+this.currentUser.getLastName();
	}
	
	@Override 
	public List<String> getStyle(){
		// Caso seja definida alguma regra consoante o tipo de utilizador, vem para aqui
		// Caso contrário, segue o estilo marcado a "default"
		
		List<String>style=new ArrayList<>();
		
		style.add("ITJobs");
		style.add("ITJobs - Projecto Final | Programação Avançada em JAVA | Duarte Gonçalves | Renata Silva");
		style.add("#3f51b5");
		style.add("#ff4081");
		style.add("1");
		
		return style;
	}

	@Override
	public boolean checkAuthentication(String email) {
		// Se chegou a este ponto (já está autenticado por esta altura no servidor) o email existe
		if(this.userBean.findUserByEmail(email).isAuthenticated())return true;
		return false;
	}
	
}
