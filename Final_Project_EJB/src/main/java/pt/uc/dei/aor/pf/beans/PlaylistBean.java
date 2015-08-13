package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.PlaylistDao;
import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Playlist;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Stateless
public class PlaylistBean implements PlaylistInterface {

	private static final Logger log = LoggerFactory
			.getLogger(PlaylistBean.class);

	@EJB
	PlaylistDao playlistDao;

	@Override
	public void save(Playlist playlist) {
		log.info("A playlist não existe e vai ser acrescentada");
		System.out.println("A playlist não existe e vai ser acrescentada");
		playlist.setData_criacao(new Date());
		playlistDao.save(playlist);

	}

	@Override
	public void update(Playlist playlist) {
		// fazer verificacoes de negocio
		if (playlist.getNome() != null) {
			log.info("A playlist seleccionada vai ser actualizada");
			playlist.setTamanho(playlist.getMusicas().size());
			playlistDao.update(playlist);
		}

	}

	@Override
	public void delete(Playlist playlist) {
		log.info("A playlist seleccionada existe na base de dados e vai ser eliminada");
		playlistDao.delete(playlist.getId(), Playlist.class);

	}

	@Override
	public List<Playlist> allPlaylists(Utilizador user) {
		log.info("Procura todas as Playlists de um utilizador");
		return playlistDao.findAllByUser(user);
	}

	@Override
	public List<Playlist> allPlaylistsById(int iduser) {
		log.info("Procura todas as Playlists de um utilizador");
		return playlistDao.findAllByUserId(iduser);
	}
	@Override
	public List<Playlist> playlistOrdenadoNome(Utilizador user, String ordem) {
		log.info("Procura todas as Playlists ordenadas por nome");
		return playlistDao.playlistsOrdNome(user, ordem);
	}

	@Override
	public Playlist playlistById(int id) {
		log.info("Procura as Playlist por id");
		return playlistDao.playlistById(id);
	}

	
	@Override
	public List<Playlist> playlistOrdenadoData(Utilizador user, String ordem) {
		log.info("Procura todas as Playlists ordenadas por data");
		return playlistDao.playlistsOrdData(user, ordem);
	}

	@Override
	public List<Playlist> playlistOrdenadoTamanho(Utilizador user, String ordem) {
		log.info("Procura todas as Playlists ordenadas por tamanho");
		return playlistDao.playlistsOrdTamanho(user, ordem);
	}

	@Override
	public boolean adicionaMusica(Musica musica, Playlist playListNome) {
		boolean existe = false;
		for (Musica m : playListNome.getMusicas()) {
			if (m.equals(musica)) {
				log.info("A musica existe na Playlist");
				existe = true;
				break;
			}
		}
		if (!existe) {
			playListNome.addMusica(musica);
			playListNome.setTamanho(playListNome.getMusicas().size());
			playlistDao.update(playListNome);
			log.info("A musica não existe na Playlist e é adicionada");
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void removeMusica(Playlist playlist, Musica musicaRemover) {
		playlist.getMusicas().remove(musicaRemover);
		playlist.setTamanho(playlist.getMusicas().size());
		playlistDao.update(playlist);
		log.info("A música escolhida é removida da Playlist");
	}

	@Override
	public List<Playlist> findAll() {
		log.info("Procura todas as Playlists da aplicação");
		return playlistDao.findAll();
	}
}
