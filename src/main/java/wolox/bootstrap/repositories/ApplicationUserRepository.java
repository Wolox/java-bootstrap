package wolox.bootstrap.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import wolox.bootstrap.models.ApplicationUser;
import wolox.bootstrap.models.Role;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findByUsername(String username);

    List<ApplicationUser> findByNameContainingAndUsernameContainingAllIgnoreCase(String name, String username);

    List<ApplicationUser> findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(String name,
                                                                                             String username, Role role);
}