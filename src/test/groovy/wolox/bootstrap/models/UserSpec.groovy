package wolox.bootstrap.models

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import spock.lang.Specification
import wolox.bootstrap.repositories.UserRepository

@DataJpaTest
class UserSpec extends Specification {

    @Autowired
    TestEntityManager entityManager

    @Autowired
    UserRepository userRepository

    def "persisting users"() {
        given: "seed the users table"
        entityManager.persist(new User("username1", "name1", "12345678@"))
        entityManager.persist(new User("username2", "name2", "12345678@"))

        expect: "the number of users is 2"
        userRepository.count() == 2L
        and: "the first user is persisted"
        userRepository.findByUsername("username1").get().getName() == "name1"
        and: "the second user is persisted"
        userRepository.findByUsername("username2").get().getName() == "name2"
    }

    def "adding a role to a user"() {
        given: "seed roles table"
        def role = new Role("ADMIN")
        entityManager.persist(role)
        and: "seed users table"
        def user = new User("username1", "name1", "12345678@")
        entityManager.persist(user)

        when: "add a role to the user"
        user.addToRole(role)

        then: "the user has that role"
        userRepository.findByUsername("username1").get().isInRole(role.getName())
    }
}
