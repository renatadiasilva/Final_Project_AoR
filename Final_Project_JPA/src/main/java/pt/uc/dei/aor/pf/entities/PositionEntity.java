package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="PositionEntity")
public class PositionEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2368658385927790368L;


	public static final String LISBOA="Lisboa";

	public static final String PORTO="Porto";

	public static final String COIMBRA="Coimbra";


	public static final String OPEN="Open";

	public static final String CLOSED="Closed";

	public static final String ONHOLD="on Hold";


	public static final String TECH_SSPA="SSPA";

	public static final String TECH_DOTNET=".Net Development";

	public static final String TECH_JAVA="Java Development";

	public static final String TECH_SAFETY="Safety Critical";

	public static final String TECH_MANAGEMENT="Project Management";

	public static final String TECH_INTEGRATION="Integration";


	public static final String SOCIAL_CRITICAL="Critical Software Website";

	public static final String SOCIAL_LINKEDIN="Linkedin";

	public static final String SOCIAL_GLASSDOOR="Glassdoor";

	public static final String SOCIAL_FACEBOOK="Facebook";


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@Column(name="openingDate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date openingDate;

	@Column(name="positionCode")
	private String positionCode;

	@ElementCollection
	private List<String> location;

	// Closed and Fullfilled or "Closed and not Fullfilled
	@Column(name="currentState")
	private String currentState;

	@Column(name="openings")
	private int openings;

	@Column(name="closingDate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date closingDate;

	@Column(name="sla")
	private String sla;

	@ManyToOne
	@JoinColumn(name="positionManager")
	private UserEntity positionManager;

	@ManyToOne
	@JoinColumn(name="positionCreator")
	private UserEntity positionCreator;

	@Column(name="company")
	private String company;

	@Column(name="technicalArea")
	private String technicalArea;

	@Column(name="description")
	private String description;

	@ElementCollection
	private List<String> advertisingChannels;

	@ManyToOne
	@JoinColumn(name="defaultScript")
	private ScriptEntity defaultScript;

	// Post
	// posições a que se candidatou - Submissions
	@ManyToMany(mappedBy="positions")
	private List<SubmissionEntity> submissions;

	public PositionEntity() {
	}

	public PositionEntity(Date openingDate, String positionCode,
			List<String> location, String currentState, int openings,
			Date closingDate, String sla, UserEntity positionManager,
			UserEntity positionCreator, String company, String technicalArea,
			String description, List<String> advertisingChannels,
			ScriptEntity defaultScript) {
		this.openingDate = openingDate;
		this.positionCode = positionCode;
		this.location = location;
		this.currentState = currentState;
		this.openings = openings;
		this.closingDate = closingDate;
		this.sla = sla;
		this.positionManager = positionManager;
		this.positionCreator = positionCreator;
		this.company = company;
		this.technicalArea = technicalArea;
		this.description = description;
		this.advertisingChannels = advertisingChannels;
		this.defaultScript = defaultScript;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public List<String> getLocation() {
		return location;
	}

	public void setLocation(List<String> location) {
		this.location = location;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public int getOpenings() {
		return openings;
	}

	public void setOpenings(int openings) {
		this.openings = openings;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public UserEntity getPositionManager() {
		return positionManager;
	}

	public void setPositionManager(UserEntity positionManager) {
		this.positionManager = positionManager;
	}

	public UserEntity getPositionCreator() {
		return positionCreator;
	}

	public void setPositionCreator(UserEntity positionCreator) {
		this.positionCreator = positionCreator;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTechnicalArea() {
		return technicalArea;
	}

	public void setTechnicalArea(String technicalArea) {
		this.technicalArea = technicalArea;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getAdvertisingChannels() {
		return advertisingChannels;
	}

	public void setAdvertisingChannels(List<String> advertisingChannels) {
		this.advertisingChannels = advertisingChannels;
	}

	public ScriptEntity getDefaultScript() {
		return defaultScript;
	}

	public void setDefaultScript(ScriptEntity defaultScript) {
		this.defaultScript = defaultScript;
	}

	public List<SubmissionEntity> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<SubmissionEntity> submissions) {
		this.submissions = submissions;
	}

}
