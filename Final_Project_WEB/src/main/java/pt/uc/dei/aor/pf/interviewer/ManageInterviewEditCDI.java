package pt.uc.dei.aor.pf.interviewer;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class ManageInterviewEditCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -307969252871726306L;
	
	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private ScriptEJBInterface scriptEJB;
	
	@EJB
	private InterviewEJBInterface interviewEJB;
	
	private SubmissionEntity submission;
	
	private PositionEntity position;
	
	private InterviewEntity interview;
	
	private UserEntity currentUser, candidate;

	private List<InterviewEntity> candidateInterviews;
	
	private List<UserEntity> interviewers, selectedInterviewers;

	private List<ScriptEntity> scripts;
	
	private ScriptEntity script;

	private List<String> conflicts;

	private ScriptEntity interviewScript;
	
	private Date interviewDate;

	
	public void loadInterview(InterviewEntity interview){
		this.unloadScript();
		
		this.interview=interview;
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

	public void unloadScript(){
		this.script=null;
	}

	private void buildInterviewersList(){

		this.interviewers=this.userEJB.findAllInterviewers();

		UserEntity manager=this.position.getPositionManager();
		UserEntity candidate=this.interview.getSubmission().getCandidate();
		
		// Se é ADMIN sai para voltar a entrar em cima mais à frente
		if(this.currentUser.getRoles().contains(Constants.ROLE_ADMIN))
			this.unloadInterviewer(this.currentUser);

		// O user actual é o MANAGER da posição
		if(currentUser.getId()==manager.getId()){

			this.unloadInterviewer(manager);

			// Se não for o candidato, vai para o cimo da lista
			if(manager.getId()!=candidate.getId())
				this.interviewers.add(0, manager);
		}

		else{
			// O user actual não é o MANAGER da posição, logo é ADMIN
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
		for(int i=0; i<this.selectedInterviewers.size(); i++)
			if(this.selectedInterviewers.get(i).getId()==interviewer.getId()){
				this.selectedInterviewers.remove(i);
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

	public void updateInterview(){
		this.buildConflicts();
		System.out.println("update interview");
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
}
