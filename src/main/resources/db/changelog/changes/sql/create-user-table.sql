create table if not exists t_user
(
    id          BIGSERIAL primary key,
    login       varchar(30) unique not null
        constraint login_constraint check (char_length(login) > 0),
    first_name   varchar(50)        not null,
    last_name    varchar(50)        not null,
    phone_number varchar(15) unique not null,
    password    varchar(50)        not null,
    role_id     int
        constraint user_table_role_table_id_fk
            references t_role
);

create
unique index if not exists t_user_login_uindex on t_user (login);