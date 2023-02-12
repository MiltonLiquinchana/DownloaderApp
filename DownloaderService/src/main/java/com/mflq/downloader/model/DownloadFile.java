package com.mflq.downloader.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DownloadFile {

	@Id
	@Column(name = "PK_DOWNLOADFILE", nullable = false, updatable = false)
	private Integer pkDownloadfile;

	@Column(name = "DF_URL", nullable = false, columnDefinition = "text")
	private String dfUrl;

	@Column(name = "DF_Name", nullable = false, length = 250)
	private String dfName;

	@Column(name = "DF_FileLength", nullable = false, precision = 65, scale = 15)
	private BigDecimal dfFileLength;

	@Column(name = "DF_Description", length = 250)
	private String dfDescription;

	@Column(name = "DF_Status", nullable = false)
	private Boolean dfStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_FILETYPE", nullable = false)
	private FileType fileType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CATEGORY", nullable = false)
	private Category category;
}
