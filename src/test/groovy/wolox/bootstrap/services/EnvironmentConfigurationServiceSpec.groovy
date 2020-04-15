package wolox.bootstrap.services


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import wolox.bootstrap.configuration.AppConfig

@ContextConfiguration(classes = [AppConfig.class])
class EnvironmentConfigurationServiceSpec extends Specification {

    @Autowired
    EnvironmentConfigurationService service;

    def "checking server.port property"() {
        expect:
        service.getProperty("server.port") == "8080"
    }

    def "checking invalid property"() {
        when:
        service.getProperty("non_existent");

        then:
        thrown(Exception)
    }
}
