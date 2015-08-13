package pt.uc.dei.aor.pf.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Stateless
public class MusicaDao extends GenericDao<Musica>{

	private static final Logger log = LoggerFactory.getLogger(MusicaDao.class);
	
	public MusicaDao() {
		super(Musica.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findlistaArtistas(){
		List<String> listaArtistas;
		listaArtistas = em.createQuery("SELECT DISTINCT d.artista FROM Musica d").getResultList();
		return listaArtistas;
	}


	@SuppressWarnings("unchecked")
	public List<Musica> findMusicaByArtista(String nome) {
		List<Musica> listaMusicas;
		Query q=em.createQuery("SELECT p FROM Musica p WHERE p.artista like :name");
		q.setParameter("name",nome);
		listaMusicas=q.getResultList();
		return listaMusicas;
	}

	@SuppressWarnings("unchecked")
	public List<String> findListaTitulos(){
		List<String> listaTitulos;
		listaTitulos = em.createQuery("SELECT DISTINCT d.titulo FROM Musica d").getResultList();
		return listaTitulos;
	}


	@SuppressWarnings("unchecked")
	public List<Musica> findMusicaByTitulo(String nome) {
		List<Musica> listaMusicas;
		Query q=em.createQuery("SELECT p FROM Musica p WHERE p.titulo like :name");
		q.setParameter("name",nome);
		listaMusicas=q.getResultList();
		return listaMusicas;
	}

	@SuppressWarnings("unchecked")
	public List<String> findListaTitulosArtistas(){
		List<String> listaTitulosArtistas;
		listaTitulosArtistas = em.createQuery("SELECT DISTINCT d.titulo, d.artista FROM Musica d").getResultList();
		return listaTitulosArtistas;
	}

	@SuppressWarnings("unchecked")
	public List<String> findListaTitulosArtista(String artista){
		List<String> listaTitulosArtista;
		Query q = em.createQuery("SELECT DISTINCT d.titulo FROM Musica d WHERE d.artista like :artista");
		q.setParameter("artista",artista);
		listaTitulosArtista=q.getResultList();
		return listaTitulosArtista;
	}
	
	public Musica findMusicaByTituloArtista(String artista, String titulo) {
		Musica musica;
		try{
		Query q=em.createQuery("SELECT p FROM Musica p WHERE p.titulo like :titulo AND p.artista like :artista");
		q.setParameter("titulo",titulo);
		q.setParameter("artista",artista);
		musica=(Musica) q.getSingleResult();
		log.info("Retorna a musica com titulo e artista seleccionado");
		}catch(NoResultException e){
			log.error("Não existe a musica com titulo e artista seleccionado");
			return null;
		}
		return musica;
	}
	
	@SuppressWarnings("unchecked")
	public List<Musica> findAllByUserId(int userId) {
		List<Musica> listaMusicas;
		int dono=userId;
		Query q=em.createQuery("SELECT p FROM Musica p inner join p.dono s where s.id = :dono");
		q.setParameter("dono",dono);
		listaMusicas=q.getResultList();
		return listaMusicas;
	}

	@SuppressWarnings("unchecked")
	public List<Musica> findAllByUser(Utilizador user) {
		List<Musica> listaMusicas;
		int dono=user.getId();
		Query q=em.createQuery("SELECT p FROM Musica p inner join p.dono s where s.id = :dono");
		q.setParameter("dono",dono);
		listaMusicas=q.getResultList();
		return listaMusicas;
	}
	
	public Musica findMusicaById(long id) {
		Musica musica;
		try{
		Query q=em.createQuery("SELECT p FROM Musica p WHERE p.id = :id");
		q.setParameter("id",id);
	
		musica=(Musica) q.getSingleResult();
		log.info("Retorna a musica por Id seleccionado");
		}catch(NoResultException e){
			log.error("Não existe a musica com id dado");
			return null;
		}
		return musica;
	}
}
