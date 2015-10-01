package pt.uc.dei.aor.pf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class UserDao extends GenericDao<UserEntity> {

	public UserDao() {
		super(UserEntity.class);
	}

	@Override
	public void save (UserEntity user){
		List<String>verifyRoles=user.getRoles();
		user.setRoles(this.verifyRoles(verifyRoles));
		super.save(user);
	}

	@Override
	public UserEntity update (UserEntity user){
		List<String>verifyRoles=user.getRoles();
		user.setRoles(this.verifyRoles(verifyRoles));
		return super.update(user);
	}

	private List<String> verifyRoles(List<String>roles){
		// Se é ADMIN ou MANAGER tem de ser também INTERVIEWER

		List<String>verifyRoles=new ArrayList<>();

		verifyRoles.addAll(roles);

		if(verifyRoles.contains(Constants.ROLE_INTERVIEWER))
			return verifyRoles;

		if(verifyRoles.contains(Constants.ROLE_ADMIN)){
			verifyRoles.add(Constants.ROLE_INTERVIEWER);
			return verifyRoles;
		}

		if(verifyRoles.contains(Constants.ROLE_MANAGER))
			verifyRoles.add(Constants.ROLE_INTERVIEWER);

		return verifyRoles;
	}

	public List<UserEntity> findAllNotRemoved() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("removed", "%"+Constants.REMOVED_DATA+"%");
		return super.findSomeResults("User.findAllNotRemoved", 
				parameters);
	}

	public List<UserEntity> findUserByEmail(String email) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		return super.findSomeResults("User.findUserByEmail", parameters);
	}

	public List<UserEntity> findUsersByEmailPattern(String email) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		parameters.put("removed", "%"+Constants.REMOVED_DATA+"%");
		return super.findSomeResults("User.findUsersByEmailPattern", 
				parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByName(String name) {

		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(", attributes, " OR ", 
				"users.id = roles.user_id"
						+ " AND email NOT LIKE :removed", "email");

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("first_name", name);
		query.setParameter("last_name", name);
		query.setParameter("removed", "%"+Constants.REMOVED_DATA+"%");
		return (List<UserEntity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByRole(String keyword, String role) {

		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(UPPER(email) LIKE :email OR ", attributes,
				" OR ", "users.id = roles.user_id AND roles.role = :role"
						+ " AND email NOT LIKE :removed",	"email");

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("email", keyword);
		query.setParameter("first_name", keyword);
		query.setParameter("last_name", keyword);
		query.setParameter("removed", "%"+Constants.REMOVED_DATA+"%");
		query.setParameter("role", role);
		return (List<UserEntity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByKeyword(String keyword) {

		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(UPPER(email) LIKE :email OR ", attributes,
				" OR ", "users.id = roles.user_id"
						+ " AND email NOT LIKE :removed", "email");

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("email", keyword);
		query.setParameter("first_name", keyword);
		query.setParameter("last_name", keyword);
		query.setParameter("removed", "%"+Constants.REMOVED_DATA+"%");
		return (List<UserEntity>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findCandidatesByPhone(String phone,
			UserEntity manager) {
		
		if (manager == null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("phone", phone);
			parameters.put("removed", Constants.REMOVED_DATA);
			return super.findSomeResults("User.findCandidatesByPhone", parameters);
		}
		
		String queryS = "DISTINCT SELECT users.*"
				+ " FROM users, users_info, submissions, positions"
				+ " WHERE home_phone LIKE :phone"
				+ " OR mobile_phone LIKE :phone"
				+ " AND email NOT LIKE :removed"
				+ " AND users.id = submissions.candidate"
				+ " AND submissions.position = positions.id"
				+ " AND positions.manager = :id"
				+ " AND users.id = users_info.user_id"
				+ " ORDER BY first_name";

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("phone", phone);
		query.setParameter("removed", "%"+Constants.REMOVED_DATA+"%");
		if (manager != null) query.setParameter("id", manager.getId());
		return (List<UserEntity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findCandidatesByKeyword(String keyword,
			UserEntity manager) {

		String[] attributes = {"email", "first_name", "last_name",
				"address", "city", "country", "course", "school"};

		String queryS;
		if (manager == null)
			queryS = makeQuery("users.*", "users, users_info", "(", 
					attributes, " OR ",
					"users.id = users_info.user_id"
							+ " AND email NOT LIKE :removed", "first_name");
		else queryS = makeQuery("DISTINCT users.*", 
				"users, users_info, submissions, positions", 
				"(", attributes, 
				" OR ", "users.id = users_info.user_id AND"
						+ " users.id = submissions.candidate"
						+ " AND submissions.position = positions.id AND"
						+ " positions.manager = :id"
						+ " AND email NOT LIKE :removed", "first_name");

		System.out.println("candidates"+queryS);
		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("email", keyword);
		query.setParameter("first_name", keyword);
		query.setParameter("last_name", keyword);
		query.setParameter("address", keyword);
		query.setParameter("city", keyword);
		query.setParameter("country", keyword);
		query.setParameter("course", keyword);
		query.setParameter("school", keyword);
		query.setParameter("removed", "%"+Constants.REMOVED_DATA+"%");
		if (manager != null) query.setParameter("id", manager.getId());
		return (List<UserEntity>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findCandidates(String email, String fname, 
			String lname, String address, String city, 
			String country, String course, String school, 
			PositionEntity position, UserEntity manager) {

		String[] attributes = {"first_name", "last_name", "address", "city", 
				"country", "course", "school"};

		String queryS;
		if (position != null) queryS = makeQuery("DISTINCT users.*", 
				"users, users_info, submissions, positions", 
				"(UPPER(email) LIKE :email AND ", attributes, 
				" AND ", "users.id = users_info.user_id AND"
						+ " users.id = submissions.candidate"
						+ " AND submissions.position = positions.id AND"
						+ " positions.id = :id"
						+ " AND email NOT LIKE :removed", "first_name");
		else if (manager != null) {
			queryS = makeQuery("DISTINCT users.*", 
					"users, users_info, submissions, positions", 
					"(UPPER(email) LIKE :email AND ", attributes, 
					" AND ", "users.id = users_info.user_id AND"
							+ " users.id = submissions.candidate"
							+ " AND submissions.position = positions.id AND"
							+ " positions.manager = :id"
							+ " AND email NOT LIKE :removed", "first_name");
		} else queryS = makeQuery("users.*", "users, users_info", 
				"(UPPER(email) LIKE :email AND ", attributes, 
				" AND ", "users.id = users_info.user_id"
						+ " AND email NOT LIKE :removed", "first_name");

		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("email", email);
		query.setParameter("first_name", fname);
		query.setParameter("last_name", lname);
		query.setParameter("address", address);
		query.setParameter("city", city);
		query.setParameter("country", country);
		query.setParameter("course", course);
		query.setParameter("school", school);
		query.setParameter("removed", "%"+Constants.REMOVED_DATA+"%");
		if (position != null) query.setParameter("id", position.getId());
		else if (manager != null) query.setParameter("id", manager.getId());
		return (List<UserEntity>) query.getResultList();

	}

	public List<UserEntity> findRemovedEmails() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("removed", "%"+Constants.REMOVED_DATA+"%");
		return super.findSomeResults("User.findRemovedEmails", 
				parameters);
	}

}