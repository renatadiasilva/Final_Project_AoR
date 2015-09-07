package pt.uc.dei.aor.pf.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uc.dei.aor.pf.entities.ScriptEntity;

public class ScriptDao extends GenericDao<ScriptEntity> {

	public ScriptDao() {
		super(ScriptEntity.class);
	}
	
	public List<ScriptEntity> findReusableScripts() {
		return super.findSomeResults("Script.reusableScripts", null);
	}

	public List<ScriptEntity> findScriptsByTitle(String title) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("t", title);
		return super.findSomeResults("Script.scriptsByTitle", parameters);
	}

}
