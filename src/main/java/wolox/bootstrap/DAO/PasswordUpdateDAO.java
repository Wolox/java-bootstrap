package wolox.bootstrap.DAO;

import lombok.Data;

@Data
public class PasswordUpdateDAO {

    private String oldPassword;
    private String newPassword;
    
}
