package ru.appsmart.demo.dkozakov.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import ru.appsmart.demo.dkozakov.auth.ITokenProvider;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.facade.ICustomerServiceFacade;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

	@MockBean
	private ICustomerServiceFacade customerServiceFacade;

	@Autowired
	private ITokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MockMvc mvc;

	@SneakyThrows
	@Test
	public void testFindAllCustomers() {

		Page page = Page.empty();

		when(customerServiceFacade.findAllCustomers(any(Pageable.class))).thenReturn(page);

		mvc.perform(get("/api/v1/customers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(customerServiceFacade).findAllCustomers(pageableCaptor.capture());
		Pageable pageable = pageableCaptor.getValue();
		Sort.Order titleOrder = pageable.getSort().getOrderFor("title");
		assertNotNull(titleOrder);
		assertTrue(titleOrder.isAscending());
		assertEquals(20, pageable.getPageSize());
	}


	@SneakyThrows
	@Test
	public void testCreateCustomer() {

		ObjectMapper objectMapper = new ObjectMapper();

		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("demo", "demo"));
		String token = tokenProvider.generateToken(authenticate);

		final String customerId = "b19d597c-8f54-41ba-ba73-02299c1adf92";
		final String customerTitle = "Customer 1";
		final LocalDateTime createdTime = LocalDateTime.now();
		final Customer customer = new Customer(customerId, customerTitle, false, createdTime, null);

		final String content = objectMapper.writeValueAsString(new Customer(null, customerTitle, false, null, null));

		when(customerServiceFacade.createCustomer(any(Customer.class))).thenReturn(customer);

		mvc.perform(
			post("/api/v1/customers")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
		verify(customerServiceFacade).createCustomer(customerCaptor.capture());
		Customer customerParameter = customerCaptor.getValue();
	}

}
