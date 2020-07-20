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
import static wolox.bootstrap.constants.ApiConstants.USERS_URI;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wolox.bootstrap.configuration.AuthorizationConfig;
import wolox.bootstrap.configuration.SecurityConfiguration;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.repositories.UserRepository;
import wolox.bootstrap.services.CustomUserDetailService;

@WebMvcTest(value = UserController.class)
@ContextConfiguration(classes = {SecurityConfiguration.class, UserController.class})
@Import(AuthorizationConfig.class)
public class UserControllerTest {

    private static final String JSON_PATH_USER_NAME = "$[0].name";

    @Autowired
    MockMvc mvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    CustomUserDetailService customUserDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private Role wrongRole;
    private String userStr;
    private String userUpdateStr;
    private String passwordUpdateStr;
    private String wrongPasswordUpdateStr;
    private String roleStr;

    @BeforeEach
    public void setUp() {
        userStr = "{\"name\": \"name\", \"username\": \"username\", "
            + "\"password\": \"password*\"}";
        userUpdateStr = "{\"name\": \"newName\", \"username\": \"\", "
            + "\"password\": \"password*\"}";
        passwordUpdateStr = "{\"oldPassword\": \"password*\", \"newPassword\": \"newPassword*\"}";
        wrongPasswordUpdateStr = "{\"oldPassword\": \"wrongPassword*\", \"newPassword\": \"wrongNewPassword*\"}";
        roleStr = "{\"name\": \"roleName\"}";
        user = new User();
        user.setName("name");
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password*"));
        Role role = new Role();
        role.setName("roleName");
        wrongRole = new Role();
        wrongRole.setName("wrongRoleName");

        given(userRepository.findByNameContainingAndUsernameContainingAllIgnoreCase("", ""))
            .willReturn(Collections.singletonList(user));
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(roleRepository.findById(1)).willReturn(Optional.of(role));
        given(roleRepository.findAll()).willReturn(Arrays.asList(role, wrongRole));
        given(roleRepository.findByName(wrongRole.getName())).willReturn(Optional.of(wrongRole));

    }

    @Test
    @WithMockUser
    public void givenCreatedUser_whenViewUsers_thenListIsNotEmpty() throws Exception {
        mvc.perform(post(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userStr))
            .andExpect(status().isCreated());
        mvc.perform(get(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath(JSON_PATH_USER_NAME, is(user.getName())));
    }

    @Test
    @WithMockUser
    public void givenUpdatedUser_whenViewUsers_thenUserIsUpdated() throws Exception {
        user.setName("newName");
        mvc.perform(put(USERS_URI + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1")
            .content(userUpdateStr))
            .andExpect(status().isNoContent());
        mvc.perform(get(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath(JSON_PATH_USER_NAME, is(user.getName())))
            .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void givenDeletedUser_whenViewUsers_thenListIsEmpty() throws Exception {
        given(userRepository.findByNameContainingAndUsernameContainingAllIgnoreCase("", ""))
            .willReturn(Collections.emptyList());
        mvc.perform(delete(USERS_URI + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1"))
            .andExpect(status().isNoContent());
        mvc.perform(get(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser
    public void givenUpdatedPassword_whenGetUser_thenPasswordIsNewPassword() throws Exception {
        mvc.perform(put(USERS_URI + "/1/updatePassword")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1")
            .content(passwordUpdateStr))
            .andExpect(status().isNoContent());
        user.setPassword("newPassword*");
        mvc.perform(get(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].password").value(user.getPassword()));
    }

    @Test
    @WithMockUser
    public void whenEnterWrongOldPassword_thenPasswordRemainsTheSame() throws Exception {

        Assertions.assertThrows(Exception.class, () -> mvc.perform(put(USERS_URI + "/1/updatePassword")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1")
            .content(wrongPasswordUpdateStr))
        );
        mvc.perform(get(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].password", is(user.getPassword())));
    }

    @Test
    @WithMockUser
    public void givenAddedUserToRole_whenFindByRole_thenReturnUser() throws Exception {
        mvc.perform(put(USERS_URI + "/1/addRole")
            .contentType(MediaType.APPLICATION_JSON)
            .content(roleStr)
            .param("id", "1"))
            .andExpect(status().isNoContent());
        mvc.perform(get(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .param("roleName", "roleName"))
            .andExpect(status().isOk())
            .andExpect(jsonPath(JSON_PATH_USER_NAME, is(user.getName())));
    }

    @Test
    @WithMockUser
    public void givenNotAddedUserToRole_whenFindByRole_thenDontReturnUser() throws Exception {
        mvc.perform(get(USERS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .param("roleName", wrongRole.getName()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

}
