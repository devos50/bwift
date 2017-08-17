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

create table transaction (
  txid                          varchar(255) not null,
  is_complete                   tinyint(1) default 0 not null,
  source_account                varchar(255),
  destination_account           varchar(255),
  amount                        double not null,
  currency                      varchar(255),
  constraint pk_transaction primary key (txid)
);


# --- !Downs

drop table if exists node;

drop table if exists transaction;

