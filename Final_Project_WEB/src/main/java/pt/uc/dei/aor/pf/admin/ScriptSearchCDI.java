package pt.uc.dei.aor.pf.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;

import java.io.Serializable;


@Named
@SessionScoped
public class ScriptSearchCDI implements Serializable {

	private static final long serialVersionUID = -1275638845087957826L;

	private static final Logger log = 
			LoggerFactory.getLogger(ScriptSearchCDI.class);

	@EJB
	private ScriptEJBInterface scriptEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private QuestionEJBInterface questionEJB;

	// search fields
	private String title;
	private Long id;

	private List<QuestionEntity> questions;
	private List<ScriptEntity> sclist;
	ScriptEntity script;

	public ScriptSearchCDI() {
	}

	public void clean() {
		title="";
		id = 0L;
		searchAllScripts();
		this.script = null;
	}

	public void cleanAll() {
		title="";
		id = 0L;
		sclist = new ArrayList<ScriptEntity>();
		this.script = null;
	}

	public void searchAllScripts() {
		log.info("Searching for all scripts");
		this.sclist = scriptEJB.findAll();
		this.script = null;
	}

	public void searchReusableScripts() {
		log.info("Searching reusable scripts");
		this.sclist = scriptEJB.findReusableScripts();
		this.script = null;
	}

	public void searchByTitle() {
		log.info("Searching for scripts by title");
		String pattern = SearchPattern.preparePattern(title);
		log.debug("Internal search string : "+pattern);
		this.sclist = scriptEJB.findScriptsByTitle(pattern);
		this.script = null;
	}

	public void searchChildScripts() {
		log.info("Searching child scripts of a given script");
		log.debug("Id "+id);
		ScriptEntity script = scriptEJB.find(id);
		if (script != null) {
			this.sclist = scriptEJB.findChildScripts(script);
		} else log.error("No script with id "+id);
		this.script = null;
	}

	public boolean loadedScript() {
		return this.script!=null;
	}

	public boolean isDeletable(){		
		// Script base de outros script
		if(!this.scriptEJB.findChildScripts(this.script).isEmpty())return false;

		// Script em uso em posições
		if(!this.positionEJB.findPositionsByScript(this.script).isEmpty())return false;

		// Script em uso em entrevistas
		if(!this.interviewEJB.findInterviewsWithScript(this.script).isEmpty())return false;

		return true;
	}

	public void deleteScript(){
		this.scriptEJB.delete(this.script);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guião Apagado"));
	}

	public boolean checkScript(ScriptEntity script){
		if(this.script==null)return false;
		if(this.script.getId()==script.getId())return true;
		return false;
	}

	public void unloadScript(){
		this.script=null;
	}

	public String getTypeText(QuestionEntity question) {
		return questionEJB.getTypeText(question);
	}

	// getters e setters

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ScriptEntity> getSclist() {
		return sclist;
	}

	public void setSlist(List<ScriptEntity> sclist) {
		this.sclist = sclist;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSclist(List<ScriptEntity> sclist) {
		this.sclist = sclist;
	}

	public ScriptEntity getScript() {
		return script;
	}

	public void setScript(ScriptEntity script) {
		this.script = script;
	}

	public List<QuestionEntity> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionEntity> questions) {
		this.questions = questions;
	}

}