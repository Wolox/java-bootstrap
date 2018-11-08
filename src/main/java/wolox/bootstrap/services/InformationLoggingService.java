package wolox.bootstrap.services;

import java.time.LocalDate;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.bootstrap.models.Log;
import wolox.bootstrap.repositories.LogRepository;

@Service
public class InformationLoggingService {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private LogRepository logRepository;

	public void log(String message) {
		logger.info(message);
	}

	public void logAndStoreInDatabase(String message) {
		Log log = new Log();
		log.setDate(LocalDate.now());
		log.setMessage(message);
		logRepository.save(log);
		log(message);
	}

	public Iterable findOldLogsByMessageContaining(String message) {
		return logRepository.findByMessageContaining(message);
	}

	public Iterable findOldLogsByDateBetween(LocalDate startDate, LocalDate finishDate) {
		return logRepository.findByDateBetween(startDate, finishDate);
	}

}
