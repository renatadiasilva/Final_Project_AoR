package pt.uc.dei.aor.pf.dao;

import java.util.List;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.StyleEntity;

@Stateless
public class StyleDao extends GenericDao<StyleEntity> {

	public StyleDao() {
		super(StyleEntity.class);
	}

	public StyleEntity findDefaultStyle(){
		List<StyleEntity> styles=
				super.findSomeResults("Style.findDefaultStyle", null);

		// Se não houver um estilo por defeito na BD, devolve o estilo por defeito da máquina
		if(styles.isEmpty()){
			return new StyleEntity(Constants.DEFAULT_COMPANY_NAME, 
					Constants.DEFAULT_COMPANY_NAME, 
					Constants.DEFAULT_COMPANY_DESCRIPTION,
					Constants.DEFAULT_FOOTER_MESSAGE,
					Constants.DEFAULT_PRIMARY_COLOR, 
					Constants.DEFAULT_SECONDARY_COLOR, false);
		}
		
		return (styles.get(0));
	}
	 
}
