package com.mflq.datagateway.repository;

import com.mflq.datagateway.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "category", collectionResourceRel = "categorys", exported = true)
public interface CategoryRespository
        extends /* PagingAndSortingRepository<Category, Integer>, */ CrudRepository<Category, Integer> {
}
