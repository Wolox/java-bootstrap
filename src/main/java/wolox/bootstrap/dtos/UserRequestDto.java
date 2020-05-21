package wolox.bootstrap.dtos;

import lombok.Data;
import wolox.bootstrap.models.User;

/**
 * Data Transfer Object to get information from the {@link User} requests
 */
@Data
public class UserRequestDto {

    private String username;
    private String name;
    private String password;
}
