package ee.bmagrupp.georivals.server.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.georivals.server.game.util.Constants;

@RestController
@RequestMapping("/download")
public class DownloadController {

	@RequestMapping(method = RequestMethod.GET)
	public void download(HttpServletResponse response) {
		try {
			response.sendRedirect(Constants.DOWNLOAD_PAGE);
		} catch (IOException e) {
		}
	}

}
