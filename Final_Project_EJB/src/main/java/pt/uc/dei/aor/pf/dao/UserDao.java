package pt.uc.dei.aor.pf.dao;

import pt.uc.dei.aor.pf.entities.UserEntity;

public class UserDAO extends OLD_GenericDao<UserEntity>{

	public UserDAO(Class<UserEntity> entityClass) {
		super(UserEntity.class);
	}

}
