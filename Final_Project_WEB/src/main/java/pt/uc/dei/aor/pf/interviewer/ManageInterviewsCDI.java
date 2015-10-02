package pt.uc.dei.aor.pf.interviewer;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class ManageInterviewsCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6287260765364497687L;

	@Inject
	private UploadFile uploadFile;

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	@EJB
	private QuestionEJBInterface questionEJB;

	@EJB
	private ScriptEJBInterface scriptEJB;

	private List<InterviewEntity> interviews;

	private List<InterviewEntity> carriedInterviews;

	private List<InterviewEntity> candidateInterviews;

	private InterviewEntity interview;

	private PositionEntity position;

	private SubmissionEntity submission;

	private boolean admin, manager, interviewer;

	private boolean editInterview;

	private UserEntity currentUser, candidate;

	private ScriptEntity script, defaultInterviewScript;

	private String interviewFeedback;

	private Date interviewDate;

	public void loadRole(String userEmail, boolean carried){

		this.currentUser=this.userEJB.findUserByEmail(userEmail);

		this.admin=this.manager=this.interviewer=false;

		if(this.currentUser.getRoles().contains(Constants.ROLE_ADMIN))
			this.admin=true;
		else if(this.currentUser.getRoles().contains(Constants.ROLE_MANAGER))
			this.manager=true;
		else if(this.currentUser.getRoles().contains(Constants.ROLE_INTERVIEWER))
			this.interviewer=true;

		if(!carried)
			this.loadInterviews();

		if(carried)
			this.loadCarriedInterviews();
	}

	private void loadInterviews() {
		this.interviews=new ArrayList<>();

		if(this.admin){
			// Carrega todas as entrevistas, ouch!
			List<PositionEntity>managedPositions=this.positionEJB.findAll();

			for(PositionEntity position:managedPositions)
				this.interviews.addAll(this.interviewEJB.findScheduledInterviewsByPosition(position));
		}

		if(this.manager){
			// Carrega todas as entrevistas de posições do manager
			List<PositionEntity>managedPositions=this.positionEJB.findOpenPositionsManagedByUser(currentUser);

			for(PositionEntity position:managedPositions)
				this.interviews.addAll(this.interviewEJB.findScheduledInterviewsByPosition(position));

		}

		if(this.interviewer)
			// Carrega todas as entrevistas do interviewer
			this.interviews=this.interviewEJB.findInterviewsOfUser(currentUser);

	}

	private void loadCarriedInterviews() {
		this.carriedInterviews=new ArrayList<>();

		if(this.admin){
			// Carrega todas as entrevistas, ouch!
			List<PositionEntity>managedPositions=this.positionEJB.findAll();

			for(PositionEntity position:managedPositions)
				this.carriedInterviews.addAll(this.interviewEJB.findCarriedOutInterviewsByPosition(position));
		}

		if(this.manager){
			// Carrega todas as entrevistas de posições do manager
			List<PositionEntity>managedPositions=this.positionEJB.findOpenPositionsManagedByUser(currentUser);

			for(PositionEntity position:managedPositions)
				this.carriedInterviews.addAll(this.interviewEJB.findCarriedOutInterviewsByPosition(position));

		}

		if(this.interviewer)
			// Carrega todas as entrevistas do interviewer
			this.carriedInterviews=this.interviewEJB.findCarriedOutInterviewsByUser(currentUser);

	}

	public List<InterviewEntity> getInterviews() {
		return interviews;
	}

	public boolean currentInterview(InterviewEntity interview){
		System.out.println("THIS id: "+this.interview.getId());
		System.out.println("ITERATED id: "+interview.getId());
		if(this.interview==null)return false;
		return this.interview.getId()==interview.getId();
	}

	public void setInterviews(List<InterviewEntity> interviews) {
		this.interviews = interviews;
	}

	public List<InterviewEntity> getCarriedInterviews() {
		return carriedInterviews;
	}

	public void setCarriedInterviews(List<InterviewEntity> carriedInterviews) {
		this.carriedInterviews = carriedInterviews;
	}

	public void loadPosition(PositionEntity position){
		this.position=position;
	}

	public void loadSubmission(InterviewEntity interview){
		this.position=interview.getSubmission().getPosition();
		this.candidate=interview.getSubmission().getCandidate();
		this.interview=interview;
		this.candidateInterviews=this.interviewEJB.findCarriedOutInterviewsByCandidate(candidate);
	}

	public void loadCandidate(InterviewEntity interview){
		this.candidate=interview.getSubmission().getCandidate();
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public void loadInterview(InterviewEntity interview){
		this.cancelConcludeInterview();

		this.editInterview=true;

		this.unloadScript();

		this.defaultInterviewScript=interview.getScript();

		this.interview=interview;

		System.out.println(this.interviews.size());

		for(InterviewEntity i:this.interviews)
			System.out.println(i);

		this.interviewDate=interview.getDate();
		this.interviewScript=interview.getScript();
		this.submission=interview.getSubmission();
		this.position=interview.getSubmission().getPosition();
		this.candidate=interview.getSubmission().getCandidate();

		this.scripts=this.scriptEJB.findReusableScripts();

		this.candidateInterviews=this.interviewEJB.findCarriedOutInterviewsByCandidate(candidate);

		this.selectedInterviewers=interview.getInterviewers();

		this.buildInterviewersList();

		this.interviewScript=interview.getScript();

		this.buildConflicts();
	}

	public void unloadInterview(){
		this.cancelConcludeInterview();
		this.editInterview=false;
		this.submission=null;
		this.conflicts=null;
		this.interview=null;
		this.selectedInterviewers=null;
	}

	public ScriptEntity getScript() {
		return script;
	}

	public void loadScript(ScriptEntity script) {
		this.script = script;
	}

	public void unloadScript(){
		this.script=null;
	}

	public boolean hasScript(){
		return this.script!=null;
	}

	public boolean currentScript(ScriptEntity script){
		if(this.script==null)return false;
		return this.script.getId()==script.getId();
	}

	public String questionType(QuestionEntity question){
		return this.questionEJB.getTypeText(question);
	}

	public boolean isInterviewer() {
		return interviewer;
	}

	public void setInterviewer(boolean interviewer) {
		this.interviewer = interviewer;
	}

	public String interviewTitle(){
		SimpleDateFormat dateFormat=new SimpleDateFormat("MMdd");
		return this.position.getPositionCode()+"_"
		+this.candidate.getFirstName()+"_"+this.candidate.getLastName()
		+"_"+dateFormat.format(this.interview.getDate());
	}

	public String displayDate(Date date){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return dateFormat.format(date);
	}

	public List<InterviewEntity> getCandidateInterviews() {
		return candidateInterviews;
	}

	public void setCandidateInterviews(List<InterviewEntity> candidateInterviews) {
		this.candidateInterviews = candidateInterviews;
	}

	public boolean isEditInterview() {
		if(this.interviewer) return false;
		return editInterview;
	}

	public void setEditInterview(boolean editInterview) {
		if(this.conflicts!=null)this.conflicts.clear();
		this.editInterview = editInterview;
	}

	public String getInterviewFeedback() {
		return interviewFeedback;
	}

	public void setInterviewFeedback(String interviewFeedback) {
		this.interviewFeedback = interviewFeedback;
	}




	// Edição de Entrevista
	private List<UserEntity> interviewers, selectedInterviewers;

	private List<ScriptEntity> scripts;

	private List<String> conflicts;

	private ScriptEntity interviewScript;

	private void buildInterviewersList(){

		this.interviewers=this.userEJB.findAllInterviewers();

		UserEntity currentUser=this.userEJB.findUserByEmail(this.userManagement.getUserMail());
		UserEntity manager=this.position.getPositionManager();
		UserEntity candidate=this.interview.getSubmission().getCandidate();

		this.unloadInterviewer(manager);
		this.unloadInterviewer(currentUser);

		// Se é ADMIN sai para voltar a entrar em cima mais à frente
		if(this.currentUser.getRoles().contains(Constants.ROLE_ADMIN))
			this.unloadInterviewer(this.currentUser);

		// O user actual é o MANAGER da posição
		if(currentUser.getId()==manager.getId()){

			// Se não for o candidato, vai para o cimo da lista
			if(manager.getId()!=candidate.getId())
				this.interviewers.add(0, manager);
		}

		else{
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

	public void buildConflicts(){
		this.conflicts=new ArrayList<>();

		// Só verifica se a data tiver sido mudada
		if(!this.interviewDate.equals(this.interview.getDate()))
			if(this.interviewEJB.candidateHasDateConflict(this.interviewDate, this.submission.getCandidate()))
				this.conflicts.add("O candidato tem um conflito de agenda");			

		for(UserEntity interviewer:this.selectedInterviewers)
			// Verifica primeiro se é entrevistador da posição ou se a data foi mudada
			if(!this.interview.getInterviewers().contains(interviewer)||!this.interviewDate.equals(this.interview.getDate()))
				if(this.interviewEJB.interviewerHasDateConflict(this.interviewDate, interviewer))
					this.conflicts.add("O entrevistador "+interviewer.getFirstName()+" "+interviewer.getLastName()+" tem um conflito de agenda");
	}

	public void removeInterviewer(UserEntity interviewer){
		for(int i=0; i<this.selectedInterviewers.size(); i++)
			if(this.selectedInterviewers.get(i).getId()==interviewer.getId()){
				this.selectedInterviewers.remove(i);
				break;
			}
		this.buildConflicts();
	}

	public boolean selectedInterviewer(UserEntity interviewer){
		if(this.selectedInterviewers==null||this.selectedInterviewers.isEmpty())
			return false;

		for(int i=0; i<this.selectedInterviewers.size(); i++)
			if(this.selectedInterviewers.get(i).getId()==interviewer.getId())
				return true;

		return false;
	}

	public List<String> getConflicts() {
		return conflicts;
	}

	public void setConflicts(List<String> conflicts) {
		this.conflicts = conflicts;
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
		this.interviewDate = interviewDate;
	}

	public boolean canSubmit(){
		if(this.conflicts==null)return false;
		return this.conflicts.isEmpty();
	}

	public List<ScriptEntity> getScripts() {
		return scripts;
	}

	public void setScripts(List<ScriptEntity> scripts) {
		this.scripts = scripts;
	}

	public void loadInterviewScript(ScriptEntity interviewScript) {
		this.interviewScript = interviewScript;
	}

	public void unloadInterviewScript(){
		this.interviewScript=null;
	}

	public boolean hasInterviewScript(){
		return this.interviewScript!=null;
	}

	public boolean currentInterviewScript(ScriptEntity interviewScript){
		if(this.interviewScript==null)return false;
		if(this.interviewScript.getId()==this.position.getDefaultScript().getId()) return false;
		return this.interviewScript.getId()==interviewScript.getId();
	}

	public boolean defaultScript(ScriptEntity interviewScript){
		return this.interviewScript.getId()==this.position.getDefaultScript().getId();
	}

	public boolean showEditInterview(SubmissionEntity submission){
		if(this.interviewer)return false;
		if(this.submission==null)return false;
		return this.submission.getId()==submission.getId();
	}

	public void updateInterview(){
		this.buildConflicts();
		if(!this.conflicts.isEmpty()){
			this.interview.setInterviewers(this.selectedInterviewers);
			this.interview.setDate(this.interviewDate);

			if(this.interviewScript==null)
				this.interview.setScript(this.defaultInterviewScript);
			else this.interview.setScript(this.interviewScript);

			this.interviewEJB.update(this.interview);

			//Envia os mails aqui

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Entrevista Actualizada."));
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Existem conflitos de agenda.", ""));
	}

	public String interviewXLSPath(InterviewEntity interview) {
		// (.xls)
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		return request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+"/"+UploadFile.FOLDER_INTERVIEW_RESULT+"/"
		+interview.getId()+UploadFile.DOCUMENT_EXTENSION_XLS;
	}

	// Concluir Entrevista
	private InterviewEntity interviewToConclude;

	private UserEntity candidateToConclude;

	private String feedback;

	private UploadedFile file;

	public boolean hasInterviewToConclude(){
		return this.interviewToConclude!=null;
	}

	public void loadInterviewToConclude(InterviewEntity interview){
		this.unloadInterview();
		this.interviewToConclude=interview;
		this.candidateToConclude=interview.getSubmission().getCandidate();
		this.feedback="";
	}

	public void cancelConcludeInterview(){
		this.interviewToConclude=null;
	}

	public boolean checkInterviewToConclude(InterviewEntity interview){
		if(this.interviewToConclude==null)return false;
		return this.interviewToConclude.getId()==interview.getId();
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public void uploadInterview(FileUploadEvent event){

		if(!this.feedback.isEmpty()){

			this.file=event.getFile();

			this.uploadFile.uploadFile(this.file, UploadFile.FOLDER_INTERVIEW_RESULT, this.interviewToConclude.getId(), UploadFile.DOCUMENT_EXTENSION_XLS);

			this.interviewToConclude.setCarriedOut(true);
			
			this.interviewToConclude.setFeedback(this.feedback);

			this.interviewEJB.update(this.interviewToConclude);

			this.unloadInterview();

			this.loadInterviews();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Entrevista concluída."));

		}else
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Introduza o Feedback da entrevista."));


	}

	public String getCandidateToConcludeName() {
		return this.candidateToConclude.getFirstName()+" "+this.candidateToConclude.getLastName();
	}

	public String getSubmissionCode() {
		return this.interviewToConclude.getSubmission().getPosition().getPositionCode();
	}

}
