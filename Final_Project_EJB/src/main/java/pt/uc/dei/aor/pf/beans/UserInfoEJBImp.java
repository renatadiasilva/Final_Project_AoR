package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.UserInfoDao;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class UserInfoEJBImp implements UserInfoEJBInterface {
	
	private static final Logger log = LoggerFactory.getLogger(UserEJBImp.class);

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
		// change something (visibility?)
		userInfoDAO.update(userInfo);
	}

	@Override
	public List<UserInfoEntity> findCandidatesByAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidatesByCity(String city) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidatesByPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidatesByMobile(String mobilePhone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidatesByCountry(String country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidatesByCourse(String course) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidatesBySchool(String school) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidatesByKeyword(String keyword,
			PositionEntity position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfoEntity> findCandidates(String address, String city,
			String phone, String mobilePhone, String country, String course,
			String school, PositionEntity position) {
		// TODO Auto-generated method stub
		return null;
	}

	private void isUserInfoComplete(UserInfoEntity u) {
		boolean hasError = false;

		if (u == null) hasError = true;
		else if (u.getAddress() == null) hasError = true;
		else if (u.getCity() == null) hasError = true;
		else if (u.getCountry() == null) hasError = true;
		else if (u.getCourse() == null) hasError = true;
		else if (u.getCv() == null) hasError = true;
		else if (u.getMobilePhone() == null) hasError = true;
		else if (u.getOwner() == null) hasError = true;
		else if (u.getSchool() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The userinfo is missing data. "
					+ "Check the notnull attributes.");
	}

}
