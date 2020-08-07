package ru.appsmart.demo.dkozakov.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Product service
 */
public interface IProductService {
	/**
	 * Find product by identifier
	 *
	 * @param productId the customer identifier
	 * @return product
	 */
	Optional<ProductEntity> findProductById(UUID productId);


	/**
	 * Get paginated list of customer's products
	 *
	 * @param customerId the customer identifier
	 * @param pageable
	 * @return customer
	 */
	Page<ProductEntity> findCustomerProducts(UUID customerId, Pageable pageable);

	/**
	 * Create product
	 *
	 * @param product a  new creating product
	 * @return created product entity
	 */
	ProductEntity createProduct(ProductEntity product);

	/**
	 * Update product
	 *
	 * @param product an updating product
	 * @return updated product entity
	 */
	ProductEntity updateProduct(ProductEntity product);

	/**
	 * Delete product. The implementation does mark an product entity
	 * as deleted and does not delete an entity from the database.
	 *
	 * @param product a deleting product
	 * @return result of the  operation
	 */
	boolean deleteProduct(ProductEntity product);
}
