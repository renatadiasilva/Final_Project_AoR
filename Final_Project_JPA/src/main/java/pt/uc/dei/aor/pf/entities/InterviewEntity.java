package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "interviews")
@NamedQueries({
	@NamedQuery(name = "Interview.carriedOutInterviews",
			query = "SELECT i FROM InterviewEntity i WHERE i.carriedOut = TRUE AND i.date BETWEEN :date1 AND :date2"),
//	@NamedQuery(name="Song.songsOfUser",
//			query="SELECT s FROM Song s WHERE s.owner = :ownerId ORDER BY s.title"),
//	@NamedQuery(name="Song.songsOfUserOrderId",
//			query="SELECT s FROM Song s WHERE s.owner = :ownerId ORDER BY s.id"),
//	@NamedQuery(name="Song.songsByArtistTitle",
//			query="SELECT s FROM Song s WHERE UPPER(s.title) LIKE :t AND UPPER(s.artist) LIKE :a ORDER BY s.title"),
//	@NamedQuery(name="Song.findSongById",
//			query="SELECT s FROM Song s WHERE s.id = :id"),
//	@NamedQuery(name="Song.songsOfUserId",
//			query="SELECT s FROM Song s WHERE s.id = :id AND s.owner = :ownerId"),
})
public class InterviewEntity implements Serializable {

	private static final long serialVersionUID = -2959804497058358297L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "interviews_interviewers",
		joinColumns = @JoinColumn(name = "interview_id"),
		inverseJoinColumns = @JoinColumn(name = "interviewer_id"))
	@OrderBy("first_name")
	private List<UserEntity> interviewers;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "submission", nullable = false)
	private SubmissionEntity submission;

	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "carried_out")
	private boolean carriedOut;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "script")
	private ScriptEntity script;

	@OneToMany(mappedBy = "interview")
	private List<AnswerEntity> answers;

	@Column(name = "approved")
	private boolean approved;

	@Column(name = "feedback", length = 100)
	private String feedback;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "scheduled_by", nullable = false)
	private UserEntity scheduledBy;

	public InterviewEntity() {
	}

	public InterviewEntity(SubmissionEntity submission, Date date, 
			ScriptEntity script, UserEntity interviewScheduledBy) {
		this.submission = submission;
		this.date = date;
		this.script = script;
		this.scheduledBy = interviewScheduledBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public UserEntity getScheduledBy() {
		return scheduledBy;
	}

	public void setScheduledBy(UserEntity interviewScheduledBy) {
		this.scheduledBy = interviewScheduledBy;
	}

	public void addInterviewer(UserEntity user) {
		if (interviewers == null) interviewers = new ArrayList<UserEntity>();
		interviewers.add(user);
	}

	public void removeInterviewer(UserEntity user) {
		if (interviewers != null) interviewers.remove(user);
	}

}
