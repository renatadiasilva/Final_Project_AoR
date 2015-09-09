package pt.uc.dei.aor.pf.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class UserDao extends GenericDao<UserEntity> {

	public UserDao() {
		super(UserEntity.class);
	}

	public List<UserEntity> findUsersByEmail(String email) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		return super.findSomeResults("User.findUsersByEmail", parameters);
	}
	
	public List<UserEntity> findUsersByName(String name) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", name);
		return super.findSomeResults("User.findUsersByName", parameters);
	}
	
	// pesquisar por várias cenas (cada atributo com sua pattern???)
	public List<UserEntity> findUsersByRole(String keyword, String role) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("keyword", keyword);
		parameters.put("role", role);
		return super.findSomeResults("User.findUsersByKeywordByRole", parameters);
	}

	public List<UserEntity> findUsersByKeyword(String keyword) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("keyword", keyword);
		return super.findSomeResults("User.findUsersByKeyword", parameters);
	}

	public List<UserEntity> findCandidates(String email, String fname, 
			String lname, String address, String city, 
			String country, String course, String school, 
			PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);
		parameters.put("fname", fname);
		parameters.put("lname", lname);
		parameters.put("address", address);
		parameters.put("city", city);
		parameters.put("country", country);
		parameters.put("course", course);
		parameters.put("school", school);
		if (position != null) {
			parameters.put("id", position);
			return super.findSomeResults("User.findCandidatesByPosition", parameters);
		}
		return super.findSomeResults("User.findCandidatesBySeveralAttributes", parameters);
	}

	public List<UserEntity> findCandidatesByPhone(String hPhone, String mPhone) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("hphone", hPhone);
		parameters.put("mphone", mPhone);
		return super.findSomeResults("User.findCandidatesByPhone", parameters);
	}

	public List<UserEntity> findCandidatesByKeyword(String keyword) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("keyword", keyword);
		return super.findSomeResults("User.findCandidatesByKeyword", parameters);
	}
	
	// tirar
	public List<UserEntity> findTest(String role, String keyword) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("role", UserEntity.ROLE_MANAGER);
		parameters.put("keyword", keyword);
		return super.findSomeResults("User.findTest", parameters);
	}
	
}