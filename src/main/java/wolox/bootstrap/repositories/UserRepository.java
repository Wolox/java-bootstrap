package wolox.bootstrap.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    List<User> findByNameContainingAndUsernameContainingAllIgnoreCase(String name, String username);

    List<User> findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(String name,
        String username, Role role);
}