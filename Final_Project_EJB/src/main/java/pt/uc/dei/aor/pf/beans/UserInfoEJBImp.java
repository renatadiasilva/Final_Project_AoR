package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class UserInfoEJBImp implements UserInfoEJBInterface {

	@Override
	public void save(UserInfoEntity user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(UserInfoEntity user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UserInfoEntity user) {
		// TODO Auto-generated method stub
		
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
	public List<UserInfoEntity> findCandidates(String address, String city,
			String phone, String mobilePhone, String country, String course,
			String school, PositionEntity position) {
		// TODO Auto-generated method stub
		return null;
	}

}
