package wolox.bootstrap.routes;

import static org.apache.camel.builder.PredicateBuilder.isNull;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    private static final String NAME_TAG = "name";

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .apiContextIdPattern("/*")
                .bindingMode(RestBindingMode.json)
        ;

        rest("hello")
                .get()
                .route()
                .choice()
                    .when(isNull(header(NAME_TAG)))
                        .setProperty(NAME_TAG, constant("world"))
                    .otherwise()
                        .setProperty(NAME_TAG, header(NAME_TAG))
                .end()
                .setBody(simple(String.format("Hello, ${property.%s}!", NAME_TAG)))
        ;

    }
}
