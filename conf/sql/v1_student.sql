-- Created by EntityToSQLGenerator
-- Table structure for v1_student

-- !Ups

drop table if exists v1_student;
create table v1_student (
  org_id bigint,
  id bigint not null auto_increment,
  student_number varchar(255),
  name varchar(255),
  class_id bigint,
  grade integer,
  evaluation_scheme integer,
  class_average_score double,
  academic_score double,
  specialty_score double,
  habit_score double,
  total_score double,
  badges varchar(255),
  reward_rank_grade integer,
  reward_rank_school integer,
  create_time bigint,
  update_time bigint,
  constraint pk_v1_student primary key (id)
);

-- !Downs

