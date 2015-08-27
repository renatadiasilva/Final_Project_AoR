package pt.uc.dei.aor.pf.dao;

import pt.uc.dei.aor.pf.entities.UserEntity;

public class UserDao extends GenericDao<UserEntity>{

	public UserDao(Class<UserEntity> entityClass) {
		super(UserEntity.class);
	}

}
