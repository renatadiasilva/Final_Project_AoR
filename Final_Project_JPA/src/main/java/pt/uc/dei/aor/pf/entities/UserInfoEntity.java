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

@Entity
@Table(name = "usersInfo")
public class UserInfoEntity implements Serializable {
	
	private static final long serialVersionUID = 5226638137102796213L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "birthday")
	@Temporal(TemporalType.DATE)
	private Date birthday;
	
	@Column(name = "address", length = 200)
	private String address;
	
	@Column(name = "city", length = 40)
	private String city;
	
//	check pattern
//  @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
//  	message="{invalid.phonenumber}")
	@Column(name = "home_phone", length = 20)
	private String homePhone;
	
//	check pattern
//	@Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
//  	message="{invalid.mobilephone}")
	@Column(name = "mobile_phone", length = 20)
	private String mobilePhone;
	
	@Column(name = "country", length = 40)
	private String country;
	
	@Column(name = "course", length = 40)
	private String course;
	
	@Column(name = "school", length = 40)
	private String school;
	
	// Link
	@Column(name = "cv")
	private String cv;
	
	// Link (validação!!)
	@Column(name = "linkedin")
	private String linkedin;
	
	@OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", unique = true, updatable = false)
	private UserEntity owner;
	
//	@OneToOne(mappedBy="userInfo")
//	private UserEntity owner;
	
	public UserInfoEntity() {
	}

	public UserInfoEntity(Date birthday, String adress, String city, String telephone,
			String mobilePhone, String country, String course, String school, String cv, UserEntity owner) {
		super();
		this.birthday = birthday;
		this.address = adress;
		this.city = city;
		this.homePhone = telephone;
		this.mobilePhone = mobilePhone;
		this.country = country;
		this.course = course;
		this.school = school;
		this.cv=cv;
		this.owner=owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
