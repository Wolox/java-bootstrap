package wolox.bootstrap.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

	@CreatedDate
	protected LocalDateTime creationDate;

	@LastModifiedDate
	protected LocalDateTime lastModifiedDate;

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
}
