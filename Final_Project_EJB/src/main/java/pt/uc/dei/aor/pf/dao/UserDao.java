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
	
	// compor queries

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
		String queryS = "SELECT DISTINCT users.* FROM users, roles"
				+ " WHERE (TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :name"
				+ " OR TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :name)"
				+ " AND users.id = roles.user_id"
				+ " AND roles.role <> \'CANDIDATE\' ORDER BY email";
		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("name", name);
		return (List<UserEntity>) query.getResultList();

		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		parameters.put("name", name);
		//		return super.findSomeResults("User.findUsersByName", parameters);
	}

	// pesquisar por várias cenas (cada atributo com sua pattern???)
	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByRole(String keyword, String role) {
		String queryS = "SELECT DISTINCT users.* FROM users, roles"
				+ " WHERE UPPER(email) LIKE :keyword"
				+ " OR (TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword"
				+ " OR TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword)"
				+ " AND users.id = roles.user_id"
				+ " AND roles.role :role ORDER BY email";
		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("keyword", keyword);
		query.setParameter("role", role);
		return (List<UserEntity>) query.getResultList();
		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		parameters.put("keyword", keyword);
		//		parameters.put("role", role);
		//		return super.findSomeResults("User.findUsersByKeywordByRole", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsersByKeyword(String keyword) {
		String queryS = "SELECT DISTINCT users.* FROM users, roles"
				+ " WHERE (UPPER(email) LIKE :keyword"
				+ " OR TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword"
				+ " OR TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword)"
				+ " AND users.id = roles.user_id"
				+ " AND roles.role <> \'CANDIDATE\' ORDER BY email";
		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("keyword", keyword);
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
		String queryS = "SELECT users.* FROM users, users_info"
				+ " WHERE (TRANSLATE(UPPER(REPLACE(address,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword"
				+ " OR TRANSLATE(UPPER(REPLACE(city,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword"
				+ " OR TRANSLATE(UPPER(REPLACE(country,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword"
				+ " OR TRANSLATE(UPPER(REPLACE(course,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword"
				+ " OR TRANSLATE(UPPER(REPLACE(school,\' \',\'\')), "
				+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :keyword)"
				+ " AND users.id = users_info.user_id ORDER BY email";
		Query query = em.createNativeQuery(queryS, UserEntity.class);
		query.setParameter("keyword", keyword);
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
		if (position != null) {
			queryS = "SELECT users.* FROM users, users_info, submissions"
					+ " WHERE TRANSLATE(UPPER(REPLACE(email,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :email"
					+ " AND TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :fname"
					+ " AND TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :lname"
					+ " AND TRANSLATE(UPPER(REPLACE(address,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :address"
					+ " AND TRANSLATE(UPPER(REPLACE(city,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :city"
					+ " AND TRANSLATE(UPPER(REPLACE(country,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :country"
					+ " AND TRANSLATE(UPPER(REPLACE(course,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :course"
					+ " AND TRANSLATE(UPPER(REPLACE(school,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :school"
					+ " AND users.id = users_info.user_id AND users.id = submission.candidate"
					+ " AND submission.position = :id ORDER BY email";
			query = em.createNativeQuery(queryS, UserEntity.class);
			query.setParameter("id", position.getId());
//			return super.findSomeResults("User.findCandidatesByPosition", parameters);
		} else {
			queryS = "SELECT users.* FROM users, users_info"
					+ " WHERE TRANSLATE(UPPER(REPLACE(email,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :email"
					+ " AND TRANSLATE(UPPER(REPLACE(first_name,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :fname"
					+ " AND TRANSLATE(UPPER(REPLACE(last_name,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :lname"
					+ " AND TRANSLATE(UPPER(REPLACE(address,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :address"
					+ " AND TRANSLATE(UPPER(REPLACE(city,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :city"
					+ " AND TRANSLATE(UPPER(REPLACE(country,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :country"
					+ " AND TRANSLATE(UPPER(REPLACE(course,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :course"
					+ " AND TRANSLATE(UPPER(REPLACE(school,\' \',\'\')), "
					+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+") LIKE :school"
					+ " AND users.id = users_info.user_id ORDER BY email";
			query = em.createNativeQuery(queryS, UserEntity.class);
		}
		//		return super.findSomeResults("User.findCandidatesBySeveralAttributes", parameters);
		query.setParameter("email", email);
		query.setParameter("fname", fname);
		query.setParameter("lname", lname);
		query.setParameter("address", address);
		query.setParameter("city", city);
		query.setParameter("country", country);
		query.setParameter("course", course);
		query.setParameter("school", school);
		return (List<UserEntity>) query.getResultList();
	}

}