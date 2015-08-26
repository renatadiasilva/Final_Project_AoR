package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="UserEntity")
public class UserEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2911048822662162612L;


	public static final String ROLE_ADMIN="ADMIN";

	public static final String ROLE_MANAGER="MANAGER";

	public static final String ROLE_INTERVIEWER="INTERVIEWER";

	public static final String ROLE_CANDIDATE="CANDIDATE";


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@Column(name="email")
	private String email;

	@Column(name="password")
	private String password;

	@Column(name="firstName")
	private String firstName;

	@Column(name="lastName")
	private String lastName;

	@Column(name="role")
	private String role;

	@ElementCollection
	private List<String>roles;

	@OneToOne(optional = true)
	private UserInfoEntity userInfo;

	@OneToOne(optional = true)
	@JoinColumn(name = "createdBy", unique = false, nullable = true, updatable = false)
	private UserEntity createdBy;


	// Post
	// manager da PositionEntity
	@OneToMany(mappedBy="positionManager", cascade=CascadeType.ALL)
	private List<PositionEntity> managedPositions;

	// Post
	// admin que criou a PositionEntity
	@OneToMany(mappedBy="positionCreator", cascade=CascadeType.ALL)
	private List<PositionEntity> createdPositions;

	// Post
	// admin que criou a ScriptEntity
	@OneToMany(mappedBy="scriptCreator", cascade=CascadeType.ALL)
	private List<ScriptEntity> scriptCreator;

	// Post
	// interviewers, a que entrevistas estão assignados
	@ManyToMany(mappedBy = "interviewers")
	private List<InterviewEntity> interviews;

	// Post
	// user que marcou a Interview
	@OneToMany(mappedBy="interviewScheduledBy", cascade=CascadeType.ALL)
	private List<InterviewEntity> interviewScheduler;
	
	// Post
	// posições a que se candidatou - Submissions
	@OneToMany(mappedBy="candidate", cascade=CascadeType.ALL)
	private List<SubmissionEntity> submissions;
	
	// Post
	// User que associou um candidatura espontanea a determinada submissão
	@OneToMany(mappedBy="associatedBy", cascade=CascadeType.ALL)
	private List<SubmissionEntity> spontaneusSubmissionAssociatedBy;

	public UserEntity() {
	}

	public UserEntity(String email, String password, String firstName, String lastName, List<String>roles) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles=roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getRoles() {
		return this.roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public UserInfoEntity getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoEntity userInfo) {
		this.userInfo = userInfo;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public List<PositionEntity> getManagedPositions() {
		return managedPositions;
	}

	public void setManagedPositions(List<PositionEntity> managedPositions) {
		this.managedPositions = managedPositions;
	}

	public List<PositionEntity> getCreatedPositions() {
		return createdPositions;
	}

	public void setCreatedPositions(List<PositionEntity> createdPositions) {
		this.createdPositions = createdPositions;
	}

	public List<ScriptEntity> getScriptCreator() {
		return scriptCreator;
	}

	public void setScriptCreator(List<ScriptEntity> scriptCreator) {
		this.scriptCreator = scriptCreator;
	}

	public List<InterviewEntity> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewEntity> interviews) {
		this.interviews = interviews;
	}

	public List<InterviewEntity> getInterviewScheduler() {
		return interviewScheduler;
	}

	public void setInterviewScheduler(List<InterviewEntity> interviewScheduler) {
		this.interviewScheduler = interviewScheduler;
	}

	public List<SubmissionEntity> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<SubmissionEntity> submissions) {
		this.submissions = submissions;
	}

}
