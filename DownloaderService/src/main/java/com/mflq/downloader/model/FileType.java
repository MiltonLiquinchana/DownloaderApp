package com.mflq.downloader.model;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "PK_FILETYPE", nullable = false, updatable = false)
	private Integer pkFiletype;

	@Column(name = "FT_Type", nullable = false, length = 10)
	private String ftType;

	@OneToMany( cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<DownloadFile> downloadFiles;
}
