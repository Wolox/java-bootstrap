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
import wolox.bootstrap.dtos.PasswordModificationDto;
import wolox.bootstrap.dtos.UserRequestDto;
import wolox.bootstrap.utils.PasswordValidator;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.repositories.UserRepository;
import wolox.bootstrap.utils.Constants;

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

    @PostMapping
    public User create(@RequestBody final UserRequestDto userRequestDto) {
        Preconditions
            .checkArgument(!userRepository.findByUsername(userRequestDto.getUsername()).isPresent(),
                messageSource.getMessage(Constants.MSG_CODE_EXISTING_USER, null, LocaleContextHolder
                    .getLocale()));
        String password = userRequestDto.getPassword();
        Preconditions
            .checkArgument(PasswordValidator.passwordIsValid(password),
                messageSource.getMessage(Constants.MSG_CODE_INVALID_PASSWORD, null, LocaleContextHolder
                    .getLocale()));
        User user = new User(userRequestDto.getUsername(), userRequestDto.getName(), userRequestDto.getPassword());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    @GetMapping
    public Iterable find(@RequestParam(defaultValue = "") final String name,
        @RequestParam(defaultValue = "") final String username,
        @RequestParam(defaultValue = "") final String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        return roleOpt.isPresent() ? userRepository
            .findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(name,
                username, roleOpt.get())
            : userRepository
                .findByNameContainingAndUsernameContainingAllIgnoreCase(name, username);
    }

    @PutMapping("/{id}")
    public User update(@RequestBody final UserRequestDto userRequestDto, @PathVariable final int id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        this.setUserFromUserRequestDto(user, userRequestDto);
        userRepository.save(user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}/updatePassword")
    public User updatePassword(@PathVariable final int id,
        @RequestBody final PasswordModificationDto passwordModificationDto) throws RoleNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        boolean equal = passwordEncoder
            .matches(passwordModificationDto.getOldPassword(), user.getPassword());

        String newPassword = passwordModificationDto.getNewPassword();
        Preconditions
            .checkArgument(equal,
                messageSource.getMessage(Constants.MSG_CODE_WRONG_PASSWORD, null, LocaleContextHolder
                    .getLocale()));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return user;
    }

    @PutMapping("/{id}/addRole")
    public User update(@RequestBody final Role role, @PathVariable final int id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        user.addToRole(role);
        userRepository.save(user);
        return user;
    }

    @PutMapping("/{id}/removeRole")
    public User updateRemove(@RequestBody final Role role, @PathVariable final int id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        Role roleAux = roleRepository.findByName(role.getName())
            .orElseThrow(() -> new RuntimeException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                    .getLocale())));

        user.removeRole(roleAux);
        roleRepository.save(roleAux);
        userRepository.save(user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final int id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                .getLocale())));
        userRepository.delete(user);
    }

    /**
     * Set the name and the username to the {@link User} from the {@link UserRequestDto}
     * @param user The {@link User} to be updated
     * @param userRequestDto The {@link UserRequestDto} from where the information will be obtained
     */
    private void setUserFromUserRequestDto(final User user, final UserRequestDto userRequestDto) {
        if (!userRequestDto.getName().isEmpty()) {
            user.setName(userRequestDto.getName());
        }
        if (!userRequestDto.getUsername().isEmpty()) {
            user.setUsername(userRequestDto.getUsername());
        }
    }
}
