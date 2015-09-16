package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.ScriptDao;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class ScriptEJBImp implements ScriptEJBInterface {

	private static final Logger log = 
			LoggerFactory.getLogger(ScriptEJBImp.class);
	
	@EJB
	private ScriptDao scriptDAO;
	
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
		scriptDAO.delete(script);
	}
	
	@Override
	public void edit(ScriptEntity script, String title,
			List<QuestionEntity> questions, String comments, 
			UserEntity creator) {
		log.info("Editing/Cloning script from DB");
		scriptDAO.edit(script, title, questions, comments, creator);
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
