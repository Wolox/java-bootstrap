package wolox.bootstrap.utils;

import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;

/**
 * Class which contains application constants. It's used as a final class to avoid inheritance.
 */
public final class Constants {

    /**
     * Message to communicate that the {@link User} already exists
     */
    public static final String MSG_CODE_EXISTING_USER = "User.already.exists";

    /**
     * Message to communicate that the {@link User} doesn't exist
     */
    public static final String MSG_CODE_NOT_EXISTING_USER = "User.does.not.exist";

    /**
     * Message to communicate that the {@link Role} doesn't exist
     */
    public static final String MSG_CODE_NOT_EXISTING_ROLE = "Role.does.not.exist";

    /**
     * Message to communicate that the {@link Role} already exists
     */
    public static final String MSG_CODE_EXISTING_ROLE = "Role.already.exists";

    /**
     * Message to communicate that the password is not valid according to the {@link PasswordValidator}
     */
    public static final String MSG_CODE_INVALID_PASSWORD = "Invalid.password";

    /**
     * Message to communicate that the password used is not the correct one
     */
    public static final String MSG_CODE_WRONG_PASSWORD = "Wrong.password";

    /**
     * Private constructor to avoid instantiation.
     */
    private Constants(){

    }

}
