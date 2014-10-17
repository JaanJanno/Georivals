package ee.bmagrupp.aardejaht.server.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

//	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
	        throws IOException, JsonProcessingException {

	    String formattedDate = dateFormat.format(date);

	    gen.writeString(formattedDate);
	}
}
