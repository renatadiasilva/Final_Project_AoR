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

@Entity
@Table(name="AnswerEntity")
public class AnswerEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6475879936674697759L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="interview")
	private InterviewEntity interview;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="question")
	private QuestionEntity question;
	
	// Nullable
	@Column(name="value", nullable=true)
	private int value;
	
	// Nullable
	@Column(name="isTrue", nullable=true)
	private boolean isTrue;
	
	// Nullable
	@Column(name="answer", nullable=true)
	private String answer;
	
	// Nullable
	@Column(name="comments", nullable=true)
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
