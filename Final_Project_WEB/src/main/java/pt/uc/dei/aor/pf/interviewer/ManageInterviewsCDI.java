package pt.uc.dei.aor.pf.interviewer;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class ManageInterviewsCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6287260765364497687L;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private QuestionEJBInterface questionEJB;

	private List<InterviewEntity> interviews;

	private List<InterviewEntity> carriedInterviews;

	private InterviewEntity interview;
	
	private PositionEntity position;
	
	private SubmissionEJBInterface submissionEJB;
	
	private SubmissionEntity submission;

	private boolean admin, manager, interviewer;

	private UserEntity currentUser, candidate;
	
	private ScriptEntity script;

	public void loadRole(String userEmail){
		this.currentUser=this.userEJB.findUserByEmail(userEmail);

		this.admin=this.manager=this.interviewer=false;

		if(this.currentUser.getRoles().contains(Constants.ROLE_ADMIN))
			this.admin=true;
		else if(this.currentUser.getRoles().contains(Constants.ROLE_MANAGER))
			this.manager=true;
		else if(this.currentUser.getRoles().contains(Constants.ROLE_INTERVIEWER))
			this.interviewer=true;

		if(this.interviews==null&&this.carriedInterviews==null)
			this.loadInterviews();
	}

	private void loadInterviews() {
		this.interviews=new ArrayList<>();
		this.carriedInterviews=new ArrayList<>();
		
		if(this.admin){
			// Carrega todas as entrevistas, ouch!
			List<PositionEntity>managedPositions=this.positionEJB.findAll();

			for(PositionEntity position:managedPositions){
				this.interviews.addAll(this.interviewEJB.findScheduledInterviewsByPosition(position));
				this.carriedInterviews.addAll(this.interviewEJB.findCarriedOutInterviewsByPosition(position));
			}
		}

		if(this.manager){
			// Carrega todas as entrevistas de posições do manager
			List<PositionEntity>managedPositions=this.positionEJB.findOpenPositionsManagedByUser(currentUser);

			for(PositionEntity position:managedPositions){
				this.interviews.addAll(this.interviewEJB.findScheduledInterviewsByPosition(position));
				this.carriedInterviews.addAll(this.interviewEJB.findCarriedOutInterviewsByPosition(position));
			}

		}

		if(this.interviewer){
			// Carrega todas as entrevistas do interviewer
			this.interviews=this.interviewEJB.findInterviewsOfUser(currentUser);
			this.carriedInterviews=this.interviewEJB.findCarriedOutInterviewsByUser(currentUser);
		}

	}

	public List<InterviewEntity> getInterviews() {
		return interviews;
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
		this.interview=interview;
		this.script=interview.getScript();
		this.position=interview.getSubmission().getPosition();
		this.candidate=interview.getSubmission().getCandidate();
	}

	public void unloadInterview(){
		this.interview=null;
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

}
