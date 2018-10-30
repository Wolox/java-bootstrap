package wolox.bootstrap;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@SpringBootApplication
public class BootstrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootstrapApplication.class, args);
	}

	/*Con el método localeResolver le indicamos a Spring que vamos a definir el idioma de las respuestas del webservice,
	utilizando cabeceras en nuestros requests al mismo.
	Como idioma predeterminado utilizaremos el inglés americano (Locale.US)*/

	@Bean
	public AcceptHeaderLocaleResolver localeResolver(){

		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();

		localeResolver.setDefaultLocale(Locale.US);

		return localeResolver;

	}
}
