package ee.bmagrupp.georivals.mobile.core.communications;

import java.lang.reflect.Type;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Class for defining a singleton Gson object that supports Date objects. Use
 * the getInstance() method to get the singleton object.
 * 
 * @author Jaan Janno
 */

public class GsonParser {

	private static final Gson instance; // Singleton object.

	static {

		/*
		 * Builds the singleton Gson object and adds support for Date object
		 * parsing.
		 */

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			public Date deserialize(JsonElement json, Type type,
					JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		});
		instance = builder.create();
	}

	// Singleton, so private constructor.

	private GsonParser() {
	}

	/**
	 * 
	 * @return Gson object that supports Date objects.
	 */

	public static Gson getInstance() {
		return instance;
	}

}
