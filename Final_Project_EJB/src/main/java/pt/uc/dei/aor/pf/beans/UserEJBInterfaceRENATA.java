package pt.uc.dei.aor.pf.beans;

import java.util.List;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface UserEJBInterfaceRENATA {
	public abstract void save(UserEntity user);
	public abstract void update(UserEntity user);
	public abstract void delete(UserEntity user);
	public abstract UserEntity find(Long id);
	public abstract List<UserEntity> findAll();
	public abstract UserEntity findUsersByEmail(String email);  //list? 
	public abstract List<UserEntity> findUsersByFirstName(String firstName);
	public abstract List<UserEntity> findUsersByLastName(String lastName); 
	public abstract List<UserEntity> findUsersByRole(String role); // list of roles!!!
	public abstract List<UserEntity> findUsers(String email, String firstName, String lastName,
			String role);
	public abstract List<UserEntity> findCandidatesByFirstName(String firstName);
	public abstract List<UserEntity> findCandidatesByLastName(String lastName); 
	public abstract List<UserEntity> findCandidatesByEmail(String email);
//	public abstract List<UserEntity> findCandidates(String email, String firstName, String lastName);
	public abstract List<UserEntity> findCandidatesByPosition(String email, String firstName, String lastName,
			PositionEntity position); // position can be null
	// ordenar pesquisas por data/ordem alfabética??
	
	public abstract List<UserEntity> findInterviewers(InterviewEntity interview);  //crazy??? get???
		
}