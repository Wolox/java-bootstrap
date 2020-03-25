package wolox.bootstrap.models;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.bootstrap.configuration.PersistenceConfig;
import wolox.bootstrap.repositories.UserRepository;
import java.util.Date;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @Filter(
		type = ASSIGNABLE_TYPE,
		classes = {PersistenceConfig.class}
))
@AutoConfigureTestDatabase(replace = NONE)
public class UserTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@Before
	public void setUp() {
		user = new User();
		user.setUsername("username");
		user.setName("name");
		user.setPassword("12345678@");
		entityManager.persist(user);
		entityManager.flush();
	}

	@Test
	public void whenFindByUsername_ThenReturnUser() {
		assert (userRepository.findByUsername("username").get().getUsername()).
			equals(user.getUsername());
	}

	@Test
	public void whenAddRole_ThenUserIsInRole() {
		Role role = new Role();
		role.setName("ADMIN");
		user.addToRole(role);

		Assertions.assertTrue(userRepository.findByUsername("username").get().isInRole(role.getName()));
	}

	@Test
	public void whenFindByUsername_ThenReturnUserWithCreationDateBeforeToNow() {
		Date dateAfterSaveUser = new Date();
		User receivedUser = userRepository.findByUsername("username").get();
		Assertions.assertTrue(receivedUser.getCreationDate().before(dateAfterSaveUser));
	}

	@Test
	public void whenFindByUsername_AndModified_ThenReturnUserWithCreationDateBeforeToModifiedDate() {
		User userToBeModified = userRepository.findByUsername("username").get();
		userToBeModified.setName("modified name");
		entityManager.persist(userToBeModified);
		entityManager.flush();
		User userAfterModified = userRepository.findByUsername("username").get();
		Assertions.assertTrue(userAfterModified.getCreationDate().before(userAfterModified.getLastModifiedDate()));
	}

}
