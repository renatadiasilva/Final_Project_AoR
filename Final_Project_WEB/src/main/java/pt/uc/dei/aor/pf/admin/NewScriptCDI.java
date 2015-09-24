package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;

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
	
	private DualListModel<QuestionEntity> questions;
	
	private List<QuestionEntity> questionsDB;
	
	private List<QuestionEntity> questionsSource;
	
	private List<QuestionEntity> questionsTarget;
	
	private List<ScriptEntity>availableScripts;
	
	private ScriptEntity scriptToStartFrom;
	
	private String newQuestion;
	
	private String title;
	
	private String comments;
	
	@PostConstruct
	public void init() {
		this.scriptToStartFrom=null;
		
		this.availableScripts=scriptEJB.findReusableScripts();
		
		this.title=this.comments=this.newQuestion="";
		this.questionsDB = this.questionsEJB.findAll();
		this.questionsSource = this.questionsDB;
		this.questionsTarget = new ArrayList<QuestionEntity>();

		this.questions = new DualListModel<QuestionEntity>(questionsSource, questionsTarget);
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
		this.questions.setTarget(this.scriptToStartFrom.getQuestions());
		
		// Retira as questões do target do source
		this.questionsSource = this.questionsDB;
		for(QuestionEntity q:this.scriptToStartFrom.getQuestions())
			if(this.questionsSource.contains(q))
				this.questionsTarget.remove(q);
		
		// Sets
		this.questions.setSource(questionsSource);
		this.questions.setTarget(questionsTarget);
	}

	public void createNewQuestion(){
		// Cria, persiste e retorna a questão
		QuestionEntity newQuestion=new QuestionEntity(QuestionEntity.ANSWER, this.newQuestion);
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
	
}
