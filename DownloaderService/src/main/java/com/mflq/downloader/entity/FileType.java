package com.mflq.downloader.model;

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
public class FileType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "PK_FILETYPE", nullable = false, updatable = false)
	private Integer pkFiletype;

	@Column(name = "FT_Type", nullable = false, length = 10)
	private String type;

//	@OneToMany(mappedBy = "fileType" ,cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//	@ToString.Exclude
//	private List<DownloadFile> downloadFiles;
}
