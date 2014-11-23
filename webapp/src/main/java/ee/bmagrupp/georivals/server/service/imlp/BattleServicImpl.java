package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.BattleHistory;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.repository.BattleHistoryRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.rest.domain.BattleHistoryDTO;
import ee.bmagrupp.georivals.server.rest.domain.BattleType;
import ee.bmagrupp.georivals.server.service.BattleService;

@Service
public class BattleServicImpl implements BattleService {
	
	@Autowired
	BattleHistoryRepository battleRepo;
	
	@Autowired
	OwnershipRepository ownerRepo;
	
	@Autowired
	PlayerRepository playerRepo;
	
	@Override
	public List<BattleHistoryDTO> getBattles(String cookie) {
		List <BattleHistory> lst = battleRepo.findBySid(cookie);
		Player requestMaker = playerRepo.findBySid(cookie);
		ArrayList<BattleHistoryDTO> rtrn = new ArrayList<BattleHistoryDTO>();
		for(BattleHistory h : lst){
			boolean attackerWon = h.isAttackerWon();
			Ownership a = ownerRepo.findByProvinceId(h.getLocation().getId());
			if(requestMaker.getId() == h.getAttacker().getId()){
				if(attackerWon){
					rtrn.add(new BattleHistoryDTO(h.getId(), a.getProvinceName(), h.getDefender().getUserName()
							, h.getAttackerStrength(), h.getDefenderStrength(), h.getAttackerLosses(), h.getDefenderLosses(),BattleType.ATTACK_PLAYER_WON));
				}
				else{
					rtrn.add(new BattleHistoryDTO(h.getId(), a.getProvinceName(), h.getDefender().getUserName()
							, h.getAttackerStrength(), h.getDefenderStrength(), h.getAttackerLosses(), h.getDefenderLosses(),BattleType.ATTACK_PLAYER_LOST));
				}
			}
			else{
				if(attackerWon){
					rtrn.add(new BattleHistoryDTO(h.getId(), a.getProvinceName(), h.getAttacker().getUserName()
							, h.getDefenderStrength(), h.getAttackerStrength(), h.getDefenderLosses(), h.getAttackerLosses(), BattleType.DEFENCE_PLAYER_LOST));
				}
				else{
					rtrn.add(new BattleHistoryDTO(h.getId(), a.getProvinceName(), h.getAttacker().getUserName()
							, h.getDefenderStrength(), h.getAttackerStrength(), h.getDefenderLosses(), h.getAttackerLosses(), BattleType.DEFENCE_PLAYER_WON));
				}
			}
		}
		return rtrn;
	}

}
