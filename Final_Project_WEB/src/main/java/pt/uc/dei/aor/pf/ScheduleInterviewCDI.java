package pt.uc.dei.aor.pf;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class ScheduleInterviewCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1929597378229479373L;

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	private List<UserEntity>interviewers, selectedInterviewers;

	private List<SubmissionEntity>submissions;

	private SubmissionEntity submission;

	private PositionEntity position;

	private List<InterviewEntity> interviews;

	private Date interviewDate;

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
	}

	public void loadSubmission(SubmissionEntity submission){
		this.submission=submission;
		this.interviews=this.interviewEJB.findInterviewsOfSubmission(this.submission);
		this.buildInterviewersList();
	}

	public void unloadSubmission(){
		this.submission=null;
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
	}

	public List<InterviewEntity> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewEntity> interviews) {
		this.interviews = interviews;
	}

	public Date minDate(){
		return new Date();
	}
	
	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		// FDP passou-se
		this.interviewDate=new Date();
//		this.interviewDate = interviewDate;
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
		if(this.selectedInterviewers==null)
			this.selectedInterviewers=new ArrayList<UserEntity>();
		this.selectedInterviewers.add(interviewer);
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
		System.out.println("Create Interview!");
		if(this.selectedInterviewers==null||this.selectedInterviewers.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Escolha o(s) entrevistador(es)", ""));
		else{
			InterviewEntity newInterview=new InterviewEntity(this.submission, this.interviewDate, this.position.getDefaultScript(), this.userEJB.findUserByEmail(this.userManagement.getUserMail()));
			newInterview.setInterviewers(this.selectedInterviewers);
			this.interviewEJB.save(newInterview);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova entrevista agendada."));
			this.cleanBean();
		}
	}

	private void cleanBean() {
		this.interviewers.clear();
		
		this.selectedInterviewers.clear();;

		this.submissions.clear();;

		this.submission=null;

		this.interviews.clear();

		this.interviewDate=null;
		
	}

}
