-- MySQL Script generated by MySQL Workbench
-- Tue Mar  8 10:49:35 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gkmdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gkmdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gkmdb` DEFAULT CHARACTER SET utf8 ;
USE `gkmdb` ;

-- -----------------------------------------------------
-- Table `gkmdb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`user` (
  `user_idx` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(10) NOT NULL,
  `user_password` VARCHAR(100) NOT NULL,
  `user_name` VARCHAR(10) NOT NULL,
  `user_birth` VARCHAR(8) NOT NULL,
  `user_email` VARCHAR(45) NOT NULL,
  `user_phone` VARCHAR(11) NOT NULL,
  `user_insertdate` DATETIME NOT NULL,
  `user_banned` INT NOT NULL,
  `usercol1` VARCHAR(45) NULL,
  `usercol2` VARCHAR(45) NULL,
  `usercol3` VARCHAR(45) NULL,
  PRIMARY KEY (`user_idx`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`board` (
  `board_idx` INT NOT NULL AUTO_INCREMENT,
  `board_name` VARCHAR(45) NOT NULL,
  `board_price` INT NOT NULL,
  `board_soldout` INT NOT NULL,
  `board_hit` INT NOT NULL,
  `board_sell_category` INT NOT NULL,
  `board_regDate` DATE NOT NULL DEFAULT now(),
  `board_category` INT NOT NULL,
  `board_auctionOnOff` INT NOT NULL,
  `board_content` VARCHAR(100) NULL,
  `boardcol1` VARCHAR(45) NULL,
  `boardcol2` VARCHAR(45) NULL,
  `boardcol3` VARCHAR(45) NULL,
  `boardcol` VARCHAR(45) NULL,
  `user_user_idx` INT NOT NULL,
  PRIMARY KEY (`board_idx`, `user_user_idx`),
  INDEX `fk_board_user1_idx` (`user_user_idx` ASC) VISIBLE,
  CONSTRAINT `fk_board_user1`
    FOREIGN KEY (`user_user_idx`)
    REFERENCES `gkmdb`.`user` (`user_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`reply`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`reply` (
  `reply_idx` INT NOT NULL AUTO_INCREMENT,
  `reply_content` VARCHAR(4000) NOT NULL,
  `replyImagecol1` VARCHAR(45) NULL,
  `replyImagecol2` VARCHAR(45) NULL,
  `replyImagecol3` VARCHAR(45) NULL,
  `board_board_idx` INT NOT NULL,
  `user_user_idx` INT NOT NULL,
  PRIMARY KEY (`reply_idx`, `board_board_idx`, `user_user_idx`),
  INDEX `fk_reply_board1_idx` (`board_board_idx` ASC) VISIBLE,
  INDEX `fk_reply_user1_idx` (`user_user_idx` ASC) VISIBLE,
  CONSTRAINT `fk_reply_board1`
    FOREIGN KEY (`board_board_idx`)
    REFERENCES `gkmdb`.`board` (`board_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reply_user1`
    FOREIGN KEY (`user_user_idx`)
    REFERENCES `gkmdb`.`user` (`user_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`replyImage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`replyImage` (
  `replyImage_idx` INT NOT NULL AUTO_INCREMENT,
  `replyImage_saveName` VARCHAR(45) NOT NULL,
  `replyImage_oriName` VARCHAR(45) NOT NULL,
  `replyImagecol1` VARCHAR(45) NULL,
  `replyImagecol2` VARCHAR(45) NULL,
  `replyImagecol3` VARCHAR(45) NULL,
  `reply_reply_idx` INT NOT NULL,
  PRIMARY KEY (`replyImage_idx`, `reply_reply_idx`),
  INDEX `fk_replyImage_reply1_idx` (`reply_reply_idx` ASC) VISIBLE,
  CONSTRAINT `fk_replyImage_reply1`
    FOREIGN KEY (`reply_reply_idx`)
    REFERENCES `gkmdb`.`reply` (`reply_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`boardImage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`boardImage` (
  `boardImage_idx` INT NOT NULL AUTO_INCREMENT,
  `boardImage_saveName` VARCHAR(500) NOT NULL,
  `boardImage_oriName` VARCHAR(500) NOT NULL,
  `boardImagecol1` VARCHAR(45) NULL,
  `boardImagecol2` VARCHAR(45) NULL,
  `boardImagecol3` VARCHAR(45) NULL,
  `board_board_idx` INT NOT NULL,
  PRIMARY KEY (`boardImage_idx`, `board_board_idx`),
  INDEX `fk_boardImage_board1_idx` (`board_board_idx` ASC) VISIBLE,
  CONSTRAINT `fk_boardImage_board1`
    FOREIGN KEY (`board_board_idx`)
    REFERENCES `gkmdb`.`board` (`board_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`auction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`auction` (
  `auction_idx` INT NOT NULL,
  `auction_highPrice` INT NOT NULL,
  `auction_startTime` DATETIME NULL,
  `auction_endTime` DATETIME NULL,
  `auctioncol1` VARCHAR(45) NULL,
  `auctioncol2` VARCHAR(45) NULL,
  `auctioncol3` VARCHAR(45) NULL,
  `board_board_idx` INT NOT NULL,
  PRIMARY KEY (`board_board_idx`),
  INDEX `fk_auction_board1_idx` (`board_board_idx` ASC) VISIBLE,
  CONSTRAINT `fk_auction_board1`
    FOREIGN KEY (`board_board_idx`)
    REFERENCES `gkmdb`.`board` (`board_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`admin` (
  `admin_idx` INT NOT NULL,
  `admin_id` VARCHAR(10) NOT NULL,
  `admin_password` VARCHAR(100) NOT NULL,
  `admin_name` VARCHAR(45) NOT NULL,
  `admin_roleGroup` VARCHAR(45) NOT NULL,
  `admincol1` VARCHAR(45) NULL,
  `admincol2` VARCHAR(45) NULL,
  `admincol3` VARCHAR(45) NULL,
  PRIMARY KEY (`admin_idx`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`menuDB`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`menuDB` (
  `menuDB_idx` INT NOT NULL,
  `menuDB_name` VARCHAR(45) NOT NULL,
  `menuDB_use` VARCHAR(45) NOT NULL,
  `menuDBcol1` VARCHAR(45) NULL,
  `menuDBcol2` VARCHAR(45) NULL,
  `menuDBcol3` VARCHAR(45) NULL,
  PRIMARY KEY (`menuDB_idx`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`xmlrole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`xmlrole` (
  `xmlrole_idx` INT NOT NULL,
  `xmlrole_xmlfilepath` VARCHAR(45) NOT NULL,
  `xmlrolecol1` VARCHAR(45) NULL,
  `xmlrolecol2` VARCHAR(45) NULL,
  `xmlrolecol3` VARCHAR(45) NULL,
  PRIMARY KEY (`xmlrole_idx`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`back_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`back_category` (
  `back_category_idx` INT NOT NULL,
  `back_category_name` VARCHAR(45) NOT NULL,
  `back_category_use` INT NOT NULL,
  `back_categorycol1` VARCHAR(45) NULL,
  `back_categorycol2` VARCHAR(45) NULL,
  `back_categorycol3` VARCHAR(45) NULL,
  PRIMARY KEY (`back_category_idx`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`back_notice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`back_notice` (
  `back_notice_idx` INT NOT NULL,
  `back_notice_subject` VARCHAR(10) NOT NULL,
  `back_notice_content` VARCHAR(100) NOT NULL,
  `back_notice_regDate` DATE NOT NULL,
  `back_notice_file` VARCHAR(45) NOT NULL,
  `back_noticecol1` VARCHAR(45) NULL,
  `back_noticecol1` VARCHAR(45) NULL,
  `back_noticecol3` VARCHAR(45) NULL,
  PRIMARY KEY (`back_notice_idx`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`back_noticefile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`back_noticefile` (
  `back_noticefile_idx` INT NOT NULL,
  `back_noticefile_saveName` VARCHAR(45) NOT NULL,
  `back_noticefile_oriName` VARCHAR(45) NOT NULL,
  `back_noticefilecol1` VARCHAR(45) NULL,
  `back_noticefilecol2` VARCHAR(45) NULL,
  `back_noticefilecol3` VARCHAR(45) NULL,
  `notice_notice_idx` INT NOT NULL,
  PRIMARY KEY (`back_noticefile_idx`, `notice_notice_idx`),
  INDEX `fk_back_noticefile_notice1_idx` (`notice_notice_idx` ASC) VISIBLE,
  CONSTRAINT `fk_back_noticefile_notice1`
    FOREIGN KEY (`notice_notice_idx`)
    REFERENCES `gkmdb`.`back_notice` (`back_notice_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`back_qna`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`back_qna` (
  `back_qna_idx` INT NOT NULL,
  `back_qna_name` VARCHAR(10) NOT NULL,
  `back_qna_content` VARCHAR(45) NOT NULL,
  `back_qna_question` INT NOT NULL,
  `back_qna_regDate` DATE NOT NULL,
  `back_qna_image` VARCHAR(45) NOT NULL,
  `back_qnacol1` VARCHAR(45) NULL,
  `back_qnacol2` VARCHAR(45) NULL,
  `back_qnacol3` VARCHAR(45) NULL,
  `user_user_idx` INT NOT NULL,
  PRIMARY KEY (`back_qna_idx`, `user_user_idx`),
  INDEX `fk_back_qna_user1_idx` (`user_user_idx` ASC) VISIBLE,
  CONSTRAINT `fk_back_qna_user1`
    FOREIGN KEY (`user_user_idx`)
    REFERENCES `gkmdb`.`user` (`user_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`back_qna_reply`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`back_qna_reply` (
  `back_qna_reply_idx` INT NOT NULL,
  `back_qna_reply_content` VARCHAR(45) NOT NULL,
  `back_qna_replycol1` VARCHAR(45) NULL,
  `back_qna_replycol2` VARCHAR(45) NULL,
  `back_qna_replycol3` VARCHAR(45) NULL,
  `back_qna_back_qna_idx` INT NOT NULL,
  PRIMARY KEY (`back_qna_reply_idx`, `back_qna_back_qna_idx`),
  INDEX `fk_back_qna_reply_back_qna1_idx` (`back_qna_back_qna_idx` ASC) VISIBLE,
  CONSTRAINT `fk_back_qna_reply_back_qna1`
    FOREIGN KEY (`back_qna_back_qna_idx`)
    REFERENCES `gkmdb`.`back_qna` (`back_qna_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`back_qnafile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`back_qnafile` (
  `back_qnafile_idx` INT NOT NULL,
  `back_qnafile_saveName` VARCHAR(45) NOT NULL,
  `back_qnafile_oriName` VARCHAR(45) NOT NULL,
  `back_qnafilecol1` VARCHAR(45) NULL,
  `back_qnafilecol2` VARCHAR(45) NULL,
  `back_qnafilecol3` VARCHAR(45) NULL,
  `back_qna_back_qna_idx` INT NOT NULL,
  PRIMARY KEY (`back_qnafile_idx`, `back_qna_back_qna_idx`),
  INDEX `fk_back_qnafile_back_qna1_idx` (`back_qna_back_qna_idx` ASC) VISIBLE,
  CONSTRAINT `fk_back_qnafile_back_qna1`
    FOREIGN KEY (`back_qna_back_qna_idx`)
    REFERENCES `gkmdb`.`back_qna` (`back_qna_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`banner`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`banner` (
  `banner_idx` INT NOT NULL,
  `banner_image` INT NOT NULL,
  `banner_show` INT NOT NULL,
  `banner_saveName` VARCHAR(45) NOT NULL,
  `banner_oriName` VARCHAR(45) NOT NULL,
  `bannercol1` VARCHAR(45) NULL,
  `bannercol2` VARCHAR(45) NULL,
  `bannercol3` VARCHAR(45) NULL,
  PRIMARY KEY (`banner_idx`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`auction_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`auction_order` (
  `auction_order_idx` INT NOT NULL AUTO_INCREMENT,
  `auction_order_used` VARCHAR(45) NOT NULL,
  `user_detailcol1` VARCHAR(45) NULL,
  `user_detailcol2` VARCHAR(45) NULL,
  `user_detailcol3` VARCHAR(45) NULL,
  `auction_idx` INT NOT NULL,
  `user_user_idx` INT NOT NULL,
  `auction_board_board_idx` INT NOT NULL,
  PRIMARY KEY (`auction_order_idx`, `auction_idx`, `user_user_idx`, `auction_board_board_idx`),
  INDEX `fk_user_detail_user1_idx` (`user_user_idx` ASC) VISIBLE,
  INDEX `fk_auction_order_auction1_idx` (`auction_board_board_idx` ASC) VISIBLE,
  CONSTRAINT `fk_user_detail_user1`
    FOREIGN KEY (`user_user_idx`)
    REFERENCES `gkmdb`.`user` (`user_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_auction_order_auction1`
    FOREIGN KEY (`auction_board_board_idx`)
    REFERENCES `gkmdb`.`auction` (`board_board_idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gkmdb`.`autio_userPoint`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gkmdb`.`autio_userPoint` (
  `autio_userPoint_idx` INT NOT NULL AUTO_INCREMENT,
  `autio_usePoint` VARCHAR(45) NULL,
  PRIMARY KEY (`autio_userPoint_idx`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
-- ------------------------------------------------------


INSERT into user(
	user_id,
	user_password,
	user_name,
	user_birth,
	user_email,
	user_phone 
	)
values(
	'chlehddh',
	'1234',
	'최동오',
	'19940520',
	'chlehddh8062@gmail.com',
	'01072318062'
);


SELECT * FROM `user` u ;

-- 성공 // <!-- 1. insert_저장하기(회원가입하기) -->
INSERT into user(
		user_id,
		user_password,
		user_name,
		user_birth,
		user_email,
		user_phone,
		userCol1,
		userCol2
		)
		values(
		'testuser23',
		'1234',
		'테스터',
		'19940520',
		'sgagkmarket@gmail.com',
		'01072318062',
		'',
		''
		)
		
-- 성공 // <!-- 2. select_1개 얻기 -->
select * from `user` where user_idx=2
	
		
-- 성공 // <!-- 3. update_수정하기(회원정보수정하기) -->
UPDATE `user` SET 
	user_phone = '01072318062'
WHERE 
	user_idx = 2
	
-- 성공 // <!-- 4. delete_삭제하기(회원탈퇴하기) -->
delete from user where user_idx=6

-- 성공 // <!-- 7. select_아디 중복 확인_0:없음/사용가능_1:있음/사용불가 -->
select count(*) from user where user_id='chlehddh'

-- 성공 // <!-- 8. select_이름과 전화번호로 가져오기(아디찾기 사용) -->
select * from user where user_email='chlehddh8062@gmail.com' and user_name='gondo';

-- 성공 // <!-- 9. select_아디와 전화번호로 가져오기(비번찾기 사용) -->
select * from user where user_id='cdo90' and user_email='chlehddh8062@gmail.com' and user_name='gondo';

-- 성공 // <!-- 9. select_아디와 전화번호로 가져오기(비번찾기 사용) -->
-- 1이면 데이터를 찾아줘야 함. 임시 비번 생성 / 업데이트 쿼리 호출 / 임시 비번 전달. 
select count(*) from user where user_id='cdo90' and user_email='chlehddh8062@gmail.com' and user_name='gondo';

SELECT * FROM user;

-- 성공 // <!-- 10. update_유저 벤 하기-->
update user set user_banned=0 where user_idx=1

-- 성공 // <!-- 11. update_비밀번호 변경하기 -->
update user set user_password='123' where user_id='chlehddh'

-- 성공 // <!-- 52. ID로 VO 가져오기 -->
select * from user where user_id='chlehddh'

-- 성공 // <!-- 53. 이름, 전화번호로 VO 가져오기 -->
select * from user where user_name='테스터' and user_phone='01072318062';

-- 성공 // <!-- 54. 이름, 아디, 이메일로 VO 가져오기 -->
select count(*) from user where user_id=#{user_id} and user_email=#{user_email} and user_name=#{user_name}

SELECT * FROM user;	
		
-- 성공 // <!-- 55. select_countCheckPassword 비밀번호 일치 여부 확인하기 -->
SELECT COUNT(*) from user WHERE user_id='cdo90' and user_password='$2a$10$CLfAUUw2wsn65e8LjjGFwuuu.w/yoFfYrAdxYLqJe0WHaXAi1ix.q' 


		