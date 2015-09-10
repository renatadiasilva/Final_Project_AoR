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
	
	final String ACCENT_LETTERS = "\'àáâãäåāăąÀÁÂÃÄÅĀĂĄèééêëēĕėęěÉÊĒĔĖĘĚ"
			+ "ìíîïìĩīĭÌÍÎÏÌĨĪĬóôõöōŏőÒÓÔÕÖŌŎŐùúûüũūŭůÙÚÛÜŨŪŬŮÇç\'";
	final String NO_ACCENT_LETTERS = "\'aaaaaaaaaAAAAAAAAAeeeeeeeeeeEEEEEEE"
			+ "iiiiiiiiIIIIIIIIoooooooOOOOOOOOuuuuuuuuUUUUUUUUcc\'";
			
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
	@SuppressWarnings("unchecked")
	public List<UserEntity> findTest() {
		String pattern = "jose";
		String queryS = "select * from users"
				+ " where translate(first_name, "+ACCENT_LETTERS+","+NO_ACCENT_LETTERS+")"
				+ " like :pattern";
        Query query = em.createNativeQuery(queryS, UserEntity.class);
        query.setParameter("pattern", pattern);
        return (List<UserEntity>) query.getResultList();
	}
	
}