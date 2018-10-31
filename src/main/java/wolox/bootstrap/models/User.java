package wolox.bootstrap.models;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import wolox.bootstrap.DAO.UserDAO;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column
  private String password;

  @ManyToMany
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
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
    /*MessageSource messageSource = null;

    Preconditions.checkNotEmpty(username,
        messageSource.getMessage("Empty.field", null, LocaleContextHolder.getLocale()));*/
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    /* MessageSource messageSource = null;
    Preconditions.checkNotEmpty(name,
        messageSource.getMessage("Empty.field", null, LocaleContextHolder.getLocale()));*/
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void addToRole(Role role) {
    roles.add(role);
    role.addUser(this);
  }

  public boolean isInRole(String name) {
    boolean found = false;
    Iterator<Role> iterator = roles.iterator();
    while (!found && iterator.hasNext()) {
      found = iterator.next().getName() == name;
    }
    return found;
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