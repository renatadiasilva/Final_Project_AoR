package pt.uc.dei.aor.pf.beans;
import com.sun.syndication.io.impl.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import pt.uc.dei.aor.pf.dao.MusicaDao;
import pt.uc.dei.aor.pf.dao.Old_UserDao;
import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Stateless
public class OLD_UserBean implements UserInterface {
	
	private static final Logger log = LoggerFactory.getLogger(OLD_UserBean.class);
	
	@EJB
	Old_UserDao userDao;
	
	@EJB
	MusicaDao musicaDao;

	@Override
	public void update(Utilizador user) {
		userDao.update(user);
	}

	@Override
	public void updatePassword(Utilizador user) {
		try {
			log.info("A password introduzida foi " + user.getPassword());
			log.info("A password foi encriptada para: "
					+ encriptarPass(user.getPassword()));
			user.setPassword(encriptarPass(user.getPassword()));
			userDao.update(user);
			log.info("A password é actualizada");
		} catch (NoSuchAlgorithmException e) {
			log.error("Erro na encriptacao da password");
		} catch (UnsupportedEncodingException e) {
			log.error("Erro na encriptacao da password");
		}

	}

	@Override
	public void delete(Utilizador user) {

		// definir dono das musicas como servidor
		List<Musica> lista = musicaDao.findAllByUser(user);
		log.info("Vai alterar todas as musicas introduzidas pelo utilizador no campo dono ");
		for (Musica m : lista) {
			log.info("A música " + m.getTitulo()
					+ "vai ficar com o dono a null");
			m.setDono(null);
			musicaDao.update(m);
		}
		userDao.delete(user.getId(), Utilizador.class);
		log.info("Apaga o utilizador");
	}

	@Override
	public void deleteUserById(int userId) {

		// definir dono das musicas como servidor
		Utilizador utilizador = userDao.find(userId);
		List<Musica> lista = musicaDao.findAllByUser(utilizador);
		log.info("Vai alterar todas as musicas introduzidas pelo utilizador no campo dono ");
		for (Musica m : lista) {
			log.info("A música " + m.getTitulo()
					+ "vai ficar com o dono a null");
			m.setDono(null);
			musicaDao.update(m);
		}
		userDao.delete(userId, Utilizador.class);
		log.info("Apaga o utilizador");
	}

	@Override
	public Utilizador login(Utilizador user) throws NoResultException {
		Utilizador activo = null;
		try {
			log.info("A password introduzida foi " + user.getPassword());
			log.info("A password foi encriptada para: "
					+ encriptarPass(user.getPassword()));
			user.setPassword(encriptarPass(user.getPassword()));
			activo = userDao.login(user);
		
			UsersCount.increment(activo);

			
		} catch (NoSuchAlgorithmException e) {
			log.error("Erro na encriptacao da password");
		} catch (UnsupportedEncodingException e) {
			log.error("Erro na encriptacao da password");
		}
		return activo;
	}

	public void logout(Utilizador user){
		UsersCount.numLogout(user);
	}
	
	public String encriptarPass(String pass) throws NoSuchAlgorithmException,
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
			e.printStackTrace();
		}

		return securedPassword;
	}

	@Override
	public boolean save(Utilizador user) {
		try {
			String pass = encriptarPass(user.getPassword());
			user.setPassword(pass);
			if (!userDao.checkEmail(user.getMail())) {
				log.info("Verifica se o email introduzido não existe");
				userDao.save(user);
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("Erro na encriptacao da password");
		} catch (UnsupportedEncodingException e) {
			log.error("Erro na encriptacao da password");
		}
		return false;
	}

	@Override
	public List<Utilizador> findAll() {
		return userDao.findAll();
	}
	
	@Override
	public List<Utilizador> findAllWS() {
		List<Utilizador> novos= new ArrayList<Utilizador>();
		List<Utilizador> list=userDao.findAll();
		for (Utilizador u:list){
			Utilizador user2=new Utilizador();
			user2.setId(u.getId());
			user2.setNome(u.getNome());
			user2.setMail(u.getMail());
			user2.setPassword("");	
			novos.add(user2);
		}
		return novos;
		
	}
	@Override
	public Utilizador find(int id) {
		return userDao.find(id);
	}
	@Override
	public Utilizador findSimpleUser(int id) {
		Utilizador user=userDao.find(id);
		Utilizador user2=new Utilizador();
		user2.setId(user.getId());
		user2.setNome(user.getNome());
		user2.setMail(user.getMail());
		user2.setPassword("");
		return user2;
	}
	@Override
	public Utilizador findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}
}
