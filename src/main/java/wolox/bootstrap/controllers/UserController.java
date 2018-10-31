package wolox.bootstrap.controllers;

import javax.management.relation.RoleNotFoundException;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessageSource messageSource;

  @GetMapping("/**")
  public Iterable index() {
    return findAll();
  }

  @PostMapping("/create")
  public User save(@RequestBody User user) {
    userRepository.save(user);
    return user;
  }

  @GetMapping("/view")
  public Iterable findAll() {
    return userRepository.findAll();
  }

  @GetMapping("/view/{username}")
  public User findByUsername(@PathVariable String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException(
            messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                .getLocale())));
  }

  @PutMapping("/updateName")
  public User updateName(@RequestParam int id, @RequestParam String newName)
      throws RuntimeException {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException(
            messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                .getLocale())));
    user.setName(newName);
    userRepository.save(user);
    return user;
  }

  @PutMapping("/updatePassword")
  public User updatePassword(@RequestParam int id, @RequestParam String oldPassword,
      @RequestParam String newPassword) throws RoleNotFoundException {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException(
            messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                .getLocale())));
    Preconditions.checkArgument(oldPassword == user.getPassword(),
        messageSource.getMessage("Wrong.password", null, LocaleContextHolder
            .getLocale()));
    user.setPassword(newPassword);
    userRepository.save(user);
    return user;
  }

  @DeleteMapping("/delete")
  public void delete(@RequestParam int id) throws RuntimeException {
    userRepository.deleteById(id);
  }

  @PostMapping("/register")
  public void registerAccount(@RequestBody UserDAO userDAO) {
    Preconditions
        .checkArgument(!userRepository.findByUsername(userDAO.getUsername()).isPresent(),
            messageSource.getMessage("User.already.exists", null, LocaleContextHolder
                .getLocale()));
    Preconditions
        .checkArgument(PasswordValidator.passwordIsValid(userDAO.getPassword()),
            messageSource.getMessage("Invalid.password", null, LocaleContextHolder
                .getLocale()));
    User user = new User(userDAO);
    save(user);
  }

}
