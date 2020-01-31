package wolox.bootstrap.dtos;

import wolox.bootstrap.models.Role;

/**
 * Data Transfer Object to get information from the {@link Role} requests
 */
public class RoleDto {

    private String name;

    public RoleDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
