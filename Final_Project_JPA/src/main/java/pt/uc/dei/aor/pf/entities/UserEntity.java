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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2911048822662162612L;

	public static final String ROLE_ADMIN="ADMIN";
	public static final String ROLE_MANAGER="MANAGER";
	public static final String ROLE_INTERVIEWER="INTERVIEWER";
	public static final String ROLE_CANDIDATE="CANDIDATE";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
//  pattern mais simples??? limita??
//	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
//    +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
//    +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
//         message="{invalid.email}")
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@NotNull
	@Column(name ="password", nullable = false, length = 64)
	private String password;

	@NotNull
	//@NotBlank??
	@Column(name = "first_name", nullable = false, length = 20)
	private String firstName;

	@NotNull
	@Column(name = "last_name", nullable = false, length = 40)
	private String lastName;

//	@NotNull
//	@Column(name = "default_role", nullable = false)
	@Column(name = "default_role")
	private String defaultRole;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "roles",
    	joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private List<String> roles;

	@OneToOne(optional = true)
	@JoinColumn(name = "user_info", unique = true, updatable = false)
	private UserInfoEntity userInfo;

	@OneToOne(optional = true)
	@JoinColumn(name = "created_by", updatable = false)
	private UserEntity createdBy;

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

	public UserEntity(String email, String password, String firstName, String lastName, List<String>roles) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles=roles;
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

	public void setScheduledInterviews(List<InterviewEntity> scheduledInterviews) {
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
		this.spontaneusSubmissionAssociatedBy = spontaneusSubmissionAssociatedBy;
	}

}
