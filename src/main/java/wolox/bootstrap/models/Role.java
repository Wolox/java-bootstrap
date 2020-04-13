package wolox.bootstrap.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.Collection;
import java.util.LinkedList;

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
    private Collection<ApplicationUser> applicationUsers = new LinkedList<>();

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

    public Collection<ApplicationUser> getApplicationUsers() {
        return applicationUsers;
    }

    public void removeUser(ApplicationUser applicationUser) {
        this.getApplicationUsers().remove(applicationUser);
        applicationUser.getRoles().remove(this);
    }

    public void addUser(ApplicationUser applicationUser) {
        applicationUsers.add(applicationUser);
    }

    @Override
    public String toString() {
        return "Role{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", applicationUsers=" + applicationUsers +
            '}';
    }


}