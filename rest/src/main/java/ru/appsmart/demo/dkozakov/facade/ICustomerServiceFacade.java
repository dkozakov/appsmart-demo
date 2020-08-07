package ru.appsmart.demo.dkozakov.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.appsmart.demo.dkozakov.core.model.Customer;

import java.util.Optional;

/**
 * Customer facade service. It hides repositories implementation layer.
 */
public interface ICustomerServiceFacade {



	/**
	 * Find customer by identifier
	 *
	 * @param customerId
	 * @return
	 */
	Optional<Customer> findCustomer(String customerId);

	/**
	 * Get pageable list of customers
	 *
	 * @param pageable
	 * @return
	 */
	Page<Customer> findAllCustomers(Pageable pageable);

	/**
	 * Create customer
	 *
	 * @param customer
	 * @return
	 */
	Customer createCustomer(Customer customer);


	/**
	 * Update customer
	 *
	 * @param customer
	 * @return
	 */
	Optional<Customer> updateCustomer(String customerId, Customer customer);


	/**
	 * Mark customer as deleted
	 *
	 * @param customerId
	 * @return true if customer was marked as deleted
	 */
	boolean deleteCustomer(String customerId);
}
