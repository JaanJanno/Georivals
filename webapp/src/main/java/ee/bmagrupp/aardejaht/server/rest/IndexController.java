package ee.bmagrupp.aardejaht.server.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController implements ErrorController{

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public void error(HttpServletResponse response) {
        try {
			response.sendRedirect("404.html");
		} catch (IOException e) {}
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
