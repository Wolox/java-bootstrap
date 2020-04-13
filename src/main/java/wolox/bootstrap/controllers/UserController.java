package wolox.bootstrap.controllers;

import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
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
import wolox.bootstrap.dtos.RoleDto;
import wolox.bootstrap.dtos.UserRequestDto;
import wolox.bootstrap.models.ApplicationUser;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.ApplicationUserRepository;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.utils.Constants;
import wolox.bootstrap.utils.PasswordValidator;

import javax.management.relation.RoleNotFoundException;
import java.net.URI;
import java.util.Optional;

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

    @PostMapping
    public ResponseEntity<ApplicationUser> create(@RequestBody final UserRequestDto userRequestDto) {
        Preconditions
            .checkArgument(!applicationUserRepository.findByUsername(userRequestDto.getUsername()).isPresent(),
                messageSource.getMessage(Constants.MSG_CODE_EXISTING_USER, null, LocaleContextHolder
                    .getLocale()));
        final String password = userRequestDto.getPassword();
        Preconditions
            .checkArgument(PasswordValidator.passwordIsValid(password),
                messageSource.getMessage(Constants.MSG_CODE_INVALID_PASSWORD, null, LocaleContextHolder
                    .getLocale()));
        final ApplicationUser applicationUser = new ApplicationUser(userRequestDto.getUsername(),
                userRequestDto.getName(), userRequestDto.getPassword());
        applicationUser.setPassword(passwordEncoder.encode(password));
        applicationUserRepository.save(applicationUser);
        final URI uri = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder.methodOn(UserController.class)
                        .find("", applicationUser.getUsername(), "")).toUri();
        return ResponseEntity.created(uri).body(applicationUser);
    }

    @GetMapping
    public ResponseEntity<Iterable<ApplicationUser>> find(@RequestParam(defaultValue = "") final String name,
        @RequestParam(defaultValue = "") final String username,
        @RequestParam(defaultValue = "") final String roleName) {
        final Optional<Role> roleOpt = roleRepository.findByName(roleName);
        Iterable<ApplicationUser> applicationUsers = roleOpt.isPresent() ? applicationUserRepository
            .findByNameContainingAndUsernameContainingAndRolesIsInAllIgnoreCase(name,
                username, roleOpt.get())
            : applicationUserRepository
                .findByNameContainingAndUsernameContainingAllIgnoreCase(name, username);
        return ResponseEntity.ok(applicationUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUser> update(@RequestBody final UserRequestDto userRequestDto,
                                                  @PathVariable final int id) {
        final ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        this.setUserFromUserRequestDto(applicationUser, userRequestDto);
        applicationUserRepository.save(applicationUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}/updatePassword")
    public ResponseEntity<ApplicationUser> updatePassword(@PathVariable final int id,
        @RequestBody final PasswordModificationDto passwordModificationDto) throws RoleNotFoundException {
        final ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        boolean equal = passwordEncoder
            .matches(passwordModificationDto.getOldPassword(), applicationUser.getPassword());
        final String newPassword = passwordModificationDto.getNewPassword();
        Preconditions
            .checkArgument(equal,
                messageSource.getMessage(Constants.MSG_CODE_WRONG_PASSWORD, null, LocaleContextHolder
                    .getLocale()));
        applicationUser.setPassword(passwordEncoder.encode(newPassword));
        applicationUserRepository.save(applicationUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/addRole")
    public ResponseEntity<ApplicationUser> update(@RequestBody final RoleDto roleDto, @PathVariable final int id) {
        final ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        final Role role = new Role(roleDto.getName());
        applicationUser.addToRole(role);
        applicationUserRepository.save(applicationUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/removeRole")
    public ResponseEntity<ApplicationUser> updateRemove(@RequestBody final RoleDto roleDto, @PathVariable final int id) {
        final ApplicationUser applicationUser = applicationUserRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null, LocaleContextHolder
                    .getLocale())));
        final Role role = roleRepository.findByName(roleDto.getName())
            .orElseThrow(() -> new RuntimeException(
                messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                    .getLocale())));
        applicationUser.removeRole(role);
        roleRepository.save(role);
        applicationUserRepository.save(applicationUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final int id) throws UsernameNotFoundException {
        final ApplicationUser applicationUser = applicationUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_USER, null,
                    LocaleContextHolder.getLocale())));
            applicationUserRepository.delete(applicationUser);
        return ResponseEntity.noContent().build();
    }

    /**
     * Set the name and the username to the {@link ApplicationUser} from the {@link UserRequestDto}
     * @param user The {@link ApplicationUser} to be updated
     * @param userRequestDto The {@link UserRequestDto} from where the information will be obtained
     */
    private void setUserFromUserRequestDto(final ApplicationUser user, final UserRequestDto userRequestDto) {
        if (!userRequestDto.getName().isEmpty()) {
            user.setName(userRequestDto.getName());
        }
        if (!userRequestDto.getUsername().isEmpty()) {
            user.setUsername(userRequestDto.getUsername());
        }
    }
}
