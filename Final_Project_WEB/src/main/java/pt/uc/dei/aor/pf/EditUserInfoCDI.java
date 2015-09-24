package pt.uc.dei.aor.pf;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

@Named
@RequestScoped
public class EditUserInfoCDI {
	
	@Inject
	UserSessionManagement userSessionManagement;
	
	// UserEntity
	private String firstName, lastName;
	
	// UserInfoEntity
	private String address, city, homePhone, mobilePhone, 
    	country, course, school, linkedin;

	private Date birthday;
	
	public EditUserInfoCDI() {

	}
	
	public void updateInfo() {
		UserEntity currentUserClone =
				this.userSessionManagement.getCurrentUserClone();
		if (currentUserClone.getFirstName() != null)
			this.firstName = currentUserClone.getFirstName();
		if (currentUserClone.getLastName() != null) {
			this.lastName = currentUserClone.getLastName();
		}

		if (currentUserClone.getUserInfo() != null) {
			UserInfoEntity currentInfo = currentUserClone.getUserInfo();
			this.address = currentInfo.getAddress();
			this.birthday = currentInfo.getBirthDate();
			this.city = currentInfo.getCity();
			this.homePhone = currentInfo.getHomePhone();
			this.mobilePhone = currentInfo.getMobilePhone();
			this.country = currentInfo.getCountry();
			this.course = currentInfo.getCourse();
			this.school = currentInfo.getSchool();
			this.linkedin = currentInfo.getLinkedin();
		}
	}
	
	public void update() {
		this.userSessionManagement.updateUserData(firstName, lastName, 
				birthday, address, city, homePhone, mobilePhone, country,
				course, school, linkedin);
		updateInfo();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.trim();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city.trim();
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone.trim();
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone.trim();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country.trim();
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course.trim();
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school.trim();
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin.trim();
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getMinDate() {
		Calendar minDate=new GregorianCalendar(1900,0,1);
		return minDate.getTime();
	}
	
	public Date getMaxDate() {
		return new Date();
	}
	
}