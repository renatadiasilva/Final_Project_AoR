package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "submissions")
@NamedQueries({
	@NamedQuery(name = "Submission.findSpontaneousSubmissions",
			query = "SELECT s FROM SubmissionEntity s WHERE s.spontaneous = TRUE"
					+ " ORDER BY s.date DESC"),
	@NamedQuery(name = "Submission.findSubmissionsByDate",
			query = "SELECT s FROM SubmissionEntity s WHERE "
					+ " s.date BETWEEN :date1 AND :date2"),
	@NamedQuery(name = "Submission.findSpontaneousSubmissionsByDate",
			query = "SELECT s FROM SubmissionEntity s WHERE "
					+ " s.spontaneous = TRUE AND s.date BETWEEN :date1 AND :date2"),
	@NamedQuery(name = "Submission.findRejectedSubmissionsByDate",
			query = "SELECT s FROM SubmissionEntity s WHERE UPPER(s.status) = 'REJECTED'"
					+ " AND s.date BETWEEN :date1 AND :date2"),
	@NamedQuery(name = "Submission.findPresentedProposalsByDate",
			query = "SELECT s FROM SubmissionEntity s WHERE UPPER(s.status) = '%PROPOSAL'"
					+ " AND s.date BETWEEN :date1 AND :date2"),
	@NamedQuery(name = "Submission.findSubmissionsBySource",
			query = "SELECT s FROM SubmissionEntity s JOIN s.sources so WHERE so = :source"
					+ " AND s.date BETWEEN :date1 AND :date2"),
//			query = "SELECT s SubmissionEntity s WHERE :source MEMBER OF s.sources"), //(TESTAR)
	@NamedQuery(name = "Submission.findSubmissionByPosition",
			query = "SELECT s FROM SubmissionEntity s WHERE s.position = :id"),
})
public class SubmissionEntity implements Serializable {

	private static final long serialVersionUID = -2164233391673103244L;
	
	public static final String STATUS_SUBMITED  = "Submited";
	public static final String STATUS_REJECTED  = "Rejected Submission";
	public static final String STATUS_ACCEPTED  = "Accepted to Interview";
	public static final String STATUS_SPROPOSAL = "Submited Proposal";  // automatismo????
	public static final String STATUS_RPROPOSAL = "Rejected Proposal";
	public static final String STATUS_APROPOSAL = "Accepted Proposal";
	public static final String STATUS_OPROPOSAL = "On Negotiation Proposal"; //"Offer Process (Negotiation)";
	public static final String STATUS_HIRED     = "Hired";
	public static final String STATUS_NOTHIRED  = "Not Hired"; 

	// outros??
	public static final String SOURCE_EXPRESSO = "EXPRESSO";
	public static final String SOURCE_LINKEDIN = "LINKEDIN";
	public static final String SOURCE_FACEBOOK = "FACEBOOK";
	public static final String SOURCE_NETEMPREGO = "NET.EMPREGO";

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "candidate", nullable = false)
	private UserEntity candidate;

	@ManyToOne
	@JoinColumn(name = "position")
	private PositionEntity position; 

	@Column(name = "spontaneous")
	private boolean spontaneous;

	@ManyToOne
	@JoinColumn(name = "associated_by")
	private UserEntity associatedBy;

	// Link
	@Column(name = "motivation_letter")
	private String motivationLetter;

	@ElementCollection
	@CollectionTable(name = "sources",
		joinColumns = @JoinColumn(name = "submission_id"))
	@Column(name = "source")
	private List<String> sources;

	@NotNull
	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@NotNull
	@Column(name = "status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
	@OrderBy("date")
	private List<InterviewEntity> interviews;

	@Column(name = "rejected_reason", length = 100)
	private String rejectReason;

	//hiredDate??
	
	public SubmissionEntity() {
	}

	public SubmissionEntity(UserEntity candidate,
			String motivationLetter, List<String> sources, boolean spontaneous) {
		this.date = new Date();  // yes??
		this.candidate = candidate;
		this.motivationLetter = motivationLetter;
		this.sources = sources;
		this.spontaneous = spontaneous;
		this.status = STATUS_SUBMITED;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getCandidate() {
		return candidate;
	}

	public void setCandidate(UserEntity candidate) {
		this.candidate = candidate;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public boolean isSpontaneous() {
		return spontaneous;
	}

	public void setSpontaneous(boolean spontaneous) {
		this.spontaneous = spontaneous;
	}

	public UserEntity getAssociatedBy() {
		return associatedBy;
	}

	public void setAssociatedBy(UserEntity associatedBy) {
		this.associatedBy = associatedBy;
	}

	public String getMotivationLetter() {
		return motivationLetter;
	}

	public void setMotivationLetter(String motivationLetter) {
		this.motivationLetter = motivationLetter;
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<InterviewEntity> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewEntity> interviews) {
		this.interviews = interviews;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

}