-- =======================================================================================================================================
-- Board logic
-- =======================================================================================================================================

-- 프론트 테이블

select * from user;

CREATE table user(
   user_idx int auto_increment primary key,
   user_id varchar(10) not null,
   user_password varchar(100) not null,
   user_name varchar(50) not null,
   user_birth varchar(8) not null,
   user_email varchar(50) not null,
   user_phone varchar(11) not null,
   user_insertdate Datetime default now(),
   user_banned int default 0,
   userCol1 varchar(50),
   userCol2 varchar(50),
   userCol3 varchar(50)
);

select * from board;

create table board(
   board_idx int auto_increment primary key,
   board_name varchar(10) not null,
   board_price int not null,
   board_soldout int default 1,
   board_hit int default 0,
   board_sell_category int not null,
   board_regDate Datetime default now(),
   board_category int not null,
   board_auctionOnOff int default 0,
   board_content varchar(100) not null,
   boardCol1 varchar(50),
   boardCol2 varchar(50),
   boardCol3 varchar(50),
   user_idx int,
   foreign key (user_idx) references user(user_idx)
);
CREATE table reply(
   reply_idx int auto_increment primary key,
   reply_content varchar(4000) not null,
   replyCol1 varchar(50),
   replyCol2 varchar(50),
   replyCol3 varchar(50),
   board_idx int,
   user_idx int,
   foreign key (board_idx) references board(board_idx),
   foreign key (user_idx) references user(user_idx)
);
CREATE table auction(
   auction_idx int auto_increment primary key,
   auction_highPrice int not null, 
   auction_startTime Datetime default now(),
   auction_endTime Datetime,
   auctionCol1 varchar(50),
   auctionCol2 varchar(50),
   auctionCol3 varchar(50),
   user_idx int,
   board_idx int,
   foreign key (user_idx) references user(user_idx),
   foreign key (board_idx) references board(board_idx)
);
create table auctionOrder(
   auctionOrder_idx int auto_increment primary key,
   auctionOrder_used int not null,
   auctionOrderCol1 varchar(50),
   auctionOrderCol2 varchar(50),
   auctionOrderCol3 varchar(50),
   auction_idx int,
   user_idx int,
   foreign key (auction_idx) references auction(auction_idx),
   foreign key (user_idx) references user(user_idx)
);



CREATE table boardimage(
   boardImage_idx int auto_increment primary key,
   boardImage_saveName varchar(500) not null,
   boardImage_oriName varchar(500) not null,
   boardImagecol1 VARCHAR(50),
   boardImagecol2 VARCHAR(50),
   boardImagecol3 VARCHAR(50),
   board_idx int,
   foreign key (board_idx) references board(board_idx)
);
create table replyimage(
   replyImage_idx int auto_increment primary key,
   replyImage_saveName varchar(50) not null,
   replyImage_oriName varchar(50) not null,
   replyImageCol1 varchar(50),
   replyImageCol2 varchar(50),
   replyImageCol3 varchar(50),
   reply_idx int,
   foreign key (reply_idx) references reply (reply_idx)
);

-- 백엔드 테이블

CREATE table admin(
	admin_idx int primary key auto_increment,
	admin_id varchar(10) not null,
	admin_password varchar(100) not null,
	admin_name varchar(50) not null,
	admin_roleGroup varchar(50) not null,
	admin_col1 varchar(50),
	admin_col2 varchar(50),
	admin_col3 varchar(50)
);


CREATE table menuDB(
	menuDB_idx int primary key auto_increment,
	menuDB_name varchar(50) not null,
	menuDB_use varchar(10) not null,
	menuDB_col1 varchar(50),
	menuDB_col2 varchar(50),
	menuDB_col3 varchar(50) 
); 


CREATE table xmlrole(
	xmlrole_idx int primary key  auto_increment ,
	xmlrole_xmlfilepath varchar(10) not null,
	xmlrole_col1 varchar(50),
	xmlrole_col2 varchar(50),
	xmlrole_col3 varchar(50)
);


CREATE table back_category(
	back_category_idx int primary key auto_increment,
	back_category_name varchar(50) not null,
	back_category_use int(1) default 0, 
	back_category_col1 varchar(50),
	back_category_col2 varchar(50),
	back_category_col3 varchar(50)
);

CREATE table back_notice(
	back_notice_idx int primary key auto_increment,
	back_notice_subject varchar(20) not null,
	back_notice_content varchar(100) not null,
	back_notice_regDate dateTime default now(),
	back_notice_col1 varchar(50),
	back_notice_col2 varchar(50),
	back_notice_col3 varchar(50)
);


CREATE table back_noticefile(
	back_noticefile_idx int primary key auto_increment,
	back_noticefile_saveName varchar(500) not null,
	back_noticefile_oriName varchar(500) not null,
	back_noticefile_col1 varchar(50),
	back_noticefile_col2 varchar(50),
	back_noticefile_col3 varchar(50),
	back_notice_idx int,
	foreign key(back_notice_idx) references back_notice(back_notice_idx)
	
);


CREATE table back_qna(
	back_qna_idx int primary key auto_increment,
	back_qna_name varchar(10) not null,
	back_qna_content varchar(100) not null,
	back_qna_question int(1) not null,
	back_qna_regDate dateTime default now(),
	user_idx int,
	foreign key(user_idx) references user(user_idx),
	back_qna_col1 varchar(50),
	back_qna_col2 varchar(50),
	back_qna_col3 varchar(50)
);


CREATE table back_qnaReply(
	back_qna_reply_idx int primary key auto_increment,
	back_qna_reply_content varchar(500) not null,
	back_qna_reply_col1 varchar(50),
	back_qna_reply_col2 varchar(50),
	back_qna_reply_col3 varchar(50),
	back_qna_idx int,
	foreign key(back_qna_idx) references back_qna(back_qna_idx)
);



CREATE table back_qnafile(
	back_qnafile_idx int primary key auto_increment,
	back_qnafile_saveName varchar(500) not null,
	back_qnafile_oriName varchar(500) not null,
	back_qnafile_col1 varchar(50),
	back_qnafile_col2 varchar(50),
	back_qnafile_col3 varchar(50),
	back_qna_idx int,
	foreign key(back_qna_idx) references back_qna(back_qna_idx)
);



CREATE table banner(
	banner_idx int primary key auto_increment,
	banner_show int(1) not null,
	banner_saveName varchar(500) not null,
	banner_oriName varchar(500) not null,
	banner_col1 varchar(50),
	banner_col2 varchar(50),
	banner_col3 varchar(50)
);

-- =======================================================================================================================================
-- 테스트 스크립트
-- =======================================================================================================================================

-- 테스트 스크립트

INSERT into board (
	board_name,
	board_price,
	board_soldout,
	board_hit,
	board_sell_category,
	board_category,
	board_auctionOnOff,
	user_idx, 
	board_content
	)
values (
	'테스트',
	9999,
	1,
	5,
	0,
	1,
	0,
	1,
	'테스트입니다'
);

INSERT into board (
	board_name,
	board_price,
	board_soldout,
	board_hit,
	board_sell_category,
	board_category,
	board_auctionOnOff,
	user_idx, 
	board_content
	)
values (
	'경매글테스트',
	1500,
	0,
	10,
	0,
	2,
	1,
	1,
	'경매글테스트입니다'
);

select max(board_idx) from board

select * from board where user_idx=1;

select user_id from board b user

SELECT * FROM boardimage;

SELECT * from board;

SELECT * from user;



INSERT into board (
		board_name,
		board_price,
		board_soldout,
		board_hit,
		board_sell_category,
		board_category,
		board_auctionOnOff,
		user_idx,
		board_content
		)
		values (
		'test',
		 3000,
		 1,
		 null,
		1,
		 1,
		 null,
		1,
		'ㅎㅇ'
		);
		
select * from board;

select * from boardimage;

select * from boardimage order by boardImage_idx DESC;

select * from boardimage where board_idx=3;

select * from boardimage where board_idx=1
	

select * from boardimage WHERE board_idx=4

select * from boardimage order by boardImage_idx desc

select * from board order by board_idx desc

select max(board_idx) from boardimage	

SELECT * FROM board ORDER BY board_idx DESC

SELECT * FROM board ORDER BY board_idx DESC LIMIT 0, 8

select * from user;

SELECT * FROM board;

SELECT * FROM board where board_idx=4

select * from boardimage

select * from board where board_idx=14

delete from board where board_idx=4

select * from reply r 

insert into reply (reply_content, board_idx, user_idx) values ('이걸 누가 삼 ㄹㅇㅋㅋ', 4, 1)

delete from reply where board_idx=4

INSERT into reply (
		reply_content, board_idx, user_idx
		)
		values (
		'1번 게시물의 댓글입니다. 여래신장을 누가 사냐 ㅋㅋ',
		4,
		1
		)

select * from reply;

select * from reply where board_idx = 4

select * from board where board_idx =4;

select * from board

select * from reply where board_idx = 4
