package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;

@Named
@SessionScoped
public class NewScriptCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3897744065558946519L;

	@EJB
	private QuestionEJBInterface questionsEJB;

	@EJB
	private ScriptEJBInterface scriptEJB;

	@EJB
	private UserEJBInterface userEJB;

	private DualListModel<QuestionEntity> questions;

	private List<QuestionEntity> questionsDB;

	private List<QuestionEntity> questionsSource;

	private List<QuestionEntity> questionsTarget;

	private List<ScriptEntity>availableScripts;

	private ScriptEntity scriptToStartFrom;

	private String newQuestion;

	private boolean numeric, trueFalse, textual;

	private String title;

	private String comments;

	@PostConstruct
	public void init() {
		this.textual=true;
		this.trueFalse=this.numeric=false;

		this.title=this.comments="";

		this.scriptToStartFrom=null;

		this.availableScripts=scriptEJB.findReusableScripts();

		this.title=this.comments=this.newQuestion="";
		this.questionsDB = this.questionsEJB.findAll();
		this.questionsSource = this.questionsDB;
		this.questionsTarget = new ArrayList<QuestionEntity>();

		this.questions = new DualListModel<QuestionEntity>(questionsSource, questionsTarget);
	}

	public void createScript(){
		boolean valid=true;

		if(this.title.isEmpty()){
			valid=false;
			this.error("Insíra um título");
		}

		if(this.comments.isEmpty()){
			valid=false;
			this.error("Insira comentários");
		}

		if(this.questions.getTarget().isEmpty()){
			valid=false;
			this.error("Crie uma lista de perguntas.");
		}

		if(valid){
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String creatorEmail=request.getRemoteUser();
			UserEntity creator=this.userEJB.findUserByEmail(creatorEmail);

			System.out.println(creatorEmail);
			System.out.println(creator.getFirstName()+" "+creator.getLastName());

			ScriptEntity newScript=new ScriptEntity(this.scriptToStartFrom, this.title, this.questions.getTarget(),
					this.comments, true, creator);

			this.scriptEJB.save(newScript);

			this.init();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Novo guião criado com sucesso"));
		}
	}

	private void error(String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean checkScriptToStartFrom(ScriptEntity script){
		if(this.scriptToStartFrom==null) return false;
		return this.scriptToStartFrom.equals(script);
	}

	public void useScript(ScriptEntity script){
		this.scriptToStartFrom=script;

		this.title=this.scriptToStartFrom.getTitle();
		this.comments=this.scriptToStartFrom.getComments();

		// Põe as questões do script no target
		this.questionsTarget=this.scriptToStartFrom.getQuestions();

		// Retira as questões do target do source
		this.questionsSource = this.questionsEJB.findAll();
		
//		long id1,id2;
//		for(QuestionEntity q1:this.questionsTarget){
//			id1=q1.getId();
//			for(QuestionEntity q2:this.questionsSource){
//				id2=q2.getId();
//				if(id1==id2){
//					System.out.println(q2.getQuestion()+" removed");
//					this.questionsSource.remove(q2);
//				}
//			}
//		}
		
//		this.questionsSource.removeAll(this.questionsTarget);
		
//		for(QuestionEntity q:this.questionsTarget)
//			if(this.questionsSource.contains(q))
//				this.questionsSource.remove(q);
		
//		for(QuestionEntity q1:this.questionsTarget){
//			for(QuestionEntity q2:this.questionsSource){
//				if(q1.equals(q2)){
//					System.out.println(q1.getQuestion()+" removed");
//					this.questionsSource.remove(q2);
//				}
//			}
//		}

		// Sets
		this.questions.setSource(questionsSource);
		this.questions.setTarget(questionsTarget);
	}

	public void createNewQuestion(){
		// Tipo de questão		
		String questionType="";
		if(this.numeric)questionType=QuestionEntity.VALUE;
		if(this.textual)questionType=QuestionEntity.ANSWER;
		if(this.trueFalse)questionType=QuestionEntity.ISTRUE;

		// Cria, persiste e retorna a questão
		QuestionEntity newQuestion=new QuestionEntity(questionType, this.newQuestion);
		newQuestion=this.questionsEJB.saveAndReturn(newQuestion);
		this.newQuestion="";

		// Actualiza a lista de questões - também é utilizada pelo converter
		this.questionsDB = this.questionsEJB.findAll();

		// Adiciona a questão ao guião
		this.questions.getTarget().add(newQuestion);
	}

	public String getNewQuestion() {
		return newQuestion;
	}

	public void setNewQuestion(String newQuestion) {
		this.newQuestion = newQuestion;
	}

	public DualListModel<QuestionEntity> getQuestions() {
		return questions;
	}

	public void setQuestions(DualListModel<QuestionEntity> questions) {
		this.questions = questions;
	}

	public void onTransfer(TransferEvent event) {
		StringBuilder builder = new StringBuilder();
		for(Object item : event.getItems()) {
			builder.append(((QuestionEntity) item).getQuestion()).append("<br />");
		}
	} 

	public List<QuestionEntity> getQuestionsDB() {
		return questionsDB;
	}

	public void setQuestionsDB(List<QuestionEntity> questionsDB) {
		this.questionsDB = questionsDB;
	}

	public ScriptEntity getScriptToStartFrom() {
		return scriptToStartFrom;
	}

	public void setScriptToStartFrom(ScriptEntity scriptToStartFrom) {
		this.scriptToStartFrom = scriptToStartFrom;
	}

	public List<ScriptEntity> getAvailableScripts() {
		return availableScripts;
	}

	public void setAvailableScripts(List<ScriptEntity> availableScripts) {
		this.availableScripts=scriptEJB.findReusableScripts();;
	}

	public boolean isNumeric() {
		return numeric;
	}

	public void setNumeric(boolean numeric) {
		this.cleanQuestionType();
		this.numeric = numeric;
	}

	public boolean isTrueFalse() {
		return trueFalse;
	}

	public void setTrueFalse(boolean trueFalse) {
		this.cleanQuestionType();
		this.trueFalse = trueFalse;
	}

	public boolean isTextual() {
		return textual;
	}

	public void setTextual(boolean textual) {
		this.cleanQuestionType();
		this.textual = textual;
	}

	private void cleanQuestionType(){
		this.textual=this.numeric=this.trueFalse=false;
	}

}
