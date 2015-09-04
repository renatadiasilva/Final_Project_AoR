package pt.uc.dei.aor.pf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.InterviewEntity;

@Stateless
public class InterviewDao extends GenericDao<InterviewEntity> {

	public InterviewDao() {
		super(InterviewEntity.class);
	}

	public List<InterviewEntity> findCarriedOutInterviews(Date date1,
			Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);  //formato!!
		return super.findSomeResults("Interview.carriedOutInterviews", parameters);
	}
}
