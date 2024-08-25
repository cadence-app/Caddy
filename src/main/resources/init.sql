create table if not exists t_cases(
    id              integer    primary key autoincrement,
    type            enum       not null,
    actor_id        varchar    not null,
    target_id       varchar    not null,
    reason          varchar    null,
    created_at      timestamp  not null,
    log_message_id  varchar    null
);