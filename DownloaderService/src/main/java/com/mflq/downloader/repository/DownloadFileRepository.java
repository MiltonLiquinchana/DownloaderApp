package com.mflq.downloader.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mflq.downloader.model.DownloadFile;

@RepositoryRestResource(path = "downloadfile", collectionResourceRel = "downloadfiles", exported = true)
public interface DownloadFileRepository extends PagingAndSortingRepository<DownloadFile, Integer> {

}
