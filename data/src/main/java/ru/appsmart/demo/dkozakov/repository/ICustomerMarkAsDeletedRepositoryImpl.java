package ru.appsmart.demo.dkozakov.repository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ICustomerMarkAsDeletedRepositoryImpl implements ICustomerMarkAsDeletedRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CustomerEntity markAsDeleted(@NonNull  CustomerEntity customer) {
		customer.setDeleted(true);
		customer.getProducts().stream().forEach(product -> product.setDeleted(true));
		return entityManager.merge(customer);
	}
}
