package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "positions")
@NamedQueries({
	@NamedQuery(name = "Position.findClosedPositions",
			query = "SELECT p FROM PositionEntity p WHERE p.status = :closed"),
	@NamedQuery(name = "Position.findAllAlphabetic",
			query = "SELECT p FROM PositionEntity p ORDER BY p.company, p.title"),
	@NamedQuery(name = "Position.findCloseToSLAPositions",
			query = "SELECT p FROM PositionEntity p WHERE :date >= p.slaDate"
					+ " AND p.status = :open"),
	@NamedQuery(name = "Position.findAfterSLAPositions",
			query = "SELECT p FROM PositionEntity p"
					+ " WHERE p.slaDate < CURRENT_DATE "
					+ " AND p.status = :status"),
	@NamedQuery(name = "Position.findPositionsByCandidate",
			query = "SELECT s.position FROM UserEntity u JOIN u.submissions s"
					+ " WHERE u = :user"),
	@NamedQuery(name = "Position.findByPositionAndCandidate",
			query = "SELECT s.position FROM UserEntity u JOIN u.submissions s"
					+ " WHERE u = :user AND s.position = :position "),
	@NamedQuery(name = "Position.findOpenPositionsByScript",
			query = "SELECT p FROM PositionEntity p WHERE p.status = :status"
					+ " AND p.defaultScript = :script"),
	@NamedQuery(name = "Position.findNotOpenPositionByScript",
			query = "SELECT p FROM PositionEntity p WHERE p.status <> :status"
					+ " AND p.defaultScript = :script"),
	@NamedQuery(name = "Position.findPositionsManagedByUser",
			query = "SELECT p FROM PositionEntity p WHERE"
					+ " p.positionManager = :user ORDER BY p.positionCode"),
	@NamedQuery(name = "Position.findOpenPositionsManagedByUser",
			query = "SELECT p FROM PositionEntity p WHERE"
					+ " p.positionManager = :user AND p.status = :status"),
	@NamedQuery(name = "Position.countSubmissionsByPosition",
			query = "SELECT s.position.id, COUNT(s) FROM SubmissionEntity s"
					+ " WHERE "
					+ " s.position.openingDate BETWEEN :date1 AND :date2"
					+ " GROUP BY s.position.id, s.position.openingDate,"
					+ " s.position.positionCode"
					+ " ORDER BY s.position.openingDate,"
					+ " s.position.positionCode"),
	@NamedQuery(name = "Position.countRejectedByPosition",
			query = "SELECT s.position.id, COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.status = :rejected AND "
					+ " s.position.openingDate BETWEEN :date1 AND :date2"
					+ " GROUP BY s.position.id, s.position.openingDate,"
					+ " s.position.positionCode"
					+ " ORDER BY s.position.openingDate,"
					+ " s.position.positionCode"),
	@NamedQuery(name = "Position.countProposalsByPosition",
			query = "SELECT s.position.id, COUNT(s) FROM SubmissionEntity s"
					+ " WHERE s.proposalDate IS NOT NULL AND"
					+ " s.position.openingDate BETWEEN :date1 AND :date2"
					+ " GROUP BY s.position.id, s.position.openingDate,"
					+ " s.position.positionCode"
					+ " ORDER BY s.position.openingDate,"
					+ " s.position.positionCode"),
})
public class PositionEntity implements Serializable {

	private static final long serialVersionUID = -2368658385927790368L;

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

	@ElementCollection(fetch=FetchType.EAGER)
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

	@NotNull
	@Column(name = "hired_people", nullable = false)
	private int hired_people;

	@Column(name = "closing_date")
	@Temporal(TemporalType.DATE)
	private Date closingDate;

	@Column(name = "sla_date")
	@Temporal(TemporalType.DATE)
	private Date slaDate;
	
	@Column(name = "last_sla_mail_date")
	@Temporal(TemporalType.DATE)
	private Date lastSlaMailDate;

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

	@Column(name = "description", length = 3000)
	private String description;

	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "advertising_channels",
	        joinColumns = @JoinColumn(name = "position_id"))
	@Column(name = "channel")
	private List<String> advertisingChannels;

	@ManyToOne
	@JoinColumn(name = "default_script")
	private ScriptEntity defaultScript;

	@OneToMany(mappedBy = "position")
	private List<SubmissionEntity> submissions;

	public PositionEntity() {
	}

	public PositionEntity(String title, List<String> locations, int openings,
			String status, Date closingDate, int slaDays,
			UserEntity positionManager, UserEntity positionCreator,
			String company, String technicalArea, String description,
			List<String> advertisingChannels, ScriptEntity defaultScript) {
		this.title = title;
		this.openingDate = new Date(); // today

		// generate code position (improve this!!!!)
		Calendar cal = Calendar.getInstance();
		this.positionCode = title.substring(0, (int) Math.min(3,title.length()))
				+technicalArea.substring(0,(int) Math.min(3,title.length()))
				+cal.get(Calendar.DAY_OF_MONTH)
				+(cal.get(Calendar.MONTH)+1)+cal.get(Calendar.YEAR);
		
		this.locations = locations;
		this.status = status;
		this.openings = openings;
		this.closingDate = closingDate; 

		// compute SLA date adding SLA days to openingDate (today)
		cal.add(Calendar.DAY_OF_YEAR, slaDays);
		this.slaDate = cal.getTime();
		
		this.positionManager = positionManager;
		this.positionCreator = positionCreator;
		this.company = company;
		this.technicalArea = technicalArea;
		this.description = description;
		this.advertisingChannels = advertisingChannels;
		this.defaultScript = defaultScript;
		this.hired_people = 0;  // change each time a submission turns HIRED
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

	public Date getLastSlaMailDate() {
		return lastSlaMailDate;
	}

	public void setLastSlaMailDate(Date lastSlaMailDate) {
		this.lastSlaMailDate = lastSlaMailDate;
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

	public int getHired_people() {
		return hired_people;
	}

	public void setHired_people(int hired_people) {
		this.hired_people = hired_people;
	}

}
