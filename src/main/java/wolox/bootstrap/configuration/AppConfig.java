package wolox.bootstrap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wolox.bootstrap.services.EnvironmentConfigurationService;

@Configuration
public class AppConfig {

    @Bean
    public EnvironmentConfigurationService environmentConfigurationService() {
        return new EnvironmentConfigurationService();
    }

}
