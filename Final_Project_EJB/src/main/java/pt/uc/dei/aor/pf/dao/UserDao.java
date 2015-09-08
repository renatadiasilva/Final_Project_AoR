package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class UserDao extends OLD_GenericDao<UserEntity>{

	public UserDao() {
		super(UserEntity.class);
	}

}
