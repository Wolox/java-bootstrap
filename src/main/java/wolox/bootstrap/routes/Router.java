package wolox.bootstrap.routes;

import static org.apache.camel.builder.PredicateBuilder.isNull;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .contextPath("/*")
                .apiContextIdPattern("/*")
                .apiContextPath("api-docs")
                .apiProperty("base.path", "/")
                .apiProperty("api.path", "/")
                .apiProperty("cors", "true")
                .port(8080)
                .apiProperty("host", "")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
        ;

        rest("hello")
                .get()
                .param()
                    .name("name")
                    .required(false)
                    .defaultValue("world")
                .endParam()
                .route()
                    .choice()
                        .when(isNull(header("name")))
                            .setProperty("name", header("name"))
                        .otherwise()
                            .setProperty("name", simple("world"))
                .end()
                .setBody(simple("Hello, ${header.name}!"))
        ;

    }
}
