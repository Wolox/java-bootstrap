package wolox.bootstrap.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.UserRepository;

@WebAppConfiguration
@WebMvcTest(value = UserController.class, secure = false)
@RunWith(SpringRunner.class)
public class UserControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	UserRepository userRepository;

	private User user;
	private String userStr;

	@Before
	public void setUp() {
		userStr = "{\"name\": \"name\", \"username\": \"username\", "
			+ "\"password\": \"password*\"}";
		user = new User();
		user.setName("name");
		user.setUsername("username");
		user.setPassword("password*");
		given(userRepository.findAll()).willReturn(Arrays.asList(user));
	}

	@Test
	public void givenCreatedUser_whenViewUsers_listIsNotEmpty() throws Exception {
		mvc.perform(post("/api/users/create")
			.contentType(MediaType.APPLICATION_JSON)
			.content(userStr))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(user.getName())));
	}

	@Test
	public void givenUpdatedUser_whenViewUsers_userIsUpdated() throws Exception {
		given(userRepository.findByUsername("username")).willReturn(Optional.of(user));
		user.setUsername("newUsername");
		mvc.perform(put("/api/users/updateName")
			.contentType(MediaType.APPLICATION_JSON)
			.param("oldUsername", "username")
			.param("newUsername", "newUsername"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].username", is(user.getUsername())));
	}

	@Test
	public void givenDeletedUser_whenViewUsers_listIsEmpty() throws Exception {
		given(userRepository.findAll()).willReturn(Arrays.asList());
		mvc.perform(delete("/api/users/delete")
			.contentType(MediaType.APPLICATION_JSON)
			.param("username", "username"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

}
