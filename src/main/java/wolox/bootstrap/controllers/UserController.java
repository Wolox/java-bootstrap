package wolox.bootstrap.controllers;

import javax.management.relation.RoleNotFoundException;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.bootstrap.DAO.UserDAO;
import wolox.bootstrap.miscelaneous.PasswordValidator;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

	private static final String USER_ALREADY_EXISTS = "The user already exists";
	private static final String USER_DOES_NOT_EXIST = "The user does not exist";
	private static final String WRONG_PASSWORD = "The password is incorrect";
	private static final String INVALID_PASSWORD = "The provided password does not comply "
		+ "with the requirements.";

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/**")
	public Iterable index() {
		return findAll();
	}

	@PostMapping("/create/**")
	public Iterable save(@RequestBody User user) {
		userRepository.save(user);
		return findAll();
	}

	@GetMapping("/view/**")
	public Iterable findAll() {
		return userRepository.findAll();
	}

	@GetMapping("/view/{name}/**")
	public User findByUsername(@RequestParam String username) {
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
	}

	@PutMapping("/updateName/**")
	public Iterable updateName(@RequestParam String oldUsername, @RequestParam String newUsername)
		throws RoleNotFoundException {
		User user = userRepository.findByUsername(oldUsername)
			.orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
		user.setName(newUsername);
		userRepository.save(user);
		return findAll();
	}

	@PutMapping("/updatePassword/**")
	public Iterable updatePassword(@RequestParam String username, @RequestParam String oldPassword,
		@RequestParam String newPassword) throws RoleNotFoundException {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
		Preconditions.checkArgument(oldPassword == user.getPassword(), WRONG_PASSWORD);
		user.setPassword(newPassword);
		userRepository.save(user);
		return findAll();
	}

	@DeleteMapping("/delete/**")
	public Iterable delete(@RequestParam String username) throws UsernameNotFoundException {
		userRepository.deleteByUsername(username);
		return findAll();
	}

	@PostMapping("/register")
	public void registerAccount(@RequestBody UserDAO userDAO) {
		Preconditions
			.checkArgument(!userRepository.findByUsername(userDAO.getUsername()).isPresent(),
				USER_ALREADY_EXISTS);
		Preconditions
			.checkArgument(PasswordValidator.passwordIsValid(userDAO.getPassword()),
				INVALID_PASSWORD);
		User user = new User(userDAO);
		save(user);
	}

}
