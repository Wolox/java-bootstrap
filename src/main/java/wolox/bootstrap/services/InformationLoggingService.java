package wolox.bootstrap.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.bootstrap.models.Log;
import wolox.bootstrap.repositories.LogRepository;

@Service
public class InformationLoggingService {

	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private String fileDestination = "application.log";

	@Autowired
	private LogRepository logRepository;

	public String getFileDestination() {
		return fileDestination;
	}

	public void setFileDestination(String fileDestination) {
		this.fileDestination = fileDestination;
		try {
			Handler[] handlers = this.logger.getHandlers();
			for (int i = 0; i < handlers.length; i++) {
				this.logger.removeHandler(handlers[i]);
			}
			this.logger.setUseParentHandlers(false);
			this.logger.addHandler(new FileHandler(fileDestination));
			this.logger.addHandler(new ConsoleHandler());
		} catch (IOException ex) {
		}
	}

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
