package ee.bmagrupp.georivals.server.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovementWorker implements Runnable {

	@Autowired
	EndMovementService endMovServ;

	private int movementId;

	public MovementWorker(int movementId) {
		this.movementId = movementId;
	}

	public MovementWorker() {

	}

	@Override
	public void run() {
		System.out.println("worker running with id " + movementId);
		endMovServ.handleMovement(movementId);

	}

	public void setMovementId(int movementId) {
		this.movementId = movementId;
	}
}
