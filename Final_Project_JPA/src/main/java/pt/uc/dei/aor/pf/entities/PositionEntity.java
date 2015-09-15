package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "positions")
@NamedQueries({
//	@NamedQuery(name = "Position.findPositionsByDate",
//			query = "SELECT p FROM PositionEntity p WHERE p.openingDate "
//					+ "BETWEEN :date1 AND :date2 ORDER BY p.openingDate"),
//	@NamedQuery(name = "Position.findPositionsByCode",
//			query = "SELECT p FROM PositionEntity p WHERE UPPER(p.positionCode) LIKE :c"
//					+ " ORDER BY p.positionCode"),
//	@NamedQuery(name = "Position.findPositionsByTitle",
//			query = "SELECT p FROM PositionEntity p WHERE UPPER(p.title) LIKE :t"
//					+ " ORDER BY p.positionCode"),
//	@NamedQuery(name = "Position.findPositionsByLocation",
//			query = "SELECT p FROM PositionEntity p JOIN p.locations l WHERE l LIKE :loc"
//					+ " ORDER BY p.positionCode"),
//			query = "SELECT p FROM PositionEntity p WHERE :loc MEMBER OF p.locations ORDER BY p.positionCode"), //(TESTAR)
//	@NamedQuery(name = "Position.findPositionsByStatus",
//			query = "SELECT p FROM PositionEntity p WHERE UPPER(p.status) LIKE :s "
//					+ "ORDER BY p.positionCode"),
//	@NamedQuery(name = "Position.findPositionsByCompany",
//			query = "SELECT p FROM PositionEntity p WHERE UPPER(p.company) LIKE :c"
//					+ " ORDER BY p.positionCode"),
//	@NamedQuery(name = "Position.findPositionsByTechArea",
//			query = "SELECT p FROM PositionEntity p WHERE UPPER(p.technicalArea) LIKE :ta"
//					+ " ORDER BY p.positionCode"),
//	@NamedQuery(name = "Position.findPositionsBySeveralAttributes",
//			query = "SELECT p FROM PositionEntity p WHERE p.openingDate BETWEEN :date1 AND :date2"
//					+ " AND UPPER(p.positionCode) LIKE :c AND UPPER(p.title) LIKE :t"
//					+ " AND :loc MEMBER OF p.locations AND UPPER(p.status) LIKE :s"
//					+ " AND UPPER(p.company) LIKE :comp"
//					+ " AND UPPER(p.technicalArea) LIKE :ta ORDER BY p.positionCode"),
//	@NamedQuery(name = "Position.findPositionsBySeveralAttributesByManager",
//			query = "SELECT p FROM PositionEntity p WHERE p.openingDate BETWEEN :date1 AND :date2"
//					+ " AND UPPER(p.positionCode) LIKE :c AND UPPER(p.title) LIKE :t"
//					+ " AND :loc MEMBER OF p.locations AND UPPER(p.status) LIKE :s"
//					+ " AND UPPER(p.company) LIKE :comp"
//					+ " AND UPPER(p.technicalArea) LIKE :ta AND p.positionManager = :id"
//					+ " ORDER BY p.positionCode"),
	@NamedQuery(name = "Position.findCloseToSLAPositions",
			query = "SELECT p FROM PositionEntity p WHERE :date >= p.slaDate AND p.status = 'OPEN'"),
//	@NamedQuery(name = "Position.findPositionsByKeyword",
//			query = "SELECT p FROM PositionEntity p WHERE"
//					+ " UPPER(p.positionCode) LIKE :keyword OR"
//					+ " UPPER(p.title) LIKE :keyword OR"
//					+ " :keyword MEMBER OF p.locations OR"
//					+ " UPPER(p.company) LIKE :keyword OR"
//					+ " UPPER(p.technicalArea) LIKE :keyword OR"
//					+ " UPPER(p.description) LIKE :keyword ORDER BY p.positionCode"),
//	@NamedQuery(name = "Position.findPositionsByKeywordByManager",
//			query = "SELECT p FROM PositionEntity p WHERE"
//					+ " (UPPER(p.positionCode) LIKE :keyword OR"
//					+ " UPPER(p.title) LIKE :keyword OR"
//					+ " :keyword MEMBER OF p.locations OR"
//					+ " UPPER(p.company) LIKE :keyword OR"
//					+ " UPPER(p.technicalArea) LIKE :keyword OR"
//					+ " UPPER(p.description) LIKE :keyword) "
//					+ " AND p.positionManager = :id ORDER BY p.positionCode"),
	@NamedQuery(name = "Position.findPositionsByCandidate",
			query = "SELECT s.position FROM UserEntity u JOIN u.submissions s WHERE u = :user"),
	@NamedQuery(name = "Position.findByPositionAndCandidate",
			query = "SELECT s.position FROM UserEntity u JOIN u.submissions s WHERE u = :user AND s.position = :position ")
})
public class PositionEntity implements Serializable {

	private static final long serialVersionUID = -2368658385927790368L;

	public static final String LOCATION_LISBOA="LISBOA";
	public static final String LOCATION_PORTO="PORTO";
	public static final String LOCATION_COIMBRA="COIMBRA";

	public static final String STATUS_OPEN="Open";
	public static final String STATUS_CLOSED="Closed";
	public static final String STATUS_ONHOLD="on Hold";
	// closed with hired people (just internal??)
	public static final String STATUS_FULFILLED="Closed and Fulfilled";

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

	@NotNull
	@Column(name = "title", nullable = false, length = 40)
	private String title;

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

	// compute using days in the web
	@Column(name = "sla_date")
	@Temporal(TemporalType.DATE)
	private Date slaDate;

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

	@OneToMany
	private List<SubmissionEntity> submissions;

	public PositionEntity() {
	}

	public PositionEntity(String title, List<String> locations, int openings,
			Date closingDate, Date slaDate, UserEntity positionManager,
			UserEntity positionCreator, String company, String technicalArea,
			String description, List<String> advertisingChannels,
			ScriptEntity defaultScript) {
		this.title = title;
		this.openingDate = new Date(); // today
		Calendar cal = Calendar.getInstance();
		this.positionCode = title.substring(0, (int) Math.min(3,title.length()))+
				technicalArea.substring(0,(int) Math.min(3,title.length()))+ 
				cal.get(Calendar.DAY_OF_MONTH)+
				(cal.get(Calendar.MONTH)+1)+cal.get(Calendar.YEAR); // do generation!
		this.locations = locations;
		this.status = STATUS_OPEN;
		this.openings = openings;
		this.closingDate = closingDate; 
		// use with days??
		this.slaDate = slaDate;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Date getSlaDate() {
		return slaDate;
	}

	public void setSlaDate(Date slaDate) {
		this.slaDate = slaDate;
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
