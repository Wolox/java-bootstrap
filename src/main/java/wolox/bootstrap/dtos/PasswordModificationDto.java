package wolox.bootstrap.dtos;

/**
 * Data Transfer Object to get information from the request to update password
 */
public class PasswordModificationDto {

    /**
     * Old password, needed for validation
     */
    private String oldPassword;

    /**
     * Value of the new password
     */
    private String newPassword;

    public PasswordModificationDto() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
