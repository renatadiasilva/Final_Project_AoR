package pt.uc.dei.aor.pf.converters;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import pt.uc.dei.aor.pf.admin.NewScriptCDI;
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.entities.QuestionEntity;

public class QuestionConverter implements Converter{
	
	@EJB
	private QuestionEJBInterface questionEJB;
	
	@Inject
	private NewScriptCDI newScript;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<QuestionEntity>questions=this.newScript.getQuestionsDB();
		
		for(QuestionEntity q:questions)
			if(q.getQuestion().equals(value))
				return q;
		
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value!=null){
			return value.toString();
		}
		return null;
	} 

}
