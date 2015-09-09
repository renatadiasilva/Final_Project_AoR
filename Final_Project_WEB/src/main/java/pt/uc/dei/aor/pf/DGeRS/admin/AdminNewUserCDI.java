package pt.uc.dei.aor.pf.DGeRS.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;

import pt.uc.dei.aor.pf.DGeRS.session.UserSessionManagement;


@Named
@RequestScoped
public class AdminNewUserCDI {

	@Inject
	UserSessionManagement userSessionManagement;

	// UserEntity
	private String email, password, firstName, lastName;

	// UserInfoEntity
	private String address, city, homePhone, mobilePhone, country, course, school, linkedin;

	private Date birthday;

	private boolean admin, manager, interviewer, candidate;

	public AdminNewUserCDI() {
	}

	public void newUser(){
		System.out.println(email+" "+password+" "+firstName+" "+lastName+" "+birthday+" "+address+" "+city+" "+homePhone+" "+mobilePhone+" "+country+" "+course+" "+school+" "+linkedin+" "+true+" "+admin+" "+manager+" "+interviewer);
		this.userSessionManagement.newUser(email, password, firstName, lastName, birthday, address, city, homePhone, mobilePhone, country, course, school, linkedin, true, admin, manager, interviewer);
	}

	public void newUserNC(){
		System.out.println(email+" "+password+" "+firstName+" "+lastName+" "+admin+" "+manager+" "+interviewer);
		this.userSessionManagement.newUserNC(email, password, firstName, lastName, admin, manager, interviewer);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getPassword() {
		this.password=RandomStringUtils.randomAlphanumeric(8);
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
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

	public void setAddress(String adress) {
		this.address = adress.trim();
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

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public boolean isInterviewer() {
		return interviewer;
	}

	public void setInterviewer(boolean interviewer) {
		this.interviewer = interviewer;
	}

	public boolean isCandidate() {
		return candidate;
	}

	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}

}
