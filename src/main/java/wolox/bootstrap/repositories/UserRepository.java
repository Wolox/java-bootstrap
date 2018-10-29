package wolox.bootstrap.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import wolox.bootstrap.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	List<User> findByName(String name);

	void deleteByUsername(String username);
}

