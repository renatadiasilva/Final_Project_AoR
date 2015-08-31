package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.entities.Utilizador;



@Stateless
public class Old_UserDao extends OLD_GenericDao<Utilizador> {

	private static final Logger log = LoggerFactory.getLogger(Old_UserDao.class);

	public Old_UserDao() {
		super(Utilizador.class);
	}

	public Utilizador login(Utilizador u) {
		// recebe user temp e faz query para tentar encontra-lo
		Utilizador user = null;
		try {
			Query q = em
//					.createQuery("select u from Utilizador u where u.mail=:mailparam and u.password =:passparam");
					.createQuery("select u from Utilizador u where u.mail=:mailparam");
			q.setParameter("mailparam", u.getMail());
//			q.setParameter("passparam", u.getPassword());
			user = (Utilizador) q.getSingleResult();
			log.info("O utilizador existe");
		} catch (NoResultException e) {
			log.error("O utilizador não existe");
			return null;
		}
		return user;
	}

	public boolean checkEmail(String email) {

		Query q = em
				.createQuery("select u.mail from Utilizador u where u.mail= :mailParam");
		q.setParameter("mailParam", email);
		try {
			@SuppressWarnings("unused")
			String emailTemp = (String) q.getSingleResult();
			log.error("O mail existe");
			return true;
		} catch (NoResultException e) {
			log.info("O mail não existe");
			return false;
		}

	}
	
	public Utilizador findUserByEmail(String email) {

		Query q = em
				.createQuery("select u from Utilizador u where u.mail= :mailParam");
		q.setParameter("mailParam", email);
		try {
			Utilizador utilizador = (Utilizador) q.getSingleResult();
			log.error("O mail existe");
			return utilizador;
		} catch (NoResultException e) {
			log.info("O mail não existe");
			return null;
		}

	}

}
