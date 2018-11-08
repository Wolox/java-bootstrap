package wolox.bootstrap.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@ComponentScan
@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(SpringRunner.class)
public class InformationLoggingServiceTest {

	@Autowired
	InformationLoggingService service;

	@Autowired
	EnvironmentConfigurationService config;

	@Test
	public void testService() throws IOException {
		service.log("Test_Works");
		String line = null;
		FileReader fileReader = new FileReader(config.getProperty("logging.file"));
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int count = 0;
		while ((line = bufferedReader.readLine()) != null && count <= 1) {
			if (line.contains("Test_Works")) {
				count++;
			}
		}
		bufferedReader.close();
		assert (count == 1);
	}

}