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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "interviews")
@NamedQueries({
	@NamedQuery(name = "Interview.findCarriedOutInterviews",
			query = "SELECT i FROM InterviewEntity i WHERE i.carriedOut = TRUE"
					+ " AND i.date BETWEEN :date1 AND :date2 ORDER BY i.date"),
	@NamedQuery(name = "Interview.findTotalCarriedOutInterviews",
			query = "SELECT COUNT(i) FROM InterviewEntity i"
					+ " WHERE i.carriedOut = TRUE"
					+ " AND i.date BETWEEN :date1 AND :date2"),
	@NamedQuery(name = "Interview.findCarriedOutInterviewsByUser",
			query = "SELECT i FROM InterviewEntity i JOIN i.interviewers u "
					+ "WHERE i.carriedOut = TRUE AND u = :user"
					+ " ORDER BY i.date DESC"),
	@NamedQuery(name = "Interview.findScheduledInterviewsByUser",
			query = "SELECT i FROM InterviewEntity i JOIN i.interviewers u "
					+ "WHERE i.carriedOut = FALSE AND u = :user"
					+ " ORDER BY i.date"),
	@NamedQuery(name = "Interview.findScheduledInterviewsByCandidate",
			query = "SELECT i FROM InterviewEntity i JOIN i.submission s "
					+ "WHERE i.carriedOut = FALSE AND s.candidate = :candidate"
					+ " ORDER BY i.date"),
	@NamedQuery(name = "Interview.findCarriedOutInterviewsByCandidate",
			query = "SELECT i FROM InterviewEntity i JOIN i.submission s "
					+ "WHERE i.carriedOut = TRUE AND s.candidate = :candidate"
					+ " ORDER BY i.date DESC"),
	@NamedQuery(name = "Interview.findByDateAndInterviewer",
			query = "SELECT i FROM UserEntity u JOIN u.interviews i "
					+ "WHERE u = :user AND i.date = :date"),
	@NamedQuery(name = "Interview.findByDateAndCandidate",
			query = "SELECT i FROM InterviewEntity i JOIN i.submission s"
					+ " WHERE i.date = :date AND s.candidate = :candidate "
					+ "ORDER BY i.date"),
	@NamedQuery(name = "Interview.findInterviewByPosition",
			query = "SELECT i FROM InterviewEntity i JOIN i.submission s "
					+ " WHERE s.position = :position ORDER BY i.date"),
	@NamedQuery(name = "Interview.findCarriedOutInterviewByPosition",
			query = "SELECT i FROM InterviewEntity i JOIN i.submission s "
					+ " WHERE i.carriedOut = TRUE AND s.position = :position"
					+ " ORDER BY i.date"),
	@NamedQuery(name = "Interview.findScheduledInterviewByPosition",
			query = "SELECT i FROM InterviewEntity i JOIN i.submission s "
					+ " WHERE i.carriedOut = FALSE AND s.position = :position"
					+ " ORDER BY i.date"),
	@NamedQuery(name = "Interview.findInterviewsOfUser",
			query = "SELECT i FROM InterviewEntity i JOIN i.interviewers u"
					+ " WHERE u = :user ORDER BY i.date"),
	@NamedQuery(name = "Interview.findInterviewsOfSubmission",
			query = "SELECT i FROM InterviewEntity i"
					+ " WHERE i.submission = :submission ORDER BY i.date"),
	@NamedQuery(name = "Interview.findInterviewsWithScript",
			query = "SELECT i FROM InterviewEntity i"
					+ " WHERE i.script = :script"),
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
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Column(name = "first")
	private boolean first;
	
	@Column(name = "carried_out")
	private boolean carriedOut;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "script")
	private ScriptEntity script;

	// has answers
	@Column(name = "answers", length = 100)
	private boolean answers;

	@Column(name = "feedback", length = 100)
	private String feedback;

	@NotNull
	@ManyToOne
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
		this.carriedOut = false;
		this.first = true;
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

	public boolean getAnswers() {
		return answers;
	}

	public void setAnswers(boolean answers) {
		this.answers = answers;
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
		if (!interviewers.contains(user)) interviewers.add(user);
	}

	public void removeInterviewer(UserEntity user) {
		if (interviewers != null) interviewers.remove(user);
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
	
	public String first3Interviewers() {
		String s = "";
		int limit = (int) Math.min(interviewers.size(), 3)-1;
		for(int i = 0; i < limit; i++)
			s += interviewers.get(i).getFirstName()+" "
					+interviewers.get(i).getLastName()+", ";
		s += interviewers.get(limit).getFirstName()+" "
				+interviewers.get(limit).getLastName();
		return s;
	}

}
