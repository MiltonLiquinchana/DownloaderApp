package com.mflq.downloader.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mflq.downloader.model.FileType;

@RepositoryRestResource(path = "filetype", collectionResourceRel = "filetypes", exported = true)
public interface FileTypeRepository extends PagingAndSortingRepository<FileType, Integer> {

}
