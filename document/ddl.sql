create table user(
	email varchar(1024) not null,
	firstname nvarchar(1024) not null,
	lastname nvarchar(1024) not null,
	create_date date,
	primary key(email)
)

create table password(
	idx int auto_increment not null,
	email varchar(1024) not null,
	password varchar(128) not null,
	isdelete bit not null default 0,
	create_date datetime,
	
	primary key(idx),
	foreign key (email) references user(email)
)

create table account_type (
	code char(4) not null,
	name nvarchar(1024) not null,
	isdelete bit not null default 0,
	
	primary key(code)
)

insert into account_type (code,name) values('PYMN', N'支出');
insert into account_type (code,name) values('INCM', N'収入');

create table category (
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
)

create table account(
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