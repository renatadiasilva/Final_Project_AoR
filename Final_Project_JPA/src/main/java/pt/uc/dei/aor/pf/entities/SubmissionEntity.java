package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="SubmissionEntity")
public class SubmissionEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2164233391673103244L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="candidate")
	private UserEntity candidate;

	// Nullable
	// Se estiver vazia é uma candidatura espontanea
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="position")
	private PositionEntity position;

	@Column(name="spontaneous")
	private boolean spontaneous;

	// Nullable
	@ManyToOne
	@JoinColumn(name="associatedBy")
	private UserEntity associatedBy;

	// Link
	@Column(name="motivationLetter")
	private String motivationLetter;

	@Column(name="linkedin")
	private String linkedin;

	@ElementCollection
	private List<String> source;

	@Column(name="date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date date;

	@Column(name="status")
	private String status;

	// entrevistas da submissão
	@OneToMany(mappedBy="submission", cascade=CascadeType.ALL)
	private List<InterviewEntity>interviews;

	// Nullable
	@Column(name="accepted", nullable = true)
	private boolean accepted;

	// Nullable
	@Column(name="proposal", nullable = true)
	private String proposal;

	// Nullable
	@Column(name="hired", nullable = true)
	private boolean hired;

	public SubmissionEntity() {
	}

	public SubmissionEntity(UserEntity candidate, PositionEntity position,
			String motivationLetter, List<String> source, boolean spontaneous) {
		this.candidate = candidate;
		this.position = position;
		this.motivationLetter = motivationLetter;
		this.source = source;
		this.spontaneous = spontaneous;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public List<String> getSource() {
		return source;
	}

	public void setSource(List<String> source) {
		this.source = source;
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

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String getProposal() {
		return proposal;
	}

	public void setProposal(String proposal) {
		this.proposal = proposal;
	}

	public boolean isHired() {
		return hired;
	}

	public void setHired(boolean hired) {
		this.hired = hired;
	}
	
}
