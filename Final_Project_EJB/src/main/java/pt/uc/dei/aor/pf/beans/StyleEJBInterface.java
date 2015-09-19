package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.StyleEntity;

@Local
public interface StyleEJBInterface {
	
	public abstract void save(StyleEntity style);
	public abstract void update(StyleEntity style);
	public abstract void delete(StyleEntity style);
	public abstract List<StyleEntity> findAll();
	public abstract StyleEntity saveAndReturn(StyleEntity style);
	public abstract StyleEntity findDefaulStyle();
	
}
