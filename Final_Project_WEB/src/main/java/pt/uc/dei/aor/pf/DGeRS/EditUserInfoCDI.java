package pt.uc.dei.aor.pf.DGeRS;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

	}
	
	public void init() {
		if(this.userSessionManagement.getCurrentUser().getFirstName()!=null)this.firstName=this.userSessionManagement.getCurrentUser().getFirstName();
		if(this.userSessionManagement.getCurrentUser().getLastName()!=null)this.lastName=this.userSessionManagement.getCurrentUser().getLastName();

		if(this.userSessionManagement.getCurrentUser().getUserInfo()!=null){
			this.address=this.userSessionManagement.getCurrentUser().getUserInfo().getAddress();
			this.birthday=this.userSessionManagement.getCurrentUser().getUserInfo().getBirthday();
			this.city=this.userSessionManagement.getCurrentUser().getUserInfo().getCity();
			this.homePhone=this.userSessionManagement.getCurrentUser().getUserInfo().getHomePhone();
			this.mobilePhone=this.userSessionManagement.getCurrentUser().getUserInfo().getMobilePhone();
			this.country=this.userSessionManagement.getCurrentUser().getUserInfo().getCountry();
			this.course=this.userSessionManagement.getCurrentUser().getUserInfo().getCourse();
			this.school=this.userSessionManagement.getCurrentUser().getUserInfo().getSchool();
			this.linkedin=this.userSessionManagement.getCurrentUser().getUserInfo().getLinkedin();
		}
		
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getAddress()!=null)this.address=this.userSessionManagement.getCurrentUser().getUserInfo().getAddress();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getBirthday()!=null)this.birthday=this.userSessionManagement.getCurrentUser().getUserInfo().getBirthday();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getCity()!=null)this.city=this.userSessionManagement.getCurrentUser().getUserInfo().getCity();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getHomePhone()!=null)this.homePhone=this.userSessionManagement.getCurrentUser().getUserInfo().getHomePhone();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getMobilePhone()!=null)this.mobilePhone=this.userSessionManagement.getCurrentUser().getUserInfo().getMobilePhone();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getCountry()!=null)this.country=this.userSessionManagement.getCurrentUser().getUserInfo().getCountry();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getCourse()!=null)this.course=this.userSessionManagement.getCurrentUser().getUserInfo().getCourse();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getSchool()!=null)this.school=this.userSessionManagement.getCurrentUser().getUserInfo().getSchool();
//		if(this.userSessionManagement.getCurrentUser().getUserInfo().getLinkedin()!=null)this.linkedin=this.userSessionManagement.getCurrentUser().getUserInfo().getLinkedin();
	}
	
	public void update(){
		this.userSessionManagement.updateUserInfo(firstName, lastName, address, city, homePhone, mobilePhone, country, course, school, linkedin);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Dados Actualizados."));
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