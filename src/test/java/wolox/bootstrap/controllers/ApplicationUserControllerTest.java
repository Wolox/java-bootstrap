package wolox.bootstrap.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wolox.bootstrap.configuration.SecurityConfiguration;
import wolox.bootstrap.models.ApplicationUser;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.ApplicationUserRepository;
import wolox.bootstrap.repositories.RoleRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@WebMvcTest(value = UserController.class, secure = false)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SecurityConfiguration.class, UserController.class})
public class ApplicationUserControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    ApplicationUserRepository applicationUserRepository;
    @MockBean
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationUser applicationUser;
    private Role role, wrongRole;
    private String userStr, userUpdateStr, passwordUpdateStr, wrongPasswordUpdateStr, roleStr;

    @Before
    public void setUp() {
        userStr = "{\"name\": \"name\", \"username\": \"username\", "
            + "\"password\": \"password*\"}";
        userUpdateStr = "{\"name\": \"newName\", \"username\": \"\", "
            + "\"password\": \"password*\"}";
        passwordUpdateStr = "{\"oldPassword\": \"password*\", \"newPassword\": \"newPassword*\"}";
        wrongPasswordUpdateStr = "{\"oldPassword\": \"wrongPassword*\", \"newPassword\": \"wrongNewPassword*\"}";
        roleStr = "{\"name\": \"roleName\"}";
        applicationUser = new ApplicationUser();
        applicationUser.setName("name");
        applicationUser.setUsername("username");
        applicationUser.setPassword(passwordEncoder.encode("password*"));
        role = new Role();
        role.setName("roleName");
        wrongRole = new Role();
        wrongRole.setName("wrongRoleName");
        given(applicationUserRepository.findByNameContainingAndUsernameContainingAllIgnoreCase("", ""))
            .willReturn(Arrays.asList(applicationUser));
        given(applicationUserRepository.findById(1)).willReturn(Optional.of(applicationUser));
        given(roleRepository.findById(1)).willReturn(Optional.of(role));
        given(roleRepository.findAll()).willReturn(Arrays.asList(role, wrongRole));
        given(roleRepository.findByName(wrongRole.getName())).willReturn(Optional.of(wrongRole));
    }

    @Test
    public void givenCreatedUser_whenViewUsers_listIsNotEmpty() throws Exception {
        mvc.perform(post("/api/users/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userStr))
            .andExpect(status().isCreated());
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is(applicationUser.getName())));
    }

    @Test
    public void givenUpdatedUser_whenViewUsers_userIsUpdated() throws Exception {
        applicationUser.setName("newName");
        mvc.perform(put("/api/users/1/")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1")
            .content(userUpdateStr))
            .andExpect(status().isNoContent());
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is(applicationUser.getName())))
            .andExpect(jsonPath("$[0].username", is(applicationUser.getUsername())));
    }

    @Test
    public void givenDeletedUser_whenViewUsers_listIsEmpty() throws Exception {
        given(applicationUserRepository.findByNameContainingAndUsernameContainingAllIgnoreCase("", ""))
            .willReturn(Arrays.asList());
        mvc.perform(delete("/api/users/1/")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1"))
            .andExpect(status().isNoContent());
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenUpdatedPassword_whenGetUser_passwordIsNewPassword() throws Exception {
        mvc.perform(put("/api/users/1/updatePassword")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1")
            .content(passwordUpdateStr))
            .andExpect(status().isNoContent());
        applicationUser.setPassword("newPassword*");
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].password").value(applicationUser.getPassword()));
    }

    @Test(expected = Exception.class)
    public void whenEnterWrongOldPassword_dontUpdate() throws Exception {
        mvc.perform(put("/api/users/1/updatePassword")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "1")
            .content(wrongPasswordUpdateStr))
            .andExpect(status().isOk());
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].password", is(applicationUser.getPassword())));
    }

    @Test
    public void givenAddedUserToRole_whenFindByRole_thenReturnUser() throws Exception {
        mvc.perform(put("/api/users/1/addRole")
            .contentType(MediaType.APPLICATION_JSON)
            .content(roleStr)
            .param("id", "1"))
            .andExpect(status().isNoContent());
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON)
            .param("roleName", "roleName"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name", is(applicationUser.getName())));
    }

    @Test
    public void givenNotAddedUserToRole_whenFindByRole_thenDontReturnUser() throws Exception {
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON)
            .param("roleName", wrongRole.getName()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

}
