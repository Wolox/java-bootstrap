package wolox.bootstrap.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import wolox.bootstrap.miscelaneous.PasswordValidator;

@Entity
@Table(name = "users")
public class User {

	private static final String INVALID_PASSWORD = "The provided password does not comply "
		+ "with the requirements.";
	private static final String EMPTY_FIELD = "This field cannot be empty.";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(nullable = false)
	private String username;

	@Column
	private String name;

	@Column
	private String password;

	public User() {
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		Preconditions.checkNotEmpty(username, EMPTY_FIELD);
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Preconditions.checkNotEmpty(name, EMPTY_FIELD);
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		Preconditions
			.checkArgument(!PasswordValidator.passwordIsValid(password), INVALID_PASSWORD);
	}

}