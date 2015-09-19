package pt.uc.dei.aor.pf.webManagement;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface UserManagementInterface {

	public abstract void login(String email, String password);

	public abstract void logout();

	public abstract void defaultRole(String role);

	public abstract boolean checkDefault(String role);

	public abstract boolean changePassword(String password, String newPassword);

	public abstract boolean newUser(String email, String password, String firstName,
			String lastName, Date birthday, String address, String city,
			String homePhone, String mobilePhone, String country,
			String course, String school, String linkedin,
			boolean createdByAdmin, boolean admin, boolean manager,
			boolean interviewer);

	public abstract boolean newUserNC(String email, String password, String firstName,
			String lastName, boolean admin, boolean manager, boolean interviewer);

	public abstract void updateUserInfo(String firstName, String lastName, String address,
			String city, String homePhone, String mobilePhone, String country,
			String course, String school, String linkedin);

	public abstract boolean isTemporaryPassword();
	
	
	
	// Set Get
	public abstract boolean isAdmin();

	public abstract void setAdmin(boolean admin);

	public abstract boolean isManager();

	public abstract void setManager(boolean manager);

	public abstract boolean isInterviewer();

	public abstract void setInterviewer(boolean interviewer);

	public abstract boolean isCandidate();

	public abstract void setCandidate(boolean candidate);

	public abstract String getUserEmail();

	public abstract String getUserDefaultRole();
	
	public abstract boolean isUserLogged();

	public abstract String getUserFullName();

	public abstract boolean recoverPassword(String email, String temporaryPassword);

	public abstract List<String> getStyle();

	public abstract boolean checkAuthentication(String email);
	
}
