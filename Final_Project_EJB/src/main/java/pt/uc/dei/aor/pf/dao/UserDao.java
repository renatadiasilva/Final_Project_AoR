package pt.uc.dei.aor.pf.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
//import javax.persistence.NamedQuery;
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
		return super.findSomeResults("User.findUsersByEmailPattern", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByName(String name) {
		String[] values = {name, name};
		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(", attributes, values, " OR ", 
				"users.id = roles.user_id AND roles.role <> \'CANDIDATE\'",
				"email");
//		String queryS = "SELECT DISTINCT users.* FROM users, roles"
//				+ " WHERE (TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :first_name"
//				+ " OR TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :last_name)"
//				+ " AND users.id = roles.user_id"
//				+ " AND roles.role <> \'CANDIDATE\' ORDER BY email";
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, UserEntity.class);
//		query.setParameter("first_name", name);
//		query.setParameter("last_name", name);
		return (List<UserEntity>) query.getResultList();

		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		parameters.put("name", name);
		//		return super.findSomeResults("User.findUsersByName", parameters);
	}

	// pesquisar por várias cenas (cada atributo com sua pattern???)
	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByRole(String keyword, String role) {
		String[] values = {keyword, keyword};
		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(UPPER(email) LIKE "+keyword+" OR ", attributes, values, " OR ", 
				"users.id = roles.user_id AND roles.role = "+role,
				"email");
//		String queryS = "SELECT DISTINCT users.* FROM users, roles"
//				+ " WHERE (UPPER(email) LIKE :email"
//				+ " OR TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :first_name"
//				+ " OR TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :last_name)"
//				+ " AND users.id = roles.user_id"
//				+ " AND roles.role :role ORDER BY email";
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, UserEntity.class);
//		query.setParameter("email", keyword);
//		query.setParameter("first_name", keyword);
//		query.setParameter("last_name", keyword);
//		query.setParameter("role", role);
		return (List<UserEntity>) query.getResultList();
		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		parameters.put("keyword", keyword);
		//		parameters.put("role", role);
		//		return super.findSomeResults("User.findUsersByKeywordByRole", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByKeyword(String keyword) {
		String[] values = {keyword, keyword};
		String[] attributes = {"first_name", "last_name"};
		String queryS = makeQuery("DISTINCT users.*", "users, roles",
				"(UPPER(email) LIKE "+keyword+" OR ", attributes, values, " OR ", 
				"users.id = roles.user_id AND roles.role <> \'CANDIDATE\'",
				"email");
//		String queryS = "SELECT DISTINCT users.* FROM users, roles"
//				+ " WHERE (UPPER(email) LIKE :email"
//				+ " OR TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :first_name"
//				+ " OR TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :last_name)"
//				+ " AND users.id = roles.user_id"
//				+ " AND roles.role <> \'CANDIDATE\' ORDER BY email";
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, UserEntity.class);
//		query.setParameter("email", keyword);
//		query.setParameter("first_name", keyword);
//		query.setParameter("last_name", keyword);
		return (List<UserEntity>) query.getResultList();
		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		parameters.put("keyword", keyword);
		//		return super.findSomeResults("User.findUsersByKeyword", parameters);
	}

	public List<UserEntity> findCandidatesByPhone(String phone) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("phone", phone);
		return super.findSomeResults("User.findCandidatesByPhone", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findCandidatesByKeyword(String keyword) {
		String[] values = {keyword, keyword, keyword, keyword, keyword};
		String[] attributes = {"address", "city", "country", "course", "school"};
		String queryS = makeQuery("users.*", "users, users_info", "(", attributes, values, " OR ",
				"users.id = users_info.user_id", "email");

//		String queryS = "SELECT users.* FROM users, users_info"
//				+ " WHERE (TRANSLATE(UPPER(REPLACE(address,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :address"
//				+ " OR TRANSLATE(UPPER(REPLACE(city,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :city"
//				+ " OR TRANSLATE(UPPER(REPLACE(country,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :country"
//				+ " OR TRANSLATE(UPPER(REPLACE(course,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :course"
//				+ " OR TRANSLATE(UPPER(REPLACE(school,\' \',\'\')), "
//				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :school)"
//				+ " AND users.id = users_info.user_id ORDER BY email";
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, UserEntity.class);
//		query.setParameter("address", keyword);
//		query.setParameter("city", keyword);
//		query.setParameter("country", keyword);
//		query.setParameter("course", keyword);
//		query.setParameter("school", keyword);
		return (List<UserEntity>) query.getResultList();
		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		parameters.put("keyword", keyword);
		//		return super.findSomeResults("User.findCandidatesByKeyword", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findCandidates(String email, String fname, 
			String lname, String address, String city, 
			String country, String course, String school, 
			PositionEntity position) {
		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		parameters.put("email", email);
		//		parameters.put("fname", fname);
		//		parameters.put("lname", lname);
		//		parameters.put("address", address);
		//		parameters.put("city", city);
		//		parameters.put("country", country);
		//		parameters.put("course", course);
		//		parameters.put("school", school);
		String queryS;
		Query query;
		String[] values = {fname, lname, address, city, country, course, school};
		String[] attributes = {"first_name", 
				"last_name", "address", "city", "country", "course", "school"};
		if (position != null) {
			queryS = makeQuery("users.*", "users, users_info, submissions", 
					"(UPPER(email) LIKE "+email+" AND ", attributes, values, " AND ",
					"users.id = users_info.user_id AND users.id = submissions.candidate"
					+ " AND submissions.position.id = "+position.getId(), "email");

//			queryS = "SELECT users.* FROM users, users_info, submissions"
//					+ " WHERE *UPPER(email) LIKE :email"
//					+ " AND TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :fname"
//					+ " AND TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :lname"
//					+ " AND TRANSLATE(UPPER(REPLACE(address,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :address"
//					+ " AND TRANSLATE(UPPER(REPLACE(city,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :city"
//					+ " AND TRANSLATE(UPPER(REPLACE(country,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :country"
//					+ " AND TRANSLATE(UPPER(REPLACE(course,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :course"
//					+ " AND TRANSLATE(UPPER(REPLACE(school,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :school"
//					+ " AND users.id = users_info.user_id AND users.id = submissions.candidate"
//					+ " AND submissions.position = :position ORDER BY email";
//			query.setParameter("position", position);
//			return super.findSomeResults("User.findCandidatesByPosition", parameters);
		} else {
			queryS = makeQuery("users.*", "users, users_info", "(UPPER(email) LIKE "+email+" AND ", 
					attributes, values, " AND ", "users.id = users_info.user_id", "email");
			
//			queryS = "SELECT users.* FROM users, users_info"
//					+ " WHERE (UPPER(email) LIKE :email"
//					+ " AND TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :first_name"
//					+ " AND TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :last_name"
//					+ " AND TRANSLATE(UPPER(REPLACE(address,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :address"
//					+ " AND TRANSLATE(UPPER(REPLACE(city,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :city"
//					+ " AND TRANSLATE(UPPER(REPLACE(country,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :country"
//					+ " AND TRANSLATE(UPPER(REPLACE(course,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :course"
//					+ " AND TRANSLATE(UPPER(REPLACE(school,\' \',\'\')), "
//					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :school"
//					+ " AND users.id = users_info.user_id ORDER BY email";
		}
		//		return super.findSomeResults("User.findCandidatesBySeveralAttributes", parameters);
//		query.setParameter("email", email);
//		query.setParameter("first_name", fname);
//		query.setParameter("last_name", lname);
//		query.setParameter("address", address);
//		query.setParameter("city", city);
//		query.setParameter("country", country);
//		query.setParameter("course", course);
//		query.setParameter("school", school);
		System.out.println(queryS);
		query = em.createNativeQuery(queryS, UserEntity.class);
		return (List<UserEntity>) query.getResultList();
	}

}