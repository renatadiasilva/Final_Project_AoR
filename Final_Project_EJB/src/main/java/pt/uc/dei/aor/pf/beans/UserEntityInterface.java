package pt.uc.dei.aor.pf.beans;


import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.UserEntity;


@Local
public interface UserEntityInterface{
	public abstract void save(UserEntity userEntity);
	public abstract void update(UserEntity userEntity);
	public abstract void delete(UserEntity userEntity);
}