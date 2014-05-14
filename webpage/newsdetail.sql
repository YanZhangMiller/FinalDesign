create table if not exists NewsDetail (
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
Summary varchar(200)
);
create table if not exists NewsSource (
	ID varchar(255) key,
	sourceUrl varchar(255)
);