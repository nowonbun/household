drop table if exists password;
drop table if exists user;
drop table if exists account;
drop table if exists category;
drop table if exists account_type;

create table if not exists user(
	email varchar(256) not null,
	firstname nvarchar(1024) not null,
	lastname nvarchar(1024) not null,
	create_date date,
	primary key(email)
);

create table if not exists password(
	idx int auto_increment not null,
	email varchar(256) not null,
	password varchar(128) not null,
	isdelete bit not null default 0,
	create_date datetime,
	
	primary key(idx),
	foreign key (email) references user(email)
);

create table if not exists account_type (
	code char(4) not null,
	name nvarchar(1024) not null,
	isdelete bit not null default 0,
	
	primary key(code)
);

create table if not exists category (
	idx int auto_increment not null,
	account_type char(4) not null,
	name nvarchar(1024) not null,
	isdelete bit not null default 0,
	create_date datetime,
	creater	varchar(1024),
	last_update datetime,
	updater varchar(1024),
	
	primary key(idx),
	foreign key (account_type) references account_type(code)
);

create table if not exists account(
	idx int auto_increment not null,
	account_type char(4) not null,
	category int not null,
	date date not null,
	name nvarchar(1024) not null,
	money decimal(20,2) not null,
	isdelete bit not null default 0,
	create_date datetime,
	creater	varchar(1024),
	last_update datetime,
	updater varchar(1024),
	
	primary key(idx),
	foreign key (account_type) references account_type(code),
	foreign key (category) references category(idx)
);