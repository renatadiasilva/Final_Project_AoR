package pt.uc.dei.aor.pf.beans;

import pt.uc.dei.aor.pf.entities.UserInfoEntity;

public interface UserInfoEJBInterface {
	
	public abstract void save(UserInfoEntity user);
	public abstract void update(UserInfoEntity user);
	public abstract void delete(UserInfoEntity user);
	
}
