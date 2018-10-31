package wolox.bootstrap.controllers;

import javax.management.relation.RoleNotFoundException;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.RoleRepository;

@RestController()
@RequestMapping(value = "/api/roles")
public class RoleController {


  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private MessageSource messageSource;

  @GetMapping("/")
  public Iterable home() {
    return findAll();
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public Role save(@RequestBody Role role) {
    Preconditions.checkArgument(!roleRepository.findByName(role.getName()).isPresent(),
        messageSource.getMessage("Role.already.exists", null, LocaleContextHolder.getLocale()));
    roleRepository.save(role);
    return role;
  }

  @GetMapping("/view")
  public Iterable findAll() {
    return roleRepository.findAll();
  }

  @GetMapping("/view/{name}")
  public Role findByName(@PathVariable String name) throws RoleNotFoundException {
    return roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException());
  }

  @PutMapping("/updateName")
  public Role updateName(@RequestParam int id, @RequestParam String newName)
      throws RoleNotFoundException {
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new RoleNotFoundException());
    role.setName(newName);
    roleRepository.save(role);
    return role;
  }

  @DeleteMapping("/delete/{name}")
  public void delete(@RequestParam String name) {
    roleRepository.deleteByName(name);
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@RequestParam int id) {
    roleRepository.deleteById(id);
  }
}
