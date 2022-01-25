create table if not exists t_token_storage
(
    id   BIG_SERIAL primary key,
    token varchar(100) not null
);