DELIMITER $$
DROP PROCEDURE IF EXISTS SEARCHID;
CREATE PROCEDURE SEARCHID(
IN id INT
)
BEGIN
	SELECT  DF_URL,
			DF_Name ,
            DF_Description,
            DF_FileLength ,
            DF_STATUS ,
            FT_Type ,
            C_Name,
            C_FileOutputPath
    FROM DownloadFile AS df 
    JOIN FileType as ft
    ON ft.PK_FILETYPE = df.FK_FILETYPE
    
    JOIN Category as ca
    ON ca.PK_CATEGORY=df.FK_CATEGORY
    
    WHERE df.PK_DOWNLOADFILE=id;
END $$
DELIMITER ;

call SEARCHID(1);
