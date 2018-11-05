package wolox.bootstrap.services;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.bootstrap.models.Log;
import wolox.bootstrap.repositories.LogRepository;

@Service
public class InformationLoggingService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LogRepository logRepository;

	public void log(String message) {
		Log log = new Log();
		log.setDate(LocalDate.now());
		log.setMessage(message);
		logRepository.save(log);
		logger.info(message);
	}

	public Iterable findOldLogsByMessageContaining(String message) {
		return logRepository.findByMessageContainingAllIgnoreCase(message);
	}

	public Iterable findOldLogsByDateBetween(LocalDate startDate, LocalDate finishDate) {
		return logRepository.findByDateBetween(startDate, finishDate);
	}

}
