package wolox.bootstrap.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Page<User> findByNameContainingAndUsernameContainingAllIgnoreCase(String name, String username,
        Pageable pageable);

    Page<User> findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(String name,
        String username, Role role, Pageable pageable);
}
