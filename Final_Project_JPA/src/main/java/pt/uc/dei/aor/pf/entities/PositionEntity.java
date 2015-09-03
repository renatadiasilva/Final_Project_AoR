package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "positions")
public class PositionEntity implements Serializable {

	private static final long serialVersionUID = -2368658385927790368L;

	public static final String LISBOA="Lisboa";
	public static final String PORTO="Porto";
	public static final String COIMBRA="Coimbra";

	public static final String OPEN="Open";
	public static final String CLOSED="Closed";
	public static final String ONHOLD="on Hold";
	// closed with hired people (just internal??)
	public static final String FULFILLED="Closed and Fulfilled";

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "opening_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date openingDate;

	@NotNull
	@Column(name = "code", nullable = false, unique = true)
	private String positionCode;

	@ElementCollection
	@CollectionTable(name = "locations",
		joinColumns=@JoinColumn(name="position_id"))
	@Column(name = "location")
	private List<String> locations;

	@NotNull
	@Column(name = "status", nullable = false)
	private String status;

	@NotNull
	@Column(name = "openings", nullable = false)
	private int openings;

	@Column(name = "closing_date")
	@Temporal(TemporalType.DATE)
	private Date closingDate;

	// weeks??? DAYS???
	@Column(name = "sla")
	private int sla;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "manager", nullable = false)
	private UserEntity positionManager;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "creator", nullable = false)
	private UserEntity positionCreator;

	@NotNull
	@Column(name = "company", nullable = false, length = 40)
	private String company;

	@NotNull
	@Column(name = "technical_area", nullable = false)
	private String technicalArea;

	@Column(name = "description", length = 200)
	private String description;

	@ElementCollection
	@CollectionTable(name = "advertising_channels",
	        joinColumns = @JoinColumn(name = "position_id"))
	@Column(name = "channel")
	private List<String> advertisingChannels;

	@ManyToOne
	@JoinColumn(name = "default_script")
	private ScriptEntity defaultScript;

//	@ManyToMany(mappedBy="positions", cascade=CascadeType.ALL)
	@ManyToMany(mappedBy = "positions")
	private List<SubmissionEntity> submissions;

	public PositionEntity() {
	}

	public PositionEntity(Date openingDate, String positionCode,
			List<String> locations, String currentState, int openings,
			Date closingDate, int sla, UserEntity positionManager,
			UserEntity positionCreator, String company, String technicalArea,
			String description, List<String> advertisingChannels,
			ScriptEntity defaultScript) {
		this.openingDate = openingDate;
		this.positionCode = positionCode;
		this.locations = locations;
		this.status = currentState;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public int getSla() {
		return sla;
	}

	public void setSla(int sla) {
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
