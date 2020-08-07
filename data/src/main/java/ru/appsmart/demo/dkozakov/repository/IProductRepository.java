package ru.appsmart.demo.dkozakov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;

import java.util.UUID;

/**
 * Repository to work with Product entities
 */
@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, UUID>, IProductMarkAsDeletedRepository {

	/**
	 * Mark customer products as deleted (is_deleted = true)
	 *
	 * @param customerId customer identifier
	 * @return count of updated customer products
	 */
	@Modifying(clearAutomatically = true)
	@Query("UPDATE ProductEntity p SET p.deleted=true WHERE p.customer.id=:customerId")
	int markAsDeletedCustomerProducts(@Param("customerId") UUID customerId);

	/**
	 * Get paginated list of customer products
	 * @param customerId customer identifier
	 * @param pageable
	 *
	 * @return paginated list
	 */
	Page<ProductEntity> findProductEntitiesByCustomerId(UUID customerId, Pageable pageable);

}
