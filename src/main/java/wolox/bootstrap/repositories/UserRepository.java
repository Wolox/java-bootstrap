package wolox.bootstrap.repositories;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import wolox.bootstrap.models.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	List<User> findByUsername(String username);

	List<User> findByName(String name);

}

