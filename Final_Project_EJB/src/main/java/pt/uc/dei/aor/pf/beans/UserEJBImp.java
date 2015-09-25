package pt.uc.dei.aor.pf.beans;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.io.impl.Base64;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.dao.InterviewDao;
import pt.uc.dei.aor.pf.dao.PositionDao;
import pt.uc.dei.aor.pf.dao.SubmissionDao;
import pt.uc.dei.aor.pf.dao.UserDao;
import pt.uc.dei.aor.pf.dao.UserInfoDao;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class UserEJBImp implements UserEJBInterface {

	private static final Logger log = LoggerFactory.getLogger(UserEJBImp.class);

	@EJB
	private UserDao userDAO;

	@EJB
	private UserInfoDao userInfoDAO;

	@EJB
	private SubmissionDao submissionDAO;

	@EJB
	private PositionDao positionDAO;

	@EJB
	private InterviewDao interviewDAO;

	@Override
	public void save(UserEntity user) {
		log.info("Saving user in DB");
		isUserComplete(user);
		try {
			user.setPassword(securePass(user.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			log.error("Error encrypting password");
		} catch (UnsupportedEncodingException e) {
			log.error("Error encrypting password");
		}
		userDAO.save(user);
	}

	@Override
	public void update(UserEntity user) {
		log.info("Updating user of DB");
		isUserComplete(user);
		userDAO.update(user);
	}

	@Override
	public void updatePassword(UserEntity user) {
		log.info("Updating password of user");
		try {
			user.setPassword(securePass(user.getPassword()));
			user.setTemporaryPassword(false);
		} catch (NoSuchAlgorithmException e) {
			log.error("Error encrypting password");
		} catch (UnsupportedEncodingException e) {
			log.error("Error encrypting password");
		}
		userDAO.update(user);
	}

	@Override
	public boolean checkPassword(UserEntity user, String password){
		try {
			log.info("Checking password");
			if(user.getPassword().equals(this.securePass(password))){
				log.info("Right Password");
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("Error checking password");
		} catch (UnsupportedEncodingException e) {
			log.error("Error checking password");
		}
		log.info("Wrong Password");
		return false;
	}

	@Override
	public int delete(UserEntity user) {
		log.info("Deleting data of user from DB");
		
		int code = 0;
		
		// protection: don't delete data of superAdmin!
		if (user.getEmail().equals(Constants.SUPER_ADMIN)) return -1;
		
		// remove the data not the user
		//		user.setEmail(Constants.REMOVED_DATA); ??
		user.setEmail(user.getEmail()+Constants.REMOVED_DATA);
		user.setPassword(Constants.REMOVED_DATA);
		user.setFirstName(Constants.REMOVED_DATA);
		user.setLastName(Constants.REMOVED_DATA);
		user.setDefaultRole(Constants.REMOVED_DATA);
		user.setRoles(null);

		// remove the userInfo data
		UserInfoEntity userInfo = user.getUserInfo();
		if (userInfo != null) user.setUserInfo(null);

		// tirar fora???
		//there are open positions managed by the given user
		List<PositionEntity> plist = 
				positionDAO.findOpenPositionsManagedByUser(user);
		if (plist != null && !plist.isEmpty()) code++;

		// there are scheduled interviews which have
		// only the given user as interviewer
		List<InterviewEntity> ilist = 
				interviewDAO.findScheduledInterviewsByUser(user);
		if (ilist != null && !ilist.isEmpty()) {
			for (InterviewEntity i : ilist) {
				List<UserEntity> ulist = i.getInterviewers();
				if (ulist != null && ulist.size() == 1) {
					code += 2;
					break;
				}
			}
		}

		userDAO.update(user);
		return code;
		// code =-1, error
		// code = 0, no changes needed
		// code = 1, there are positions but no interviews
		// code = 2, there are interviews but no positions
		// code = 3, there are positions and interviews
		
		// limpar também dados candidaturas/entrevistas
		// realizadas?? 
		// tipo motivation letter/feedback/answers?
	}

	@Override
	public UserEntity find(Long id) {
		log.info("Finding user by ID");
		return userDAO.find(id);
	}

	@Override
	public List<UserEntity> findAll() {
		log.info("Creating Query for all users");
		return userDAO.findAll();
	}

	@Override
	public List<UserEntity> findAllNotRemoved() {
		log.info("Creating Query for all not removed users");
		return userDAO.findAllNotRemoved();
	}

	@Override
	public UserEntity findUserByEmail(String email) {
		log.info("Finding user by exact email");
		// email is unique
		List<UserEntity> u = userDAO.findUserByEmail(email.toUpperCase());
		if (u == null) return null; // 0 results: email not found
		if (u.size() == 1) return u.get(0); // 1 result: email found
		return null; // 0 results: email not found
	}

	@Override
	public List<UserEntity> findUsersByEmail(String emailPattern) {
		log.info("Finding users by email pattern");
		return userDAO.findUsersByEmailPattern(emailPattern);
	}

	@Override
	public List<UserEntity> findUsersByName(String name) {
		log.info("Finding users by name");
		return userDAO.findUsersByName(name);
	}

	@Override
	public List<UserEntity> findAllAdmins() {
		log.info("Finding all administrators");
		return userDAO.findUsersByRole("%", Constants.ROLE_ADMIN);
	}

	@Override
	public List<UserEntity> findAllManagers() {
		log.info("Finding all managers");
		return userDAO.findUsersByRole("%", Constants.ROLE_MANAGER);
	}

	@Override
	public List<UserEntity> findAllInterviewers() {
		log.info("Finding all interviewers");
		return userDAO.findUsersByRole("%", Constants.ROLE_INTERVIEWER);
	}

	@Override
	public List<UserEntity> findAllCandidates() {
		log.info("Finding all candidates");
		return userDAO.findUsersByRole("%", Constants.ROLE_CANDIDATE);
	}

	@Override
	public List<UserEntity> findUsersByKeywordAndRole(String keyword,
			String role) {
		log.info("Finding users by keyword and role");
		return userDAO.findUsersByRole(keyword, role);
	}

	public List<UserEntity> findUsersByKeyword(String keyword) {
		log.info("Finding internal users by keyword and role");
		return userDAO.findUsersByKeyword(keyword);	
	}

	@Override
	public List<UserEntity> findCandidatesByEmail(String email) {
		log.info("Finding candidates by email");
		return userDAO.findCandidates(email, "%", "%", "%", "%", "%", "%",
				"%", null);
	}

	public List<UserEntity> findCandidatesByFirstName(String firstName) {
		log.info("Finding candidates by first name");
		return userDAO.findCandidates("%", firstName, "%", "%", "%", "%",
				"%", "%", null);
	}

	@Override
	public List<UserEntity> findCandidatesByLastName(String lastName) {
		log.info("Finding candidates by last name");
		return userDAO.findCandidates("%", "%", lastName, "%", "%", "%",
				"%", "%", null);
	}

	@Override
	public List<UserEntity> findCandidatesByAddress(String address) {
		log.info("Finding candidates by address");
		return userDAO.findCandidates("%", "%", "%", address, "%", "%", "%",
				"%", null);
	}

	@Override
	public List<UserEntity> findCandidatesByCity(String city) {
		log.info("Finding candidates by city");
		return userDAO.findCandidates("%", "%", "%", "%", city, "%", "%",
				"%", null);
	}

	@Override
	public List<UserEntity> findCandidatesByPhone(String phone) {
		log.info("Finding candidates by (home) phone");
		return userDAO.findCandidatesByPhone(phone);
	}

	@Override
	public List<UserEntity> findCandidatesByCountry(String country) {
		log.info("Finding candidates by country");
		return userDAO.findCandidates("%", "%", "%", "%", "%", country, "%",
				"%", null);
	}

	@Override
	public List<UserEntity> findCandidatesByCourse(String course) {
		log.info("Finding candidates by course");
		return userDAO.findCandidates("%", "%", "%", "%", "%", "%", course, 
				"%", null);
	}

	@Override
	public List<UserEntity> findCandidatesBySchool(String school) {
		log.info("Finding candidates by school");
		return userDAO.findCandidates("%", "%", "%", "%", "%", "%", "%",
				school, null);
	}

	@Override
	public List<UserEntity> findCandidatesByPosition(PositionEntity position) {
		log.info("Finding candidates by position");
		return userDAO.findCandidates("%", "%", "%", "%", "%", "%", "%",
				"%", position);
	}

	@Override
	public List<UserEntity> findCandidatesByPosition(String email, String firstName,
			String lastName, PositionEntity position) {
		log.info("Finding candidates of a given position by email or name");
		return userDAO.findCandidates(email, firstName, lastName, "%", "%",
				"%", "%", "%", position);
	}

	@Override
	public List<UserEntity> findCandidatesByPosition(String email, 
			String firstName, String lastName, String address, String city, 
			String country, String course, String school,
			PositionEntity position) {
		log.info("Finding candidates of a given position by several"
				+ " attributes");
		return userDAO.findCandidates(email, firstName, lastName, address, city, 
				country, course, school, position);
	}

	@Override
	public List<UserEntity> findCandidates(String email, String firstName,
			String lastName, String address, String city, String country,
			String course, String school) {
		log.info("Finding candidates by several attributes");
		return userDAO.findCandidates(email, firstName, lastName, 
				address, city, country, course, school, null);
	}

	@Override
	public List<UserEntity> findCandidatesByKeywordShort(String keyword) {
		log.info("Finding candidates by keyword");
		return userDAO.findUsersByRole(keyword, Constants.ROLE_CANDIDATE);
	}

	@Override
	public List<UserEntity> findCandidatesByKeyword(String keyword) {
		log.info("Finding candidates by keyword");
		return userDAO.findCandidatesByKeyword(keyword);
	}

	@Override
	public List<UserEntity> findRemovedEmails() {
		log.info("Finding all removed User emails");
		return userDAO.findRemovedEmails();
	}

	private String securePass(String pass) throws NoSuchAlgorithmException,
	UnsupportedEncodingException {

		String securedPassword = "";

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pass.getBytes());

			byte byteData[] = md.digest();
			byte[] data2 = Base64.encode(byteData);
			securedPassword = new String(data2);
			return securedPassword;
		} catch (NoSuchAlgorithmException e) {
			log.error("Error securing password");
		}

		return securedPassword;
	}

	private void isUserComplete(UserEntity u) {
		boolean hasError = false;

		if (u == null) hasError = true;
		else if (u.getEmail() == null) hasError = true;
		else if (u.getPassword() == null) hasError = true;
		else if (u.getFirstName() == null) hasError = true;
		else if (u.getLastName() == null) hasError = true;
		else if (u.getDefaultRole() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The user is missing data. "
					+ "Check the notnull attributes.");
	}

}