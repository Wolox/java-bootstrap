package wolox.bootstrap.utils;

public class PasswordValidator {

	private static final int LENGTH = 8;
	private static final String REGEX = ".{0,}[^a-zA-Z\\d\\s:].{0,}";

	public static boolean passwordIsValid(final String password) {
		return (password.length() >= LENGTH && password.matches(REGEX));
	}

}
