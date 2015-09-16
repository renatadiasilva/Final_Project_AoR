package pt.uc.dei.aor.pf.dao;

import java.util.List;

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
