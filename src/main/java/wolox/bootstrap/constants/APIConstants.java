package wolox.bootstrap.constants;

/**
 * Class which contains API constants as static fields. This class can't be instantiate
 * or inherited.
 */
public final class APIConstants {

    public static final String BASE_API_URI = "/api/";
    public static final String ROLES_URI = BASE_API_URI + "roles";
    public static final String USERS_URI = BASE_API_URI + "users";

    private APIConstants() {

    }

}
