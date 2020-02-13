package wolox.bootstrap.routes;

import static org.apache.camel.builder.PredicateBuilder.isNull;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.bootstrap.exceptions.MissingParameterException;
import wolox.bootstrap.processors.MomentOfDayProcessor;
import wolox.bootstrap.utils.CamelConstants;
import wolox.bootstrap.utils.Constants;

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

        rest(CamelConstants.GREETING_ENDPOINT)
            .get(CamelConstants.HELLO_ENDPOINT).route().to(CamelConstants.HELLO_ROUTE).endRest()
            .get(CamelConstants.BYE_ENDPOINT).route().to(CamelConstants.BYE_ROUTE).endRest()
        ;


        from(CamelConstants.HELLO_ROUTE)
            .onException(MissingParameterException.class)
                .handled(true)
                .setBody(simple(String.format(Constants.MISSING_PARAM, NAME_TAG)))
            .end()
            .choice()
                .when(isNull(header(NAME_TAG)))
                    .throwException(new MissingParameterException())
                .otherwise()
                    .setProperty(NAME_TAG, header(NAME_TAG))
            .end()
            .process(momentOfDayProcessor)
            .setBody(simple(String.format(Constants.GREETING_MESSAGE, NAME_TAG)))
        ;

        from(CamelConstants.BYE_ROUTE)
            .setBody(simple(Constants.BYE_MESSAGE))
        ;

    }
}
