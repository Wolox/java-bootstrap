package wolox.bootstrap.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wolox.bootstrap.services.CustomUserDetailService;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthorizationConfig extends WebSecurityConfigurerAdapter {

    private static final String ROLES_URL = "/api/roles";
    private static final String USERS_URL = "/api/users";
    private static final String ALL_PATTERNS = "/**";

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers(AuthorizationConfig.USERS_URL + AuthorizationConfig.ALL_PATTERNS).permitAll()
                .antMatchers(AuthorizationConfig.ROLES_URL + AuthorizationConfig.ALL_PATTERNS).permitAll()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }


}
