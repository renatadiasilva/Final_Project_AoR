package pt.uc.dei.aor.pf.DGeRS;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.DGeRS.session.UserSessionManagement;

@Named
@RequestScoped
public class EditUserInfoCDI {
	
	@Inject
	UserSessionManagement userSessionManagement;
	
	// UserEntity
	private String firstName, lastName;
	
	// UserInfoEntity
	private String address, city, homePhone, mobilePhone, country, course, school, linkedin;

	private Date birthday;
	
	public EditUserInfoCDI() {
//		this.firstName=this.userSessionManagement.getCurrentUser().getFirstName();
//		this.lastName=this.userSessionManagement.getCurrentUser().getLastName();
//		
//		this.address=this.userSessionManagement.getCurrentUser().getUserInfo().getAddress();
//		this.city=this.userSessionManagement.getCurrentUser().getUserInfo().getCity();
//		this.homePhone=this.userSessionManagement.getCurrentUser().getUserInfo().getHomePhone();
//		this.mobilePhone=this.userSessionManagement.getCurrentUser().getUserInfo().getMobilePhone();
//		this.country=this.userSessionManagement.getCurrentUser().getUserInfo().getCountry();
//		this.course=this.userSessionManagement.getCurrentUser().getUserInfo().getCourse();
//		this.school=this.userSessionManagement.getCurrentUser().getUserInfo().getSchool();
//		this.linkedin=this.userSessionManagement.getCurrentUser().getUserInfo().getLinkedin();
	}
	
	public void update(){
		this.userSessionManagement.updateUserInfo(address, city, homePhone, mobilePhone, country, course, school, linkedin);
		this.userSessionManagement.updateUser(firstName, lastName);
	}

	public String getFirstName() {
		if(this.firstName==null) return this.userSessionManagement.getCurrentUser().getFirstName();
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
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
