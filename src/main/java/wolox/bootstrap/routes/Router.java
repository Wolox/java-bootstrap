package wolox.bootstrap.routes;

import static org.apache.camel.builder.PredicateBuilder.isNull;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.bootstrap.exceptions.MissingParameterException;
import wolox.bootstrap.processors.MomentOfDayProcessor;

@Component
public class Router extends RouteBuilder {

    private static final String NAME_TAG = "name";

    @Autowired
    private MomentOfDayProcessor momentOfDayProcessor;

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .bindingMode(RestBindingMode.json)
        ;

        rest()
            .get("hello").route().to("direct:say-hi").endRest()
            .get("bye").route().to("direct:say-bye").endRest()
        ;


        from("direct:say-hi")
            .onException(MissingParameterException.class)
                .handled(true)
                .setBody(constant("Expected a query param 'name' that could not be found."))
            .end()
            .choice()
                .when(isNull(header(NAME_TAG)))
                    .throwException(new MissingParameterException())
                .otherwise()
                    .setProperty(NAME_TAG, header(NAME_TAG))
            .end()
            .process(momentOfDayProcessor)
            .setBody(simple(String.format("Good ${header.momentOfDay}, ${property.%s}!", NAME_TAG)))
        ;

        from("direct:say-bye")
            .setBody(simple("Bye, see you soon!"))
        ;

    }
}
