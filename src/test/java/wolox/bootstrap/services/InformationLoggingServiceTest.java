package wolox.bootstrap.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import wolox.bootstrap.repositories.LogRepository;

@DataJpaTest
@ComponentScan
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class InformationLoggingServiceTest {

    @MockBean
    LogRepository logRepository;

    @Autowired
    InformationLoggingService service;

    @Test
    public void testService() throws IOException {
        service.log("Test_Works");
        String line = null;
        FileReader fileReader = new FileReader(service.getFileDestination());
        int count;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            count = 0;
            while ((line = bufferedReader.readLine()) != null && count <= 1) {
                if (line.contains("Test_Works")) {
                    count++;
                }
            }
        }
        assert (count == 1);
    }

}
