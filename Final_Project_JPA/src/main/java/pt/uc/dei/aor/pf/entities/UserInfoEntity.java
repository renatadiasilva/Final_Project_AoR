package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="UserInfoEntity")
public class UserInfoEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5226638137102796213L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="adress")
	private String adress;
	
	@Column(name="city")
	private String city;
	
	// Nullable
	@Column(name="telephone", nullable=true)
	private int telephone;
	
	@Column(name="mobilePhone")
	private int mobilePhone;
	
	@Column(name="country")
	private String country;
	
	@Column(name="course")
	private String course;
	
	@Column(name="school")
	private String school;
	
	// Link
	@Column(name="cv")
	private String cv;
	
	// Na SubmissionEntity:
//	private String motivationLetter;
//	private List<String> source;

	@OneToOne(mappedBy= "userInfo")
	private UserEntity owner;
	
	public UserInfoEntity() {
	}

	public UserInfoEntity(String adress, String city, int telephone,
			int mobilePhone, String country, String course, String school, String cv) {
		super();
		this.adress = adress;
		this.city = city;
		this.telephone = telephone;
		this.mobilePhone = mobilePhone;
		this.country = country;
		this.course = course;
		this.school = school;
		this.cv=cv;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getTelephone() {
		return telephone;
	}

	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}

	public int getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(int mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

}
