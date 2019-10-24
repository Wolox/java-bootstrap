package wolox.bootstrap.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import wolox.bootstrap.DAO.FooDAO;

@Document
public class Foo {

    @Id
    private String id;

    private String description;

    public Foo() {
    }

    public Foo(String description) {
        this.description = description;
    }

    public Foo(FooDAO dao) {
        this.description = dao.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Foo && description.equals(((Foo) obj).getDescription());
    }
}
