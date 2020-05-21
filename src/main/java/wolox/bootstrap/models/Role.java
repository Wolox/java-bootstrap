package wolox.bootstrap.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;
import java.util.LinkedList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    @SequenceGenerator(name = "ROLE_SEQ", sequenceName = "ROLE_SEQ")
    @ApiModelProperty(notes = "The role id. It's autogenerated")
    private int id;

    @Column(nullable = false)
    @ApiModelProperty(notes = "The role name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties("roles")
    @ApiModelProperty(notes = "A list of users that have the role")
    private Collection<User> users = new LinkedList<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name.toUpperCase();
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void removeUser(final User user) {
        this.getUsers().remove(user);
        user.getRoles().remove(this);
    }

    public void addUser(final User user) {
        users.add(user);
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