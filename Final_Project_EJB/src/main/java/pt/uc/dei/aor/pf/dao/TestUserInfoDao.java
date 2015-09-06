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

//	public UserEntity findUserByEmail(String email) {
//		Query q = em.createQuery("select u from UserEntity u where u.email= :mailParam");
//		q.setParameter("mailParam", email);
//		
//		try {
//			UserEntity userInfo = (UserEntity) q.getSingleResult();
//			return userInfo;
//		} catch (NoResultException e) {
//			return null;
//		}
//	}

}