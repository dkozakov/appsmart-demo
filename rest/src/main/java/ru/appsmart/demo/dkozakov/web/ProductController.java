package ru.appsmart.demo.dkozakov.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import ru.appsmart.demo.dkozakov.core.model.Product;
import ru.appsmart.demo.dkozakov.facade.IProductServiceFacade;

import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Products")
@SecurityScheme(name = "jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class ProductController {

	private final IProductServiceFacade productServiceFacade;

	@GetMapping("/api/v1/customers/{customerId}/products")
	@Operation(summary = "Returns paginated list of all customers")
	public Page<Product> findCustomerProducts(@PathVariable("customerId") String customerId, @PageableDefault(size = 20)
	                                       @SortDefault.SortDefaults({
			                                       @SortDefault(sort = "title", direction = Sort.Direction.ASC),
			                                       @SortDefault(sort = "id", direction = Sort.Direction.ASC)
	                                       }) Pageable pageable) {

		return productServiceFacade.findCustomerProducts(customerId, pageable);
	}


	@PostMapping("/api/v1/customers/{customerId}/products")
	@Operation(summary = "Create new product for customer")
	@SecurityRequirement(name = "jwt")
	public Product createProduct(@PathVariable("customerId") String customerId, @RequestBody Product product) {
		Optional<Product> createProductResult = productServiceFacade.createProduct(customerId, product);
		if (createProductResult.isEmpty()) {
			throw new CustomerNotFoundException(customerId);
		}

		return createProductResult.get();
	}

	@PutMapping("/products/{productId}")
	@Operation(summary = "Edit product")
	@SecurityRequirement(name = "jwt")
	public Product updateProduct(@PathVariable("productId") String productId, @RequestBody Product product) {

		Optional<Product> updateProductResult = productServiceFacade.updateProduct(productId, product);
		if (updateProductResult.isEmpty()) {
			throw new ProductNotFoundException(productId);
		}

		return updateProductResult.get();
	}

	@DeleteMapping("/products/{productId}")
	@Operation(summary = "Delete product")
	@SecurityRequirement(name = "jwt")
	public void deleteProduct(@PathVariable("productId") String productId) {

		boolean deleteзProductResult = productServiceFacade.deleteProduct(productId);
		if (!deleteзProductResult) {
			throw new ProductNotFoundException(productId);
		}
	}

	@GetMapping("/products/{productId}")
	@Operation(summary = "Returns product by id")
	public Product findProduct(@PathVariable("productId") String productId) {

		Optional<Product> findProductResult = productServiceFacade.findProduct(productId);
		if (findProductResult.isEmpty()) {
			throw new ProductNotFoundException(productId);
		}
		return findProductResult.get();
	}


}
