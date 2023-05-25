package com.mflq.datagateway.repository;

import com.mflq.datagateway.entity.DownloadFile;
import com.mflq.datagateway.model.DownloadFileProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "downloadfile", collectionResourceRel = "downloadfiles", exported = true)
public interface DownloadFileRepository
        extends /* PagingAndSortingRepository<DownloadFile, Integer>, */CrudRepository<DownloadFile, Integer> {
    /*
     * Es rocomendable usar una proyeccion con un procedure que hace un join, cuando
     * el resultado a devolver es solo un objeto, caso contrario si es una lista de
     * objetos dara error
     */
    @Query(value = "CALL SEARCHID(:id)", nativeQuery = true)
    DownloadFileProjection getDownloadFileById(@Param("id") Integer ident);

}
