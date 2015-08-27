package pt.uc.dei.aor.pf.beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.dao.UserEntityDao;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class UserEntityBean implements UserEntityInterface {

	@EJB
	private UserEntityDao loginTestsDao;

	@Override
	public void save(UserEntity user){
		loginTestsDao.save(user);
	}

	@Override
	public void update(UserEntity user){
		loginTestsDao.update(user);
	}

	@Override
	public void delete(UserEntity user) {
		loginTestsDao.update(user);
	}
}
