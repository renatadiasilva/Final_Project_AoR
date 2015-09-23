package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
 
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
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
	
	private DualListModel<QuestionEntity> questions;
	
	private List<QuestionEntity> questionsSource;
	
	private List<QuestionEntity> questionsTarget;
	
	private ScriptEntity scriptToStartFrom;
	
	@PostConstruct
	public void init() {
		//Themes
		this.questionsSource = this.questionsEJB.findAll();
		this.questionsTarget = new ArrayList<QuestionEntity>();

		this.questions = new DualListModel<QuestionEntity>(questionsSource, questionsTarget);
	}
	
	public void createNewQuestion(String question){
		QuestionEntity newQuestion=new QuestionEntity(QuestionEntity.ANSWER, question);
		newQuestion=this.questionsEJB.saveAndReturn(newQuestion);
		
		this.questions.getTarget().add(newQuestion);
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

		FacesMessage msg = new FacesMessage();
		msg.setSeverity(FacesMessage.SEVERITY_INFO);
		msg.setSummary("Items Transferred");
		msg.setDetail(builder.toString());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	} 

	public void onSelect(SelectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
	}

	public void onUnselect(UnselectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
	}

	public void onReorder() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
	} 

}
