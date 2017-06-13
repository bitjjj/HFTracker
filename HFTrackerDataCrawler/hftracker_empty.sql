/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.24 : Database - hftracker
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hftracker` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `hftracker`;

/*Table structure for table `fund_date_list` */

DROP TABLE IF EXISTS `fund_date_list`;

CREATE TABLE `fund_date_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` varchar(50) DEFAULT NULL,
  `portfolioValue` varchar(200) DEFAULT NULL,
  `qtr` varchar(100) DEFAULT NULL COMMENT 'Change This QTR',
  `fund_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fund_info_date` (`fund_name`),
  CONSTRAINT `fund_info_date` FOREIGN KEY (`fund_name`) REFERENCES `fund_info` (`uniqueName`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=745 DEFAULT CHARSET=utf8;

/*Table structure for table `fund_holdings` */

DROP TABLE IF EXISTS `fund_holdings`;

CREATE TABLE `fund_holdings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `security` varchar(200) DEFAULT NULL,
  `ticker` varchar(50) DEFAULT NULL,
  `shares` int(11) DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  `activity` float DEFAULT NULL,
  `action` int(11) DEFAULT NULL COMMENT '1:up,-1:down,0:unchanged,2:new,-2:removed',
  `port` float DEFAULT NULL,
  `dateStr` varchar(50) DEFAULT NULL,
  `fund_name` varchar(200) DEFAULT NULL,
  `securityType` varchar(50) DEFAULT NULL,
  `addBy` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fund_info_holdings` (`fund_name`),
  CONSTRAINT `fund_info_holdings` FOREIGN KEY (`fund_name`) REFERENCES `fund_info` (`uniqueName`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=500267 DEFAULT CHARSET=utf8;

/*Table structure for table `fund_industry_percentage` */

DROP TABLE IF EXISTS `fund_industry_percentage`;

CREATE TABLE `fund_industry_percentage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` varchar(50) DEFAULT NULL,
  `num` float DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `fund_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fund_info_industry` (`fund_name`),
  CONSTRAINT `fund_info_industry` FOREIGN KEY (`fund_name`) REFERENCES `fund_info` (`uniqueName`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5470 DEFAULT CHARSET=utf8;

/*Table structure for table `fund_info` */

DROP TABLE IF EXISTS `fund_info`;

CREATE TABLE `fund_info` (
  `nameEN` varchar(200) DEFAULT NULL,
  `nameZH` varchar(50) DEFAULT NULL,
  `nameTW` varchar(50) DEFAULT NULL,
  `managerEN` varchar(200) DEFAULT NULL,
  `managerZH` varchar(50) DEFAULT NULL,
  `managerTW` varchar(50) DEFAULT NULL,
  `summary` text,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uniqueName` varchar(200) DEFAULT NULL,
  `managerPic` varchar(200) DEFAULT NULL,
  `refId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FUNDNAME` (`uniqueName`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

/*Table structure for table `fund_news` */

DROP TABLE IF EXISTS `fund_news`;

CREATE TABLE `fund_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(1000) DEFAULT NULL,
  `dateStr` varchar(50) DEFAULT NULL,
  `summary` text,
  `picUrl` varchar(1024) DEFAULT NULL,
  `url` varchar(1024) DEFAULT NULL,
  `refId` varchar(1024) DEFAULT NULL,
  `newsType` int(11) DEFAULT NULL COMMENT '1,EN;2,ZH',
  `fund_name` varchar(200) DEFAULT NULL,
  `timeMark` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fund_info_news` (`fund_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6890 DEFAULT CHARSET=utf8;

/*Table structure for table `spx_dps_year` */

DROP TABLE IF EXISTS `spx_dps_year`;

CREATE TABLE `spx_dps_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` varchar(100) DEFAULT NULL,
  `timeMark` bigint(20) DEFAULT NULL,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=719 DEFAULT CHARSET=latin1;

/*Table structure for table `spx_eps_year` */

DROP TABLE IF EXISTS `spx_eps_year`;

CREATE TABLE `spx_eps_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` varchar(100) DEFAULT NULL,
  `timeMark` bigint(20) DEFAULT NULL,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=865 DEFAULT CHARSET=latin1;

/*Table structure for table `spx_info_year` */

DROP TABLE IF EXISTS `spx_info_year`;

CREATE TABLE `spx_info_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` varchar(100) DEFAULT NULL,
  `timeMark` bigint(20) DEFAULT NULL,
  `value` float DEFAULT NULL,
  `spx_index_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Table structure for table `spx_pe_year` */

DROP TABLE IF EXISTS `spx_pe_year`;

CREATE TABLE `spx_pe_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` varchar(100) DEFAULT NULL,
  `timeMark` bigint(20) DEFAULT NULL,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1106 DEFAULT CHARSET=latin1;

/*Table structure for table `spx_shiller_pe_year` */

DROP TABLE IF EXISTS `spx_shiller_pe_year`;

CREATE TABLE `spx_shiller_pe_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` varchar(100) DEFAULT NULL,
  `timeMark` bigint(20) DEFAULT NULL,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=675 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
