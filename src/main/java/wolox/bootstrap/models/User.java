package wolox.bootstrap.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import wolox.bootstrap.DAO.UserDAO;

@Entity
@Table(name = "users")
public class User {

    private static final String EMPTY_FIELD = "This field cannot be empty.";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
    private int id;

    @Column(nullable = false)
    private String username;

    @Column
    private String name;

    @Column
    private String password;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("users")
    private Collection<Role> roles = new LinkedList<>();

    public User() {
    }

    public User(UserDAO userDAO) {
        username = userDAO.getUsername();
        name = userDAO.getName();
    }

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
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
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public void addToRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
            role.addUser(this);
        }
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.removeUser(this);

    }

    public boolean isInRole(String name) {
        boolean found = false;
        Iterator<Role> iterator = roles.iterator();
        while (!found && iterator.hasNext()) {
            found = iterator.next().getName() == name;
        }
        return found;
    }

    public void update(UserDAO userDAO) {
        if (!userDAO.getName().isEmpty()) {
            this.setName(userDAO.getName());
        }
        if (!userDAO.getUsername().isEmpty()) {
            this.setUsername(userDAO.getUsername());
        }
    }


    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", name='" + name + '\'' +
            ", password='" + password + '\'' +
            ", roles=" + roles +
            '}';
    }
}