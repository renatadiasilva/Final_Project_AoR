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
	
	@Column(name="address")
	private String address;
	
	@Column(name="city")
	private String city;
	
	// Nullable
	@Column(name="phone", nullable=true)
	private int phone;
	
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
	
	// Link
	@Column(name="linkedin")
	private String linkedin;
	
	@OneToOne(mappedBy= "userInfo")
	private UserEntity owner;
	
	public UserInfoEntity() {
	}

	public UserInfoEntity(String adress, String city, int telephone,
			int mobilePhone, String country, String course, String school, String cv) {
		super();
		this.address = adress;
		this.city = city;
		this.phone = telephone;
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
		return address;
	}

	public void setAdress(String adress) {
		this.address = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getTelephone() {
		return phone;
	}

	public void setTelephone(int telephone) {
		this.phone = telephone;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
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
