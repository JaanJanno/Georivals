package ee.bmagrupp.aardejaht.server.service.imlp;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.core.domain.Ownership;
import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.core.domain.Unit;
import ee.bmagrupp.aardejaht.server.core.domain.UnitState;
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
	
	/**
	 * @author Sander
	 * @param latitude
	 * @param longitude
	 * @param playerId - The player who sent the request
	 */
	
	public void getProvinces(double lat1,double lat2, double long1,double long2, int playerId){
		lat1 = Math.round(lat1 * 1000) / 1000;
		lat2 = Math.round(lat2 * 1000) / 1000;
		long1 = Math.round(long1 * 1000) / 1000;
		long2 = Math.round(long2 * 1000) / 1000;
		
		List<Ownership> lst = ownerRepo.findBetween(long1, lat1, long2, lat2);
		for(Ownership a : lst){
			Player player = playerRepo.findOne(a.getId());
			if(player.getId() == 0){
				Player curPlayer = playerRepo.findOne(playerId);
				Set<Ownership> provinces = curPlayer.getOwnedProvinces();
				int overall = 0;
				for(Ownership b : provinces){
					Set<Unit> units = b.getUnits();
					for(Unit unit : units){
						if(unit.getState() == UnitState.CLAIMED){
							overall += unit.getSize();
						}
					}
				}
				// randomly calculate bot land strength, and add to returnable list
			}
			else{
				int overall = 0;
				Set<Unit> units = a.getUnits();
				for (Unit b : units){
					if(b.getState() == UnitState.CLAIMED){
						overall += b.getSize();
					}
				}
				// add this strength to the list along with the marking that it's an enemy
			}
		}
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
