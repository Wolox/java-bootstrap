package wolox.bootstrap.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import spock.lang.Specification
import wolox.bootstrap.repositories.LogRepository

@DataJpaTest
@ComponentScan
class InformationLoggingServiceSpec extends Specification {
    @MockBean
    LogRepository logRepository;

    @Autowired
    InformationLoggingService service;


    def "test"() {
        given: "add an entry to the log"
        service.log("Test_Works");
        and: "read the file log"
        String line = null;
        FileReader fileReader = new FileReader(service.getFileDestination());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int count = 0;
        while ((line = bufferedReader.readLine()) != null && count <= 1) {
            if (line.contains("Test_Works")) {
                count++;
            }
        }
        bufferedReader.close();

        expect:
        count == 1
    }
}
