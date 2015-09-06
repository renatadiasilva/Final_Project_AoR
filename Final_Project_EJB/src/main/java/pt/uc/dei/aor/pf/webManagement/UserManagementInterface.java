package pt.uc.dei.aor.pf.webManagement;

import javax.ejb.Local;

@Local
public interface UserManagementInterface {
	
	public abstract void init();
	
	public abstract String login(String email, String password);
	
	public abstract void logout();
	
	public abstract void defaultRole(String role);
	
	public abstract boolean checkDefault(String role);
	
	public abstract void newUser(String email, String password, String firstName, String lastName, String adress, String city,
			String homePhone, String mobilePhone, String country, String course, String school, String linkedin);

	public abstract boolean isAdmin();
	
	public abstract boolean isManager();

	public abstract boolean isInterviewer();

	public abstract boolean isCandidate();

	public String getUserFullName();

	public boolean isLogged();

	public String getDefaultRole();
	
}
