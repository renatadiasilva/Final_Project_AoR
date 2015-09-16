package pt.uc.dei.aor.pf.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class UserDao extends GenericDao<UserEntity> {
	
	public UserDao() {
		super(UserEntity.class);
	}
	
	public List<UserEntity> findUserByEmail(String email) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		return super.findSomeResults("User.findUserByEmail", parameters);
	}

	public List<UserEntity> findUsersByEmailPattern(String email) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		parameters.put("role", UserEntity.ROLE_CANDIDATE);
		return super.findSomeResults("User.findUsersByEmailPattern", 
				parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByName(String name) {

		String[] values = {name, name};
		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(", attributes, values, " OR ", 
				"users.id = roles.user_id AND roles.role <> \'"+
				UserEntity.ROLE_CANDIDATE+"\'","email");
		
		Query query = em.createNativeQuery(queryS, UserEntity.class);
		return (List<UserEntity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByRole(String keyword, String role) {

		String[] values = {keyword, keyword};
		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(UPPER(email) LIKE \'"+keyword+"\' OR ", attributes, values,
				" OR ", "users.id = roles.user_id AND roles.role = \'"+
				role+"\'", "email");

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		return (List<UserEntity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByKeyword(String keyword) {

		String[] values = {keyword, keyword};
		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(UPPER(email) LIKE \'"+keyword+"\' OR ", attributes, values,
				" OR ", "users.id = roles.user_id AND roles.role <> \'"+
				UserEntity.ROLE_CANDIDATE+"\'",	"email");

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		return (List<UserEntity>) query.getResultList();
	}

	public List<UserEntity> findCandidatesByPhone(String phone) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("phone", phone);
		return super.findSomeResults("User.findCandidatesByPhone", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findCandidatesByKeyword(String keyword) {

		String[] values = {keyword, keyword, keyword, keyword, keyword};
		String[] attributes = {"address", "city", "country", "course",
				"school"};
		String queryS = makeQuery("users.*", "users, users_info", "(", 
				attributes, values, " OR ",
				"users.id = users_info.user_id", "email");

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		return (List<UserEntity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findCandidates(String email, String fname, 
			String lname, String address, String city, 
			String country, String course, String school, 
			PositionEntity position) {

		String[] values = {fname, lname, address, city, country, course,
			school};
		String[] attributes = {"first_name", "last_name", "address", "city", 
			"country", "course", "school"};
		
		String queryS;
		if (position != null) queryS = makeQuery("users.*", 
			"users, users_info, submissions, positions", 
			"(UPPER(email) LIKE \'"+email+"\' AND ", attributes, 
			values, " AND ", "users.id = users_info.user_id AND"
			+ " users.id = submissions.candidate"
			+ " AND submissions.position = positions.id AND"
			+ " positions.id = "+position.getId(), "email");
		else queryS = makeQuery("users.*", "users, users_info", 
			"(UPPER(email) LIKE \'"+email+"\' AND ", attributes, values, 
			" AND ", "users.id = users_info.user_id", "email");
		
		Query query = em.createNativeQuery(queryS, UserEntity.class);
		return (List<UserEntity>) query.getResultList();

	}

}