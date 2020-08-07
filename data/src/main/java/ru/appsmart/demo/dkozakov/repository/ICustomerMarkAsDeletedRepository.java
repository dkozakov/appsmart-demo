package ru.appsmart.demo.dkozakov.repository;

import ru.appsmart.demo.dkozakov.entity.CustomerEntity;

import java.util.UUID;

/**
 * Customer Repository Extension to support "mark as deleted" operations
 */
public interface ICustomerMarkAsDeletedRepository extends IMarkAsDeletedRepository<CustomerEntity, UUID> {
}
