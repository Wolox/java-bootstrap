package wolox.bootstrap.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;

public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    Page<Role> findByNameContainingAndUsersIsInAllIgnoreCase(String name, User user,
        Pageable pageable);

    Page<Role> findByNameContainingAllIgnoreCase(String name, Pageable pageable);
}
