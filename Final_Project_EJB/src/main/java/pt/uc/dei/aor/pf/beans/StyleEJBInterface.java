package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.StyleEntity;

@Local
public interface StyleEJBInterface {
	
	public abstract void save(StyleEntity style);
	public abstract void update(StyleEntity style);
	public abstract List<StyleEntity> findAll(StyleEntity style);
	public abstract StyleEntity saveAndReturn(StyleEntity style);
	public abstract StyleEntity findDefaulStyle();
	
}
