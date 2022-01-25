create table if not exists t_token_storage
(
    id   BIGSERIAL primary key,
    token varchar(100) not null
);