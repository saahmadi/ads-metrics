drop table if exists impression_clicks;
drop table if exists impression;

create table if not exists impression (
    id serial primary key,
    impression_id text,
    app_id bigint,
    country_code varchar(4),
    advertiser_id bigint
);

create table if not exists impression_clicks(
    id serial primary key,
    impression bigint not null references impression,
    revenue float
);
