package wolox.bootstrap.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	List<User> findByUsernameContainingAllIgnoreCase(String username);

	List<User> findByNameContainingAllIgnoreCase(String name);

	List<User> findByRolesContainingAllIgnoreCase(Role role);

	List<User> findByNameContainingAndUsernameContainingAllIgnoreCase(String name, String username);

	List<User> findByNameContainingAndRolesContainingAllIgnoreCase(String name, Role role);

	List<User> findByUsernameContainingAndRolesContainingAllIgnoreCase(String username, Role role);

	List<User> findByNameContainingAndUsernameContainingAndRolesContainingAllIgnoreCase(String name,
		String username, Role role);

	List<User> findByRolesIn(List<Role> roles);

}