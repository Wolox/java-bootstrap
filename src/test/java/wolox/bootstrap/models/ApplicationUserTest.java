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
import wolox.bootstrap.repositories.ApplicationUserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ApplicationUserTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	private ApplicationUser applicationUser;

	@Before
	public void setUp() {
		applicationUser = new ApplicationUser();
		applicationUser.setUsername("username");
		applicationUser.setName("name");
		applicationUser.setPassword("12345678@");
		entityManager.persist(applicationUser);
		entityManager.flush();
	}

	@Test
	public void whenFindByUsername_ThenReturnUser() {
		assert (applicationUserRepository.findByUsername("username").get().getUsername()).
			equals(applicationUser.getUsername());
	}

	@Test
	public void whenAddRole_ThenUserIsInRole() {
		Role role = new Role();
		role.setName("ADMIN");
		applicationUser.addToRole(role);

		assertTrue(applicationUserRepository.findByUsername("username").get().isInRole(role.getName()));
	}

}
