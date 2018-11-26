package wolox.bootstrap.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import wolox.bootstrap.configuration.AppConfig;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class EnvironmentConfigurationServiceTest {

	@Autowired
	EnvironmentConfigurationService service;

	@Test
	public void whenGetPort_thenReturnPort() {
		assert (service.getProperty("server.port").equals("8080"));
	}

	@Test(expected = Exception.class)
	public void whenGetInexistentProperty_thenThrowException() {
		service.getProperty("non_existent");
	}

}