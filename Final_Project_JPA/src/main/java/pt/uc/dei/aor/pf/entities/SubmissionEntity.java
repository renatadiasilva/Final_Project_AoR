package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "submissions")
public class SubmissionEntity implements Serializable {

	private static final long serialVersionUID = -2164233391673103244L;

	// more??
	public static final String STATUS_SUBMITED = "Submited";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_ACCEPTED = "Accepted to Interview";
	public static final String STATUS_PROPOSAL = "Presented Proposal";
	public static final String STATUS_HIRED    = "Hired";
	public static final String STATUS_NOTHIRED = "Not Hired"; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "candidate", nullable = false)
	private UserEntity candidate;

	// Se a list estiver vazia é uma candidatura espontanea
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "submissions_positions",
			joinColumns = @JoinColumn(name = "submission_id"),
			inverseJoinColumns = @JoinColumn(name = "position_id"))
	private List<PositionEntity> positions;

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
	
	public SubmissionEntity() {
	}

	public SubmissionEntity(UserEntity candidate,
			String motivationLetter, List<String> sources, boolean spontaneous) {
		this.candidate = candidate;
		this.motivationLetter = motivationLetter;
		this.sources = sources;
		this.spontaneous = spontaneous;
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

	public List<PositionEntity> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionEntity> positions) {
		this.positions = positions;
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

	public void addPosition(PositionEntity position) {
		if (positions == null) positions = new ArrayList<PositionEntity>();
		positions.add(position);
	}
	
	public void removePosition(PositionEntity position) {
		if (positions != null) positions.remove(position);
	}
}