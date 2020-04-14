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

    def "spring context loads for data jpa slice"() {
        given: "some existing books"
        entityManager.persist(new User("username", "name", "12345678@"))
        entityManager.persist(new User("username", "name", "12345678@"))

        expect: "the correct count is inside the repository"
        userRepository.count() == 2L
    }
}
