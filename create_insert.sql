create database tests;
use tests;
create table public_keys
(
id int auto_increment,
cd_key text not null,
public_key text not null,
verified boolean default 0,
primary key(id)
);
insert into public_keys(cd_key,public_key)values('123456','6543210');
