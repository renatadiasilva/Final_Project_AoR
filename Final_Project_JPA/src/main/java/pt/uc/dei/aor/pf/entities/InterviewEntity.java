package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="InterviewEntity")
public class InterviewEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2959804497058358297L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@ManyToMany(fetch=FetchType.EAGER)
	private List<UserEntity> interviewers;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="submission")
	private SubmissionEntity submission;

	@Column(name="date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date date;

	@Column(name="carriedOut", nullable = true)
	private boolean carriedOut;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="script", nullable = true)
	private ScriptEntity script;

	@OneToMany
	private List<AnswerEntity>answers;

	@Column(name="approved", nullable = true)
	private boolean approved;

	@Column(name="feedback", nullable = true)
	private String feedback;

	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="interviewScheduledBy")
	private UserEntity interviewScheduledBy;

	public InterviewEntity() {
	}

	public InterviewEntity(List<UserEntity> interviewers,
			SubmissionEntity submission, Date date, ScriptEntity script,
			UserEntity interviewScheduledBy) {
		this.interviewers = interviewers;
		this.submission = submission;
		this.date = date;
		this.script = script;
		this.interviewScheduledBy = interviewScheduledBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<UserEntity> getInterviewers() {
		return interviewers;
	}

	public void setInterviewers(List<UserEntity> interviewers) {
		this.interviewers = interviewers;
	}

	public SubmissionEntity getSubmission() {
		return submission;
	}

	public void setSubmission(SubmissionEntity submission) {
		this.submission = submission;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isCarriedOut() {
		return carriedOut;
	}

	public void setCarriedOut(boolean carriedOut) {
		this.carriedOut = carriedOut;
	}

	public ScriptEntity getScript() {
		return script;
	}

	public void setScript(ScriptEntity script) {
		this.script = script;
	}

	public List<AnswerEntity> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerEntity> answers) {
		this.answers = answers;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public UserEntity getInterviewScheduledBy() {
		return interviewScheduledBy;
	}

	public void setInterviewScheduledBy(UserEntity interviewScheduledBy) {
		this.interviewScheduledBy = interviewScheduledBy;
	}

}
