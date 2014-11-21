package ee.bmagrupp.georivals.server.service.imlp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.repository.BattleHistoryRepository;
import ee.bmagrupp.georivals.server.rest.domain.BattleHistoryDTO;
import ee.bmagrupp.georivals.server.service.BattleService;

@Service
public class BattleServicImpl implements BattleService {
	
	@Autowired
	BattleHistoryRepository battleRepo;
	
	@Override
	public List<BattleHistoryDTO> getBattles(String cookie) {
		List <BattleHistoryDTO> lst = null;
		/**
		 * \ todo query findBySid
		 */
		//lst = battleRepo.findBySid(cookie);
		return lst;
	}

}
