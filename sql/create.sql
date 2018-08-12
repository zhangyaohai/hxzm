create database test1;
use test1;

CREATE TABLE `test1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create database test2;
use test2;
CREATE TABLE `test1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create database test3;
use test3;
CREATE TABLE `test1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create database hxzm;
use hxzm;

CREATE TABLE `test1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table hxzm_user(
  `id` int(11) NOT NULL primary key AUTO_INCREMENT,
  `nick_name` varchar(50) DEFAULT NULL,
  account  varchar (20),
  real_name varchar(50) DEFAULT NULL,
  email  varchar(50) DEFAULT NULL,
  phone  char(11) ,
  image   varchar (200),
  user_type   int(2) comment '10 前端用户， 20 后台用户',
  status      int(2) default 10 comment '10 正常 20 冻结 30 删除',
  create_time  date,
  registe_time date,
  last_login_time date
);

create table hxzm_article(
  `id` int(11) NOT NULL primary key  AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL,
  content  varchar (20),
  `user_id` int(11),
  topic_id   int(2) comment '主题',
  tag         varchar(100),
  keywords    varchar(100),
  status      int(2) default 10 comment '1 初始 2 审核中  3 审核通过 4 审核失败',
  create_time  date,
  update_time date,
  update_user_id int(11)
);

create table hxzm_article_comment (
    `id` int(11) NOT NULL primary key  AUTO_INCREMENT,
    content  varchar (20),
    `user_id` int(11),
    `article_id` int(11),
      create_time  date
);

create table hxzm_topic(
  `id` int(11) NOT NULL primary key  AUTO_INCREMENT,
  `topid_name` varchar(50) DEFAULT NULL,
  parent_id  varchar (20),
  sort_num   int(11),
  level   int(2)
  );
