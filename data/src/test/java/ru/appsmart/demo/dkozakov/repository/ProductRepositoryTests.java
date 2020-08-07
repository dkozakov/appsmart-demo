package ru.appsmart.demo.dkozakov.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTests extends BaseRepositoryTests {

	@Autowired
	private IProductRepository productRepository;

	@Autowired
	private ICustomerRepository customerRepository;


	@Test
	@Sql(scripts = "classpath:db/ProductRepositoryTests/testFindProductEntitiesByCustomerId.sql")
	public void testFindProductEntitiesByCustomerId() {

		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		Page<ProductEntity> customerProducts = productRepository.findProductEntitiesByCustomerId(customerId, Pageable.unpaged());
		assertEquals(3, customerProducts.getTotalElements());
	}

	@Test
	@Sql(scripts = "classpath:db/ProductRepositoryTests/testMarkAsDeletedCustomerProducts.sql")
	public void testMarkAsDeletedCustomerProducts() {

		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		int updatedCustomerProductCount = productRepository.markAsDeletedCustomerProducts(customerId);
		assertEquals(3, updatedCustomerProductCount);

		Page<ProductEntity> customerProducts = productRepository.findProductEntitiesByCustomerId(customerId, Pageable.unpaged());
		assertEquals(0, customerProducts.getTotalElements());
	}


	@Test
	@Sql(scripts = "classpath:db/ProductRepositoryTests/testSave.sql")
	public void testSave() {
		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");

		CustomerEntity customer = customerRepository.getOne(customerId);

		assertEquals(2, customer.getProducts().size());


		ProductEntity product = new ProductEntity();
		product.setTitle("Product 3");
		product.setPrice(new BigDecimal("3.3"));
		customer.addProduct(product);

		ProductEntity savedProduct = productRepository.save(product);
		assertNotNull(savedProduct);
		assertNotNull(savedProduct.getCreatedAt());
		assertNotNull(savedProduct.getId());
		assertNotNull(savedProduct.getCustomer());
	}

	@Test
	@Sql(scripts = "classpath:db/ProductRepositoryTests/testGetById.sql")
	public void testGetById() {
		final UUID uuid = UUID.fromString("c2d29867-3d0b-d497-9191-18a9d8ee7830");
		Optional<ProductEntity> result = productRepository.findById(uuid);
		result.ifPresentOrElse(
				entity -> {
					assertEquals("Product 3", entity.getTitle());
				},
				() -> fail());
	}

	@Test
	@Sql(scripts = "classpath:db/ProductRepositoryTests/testUpdate.sql")
	public void testUpdate() {

		final UUID uuid = UUID.fromString("c2d29867-3d0b-d497-9191-18a9d8ee7830");
		ProductEntity productEntity  = productRepository.getOne(uuid);
		assertEquals("Product 3", productEntity.getTitle());
		assertNull(productEntity.getModifiedAt());

		final BigDecimal newPrice = new BigDecimal("33.3");
		productEntity.setPrice(newPrice);
		ProductEntity savedProductEntity = productRepository.saveAndFlush(productEntity);
		assertNotNull(productEntity.getModifiedAt());
		assertNotNull(savedProductEntity.getModifiedAt());
		assertEquals(newPrice, savedProductEntity.getPrice());
		assertEquals(uuid, savedProductEntity.getId());
	}

	@Test
	@Sql(scripts = "classpath:db/ProductRepositoryTests/testDelete.sql")
	public void testDelete() {
		final UUID uuid = UUID.fromString("c2d29867-3d0b-d497-9191-18a9d8ee7830");

		ProductEntity product = productRepository.getOne(uuid);
		productRepository.delete(product);

		Optional<ProductEntity> result = productRepository.findById(uuid);
		result.ifPresent(entity -> fail());
	}
}
