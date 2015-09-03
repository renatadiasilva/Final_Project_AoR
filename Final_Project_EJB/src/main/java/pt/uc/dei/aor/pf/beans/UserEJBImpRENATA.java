package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.dao.TestUserDao;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class UserEJBImpRENATA implements UserEJBInterfaceRENATA {

	@EJB
	private TestUserDao loginTestsDao;

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

	@Override
	public UserEntity find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserEntity findUsersByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findUsersByRole(String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findUsers(String email, String firstName,
			String lastName, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findInterviewers(InterviewEntity interview) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findUsersByFirstName(String firstName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findUsersByLastName(String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findCandidatesByFirstName(String firstName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findCandidatesByLastName(String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findCandidatesByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> findCandidatesByPosition(String email,
			String firstName, String lastName, PositionEntity position) {
		// TODO Auto-generated method stub
		return null;
	}
}
