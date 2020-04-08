package wolox.bootstrap.dtos;

import lombok.Data;

/**
 * Data Transfer Object to get information from the request to update password
 */
@Data
public class PasswordModificationDto {

    /**
     * Old password, needed for validation
     */
    private String oldPassword;

    /**
     * Value of the new password
     */
    private String newPassword;
}
