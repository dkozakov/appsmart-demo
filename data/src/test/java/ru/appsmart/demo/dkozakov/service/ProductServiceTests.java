package ru.appsmart.demo.dkozakov.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;
import ru.appsmart.demo.dkozakov.repository.IProductRepository;
import ru.appsmart.demo.dkozakov.service.impl.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ProductServiceTests extends BaseServiceTests {

	@Mock
	private IProductRepository productRepository;
	@InjectMocks
	private ProductService productService;

	@Test
	public void testFindProductById() {
		final UUID productId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final ProductEntity product = new ProductEntity();
		product.setId(productId);

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		Optional<ProductEntity> result = this.productService.findProductById(productId);
		assertEquals(product, result.get());
	}

	@Test
	public void testFindCustomerProducts() {
		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final Pageable pageable = Pageable.unpaged();
		final Page page = Page.empty();

		when(productRepository.findProductEntitiesByCustomerId(customerId, pageable)).thenReturn(page);

		Page result = this.productService.findCustomerProducts(customerId, pageable);
		assertEquals(page, result);
	}


	@Test
	public void testCreateProduct() {
		final LocalDateTime createdTime = LocalDateTime.now();
		final UUID productId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final BigDecimal productPrice = new BigDecimal("3.3");
		final String productTitle = "Product 1";

		final ProductEntity product = new ProductEntity();
		product.setPrice(productPrice);
		product.setTitle(productTitle);
		product.setCustomer(new CustomerEntity());

		when(productRepository.save(product)).then(new Answer<ProductEntity>() {
			@Override
			public ProductEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
				ProductEntity productParameter = (ProductEntity) invocationOnMock.getArgument(0);
				productParameter.setId(productId);
				productParameter.setCreatedAt(createdTime);
				return productParameter;
			}
		});
		ProductEntity result = this.productService.createProduct(product);
		assertEquals(productId, result.getId());
	}

	@Test
	public void testUpdateProduct() {
		final LocalDateTime modifiedTime = LocalDateTime.now();
		final UUID productId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final BigDecimal productPrice = new BigDecimal("3.3");
		final String productTitle = "Product 1";

		final ProductEntity product = new ProductEntity();
		product.setPrice(productPrice);
		product.setTitle(productTitle);
		product.setId(productId);
		product.setCustomer(new CustomerEntity());

		when(productRepository.save(product)).then(new Answer<ProductEntity>() {
			@Override
			public ProductEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
				ProductEntity productParameter = (ProductEntity) invocationOnMock.getArgument(0);
				productParameter.setModifiedAt(modifiedTime);
				return productParameter;
			}
		});
		ProductEntity result = this.productService.updateProduct(product);
		assertEquals(modifiedTime, result.getModifiedAt());
	}

	@Test
	public void testDeleteProduct() {
		final UUID productId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final BigDecimal productPrice = new BigDecimal("3.3");
		final String productTitle = "Product 1";

		final ProductEntity product = new ProductEntity();
		product.setPrice(productPrice);
		product.setTitle(productTitle);
		product.setId(productId);
		product.setCustomer(new CustomerEntity());

		when(productRepository.markAsDeleted(product)).then(new Answer<ProductEntity>() {
			@Override
			public ProductEntity answer(InvocationOnMock invocationOnMock) throws Throwable {
				ProductEntity productParameter = (ProductEntity) invocationOnMock.getArgument(0);
				productParameter.setDeleted(true);
				return productParameter;
			}
		});
		boolean result = this.productService.deleteProduct(product);
		assertTrue(result);
	}
}
