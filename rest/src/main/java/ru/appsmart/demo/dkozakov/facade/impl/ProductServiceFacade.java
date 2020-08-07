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
import ru.appsmart.demo.dkozakov.core.model.Product;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;
import ru.appsmart.demo.dkozakov.facade.IProductServiceFacade;
import ru.appsmart.demo.dkozakov.service.ICustomerService;
import ru.appsmart.demo.dkozakov.service.IProductService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Default implementation of customer facade service
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceFacade implements IProductServiceFacade {

	private final IProductService productService;
	private final ICustomerService customerService;
	private final ModelMapper mapper;

	@Override
	public Optional<Product> findProduct(String productId) {
		if (StringUtils.isEmpty(productId)) {
			return Optional.empty();
		}
		Optional<ProductEntity> findProductResult = productService.findProductById(UUID.fromString(productId));
		if (findProductResult.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(mapper.map(findProductResult.get(), Product.class));
	}

	@Override
	public Page<Product> findCustomerProducts(String customerId, Pageable pageable) {
		if (StringUtils.isEmpty(customerId)) {
			return Page.empty();
		}

		Page<ProductEntity> products = productService.findCustomerProducts(UUID.fromString(customerId), pageable);
		Type listType = new TypeToken<List<Product>>() {}.getType();

		return new PageImpl<>(mapper.map(products.getContent(),listType), pageable, products.getTotalElements());
	}

	@Override
	public Optional<Product> createProduct(String customerId, Product product) {

		if (StringUtils.isEmpty(customerId)) {
			return Optional.empty();
		}

		Optional<CustomerEntity> findCustomerResult = customerService.findCustomerById(UUID.fromString(customerId));
		if (findCustomerResult.isEmpty()) {
			return Optional.empty();
		}

		CustomerEntity customerEntity = findCustomerResult.get();

		ProductEntity productEntity = mapper.map(product, ProductEntity.class);
		productEntity.setId(null);
		customerEntity.addProduct(productEntity);

		ProductEntity createdProductEntity = productService.createProduct(productEntity);

		return Optional.of(mapper.map(createdProductEntity, Product.class));
	}

	@Override
	public Optional<Product> updateProduct(String productId, Product product) {

		if (StringUtils.isEmpty(productId) || null == product) {
			return Optional.empty();
		}

		Optional<ProductEntity> findProductResult = productService.findProductById(UUID.fromString(productId));
		if (findProductResult.isEmpty()) {
			return Optional.empty();
		}
		ProductEntity productEntity = findProductResult.get();
		productEntity.setTitle(product.getTitle());
		productEntity.setDescription(product.getDescription());
		productEntity.setPrice(product.getPrice());
		ProductEntity updatedProductEntity = productService.updateProduct(productEntity);
		return Optional.of(mapper.map(updatedProductEntity, Product.class));
	}

	@Override
	public boolean deleteProduct(String productId) {

		if (StringUtils.isEmpty(productId)) {
			return false;
		}
		Optional<ProductEntity> findProductResult = productService.findProductById(UUID.fromString(productId));
		if (findProductResult.isEmpty()) {
			return false;
		}

		return productService.deleteProduct(findProductResult.get());
	}
}
