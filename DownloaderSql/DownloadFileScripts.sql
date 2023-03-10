-- Scripts DownloadFile Table
-- Insercion DownloadFile scripts
INSERT INTO DownloadFile(
DF_URL,
DF_NAME,
DF_FileLength,
DF_Description,
FK_CATEGORY,
FK_FILETYPE,
FK_DOWNLOADSTATUS,
FK_DOWNLOADQUEUE
) VALUES(
"https://speed.hetzner.de/100MB.bin",
"100MB.bin",
104857600,
"description de archivo",
1,
2,
1,
1
),
(
"https://rr3---sn-n4v7snly.googlevideo.com/videoplayback?expire=1676104384&ei=YP7mY6qtGoGp1wLz767oCg&ip=216.131.114.25&id=o-AFkupBNrlSDMQHhiZEyAzuuFtzK5pmoM1OjgbmVV7I7Y&itag=22&source=youtube&requiressl=yes&spc=H3gIhhRbkucy1fureJ9MkhlXKUKzmMk&vprv=1&mime=video%2Fmp4&ns=bmAX6uZxMjglDkyKTAQxydsL&cnr=14&ratebypass=yes&dur=5760.592&lmt=1663445857638180&fexp=24007246&c=WEB&txp=5532434&n=lJt6bLFR705LHA&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cspc%2Cvprv%2Cmime%2Cns%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIhANtfWIbcmoUpGxhbL2SQlvrJZ4iyy3T9AsYP5l-edSSBAiAQKbs5ZtdgrN3_xyJtSKOoSo6bDHNm_kvPuXSVM_SSPQ%3D%3D&title=Super%20Dragon%20Ball%20Heroes%20Universal%20Conflict%20Arc%20(All%20Season%202%20Anime%20Episodes)&redirect_counter=1&rm=sn-4g5ekz7e&req_id=5e3443ee303aa3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=oA&mip=45.236.107.191&mm=31&mn=sn-n4v7snly&ms=au&mt=1675821231&mv=D&mvi=3&pl=0&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRgIhAO8stGNE0N7XuUvi_aKG8Htv2mV9XsIAI0jIH636A655AiEAhdylk-u0hNa09LE68nnEw5zUW3EDQWKSxHjwLn4XNew%3D",
"Super Dragon Ball Heroes Universal Conflict Arc (All Season 2 Anime Episodes).mp4",
521134832,
"description de video",
2,
3,
3,
1
);

-- selected DownloadFile scripts
SELECT * FROM DownloadFile;
-- updated DownloadFile scrips
