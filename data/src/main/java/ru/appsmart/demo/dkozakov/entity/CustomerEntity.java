package ru.appsmart.demo.dkozakov.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Where;
import ru.appsmart.demo.dkozakov.core.model.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "customers")
@Where(clause = "is_deleted = false")
@NamedNativeQuery(
		name="CustomerEntity.getDeletedOne",
		query="select id\\:\\:text as id, title, is_deleted, created_at, modified_at from customers where id\\:\\:text=:id and is_deleted=true",
		resultSetMapping="customer-dto"
)
@SqlResultSetMapping(name="customer-dto",
		classes={
				@ConstructorResult(targetClass= Customer.class, columns={
						@ColumnResult(name="id", type=String.class),
						@ColumnResult(name="title", type=String.class),
						@ColumnResult(name="is_deleted", type=Boolean.class),
						@ColumnResult(name="created_at", type= LocalDateTime.class),
						@ColumnResult(name="modified_at", type= LocalDateTime.class)
				})
		}
)
public class CustomerEntity extends BaseEntity {


	@Column(name = "title", length = 255, nullable = false)
	@NonNull
	private String title;


	@OneToMany(cascade = CascadeType.ALL,
			mappedBy = "customer", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ProductEntity> products = new ArrayList<>();



	public void addProduct(ProductEntity product) {
		this.products.add(product);
		product.setCustomer(this);
	}

	public void removeProduct(ProductEntity product) {
		product.setCustomer(null);
		this.products.remove(product);
	}

	public void removeProducts() {
		Iterator<ProductEntity> iterator = this.products.iterator();

		while (iterator.hasNext()) {
			ProductEntity product = iterator.next();

			product.setCustomer(null);
			iterator.remove();
		}
	}
}
