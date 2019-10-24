package wolox.bootstrap.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import wolox.bootstrap.models.Foo;

@Repository
public interface FooRepository extends ReactiveMongoRepository<Foo, String> {

}
