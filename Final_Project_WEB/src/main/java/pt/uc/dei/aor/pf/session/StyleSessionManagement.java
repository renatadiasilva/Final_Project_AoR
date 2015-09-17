package pt.uc.dei.aor.pf.session;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class StyleSessionManagement implements Serializable{
	
	private static final long serialVersionUID = 7618604333949807716L;
	
	@Inject
	UserSessionManagement userManagement;
	
	private List<String>style;

	private String companyName;
	
	private String footerMessage;
	
	private String primaryColor;
	
	private String secondaryColor;
	
	private String logoPath;
	
	private String logoWidth;

	public StyleSessionManagement() {
//		this.style=this.userManagement.getStyle();
//		
//		if(this.style!=null&&this.style.size()==4){
//			this.companyName=this.style.get(0);
//			this.footerMessage=this.style.get(1);
//			this.primaryColor=this.style.get(2);
//			this.secondaryColor=this.style.get(3);
//		}else{
			this.companyName="IT Jobs";
			this.footerMessage="IT Jobs - Projecto Final | Programação Avançada em JAVA | Duarte Gonçalves | Renata Silva";
			this.primaryColor="#3f51b5";
			this.secondaryColor="#ff4081";
//		}
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFooterMessage() {
		return footerMessage;
	}

	public void setFooterMessage(String footerMessage) {
		this.footerMessage = footerMessage;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = primaryColor;
	}

	public String getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(String secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getLogoWidth() {
		return logoWidth;
	}

	public void setLogoWidth(String logoWidth) {
		this.logoWidth = logoWidth;
	}

}
