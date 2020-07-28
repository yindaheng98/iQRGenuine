drop database if exists iqrgenuine;
create database iqrgenuine DEFAULT CHARACTER SET utf8;
use iqrgenuine;
create table public_keys
(
cd_key int auto_increment,
md5_info text not null,
public_key text not null,
verified boolean default 0,
primary key(cd_key)
);
insert into public_keys(md5_info,public_key)values(md5('iqrgenuine'),'123456');
create table user_infos
(
username varchar(255),
md5_password varchar(255),
primary key(username)
);
insert into user_infos(username,md5_password)values('iqrgenuine',md5('iqrgenuine'));
create user 'iqrgenuine'@'%' identified by 'iqrgenuine';
grant select,update,insert on iqrgenuine.* to 'iqrgenuine'@'%';
flush privileges;