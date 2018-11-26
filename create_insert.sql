#create database tests;
use tests;
create table public_keys
(
cd_key int auto_increment,
md5_info text not null,
public_key text not null,
verified boolean default 0,
primary key(cd_key)
);
insert into public_keys(md5_info,public_key)values('123456','6543210');
