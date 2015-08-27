package pt.uc.dei.aor.pf.dao;

import pt.uc.dei.aor.pf.entities.UserEntity;

public class UserEntityDao extends GenericDao<UserEntity>{

	public UserEntityDao(Class<UserEntity> entityClass) {
		super(UserEntity.class);
	}

}
