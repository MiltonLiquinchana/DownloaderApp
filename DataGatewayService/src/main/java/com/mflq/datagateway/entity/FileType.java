package com.mflq.datagateway.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

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

	@Column(name = "FT_Name", nullable = false, length = 10)
	private String fileTypeName;

//	@OneToMany(mappedBy = "fileType", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//	@ToString.Exclude
//	private List<DownloadFile> downloadFiles;
}
