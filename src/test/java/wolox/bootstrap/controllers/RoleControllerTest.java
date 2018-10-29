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
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.RoleRepository;

@WebAppConfiguration
@WebMvcTest(value = RoleController.class, secure = false)
@RunWith(SpringRunner.class)
public class RoleControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	RoleRepository roleRepository;

	private Role role;
	private String roleStr;

	@Before
	public void setUp() {
		roleStr = "{\"name\": \"name\"}";
		role = new Role();
		role.setName("name");
		given(roleRepository.findAll()).willReturn(Arrays.asList(role));
	}

	@Test
	public void givenCreatedRole_whenViewRoles_listIsNotEmpty() throws Exception {
		mvc.perform(post("/api/roles/create")
			.contentType(MediaType.APPLICATION_JSON)
			.content(roleStr))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(role.getName())));
	}

	@Test
	public void givenUpdatedRole_whenViewRoles_roleIsUpdated() throws Exception {
		given(roleRepository.findByName("name")).willReturn(Optional.of(role));
		role.setName("newName");
		mvc.perform(put("/api/roles/updateName")
			.contentType(MediaType.APPLICATION_JSON)
			.param("oldName", "name")
			.param("newName", "newName"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(role.getName())));
	}

	@Test
	public void givenDeletedRole_whenViewRoles_listIsEmpty() throws Exception {
		given(roleRepository.findAll()).willReturn(Arrays.asList());
		mvc.perform(delete("/api/roles/delete")
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "newName"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}
}
