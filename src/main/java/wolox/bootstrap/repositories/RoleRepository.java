package wolox.bootstrap.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.ApplicationUser;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    Iterable<Role> findByNameContainingAndApplicationUsersIsInAllIgnoreCase(String name, ApplicationUser applicationUser);

    Iterable<Role> findByNameContainingAllIgnoreCase(String name);
}
