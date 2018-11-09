package wolox.bootstrap.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.bootstrap.models.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	Optional<Role> findByName(String name);

}
