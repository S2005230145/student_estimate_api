-- Created by Ebean DDL
-- To stop Ebean DDL generation, remove this comment (both lines) and start using Evolutions

-- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
DECLARE done INT DEFAULT FALSE;
DECLARE c_fk_name CHAR(255);
DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
AND REFERENCED_TABLE_NAME IS NOT NULL;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN curs;

read_loop: LOOP
FETCH curs INTO c_fk_name;
IF done THEN
LEAVE read_loop;
END IF;
SET @sql = CONCAT('ALTER TABLE `', p_table_name, '` DROP FOREIGN KEY ', c_fk_name);
PREPARE stmt FROM @sql;
EXECUTE stmt;
END LOOP;

CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
SET @sql = CONCAT('ALTER TABLE `', p_table_name, '` DROP COLUMN `', p_column_name, '`');
PREPARE stmt FROM @sql;
EXECUTE stmt;
END
$$
-- apply changes
create table cp_system_action (
  id                            varchar(255) auto_increment not null,
  action_name                   varchar(255),
  action_desc                   varchar(255),
  module_name                   varchar(255),
  module_desc                   varchar(255),
  need_show                     tinyint(1) default 0 not null,
  display_order                 integer not null,
  create_time                   bigint not null,
  constraint pk_cp_system_action primary key (id)
);

create table v1_admin_config (
  id                            integer auto_increment not null,
  config_key                    varchar(255),
  config_value                  varchar(255),
  note                          varchar(255),
  enable                        tinyint(1) default 0 not null,
  is_encrypt                    tinyint(1) default 0 not null,
  update_time                   bigint not null,
  constraint pk_v1_admin_config primary key (id)
);

create table cp_group (
  id                            integer auto_increment not null,
  name                          varchar(255),
  is_admin                      tinyint(1) default 0 not null,
  description                   varchar(255),
  create_time                   bigint not null,
  constraint pk_cp_group primary key (id)
);

create table cp_group_action (
  id                            integer auto_increment not null,
  group_id                      integer not null,
  system_action_id              varchar(255),
  constraint pk_cp_group_action primary key (id)
);

create table cp_group_menu (
  id                            integer auto_increment not null,
  menu_id                       integer not null,
  group_id                      integer not null,
  constraint pk_cp_group_menu primary key (id)
);

create table cp_group_user (
  id                            bigint auto_increment not null,
  group_id                      integer not null,
  group_name                    varchar(255),
  member_id                     bigint not null,
  realname                      varchar(255),
  create_time                   bigint not null,
  constraint pk_cp_group_user primary key (id)
);

create table cp_log (
  log_id                        bigint auto_increment not null,
  log_unique                    varchar(255),
  log_sym_id                    varchar(255),
  log_mer_id                    integer not null,
  log_param                     varchar(255),
  log_created                   bigint not null,
  constraint pk_cp_log primary key (log_id)
);

create table v1_member (
  id                            bigint auto_increment not null,
  login_password                varchar(255),
  pay_password                  varchar(255),
  status                        integer not null,
  real_name                     varchar(255),
  nick_name                     varchar(255),
  phone_number                  varchar(255),
  car_no                        varchar(255),
  contact_number                varchar(255),
  create_time                   bigint not null,
  dealer_id                     bigint not null,
  second_dealer_id              bigint not null,
  dealer_type                   bigint not null,
  level                         integer not null,
  level_name                    varchar(255),
  station_id                    bigint not null,
  station_name                  varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  avatar                        varchar(255),
  user_type                     integer not null,
  logical_number                varchar(255),
  physical_number               varchar(255),
  card_password                 varchar(255),
  vin                           varchar(255),
  birthday                      bigint not null,
  birthday_month                integer not null,
  birthday_day                  integer not null,
  barcode_img_url               varchar(255),
  open_id                       varchar(255),
  session_key                   varchar(255),
  union_id                      varchar(255),
  id_card_no                    varchar(255),
  filter                        varchar(255),
  note                          varchar(255),
  user_note                     varchar(255),
  dealer_level                  bigint not null,
  sex                           integer not null,
  receiver_status               integer not null,
  login_count                   integer not null,
  group_id                      bigint not null,
  group_name                    varchar(255),
  dept_id                       bigint not null,
  update_time                   bigint not null,
  constraint pk_v1_member primary key (id)
);

create table cp_menu (
  id                            integer auto_increment not null,
  sort                          integer not null,
  parent_id                     integer not null,
  enable                        tinyint(1) default 0 not null,
  hidden                        tinyint(1) default 0 not null,
  path                          varchar(255),
  name                          varchar(255),
  component                     varchar(255),
  redirect                      varchar(255),
  title                         varchar(255),
  icon                          varchar(255),
  no_cache                      tinyint(1) default 0 not null,
  relative_path                 varchar(255),
  active_menu                   varchar(255),
  create_time                   bigint not null,
  constraint pk_cp_menu primary key (id)
);

create table v1_operation_log (
  id                            bigint auto_increment not null,
  admin_id                      bigint not null,
  org_id                        bigint not null,
  admin_name                    varchar(255),
  ip                            varchar(255),
  place                         varchar(255),
  note                          varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_operation_log primary key (id)
);

create table v1_system_config (
  id                            bigint auto_increment not null,
  config_key                    varchar(255),
  config_value                  varchar(255),
  note                          varchar(255),
  enable                        tinyint(1) default 0 not null,
  org_id                        bigint not null,
  tab_name                      varchar(255),
  org_name                      varchar(255),
  is_encrypt                    tinyint(1) default 0 not null,
  content_type                  integer not null,
  update_time                   bigint not null,
  constraint pk_v1_system_config primary key (id)
);

create table v1_system_config_template (
  id                            integer auto_increment not null,
  config_key                    varchar(255),
  config_value                  varchar(255),
  note                          varchar(255),
  enable                        tinyint(1) default 0 not null,
  is_encrypt                    tinyint(1) default 0 not null,
  update_time                   bigint not null,
  constraint pk_v1_system_config_template primary key (id)
);

create table v1_shop_admin (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  realname                      varchar(255),
  avatar                        varchar(255),
  password                      varchar(255),
  create_time                   bigint not null,
  last_time                     bigint not null,
  last_ip                       varchar(255),
  phone_number                  varchar(255),
  is_admin                      tinyint(1) default 0 not null,
  org_id                        bigint not null,
  org_name                      varchar(255),
  shop_id                       bigint not null,
  shop_name                     varchar(255),
  rules                         varchar(255),
  pinyin_abbr                   varchar(255),
  status                        integer not null,
  bg_img_url                    varchar(255),
  constraint pk_v1_shop_admin primary key (id)
);

create table v1_suggestion (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  uid                           bigint not null,
  org_id                        bigint not null,
  status                        integer not null,
  content                       varchar(255),
  note                          varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_suggestion primary key (id)
);


-- !Downs

-- drop all
drop table if exists cp_system_action;

drop table if exists v1_admin_config;

drop table if exists cp_group;

drop table if exists cp_group_action;

drop table if exists cp_group_menu;

drop table if exists cp_group_user;

drop table if exists cp_log;

drop table if exists v1_member;

drop table if exists cp_menu;

drop table if exists v1_operation_log;

drop table if exists v1_system_config;

drop table if exists v1_system_config_template;

drop table if exists v1_shop_admin;

drop table if exists v1_suggestion;

