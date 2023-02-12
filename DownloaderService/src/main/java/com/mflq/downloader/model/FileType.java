package com.mflq.downloader.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class FileType {

	@Id
	@Column(name = "PK_FILETYPE", nullable = false, updatable = false)
	private Integer pkFiletype;

	@Column(name = "FT_Type", nullable = false, length = 10)
	private String ftType;

	@OneToMany(mappedBy = "fileType", cascade = CascadeType.REFRESH)
	@ToString.Exclude
	private List<DownloadFile> downloadFiles;
}
