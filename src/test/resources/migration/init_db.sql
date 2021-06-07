create table if not exists rule_template
(
    id         serial4 primary key,
    created_at TIMESTAMP  default CURRENT_TIMESTAMP null,
    updated_at TIMESTAMP  default CURRENT_TIMESTAMP null,
    name       varchar(50)                          not null,
    arguments  jsonb                        not null,
    flink_job  varchar(50)                          not null,
    active     bool default true                 not null,
        constraint rule_template_flink_job_uindex
            unique (flink_job)
);