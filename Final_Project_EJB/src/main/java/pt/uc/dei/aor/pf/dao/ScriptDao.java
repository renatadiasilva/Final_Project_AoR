package pt.uc.dei.aor.pf.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.entities.ScriptEntity;

@Stateless
public class ScriptDao extends GenericDao<ScriptEntity> {

	public ScriptDao() {
		super(ScriptEntity.class);
	}

	public List<ScriptEntity> findReusableScripts() {
		return super.findSomeResults("Script.findReusableScripts", null);
	}

	public List<ScriptEntity> findAllScripts() {
		return super.findSomeResults("Script.findAllScriptsOrderBy", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<ScriptEntity> findScriptsByTitle(String title) {

		String[] attributes = {"title"};
		String queryS = makeQuery("*", "scripts", "(", attributes,
				" OR ", "", "creation_date DESC, title");

		Query query = em.createNativeQuery(queryS, ScriptEntity.class);
		query.setParameter("title", title);
		return (List<ScriptEntity>) query.getResultList();

	}

	public List<ScriptEntity> findChildScripts(ScriptEntity script) {		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("script", script);
		return super.findSomeResults("Script.findChildScripts", parameters);
	}

}