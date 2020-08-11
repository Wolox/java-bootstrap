package wolox.bootstrap.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.bootstrap.constants.APIConstants.ROLES_URI;

import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wolox.bootstrap.configuration.AuthorizationConfig;
import wolox.bootstrap.configuration.SecurityConfiguration;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.repositories.UserRepository;
import wolox.bootstrap.services.CustomUserDetailService;

@WebMvcTest(RoleController.class)
@ContextConfiguration(classes = {SecurityConfiguration.class, RoleController.class})
@Import(AuthorizationConfig.class)
public class RoleControllerTest {

    private static final String JSON_PATH_ROLE_NAME = "$[0].name";

    @Autowired
    MockMvc mvc;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CustomUserDetailService customUserDetailService;

    private Role role;
    private String roleStr;
    private String roleUpdateStr;

    @BeforeEach
    public void setUp() {
        roleStr = "{\"name\": \"roleName\"}";
        roleUpdateStr = "{\"name\": \"newRoleName\"}";
        role = new Role();
        role.setName("roleName");
        given(roleRepository.findByNameContainingAllIgnoreCase(""))
            .willReturn(Collections.singletonList(role));
        given(roleRepository.findById(1)).willReturn(Optional.of(role));
        given(roleRepository.findByNameContainingAndUsersContainingAllIgnoreCase("", null))
            .willReturn(Collections.singletonList(role));
    }

    @Test
    public void whenSaveRole_thenStatusCodeIsCreated() throws Exception {
        mvc.perform(post(ROLES_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(roleStr))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void givenExistingRoles_whenViewRoles_thenListIsNotEmpty() throws Exception {
        mvc.perform(get(ROLES_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath(JSON_PATH_ROLE_NAME, is(role.getName())))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void givenUpdatedRole_whenViewRoles_thenRoleIsUpdated() throws Exception {
        role.setName("newRoleName");
        mvc.perform(put(ROLES_URI + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1")
            .content(roleUpdateStr))
            .andExpect(status().isNoContent());
        mvc.perform(get(ROLES_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath(JSON_PATH_ROLE_NAME, is(role.getName())));
    }

    @Test
    @WithMockUser
    public void givenDeletedRole_whenViewRoles_thenListIsEmpty() throws Exception {
        given(roleRepository.findByNameContainingAllIgnoreCase(""))
            .willReturn(Collections.emptyList());
        mvc.perform(delete(ROLES_URI + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1"));
        mvc.perform(get(ROLES_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

}
