package ee.bmagrupp.aardejaht.server.service.imlp;

import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.ServerResponse;
import ee.bmagrupp.aardejaht.server.service.AuthenticationService;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Override
	public ServerResponse registrationPhase1(RegistrationDTO registration) {
		return new ServerResponse(ServerResult.OK);
	}

	@Override
	public ServerResponse registrationPhase2(RegistrationDTO registration) {
		// TODO Auto-generated method stub
		return null;
	}

}
