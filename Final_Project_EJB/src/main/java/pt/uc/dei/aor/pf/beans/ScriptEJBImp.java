package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.InterviewDao;
import pt.uc.dei.aor.pf.dao.PositionDao;
import pt.uc.dei.aor.pf.dao.ScriptDao;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class ScriptEJBImp implements ScriptEJBInterface {

	private static final Logger log = 
			LoggerFactory.getLogger(ScriptEJBImp.class);

	@EJB
	private ScriptDao scriptDAO;

	@EJB
	private PositionDao positionDAO;
	
	@EJB
	private InterviewDao interviewDAO;

	@Override
	public void save(ScriptEntity script) {
		log.info("Saving script in DB");
		isScriptComplete(script);
		scriptDAO.save(script);
	}

	@Override
	public void update(ScriptEntity script) {
		log.info("Updating script of DB");
		isScriptComplete(script);
		scriptDAO.update(script);
	}

	@Override
	public void delete(ScriptEntity script) {
		log.info("Deleting/Making not reusable a script from DB");

		// there are open positions with this script as default
		List<PositionEntity> plist = 
				positionDAO.findOpenPositionsByScript(script);
		if (plist != null && !plist.isEmpty()) {
			// erro: avisar para o admin ir mudar o default 
			// das posições e/ou listá-las
			// ou apresentar logo uma lista com as posições e scripts??
			System.out.println("Não pode apagar um guião usado em posições"
					+ " abertas");
			return;
		} else { //open no positions
			plist = positionDAO.findNotOpenPositionsByScript(script);
			// there are not open positions
			if ( plist != null && !plist.isEmpty() ) {
				for (PositionEntity p : plist)
					// remove script from position
					p.setDefaultScript(null);
				// script cannot be used anymore, but don't delete it
//				script.setReusable(false);
//				return;
			}
			List<InterviewEntity> ilist = 
					interviewDAO.findInterviewsWithScript(script);
			// there are interviews using this script
			if (ilist != null && !ilist.isEmpty()) {
				for (InterviewEntity i : ilist)
					//remove script from interview
					i.setScript(null);
				// script cannot be used anymore, but don't delete it
//				script.setReusable(false);
			} else scriptDAO.delete(script.getId(), ScriptEntity.class);
		}
	}

	@Override
	public void edit(ScriptEntity script, List<QuestionEntity> questions, 
			UserEntity creator) {
		log.info("Editing/Cloning script from DB");

		// there are positions with this script as default
		// and/or there are interviews using this script
		List<PositionEntity> plist = script.getPositionsWithScriptDefault(); 
		if (plist != null || script.getInterviewsUsingScript() != null) {
			// clone script
			ScriptEntity newScript = new ScriptEntity(script, script.getTitle(),
					questions, script.getComments(), true, creator);
			save(newScript);
			// old script cannot be used anymore
			script.setReusable(false);
			
			// change position default script, if any
			if (plist != null)
				for (PositionEntity p : plist)
					p.setDefaultScript(newScript);
		} else { // no position/interviews using this script
			script.setQuestions(questions);
			scriptDAO.update(script);
		}
	}

	@Override
	public ScriptEntity find(Long id) {
		log.info("Finding script by ID");
		return scriptDAO.find(id);
	}

	@Override
	public List<ScriptEntity> findAll() {
		log.info("Creating Query for all scripts");
		return scriptDAO.findAll();
	}

	@Override
	public List<ScriptEntity> findReusableScripts() {
		log.info("Finding all reusable scripts");
		return scriptDAO.findReusableScripts();
	}

	@Override
	public List<ScriptEntity> findScriptsByTitle(String title) {
		log.info("Finding scripts by title");
		return scriptDAO.findScriptsByTitle(title);
	}

	private void isScriptComplete(ScriptEntity script) {
		boolean hasError = false;

		if (script == null) hasError = true;
		else if (script.getTitle() == null) hasError = true;
		else if (script.getCreationDate() == null) hasError = true;
		else if (script.getScriptCreator() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The script is missing data. "
					+ "Check the notnull attributes.");
	}

}
