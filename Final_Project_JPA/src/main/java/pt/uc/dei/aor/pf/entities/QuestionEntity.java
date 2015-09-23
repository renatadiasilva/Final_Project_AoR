package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "questions")
public class QuestionEntity implements Serializable{

	private static final long serialVersionUID = 8262287711073210783L;

	public static final String VALUE  = "value";
	public static final String ISTRUE = "isTrue";
	public static final String ANSWER = "answer";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "type", updatable = false, nullable = false)
	private String type;

	@NotNull
	@Column(name="question", updatable = false, nullable = false, length = 100)
	private String question;

	@ManyToMany(mappedBy = "questions")
	private List<ScriptEntity> scripts;

	@OneToMany(mappedBy = "question")
	private List<AnswerEntity> answers;

	public QuestionEntity() {
	}
	
	public QuestionEntity(String type, String question) {
		
		if (type.equals(QuestionEntity.VALUE)) 
			this.type = QuestionEntity.VALUE;
		if (type.equals(QuestionEntity.ISTRUE)) 
			this.type = QuestionEntity.ISTRUE;
		if (type.equals(QuestionEntity.ANSWER)) 
			this.type = QuestionEntity.ANSWER;
		
		this.question = question;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<ScriptEntity> getScripts() {
		return scripts;
	}

	public void setScripts(List<ScriptEntity> scripts) {
		this.scripts = scripts;
	}

	public List<AnswerEntity> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerEntity> answers) {
		this.answers = answers;
	}

}
