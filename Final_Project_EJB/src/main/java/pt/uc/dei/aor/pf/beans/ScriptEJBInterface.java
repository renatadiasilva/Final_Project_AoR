package pt.uc.dei.aor.pf.beans;

import java.util.List;

import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface ScriptEJBInterface {
	public abstract void save(ScriptEntity script);
	public abstract void update(ScriptEntity script);
	public abstract void delete(ScriptEntity script);
	public abstract ScriptEntity find(Long id);
//	public abstract List<ScriptEntity> findAll();
	public abstract List<ScriptEntity> findReusableScripts();
	public abstract List<ScriptEntity> findScriptByTitle(String title);  //será preciso???
	public abstract List<ScriptEntity> findScriptByAdmin(UserEntity admin);

}
