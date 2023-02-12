package com.mflq.downloader.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mflq.downloader.model.DownloadFile;

@RepositoryRestResource(path = "downloadfiles", collectionResourceRel = "DownloadFile")
public interface DownloadFileRepository extends PagingAndSortingRepository<DownloadFile, Integer> {

}
