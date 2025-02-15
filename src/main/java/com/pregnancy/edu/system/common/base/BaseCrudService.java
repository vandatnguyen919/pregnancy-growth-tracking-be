package com.pregnancy.edu.system.common.base;

import java.util.List;

public interface BaseCrudService<T, ID> {

    List<T> findAll();

    T findById(ID id);

    T save(T entity);

    T update(ID id, T entity);

    void delete(Long id);
}
