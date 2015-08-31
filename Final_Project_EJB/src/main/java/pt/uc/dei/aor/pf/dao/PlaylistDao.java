package pt.uc.dei.aor.pf.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.entities.Playlist;
import pt.uc.dei.aor.pf.entities.Utilizador;


@Stateless
public class PlaylistDao extends OLD_GenericDao<Playlist> {
	
	private static final Logger log = LoggerFactory.getLogger(PlaylistDao.class);

	public PlaylistDao() {
		super(Playlist.class);
	}

	@SuppressWarnings("unchecked")
	public List<Playlist> findAllByUser(Utilizador user) {
		Query q = em.createQuery("from Playlist p where p.dono.id = :userId");
		q.setParameter("userId", new Integer(user.getId()));
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Playlist> findAllByUserId(int userid) {
		Query q = em.createQuery("from Playlist p where p.dono.id = :userId");
		q.setParameter("userId",userid);
		return q.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Playlist> playlistsOrdNome(Utilizador user, String sentido) {
		Query q = null;
		if (sentido.equalsIgnoreCase("asc")) {
			log.info("Ordena lista por ordem ascendente de nome");
			q = em.createNamedQuery("PlaylistName.ASC");
		} else if (sentido.equalsIgnoreCase("desc")) {
			log.info("Ordena lista por ordem descendente de nome");
			q = em.createNamedQuery("PlaylistName.DESC");
		}
		q.setParameter("userId", new Integer(user.getId()));
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Playlist> playlistsOrdData(Utilizador user, String sentido) {
		Query q = null;
		if (sentido.equalsIgnoreCase("asc")) {
			log.info("Ordena lista por ordem ascendente de data");
			q = em.createNamedQuery("PlaylistData.ASC");
		} else if (sentido.equalsIgnoreCase("desc")) {
			q = em.createNamedQuery("PlaylistData.DESC");
			log.info("Ordena lista por ordem descendente de data");
		}
		q.setParameter("userId", new Integer(user.getId()));
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Playlist> playlistsOrdTamanho(Utilizador user, String sentido) {
		Query q = null;
		if (sentido.equalsIgnoreCase("asc")) {
			log.info("Ordena lista por ordem ascendente de tamanho");
			q = em.createNamedQuery("PlaylistTamanho.ASC");
		} else if (sentido.equalsIgnoreCase("desc")) {
			log.info("Ordena lista por ordem descendente de tamanho");
			q = em.createNamedQuery("PlaylistTamanho.DESC");
		}
		q.setParameter("userId", new Integer(user.getId()));
		return q.getResultList();
	}
	
	public boolean checkPlaylist(String nomePlaylist){
		Query q = em.createQuery("select p.nome from Playlist p where p.nome= :nomeParam");
		q.setParameter("nomeParam", nomePlaylist);
		try {
		@SuppressWarnings("unused")
		String nomeTemp = (String) q.getSingleResult();
		log.info("Existe playlist com o nome introduzido");
		return true;
		} catch (NoResultException e){
			log.error("Não existe playlist com o nome introduzido");
			return false;
		}
	}
	
	public Playlist playlistById(int playlistId){
		Playlist playlist;
		try{
		Query q = em.createQuery("select p from Playlist p where p.id = :id");
		q.setParameter("id", playlistId);
		playlist=(Playlist) q.getSingleResult();
		return playlist;
		}catch(NoResultException e){
			log.error("Não existe playlist com o nome introduzido");
			return null;
		}
	}
}
