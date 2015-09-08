package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class UserInfoDao extends GenericDao<UserInfoEntity> {

	public UserInfoDao() {
		super(UserInfoEntity.class);
	}

}
