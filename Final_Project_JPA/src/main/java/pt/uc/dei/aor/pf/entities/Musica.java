package pt.uc.dei.aor.pf.entities;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotEmpty;



public class Musica implements Serializable {

	private static final long serialVersionUID = 9008393849875735770L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	@Column(name = "titulo", nullable = false, length = 40)
	private String titulo;

	@NotEmpty
	@Column(name = "artista", nullable = false, length = 40)
	private String artista;

	@NotEmpty
	@Column(name="album", nullable = false, length = 30)
	private String album;

	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "ano_lancamento")
	private Date anoLancamento;

	
	@Column(name="Path")
	private String filePath;

	@ManyToOne
	@XmlTransient
	private Utilizador dono;
	

	@ManyToMany(mappedBy = "musicas")
	@XmlTransient 
	private List<Playlist> playlists;
		
	@XmlTransient 
	public Utilizador getDono() {
		return dono;
	}
	@XmlTransient 
	public List<Playlist> getPlaylists() {
		return playlists;
	}


	public void setPlaylists(List<Playlist> playlist) {
		this.playlists = playlist;
	}


	public void setDono(Utilizador dono) {
		this.dono = dono;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Musica() {
		
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public Date getAnoLancamento() {
		return anoLancamento;
	}

	public void setAnoLancamento(Date anoLancamento) {
		this.anoLancamento = anoLancamento;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((album == null) ? 0 : album.hashCode());
		result = prime * result
				+ ((anoLancamento == null) ? 0 : anoLancamento.hashCode());
		result = prime * result + ((artista == null) ? 0 : artista.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Musica other = (Musica) obj;
		if (album == null) {
			if (other.album != null)
				return false;
		} else if (!album.equals(other.album))
			return false;
		if (anoLancamento == null) {
			if (other.anoLancamento != null)
				return false;
		} else if (!anoLancamento.equals(other.anoLancamento))
			return false;
		if (artista == null) {
			if (other.artista != null)
				return false;
		} else if (!artista.equals(other.artista))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}


}

