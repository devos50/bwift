# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bank_account (
  address                       varchar(255) not null,
  balance                       double not null,
  constraint pk_bank_account primary key (address)
);

create table transaction (
  id                            varchar(255) not null,
  source_account                varchar(255),
  destination_account           varchar(255),
  amount                        double not null,
  timestamp                     bigint not null,
  constraint pk_transaction primary key (id)
);


# --- !Downs

drop table if exists bank_account;

drop table if exists transaction;

