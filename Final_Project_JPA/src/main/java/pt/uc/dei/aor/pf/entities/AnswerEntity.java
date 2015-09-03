package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "answers")
//@NamedQueries({
//	@NamedQuery(name="User.findUserByEmail", 
//			query="SELECT u FROM User u WHERE u.email = :email"),
//	@NamedQuery(name="User.findUserStartingBy",
//			query="SELECT u FROM User u WHERE u.name like :exp ORDER BY u.name"),
//	@NamedQuery(name="User.findUserById", 
//		query="SELECT u FROM User u WHERE u.id = :id"),
//	@NamedQuery(name="User.findAllByIdOrder", 
//		query="SELECT u FROM User u ORDER BY u.id")
//}) 
public class AnswerEntity implements Serializable {
	
	private static final long serialVersionUID = 6475879936674697759L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "interview", nullable = false)
	private InterviewEntity interview;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "question", nullable = false)
	private QuestionEntity question;
	
	@Column(name = "value")
	private int value;
	
	@Column(name = "is_true")
	private boolean isTrue;
	
	@Column(name = "answer", length = 100)
	private String answer;
	
	@Column(name = "comments", length = 100)
	private String comments;

	public AnswerEntity() {
	}

	public AnswerEntity(QuestionEntity question, int value, boolean isTrue,
			String answer, String comments) {
		
		this.question = question;
		
		if (question.getType().equals(QuestionEntity.VALUE)) this.value = value;
		if (question.getType().equals(QuestionEntity.ISTRUE)) this.isTrue = isTrue;
		if (question.getType().equals(QuestionEntity.ANSWER)) this.answer = answer;
		
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InterviewEntity getInterview() {
		return interview;
	}

	public void setInterview(InterviewEntity interview) {
		this.interview = interview;
	}

	public QuestionEntity getQuestion() {
		return question;
	}

	public void setQuestion(QuestionEntity question) {
		this.question = question;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
