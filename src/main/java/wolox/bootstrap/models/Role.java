package wolox.bootstrap.models;

import java.util.Collection;
import java.util.LinkedList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import wolox.bootstrap.DAO.RoleDAO;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(nullable = false)
	private String name;

	@ManyToMany(mappedBy = "roles")
	private Collection<User> users = new LinkedList<>();

	public Role() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public void addUser(User user) {
		users.add(user);
	}

	public void update(RoleDAO roleDAO) {
		if (!roleDAO.getName().isEmpty()) {
			this.setName(roleDAO.getName().toUpperCase());
		}
	}
}