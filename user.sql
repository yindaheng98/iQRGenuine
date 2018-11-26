#create user tests@localhost identified by 'tests';
grant select,update,insert on tests.* to tests@localhost;
flush privileges;