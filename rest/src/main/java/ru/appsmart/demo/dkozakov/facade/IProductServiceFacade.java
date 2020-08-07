package ru.appsmart.demo.dkozakov.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.core.model.Product;

import java.util.Optional;

/**
 * Product facade service. It hides repositories implementation layer.
 */
public interface IProductServiceFacade {



	/**
	 * Find product by identifier
	 *
	 * @param productId
	 * @return
	 */
	Optional<Product> findProduct(String productId);

	/**
	 * Get pageable list of customer products
	 *
	 * @param customerId customer identifier
	 * @param pageable
	 * @return
	 */
	Page<Product> findCustomerProducts(String customerId, Pageable pageable);

	/**
	 * Create customer product
	 *
	 * @param customerId customer identifier
	 * @param product
	 * @return
	 */
	Optional<Product> createProduct(String customerId, Product product);


	/**
	 * Update product
	 *
	 * @param productId product identifier
	 * @param product
	 * @return
	 */
	Optional<Product> updateProduct(String productId, Product product);


	/**
	 * Mark product as deleted
	 *
	 * @param productId
	 * @return true if product was marked as deleted
	 */
	boolean deleteProduct(String productId);
}
