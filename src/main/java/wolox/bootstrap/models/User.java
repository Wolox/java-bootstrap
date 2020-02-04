package wolox.bootstrap.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;

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
import java.util.Collection;
import java.util.LinkedList;

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
    private Collection<Role> roles;

    public User() {
        this.roles = new LinkedList<>();
    }

    public User(final String username, final String name, final String password) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.roles = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        Preconditions.checkNotEmpty(username, EMPTY_FIELD);
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        Preconditions.checkNotEmpty(name, EMPTY_FIELD);
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void addToRole(final Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
            role.addUser(this);
        }
    }

    public void removeRole(final Role role) {
        this.roles.remove(role);
        role.removeUser(this);

    }

    /**
     * Returns true if the {@link User} has the {@link Role} with the role name. False otherwise.
     * @param rolName Name of the searched role
     * @return true if the user has the role. False otherwise.
     */
    public boolean isInRole(final String rolName) {
        return (roles.stream()
                .anyMatch(role -> role.getName().equals(rolName)));
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