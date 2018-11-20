package wolox.bootstrap.repositories;

import java.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import wolox.bootstrap.models.Log;

public interface LogRepository extends CrudRepository<Log, Integer> {

    Iterable<Log> findByDateBetween(LocalDate startDate, LocalDate finishDate);

    Iterable<Log> findByMessageContaining(String message);

}