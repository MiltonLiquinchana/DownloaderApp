package com.mflq.downloader.model;

import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class DownloadFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "PK_DOWNLOADFILE", nullable = false, updatable = false)
	private Integer pkDownloadfile;

	@Column(name = "DF_URL", nullable = false, columnDefinition = "text")
	private String url;

	@Column(name = "DF_Name", nullable = false, length = 250)
	private String downloadFileName;

	@Column(name = "DF_FileLength", nullable = false, precision = 65, scale = 15)
	private BigDecimal downloadFileLength;

	@Column(name = "DF_Description", length = 250)
	private String downloadFileDescription;

	@Column(name = "DF_Status", nullable = false)
	private Boolean downloadFilestatus;

	@Column(name = "FK_FILETYPE")
	private Integer fileType;

	@Column(name = "FK_CATEGORY")
	private Integer category;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "FK_FILETYPE")
//	private FileType fileType;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "FK_CATEGORY")
//	private Category category;
}
