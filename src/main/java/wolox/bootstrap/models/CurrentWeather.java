package wolox.bootstrap.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentWeather {

    @JsonProperty(value = "weather_descriptions")
    private List weatherDescriptions;

    @JsonProperty(value = "precip")
    private long precipitations;

    @JsonProperty(value = "feelslike")
    private long feelsLike;

    public CurrentWeather() {
    }

    public List getWeatherDescriptions() {
        return weatherDescriptions;
    }

    public void setWeatherDescriptions(List weatherDescriptions) {
        this.weatherDescriptions = weatherDescriptions;
    }

    public long getPrecipitations() {
        return precipitations;
    }

    public void setPrecipitations(long precipitations) {
        this.precipitations = precipitations;
    }

    public long getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(long feelsLike) {
        this.feelsLike = feelsLike;
    }
}
