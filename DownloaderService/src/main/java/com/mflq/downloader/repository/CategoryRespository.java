package com.mflq.downloader.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mflq.downloader.model.Category;

@RepositoryRestResource(path = "categorys", collectionResourceRel = "Category")
public interface CategoryRespository extends PagingAndSortingRepository<Category, Integer> {

}
