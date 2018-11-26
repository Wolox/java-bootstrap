package wolox.bootstrap.controllers;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.bootstrap.DAO.PasswordUpdateDAO;
import wolox.bootstrap.DAO.UserDAO;
import wolox.bootstrap.miscelaneous.PasswordValidator;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/create")
    public User create(@RequestBody User user) {
        Preconditions
            .checkArgument(userRepository.findByUsername(user.getUsername()) == null,
                messageSource.getMessage("User.already.exists", null, LocaleContextHolder
                    .getLocale()));
        String password = user.getPassword();
        Preconditions
            .checkArgument(PasswordValidator.passwordIsValid(password),
                messageSource.getMessage("Invalid.password", null, LocaleContextHolder
                    .getLocale()));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/find")
    public Iterable find(@RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "") String username,
        @RequestParam(defaultValue = "") String roleName) {
        Role roleOpt = roleRepository.findByName(roleName);
        return roleOpt != null ? userRepository
            .findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(name,
                username, roleOpt)
            : (Iterable) userRepository
                .findByNameContainingAndUsernameContainingAllIgnoreCase(name, username);
    }

    @PutMapping("/update")
    public User update(@RequestBody UserDAO userDAO, @RequestParam int id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale()));
        }
        user.update(userDAO);
        userRepository.save(user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/updatePassword")
    public User updatePassword(@RequestParam int id,
        @RequestBody PasswordUpdateDAO passwordUpdateDAO) throws RoleNotFoundException {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale()));
        }
        boolean equal = passwordEncoder
            .matches(passwordUpdateDAO.getOldPassword(), user.getPassword());

        String newPassword = passwordUpdateDAO.getNewPassword();
        Preconditions
            .checkArgument(equal,
                messageSource.getMessage("Wrong.password", null, LocaleContextHolder
                    .getLocale()));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return user;
    }

    @PutMapping("/addRole")
    public User update(@RequestBody Role role, @RequestParam int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale()));
        }

        Role roleAux = roleRepository.findByName(role.getName());
        if (roleAux == null) {
            throw new RuntimeException(
                messageSource.getMessage("Role.does.not.exist", null, LocaleContextHolder
                    .getLocale()));
        }

        user.addToRole(role);
        userRepository.save(user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/removeRole")
    public User updateRemove(@RequestBody Role role, @RequestParam int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UsernameNotFoundException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale()));
        }
        Role roleAux = roleRepository.findByName(role.getName());
        if (roleAux == null) {
            throw new RuntimeException(
                messageSource.getMessage("Role.does.not.exist", null, LocaleContextHolder
                    .getLocale()));
        }
        user.removeRole(roleAux);
        roleRepository.save(roleAux);
        userRepository.save(user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public void delete(@RequestParam int id) throws UsernameNotFoundException {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException(
                messageSource.getMessage("User.does.not.exist", null, LocaleContextHolder
                    .getLocale()));
        }
        userRepository.delete(user);
    }
}
