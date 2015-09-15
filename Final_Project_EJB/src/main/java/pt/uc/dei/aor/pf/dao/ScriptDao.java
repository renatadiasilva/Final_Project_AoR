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
//		String queryS = "SELECT * FROM scripts"
//				+ " WHERE (TRANSLATE(UPPER(REPLACE(title,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :title)"
//				+ " ORDER BY title";
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, ScriptEntity.class);
//		query.setParameter("title", title);
		return (List<ScriptEntity>) query.getResultList();

//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("t", title);
//		return super.findSomeResults("Script.findScriptsByTitle", parameters);
	}

}
