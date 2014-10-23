package ee.bmagrupp.aardejaht.server.service.imlp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.OwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.core.repository.ProvinceRepository;
import ee.bmagrupp.aardejaht.server.core.repository.UnitRepository;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;

@Service
public class ProvinceServiceImpl implements ProvinceService {

	private static Logger LOG = LoggerFactory
			.getLogger(ProvinceServiceImpl.class);

	@Autowired
	ProvinceRepository provRepo;

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	OwnershipRepository ownerRepo;

	@Autowired
	HomeOwnershipRepository homeRepo;

	@Autowired
	UnitRepository unitRepo;

	
	public void getProvinces(double latitude, double longitude){
		/*
		 * 	round latitude and longitude to 0.001 precision
		 *  Ownership[] list = getOwnershipsFromDatabase(latitude,longitude);
		 *  for (Ownership a : list){
		 *  	if (a == unowned){
		 *  		calculate province strength
		 *  	}
		 *  	else{
		 *  		find owner and strength
		 *  	}
		 *  }
		 *  
		 *  return SomeSortOfDataStructureContainingProvindes;
		 */
	}
	
	@Override
	public void testStuff() {
		LOG.info("This is province generation test");
		// example how to get stuff from db
		List<Province> provs = (List<Province>) provRepo.findAll();
		for (int i = 0; i < provs.size(); i++) {
			LOG.info(provs.get(i).toString());
		}

	}

	@Override
	public List<ee.bmagrupp.aardejaht.server.rest.domain.Province> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
