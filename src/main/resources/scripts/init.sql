drop table if exists user_data cascade ;
drop sequence if exists user_sequence;
create sequence user_sequence start with 1 increment by 50;
create table user_data (
                      id bigint not null,
                      first_name varchar(255) not null ,
                      last_name varchar(255) not null ,
                      primary key (id)
);

drop table if exists email;
drop sequence if exists email_sequence;
create sequence email_sequence start with 1 increment by 50;
create table email (
                        id bigint not null,
                        email varchar(255) not null ,
                        user_data_id bigint not null,
                        primary key (id)
);

drop table if exists phone;
drop sequence if exists phone_sequence;
create sequence phone_sequence start with 1 increment by 50;
create table phone (
                       id bigint not null,
                       phone varchar(255) not null ,
                       user_data_id bigint not null,
                       primary key (id)
);
alter table email
    add constraint user_email_fk foreign key (user_data_id) references user_data (id);
alter table phone
    add constraint user_phone_fk foreign key (user_data_id) references user_data (id);
