package pt.uc.dei.aor.pf.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "styles")
@NamedQueries({
	@NamedQuery(name = "Style.findDefaultStyle", query = "SELECT s FROM StyleEntity s WHERE s.userDefaultStyle=TRUE")
})
public class StyleEntity implements Serializable{

	private static final long serialVersionUID = 4349097829472427150L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@NotNull
	@Column(name = "style_title", nullable = false)
	private String styleTitle;

	@NotNull
	@Column(name = "company_name", nullable = false)
	private String companyName;

	@NotNull
	@Column(name = "company_description", nullable = false)
	private String companyDescription;
	
	@NotNull
	@Column(name = "footer_message", nullable = false)
	private String footerMessage;

	@NotNull
	@Column(name = "primary_color", nullable = false)
	private String primaryColor;

	@NotNull
	@Column(name = "secondary_color", nullable = false)
	private String secondaryColor;
	
	@Column(name = "logo_format", nullable = true)
	private String logoFormat;

	// O style default está a true, os outros a null
	@Column(name = "default_style", nullable = false)
	private boolean userDefaultStyle;
	

	public StyleEntity() {
	}

	public StyleEntity(String styleTitle, String companyName, String companyDescription, String footerMessage,
			String primaryColor, String secondaryColor, boolean userDefaultStyle) {
		this.styleTitle = styleTitle;
		this.companyName = companyName;
		this.companyDescription = companyDescription;
		this.footerMessage = footerMessage;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.userDefaultStyle = userDefaultStyle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStyleTitle() {
		return styleTitle;
	}

	public void setStyleTitle(String styleTitle) {
		this.styleTitle = styleTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}

	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
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

	public String getLogoFormat() {
		return logoFormat;
	}

	public void setLogoFormat(String logoFormat) {
		this.logoFormat = logoFormat;
	}

	public boolean isUserDefaultStyle() {
		return userDefaultStyle;
	}

	public void setUserDefaultStyle(boolean userDefaultStyle) {
		this.userDefaultStyle = userDefaultStyle;
	}
	
}
