package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="ScriptEntity")
public class ScriptEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8279496261691105535L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	// O Script do qual deriva este Script - Null: foi criado de raíz
	@OneToOne(optional = true)
	@JoinColumn(name = "derivedFrom", unique = false, nullable = true, updatable = false)
	private ScriptEntity derivedFrom;

	@Column(name="title")
	private String title;

	@Column(name="creationDate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date creationDate;

	@ManyToMany(fetch=FetchType.EAGER)
	private List<QuestionEntity> questions;

	@Column(name="comments")
	private String comments;

	// True by Default
	@Column(name="reusable")
	private boolean reusable;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="scriptCreator")
	private UserEntity scriptCreator;

	// Post
	// Os scripts por defeito das positions
	@OneToMany(mappedBy="defaultScript", cascade=CascadeType.ALL)
	private List<PositionEntity> defaultScriptInPositions;

	// Post
	// Os scripts das interviews
	@OneToMany(mappedBy="script", cascade=CascadeType.ALL)
	private List<InterviewEntity> interviewScript;

	public ScriptEntity() {
	}

	public ScriptEntity(long id, ScriptEntity derivedFrom, String title,
			Date creationDate, List<QuestionEntity> questions, String comments,
			boolean reusable, UserEntity scriptCreator) {
		this.id = id;
		this.derivedFrom = derivedFrom;
		this.title = title;
		this.creationDate = creationDate;
		this.questions = questions;
		this.comments = comments;
		this.reusable = reusable;
		this.scriptCreator = scriptCreator;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ScriptEntity getDerivedFrom() {
		return derivedFrom;
	}

	public void setDerivedFrom(ScriptEntity derivedFrom) {
		this.derivedFrom = derivedFrom;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<QuestionEntity> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionEntity> questions) {
		this.questions = questions;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isReusable() {
		return reusable;
	}

	public void setReusable(boolean reusable) {
		this.reusable = reusable;
	}

	public UserEntity getScriptCreator() {
		return scriptCreator;
	}

	public void setScriptCreator(UserEntity scriptCreator) {
		this.scriptCreator = scriptCreator;
	}

	public List<PositionEntity> getDefaultScriptInPositions() {
		return defaultScriptInPositions;
	}

	public void setDefaultScriptInPositions(
			List<PositionEntity> defaultScriptInPositions) {
		this.defaultScriptInPositions = defaultScriptInPositions;
	}

	public List<InterviewEntity> getInterviewScript() {
		return interviewScript;
	}

	public void setInterviewScript(List<InterviewEntity> interviewScript) {
		this.interviewScript = interviewScript;
	}

}
