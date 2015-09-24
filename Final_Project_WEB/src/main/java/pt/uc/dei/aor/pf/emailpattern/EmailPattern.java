package pt.uc.dei.aor.pf.emailpattern;

public class EmailPattern {
	
	public static boolean checkEmailPatter(String email) {
		return email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+"
				+ "(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
	}
}
