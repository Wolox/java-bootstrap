package wolox.bootstrap.DAO;

import javax.validation.constraints.NotEmpty;

public class FooDAO {

    @NotEmpty
    private String description;

    public FooDAO() {
    }

    public FooDAO(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
