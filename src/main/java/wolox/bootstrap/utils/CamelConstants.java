package wolox.bootstrap.utils;

public class CamelConstants {

	public static final String HELLO_ENDPOINT = "hello";
	public static final String BYE_ENDPOINT = "bye";
	public static final String GREETING_ENDPOINT = "greeting";
	public static final String WEATHER_ENDPOINT = "weather";

	public static final String HELLO_ROUTE = "direct:say-hi";
	public static final String BYE_ROUTE = "direct:say-bye";
	public static final String WEATHER_ROUTE = "direct:get-weather-advice";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private CamelConstants() {

	}

}
