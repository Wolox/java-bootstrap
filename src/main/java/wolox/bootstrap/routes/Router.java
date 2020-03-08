package wolox.bootstrap.routes;

import static org.apache.camel.builder.PredicateBuilder.isNull;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wolox.bootstrap.exceptions.MissingParameterException;
import wolox.bootstrap.models.WeatherResponse;
import wolox.bootstrap.processors.MomentOfDayProcessor;
import wolox.bootstrap.processors.WeatherAdviserProcessor;
import wolox.bootstrap.utils.CamelConstants;
import wolox.bootstrap.utils.Constants;

@Component
public class Router extends RouteBuilder {

    private static final String NAME_TAG = "name";
    private static final String CITY_TAG = "city";
    private static final String WEATHER_SERVICE_URL =
            "http://api.weatherstack.com/current?access_key=%s&query=%s&units=m";

    @Value(value = "${api.key}")
    private String apiKey;

    @Autowired
    private MomentOfDayProcessor momentOfDayProcessor;

    @Autowired
    private WeatherAdviserProcessor weatherAdviserProcessor;

    private String generateServiceUrl() {
        return String.format(WEATHER_SERVICE_URL, apiKey, String.format("${property.%s}", CITY_TAG));
    }

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .bindingMode(RestBindingMode.json)
        ;

        rest(CamelConstants.GREETING_ENDPOINT)
            .get(CamelConstants.HELLO_ENDPOINT).route().to(CamelConstants.HELLO_ROUTE).endRest()
            .get(CamelConstants.BYE_ENDPOINT).route().to(CamelConstants.BYE_ROUTE).endRest()
            .get(CamelConstants.WEATHER_ENDPOINT).route().to(CamelConstants.WEATHER_ROUTE).endRest()
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

        from(CamelConstants.WEATHER_ROUTE)
            .setProperty(CITY_TAG, header(CITY_TAG))
            .removeHeaders("*")
            .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
            .toD(generateServiceUrl())
            .unmarshal().json(JsonLibrary.Jackson, WeatherResponse.class)
            .process(weatherAdviserProcessor)
        ;

    }
}
