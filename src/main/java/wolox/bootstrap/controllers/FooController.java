package wolox.bootstrap.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wolox.bootstrap.DAO.FooDAO;
import wolox.bootstrap.models.Foo;
import wolox.bootstrap.repositories.FooRepository;

@RestController
@RequestMapping("/foo")
public class FooController {

    @Autowired
    private FooRepository fooRepository;

    @PostMapping
    public Mono<Void> create(@Valid @RequestBody FooDAO dao) {
        return fooRepository
            .insert(new Foo(dao))
            .then()
            .log();
    }

    @GetMapping
    public Flux<Foo> getAll(@RequestParam(value = "size", defaultValue = "20") Integer size) {
        return fooRepository
            .findAll()
            .take(size)
            .log();
    }
}
