package pt.uc.dei.aor.pf.constants;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

@Stateless
public class Constants {

	// SuperAdmin (the first admin to be created) email
	public static final String SUPER_ADMIN = "admin@mail.com";	

	// String used when removing data of users
	public static final String REMOVED_DATA = "Dados Apagados";	

	// Strings related to UserEntity
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_MANAGER = "MANAGER";
	public static final String ROLE_INTERVIEWER = "INTERVIEWER";
	public static final String ROLE_CANDIDATE = "CANDIDATE";

	// Strings related to PositionEntity
	public static final String LOCATION_LISBOA = "Lisboa";
	public static final String LOCATION_PORTO = "Porto";
	public static final String LOCATION_COIMBRA = "Coimbra";

	public static final String STATUS_OPEN = "Aberta";
	public static final String STATUS_CLOSED = "Fechada";
	public static final String STATUS_ONHOLD = "On hold";

	public static final String TECH_SSPA = "SSPA";
	public static final String TECH_DOTNET = ".Net Development";
	public static final String TECH_JAVA = "Java Development";
	public static final String TECH_SAFETY = "Safety Critical";
	public static final String TECH_MANAGEMENT = "Project Management";
	public static final String TECH_INTEGRATION = "Integration";

	public static final String SOCIAL_CRITICAL = "Website Critical Software";
	public static final String SOCIAL_LINKEDIN = "Linkedin";
	public static final String SOCIAL_GLASSDOOR = "Glassdoor";
	public static final String SOCIAL_FACEBOOK = "Facebook";

	// Strings related to SubmissionEntity
	public static final String STATUS_SUBMITED = "Submetida";
	public static final String STATUS_ACCEPTED = "Aceite para entrevista";
	public static final String STATUS_PROPOSAL = "Proposta apresentada";
	public static final String STATUS_NEGOTIATION = "Proposta em negociação";
	public static final String STATUS_REJECTED = "Rejeitada";
	public static final String STATUS_HIRED = "Contratado(a)";

	// outros??
	public static final String SOURCE_EXPRESSO = "Expresso";
	public static final String SOURCE_LINKEDIN = "Linkedin";
	public static final String SOURCE_FACEBOOK = "Facebook";
	public static final String SOURCE_GLASSDOOR = "Glassdoor";
	public static final String SOURCE_NETEMPREGO = "Net-Emprego";
	
	public static final List<String> ALL_SOURCES = Arrays.asList(
			Constants.SOURCE_EXPRESSO,
			Constants.SOURCE_FACEBOOK, Constants.SOURCE_LINKEDIN, 
			Constants.SOURCE_NETEMPREGO, Constants.SOURCE_GLASSDOOR);

	// Strings related to StyleEntity
	public static final String DEFAULT_COMPANY_NAME = "ITJobs";
	public static final String DEFAULT_LOGO = "logo_49px.png";
	public static final String DEFAULT_WELCOME_MESSAGE = "Seja bem-vind@ à Plataforma de Gestão de Candidaturas da ";
	public static final String DEFAULT_COMPANY_DESCRIPTION = "Fazemos a gestão das suas candidaturas.";
	public static final String DEFAULT_FOOTER_MESSAGE = "ITJobs -"
			+ " Projecto Final | Programação Avançada em JAVA |"
			+ " Duarte Gonçalves | Renata Silva";
	public static final String DEFAULT_PRIMARY_COLOR = "#3f51b5";
	public static final String DEFAULT_SECONDARY_COLOR = "#ff4081";

	// Strings related to Reports

	public static final String PERIOD_DAILY = "DAILY";
	public static final String PERIOD_MONTHLY = "MONTHLY";
	public static final String PERIOD_YEARLY = "YEARLY";

	public static final char DAILY = 'd';
	public static final char MONTHLY = 'm';
	public static final char YEARLY = 'y';

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
	public static final String SERVLET_SUBJECT = "subject";
	public static final String SERVLET_AUTH_CANDIDATE = "authCand";
	public static final String SERVLET_EMAIL = "email";
	public static final String SERVLET_EMAIL_KEY = "key";
	
	// days to SLA
	public static final int DAYS_TO_SLA = 3;
	
	// types of questions
	public static final String QUESTION_VALUE = "Numérica";
	public static final String QUESTION_ISTRUE = "V/F";
	public static final String QUESTION_ASWER = "Texto livre";

	// working hours
	public static final int MIN_WORK_HOUR = 9;
	public static final int MAX_WORK_HOUR = 18;	

}
