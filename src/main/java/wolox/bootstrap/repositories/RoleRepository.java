package wolox.bootstrap.repositories;

import org.springframework.data.repository.CrudRepository;
import wolox.bootstrap.models.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	Role findByName(String name);

}
