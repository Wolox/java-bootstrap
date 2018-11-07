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

import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wolox.bootstrap.DAO.PasswordUpdateDAO;
import wolox.bootstrap.configuration.SecurityConfiguration;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.UserRepository;

@WebAppConfiguration
@WebMvcTest(value = UserController.class, secure = false)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SecurityConfiguration.class, UserController.class})
public class UserControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	UserRepository userRepository;

	private User user;
	private PasswordUpdateDAO passwordUpdateDAO, wrongPasswordUpdateDAO;
	private String userStr, userUpdateStr, passwordUpdateStr, wrongPasswordUpdateStr;

	@Before
	public void setUp() {
		userStr = "{\"name\": \"name\", \"username\": \"username\", "
			+ "\"password\": \"password*\"}";
		userUpdateStr = "{\"name\": \"newName\", \"username\": \"\", "
			+ "\"password\": \"password*\"}";
		passwordUpdateStr = "{\"oldPassword\": \"password*\", \"newPassword\": \"newPassword*\"}";
		wrongPasswordUpdateStr = "{\"oldPassword\": \"wrongPassword*\", \"newPassword\": \"wrongNewPassword*\"}";
		user = new User();
		user.setName("name");
		user.setUsername("username");
		user.setPassword("password*");
		passwordUpdateDAO = new PasswordUpdateDAO();
		passwordUpdateDAO.setOldPassword("password*");
		passwordUpdateDAO.setNewPassword("newPassword*");
		wrongPasswordUpdateDAO = new PasswordUpdateDAO();
		wrongPasswordUpdateDAO.setOldPassword("wrongPassword*");
		wrongPasswordUpdateDAO.setNewPassword("wrongNewPassword*");
		given(userRepository.findAll()).willReturn(Arrays.asList(user));
		given(userRepository.findById(1)).willReturn(Optional.of(user));
	}

	@Test
	public void givenCreatedUser_whenViewUsers_listIsNotEmpty() throws Exception {
		mvc.perform(post("/api/users/")
			.contentType(MediaType.APPLICATION_JSON)
			.content(userStr))
			.andExpect(status().isOk());
		mvc.perform(get("/api/users/")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(user.getName())));
	}

	@Test
	public void givenUpdatedUser_whenViewUsers_userIsUpdated() throws Exception {
		user.setName("newName");
		mvc.perform(put("/api/users/1/")
			.contentType(MediaType.APPLICATION_JSON)
			.param("id", "1")
			.content(userUpdateStr))
			.andExpect(status().isOk());
		mvc.perform(get("/api/users/")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(user.getName())))
			.andExpect(jsonPath("$[0].username", is(user.getUsername())));
	}

	@Test
	public void givenDeletedUser_whenViewUsers_listIsEmpty() throws Exception {
		given(userRepository.findAll()).willReturn(Arrays.asList());
		mvc.perform(delete("/api/users/1/")
			.contentType(MediaType.APPLICATION_JSON)
			.param("id", "1"))
			.andExpect(status().isOk());
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
			.andExpect(status().isOk());
		user.setPassword("newPassword*");
		mvc.perform(get("/api/users/")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].password", is(user.getPassword())));
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
			.andExpect(jsonPath("$[0].password", is(user.getPassword())));
	}
}
