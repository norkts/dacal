-- auto-generated definition
create table RawMessage
(
    id       INTEGER
        primary key autoincrement,
    time     datetime,
    msg      TEXT,
    scenne   varchar(64),
    platform INTEGER
);



-- auto-generated definition
create table GiftMessage
(
    id        INTEGER
        primary key autoincrement,
    msg       TEXT,
    time      datetime,
    user      varchar(64),
    scene     varchar(64),
    gift_name varchar(64),
    num       INTEGER,
    platform  INTEGER
);

create table GiftType
(
    id        INT
        constraint GiftType_pk
            primary key,
    gift_name varchar(128),
    value     INT
);

create unique index GiftType_id_uindex
    on GiftType (id);


