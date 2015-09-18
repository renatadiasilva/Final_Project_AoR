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
import pt.uc.dei.aor.pf.entities.ScriptEntity;

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
	public int delete(ScriptEntity script) {
		log.info("Deleting a script from DB");

		// there are open positions with this script as default
		List<PositionEntity> plist = 
				positionDAO.findOpenPositionsByScript(script);
		if (plist != null && !plist.isEmpty()) return -1;

		// there are scheduled interviews associated with this script
		List<InterviewEntity> ilist = 
				interviewDAO.findScheduledInterviewsWithScript(script);
		if (ilist != null && !ilist.isEmpty()) return -2;
		
		// script can be removed but first clean all connections!

		// closed or on hold positions with script
		plist = positionDAO.findNotOpenPositionsByScript(script);
		// there are not open positions with script
		// verificar como faremos com os scripts...
		if (plist != null && !plist.isEmpty())
			for (PositionEntity p : plist) {
				// remove script from position
				p.setDefaultScript(null);
				positionDAO.update(p);
			}

		// carried out interviews with script
		// verificar como faremos com os scripts...
		ilist = interviewDAO.findCarriedOutInterviewsWithScript(script);
		if (ilist != null && !ilist.isEmpty())
			for (InterviewEntity i : ilist) {
				//remove script from interview
				i.setScript(null);
				interviewDAO.update(i);
			}
		
		//tirar??
		// scripts derived from this script
		// verificar como faremos com os scripts...
		List<ScriptEntity> sclist = scriptDAO.findChildScripts(script);
		if (sclist != null && !sclist.isEmpty()) {
			for (ScriptEntity sc : sclist) {
				//remove 'parent' script from 'child' script
				sc.setDerivedFrom(null);
				scriptDAO.update(sc);
			}
		}
		// finally remove script
		scriptDAO.delete(script.getId(), ScriptEntity.class);
		return 0;
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

	@Override
	public List<ScriptEntity> findChildScripts(ScriptEntity script) {
		log.info("Finding scripts derived from a given script");
		return scriptDAO.findChildScripts(script);
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
