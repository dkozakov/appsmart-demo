package ru.appsmart.demo.dkozakov.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Customer Service
 */
public interface ICustomerService {

	/**
	 * Find customer by identifier
	 *
	 * @param customerId the customer identifier
	 * @return customer
	 */
	Optional<CustomerEntity> findCustomerById(UUID customerId);

	/**
	 * Get paginated list of all customers
	 *
	 * @param pageable
	 * @return
	 */
	Page<CustomerEntity> findAllCustomers(Pageable pageable);

	/**
	 * Create customer
	 *
	 * @param customer a  new creating customer
	 * @return created customer entity
	 */
	CustomerEntity createCustomer(CustomerEntity customer);

	/**
	 * Update customer
	 *
	 * @param customer an updating customer
	 * @return updated customer entity
	 */
	CustomerEntity updateCustomer(CustomerEntity customer);

	/**
	 * Delete customer. The implementation does mark an customer entity and all its product entities
	 * as deleted and does not delete entities from the database.
	 *
	 * @param customer a deleting customer
	 * @return result of the  operation
	 */
	boolean deleteCustomer(CustomerEntity customer);
}
