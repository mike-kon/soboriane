/**
* Создание схемы и раздача  прав
*/

create schema soboriane;
create schema liquibase;

grant CREATE, USAGE on schema soboriane to koma_worker;
grant CREATE, USAGE on schema liquibase to koma_worker;