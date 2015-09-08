package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class TestUserInfoDao extends GenericDao<UserInfoEntity> {
	
	public TestUserInfoDao() {
		super(UserInfoEntity.class);
	}

	public UserInfoEntity findUserInfoByUserID(Long userID) {
		Query q = em.createQuery("select ui from UserInfoEntity ui where ui.owner = :userID");
		q.setParameter("userID", userID);
		
		try {
			UserInfoEntity userInfo = (UserInfoEntity) q.getSingleResult();
			return userInfo;
		} catch (NoResultException e) {
			return null;
		}
	}

}