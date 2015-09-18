package pt.uc.dei.aor.pf.beans;

import java.util.List;

import pt.uc.dei.aor.pf.entities.ScriptEntity;

public interface ScriptEJBInterface {

	public abstract void save(ScriptEntity script);
	public abstract void update(ScriptEntity script);
	public abstract int delete(ScriptEntity script);
	public abstract ScriptEntity find(Long id);
	public abstract List<ScriptEntity> findAll();
	public abstract List<ScriptEntity> findReusableScripts();
	public abstract List<ScriptEntity> findScriptsByTitle(String title);
	public abstract List<ScriptEntity> findChildScripts(ScriptEntity script);

//	public abstract List<ScriptEntity> findScriptsByQuestion(
//			QuestionEntity question);  //reports???

}