package wolox.bootstrap.controllers;

import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
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
import wolox.bootstrap.dtos.RoleDto;
import wolox.bootstrap.models.ApplicationUser;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.ApplicationUserRepository;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.utils.Constants;

import javax.management.relation.RoleNotFoundException;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody RoleDto roleDto) throws RoleNotFoundException {
        Preconditions.checkArgument(!roleRepository.findByName(roleDto.getName()).isPresent(),
            messageSource.getMessage(Constants.MSG_CODE_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale()));
        final Role role = new Role(roleDto.getName());
        roleRepository.save(role);
        final URI uri = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder.methodOn(RoleController.class).findById(role.getId())).toUri();
        return ResponseEntity.created(uri).body(role);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<Iterable<Role>> find(@RequestParam(defaultValue = "") final String name,
                                               @RequestParam(defaultValue = "") final String username) {
        final Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findByUsername(username);
        final Iterable<Role> roles = applicationUserOptional.isPresent() ? roleRepository
                .findByNameContainingAndApplicationUsersIsInAllIgnoreCase(name, applicationUserOptional.get())
                : roleRepository.findByNameContainingAllIgnoreCase(name);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable final int id) throws RoleNotFoundException {
        return ResponseEntity.ok(roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale()))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@RequestBody final RoleDto roleDto, @PathVariable final int id)
        throws RoleNotFoundException {
        final Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale())));
        this.setRoleFromRoleDto(role, roleDto);
        roleRepository.save(role);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final int id) throws RoleNotFoundException {
        final Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale())));
        for (final ApplicationUser user : role.getApplicationUsers()) {
            user.getRoles().remove(role);
        }
        roleRepository.delete(role);
        return ResponseEntity.noContent().build();
    }

    /**
     * Set the name (Using uppercase) to the {@link Role} from the {@link RoleDto}
     * @param role The {@link Role} to be updated
     * @param roleDto The {@link RoleDto} from where the information will be obtained
     */
    private void setRoleFromRoleDto(final Role role, final RoleDto roleDto) {
        if (!roleDto.getName().isEmpty()) {
            role.setName(roleDto.getName().toUpperCase());
        }
    }
}
