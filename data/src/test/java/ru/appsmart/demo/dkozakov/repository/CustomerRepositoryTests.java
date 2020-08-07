package ru.appsmart.demo.dkozakov.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRepositoryTests extends BaseRepositoryTests {

	@Autowired
	private ICustomerRepository customerRepository;

	@Test
	@Sql(scripts = "classpath:db/CustomerRepositoryTests/testGetAll.sql")
	public void testGetAll() {
		Pageable page = PageRequest.of(0, 5);
		Page<CustomerEntity> result = customerRepository.findAll(page);
		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		result.get().findFirst().ifPresentOrElse(
				entity -> assertEquals("Customer 1", entity.getTitle()),
				() -> fail());
	}

	@Test
	public void testSave() {
		CustomerEntity customer = new CustomerEntity();
		customer.setTitle("New Customer");
		CustomerEntity result = customerRepository.save(customer);
		assertNotNull(result);
		assertNotNull(result.getCreatedAt());
		assertNotNull(result.getId());
		assertNotNull(customer.getId());
	}

	@Test
	@Sql(scripts = "classpath:db/CustomerRepositoryTests/testGetById.sql")
	public void testGetById() {
		final UUID uuid = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		Optional<CustomerEntity> result = customerRepository.findById(uuid);
		result.ifPresentOrElse(
				entity -> {
					assertEquals("Customer GetById", entity.getTitle());
				},
				() -> fail());
	}

	@Test
	@Sql(scripts = "classpath:db/CustomerRepositoryTests/testUpdate.sql")
	public void testUpdate() {

		final UUID uuid = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		CustomerEntity customerEntity  = customerRepository.getOne(uuid);
		assertEquals("Customer testUpdate", customerEntity.getTitle());
		assertNull(customerEntity.getModifiedAt());

		final String newCustomerTitle = "Customer New Title";
		customerEntity.setTitle(newCustomerTitle);
		CustomerEntity savedCustomerEntity = customerRepository.saveAndFlush(customerEntity);
		assertNotNull(customerEntity.getModifiedAt());
		assertNotNull(savedCustomerEntity.getModifiedAt());
		assertEquals(newCustomerTitle, savedCustomerEntity.getTitle());
		assertEquals(uuid, savedCustomerEntity.getId());
	}

	@Test
	@Sql(scripts = "classpath:db/CustomerRepositoryTests/testDelete.sql")
	public void testDelete() {
		final UUID uuid = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		CustomerEntity customer = customerRepository.getOne(uuid);
		customerRepository.delete(customer);

		Optional<CustomerEntity> result = customerRepository.findById(uuid);
		result.ifPresent(entity -> fail());
	}

	@Test
	@Sql(scripts = "classpath:db/CustomerRepositoryTests/testMarkAsDeleted.sql")
	public void testMarkAsDeleted() {
		final UUID uuid = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		CustomerEntity customer = customerRepository.getOne(uuid);
		CustomerEntity markAsDeletedCustomer = customerRepository.markAsDeleted(customer);

		Optional<CustomerEntity> result = customerRepository.findById(uuid);
		if (result.isEmpty()) {
			fail();
		}
		assertEquals(true, customer.isDeleted());
		assertEquals(true, markAsDeletedCustomer.isDeleted());
	}

	@Test
	@Sql(scripts = "classpath:db/CustomerRepositoryTests/testMarkAsDeletedWithParam.sql")
	public void testMarkAsDeletedWithParam() {
		final UUID uuid = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		int updatedCount = customerRepository.markAsDeleted(uuid);
		assertEquals(1, updatedCount);

		Customer customer = customerRepository.getDeletedOne(uuid.toString());
		assertEquals(true, customer.isDeleted());
	}
}
