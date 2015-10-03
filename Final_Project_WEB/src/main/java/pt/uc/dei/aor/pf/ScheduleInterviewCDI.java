package pt.uc.dei.aor.pf;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementImp;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class ScheduleInterviewCDI implements Serializable {

	private static final long serialVersionUID = -1929597378229479373L;

	private static final Logger log = LoggerFactory.getLogger(ScheduleInterviewCDI.class);

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private ScriptEJBInterface scriptEJB;

	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private SecureMailManagementImp mail;

	private List<PositionEntity>positions;

	private List<UserEntity>interviewers, selectedInterviewers;

	private List<SubmissionEntity>submissions;

	private SubmissionEntity submission;

	private PositionEntity position;

	private List<InterviewEntity> interviews;

	private Date interviewDate;

	private List<ScriptEntity> scripts;

	private ScriptEntity selectedScript;

	private List<String>conflicts;

	private String keyword, emptyTable, headerTable;
	
	private UserEntity manager;

	public void enterManager() {
		manager = userEJB.find(userManagement.getCurrentUserClone().getId());
		positions = positionEJB.findOpenPositionsManagedByUser(manager);
		setEmptyTable("Não é gestor de nenhuma posição neste momento ou "
				+ "não exitem posições que verifiquem a pesquisa.");
		setHeaderTable("Posições a gerir por "
				+ userManagement.getUserFullName());
		cleanBean();
		position = null;
	}
	
	public void enterAdmin() {
		manager = null;
		this.positions=this.positionEJB.findOpenPositions();
		setEmptyTable("Não existem posições ou "
				+ "não exitem posições que verifiquem a pesquisa.");
		setHeaderTable("Todas as posições");
		cleanBean();
		position = null;		
	}

	public List<SubmissionEntity> getSubmissions() {

		if(this.position!=null){
			// Vai buscar as candidaturas à posição
			this.submissions=this.submissionEJB.findSubmissionsOfPosition(this.position);

			if(this.submissions!=null&&!this.submissions.isEmpty())
				for(int i=0; i<this.submissions.size(); i++)
					// Só mostra sem proposta e sem contrato
					if(this.submissions.get(i).getProposalDate()!=null&&this.submissions.get(i).getHiredDate()!=null
					&&!this.submissions.get(i).getStatus().equals(Constants.STATUS_REJECTED))
						this.submissions.remove(i);

			//			if(this.submissions!=null&&!this.submissions.isEmpty())
			//				for(SubmissionEntity submission:this.submissions)
			//					// Só mostra sem proposta e sem contrato
			//					if(submission.getProposalDate()!=null&&submission.getHiredDate()!=null
			//					&&!submission.getStatus().equals(Constants.STATUS_REJECTED))
			//						this.submissions.remove(submission);
		}

		return submissions;
	}

	public void setSubmissions(List<SubmissionEntity> submissions) {
		this.submissions = submissions;
	}

	public void unloadPosition(){
		this.position=null;
		this.cleanBean();
	}

	public void loadSubmission(SubmissionEntity submission){
		this.submission=submission;
		this.interviewDate=null;
		this.interviews=this.interviewEJB.findInterviewsOfSubmission(this.submission);
		this.selectedInterviewers=null;
		this.buildInterviewersList();
	}

	public void unloadSubmission(){
		this.submission=null;
		this.interviewDate=null;
		this.interviewers=null;
		this.selectedScript=null;
		this.conflicts=null;
		this.selectedInterviewers=null;
	}

	public boolean loadedPosition(){
		if(this.position==null)
			return false;

		return this.position.getStatus().equals(Constants.STATUS_OPEN);
	}

	public boolean loadedSubmission(){
		return this.submission!=null;
	}

	public boolean currentSubmission(SubmissionEntity submission){
		if(this.submission==null)return false;
		return this.submission.getId()==submission.getId();
	}

	public boolean hasInterviews(SubmissionEntity submission){
		if(submission.getInterviews()==null) return false;
		return this.interviews!=null&&!this.interviews.isEmpty();
	}

	public boolean currentSubmissionHasInterviews(){
		if(this.submission==null)return false;
		if(this.submission.getInterviews()==null) return false;
		return !this.submission.getInterviews().isEmpty();
	}

	public SubmissionEntity getSubmission() {
		return submission;
	}

	public void setSubmission(SubmissionEntity submission) {
		this.submission = submission;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
		this.cleanBean();
	}

	public List<InterviewEntity> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewEntity> interviews) {
		this.interviews = interviews;
	}

	public Date minDate(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate=interviewDate;
	}

	public boolean hasDate(){
		return this.interviewDate!=null;
	}

	public void buildInterviewersList(){

		this.interviewers=this.userEJB.findAllInterviewers();

		UserEntity currentUser=this.userEJB.findUserByEmail(this.userManagement.getUserMail());
		UserEntity manager=this.position.getPositionManager();
		UserEntity candidate=this.submission.getCandidate();

		// O user actual é o MANAGER da posição
		if(currentUser.getId()==manager.getId()){

			this.unloadInterviewer(manager);

			// Se não for o candidato, vai para o cimo da lista
			if(manager.getId()!=candidate.getId())
				this.interviewers.add(0, manager);
		}

		else{
			// O user actual não é o MANAGER da posição, logo é ADMIN
			this.unloadInterviewer(currentUser);
			this.unloadInterviewer(manager);

			// Se não for o candidato, o MANAGER vai para o cimo da lista
			if(manager.getId()!=candidate.getId())
				this.interviewers.add(0, manager);

			// Se não for o candidato, o ADMIN vai para o cimo da lista
			if(currentUser.getId()!=candidate.getId())
				this.interviewers.add(0, currentUser);

		}

	}

	private void unloadInterviewer(UserEntity interviewer) {
		for(int i=0; i<this.interviewers.size(); i++)
			if(this.interviewers.get(i).getId()==interviewer.getId()){
				this.interviewers.remove(i);
				break;
			}
	}

	public List<UserEntity> getInterviewers() {
		return this.interviewers;
	}

	public void setInterviewers(List<UserEntity> interviewers) {
		this.interviewers = interviewers;
	}

	public void addInterviewer(UserEntity interviewer){
		if(this.scripts==null||this.scripts.isEmpty())
			this.scripts=this.scriptEJB.findReusableScripts();
		if(this.selectedInterviewers==null)
			this.selectedInterviewers=new ArrayList<UserEntity>();
		this.selectedInterviewers.add(interviewer);
		this.buildConflicts();
	}

	public void removeInterviewer(UserEntity interviewer){
		this.selectedInterviewers.remove(interviewer);
	}

	public boolean selectedInterviewer(UserEntity interviewer){
		if(this.selectedInterviewers==null||this.selectedInterviewers.isEmpty())
			return false;

		return this.selectedInterviewers.contains(interviewer);
	}

	public void createInterview(){
		if(this.selectedScript==null)this.selectedScript=this.position.getDefaultScript();

		if(this.selectedInterviewers==null||this.selectedInterviewers.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Escolha o(s) entrevistador(es)", ""));
		else{
			//if this is not the first interview of the submission set first to false
			List<InterviewEntity> list = interviewEJB.findInterviewsOfSubmission(submission);
			boolean notFirst = false;
			if (list != null && interviews.size() > 0) notFirst = true; 

			InterviewEntity newInterview=new InterviewEntity(this.submission, this.interviewDate, this.selectedScript, this.userEJB.findUserByEmail(this.userManagement.getUserMail()));
			newInterview.setInterviewers(this.selectedInterviewers);
			this.interviewEJB.save(newInterview);
			
			if (notFirst) {
				newInterview.setFirst(false);
				interviewEJB.update(newInterview);
            }
			
			// change submission status to ACCEPTED
			submission.setStatus(Constants.STATUS_ACCEPTED);
			submissionEJB.update(submission);
				
			// send mail to interviewers and candidate
			this.mail.notifyScheduledInterview(newInterview);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova entrevista agendada."));
			this.cleanBean();
		}
	}

	public boolean hasSelectedInterviewers(){
		if(this.selectedInterviewers==null)
			return false;
		return !this.selectedInterviewers.isEmpty();
	}

	public List<ScriptEntity> getScripts() {
		return scripts;
	}

	public void setScripts(List<ScriptEntity> scripts) {
		this.scripts = scripts;
	}

	public void loadScript(ScriptEntity selectedScript){
		this.selectedScript=selectedScript;
	}

	public void unloadScript(){
		this.selectedScript=null;
	}

	public boolean currentScript(ScriptEntity script){
		if(this.selectedScript==null)return false;
		return this.selectedScript.getId()==script.getId();
	}

	public boolean hasScript(){
		return this.selectedScript!=null;
	}

	private void cleanBean() {
		this.keyword="";
		this.interviewDate=null;

		this.selectedScript=null;

		this.interviewers = null;
		this.selectedInterviewers = null;
		this.submissions = null;
		this.interviews = null;
//		if(this.interviewers!=null)
//			this.interviewers.clear();
//
//		if(this.selectedInterviewers!=null)
//			this.selectedInterviewers.clear();
//
//		if(this.submissions!=null)
//			this.submissions.clear();

		this.submission=null;

//		if(this.interviews!=null)
//			this.interviews.clear();

		this.conflicts=null;
		
	}

	public boolean canSubmit(){
		return this.hasSelectedInterviewers()&&this.hasDate()&&this.conflicts.isEmpty();
	}

	public void buildConflicts(){
		if(this.conflicts==null)
			this.conflicts=new ArrayList<>();
		else this.conflicts.clear();

		if (this.interviewDate != null && this.submission != null)
		if(this.interviewEJB.candidateHasDateConflict(this.interviewDate, this.submission.getCandidate()))
			this.conflicts.add("O candidato tem um conflito de agenda");			

		for(UserEntity interviewer:this.selectedInterviewers)
			if(this.interviewEJB.interviewerHasDateConflict(this.interviewDate, interviewer))
				this.conflicts.add("O entrevistador "+interviewer.getFirstName()+" "+interviewer.getLastName()+" tem um conflito de agenda");
	}

	public List<String> getConflicts() {
		return conflicts;
	}

	public void setConflicts(List<String> conflicts) {
		this.conflicts = conflicts;
	}

	public boolean checkPosition(PositionEntity position){
		if(this.position==null)return false;

		return this.position.getId()==position.getId();
	}
	
	public void searchAllPositions(){
		this.positions=this.positionEJB.findOpenPositions();
		this.position=null;
	}
	
	public void searchByKeyword(){
		this.position=null;
		String pattern = SearchPattern.preparePattern(keyword);
		this.positions=this.positionEJB.findPositionsByKeyword(pattern);
	}

	public void searchPositionsManagedByUser(long idM){
		this.position=null;
		log.info("Searching for positions by manager");
		log.debug("Id "+idM);
		UserEntity manager = userEJB.find(idM);
		if (manager != null && manager.getRoles().contains("MANAGER")) {
			log.debug("Manager "+manager.getFirstName());
			this.positions = positionEJB.findPositionsManagedByUser(manager);
		} else log.error("No manager with id "+idM);
	}

	public void searchPositionsByKeywordAndManager(Long idM) {
		this.position=null;
		log.info("Searching for positions of manager by keyword");
		log.debug("Id "+idM);
		UserEntity manager = userEJB.find(idM);
		if ( (manager != null) && (manager.getRoles().contains("MANAGER")) ) {
			log.debug("Manager "+manager.getFirstName());
			String pattern = SearchPattern.preparePattern(keyword);
			log.debug("Internal search string: "+pattern);
			this.positions = positionEJB.findPositionsByKeywordAndManager(pattern,
					manager);
		} else log.error("No manager with id "+idM);
	}

	public void searchPositionsByKeyword() {
		this.position=null;
		this.submission=null;
		String pattern = SearchPattern.preparePattern(keyword);
		this.positions = positionEJB.findOpenPositionsByKeyword(pattern,
				manager);
	}
	
	private boolean weekEndDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(interviewDate);
		return c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
			|| c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}
	
	private boolean notWorkHour() {
		Calendar c = Calendar.getInstance();
		c.setTime(interviewDate);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return hour < Constants.MIN_WORK_HOUR
				|| hour >= Constants.MAX_WORK_HOUR;
	}
	
	public boolean dateProbablyNotOk() {
		if (interviewDate==null) return false;
		return weekEndDay() || notWorkHour();
	}

	public List<PositionEntity> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionEntity> positions) {
		this.positions = positions;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public UserEntity getManager() {
		return manager;
	}

	public void setManager(UserEntity manager) {
		this.manager = manager;
	}

	public String getEmptyTable() {
		return emptyTable;
	}

	public void setEmptyTable(String emptyTable) {
		this.emptyTable = emptyTable;
	}

	public String getHeaderTable() {
		return headerTable;
	}

	public void setHeaderTable(String headerTable) {
		this.headerTable = headerTable;
	}

}
