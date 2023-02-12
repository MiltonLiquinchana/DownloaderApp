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
public class Category {

	@Id
	@Column(name = "PK_CATEGORY", nullable = false, updatable = false)
	private Integer pkCategory;

	@Column(name = "C_Name", nullable = false, length = 100)
	private String cName;

	@Column(name = "C_FileOutputPath", nullable = false, length = 100)
	private String cFileOutputPath;

	@OneToMany(mappedBy = "category", cascade = CascadeType.REFRESH)
	@ToString.Exclude
	private List<DownloadFile> downloadFiles;
}
