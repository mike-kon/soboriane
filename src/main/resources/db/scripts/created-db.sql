/**
* Создание БД.
*
* Этот скрипт запускается на новой системе от имени пользователя postgresql
* или любого другого пользователя с правом superuser.
* Скрипт должен отрпботать до запуска liquibase.
*/

create database soboriane owner koma_worker ENCODING  = 'UTF8';