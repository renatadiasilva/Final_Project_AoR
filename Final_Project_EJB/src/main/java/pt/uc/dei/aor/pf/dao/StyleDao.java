package pt.uc.dei.aor.pf.dao;

import java.util.List;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.StyleEntity;

@Stateless
public class StyleDao extends GenericDao<StyleEntity> {

	public StyleDao() {
		super(StyleEntity.class);
	}

	public StyleEntity findDefaultStyle(){
		List<StyleEntity> styles=super.findSomeResults("Style.findDefaultStyle", null);

		if(styles.isEmpty()){
			return new StyleEntity(StyleEntity.DEFAULT_COMPANY_NAME, StyleEntity.DEFAULT_COMPANY_NAME, StyleEntity.DEFAULT_FOOTER_MESSAGE, StyleEntity.DEFAULT_PRIMARY_COLOR, StyleEntity.DEFAULT_SECONDARY_COLOR, false);
		}
		
		return (styles.get(0));
	}
	 
}
