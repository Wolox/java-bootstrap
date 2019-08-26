package wolox.bootstrap.controllers;

import java.util.Optional;
import javax.management.relation.RoleNotFoundException;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.bootstrap.DAO.PasswordUpdateDAO;
import wolox.bootstrap.DAO.UserDAO;
import wolox.bootstrap.miscelaneous.PasswordValidator;
import wolox.bootstrap.models.ApplicationUser;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.repositories.ApplicationUserRepository;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/")
    public ApplicationUser create(@RequestBody ApplicationUser applicationUser) {
        Preconditions
            .checkArgument(!applicationUserRepository.findByUsername(applicationUser.getUsername()).isPresent(),
                messageSource.getMessage("User.already.exists", null, LocaleContextHolder
                    .getLocale()));
        String password = applicationUser.getPassword();
        Preconditions
            .checkArgument(PasswordValidator.passwordIsValid(password),
                messageSource.getMessage("Invalid.password", null, LocaleContextHolder
                    .getLocale()));
        applicationUser.setPassword(passwordEncoder.encode(password));
        applicationUserRepository.save(applicationUser);
        return applicationUser;
    }

    @GetMapping("/")
    public Iterable find(@RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "") String username,
        @RequestParam(defaultValue = "") String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        return roleOpt.isPresent() ? applicationUserRepository
            .findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(name,
                username, roleOpt.get())
            : applicationUserRepository
                .findByNameContainingAndUsernameContainingAllIgnoreCase(name, username);
    }

    @PutMapping("/{id}")
    public ApplicationUser update(@RequestBody UserDAO userDAO, @PathVariable int id) {
        ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale())));
        applicationUser.update(userDAO);
        applicationUserRepository.save(applicationUser);
        return applicationUser;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}/updatePassword")
    public ApplicationUser updatePassword(@PathVariable int id,
                                          @RequestBody PasswordUpdateDAO passwordUpdateDAO) throws RoleNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale())));
        boolean equal = passwordEncoder
            .matches(passwordUpdateDAO.getOldPassword(), applicationUser.getPassword());

        String newPassword = passwordUpdateDAO.getNewPassword();
        Preconditions
            .checkArgument(equal,
                messageSource.getMessage("Wrong.password", null, LocaleContextHolder
                    .getLocale()));
        applicationUser.setPassword(passwordEncoder.encode(newPassword));
        applicationUserRepository.save(applicationUser);
        return applicationUser;
    }

    @PutMapping("/{id}/addRole")
    public ApplicationUser update(@RequestBody Role role, @RequestParam int id) {
        ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale())));
        applicationUser.addToRole(role);
        applicationUserRepository.save(applicationUser);
        return applicationUser;
    }

    @PutMapping("/{id}/removeRole")
    public ApplicationUser updateRemove(@RequestBody Role role, @RequestParam int id) {
        ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale())));
        Role roleAux = roleRepository.findByName(role.getName())
            .orElseThrow(() -> new RuntimeException(
                messageSource.getMessage("Role.does.not.exist", null, LocaleContextHolder
                    .getLocale())));

        applicationUser.removeRole(roleAux);
        roleRepository.save(roleAux);
        applicationUserRepository.save(applicationUser);
        return applicationUser;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findById(id).orElseThrow(() -> new RuntimeException(
            messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                .getLocale())));
        applicationUserRepository.delete(applicationUser);
    }
}
