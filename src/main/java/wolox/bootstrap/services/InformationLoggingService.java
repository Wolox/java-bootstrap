package wolox.bootstrap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import wolox.bootstrap.models.Log;
import wolox.bootstrap.repositories.LogRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

@Service
@ComponentScan
public class InformationLoggingService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${log.file.output}")
    private String fileDestination;

    @Autowired
    private LogRepository logRepository;

    public String getFileDestination() {
        return fileDestination;
    }

    public void setFileDestination(final String fileDestination) {
        this.fileDestination = fileDestination;
    }

    private void clearHandlers() throws IOException {
        final Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            logger.removeHandler(handler);
        }
        logger.addHandler(new FileHandler(fileDestination, false));
        logger.addHandler(new ConsoleHandler());
    }

    public void log(final String message) throws IOException {
        clearHandlers();
        logger.info(message);
    }

    public void logAndStoreInDatabase(final String message) throws IOException {
        Log log = new Log();
        log.setDate(LocalDate.now());
        log.setMessage(message);
        logRepository.save(log);
        log(message);
    }

    public Iterable findOldLogsByMessageContaining(final String message) {
        return logRepository.findByMessageContaining(message);
    }

    public Iterable findOldLogsByDateBetween(final LocalDate startDate, final LocalDate finishDate) {
        return logRepository.findByDateBetween(startDate, finishDate);
    }

}
