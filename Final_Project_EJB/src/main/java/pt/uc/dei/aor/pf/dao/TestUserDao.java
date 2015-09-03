package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;



import javax.persistence.NoResultException;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class TestUserDao extends GenericDAO<UserEntity> {
	
	public TestUserDao() {
		super(UserEntity.class);
	}

	public UserEntity findUserByEmail(String email) {
		Query q = em.createQuery("select u from UserEntity u where u.email= :mailParam");
		q.setParameter("mailParam", email);
		
		try {
			UserEntity user = (UserEntity) q.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}

}
