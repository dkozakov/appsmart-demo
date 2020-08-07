package ru.appsmart.demo.dkozakov.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public abstract class BaseEntity {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name="id", columnDefinition = "uuid", updatable = false)
	private UUID id;

	@CreatedDate
	@Column(name="created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name="modified_at")
	private LocalDateTime modifiedAt;

	@Column(name="is_deleted", nullable = false)
	private boolean deleted;

	@SuppressWarnings("unused")
	@PrePersist
	private void onInsert() {
		this.createdAt = LocalDateTime.now();
	}

	@SuppressWarnings("unused")
	@PreUpdate
	private void onUpdate() {
		this.modifiedAt = LocalDateTime.now();
	}
}
