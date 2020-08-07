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
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.facade.ICustomerServiceFacade;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/customers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Customers")
@SecurityScheme(name = "jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class CustomerController {

	private final ICustomerServiceFacade customerServiceFacade;

	@GetMapping("")
	@Operation(summary = "Returns paginated list of all customers")
	public Page<Customer> findAllCustomers(@PageableDefault(size = 20)
	                                       @SortDefault.SortDefaults({
			                                       @SortDefault(sort = "title", direction = Sort.Direction.ASC),
			                                       @SortDefault(sort = "id", direction = Sort.Direction.ASC)
	                                       }) Pageable pageable) {

		return customerServiceFacade.findAllCustomers(pageable);
	}


	@PostMapping("")
	@Operation(summary = "Create new customer")
	@SecurityRequirement(name = "jwt")
	public Customer createCustomer(@RequestBody Customer customer) {

		return customerServiceFacade.createCustomer(customer);
	}

	@PutMapping("/{customerId}")
	@Operation(summary = "Edit customer")
	@SecurityRequirement(name = "jwt")
	public Customer updateCustomer(@PathVariable("customerId") String customerId, @RequestBody Customer customer) {

		Optional<Customer> updateCustomerResult = customerServiceFacade.updateCustomer(customerId, customer);
		if (updateCustomerResult.isEmpty()) {
			throw new CustomerNotFoundException(customerId);
		}

		return updateCustomerResult.get();
	}

	@DeleteMapping("/{customerId}")
	@Operation(summary = "Delete customer")
	@SecurityRequirement(name = "jwt")
	public void deleteCustomer(@PathVariable("customerId") String customerId) {

		boolean deleteCustomerResult = customerServiceFacade.deleteCustomer(customerId);
		if (!deleteCustomerResult) {
			throw new CustomerNotFoundException(customerId);
		}
	}

	@GetMapping("/{customerId}")
	@Operation(summary = "Returns customer by id")
	public Customer findCustomer(@PathVariable("customerId") String customerId) {

		Optional<Customer> findCustomerResult = customerServiceFacade.findCustomer(customerId);
		if (findCustomerResult.isEmpty()) {
			throw new CustomerNotFoundException(customerId);
		}
		return findCustomerResult.get();
	}


}
