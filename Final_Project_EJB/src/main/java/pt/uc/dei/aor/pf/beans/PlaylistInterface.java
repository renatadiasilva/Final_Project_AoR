package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Playlist;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Local
public interface PlaylistInterface {
	
	public abstract void save(Playlist playlist);
	public abstract void update(Playlist playlist);
	public abstract void delete(Playlist playlist);
	public abstract List<Playlist> allPlaylists(Utilizador user);
	public abstract List<Playlist> allPlaylistsById(int iduser);
	public abstract List<Playlist> playlistOrdenadoNome(Utilizador user, String ordem);
	public abstract List<Playlist> playlistOrdenadoData(Utilizador user, String ordem);
	public abstract List<Playlist> playlistOrdenadoTamanho(Utilizador user, String ordem);
	public abstract boolean adicionaMusica(Musica musica,Playlist playListNome);
	public abstract void removeMusica(Playlist playlist, Musica musicaRemover);
	public abstract List<Playlist> findAll();
	public abstract Playlist playlistById(int id);
}
