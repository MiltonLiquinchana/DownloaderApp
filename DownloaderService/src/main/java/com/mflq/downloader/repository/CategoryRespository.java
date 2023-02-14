package com.mflq.downloader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mflq.downloader.model.Category;

@RepositoryRestResource(path = "category", collectionResourceRel = "categorys", exported = true)
public interface CategoryRespository
		extends PagingAndSortingRepository<Category, Integer>, CrudRepository<Category, Integer> {

}
