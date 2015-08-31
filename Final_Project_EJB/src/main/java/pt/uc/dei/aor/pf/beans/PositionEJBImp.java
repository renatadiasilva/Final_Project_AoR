package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class PositionEJBImp implements PositionEJBInterface {

	//private static final Logger log = LoggerFactory.getLogger(PositionEJBInterface.class);
	
	@Override
	public void save(PositionEntity position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PositionEntity position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PositionEntity position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PositionEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByDate(Date openingDate1,
			Date openingDate2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByCode(String positionCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByLocation(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByState(String currentState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByCompany(String company) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByTechArea(String tecnhicalArea) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByManager(
			UserEntity positionManager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositionEntity find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByAdmin(UserEntity positionCreator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositions(Date openingDate1,
			Date openingDate2, String positionCode, String title,
			String location, String currentState, String company,
			String tecnhicalArea, UserEntity positionManager,
			UserEntity positionCreator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findOpenPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionEntity> findPositionsByCandidate(
			UserEntity candidate) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
