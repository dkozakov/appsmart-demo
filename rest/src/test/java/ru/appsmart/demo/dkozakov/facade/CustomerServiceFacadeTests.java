package ru.appsmart.demo.dkozakov.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.appsmart.demo.dkozakov.BaseTests;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.facade.impl.CustomerServiceFacade;
import ru.appsmart.demo.dkozakov.service.ICustomerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceFacadeTests extends BaseTests {
	@Mock
	private ICustomerService customerService;

	@Autowired
	private ModelMapper mapper;

	private CustomerServiceFacade customerServiceFacade;

	@BeforeEach
	public void setUp() {
		this.customerServiceFacade = new CustomerServiceFacade(customerService, mapper);
	}

	@Test
	public void testFindAllCustomers() {

		Pageable pageable = Pageable.unpaged();


		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final CustomerEntity customerEntity = new CustomerEntity();
		final String customerTitle = "Customer 1";
		final LocalDateTime createdTime = LocalDateTime.now();
		customerEntity.setId(customerId);
		customerEntity.setTitle(customerTitle);
		customerEntity.setCreatedAt(createdTime);


		List<CustomerEntity> content = List.of(customerEntity);
		Page<CustomerEntity> page = new PageImpl<>(content, pageable, 1);

		when(customerService.findAllCustomers(pageable)).thenReturn(page);

		Page<Customer> result = this.customerServiceFacade.findAllCustomers(pageable);
		assertEquals(1, result.getTotalElements());
	}

}
