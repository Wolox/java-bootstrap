package wolox.bootstrap.utils;

public class CamelConstants {

	public static final String HELLO_ENDPOINT = "hello";
	public static final String BYE_ENDPOINT = "bye";
	public static final String GREETING_ENDPOINT = "greeting";

	public static final String HELLO_ROUTE = "direct:say-hi";
	public static final String BYE_ROUTE = "direct:say-bye";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private CamelConstants() {

	}

}
