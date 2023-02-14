package com.mflq.downloader.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mflq.downloader.model.DownloadFile;
import com.mflq.downloader.model.DownloadFileProjection;

@RepositoryRestResource(path = "downloadfile", collectionResourceRel = "downloadfiles", exported = true)
public interface DownloadFileRepository
		extends PagingAndSortingRepository<DownloadFile, Integer>, CrudRepository<DownloadFile, Integer> {
	@Query(value = "CALL getById(:id)", nativeQuery = true)
	DownloadFileProjection getDownloadFileById(@Param("id") Integer id);

}
