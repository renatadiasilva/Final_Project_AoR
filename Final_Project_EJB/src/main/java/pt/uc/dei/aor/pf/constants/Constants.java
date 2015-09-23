package pt.uc.dei.aor.pf.constants;

import javax.ejb.Stateless;

@Stateless
public class Constants {

	// SuperAdmin (the first admin to be created) email
	public static final String SUPER_ADMIN = "admin@mail.com";	

	// String used when removing data of users
	public static final String REMOVED_DATA = "DADOS APAGADOS";	

	// Strings related to UserEntity
	public static final String ROLE_ADMIN       = "ADMIN";
	public static final String ROLE_MANAGER     = "MANAGER";
	public static final String ROLE_INTERVIEWER = "INTERVIEWER";
	public static final String ROLE_CANDIDATE   = "CANDIDATE";

	// Strings related to PositionEntity
	public static final String LOCATION_LISBOA  = "LISBOA";
	public static final String LOCATION_PORTO   = "PORTO";
	public static final String LOCATION_COIMBRA = "COIMBRA";

	public static final String STATUS_OPEN      = "OPEN";
	public static final String STATUS_CLOSED    = "CLOSED";
	public static final String STATUS_ONHOLD    = "ON HOLD";

	public static final String TECH_SSPA        = "SSPA";
	public static final String TECH_DOTNET      = ".NET DEVELOPMENT";
	public static final String TECH_JAVA        = "JAVA DEVELOPMENT";
	public static final String TECH_SAFETY      = "SAFETY CRITICAL";
	public static final String TECH_MANAGEMENT  = "PROJECT MANAGEMENT";
	public static final String TECH_INTEGRATION = "INTEGRATION";

	public static final String SOCIAL_CRITICAL  = "CRITICAL SOFTWARE WEBSITE";
	public static final String SOCIAL_LINKEDIN  = "LINKEDIN";
	public static final String SOCIAL_GLASSDOOR = "GLASSDOOR";
	public static final String SOCIAL_FACEBOOK  = "FACEBOOK";

	// Strings related to SubmissionEntity
	public static final String STATUS_SUBMITED = "SUBMITED";
	public static final String STATUS_ACCEPTED = "ACCEPTED TO INTERVIEW";
	public static final String STATUS_PROPOSAL = "PRESENTED PROPOSAL";
	public static final String STATUS_NEGOTIATION =
			"OFFER PROCESS (NEGOTIATION)";
	public static final String STATUS_REJECTED = "REJECTED";
	public static final String STATUS_HIRED = "HIRED";

	// outros??
	public static final String SOURCE_EXPRESSO   = "EXPRESSO";
	public static final String SOURCE_LINKEDIN   = "LINKEDIN";
	public static final String SOURCE_FACEBOOK   = "FACEBOOK";
	public static final String SOURCE_NETEMPREGO = "NET.EMPREGO";

	// Strings related to StyleEntity
	public static final String DEFAULT_COMPANY_NAME    = "ITJobs";
	public static final String DEFAULT_LOGO            = "logo_49px.png";
	public static final String DEFAULT_FOOTER_MESSAGE  = "ITJobs -"
			+ " Projecto Final | Programação Avançada em JAVA |"
			+ " Duarte Gonçalves | Renata Silva";
	public static final String DEFAULT_PRIMARY_COLOR   = "#3f51b5";
	public static final String DEFAULT_SECONDARY_COLOR = "#ff4081";

	// Strings related to Reports

	public static final String PERIOD_DAILY   = "DAILY";
	public static final String PERIOD_MONTHLY = "MONTHLY";
	public static final String PERIOD_YEARLY  = "YEARLY";

	public static final char DAILY   = 'd';
	public static final char MONTHLY = 'm';
	public static final char YEARLY  = 'y';

	public static final String PERIOD_DHEADER = "Dia";
	public static final String PERIOD_MHEADER = "Mês (completo)";
	public static final String PERIOD_YHEADER = "Ano (completo)";

	// limit searching days by period
	public static final long LIMITDAY   = 100;
	public static final long LIMITMONTH = 1000;
	
	// Miliseconds per day (to convert on days)
	public static final long MSPERDAY = 60 * 60 * 24 * 1000;

	// native queries
	public static final String QUERY_SPONT = "SPONTANEOUS";
	public static final String QUERY_REJEC = "REJECTED";
	public static final String QUERY_PROPO = "PROPOSAL";
	public static final String QUERY_HIRED = "HIRED";
	

	// No occurencies
	public static final String REPORT_NO_HIRED = "Sem contratações";
	
	// Queries from user emails
	public static final String SERVLET_SUBJECT="subject";
	public static final String SERVLET_AUTH_CANDIDATE="authCand";
	public static final String SERVLET_EMAIL="email";
}
