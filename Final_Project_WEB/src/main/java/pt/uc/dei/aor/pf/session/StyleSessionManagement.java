package pt.uc.dei.aor.pf.session;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import pt.uc.dei.aor.pf.beans.StyleEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.StyleEntity;

@Named
@SessionScoped
public class StyleSessionManagement implements Serializable{

	private static final long serialVersionUID = 7618604333949807716L;

	@EJB
	private StyleEJBInterface styleBean;

	private Long id;

	private String companyName;

	private String footerMessage;

	private String primaryColor;

	private String secondaryColor;

	private boolean defaultLogo;

	private String logoFormat;

	public StyleSessionManagement() {
		this.companyName=Constants.DEFAULT_COMPANY_NAME;
		this.footerMessage=Constants.DEFAULT_FOOTER_MESSAGE;
		this.primaryColor=Constants.DEFAULT_PRIMARY_COLOR;
		this.secondaryColor=Constants.DEFAULT_SECONDARY_COLOR;
		this.defaultLogo=true;
	}

	@PostConstruct
	public void init(){
		StyleEntity style=styleBean.findDefaulStyle();

		this.id=style.getId();
		this.companyName=style.getCompanyName();
		this.footerMessage=style.getFooterMessage();
		this.primaryColor=style.getPrimaryColor();
		this.secondaryColor=style.getSecondaryColor();
		this.logoFormat=style.getLogoFormat();
		
		if(style.isUserDefaultStyle()){
			this.defaultLogo=false;
		}else{
			this.defaultLogo=true;
		}

	}

	public String getCompanyName() {
		return companyName;
	}

	public String getFooterMessage() {
		return footerMessage;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public String getSecondaryColor() {
		return secondaryColor;
	}

	public String getLogoPath() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		System.out.println("1 "+request.getScheme());
		System.out.println("2 "+request.getContextPath());
		System.out.println("3 "+request.getServletPath());
		System.out.println("4 "+request.getRequestURI());
		System.out.println("5 "+request.getRequestURL());
		System.out.println("6 "+request.getServerName());
		System.out.println("7 "+request.getServerPort());
		
//		return request.getContextPath()+"/customLogos/"+"critical"+".jpg";

		if(this.companyName.equals(Constants.DEFAULT_COMPANY_NAME)){
			return "";
		}
		request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/customLogos/"+this.id+this.logoFormat;
	}

	public boolean isDefaultLogo() {
		return defaultLogo;
	}

	public String getITJobsLogo(){
		return Constants.DEFAULT_LOGO;
	}

}
