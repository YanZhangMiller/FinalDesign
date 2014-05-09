create table NewsDetail (
ID varchar(255) key,
Website varchar(64),
ReleaseDateTime datetime,
Title Text,
Author Varchar(64),
Content mediumtext,
Hot int,
Source varchar(64),
UpdateDateTime datetime,
category int,
Geo varchar(255),
Sentiment tinyint,
Summary varchar(200),
SourceUrl varchar(255),
Processed bool
)