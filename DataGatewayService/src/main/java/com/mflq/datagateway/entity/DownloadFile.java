package com.mflq.datagateway.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class DownloadFile {
	/*
	 * DF_URL, DF_NAME, DF_FileLength, DF_Description, FK_CATEGORY, FK_FILETYPE,
	 * FK_DOWNLOADSTATUS, FK_DOWNLOADQUEUE
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "PK_DOWNLOADFILE", nullable = false, updatable = false)
	private Integer pkDownloadfile;

	@Column(name = "DF_URL", nullable = false, columnDefinition = "text")
	private String donwloadFileUrl;

	@Column(name = "DF_Name", nullable = false, length = 250)
	private String downloadFileName;

	@Column(name = "DF_FileLength", nullable = false, precision = 65, scale = 15)
	private BigDecimal downloadFileLength;

	@Column(name = "DF_Description", length = 250)
	private String downloadFileDescription;

	@Column(name = "FK_CATEGORY")
	private Integer category;

	@Column(name = "FK_FILETYPE")
	private Integer fileType;

	@Column(name = "FK_DOWNLOADSTATUS")
	private Integer downloadStatus;

	@Column(name = "FK_DOWNLOADQUEUE")
	private Integer downloadQueue;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "FK_FILETYPE")
//	private FileType fileType;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "FK_CATEGORY")
//	private Category category;
}
