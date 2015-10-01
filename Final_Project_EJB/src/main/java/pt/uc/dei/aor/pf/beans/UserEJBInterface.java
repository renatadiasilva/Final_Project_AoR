package pt.uc.dei.aor.pf.beans;

import java.util.List;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface UserEJBInterface {
	
	public abstract void save(UserEntity user);
	public abstract void update(UserEntity user);
	public abstract void updatePassword(UserEntity user);
	public abstract void deleteData(UserEntity user);
	public abstract UserEntity find(Long id);
	public abstract List<UserEntity> findAll();
	public abstract List<UserEntity> findAllNotRemoved();
	public abstract List<UserEntity> findRemovedEmails();
	public abstract UserEntity findUserByEmail(String email);
	public abstract List<UserEntity> findUsersByEmail(String emailPattern); 
	public abstract List<UserEntity> findUsersByName(String name);
	public abstract List<UserEntity> findAllAdmins();
	public abstract List<UserEntity> findAllManagers();
	public abstract List<UserEntity> findAllInterviewers();
	public abstract List<UserEntity> findAllCandidates();
	public abstract List<UserEntity> findUsersByKeywordAndRole(String keyword,
			String role);
	public abstract List<UserEntity> findUsersByKeyword(String keyword);
	public abstract List<UserEntity> findCandidatesByFirstName(
			String firstName, UserEntity manager);
	public abstract List<UserEntity> findCandidatesByLastName(String lastName,
			UserEntity manager); 
	public abstract List<UserEntity> findCandidatesByEmail(String email,
			UserEntity manager);
	public abstract List<UserEntity> findCandidatesByAddress(String address,
			UserEntity manager);
	public abstract List<UserEntity> findCandidatesByCity(String city,
			UserEntity manager);
	public abstract List<UserEntity> findCandidatesByPhone(String phone,
			UserEntity manager);
	public abstract List<UserEntity> findCandidatesByCountry(String country,
			UserEntity manager);
	public abstract List<UserEntity> findCandidatesByCourse(String course,
			UserEntity manager);
	public abstract List<UserEntity> findCandidatesBySchool(String school,
			UserEntity manager);	
	public abstract List<UserEntity> findCandidatesByPosition(
			PositionEntity position);
	public abstract List<UserEntity> findCandidatesByPosition(String email, 
			String firstName, String lastName, PositionEntity position);
	public abstract List<UserEntity> findCandidatesByPosition(String email,
			String firstName, String lastName, String address, String city,
			String country, String course, String school, 
			PositionEntity position);
	public abstract List<UserEntity> findCandidates(String email,
			String firstName, String lastName, String address, String city, 
			String country, String course, String school);
	public abstract List<UserEntity> findCandidatesByKeyword(String keyword,
			UserEntity manager);
	public abstract List<UserEntity> findCandidatesByKeywordShort(
			String keyword);
	public abstract boolean checkPassword(UserEntity user, String password);
	public abstract List<UserEntity> findAllCandidatesWithInfo(UserEntity manager);	
}