package wolox.bootstrap.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import wolox.bootstrap.configuration.AppConfig;

@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class EnvironmentConfigurationServiceTest {

    @Autowired
    EnvironmentConfigurationService service;

    @Test
    public void whenGetInexistentProperty_thenThrowException() {
        Assertions.assertThrows(Exception.class, () -> service.getProperty("non_existent"));
    }

}
