package wolox.bootstrap.dtos;

import wolox.bootstrap.models.User;

/**
 * Data Transfer Object to get information from the {@link User} requests
 */
public class UserRequestDto {

    private String username;
    private String name;
    private String password;

    public UserRequestDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
