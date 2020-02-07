package wolox.bootstrap.services;

import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentConfigurationService {

    private static final String PROPERTY_DOES_NOT_EXIST = "The property does not exist.";

    @Autowired
    private ApplicationContext context;

    public String getProperty(final String name) {
        Environment environment = context.getEnvironment();
        Preconditions.checkArgument(environment.containsProperty(name), PROPERTY_DOES_NOT_EXIST);
        return environment.getProperty(name);
    }

}