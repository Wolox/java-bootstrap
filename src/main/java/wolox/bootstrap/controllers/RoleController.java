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
import wolox.bootstrap.dtos.RoleDto;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.models.User;
import wolox.bootstrap.repositories.RoleRepository;
import wolox.bootstrap.repositories.UserRepository;
import wolox.bootstrap.utils.Constants;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    public Role create(@RequestBody Role role) {
        Preconditions.checkArgument(!roleRepository.findByName(role.getName()).isPresent(),
            messageSource.getMessage(Constants.MSG_CODE_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale()));
        roleRepository.save(role);
        return role;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Iterable find(@RequestParam(defaultValue = "") final String name,
        @RequestParam(defaultValue = "") final String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.isPresent() ? roleRepository
            .findByNameContainingAndUsersIsInAllIgnoreCase(name, userOpt.get())
            : roleRepository.findByNameContainingAllIgnoreCase(name);
    }

    @GetMapping("/{id}")
    public Role findById(@PathVariable final int id) throws RoleNotFoundException {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale())));
    }

    @PutMapping("/{id}")
    public Role update(@RequestBody final RoleDto roleDto, @PathVariable final int id)
        throws RoleNotFoundException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale())));
        this.setRoleFromRoleDto(role, roleDto);
        roleRepository.save(role);
        return role;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final int id) throws RoleNotFoundException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(
            messageSource.getMessage(Constants.MSG_CODE_NOT_EXISTING_ROLE, null, LocaleContextHolder
                .getLocale())));

        for (User user : role.getUsers()) {
            user.getRoles().remove(role);
        }

        roleRepository.delete(role);
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
