package wolox.bootstrap.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wolox.bootstrap.models.ApplicationUser;
import wolox.bootstrap.repositories.ApplicationUserRepository;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {


    @Autowired
    private ApplicationUserRepository applicationUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username).get();

        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}