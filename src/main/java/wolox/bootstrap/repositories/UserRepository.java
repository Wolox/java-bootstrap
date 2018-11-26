package wolox.bootstrap.repositories;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    User findById(int id);

    User findByUsername(String username);

    Iterable<User> findByNameContainingAndUsernameContainingAllIgnoreCase(String name,
        String username);

    List<User> findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(String name,
        String username, Role role);
}