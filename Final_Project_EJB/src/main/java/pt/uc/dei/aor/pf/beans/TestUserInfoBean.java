package pt.uc.dei.aor.pf.beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.dao.TestUserInfoDao;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class TestUserInfoBean implements TestUserInfoInterface {

	@EJB
	private TestUserInfoDao testUserInfoDao;

	@Override
	public void save(UserInfoEntity userInfo){		
		this.testUserInfoDao.save(userInfo);
	}

	@Override
	public void update(UserInfoEntity userInfo){
		this.testUserInfoDao.update(userInfo);
	}

	@Override
	public void delete(UserInfoEntity userInfo) {
		this.testUserInfoDao.update(userInfo);
	}
	
}