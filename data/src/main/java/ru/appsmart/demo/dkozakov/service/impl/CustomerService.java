package ru.appsmart.demo.dkozakov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.repository.ICustomerRepository;
import ru.appsmart.demo.dkozakov.service.ICustomerService;

import java.util.Optional;
import java.util.UUID;

/**
 * Default Customer Service implementation
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService implements ICustomerService {

	private final ICustomerRepository customerRepository;

	@Override
	public Optional<CustomerEntity> findCustomerById(UUID customerId) {
		return this.customerRepository.findById(customerId);
	}

	@Override
	public Page<CustomerEntity> findAllCustomers(Pageable pageable) {
		return this.customerRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public CustomerEntity createCustomer(CustomerEntity customer) {
		return this.customerRepository.save(customer);
	}

	@Override
	@Transactional
	public CustomerEntity updateCustomer(CustomerEntity customer) {
		return this.customerRepository.save(customer);
	}

	@Override
	@Transactional
	public boolean deleteCustomer(CustomerEntity customer) {
		CustomerEntity customerEntity = this.customerRepository.markAsDeleted(customer);
		return customerEntity.isDeleted();
	}
}
