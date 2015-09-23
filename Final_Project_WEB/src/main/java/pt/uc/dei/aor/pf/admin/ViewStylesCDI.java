package pt.uc.dei.aor.pf.admin;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.StyleEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.StyleEntity;
import pt.uc.dei.aor.pf.session.StyleSessionManagement;

@Named
@RequestScoped
public class ViewStylesCDI {
	
	@EJB
	private StyleEJBInterface styleEJB;
	
	@Inject
	private StyleSessionManagement currentSessionStyle;
	
	public List<StyleEntity> getStyles() {
		
		// Por alguma razão volta e meia o estilo default persiste. Não acredito em bruxas, mas que as há, há.
		for(StyleEntity style:this.styleEJB.findAll())
			if(style.getCompanyName().equals(Constants.DEFAULT_COMPANY_NAME))
				this.styleEJB.delete(style);
		
		return this.styleEJB.findAll();
	}
	
	public void deleteStyle(StyleEntity style){
		this.styleEJB.delete(style);
		this.currentSessionStyle.init();
	}
	
	public void newDefaultStyle(StyleEntity style){
		// Desmarca o default actual - se existir
		StyleEntity currentDefault=this.styleEJB.findDefaulStyle();
		if(currentDefault!=null){
			currentDefault.setUserDefaultStyle(false);
			this.styleEJB.update(currentDefault);
		}
		
		// Marca o novo default
		style.setUserDefaultStyle(true);
		this.styleEJB.update(style);
		
		// Reinicia o estilo da sessão
		this.currentSessionStyle.init();
	}
	
	public void unsetDefault(StyleEntity style){
		style.setUserDefaultStyle(false);
		this.styleEJB.update(style);

		// Reinicia o estilo da sessão
		this.currentSessionStyle.init();
	}

}
