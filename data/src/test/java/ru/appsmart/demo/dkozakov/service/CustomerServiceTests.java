package ru.appsmart.demo.dkozakov.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.repository.ICustomerRepository;
import ru.appsmart.demo.dkozakov.service.impl.CustomerService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class CustomerServiceTests extends BaseServiceTests {
	@Mock
	private ICustomerRepository customerRepository;
	@InjectMocks
	private CustomerService customerService;


	@Test
	public void testFindCustomerById() {
		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setId(customerId);

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

		Optional<CustomerEntity> result = this.customerService.findCustomerById(customerId);
		assertEquals(customerEntity, result.get());
	}


	@Test
	public void testFindAllCustomers() {
		final Pageable pageable = Pageable.unpaged();
		final Page page = Page.empty();

		when(customerRepository.findAll(pageable)).thenReturn(page);

		Page result = this.customerService.findAllCustomers(pageable);
		assertEquals(page, result);
	}

	@Test
	public void testCreateCustomer() {
		final String customerTitle = "Customer 1";
		final LocalDateTime createdTime = LocalDateTime.now();
		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		final CustomerEntity customer = new CustomerEntity();
		customer.setTitle(customerTitle);

		when(customerRepository.save(customer)).then(new Answer<CustomerEntity>() {
			@Override
			public CustomerEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
				CustomerEntity customerParameter = (CustomerEntity) invocationOnMock.getArgument(0);
				customerParameter.setId(customerId);
				customerParameter.setCreatedAt(createdTime);
				return customerParameter;
			}
		});
		CustomerEntity result = this.customerService.createCustomer(customer);
		assertEquals(customerId, result.getId());
	}

	@Test
	public void testUpdateCustomer() {
		final String customerTitle = "Customer 1";
		final LocalDateTime createdTime = LocalDateTime.now();
		final LocalDateTime modifiedTime = LocalDateTime.now();
		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		final CustomerEntity customer = new CustomerEntity();
		customer.setId(customerId);
		customer.setTitle(customerTitle);
		customer.setCreatedAt(createdTime);

		when(customerRepository.save(customer)).then(new Answer<CustomerEntity>() {
			@Override
			public CustomerEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
				CustomerEntity customerParameter = (CustomerEntity) invocationOnMock.getArgument(0);
				customerParameter.setModifiedAt(modifiedTime);
				return customerParameter;
			}
		});
		CustomerEntity result = this.customerService.updateCustomer(customer);
		assertEquals(modifiedTime, result.getModifiedAt());
	}

	@Test
	public void testDeleteCustomer() {
		final String customerTitle = "Customer 1";
		final LocalDateTime createdTime = LocalDateTime.now();
		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		final CustomerEntity customer = new CustomerEntity();
		customer.setId(customerId);
		customer.setTitle(customerTitle);
		customer.setCreatedAt(createdTime);

		when(customerRepository.markAsDeleted(customer)).then(new Answer<CustomerEntity>() {
			@Override
			public CustomerEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
				CustomerEntity customerParameter = (CustomerEntity) invocationOnMock.getArgument(0);
				customerParameter.setDeleted(true);
				return customerParameter;
			}
		});
		boolean result = this.customerService.deleteCustomer(customer);
		assertTrue(result);
	}
}
