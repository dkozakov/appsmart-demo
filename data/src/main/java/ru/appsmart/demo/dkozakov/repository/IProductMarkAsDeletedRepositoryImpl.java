package ru.appsmart.demo.dkozakov.repository;

import org.springframework.stereotype.Repository;
import ru.appsmart.demo.dkozakov.entity.ProductEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class IProductMarkAsDeletedRepositoryImpl implements IProductMarkAsDeletedRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ProductEntity markAsDeleted(ProductEntity product) {
		product.setDeleted(true);
		return entityManager.merge(product);
	}
}
