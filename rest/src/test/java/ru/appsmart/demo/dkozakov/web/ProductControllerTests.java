package ru.appsmart.demo.dkozakov.web;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.appsmart.demo.dkozakov.facade.IProductServiceFacade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

	@MockBean
	private IProductServiceFacade productServiceFacade;

	@Autowired
	private MockMvc mvc;

	@SneakyThrows
	@Test
	public void testFindCustomerProducts() {

		final String customerId = "b19d597c-8f54-41ba-ba73-02299c1adf92";

		Page page = Page.empty();

		when(productServiceFacade.findCustomerProducts(any(String.class), any(Pageable.class))).thenReturn(page);

		mvc.perform(get("/api/v1/customers/" + customerId + "/products").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<String> parameterCaptor = ArgumentCaptor.forClass(String.class);
		verify(productServiceFacade).findCustomerProducts(parameterCaptor.capture(), pageableCaptor.capture());
		Pageable pageable = pageableCaptor.getValue();
		Sort.Order titleOrder = pageable.getSort().getOrderFor("title");
		assertNotNull(titleOrder);
		assertTrue(titleOrder.isAscending());
		assertEquals(20, pageable.getPageSize());
		assertEquals(customerId, parameterCaptor.getValue());
	}

}
