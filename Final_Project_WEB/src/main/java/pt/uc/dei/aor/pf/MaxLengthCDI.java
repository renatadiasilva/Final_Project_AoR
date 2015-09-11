package pt.uc.dei.aor.pf;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.webManagement.EntityFieldLengthsInterface;

@Named(value = "max")
@RequestScoped
public class MaxLengthCDI {
	
	@Inject
	EntityFieldLengthsInterface length;

	public EntityFieldLengthsInterface getLength() {
		return length;
	}
	
}
