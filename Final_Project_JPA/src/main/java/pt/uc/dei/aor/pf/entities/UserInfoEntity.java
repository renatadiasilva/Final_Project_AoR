package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Entity
@Table(name = "users_info")
public class UserInfoEntity implements Serializable {
	
	private static final long serialVersionUID = 5226638137102796213L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Past
	@Column(name = "birth_date")
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	@NotNull
	@Column(name = "address", nullable = false, length = 200)
	private String address;
	
	@NotNull
	@Column(name = "city", nullable = false, length = 40)
	private String city;
	
	@Column(name = "home_phone", length = 20)
	private String homePhone;
	
	@NotNull
	@Column(name = "mobile_phone", nullable = false, length = 20)
	private String mobilePhone;
	
	@NotNull
	@Column(name = "country", nullable = false, length = 40)
	private String country;
	
	@NotNull
	@Column(name = "course", nullable = false, length = 40)
	private String course;
	
	@NotNull
	@Column(name = "school", nullable = false, length = 100)
	private String school;
	
	@Column(name = "cv")
	private String cv;
	
	@Column(name = "linkedin")
	private String linkedin;
	
	@OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", unique = true, updatable = false)
	private UserEntity owner;
	
	public UserInfoEntity() {
	}

	public UserInfoEntity(Date birthday, String adress, String city,
			String telephone, String mobilePhone, String country, 
			String course, String school, String cv, UserEntity owner) {
		this.birthDate = birthday;
		this.address = adress;
		this.city = city;
		this.homePhone = telephone;
		this.mobilePhone = mobilePhone;
		this.country = country;
		this.course = course;
		this.school = school;
		this.cv = cv;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthday) {
		this.birthDate = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
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

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

}
