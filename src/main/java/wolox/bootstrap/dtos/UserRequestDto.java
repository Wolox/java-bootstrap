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

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
