package pt.uc.dei.aor.pf.interviewer;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.upload.UploadFile;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class ManageInterviewScriptCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7870689217366569492L;
	
	@EJB
	private QuestionEJBInterface questionEJB;
	
	@EJB
	private InterviewEJBInterface interviewEJB;
	
	private InterviewEntity interview;
	
	private SubmissionEntity submission;
	
	private UserEntity candidate;
	
	private PositionEntity position;
	
	private ScriptEntity script;
	
	private List<InterviewEntity>candidateInterviews;
	
	private String interviewFeedback;
	
	public void loadInterview(InterviewEntity interview){
		this.interview=interview;
		this.script=interview.getScript();
		this.submission=interview.getSubmission();
		this.position=this.submission.getPosition();
		this.candidate=this.submission.getCandidate();
		this.candidateInterviews=this.interviewEJB.findCarriedOutInterviewsByCandidate(this.candidate);
		
		if(this.candidateInterviews==null)
			this.candidateInterviews=new ArrayList<>();
	}
	
	public void unloadInterview(){
		this.interview=null;
		this.script=null;
		this.submission=null;
		this.candidate=null;
		this.position=null;
	}
	
	public boolean currentScript(ScriptEntity script){
		if(this.script==null)return false;
		return this.script.getId()==script.getId();
	}
	
	public boolean hasScript(){
		return this.script!=null;
	}

	public ScriptEntity getScript() {
		return script;
	}

	public void setScript(ScriptEntity script) {
		this.script = script;
	}
	
	public String interviewTitle(){
		SimpleDateFormat dateFormat=new SimpleDateFormat("MMdd");
		return this.position.getPositionCode()+"_"
		+this.candidate.getFirstName()+"_"+this.candidate.getLastName()
		+"_"+dateFormat.format(this.interview.getDate());
	}
	
	public String questionType(QuestionEntity question){
		return this.questionEJB.getTypeText(question);
	}

	public List<InterviewEntity> getCandidateInterviews() {
		return candidateInterviews;
	}

	public void setCandidateInterviews(List<InterviewEntity> candidateInterviews) {
		this.candidateInterviews = candidateInterviews;
	}
	
	public String displayDate(Date date){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return dateFormat.format(date);
	}
	
	public void loadPosition(PositionEntity position){
		this.position=position;
	}
	
	public String getInterviewFeedback() {
		return interviewFeedback;
	}

	public void setInterviewFeedback(String interviewFeedback) {
		this.interviewFeedback = interviewFeedback;
	}
	
	public String interviewXLSPath(InterviewEntity interview){
		// (.xls)
		HttpServletRequest request = (HttpServletRequest) FacesContext.
				getCurrentInstance().getExternalContext().getRequest();
		return request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+"/"+UploadFile.FOLDER_INTERVIEW_RESULT+"/"
		+interview.getId()+UploadFile.DOCUMENT_EXTENSION_XLS;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

}
