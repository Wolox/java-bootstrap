package wolox.bootstrap.controllers

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import wolox.bootstrap.models.Role
import wolox.bootstrap.models.User
import wolox.bootstrap.repositories.RoleRepository
import wolox.bootstrap.repositories.UserRepository

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(value = UserController.class, secure = false)
class UserControllerSpec extends Specification {

    @Autowired
    MockMvc mvc

    @SpringBean
    PasswordEncoder passwordEncoder = Stub() {
        encode("password*") >> "encodedPassword*"
        matches("encodedPassword*", "encodedPassword*") >> true
    }

    def role = new Role("ROLENAME")
    def wrongRole = new Role("WRONGROLENAME")
    def user = new User("username", "name", passwordEncoder.encode("password*"))

    @SpringBean
    RoleRepository roleRepository = Stub() {
        findById(1) >> Optional.of(role)
        findAll() >> Arrays.asList(role, wrongRole)
        findByName(wrongRole.getName()) >> Optional.of(wrongRole)
    }

    @SpringBean
    UserRepository userRepository = Stub() {
        findByNameContainingAndUsernameContainingAllIgnoreCase("", "") >> Arrays.asList(user)
        findById(1) >> Optional.of(user)
    }

    def "when post is performed"() {
        given:
        def userReq = "{\"name\": \"name\", \"username\": \"username\", \"password\": \"password*\"}"
        def userRes = "[{\"id\":0,\"username\":\"username\",\"name\":\"name\",\"password\":\"encodedPassword*\",\"roles\":[]}]"

        expect: "Status is 201"
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userReq))
                .andExpect(status().isCreated())
        and: "Status is 200"
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == userRes
    }

    def "when put is performed"() {
        given:
        def userReq = "{\"name\": \"newName\", \"username\": \"username\", \"password\": \"password*\"}"
        def userRes = "[{\"id\":0,\"username\":\"username\",\"name\":\"newName\",\"password\":\"encodedPassword*\",\"roles\":[]}]"

        expect: "Status is 204"
        mvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userReq))
                .andExpect(status().isNoContent())
        and: "Status is 200"
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == userRes
    }

    def "when delete is performed"() {
        expect: "Status is 204"
        mvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
    }

    def "when updatePassword is performed"() {
        given:
        def userReq = "{\"oldPassword\": \"encodedPassword*\", \"newPassword\": \"newPassword*\"}"

        expect: "Status is 204"
        mvc.perform(put("/api/users/1/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userReq))
                .andExpect(status().isNoContent())
    }

    def "when updatePassword is performed with wrong password"() {
        given:
        def userReq = "{\"oldPassword\": \"wrongPassword*\", \"newPassword\": \"newPassword*\"}"

        when:
        mvc.perform(put("/api/users/1/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userReq))
                .andExpect(status().isNoContent())
        then:
        thrown(Exception)
    }

    def "when addRole is performed"() {
        given:
        def userReq = "{\"name\": \"roleName\"}"

        expect: "Status is 204"
        mvc.perform(put("/api/users/1/addRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userReq))
                .andExpect(status().isNoContent())
    }

    def "when get is performed with wrong role"() {
        expect: "Status is 200 and is empty"
        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("roleName", wrongRole.getName()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == "[]"
    }
}