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
import org.springframework.security.core.parameters.P;
import ru.appsmart.demo.dkozakov.BaseTests;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.core.model.Product;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;
import ru.appsmart.demo.dkozakov.facade.impl.ProductServiceFacade;
import ru.appsmart.demo.dkozakov.service.ICustomerService;
import ru.appsmart.demo.dkozakov.service.IProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceFacadeTests  extends BaseTests {
	@Mock
	private IProductService productService;
	@Mock
	private ICustomerService customerService;

	@Autowired
	private ModelMapper mapper;

	private ProductServiceFacade productServiceFacade;

	@BeforeEach
	public void setUp() {
		this.productServiceFacade = new ProductServiceFacade(productService, customerService, mapper);
	}

	@Test
	public void testFindAllCustomers() {

		final UUID customerId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setId(customerId);
		customerEntity.setTitle("Customer 1");
		customerEntity.setCreatedAt(LocalDateTime.now());
		customerEntity.setModifiedAt(LocalDateTime.now());

		final UUID productId = UUID.fromString("b19d597c-8f54-41ba-ba73-02299c1adf92");
		final BigDecimal productPrice = new BigDecimal("3.3");
		final String productTitle = "Product 1";

		final ProductEntity product = new ProductEntity();
		product.setPrice(productPrice);
		product.setTitle(productTitle);
		product.setId(productId);
		customerEntity.addProduct(product);

		final Pageable pageable = Pageable.unpaged();
		final Page page = new PageImpl(List.of(product), pageable, 1);

		when(productService.findCustomerProducts(customerId, pageable)).thenReturn(page);

		Page<Product> result = this.productServiceFacade.findCustomerProducts(customerId.toString(), pageable);
		assertEquals(1, result.getTotalElements());
		Product resultProduct = result.getContent().stream().findFirst().get();
		assertEquals(productPrice, resultProduct.getPrice());
		assertEquals(productId.toString(), resultProduct.getId());
		assertEquals(customerId.toString(), resultProduct.getCustomerId());
		assertEquals(productTitle, resultProduct.getTitle());

	}

}
