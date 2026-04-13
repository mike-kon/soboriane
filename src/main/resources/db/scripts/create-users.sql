/**
* Создание пользователей.
*
* Этот скрипт запускается на новой системе от имени пользователя postgresql
* или любого другого пользователя с правом superuser.
* Скрипт должен отрпботать до запуска liquibase.
* Пользователь создается с паролем 123456. Можно (и нужно) изменить
*/

CREATE ROLE koma_admin WITH
	NOSUPERUSER
	NOCREATEDB
	NOCREATEROLE
	NOINHERIT
	NOLOGIN
	NOREPLICATION
	NOBYPASSRLS
	CONNECTION LIMIT -1;

CREATE ROLE koma_worker WITH
	NOSUPERUSER
	NOCREATEDB
	NOCREATEROLE
	NOINHERIT
	LOGIN
	NOREPLICATION
	NOBYPASSRLS
	ENCRYPTED PASSWORD '123456';
	CONNECTION LIMIT -1;
