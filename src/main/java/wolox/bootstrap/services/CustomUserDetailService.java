package wolox.bootstrap.services;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wolox.bootstrap.models.ApplicationUser;
import wolox.bootstrap.models.Role;
import wolox.bootstrap.repositories.ApplicationUserRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Autowired
    public CustomUserDetailService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws RuntimeException {

        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username).orElseThrow(RuntimeException::new);

        if (applicationUser == null) {
            throw new UsernameNotFoundException("username " + username
                + " not found");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : applicationUser.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(applicationUser.getUsername(),
            applicationUser.getPassword(), grantedAuthorities);
    }
}
