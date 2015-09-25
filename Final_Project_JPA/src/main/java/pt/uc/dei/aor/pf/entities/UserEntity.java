package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;


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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(name = "User.findUserByEmail",
			query = "SELECT u FROM UserEntity u "
					+ "WHERE UPPER(u.email) LIKE :email"),
	@NamedQuery(name = "User.findUsersByEmailPattern",
			query = "SELECT DISTINCT u FROM UserEntity u JOIN u.roles r"
					+ " WHERE UPPER(u.email) LIKE :email"
					+ " AND (r <> :role) AND u.email NOT LIKE :removed"
					+ " ORDER BY u.email"),
	@NamedQuery(name = "User.findCandidatesByPhone",
			query = "SELECT u.owner FROM UserInfoEntity u WHERE"
					+ " u.homePhone LIKE :phone OR u.mobilePhone LIKE :phone"
					+ " AND u.owner.email NOT LIKE :removed"
					+ " ORDER BY u.owner.email"),
	@NamedQuery(name = "User.findRemovedEmails",
			query = "SELECT u FROM UserEntity u WHERE"
					+ " u.email LIKE :removed ORDER BY u.email"),
	@NamedQuery(name = "User.findAllNotRemoved",
			query = "SELECT u FROM UserEntity u WHERE"
					+ " u.email NOT LIKE :removed ORDER BY u.email"),
})
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2911048822662162612L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@NotNull
	@Column(name ="password", nullable = false, length = 64)
	private String password;
	
	@Column(name ="temporary_password", nullable = false)
	private boolean temporaryPassword;
	
	@NotNull
	@Column(name ="authenticated", nullable = false)
	private boolean authenticated;
	
	@Column(name ="authentication_key")
	private String authenticationKey;

	@NotNull
	@Column(name = "first_name", nullable = false, length = 20)
	private String firstName;

	@NotNull
	@Column(name = "last_name", nullable = false, length = 40)
	private String lastName;

	@NotNull
	@Column(name = "default_role", nullable = false)
	private String defaultRole;
	
	@NotNull
	@Column(name = "uploaded_cv", nullable = false)
	private boolean uploadedCV;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "roles",
    	joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private List<String> roles;

	@OneToOne(mappedBy="owner", fetch = FetchType.EAGER, 
			cascade = CascadeType.ALL, orphanRemoval = true)
	private UserInfoEntity userInfo;

	@ManyToOne
	@JoinColumn(name = "created_by", updatable = false)
	private UserEntity createdBy;

	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
	private List<UserEntity> createdUsers; 

	@OneToMany(mappedBy = "positionManager", cascade = CascadeType.ALL)
	@OrderBy("positionCode")
	private List<PositionEntity> managedPositions;

	@OneToMany(mappedBy = "positionCreator", cascade = CascadeType.ALL)
	@OrderBy("positionCode")
	private List<PositionEntity> createdPositions;

	@OneToMany(mappedBy = "scriptCreator", cascade = CascadeType.ALL)
	@OrderBy("title")
	private List<ScriptEntity> createdScripts;

	@ManyToMany(mappedBy = "interviewers")
	private List<InterviewEntity> interviews;

	@OneToMany(mappedBy="scheduledBy", cascade = CascadeType.ALL)
	@OrderBy("date DESC")
	private List<InterviewEntity> scheduledInterviews;
	
	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
	@OrderBy("date")
	private List<SubmissionEntity> submissions;
	
	@OneToMany(mappedBy = "associatedBy", cascade = CascadeType.ALL)
	@OrderBy("date")
	private List<SubmissionEntity> spontaneusSubmissionAssociatedBy;

	public UserEntity() {
	}

	public UserEntity(String email, String password, String firstName, 
			String lastName, List<String>roles) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
		this.temporaryPassword = false;
		this.uploadedCV=false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public boolean isTemporaryPassword() {
		return temporaryPassword;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getAuthenticationKey() {
		return authenticationKey;
	}

	public void setAuthenticationKey(String authenticationKey) {
		this.authenticationKey = authenticationKey;
	}

	public void setTemporaryPassword(boolean temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}

	public boolean isUploadedCV() {
		return uploadedCV;
	}

	public void setUploadedCV(boolean uploadedCV) {
		this.uploadedCV = uploadedCV;
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

	public String getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}

	public List<String> getRoles() {
		return roles;
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

	public List<ScriptEntity> getCreatedScripts() {
		return createdScripts;
	}

	public void setCreatedScripts(List<ScriptEntity> createdScripts) {
		this.createdScripts = createdScripts;
	}

	public List<InterviewEntity> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewEntity> interviews) {
		this.interviews = interviews;
	}

	public List<InterviewEntity> getScheduledInterviews() {
		return scheduledInterviews;
	}

	public void setScheduledInterviews(
			List<InterviewEntity> scheduledInterviews) {
		this.scheduledInterviews = scheduledInterviews;
	}

	public List<SubmissionEntity> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<SubmissionEntity> submissions) {
		this.submissions = submissions;
	}

	public List<SubmissionEntity> getSpontaneusSubmissionAssociatedBy() {
		return spontaneusSubmissionAssociatedBy;
	}

	public void setSpontaneusSubmissionAssociatedBy(
			List<SubmissionEntity> spontaneusSubmissionAssociatedBy) {
		this.spontaneusSubmissionAssociatedBy = 
				spontaneusSubmissionAssociatedBy;
	}

	public List<UserEntity> getCreatedUsers() {
		return createdUsers;
	}

	public void setCreatedUsers(List<UserEntity> createdUsers) {
		this.createdUsers = createdUsers;
	}

}
