package pt.uc.dei.aor.pf.admin;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.entities.ScriptEntity;


@Named
@RequestScoped
public class ScriptSearchCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(ScriptSearchCDI.class);

	@EJB
	private ScriptEJBInterface scriptEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;
	// search fields
	private String title;
	private Long id;

	private List<ScriptEntity> sclist;

	public ScriptSearchCDI() {
	}

	public void searchAllScripts() {
		log.info("Searching for all scripts");
		this.sclist = scriptEJB.findAll();
	}

	public void searchReusableScripts() {
		log.info("Searching reusable scripts");
		this.sclist = scriptEJB.findReusableScripts();
	}

	public void searchByTitle() {
		log.info("Searching for scripts by title");
		String pattern = SearchPattern.preparePattern(title);
		log.debug("Internal search string : "+pattern);
		this.sclist = scriptEJB.findScriptsByTitle(pattern);
	}

	public void searchChildScripts() {
		log.info("Searching child scripts of a given script");
		log.debug("Id "+id);
		ScriptEntity script = scriptEJB.find(id);
		if (script != null) {
			this.sclist = scriptEJB.findChildScripts(script);
		} else log.error("No script with id "+id);
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

}