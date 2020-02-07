package wolox.bootstrap.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    Iterable<Role> findByNameContainingAndUsersIsInAllIgnoreCase(String name, User user);

    Iterable<Role> findByNameContainingAllIgnoreCase(String name);
}
