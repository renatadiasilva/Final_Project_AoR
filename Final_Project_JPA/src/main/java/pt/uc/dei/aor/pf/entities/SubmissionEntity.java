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
//DATE_PART
@NamedQueries({
	@NamedQuery(name = "Submission.findSpontaneousSubmissions",
			query = "SELECT s FROM SubmissionEntity s"
					+ " WHERE s.spontaneous = TRUE ORDER BY s.date DESC"),
	@NamedQuery(name = "Submission.findSubmissionsByDate",
			query = "SELECT s FROM SubmissionEntity s WHERE "
					+ " s.date BETWEEN :date1 AND :date2 ORDER BY s.date"),
	@NamedQuery(name = "Submission.findSpontaneousSubmissionsByDate",
			query = "SELECT s FROM SubmissionEntity s WHERE "
					+ " s.spontaneous = TRUE AND s.date BETWEEN :date1"
					+ " AND :date2 ORDER BY s.date"),
	@NamedQuery(name = "Submission.findRejectedSubmissionsByDate",
			query = "SELECT s FROM SubmissionEntity s WHERE s.status = :status"
					+ " AND s.date BETWEEN :date1 AND :date2 ORDER BY s.date"),
	@NamedQuery(name = "Submission.findPresentedProposalsByDate",
			query = "SELECT s FROM SubmissionEntity s"
					+ " WHERE s.proposalDate IS NOT NULL AND"
					+ " s.date BETWEEN :date1 AND :date2 ORDER BY s.date"),
	@NamedQuery(name = "Submission.countSubmissionsBySource",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE :source MEMBER OF s.sources"
					+ " AND s.date BETWEEN :date1 AND :date2 ORDER BY s.date"),
	@NamedQuery(name = "Submission.findSubmissionsOfPosition",
			query = "SELECT s FROM SubmissionEntity s "
					+ "WHERE s.position = :position ORDER BY s.date"),
	@NamedQuery(name = "Submission.findSubmissionsOfCandidate",
			query = "SELECT s FROM SubmissionEntity s"
					+ " WHERE s.candidate = :user"
					+ " AND s.spontaneous = FALSE ORDER BY s.date DESC"),
	@NamedQuery(name = "Submission.findSpontaneousSubmissionsOfCandidate",
			query = "SELECT s FROM SubmissionEntity s"
					+ " WHERE s.candidate = :user"
					+ " AND s.spontaneous = TRUE ORDER BY s.date DESC"),
	@NamedQuery(name = "Submission.countTotalSubmissionsPos",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.position.openingDate"
					+ " BETWEEN :date1 AND :date2"),
	@NamedQuery(name = "Submission.countTotalRejectedPos",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.position.openingDate"
					+ " BETWEEN :date1 AND :date2"
					+ " AND s.status = :rejected"),
	@NamedQuery(name = "Submission.countTotalProposalsPos",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.position.openingDate"
					+ " BETWEEN :date1 AND :date2"
					+ " AND s.proposalDate IS NOT NULL"),
	@NamedQuery(name = "Submission.countTotalSubmissions",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.date"
					+ " BETWEEN :date1 AND :date2"),
	@NamedQuery(name = "Submission.countTotalSpontaneous",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.date"
					+ " BETWEEN :date1 AND :date2"
					+ " AND s.spontaneous = TRUE"),
	@NamedQuery(name = "Submission.countTotalRejected",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.date"
					+ " BETWEEN :date1 AND :date2"
					+ " AND s.status = :rejected"),
	@NamedQuery(name = "Submission.countTotalProposals",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.date"
					+ " BETWEEN :date1 AND :date2"
					+ " AND s.proposalDate IS NOT NULL"),
	@NamedQuery(name = "Submission.countTotalHired",
			query = "SELECT COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.date"
					+ " BETWEEN :date1 AND :date2"
					+ " AND s.status = :hired"),
	@NamedQuery(name = "Submission.countSubmissionsByDate",
			query = "SELECT COUNT(s), s.date FROM SubmissionEntity s"
					+ " WHERE s.candidate = :user ORDER BY s.date"),
	@NamedQuery(name = "Submission.findDetailOfPosition",
			query = "SELECT s FROM SubmissionEntity s"
					+ " WHERE s.position = :position"
					+ " ORDER BY s.status, s.candidate.firstName"),
})
public class SubmissionEntity implements Serializable {

	private static final long serialVersionUID = -2164233391673103244L;
	
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

	@Column(name = "motivation_letter")
	private String motivationLetter;
	
	@Column(name="custom_cv", nullable = false)
	private boolean customCV;

	@ElementCollection
	@CollectionTable(name = "sources",
		joinColumns = @JoinColumn(name = "submission_id"))
	@Column(name = "source")
	private List<String> sources;

	@NotNull
	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@NotNull
	@Column(name = "status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
	@OrderBy("date")
	private List<InterviewEntity> interviews;

	@Column(name = "rejected_reason", length = 100)
	private String rejectReason;

	@Column(name = "hired_date")
	@Temporal(TemporalType.DATE)
	private Date hiredDate;

	@Column(name = "proposal_date")
	@Temporal(TemporalType.DATE)
	private Date proposalDate;

	public SubmissionEntity() {
	}

	public SubmissionEntity(UserEntity candidate, String status,
			String motivationLetter, List<String> sources,
			boolean spontaneous) {
		this.date = new Date();
		this.candidate = candidate;
		this.motivationLetter = motivationLetter;
		this.sources = sources;
		this.spontaneous = spontaneous;
		this.status = status;
		
		this.customCV = false;
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

	public boolean isCustomCV() {
		return customCV;
	}

	public void setCustomCV(boolean customCV) {
		this.customCV = customCV;
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

	public Date getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(Date hiredDate) {
		this.hiredDate = hiredDate;
	}

	public Date getProposalDate() {
		return proposalDate;
	}

	public void setProposalDate(Date proposalDate) {
		this.proposalDate = proposalDate;
	}

}