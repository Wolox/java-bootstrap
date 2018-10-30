package wolox.bootstrap.DAO;

public class UserDAO {

	private String username;
	private String name;
	private String password;

	public UserDAO(String username, String name, String password) {
		this.username = username;
		this.name = name;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
}
