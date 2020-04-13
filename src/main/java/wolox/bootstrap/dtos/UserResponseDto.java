package wolox.bootstrap.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import wolox.bootstrap.models.ApplicationUser;

/**
 * Data Transfer Object to get information from the {@link ApplicationUser} to return in the responses
 */
public class UserResponseDto {

    /**
     * The corresponding {@link ApplicationUser}
     */
    private ApplicationUser user;

    public UserResponseDto(final ApplicationUser user) {
        this.user = user;
    }

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    public int getId() {
        return user.getId();
    }

    @JsonProperty(value = "username", access = JsonProperty.Access.READ_ONLY)
    public String getUsername() {
        return user.getUsername();
    }

    @JsonProperty(value = "name", access = JsonProperty.Access.READ_ONLY)
    public String getName() {
        return user.getName();
    }

}
