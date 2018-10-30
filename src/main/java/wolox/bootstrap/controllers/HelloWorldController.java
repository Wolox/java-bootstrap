package wolox.bootstrap.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
  @Autowired

  private MessageSource messageSource;



  @GetMapping("/saludo-idiomas")
  public String saludoIdiomas(){

    return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());

  }
}
