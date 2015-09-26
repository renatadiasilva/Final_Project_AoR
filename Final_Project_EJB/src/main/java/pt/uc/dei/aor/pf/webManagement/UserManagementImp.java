package pt.uc.dei.aor.pf.webManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementInterface;


@Stateful
public class UserManagementImp implements UserManagementInterface {

	private static final Logger log = LoggerFactory.getLogger(UserManagementImp.class);

	@EJB
	PositionEJBInterface positionEJB;
	
	@EJB
	UserEJBInterface userEJB;

	@EJB
	UserInfoEJBInterface userInfoEJB;

	@EJB
	SecureMailManagementInterface mail;

	private UserEntity currentUser;

	private boolean admin, manager, interviewer, candidate;

	private boolean newPasswordCheck;
	
	private Calendar yesterday;

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
		this.currentUser = this.userEJB.findUserByEmail(email);

		// Se houver um pirata que consiga chegar aqui com a 
		// password errada o user vai para null e eventualmente
		// a aplicação vai rebentar de propósito
		if(!this.userEJB.checkPassword(this.currentUser, password)){
			this.currentUser=null;
			// Pode ser gerada por algum problema de autenticação no servidor, mas...
			log.warn("Possible security breach, heads up!");
		}

		// Roles para mostrar na web
		this.setAvailableRoles();
		
		// Quando se loga um Admin
		// Fecha as posições que já passaram o SLA
		// Também notifica quando o SLA se aproxima do fim
		if(this.admin)
			this.checkSLAs();
	}

	private void checkSLAs() {
		
		List<PositionEntity>overdueSLAs=this.positionEJB.findCloseToSLAPositions(0);
		
		// Fecha as posições que já passaram o SLA
		for(PositionEntity position:overdueSLAs){
			position.setStatus(Constants.STATUS_CLOSED);
			position.setClosingDate(new Date());
			this.positionEJB.update(position);
		}
		
		// Procura as posições que têm uma margem de 3 dias para acabar
		List<PositionEntity>threeDaysToCloseSLAs=this.positionEJB.findCloseToSLAPositions(3);

		this.yesterday=Calendar.getInstance();
		this.yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		// Envia email a alertar a aproximação do fim do SLA
		for(PositionEntity position:threeDaysToCloseSLAs)			
			if(this.sendMail(position)){
				this.mail.slaWarning(position);
				position.setLastSlaMailDate(new Date());
				this.positionEJB.update(position);
			}
		
	}
	
	private boolean sendMail(PositionEntity position){
		// Se ainda não foi enviado nenhum email, envia agora
		if(position.getLastSlaMailDate()==null)
			return true;
		
		// Se o último mail foi há mais de um dia envia de novo
		if(position.getLastSlaMailDate().after(this.yesterday.getTime()))
			return true;
		
		return false;
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

		this.userEJB.update(this.currentUser);	
	}

	@Override
	public boolean checkDefault(String role){
		if(this.currentUser.getDefaultRole()!=null&&this.currentUser.getDefaultRole().equals(role)) return true;
		return false;
	}

	@Override
	public boolean changePassword (String password, String newPassword){
		log.info("Changing password");
		log.debug("User: "+ currentUser.getEmail());

		// Verifica e altera se estiver tudo certo
		if(this.userEJB.checkPassword(this.currentUser, password)){
			this.currentUser.setPassword(newPassword);
			this.userEJB.updatePassword(currentUser);
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
		if(this.userEJB.findUserByEmail(email)==null){
			List<String> roles = new ArrayList<String>();
			roles.add(Constants.ROLE_CANDIDATE);

			if(admin&&createdByAdmin)roles.add(Constants.ROLE_ADMIN);
			if(manager&&createdByAdmin)roles.add(Constants.ROLE_MANAGER);
			if(interviewer&&createdByAdmin)roles.add(Constants.ROLE_INTERVIEWER);

			// Atributos do UserEntity
			UserEntity newUser=new UserEntity(email, password, firstName, lastName, roles);

			// Role por default
			newUser.setDefaultRole(Constants.ROLE_CANDIDATE);	
			if(interviewer)newUser.setDefaultRole(Constants.ROLE_INTERVIEWER);
			if(manager)newUser.setDefaultRole(Constants.ROLE_MANAGER);
			if(admin)newUser.setDefaultRole(Constants.ROLE_ADMIN);

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
				// Gera a chave de autenticação
				newUser.setAuthenticationKey(RandomStringUtils.randomAlphanumeric(8));
			}

			// Grava o UserEntity
			this.userEJB.save(newUser);

			log.info("Successfully created new user (candidate)");
			if(createdByAdmin){
				this.mail.sendPassToNewUser(newUser, password);
				log.info("An email was sent to a new user: "
						+ newUser.getEmail()+" with "
						+ "temporary password "+password);
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
		if(this.userEJB.findUserByEmail(email)==null){

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

			this.userEJB.save(newUser);

			// an email is sent to the new user
			this.mail.sendPassToNewUser(newUser, password);
			log.info("An email was sent to a new user: "
					+ newUser.getEmail()+" with "
					+ "temporary password "+password);

			return true;

		}else return false;
	}

	@Override
	public UserEntity getUserData() {
		//clone current user and sent to web layer
		UserEntity u = new UserEntity(currentUser.getEmail(), "no password",
				currentUser.getFirstName(), currentUser.getLastName(), null);
		u.setId(currentUser.getId());
		if (currentUser.getUserInfo() != null) {
			UserInfoEntity uinfo = new UserInfoEntity(
					currentUser.getUserInfo().getBirthDate(),
					currentUser.getUserInfo().getAddress(), 
					currentUser.getUserInfo().getCity(),
					currentUser.getUserInfo().getHomePhone(),
					currentUser.getUserInfo().getMobilePhone(),
					currentUser.getUserInfo().getCountry(),
					currentUser.getUserInfo().getCourse(),
					currentUser.getUserInfo().getSchool(),
					currentUser.getUserInfo().getCv(),
					currentUser.getUserInfo().getOwner());
			u.setUserInfo(uinfo);
		}
		return u;
	}

	@Override
	public void updateUserData(String firstName, String lastName, 
			Date birthday, String address, 
			String city, String homePhone, String mobilePhone, String country, 
			String course, String school, String linkedin) {

		log.info("Updating user info");
		log.debug("New user: "+ currentUser.getEmail());

		this.currentUser.setFirstName(firstName);
		this.currentUser.setLastName(lastName);

		if ( (birthday == null) &&
				(address == null || address.isEmpty()) &&
				(city == null || city.isEmpty()) &&
				(homePhone == null || homePhone.isEmpty()) &&
				(mobilePhone == null || mobilePhone.isEmpty()) &&
				(country == null || country.isEmpty()) &&
				(course == null || course.isEmpty()) &&
				(school == null || school.isEmpty()) ) {
			// no need for user info creation/update
		} else {

			if(this.currentUser.getUserInfo()==null) {
				this.currentUser.setUserInfo(new UserInfoEntity());
				this.currentUser.getUserInfo().setOwner(this.currentUser);
			}

			this.currentUser.getUserInfo().setBirthDate(birthday);
			this.currentUser.getUserInfo().setAddress(address);
			this.currentUser.getUserInfo().setCity(city);
			this.currentUser.getUserInfo().setHomePhone(homePhone);
			this.currentUser.getUserInfo().setMobilePhone(mobilePhone);
			this.currentUser.getUserInfo().setCountry(country);
			this.currentUser.getUserInfo().setCourse(course);
			this.currentUser.getUserInfo().setSchool(school);
			this.currentUser.getUserInfo().setLinkedin(linkedin);
		}

		this.userEJB.update(this.currentUser);
	}

	@Override
	public boolean recoverPassword (String email, String temporaryPassword){
		log.info("Password recovery: "+email);

		UserEntity userToRecover=userEJB.findUserByEmail(email);

		// Verifica se existe algum utilizador com o email na BD
		if(userToRecover!=null){

			// Muda a password para uma password temporária
			userToRecover.setPassword(temporaryPassword);
			this.userEJB.updatePassword(userToRecover);

			// Persiste com o atributo de password temporária marcado a true
			userToRecover.setTemporaryPassword(true);
			this.userEJB.update(userToRecover);

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
		if(this.userEJB.findUserByEmail(email).isAuthenticated())return true;
		return false;
	}

	@Override
	public long uploadCV() {
		this.currentUser.setUploadedCV(true);
		this.userEJB.update(this.currentUser);

		log.info("CV uploaded, User uploaded, id returned: "+this.currentUser.getEmail());
		return this.currentUser.getId();
	}

	@Override
	public boolean isCv() {
		return this.currentUser.isUploadedCV();
	}

	@Override
	public long getId() {
		return this.currentUser.getId();
	}	

}
