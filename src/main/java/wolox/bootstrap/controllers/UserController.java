package wolox.bootstrap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wolox.bootstrap.repositories.UserRepository;

@RestController
@RequestMapping(value = "api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/**")
	public String index() {
		return "Home";
	}


}
