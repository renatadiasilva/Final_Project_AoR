package pt.uc.dei.aor.pf.dao.servicesManagement;

import java.util.List;

public interface ServicesManagementInterface {
	
	public static final String AUTHENTICATE_CANDIDATE="authCand";

	public abstract List<String> authenticateCandidate(String email);

}
