package wolox.bootstrap.controllers


import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import wolox.bootstrap.models.Role
import wolox.bootstrap.repositories.RoleRepository
import wolox.bootstrap.repositories.UserRepository

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(value = RoleController.class, secure = false)
class RoleControllerSpec extends Specification {

    @Autowired
    MockMvc mvc;

    def role = new Role("ROLENAME")

    @SpringBean
    RoleRepository roleRepository = Stub() {
        findByNameContainingAllIgnoreCase("") >> Arrays.asList(role)
        findById(1) >> Optional.of(role)
    }

    @SpringBean
    UserRepository userRepository = Stub() {
        findByUsername(_) >> Optional.empty()
    }

    def "when post is performed"() {
        given:
        def roleReq = "{\"name\": \"roleName\"}"
        def roleRes = "[{\"id\":0,\"name\":\"ROLENAME\",\"users\":[]}]"

        expect: "Status is 201"
        mvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleReq))
                .andExpect(status().isCreated())
        and: "Status is 200"
        mvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == roleRes
    }

    def "when put is performed"() {
        given:
        def roleReq = "{\"name\": \"newRoleName\"}"
        def roleRes = "[{\"id\":0,\"name\":\"NEWROLENAME\",\"users\":[]}]"

        expect: "Status is 204"
        mvc.perform(put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleReq))
                .andExpect(status().isNoContent())
        and: "Status is 200"
        mvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == roleRes
    }

    def "when delete is performed"() {
        expect: "Status is 204"
        mvc.perform(delete("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
    }
}