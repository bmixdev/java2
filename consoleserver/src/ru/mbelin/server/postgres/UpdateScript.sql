CREATE TABLE IF NOT EXISTS users
(
	id serial not null PRIMARY KEY,
	username varchar(100),
	password varchar(30) not null,
	uid varchar(100),
	color varchar(100),
	description text
);

create unique index IF NOT EXISTS users_id_uindex
	on users (id);

create unique index IF NOT EXISTS  users_username_uindex
	on users (username);
