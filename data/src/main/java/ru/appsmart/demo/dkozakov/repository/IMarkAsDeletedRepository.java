package ru.appsmart.demo.dkozakov.repository;

/**
 * Custom repository to support "mark as deleted" operation
 *
 * @param <T> entity class
 * @param <ID> identifier class
 */
public interface IMarkAsDeletedRepository<T, ID> {

	T markAsDeleted(T entity);
}
