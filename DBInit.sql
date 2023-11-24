/*YOU CAN USE THIS AS THE SQL FILE. MAKE SURE TO COPY AND PASTE THE NEW DATA HERE*/

create table CRUISE1PATH
(
    LOCATION    VARCHAR(255) not null
        constraint "CRUISE1PATH_pk"
            primary key,
    TRAVELDAYS  INTEGER      not null,
    ARRIVALDATE DATE
);



create table CRUISE2PATH
(
    LOCATION    VARCHAR(255) not null
        constraint "CRUISE2PATH_pk"
            primary key,
    TRAVELDAYS  INTEGER,
    ARRIVALDATE DATE
);


create table CRUISE3PATH
(
    LOCATION    VARCHAR(255) not null
        constraint "CRUISE3PATH_pk"
            primary key,
    TRAVELDAYS  INTEGER,
    ARRIVALDATE DATE
);

create table LOGINDATA
(
    USERNAME      VARCHAR(255) not null
        constraint "LOGINDATA_pk"
            primary key,
    PASSWORD      VARCHAR(255) not null,
    NAME          VARCHAR(255) not null,
    EMAIL         VARCHAR(255) not null,
    ADDRESS       VARCHAR(255) not null,
    "AccountType" VARCHAR(255) not null
);

create table PASSWORDRESETS
(
    USERNAME    VARCHAR(255)
        constraint "PASSWORDRESETS_LOGINDATA_USERNAME_fk"
            references LOGINDATA,
    NEWPASSWORD VARCHAR(255)
);