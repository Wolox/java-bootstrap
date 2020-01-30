package wolox.bootstrap.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import java.util.LinkedList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import wolox.bootstrap.dtos.RoleDto;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    @SequenceGenerator(name = "ROLE_SEQ", sequenceName = "ROLE_SEQ")
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties("roles")
    private Collection<User> users = new LinkedList<>();

    public Role() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public void removeUser(User user) {
        this.getUsers().remove(user);
        user.getRoles().remove(this);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void update(RoleDto roleDto) {
        if (!roleDto.getName().isEmpty()) {
            this.setName(roleDto.getName().toUpperCase());
        }
    }

    @Override
    public String toString() {
        return "Role{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", users=" + users +
            '}';
    }


}