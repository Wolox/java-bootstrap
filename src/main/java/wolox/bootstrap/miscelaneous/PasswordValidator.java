package wolox.bootstrap.miscelaneous;

public class PasswordValidator {

	public static final int LENGTH = 8;
	public static final String REGEX = ".{0,10}[^a-zA-Z\\d\\s:].{0,10}";

	public static boolean passwordIsValid(String password) {
		return (password.length() >= LENGTH && REGEX.matches(password));
	}

}
