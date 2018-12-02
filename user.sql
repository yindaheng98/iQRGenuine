create user iqrgenuine@localhost identified by 'iqrgenuine';
grant select,update,insert on iqrgenuine.* to iqrgenuine@localhost;
flush privileges;