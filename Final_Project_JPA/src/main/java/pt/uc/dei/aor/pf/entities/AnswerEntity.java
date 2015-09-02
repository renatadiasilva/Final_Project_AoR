package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="AnswerEntity")
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
public class AnswerEntity implements Serializable{
	
	//Strategy AUTO
	
	// Min Max Pattern Size Past
	
	//nullable unique lenght
//	, nullable = false, unique = true
	
//	@OrderBy("name")
	
	// NotNull NotEmpty NotBlank
	
//	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
//	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
//	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
//	             message="{invalid.email}")

//	@Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
//            message="{invalid.phonenumber}")
//   protected String mobilePhone;
//   @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
//            message="{invalid.phonenumber}")
//   protected String homePhone;
	
//	(xxx) xxx–xxxx
	
//	@Temporal(TemporalType.DATE)

//	@ManyToMany
//	@JoinTable(name = "playlist_contains_songs",
//			joinColumns = @JoinColumn(name = "playlist_id"),
//			inverseJoinColumns = @JoinColumn(name = "song_id"))

   
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
