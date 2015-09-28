package pt.uc.dei.aor.pf.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.emailpattern.EmailPattern;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

import java.io.Serializable;


@Named
@SessionScoped
public class AdminNewUserCDI implements Serializable {

	private static final long serialVersionUID = -8739801850337571946L;

	@Inject
	private UserSessionManagement userSessionManagement;

	@EJB
	private UserEJBInterface userEJB;	

	// UserEntity
	private String email, firstName, lastName;

	// UserInfoEntity
	private String address, city, homePhone, mobilePhone, country, course, school, linkedin;

	private Date birthday;

	private boolean admin, manager, interviewer, candidate;

	public AdminNewUserCDI() {
	}

	public void clear(){
		email=firstName=lastName=null;
		address=city=homePhone=mobilePhone=country=course=school=linkedin;
		admin=manager=interviewer=candidate=false;
		birthday=null;
	}

	public void newUser(){

		if (EmailPattern.checkEmailPattern(email)) {

			this.userSessionManagement.newUser(email, 
					userSessionManagement.getRandomPass(), firstName, lastName,
					birthday, address, city, homePhone, mobilePhone, country,
					course, school, linkedin, true, admin, manager, 
					interviewer);

			this.newCandidate = this.userEJB.findUserByEmail(email);

			this.clear();
			this.candidate=true;

		} else FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email inválido.", "Email inválido."));
	}

	public void newUserNC(){

		if (EmailPattern.checkEmailPattern(email)) {

			if(this.admin||this.manager||this.interviewer){

				this.userSessionManagement.newUserNC(email, 
						userSessionManagement.getRandomPass(), 
						firstName, lastName, admin, manager, interviewer);

				this.clear();

			} else FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Defina o tipo de utilizador.",
							"Defina o tipo de utilizador."));

		} else FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Email inválido.", "Email inválido."));
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String adress) {
		this.address = adress.trim();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city.trim();
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone.trim();
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone.trim();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country.trim();
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course.trim();
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school.trim();
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin.trim();
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getMinDate() {
		Calendar minDate=new GregorianCalendar(1900,0,1);
		return minDate.getTime();
	}

	public Date getMaxDate() {
		return new Date();
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public boolean isInterviewer() {
		return interviewer;
	}

	public void setInterviewer(boolean interviewer) {
		this.interviewer = interviewer;
	}

	public boolean isCandidate() {
		return candidate;
	}

	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}




	// Associar novo user a posições
	
	@Inject
	private UserSessionManagement userManagement;

	@Inject
	private UploadFile uploadFile;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	private UserEntity newCandidate;

	private PositionEntity position;

	public void uploadCV(FileUploadEvent event){
		UploadedFile file=event.getFile();

		this.newCandidate.setUploadedCV(true);
		this.userEJB.update(newCandidate);

		this.uploadFile.uploadFile(file, UploadFile.FOLDER_USER_CV, this.newCandidate.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CV do candidato submetido."));
	}

	public void createSubmission(FileUploadEvent event){
		UploadedFile motivationLetter=event.getFile();

		UserEntity admin=this.userEJB.findUserByEmail(this.userManagement.getUserMail());

		SubmissionEntity submission=new SubmissionEntity(newCandidate, Constants.STATUS_SUBMITED, null, null, false);
		submission.setPosition(this.position);
		submission.setAssociatedBy(admin);

		submission=this.submissionEJB.saveAndReturn(submission);

		this.uploadFile.uploadFile(motivationLetter, UploadFile.FOLDER_SUBMISSION_MOTIVATION_LETTER, submission.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);

		this.position=null;

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Candidatura Submetida"));
	}

	public void associateCandidate(UserEntity candidate) {
		System.out.println("Candidate: "+candidate.getEmail());

		this.newCandidate = candidate;
	}

	public void associatePosition(PositionEntity position){

		if(!this.positionEJB.alreadyCandidateOfPosition(newCandidate, position))this.position = position;

		else{
			this.position=null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Já é candidato."));
		}

	}

	public void cleanPandC(){
		System.out.println("Clean P and C");
		this.position=null;
		this.newCandidate=null;
	}

	public void dissociatePosition(){
		this.position=null;
	}

	public boolean cancelPosition(){
		return this.candidateHasCV()&&this.hasPosition();
	}

	public boolean checkPosition(PositionEntity position){
		if(this.position==null) return false;
		return this.position.getPositionCode().equals(position.getPositionCode());
	}

	public boolean hasPosition(){
		return this.position!=null;
	}

	public boolean hasCandidate(){
		return this.newCandidate!=null;
	}

	public boolean candidateHasCV(){
		if(this.newCandidate==null)return false;
		return this.newCandidate.isUploadedCV();
	}

	public String getUserMail(){
		if(this.newCandidate==null) return"";
		return this.newCandidate.getEmail();
	}
	
	public String positionTitle(){
		if(this.position==null)return"";
		return this.position.getTitle();
	}
	
	
	
	private boolean forbidden, currentCandidate;

	// 1ª Verificação
	public boolean forbidden(PositionEntity position){
		this.forbidden=this.currentCandidate=false;

		this.forbidden=position.getPositionManager().getEmail().equals(this.newCandidate.getEmail());

		return this.forbidden;
	}

	// 2ª Verificação
	public boolean candidate(PositionEntity position){
		
		if(!this.forbidden){
			this.currentCandidate = positionEJB.alreadyCandidateOfPosition(newCandidate, position);
			return this.currentCandidate;
		}
		return false;
	}

	// 3ª Verificação
	public boolean submitable(){
		return !this.forbidden&&!this.currentCandidate;
	}
}
