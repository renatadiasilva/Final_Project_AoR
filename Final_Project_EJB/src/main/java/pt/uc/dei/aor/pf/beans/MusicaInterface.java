package pt.uc.dei.aor.pf.beans;


import java.util.List;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Local
public interface MusicaInterface{
	
	public abstract void save(Musica musica);
	public abstract void update(Musica musica);
	public abstract void delete(Musica musica);
	public abstract void deleteMusicById(int id);
	public abstract Musica findMusicaById(int musicaID);
	public abstract List<Musica> findAll();
	public abstract List<String> findListaArtistas();
	public abstract List<Musica> findMusicaByArtista(String nome);
	public abstract List<String> findListaTitulos();
	public abstract List<Musica> findMusicaByTitulo(String nome);
	public abstract List<Musica> findAllByUser(Utilizador user);
	public abstract List<Musica> findAllByUserId(int userId);
	public abstract List<String> findListaTitulosPorArtista(String artistaSeleccionado);
	public abstract Musica findMusicaByTituloArtista(String artista, String titulo);
	
}