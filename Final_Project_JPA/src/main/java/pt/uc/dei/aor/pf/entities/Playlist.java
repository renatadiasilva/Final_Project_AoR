package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "playlists")
@NamedQueries({
@NamedQuery(name="PlaylistName.ASC", query="select p from Playlist p where p.dono.id = :userId order by p.nome asc"),
@NamedQuery(name="PlaylistName.DESC", query="select p from Playlist p where p.dono.id = :userId order by p.nome desc"),
@NamedQuery(name="PlaylistData.ASC", query="select p from Playlist p where p.dono.id = :userId order by p.data_criacao asc"),
@NamedQuery(name="PlaylistData.DESC", query="select p from Playlist p where p.dono.id = :userId order by p.data_criacao desc"),
@NamedQuery(name="PlaylistTamanho.ASC", query="select p from Playlist p where p.dono.id = :userId order by p.tamanho asc"),
@NamedQuery(name="PlaylistTamanho.DESC", query="select p from Playlist p where p.dono.id = :userId order by p.tamanho desc"),
}
)
@XmlRootElement(name="playlist")
public class Playlist implements Serializable {

	private static final long serialVersionUID = 7363136456493451140L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "playlist_id")
	private Integer id;

	@Column(name = "nome", nullable = false, length = 20)
	private String nome;

	@Column(name = "data_criacao")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date data_criacao;

	@Column(name = "tamanho")
	private int tamanho;
	
	@XmlTransient 
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Musica> musicas;
	
	@XmlTransient 
	@ManyToOne
	@JoinColumn(name="id_dono")
	private Utilizador dono;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData_criacao() {
		return data_criacao;
	}

	public void setData_criacao(Date data_criacao) {
		this.data_criacao = data_criacao;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	@XmlTransient 
	public List<Musica> getMusicas() {
		return musicas;
	}

	public void setMusicas(List<Musica> musica) {
		this.musicas = musica;
	}
	@XmlTransient 
	public Utilizador getDono() {
		return dono;
	}

	public void setDono(Utilizador dono) {
		System.out.println(dono.getNome());
		this.dono = dono;
	}
	public void addMusica(Musica musica){
		if(musicas==null){
			musicas = new ArrayList<Musica>();
		} else {
			musicas.add(musica);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Playlist other = (Playlist) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
