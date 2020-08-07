package ru.appsmart.demo.dkozakov.repository;

import ru.appsmart.demo.dkozakov.entity.ProductEntity;

import java.util.UUID;

/**
 * Customer Repository Extension to support "mark as deleted" operations
 */
public interface IProductMarkAsDeletedRepository extends IMarkAsDeletedRepository<ProductEntity, UUID> {
}
