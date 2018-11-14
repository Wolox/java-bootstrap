package wolox.bootstrap.controllers;

import java.util.Optional;
import javax.management.relation.RoleNotFoundException;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessageSource messageSource;

  @PostMapping("/")
  public Role create(@RequestBody Role role) {
    Preconditions.checkArgument(!roleRepository.findByName(role.getName()).isPresent(),
        messageSource.getMessage("User.already.exists", null, LocaleContextHolder
            .getLocale()));
    roleRepository.save(role);
    return role;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/")
  public Iterable find(@RequestParam(defaultValue = "") String name,
      @RequestParam(defaultValue = "") String username) {
    Optional<User> userOpt = userRepository.findByUsername(username);
    return userOpt.isPresent() ? roleRepository
        .findByNameContainingAndUsersIsInAllIgnoreCase(name, userOpt.get())
        : roleRepository.findByNameContainingAllIgnoreCase(name);
  }

  @GetMapping("/{id}")
  public Role findById(@PathVariable int id) throws RoleNotFoundException {
    return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
        messageSource.getMessage("Role.does.not.exist", null, LocaleContextHolder
            .getLocale())));
  }

  @PutMapping("/{id}")
  public Role update(@RequestBody RoleDAO roleDAO, @PathVariable int id)
      throws RoleNotFoundException {
    Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
        messageSource.getMessage("Role.does.not.exist", null, LocaleContextHolder
            .getLocale())));
    role.update(roleDAO);
    roleRepository.save(role);
    return role;
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  public void delete(@PathVariable int id) throws RoleNotFoundException {
    Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
        messageSource.getMessage("Role.does.not.exist", null, LocaleContextHolder
            .getLocale())));

    for (User user : role.getUsers()) {
      user.getRoles().remove(role);
    }

    roleRepository.delete(role);
  }
}
