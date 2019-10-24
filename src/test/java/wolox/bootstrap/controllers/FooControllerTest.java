package wolox.bootstrap.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.thymeleaf.util.StringUtils.randomAlphanumeric;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wolox.bootstrap.DAO.FooDAO;
import wolox.bootstrap.models.Foo;
import wolox.bootstrap.repositories.FooRepository;

@RunWith(SpringRunner.class)
@WebFluxTest(FooController.class)
public class FooControllerTest {

    private final String baseUrl = "/foo/";

    @MockBean
    private FooRepository fooRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void givenValidInput_whenCreateFooIsCalled_thenReturnSuccess() {
        FooDAO fooDAO = new FooDAO();
        fooDAO.setDescription(randomAlphanumeric(10));

        Foo foo = new Foo(fooDAO);

        when(fooRepository.insert(any(Foo.class)))
            .thenReturn(Mono.just(foo));

        webTestClient
            .post()
            .uri(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(fooDAO))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void whenGetAllFooIsCalled_thenReturnList() {
        Foo foo = new Foo(randomAlphanumeric(10));

        List<Foo> foos = Collections.singletonList(foo);

        when(fooRepository.findAll())
            .thenReturn(Flux.fromIterable(foos));

        webTestClient
            .get()
            .uri(baseUrl)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Foo.class).hasSize(1)
            .isEqualTo(foos);


    }


}