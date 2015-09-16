package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class UserInfoDao extends GenericDao<UserInfoEntity> {

	public UserInfoDao() {
		super(UserInfoEntity.class);
	}
	
	//não usar??
	public void delete(UserInfoEntity userInfo) {
		// remove the data not the user
		userInfo.setBirthDate(null);
		userInfo.setAddress(REMOVED_DATA);
		userInfo.setCity(REMOVED_DATA);
		userInfo.setHomePhone(null);
		userInfo.setMobilePhone("999 999 999");
		userInfo.setCountry(REMOVED_DATA);
		userInfo.setCourse(REMOVED_DATA);
		userInfo.setSchool(REMOVED_DATA);
		userInfo.setCv(null);
		userInfo.setLinkedin(null);
		update(userInfo);
	}
	
}
