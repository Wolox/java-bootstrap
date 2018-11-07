package wolox.bootstrap.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.management.relation.RoleNotFoundException;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.bootstrap.DAO.PasswordUpdateDAO;
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
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/")
	public User create(@RequestBody User user) {
		Preconditions
			.checkArgument(!userRepository.findByUsername(user.getUsername()).isPresent(),
				USER_ALREADY_EXISTS);
		String password = user.getPassword();
		Preconditions
			.checkArgument(PasswordValidator.passwordIsValid(password),
				INVALID_PASSWORD);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return user;
	}

	@GetMapping(value = {"/", "/{name}", "/{username}", "/{name}/{username}"})
	public Iterable find(@RequestParam(defaultValue = "") String name,
		@RequestParam(defaultValue = "") String username)
		throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		ArrayList<String> methodNameModifiers = new ArrayList<>();
		ArrayList<String> params = new ArrayList<>();
		StringBuilder methodNameStr = new StringBuilder();
		methodNameStr.append("find");
		if (!name.equals("")) {
			methodNameModifiers.add("Name");
			params.add(name);
		}
		if (!username.equals("")) {
			methodNameModifiers.add("Username");
			params.add(username);
		}
		for (int i = 0; i < methodNameModifiers.size(); i++) {
			if (i > 0) {
				methodNameStr.append("And");
			} else {
				methodNameStr.append("By");
			}
			methodNameStr.append(methodNameModifiers.get(i));
		}
		methodNameStr.append("All").append(methodNameModifiers.isEmpty() ? "" : "IgnoreCase");
		Method method = UserRepository.class.getMethod(methodNameStr.toString());
		return params.isEmpty() ? (Iterable) method.invoke(userRepository)
			: (Iterable) method.invoke(userRepository, params);
	}

	@PutMapping("/{id}")
	public User update(@RequestBody UserDAO userDAO, @RequestParam int id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
		user.update(userDAO);
		userRepository.save(user);
		return user;
	}

	@PutMapping("/{id}/updatePassword")
	public User updatePassword(@RequestParam int id,
		@RequestBody PasswordUpdateDAO passwordUpdateDAO) throws RoleNotFoundException {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
		boolean equal = passwordUpdateDAO.getOldPassword().contentEquals(user.getPassword());
		String newPassword = passwordUpdateDAO.getNewPassword();
		Preconditions.checkArgument(equal,
			WRONG_PASSWORD);
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		return user;
	}

	@DeleteMapping("/{id}")
	public void delete(@RequestParam int id) throws UsernameNotFoundException {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
		userRepository.delete(user);
	}

}
