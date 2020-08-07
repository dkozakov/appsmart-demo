package ru.appsmart.demo.dkozakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.appsmart.demo.dkozakov.core.model.Customer;
import ru.appsmart.demo.dkozakov.entity.CustomerEntity;

import java.util.UUID;

/**
 * Repository to work with Customer entity
 */
@Repository
public interface ICustomerRepository extends JpaRepository<CustomerEntity, UUID>, ICustomerMarkAsDeletedRepository {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE CustomerEntity SET deleted=true WHERE id=:id")
	int markAsDeleted(@Param("id") UUID id);

	@Query(name = "CustomerEntity.getDeletedOne", nativeQuery = true)
	Customer getDeletedOne(@Param("id") String id);
}
