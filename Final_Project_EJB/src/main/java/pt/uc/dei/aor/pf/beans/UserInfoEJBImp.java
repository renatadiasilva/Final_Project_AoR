package pt.uc.dei.aor.pf.beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.UserInfoDao;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class UserInfoEJBImp implements UserInfoEJBInterface {
	
	private static final Logger log = 
			LoggerFactory.getLogger(UserInfoEJBImp.class);

	@EJB
	private UserInfoDao userInfoDAO;

	@Override
	public void save(UserInfoEntity userInfo) {
		log.info("Saving userInfo in DB");
		isUserInfoComplete(userInfo);
		userInfoDAO.save(userInfo);
	}

	@Override
	public void update(UserInfoEntity userInfo) {
		log.info("Updating userInfo of DB");
		isUserInfoComplete(userInfo);
		userInfoDAO.update(userInfo);
	}

	@Override
	public void delete(UserInfoEntity userInfo) {
		log.info("Deleting userInfo from DB");
		userInfoDAO.delete(userInfo, UserInfoEntity.class);
	}

	private void isUserInfoComplete(UserInfoEntity u) {
		boolean hasError = false;

		if (u == null) hasError = true;
		else if (u.getAddress() == null) hasError = true;
		else if (u.getCity() == null) hasError = true;
		else if (u.getCountry() == null) hasError = true;
		else if (u.getCourse() == null) hasError = true;
		else if (u.getMobilePhone() == null) hasError = true;
		else if (u.getOwner() == null) hasError = true;
		else if (u.getSchool() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The user info is missing data. "
					+ "Check the notnull attributes.");
	}

}
