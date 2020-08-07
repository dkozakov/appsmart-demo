package ru.appsmart.demo.dkozakov;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.appsmart.demo.dkozakov.auth.ITokenProvider;
import ru.appsmart.demo.dkozakov.auth.TokenAuthorizationFilter;
import ru.appsmart.demo.dkozakov.auth.UnauthorizedEntryPoint;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.core.model.Product;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;

import java.util.UUID;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	@RequiredArgsConstructor(onConstructor = @__(@Autowired))
	@ComponentScan(basePackages = "ru.appsmart.demo.dkozakov.auth.*")
	public static class AppConfiguration extends WebSecurityConfigurerAdapter {

		@Value("${security.get.token.uri:/auth}")
		private String getTokenUri;

		private final ITokenProvider tokenProvider;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.httpBasic().disable()
					.csrf().disable()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, getTokenUri).permitAll()
					.antMatchers(HttpMethod.GET, "/api/v1/customers/**", "/api/v1/products/**").permitAll()
					.anyRequest().authenticated()
					.and()
					.addFilterBefore(new TokenAuthorizationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
					.exceptionHandling()
					.authenticationEntryPoint(new UnauthorizedEntryPoint());
		}
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/v3/api-docs/**",
					"/configuration/ui/**",
					"/swagger-resources/**",
					"/configuration/security/**",
					"/swagger-ui.html",
					"/swagger-ui/**",
					"/webjars/**");
		}


		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("demo").password("{bcrypt}$2y$12$zJIaGXG1LmGQ.t.yAkm.Su48hdeEzXDEo9eRwH7MNV9jqz7TG/6/y").roles("USER");
		}


		@Bean
		public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion) {

			return new OpenAPI()
					.info(new Info()
							.title("sample application API")
							.version(appVersion)
							.description(appDesciption)
							.termsOfService("http://swagger.io/terms/")
							.license(new License().name("Apache 2.0").url("http://springdoc.org")));
		}

		@Bean
		public AuthenticationManager customAuthenticationManager() throws Exception {
			return authenticationManager();
		}

		@Bean
		public ModelMapper modelMapper() {
			ModelMapper mapper = new ModelMapper();
			mapper.createTypeMap(CustomerEntity.class, Customer.class).setProvider(
					request -> {
						CustomerEntity entity = CustomerEntity.class.cast(request.getSource());
						return new Customer(entity.getId().toString(), entity.getTitle(), entity.isDeleted(), entity.getCreatedAt(), entity.getModifiedAt());
					}
			);
			mapper.createTypeMap(ProductEntity.class, Product.class).setProvider(
					request -> {
						ProductEntity entity = ProductEntity.class.cast(request.getSource());
						return new Product(entity.getId().toString(), entity.getCustomer().getId().toString(), entity.getTitle(), entity.getDescription(), entity.getPrice(), entity.isDeleted(), entity.getCreatedAt(), entity.getModifiedAt());
					}
			);
			mapper.createTypeMap(Customer.class, CustomerEntity.class).setProvider(
					request -> {
						Customer customer = Customer.class.cast(request.getSource());
						CustomerEntity result = new CustomerEntity();
						String uuid = customer.getId();
						if (null != uuid) {
							result.setId(UUID.fromString(uuid));
						}
						result.setTitle(customer.getTitle());
						result.setDeleted(customer.isDeleted());
						result.setCreatedAt(customer.getCreatedAt());
						result.setModifiedAt(customer.getModifiedAt());

						return result;
					}
			);
			mapper.createTypeMap(Product.class, ProductEntity.class).setProvider(
					request -> {
						Product product = Product.class.cast(request.getSource());
						ProductEntity result = new ProductEntity();
						String uuid = product.getId();
						if (null != uuid) {
							result.setId(UUID.fromString(uuid));
						}
						result.setTitle(product.getTitle());
						result.setDescription(product.getDescription());
						result.setPrice(product.getPrice());
						result.setDeleted(product.isDeleted());
						result.setCreatedAt(product.getCreatedAt());
						result.setModifiedAt(product.getModifiedAt());
						return result;
					}
			);
			return mapper;
		}
	}
}
