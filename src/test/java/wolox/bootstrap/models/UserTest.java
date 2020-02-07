package wolox.bootstrap.models;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.bootstrap.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
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

		assertTrue(userRepository.findByUsername("username").get().isInRole(role.getName()));
	}

}
