package wolox.bootstrap.dtos;

import lombok.Data;
import wolox.bootstrap.models.Role;

/**
 * Data Transfer Object to get information from the {@link Role} requests
 */
@Data
public class RoleDto {

    private String name;
}
