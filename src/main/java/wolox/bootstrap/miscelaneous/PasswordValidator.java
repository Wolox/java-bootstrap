package wolox.bootstrap.miscelaneous;

public class PasswordValidator {

	public static final int LENGTH = 8;
	public static final String REGEX = "";
	
	public static boolean passwordIsValid(String password) {
		return (password.length() >= LENGTH && password.matches(REGEX));
	}

}
