package pt.uc.dei.aor.pf.beans;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.sun.syndication.io.impl.Base64;

import pt.uc.dei.aor.pf.dao.TestUserDao;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class TestUserBean implements TestUserInterface {

	@EJB
	private TestUserDao testUserDao;

	@Override
	public void save(UserEntity user){
		try {
			user.setPassword(securePass(user.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		this.testUserDao.save(user);
	}

	@Override
	public void update(UserEntity user){
		this.testUserDao.update(user);
	}

	@Override
	public void delete(UserEntity user) {
		this.testUserDao.update(user);
	}
	
	public String securePass(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String securedPassword = "";

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pass.getBytes());

			byte byteData[] = md.digest();
			byte[] data2 = Base64.encode(byteData);
			securedPassword = new String(data2);
			return securedPassword;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return securedPassword;
	}

	@Override
	public UserEntity findUserByEmail(String email) {
		return testUserDao.findUserByEmail(email);
	}
}
