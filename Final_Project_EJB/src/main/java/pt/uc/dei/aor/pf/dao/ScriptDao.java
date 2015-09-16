package pt.uc.dei.aor.pf.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class ScriptDao extends GenericDao<ScriptEntity> {

	public ScriptDao() {
		super(ScriptEntity.class);
	}

	public List<ScriptEntity> findReusableScripts() {
		return super.findSomeResults("Script.findReusableScripts", null);
	}

	public void delete(ScriptEntity script) {
		// there are open positions with this script as default
		List<PositionEntity> plist = script.getPositionsWithScriptDefault(); 
		if (plist != null) { // empty??
			// erro: avisar para o admin ir mudar o default das posições
			// e listá-las
			
			// ou apresentar logo uma lista com a posições e scripts??
			return;
		}
		
		// there are interviews using this script
		List<InterviewEntity> ilist = script.getInterviewsUsingScript(); 
		if (ilist != null) {
			// script cannot be used anymore
			script.setReusable(false);
		} else {
			List<QuestionEntity> qlist = script.getQuestions();
			if (qlist != null) {
				// fazer script.removeAllQuestions();
				//script.removeQuestion(question);
			}
			else delete(script, ScriptEntity.class);
		}
		
			// remove the data not the script
//			script.setDerivedFrom(null);
//			script.setTitle(REMOVED_DATA);
//			Calendar cal = Calendar.getInstance();
//			cal.set(1900, 1, 1);
//			script.setCreationDate(cal.getTime());
//			script.setComments(REMOVED_DATA);
//			script.setReusable(false);
//			update(script);
	}

	public void edit(ScriptEntity script, String title,
			List<QuestionEntity> questions, String comments,
			UserEntity creator) {	

		// there are positions with this script as default
		// and/or there are interviews using this script
		List<PositionEntity> plist = script.getPositionsWithScriptDefault(); 
		List<InterviewEntity> ilist = script.getInterviewsUsingScript(); 
		if (plist != null || ilist != null) {
			// clone script
			ScriptEntity newScript = new ScriptEntity(script, title,
					questions, comments, true, creator);
			save(newScript);
			// old script cannot be used anymore
			script.setReusable(false);
			
			// change position default script, if any
			if (plist != null)
				for (PositionEntity p : plist)
					p.setDefaultScript(newScript);
		} else { // no position/interviews using this script
			script.setTitle(title);
			script.setQuestions(questions);
//				script.setCreationDate(new Date()); mudar?
			script.setComments(comments);
			update(script);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ScriptEntity> findScriptsByTitle(String title) {

		String[] values = {title};
		String[] attributes = {"title"};
		String queryS = makeQuery("*", "scripts", "(", attributes,
				values, " OR ", "", "title");

		Query query = em.createNativeQuery(queryS, ScriptEntity.class);
		return (List<ScriptEntity>) query.getResultList();

	}

}
