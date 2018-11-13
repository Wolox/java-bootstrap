package wolox.bootstrap.controllers;

import java.util.Optional;
import javax.management.relation.RoleNotFoundException;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.bootstrap.DAO.RoleDAO;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {

	private static final String ROLE_ALREADY_EXISTS = "The role already exists";

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/")
	public Role create(@RequestBody Role role) {
		Preconditions.checkArgument(!roleRepository.findByName(role.getName()).isPresent(),
			ROLE_ALREADY_EXISTS);
		roleRepository.save(role);
		return role;
	}

	@GetMapping("/")
	public Iterable find(@RequestParam(defaultValue = "") String name,
		@RequestParam(defaultValue = "") String username) {
		Optional<User> userOpt = userRepository.findByUsername(username);
		return userOpt.isPresent() ? roleRepository
			.findByNameContainingAndUsersContainingAllIgnoreCase(name, userOpt.get())
			: roleRepository.findByNameContainingAllIgnoreCase(name);
	}

	@GetMapping("/{id}")
	public Role findById(@RequestParam int id) throws RoleNotFoundException {
		return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException());
	}

	@PutMapping("/{id}")
	public Role update(@RequestBody RoleDAO roleDAO, @RequestParam int id)
		throws RoleNotFoundException {
		Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException());
		role.update(roleDAO);
		roleRepository.save(role);
		return role;
	}

	@DeleteMapping("/{id}")
	public void delete(@RequestParam int id) throws RoleNotFoundException {
		Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException());
		roleRepository.delete(role);
	}
}
