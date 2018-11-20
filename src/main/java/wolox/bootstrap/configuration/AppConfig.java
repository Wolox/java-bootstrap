package wolox.bootstrap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import wolox.bootstrap.services.EnvironmentConfigurationService;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public EnvironmentConfigurationService environmentConfigurationService() {
        return new EnvironmentConfigurationService();
    }

}
