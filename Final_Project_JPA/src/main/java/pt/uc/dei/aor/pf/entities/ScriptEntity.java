package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="scripts")
@NamedQueries({
	@NamedQuery(name = "Script.findReusableScripts",
			query = "SELECT s FROM ScriptEntity s WHERE s.reusable = TRUE"
					+ " ORDER BY s.creationDate DESC"),
})
public class ScriptEntity implements Serializable{

	private static final long serialVersionUID = 8279496261691105535L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@OneToOne(optional = true)
	@JoinColumn(name = "derived_from", updatable = false)
	private ScriptEntity derivedFrom;

	@NotNull
	@Column(name = "title", nullable = false, length = 40)
	private String title;

	@NotNull
	@Column(name = "creation_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "scripts_questions",
		joinColumns = @JoinColumn(name = "script_id"),
		inverseJoinColumns = @JoinColumn(name = "question_id"))
	private List<QuestionEntity> questions;

	@Column(name="comments", length = 100)
	private String comments;

	@Column(name = "reusable")
	private boolean reusable;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "creator", nullable = false)
	private UserEntity scriptCreator;

	@OneToMany(mappedBy = "defaultScript", cascade = CascadeType.ALL)
	@OrderBy("positionCode")
	private List<PositionEntity> positionsWithScriptDefault;

	@OneToMany(mappedBy = "script", cascade=CascadeType.ALL)
	private List<InterviewEntity> interviewsUsingScript;

	public ScriptEntity() {
	}

	public ScriptEntity(ScriptEntity derivedFrom, String title,
			List<QuestionEntity> questions, 
			String comments, boolean reusable, UserEntity scriptCreator) {
		this.derivedFrom = derivedFrom;
		this.title = title;
		this.creationDate = new Date();
		this.questions = questions;
		this.comments = comments;
		this.reusable = reusable;
		this.scriptCreator = scriptCreator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<PositionEntity> getPositionsWithScriptDefault() {
		return positionsWithScriptDefault;
	}

	public void setPositionsWithScriptDefault(
			List<PositionEntity> positionsWithScriptDefault) {
		this.positionsWithScriptDefault = positionsWithScriptDefault;
	}

	public List<InterviewEntity> getInterviewsUsingScript() {
		return interviewsUsingScript;
	}

	public void setInterviewsUsingScript(
			List<InterviewEntity> interviewsUsingScript) {
		this.interviewsUsingScript = interviewsUsingScript;
	}

	public void addQuestion(QuestionEntity question) {
		if (questions == null) questions = new ArrayList<QuestionEntity>();
		if (!questions.contains(question)) questions.add(question);
	}

	public void removeQuestion(QuestionEntity question) {
		if (questions != null) questions.remove(question);
	}

}
