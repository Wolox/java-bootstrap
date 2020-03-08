package wolox.bootstrap.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import wolox.bootstrap.models.WeatherResponse;

@Component
public class WeatherAdviserProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String advice = Strings.EMPTY;

        final WeatherResponse weatherResponse = exchange.getIn().getBody(WeatherResponse.class);

        final long feelsLike = weatherResponse.getCurrentWeather().getFeelsLike();
        final boolean isSunny =
                weatherResponse.getCurrentWeather().getWeatherDescriptions().contains("Sunny");
        final boolean isRainy =
                weatherResponse.getCurrentWeather().getWeatherDescriptions().contains("Rainy") ||
                weatherResponse.getCurrentWeather().getPrecipitations() > 0;

        if (feelsLike < 15) {
            advice += "It's cold out there, better bring a jacket! ";
        } else if (feelsLike <= 20) {
            advice += "It's nice outside, but maybe bring a light sweater just in case. ";
        } else {
            advice += "It's really hot, don't forget your bottle of water" +
                    (isSunny ? "and wear sunscreen! " : "! ");
        }

        if (isRainy) {
            advice += "It's raining so you should probably bring an umbrella too!";
        }

        exchange.getOut().setBody(advice);

    }
}
