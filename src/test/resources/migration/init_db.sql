create table rule_template
(
    id         serial4 primary key,
    created_at TIMESTAMP  default CURRENT_TIMESTAMP null,
    updated_at TIMESTAMP                            NULL DEFAULT CURRENT_TIMESTAMP,
    name       varchar(50)                          not null,
    arguments  varchar(2048)                        not null,
    flink_job  varchar(50)                          not null,
    active     bool default true                 not null,
        constraint rule_template_flink_job_uindex
            unique (flink_job)
);