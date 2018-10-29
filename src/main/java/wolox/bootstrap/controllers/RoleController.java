package wolox.bootstrap.controllers;

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
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.RoleRepository;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {

	private static final String ROLE_ALREADY_EXISTS = "The role already exists";

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/**")
	public Iterable home() {
		return findAll();
	}

	@PostMapping("/create/**")
	public Iterable save(@RequestBody Role role) {
		Preconditions.checkArgument(!roleRepository.findByName(role.getName()).isPresent(),
			ROLE_ALREADY_EXISTS);
		roleRepository.save(role);
		return findAll();
	}

	@GetMapping("/view/**")
	public Iterable findAll() {
		return roleRepository.findAll();
	}

	@GetMapping("/view/{name}/**")
	public Role findByName(@RequestParam String name) throws RoleNotFoundException {
		return roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException());
	}

	@PutMapping("/updateName/**")
	public Iterable updateName(@RequestParam String oldName, @RequestParam String newName)
		throws RoleNotFoundException {
		Role role = roleRepository.findByName(oldName)
			.orElseThrow(() -> new RoleNotFoundException());
		role.setName(newName);
		roleRepository.save(role);
		return findAll();
	}

	@DeleteMapping("/delete/**")
	public Iterable delete(@RequestParam String name) throws RoleNotFoundException {
		roleRepository.deleteByName(name);
		return findAll();
	}
}
