package ru.appsmart.demo.dkozakov.facade.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.facade.ICustomerServiceFacade;
import ru.appsmart.demo.dkozakov.service.ICustomerService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Default implementation of customer facade service
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceFacade implements ICustomerServiceFacade {

	private final ICustomerService customerService;
	private final ModelMapper mapper;

	@Override
	public Optional<Customer> findCustomer(String customerId) {

		if (StringUtils.isEmpty(customerId)) {
			return Optional.empty();
		}
		Optional<CustomerEntity> findCustomerResult = customerService.findCustomerById(UUID.fromString(customerId));
		if (findCustomerResult.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(mapper.map(findCustomerResult.get(), Customer.class));
	}

	@Override
	public Page<Customer> findAllCustomers(Pageable pageable) {
		Page<CustomerEntity> customers = customerService.findAllCustomers(pageable);
		Type listType = new TypeToken<List<Customer>>() {}.getType();

		return new PageImpl<>(mapper.map(customers.getContent(),listType), pageable, customers.getTotalElements());
	}

	@Override
	public Customer createCustomer(Customer customer) {

		CustomerEntity customerEntity = mapper.map(customer, CustomerEntity.class);
		customerEntity.setId(null);
		CustomerEntity createdCustomerEntity = customerService.createCustomer(customerEntity);

		return mapper.map(createdCustomerEntity, Customer.class);
	}

	@Override
	public Optional<Customer> updateCustomer(String customerId, Customer customer) {

		if (StringUtils.isEmpty(customerId) || null == customer) {
			return Optional.empty();
		}

		Optional<CustomerEntity> findCustomerResult = customerService.findCustomerById(UUID.fromString(customerId));
		if (findCustomerResult.isEmpty()) {
			return Optional.empty();
		}
		CustomerEntity customerEntity = findCustomerResult.get();
		customerEntity.setTitle(customer.getTitle());
		CustomerEntity updatedCustomerEntity = customerService.updateCustomer(customerEntity);
		return Optional.of(mapper.map(updatedCustomerEntity, Customer.class));
	}

	@Override
	public boolean deleteCustomer(String customerId) {

		if (StringUtils.isEmpty(customerId)) {
			return false;
		}
		Optional<CustomerEntity> findCustomerResult = customerService.findCustomerById(UUID.fromString(customerId));
		if (findCustomerResult.isEmpty()) {
			return false;
		}

		return customerService.deleteCustomer(findCustomerResult.get());
	}
}
