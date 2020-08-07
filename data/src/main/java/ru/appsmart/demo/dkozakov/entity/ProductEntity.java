package ru.appsmart.demo.dkozakov.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(name = "products")
@Where(clause = "is_deleted = false")

public class ProductEntity extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@NonNull
	private CustomerEntity customer;

	@Column(name = "title", length = 255, nullable = false)
	@NonNull
	private String title;

	@Column(name = "description", length = 255)
	private String description;

	@Column(name="price", nullable = false, precision=10, scale=2)
	@NonNull
	private BigDecimal price;
}
