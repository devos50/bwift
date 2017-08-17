# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table node (
  name                          varchar(255) not null,
  is_company                    tinyint(1) default 0 not null,
  identification_code           varchar(255),
  bank_account                  varchar(255),
  ip_address                    varchar(255),
  port                          integer not null,
  constraint pk_node primary key (name)
);


# --- !Downs

drop table if exists node;

