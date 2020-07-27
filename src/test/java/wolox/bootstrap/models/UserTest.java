package wolox.bootstrap.models;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wolox.bootstrap.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserTest {

    private static final String USERNAME_EXAMPLE = "username";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername(USERNAME_EXAMPLE);
        user.setName("name");
        user.setPassword("12345678@");
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void whenFindByUsername_ThenReturnUser() {
        String usernameFromDB = userRepository
                .findByUsername(USERNAME_EXAMPLE)
                .orElseThrow()
                .getUsername();
        assertEquals(usernameFromDB, user.getUsername());
    }

    @Test
    public void whenAddRole_ThenUserIsInRole() {
        Role role = new Role();
        role.setName("ADMIN");
        user.addToRole(role);

        assertTrue(userRepository
                .findByUsername(USERNAME_EXAMPLE)
                .orElseThrow()
                .isInRole(role.getName())
        );
    }

}
