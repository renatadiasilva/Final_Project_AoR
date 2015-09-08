package pt.uc.dei.aor.pf.beans;

import java.util.List;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

public interface UserInfoEJBInterface {
	
	public abstract void save(UserInfoEntity user);
	public abstract void update(UserInfoEntity user);
	public abstract void delete(UserInfoEntity user);
	//keyword
	public abstract List<UserInfoEntity> findCandidatesByAddress(String address);
	public abstract List<UserInfoEntity> findCandidatesByCity(String city);
	public abstract List<UserInfoEntity> findCandidatesByPhone(String phone);
	public abstract List<UserInfoEntity> findCandidatesByMobile(String mobilePhone);
	public abstract List<UserInfoEntity> findCandidatesByCountry(String country);
	public abstract List<UserInfoEntity> findCandidatesByCourse(String course);
	public abstract List<UserInfoEntity> findCandidatesBySchool(String school);	
	public abstract List<UserInfoEntity> findCandidatesByKeyword(String keyword,
			PositionEntity position); // position can be null
	public abstract List<UserInfoEntity> findCandidates(String address, String city,
			String phone, String mobilePhone, String country, String course, String school,
			PositionEntity position); // position can be null
}
