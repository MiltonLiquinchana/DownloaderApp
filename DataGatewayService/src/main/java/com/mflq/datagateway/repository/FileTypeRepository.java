package com.mflq.datagateway.repository;

import com.mflq.datagateway.entity.FileType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "filetype", collectionResourceRel = "filetypes", exported = true)
public interface FileTypeRepository
        extends /*PagingAndSortingRepository<FileType, Integer>,*/ CrudRepository<FileType, Integer> {

}
