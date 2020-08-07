package ru.appsmart.demo.dkozakov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;
import ru.appsmart.demo.dkozakov.repository.IProductRepository;
import ru.appsmart.demo.dkozakov.service.IProductService;

import java.util.Optional;
import java.util.UUID;

/**
 * Default implementation of Product Service
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService implements IProductService {

	private final IProductRepository productRepository;

	@Override
	public Optional<ProductEntity> findProductById(UUID productId) {
		return productRepository.findById(productId);
	}

	@Override
	public Page<ProductEntity> findCustomerProducts(UUID customerId, Pageable pageable) {
		return productRepository.findProductEntitiesByCustomerId(customerId, pageable);
	}

	@Override
	@Transactional
	public ProductEntity createProduct(ProductEntity product) {
		return productRepository.save(product);
	}

	@Override
	@Transactional
	public ProductEntity updateProduct(ProductEntity product) {
		return productRepository.save(product);
	}

	@Override
	@Transactional
	public boolean deleteProduct(ProductEntity product) {
		ProductEntity updatedProduct = productRepository.markAsDeleted(product);
		return updatedProduct.isDeleted();
	}
}
