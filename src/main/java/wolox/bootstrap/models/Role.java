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
import wolox.bootstrap.DAO.RoleDAO;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public Collection<ApplicationUser> getApplicationUsers() {
        return applicationUsers;
    }

    public void setApplicationUsers(Collection<ApplicationUser> applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    public void removeUser(ApplicationUser applicationUser) {
        this.getApplicationUsers().remove(applicationUser);
        applicationUser.getRoles().remove(this);
    }

    public void addUser(ApplicationUser applicationUser) {
        applicationUsers.add(applicationUser);
    }

    public void update(RoleDAO roleDAO) {
        if (!roleDAO.getName().isEmpty()) {
            this.setName(roleDAO.getName().toUpperCase());
        }
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