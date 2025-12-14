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
create table v1_academic_record (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  student_id                    bigint not null comment '学生ID',
  exam_type                     integer not null comment '考试类型',
  chinese_score                 double not null comment '语文成绩',
  math_score                    double not null comment '数学成绩',
  english_score                 double not null comment '英语成绩',
  average_score                 double not null comment '平均分',
  grade_ranking                 integer not null comment '年级排名',
  class_ranking                 integer not null comment '班级排名',
  progress_amount               integer not null comment '进步名次',
  progress_ranking              integer not null comment '进步排名',
  calculated_score              double not null comment '计算得分',
  badge_awarded                 varchar(255) comment '授予徽章',
  exam_date                     bigint not null comment '考试时间',
  create_time                   bigint not null comment '创建时间',
  update_time                   bigint not null comment '更新时间',
  constraint pk_v1_academic_record primary key (id)
) comment='学业成绩记录';

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

create table v1_badge_record (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  student_id                    bigint not null comment '学生ID',
  badge_type                    varchar(255) comment '徽章类型',
  award_reason                  varchar(255) comment '授予原因',
  award_time                    bigint not null comment '授予时间',
  award_period                  varchar(255) comment '授予周期',
  create_time                   bigint not null comment '创建时间',
  constraint pk_v1_badge_record primary key (id)
) comment='徽章授予记录';

create table v1_class_routine (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  class_id                      bigint not null comment '班级ID',
  week_number                   integer not null comment '周次',
  month                         integer not null comment '月份',
  year                          integer not null comment '年份',
  hygiene_score                 double not null comment '卫生得分',
  discipline_score              double not null comment '纪律得分',
  exercise_score                double not null comment '两操得分',
  manner_score                  double not null comment '文明礼仪得分',
  reading_score                 double not null comment '晨诵午读得分',
  total_score                   double not null comment '周总分',
  evaluator_id                  bigint not null comment '评分人ID',
  evaluator_name                varchar(255) comment '评分人姓名',
  evaluate_type                 integer not null comment '评分类型',
  comments                      varchar(255) comment '评语',
  record_time                   bigint not null comment '记录时间',
  create_time                   bigint not null comment '创建时间',
  update_time                   bigint not null comment '更新时间',
  constraint pk_v1_class_routine primary key (id)
) comment='班级常规评比';

create table v1_class_teacher_relation (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  class_id                      bigint not null comment '班级ID',
  teacher_id                    bigint not null comment '教师ID',
  subject                       varchar(255) comment '任教科目',
  is_head_teacher               tinyint(1) default 0 not null comment '是否班主任',
  create_time                   bigint not null comment '创建时间',
  update_time                   bigint not null comment '更新时间',
  constraint pk_v1_class_teacher_relation primary key (id)
) comment='班级教师关系表';

create table v1_evaluation_rule (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  rule_type                     varchar(255) comment '规则类型',
  conditions                    varchar(255) comment '条件',
  score                         double not null comment '得分',
  badge_type                    varchar(255) comment '徽章类型',
  description                   varchar(255) comment '描述',
  active                        tinyint(1) default 0 not null comment '是否启用',
  create_time                   bigint not null comment '创建时间',
  constraint pk_v1_evaluation_rule primary key (id)
) comment='评价规则配置';

create table cp_group (
  id                            integer auto_increment not null,
  name                          varchar(255),
  is_admin                      tinyint(1) default 0 not null,
  description                   varchar(255),
  create_time                   bigint not null,
  org_id                        bigint not null,
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

create table v1_habit_record (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  student_id                    bigint not null comment '学生ID',
  habit_type                    integer not null comment '习惯类型',
  evaluator_type                varchar(255) comment '评价者类型',
  evaluator_id                  bigint not null comment '评价者ID',
  score_change                  double not null comment '分数变化',
  description                   varchar(255) comment '行为描述',
  evidence_image                varchar(255) comment '证据图片',
  record_time                   bigint not null comment '记录时间',
  create_time                   bigint not null comment '创建时间',
  constraint pk_v1_habit_record primary key (id)
) comment='习惯评价记录';

create table v1_home_visit (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  teacher_id                    bigint not null comment '教师ID',
  class_id                      bigint not null comment '班级ID',
  student_id                    bigint not null comment '学生ID',
  visit_type                    integer not null comment '家访类型',
  record_content                varchar(255) comment '记录内容',
  case_study                    varchar(255) comment '优秀案例',
  case_level                    varchar(255) comment '案例评价等级',
  video_evidence                varchar(255) comment '视频证据',
  video_level                   varchar(255) comment '视频评价等级',
  base_score                    integer not null comment '基础分',
  bonus_score                   integer not null comment '加分',
  total_score                   integer not null comment '总分',
  status                        integer not null comment '状态',
  visit_time                    bigint not null comment '家访时间',
  create_time                   bigint not null comment '创建时间',
  constraint pk_v1_home_visit primary key (id)
) comment='家访工作记录';

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

create table v1_monthly_rating_quota (
  id                            bigint auto_increment not null,
  org_id                        bigint not null comment '机构ID',
  class_id                      bigint not null comment '班级ID',
  evaluator_id                  bigint not null comment '教师ID/评价者ID',
  role_type                     varchar(255) comment '身份：head/basic/other/parent/adm',
  month_key                     varchar(255) comment '月份yyyy-MM',
  rating_amount                 double not null comment '当月已用额度',
  cap_value                     double not null comment '当月上限冗余存储',
  create_time                   bigint not null comment '创建时间',
  update_time                   bigint not null comment '更新时间',
  constraint pk_v1_monthly_rating_quota primary key (id)
) comment='每月评分额度表';

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

create table v1_parent_student_relation (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  parent_id                     bigint not null comment '家长ID',
  student_id                    bigint not null comment '学生ID',
  relationship                  varchar(255) comment '关系类型',
  create_time                   bigint not null comment '创建时间',
  update_time                   bigint not null comment '更新时间',
  constraint pk_v1_parent_student_relation primary key (id)
) comment='家长学生关系表';

create table v1_school_class (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  class_name                    varchar(255) comment '班级名称',
  grade                         integer not null comment '年级',
  head_teacher_id               bigint not null comment '班主任ID',
  student_num                   integer not null comment '人数',
  academic_score                double not null comment '学业得分总分',
  specialty_score               double not null comment '特长得分总分',
  routine_score                 double not null comment '常规得分',
  home_visit_score              double not null comment '家访得分',
  total_score                   double not null comment '总分',
  disqualified                  tinyint(1) default 0 not null comment '一票否决',
  deduction_score               double not null comment '扣分',
  honor_title                   varchar(255) comment '荣誉称号',
  create_time                   bigint not null comment '创建时间',
  constraint pk_v1_school_class primary key (id)
) comment='班级信息';

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

create table v1_specialty_award (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  student_id                    bigint not null comment '学生ID',
  award_level                   integer not null comment '奖项级别',
  award_grade                   integer not null comment '奖项等级',
  competition_name              varchar(255) comment '竞赛名称',
  category                      varchar(255) comment '比赛类别',
  award_score                   double not null comment '奖项得分',
  status                        integer not null comment '审核状态',
  certificate_image             varchar(255) comment '证书图片',
  badge_awarded                 varchar(255) comment '授予徽章',
  award_date                    bigint not null comment '获奖时间',
  create_time                   bigint not null comment '创建时间',
  update_time                   bigint not null comment '更新时间',
  constraint pk_v1_specialty_award primary key (id)
) comment='特长获奖记录';

create table v1_student (
  id                            bigint auto_increment not null comment '唯一标识',
  org_id                        bigint not null comment '机构ID',
  student_number                varchar(255) comment '学号',
  name                          varchar(255) comment '学生姓名',
  class_id                      bigint not null comment '班级ID',
  grade                         integer not null comment '年级',
  evaluation_scheme             integer not null comment '评价方案',
  class_average_score           double not null comment '班级平均分',
  academic_score                double not null comment '学业得分',
  specialty_score               double not null comment '特长得分',
  habit_score                   double not null comment '习惯得分',
  total_score                   double not null comment '总分',
  badges                        varchar(255) comment '获得徽章',
  reward_rank_grade             integer not null comment '奖项年级排名',
  reward_rank_school            integer not null comment '奖项学校排名',
  create_time                   bigint not null comment '创建时间',
  update_time                   bigint not null comment '更新时间',
  constraint pk_v1_student primary key (id)
) comment='学生';

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
drop table if exists v1_academic_record;

drop table if exists cp_system_action;

drop table if exists v1_admin_config;

drop table if exists v1_badge_record;

drop table if exists v1_class_routine;

drop table if exists v1_class_teacher_relation;

drop table if exists v1_evaluation_rule;

drop table if exists cp_group;

drop table if exists cp_group_action;

drop table if exists cp_group_menu;

drop table if exists cp_group_user;

drop table if exists v1_habit_record;

drop table if exists v1_home_visit;

drop table if exists cp_log;

drop table if exists v1_member;

drop table if exists cp_menu;

drop table if exists v1_monthly_rating_quota;

drop table if exists v1_operation_log;

drop table if exists v1_system_config;

drop table if exists v1_system_config_template;

drop table if exists v1_parent_student_relation;

drop table if exists v1_school_class;

drop table if exists v1_shop_admin;

drop table if exists v1_specialty_award;

drop table if exists v1_student;

drop table if exists v1_suggestion;

