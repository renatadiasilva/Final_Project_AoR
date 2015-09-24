package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.StyleDao;
import pt.uc.dei.aor.pf.entities.StyleEntity;

@Stateless
public class StyleEJBImp implements StyleEJBInterface{
	
	private static final Logger log = LoggerFactory.getLogger(StyleEJBImp.class);
	
	@EJB
	private StyleDao styleDao;

	@Override
	public void save(StyleEntity style) {
		styleDao.save(style);
		log.info("New style ("+style.getCompanyName()+") saved.");
	}
	
	@Override
	public StyleEntity saveAndReturn(StyleEntity style) {
		log.info("New style ("+style.getCompanyName()+") saved and returned.");
		return styleDao.saveAndReturn(style);
	}

	@Override
	public void update(StyleEntity style) {
		styleDao.update(style);
		log.info("Style ("+style.getCompanyName()+") updated.");
	}

	@Override
	public List<StyleEntity> findAll() {
		log.info("Retreiving all styles.");
		return styleDao.findAll();
	}
	
	@Override
	public StyleEntity findDefaulStyle(){
		log.info("Retreiving default style.");
		return styleDao.findDefaultStyle();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(StyleEntity style) {
		styleDao.delete(style.getId(), (Class<StyleEntity>) style.getClass());
		log.info("Style ("+style.getCompanyName()+") deleted.");
	}

}
