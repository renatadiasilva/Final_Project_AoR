package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.MusicaDao;
import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Stateless
public class MusicaBean implements MusicaInterface{
	
	private static final Logger log = LoggerFactory.getLogger(MusicaBean.class);
	
	@EJB
	private MusicaDao musicaDao;

	@Override
	public void save(Musica musica){
		musicaDao.save(musica);
	}

	@Override
	public void update(Musica musica){
		isMusicaComplete(musica);
		log.info("Os dados da música estão completos e vão ser actualizados");
		musicaDao.update(musica);
	}

	@Override
	public void delete(Musica musica) {
		musica.setDono(null);
		log.info("Colocou p dono da música a nulo e vai eliminar a música dos registos do utilizador");
		musicaDao.update(musica);
	}
	@Override
	public void deleteMusicById(int id) {
		Musica musica=musicaDao.findMusicaById(id);
		musica.setDono(null);
		log.info("Colocou p dono da música a nulo e vai eliminar a música dos registos do utilizador");
		musicaDao.update(musica);
	}
	@Override
	public Musica findMusicaById(int musicaID) {
		// Teste
		System.out.println("Music title: "+musicaDao.findMusicaById(musicaID).getTitulo());
		return musicaDao.findMusicaById(musicaID);
	}


	@Override
	public List<Musica> findAll() {
		return musicaDao.findAll();
	}
	@Override
	public List<Musica> findAllByUserId(int userId){
		return musicaDao.findAllByUserId(userId) ;
	}
	@Override
	public List<Musica> findAllByUser(Utilizador user) {
		return musicaDao.findAllByUser(user);
	}



	private void isMusicaComplete(Musica musica){
		boolean hasError = false;

		if(musica.getTitulo() == null){
			hasError = true;
			log.error("Se o titulo está a nulo dá erro!");
		}

		if (musica.getTitulo() == null || "".equals(musica.getTitulo().trim())){
			hasError = true;
			log.error("Se o titulo está a nulo dá erro!");
		}

		if (hasError){
			throw new IllegalArgumentException("Verificar os elementos introduzidos.");
		}
	}

	//procurar todas as musicas por artista
	public List<Musica> findMusicaByArtista(String nome){
		log.info("Procura as músicas por nome de utilizador");
		return musicaDao.findMusicaByArtista(nome);
	}
	//procurar a lista de todos os artistas
	public List<String> findListaArtistas(){
		log.info("Procura a lista com os nomes de todos os artistas ");
		return musicaDao.findlistaArtistas();

	}

	//procurar todas as musicas por titulo
	public List<Musica> findMusicaByTitulo(String nome){
		log.info("Procura a lista de musicas com o titulo igual ao introduzido");
		return musicaDao.findMusicaByTitulo(nome);
	}
	//procurar a lista de todos os titulos
	public List<String> findListaTitulos(){
		log.info("Procura a lista com os nomes de todos os titulos");
		return musicaDao.findListaTitulos();

	}

	//procurar a lista de todos os titulos Por Artista
		public List<String> findListaTitulosPorArtista(String artistaSeleccionado){
			log.info("Procura a lista com os nomes de todos os titulos por determinado artista");
			return musicaDao.findListaTitulosArtista(artistaSeleccionado);

		}
	
	
	//procurar todas as musicas por titulo&&artista
	public Musica findMusicaByTituloArtista(String artista,String titulo){
		log.info("Procura a musica com o nomes e o titulo introduzido");
		return musicaDao.findMusicaByTituloArtista(artista,titulo);
	}

}
