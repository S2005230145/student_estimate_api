/*
 Navicat Premium Data Transfer

 Source Server         : 120.48.81.209
 Source Server Type    : MySQL
 Source Server Version : 80044
 Source Host           : 120.48.81.209:3306
 Source Schema         : student_estimate

 Target Server Type    : MySQL
 Target Server Version : 80044
 File Encoding         : 65001

 Date: 12/12/2025 15:10:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cp_group
-- ----------------------------
DROP TABLE IF EXISTS `cp_group`;
CREATE TABLE `cp_group`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cp_group
-- ----------------------------

-- ----------------------------
-- Table structure for cp_group_action
-- ----------------------------
DROP TABLE IF EXISTS `cp_group_action`;
CREATE TABLE `cp_group_action`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` int NOT NULL,
  `system_action_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cp_group_action
-- ----------------------------

-- ----------------------------
-- Table structure for cp_group_menu
-- ----------------------------
DROP TABLE IF EXISTS `cp_group_menu`;
CREATE TABLE `cp_group_menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `menu_id` int NOT NULL,
  `group_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cp_group_menu
-- ----------------------------

-- ----------------------------
-- Table structure for cp_group_user
-- ----------------------------
DROP TABLE IF EXISTS `cp_group_user`;
CREATE TABLE `cp_group_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` int NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `member_id` bigint NOT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cp_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for cp_log
-- ----------------------------
DROP TABLE IF EXISTS `cp_log`;
CREATE TABLE `cp_log`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `log_unique` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `log_sym_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `log_mer_id` int NOT NULL,
  `log_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `log_created` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cp_log
-- ----------------------------

-- ----------------------------
-- Table structure for cp_menu
-- ----------------------------
DROP TABLE IF EXISTS `cp_menu`;
CREATE TABLE `cp_menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sort` int NOT NULL,
  `parent_id` int NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `hidden` tinyint(1) NOT NULL DEFAULT 0,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `no_cache` tinyint(1) NOT NULL DEFAULT 0,
  `relative_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `active_menu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cp_menu
-- ----------------------------

-- ----------------------------
-- Table structure for cp_system_action
-- ----------------------------
DROP TABLE IF EXISTS `cp_system_action`;
CREATE TABLE `cp_system_action`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `action_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `action_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `module_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `module_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `need_show` tinyint(1) NOT NULL DEFAULT 0,
  `display_order` int NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cp_system_action
-- ----------------------------

-- ----------------------------
-- Table structure for v1_academic_record
-- ----------------------------
DROP TABLE IF EXISTS `v1_academic_record`;
CREATE TABLE `v1_academic_record`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `exam_type` int NOT NULL,
  `chinese_score` double NOT NULL,
  `math_score` double NOT NULL,
  `english_score` double NOT NULL,
  `average_score` double NOT NULL,
  `grade_ranking` int NOT NULL,
  `class_ranking` int NOT NULL,
  `progress_amount` int NOT NULL,
  `progress_ranking` int NOT NULL,
  `calculated_score` double NOT NULL,
  `badge_awarded` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `exam_date` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `chinese_math_average_score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_org_id_id_desc`(`org_id` ASC, `id` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 249 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_academic_record
-- ----------------------------
INSERT INTO `v1_academic_record` VALUES (0, 200, 9, 1, 97.5, 92, 95, 94.83, 1, 1, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844432, 1762334844476, 0);
INSERT INTO `v1_academic_record` VALUES (0, 201, 7, 1, 90.5, 96, 93, 93.17, 2, 1, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844426, 1762334844477, 0);
INSERT INTO `v1_academic_record` VALUES (0, 202, 14, 1, 93, 87.5, 90.5, 90.33, 3, 1, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844448, 1762334844478, 0);
INSERT INTO `v1_academic_record` VALUES (0, 203, 5, 1, 90, 85.5, 88, 87.83, 4, 2, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844421, 1762334844479, 0);
INSERT INTO `v1_academic_record` VALUES (0, 204, 12, 1, 86, 88.5, 87, 87.17, 5, 2, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844443, 1762334844480, 0);
INSERT INTO `v1_academic_record` VALUES (0, 205, 16, 1, 84.5, 90, 87, 87.17, 6, 2, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844452, 1762334844481, 0);
INSERT INTO `v1_academic_record` VALUES (0, 206, 3, 1, 82, 90.5, 85, 85.83, 7, 3, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844417, 1762334844482, 0);
INSERT INTO `v1_academic_record` VALUES (0, 207, 4, 1, 80.5, 87, 83.5, 83.67, 8, 4, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844419, 1762334844483, 0);
INSERT INTO `v1_academic_record` VALUES (0, 208, 13, 1, 80, 84.5, 82, 82.17, 9, 3, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844445, 1762334844484, 0);
INSERT INTO `v1_academic_record` VALUES (0, 209, 17, 1, 77, 81.5, 79, 79.17, 10, 4, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844454, 1762334844485, 0);
INSERT INTO `v1_academic_record` VALUES (0, 210, 10, 1, 79, 76.5, 78, 77.83, 11, 3, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844435, 1762334844486, 0);
INSERT INTO `v1_academic_record` VALUES (0, 211, 6, 1, 74, 78.5, 76, 76.17, 12, 5, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844424, 1762334844487, 0);
INSERT INTO `v1_academic_record` VALUES (0, 212, 11, 1, 73.5, 79, 76, 76.17, 13, 4, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844439, 1762334844488, 0);
INSERT INTO `v1_academic_record` VALUES (0, 213, 15, 1, 67, 73.5, 70, 70.17, 14, 5, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844450, 1762334844489, 0);
INSERT INTO `v1_academic_record` VALUES (0, 214, 8, 1, 62.5, 70, 66, 66.17, 15, 5, 0, 0, 40, '星辰徽章', 1702569600000, 1762334844429, 1762334844490, 0);
INSERT INTO `v1_academic_record` VALUES (0, 215, 16, 0, 87, 92.5, 89.5, 89.67, 4, 1, 2, 1, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852676, 1762334852696, 0);
INSERT INTO `v1_academic_record` VALUES (0, 216, 10, 0, 82.5, 79, 85.5, 82.33, 9, 3, 2, 2, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852664, 1762334852696, 0);
INSERT INTO `v1_academic_record` VALUES (0, 217, 5, 0, 92.5, 88, 90.5, 90.33, 3, 2, 1, 3, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852654, 1762334852697, 0);
INSERT INTO `v1_academic_record` VALUES (0, 218, 3, 0, 85.5, 92, 88.5, 88.67, 6, 3, 1, 4, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852650, 1762334852698, 0);
INSERT INTO `v1_academic_record` VALUES (0, 219, 13, 0, 83.5, 87, 85, 85.17, 8, 3, 1, 5, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852670, 1762334852699, 0);
INSERT INTO `v1_academic_record` VALUES (0, 220, 9, 0, 95, 89.5, 93, 92.5, 1, 1, 0, 6, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852662, 1762334852700, 0);
INSERT INTO `v1_academic_record` VALUES (0, 221, 7, 0, 88, 94.5, 91, 91.17, 2, 1, 0, 7, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852658, 1762334852701, 0);
INSERT INTO `v1_academic_record` VALUES (0, 222, 12, 0, 89, 91.5, 87, 89.17, 5, 2, 0, 8, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852668, 1762334852702, 0);
INSERT INTO `v1_academic_record` VALUES (0, 223, 6, 0, 76.5, 81, 79.5, 79, 12, 5, 0, 9, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852656, 1762334852703, 0);
INSERT INTO `v1_academic_record` VALUES (0, 224, 11, 0, 71, 76.5, 73.5, 73.67, 13, 4, 0, 10, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852667, 1762334852703, 0);
INSERT INTO `v1_academic_record` VALUES (0, 225, 15, 0, 69.5, 75, 72, 72.17, 14, 5, 0, 11, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852674, 1762334852704, 0);
INSERT INTO `v1_academic_record` VALUES (0, 226, 8, 0, 65, 72.5, 68, 68.5, 15, 5, 0, 12, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852660, 1762334852706, 0);
INSERT INTO `v1_academic_record` VALUES (0, 227, 17, 0, 79.5, 83, 81, 81.17, 11, 4, -1, 13, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852679, 1762334852706, 0);
INSERT INTO `v1_academic_record` VALUES (0, 228, 4, 0, 78, 85.5, 82, 81.83, 10, 4, -2, 14, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852652, 1762334852707, 0);
INSERT INTO `v1_academic_record` VALUES (0, 229, 14, 0, 91.5, 84, 88.5, 88, 7, 2, -4, 15, 40, '星辰徽章,星火徽章', 1705248000000, 1762334852672, 1762334852708, 0);
INSERT INTO `v1_academic_record` VALUES (0, 230, 16, 1, 90.5, 95, 92.5, 92.67, 1, 1, 3, 1, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858449, 1762334858472, 0);
INSERT INTO `v1_academic_record` VALUES (0, 231, 12, 1, 91.5, 93, 92, 92.17, 3, 1, 2, 2, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858440, 1762334858473, 0);
INSERT INTO `v1_academic_record` VALUES (0, 232, 3, 1, 88, 95.5, 90, 91.17, 4, 2, 2, 3, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858419, 1762334858474, 0);
INSERT INTO `v1_academic_record` VALUES (0, 233, 13, 1, 86, 90.5, 88, 88.17, 6, 2, 2, 4, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858442, 1762334858475, 0);
INSERT INTO `v1_academic_record` VALUES (0, 234, 17, 1, 82, 86.5, 84, 84.17, 9, 4, 2, 5, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858451, 1762334858476, 0);
INSERT INTO `v1_academic_record` VALUES (0, 235, 5, 1, 94, 90.5, 92.5, 92.33, 2, 1, 1, 6, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858424, 1762334858477, 0);
INSERT INTO `v1_academic_record` VALUES (0, 236, 6, 1, 80, 85.5, 82, 82.5, 11, 4, 1, 7, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858426, 1762334858478, 0);
INSERT INTO `v1_academic_record` VALUES (0, 237, 15, 1, 74, 80.5, 76.5, 77, 13, 5, 1, 8, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858447, 1762334858479, 0);
INSERT INTO `v1_academic_record` VALUES (0, 238, 8, 1, 72, 78.5, 74.5, 75, 14, 4, 1, 9, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858431, 1762334858480, 0);
INSERT INTO `v1_academic_record` VALUES (0, 239, 14, 1, 89.5, 82, 86, 85.83, 8, 3, -1, 10, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858445, 1762334858481, 0);
INSERT INTO `v1_academic_record` VALUES (0, 240, 10, 1, 85, 82.5, 84, 83.83, 10, 3, -1, 11, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858436, 1762334858482, 0);
INSERT INTO `v1_academic_record` VALUES (0, 241, 4, 1, 75.5, 82, 78.5, 78.67, 12, 5, -2, 12, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858422, 1762334858483, 0);
INSERT INTO `v1_academic_record` VALUES (0, 242, 11, 1, 68.5, 74, 70.5, 71, 15, 5, -2, 13, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858438, 1762334858484, 0);
INSERT INTO `v1_academic_record` VALUES (0, 243, 9, 1, 92.5, 87, 90, 89.83, 5, 2, -4, 14, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858433, 1762334858485, 0);
INSERT INTO `v1_academic_record` VALUES (0, 244, 7, 1, 85.5, 90, 87.5, 87.67, 7, 3, -5, 15, 40, '星辰徽章,星火徽章', 1718812800000, 1762334858429, 1762334858486, 0);
INSERT INTO `v1_academic_record` VALUES (0, 246, 42, 0, 90, 61, 85, 78.67, 1, 1, 0, 0, 40, '星辰徽章', 1718899200000, 1765258325128, 1765258326200, 0);
INSERT INTO `v1_academic_record` VALUES (0, 247, 41, 0, 80, 71, 83, 78, 2, 2, 0, 0, 40, '星辰徽章', 1718899200000, 1765258324702, 1765258326411, 0);
INSERT INTO `v1_academic_record` VALUES (0, 248, 40, 0, 89.5, 70.5, 55.5, 71.83, 3, 3, 0, 0, 40, '星辰徽章', 1718899200000, 1765258324274, 1765258326624, 0);

-- ----------------------------
-- Table structure for v1_admin_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_admin_config`;
CREATE TABLE `v1_admin_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `is_encrypt` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_admin_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_badge_record
-- ----------------------------
DROP TABLE IF EXISTS `v1_badge_record`;
CREATE TABLE `v1_badge_record`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `badge_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `award_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `award_time` bigint NOT NULL,
  `award_period` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_badge_record
-- ----------------------------

-- ----------------------------
-- Table structure for v1_class_routine
-- ----------------------------
DROP TABLE IF EXISTS `v1_class_routine`;
CREATE TABLE `v1_class_routine`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `class_id` bigint NOT NULL,
  `week_number` int NOT NULL,
  `month` int NOT NULL,
  `hygiene_score` double NOT NULL,
  `discipline_score` double NOT NULL,
  `exercise_score` double NOT NULL,
  `manner_score` double NOT NULL,
  `reading_score` double NOT NULL,
  `total_score` double NOT NULL,
  `record_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NULL DEFAULT NULL,
  `comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluator_id` bigint NULL DEFAULT NULL,
  `evaluator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluate_type` int NULL DEFAULT NULL,
  `year` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_class_routine
-- ----------------------------
INSERT INTO `v1_class_routine` VALUES (0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1765348939000, 1765348941024, 1765348941024, '123', 21, '', 0, 0);
INSERT INTO `v1_class_routine` VALUES (0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1765349150000, 1765349150613, 1765349150613, 'dggggg', 21, '', 0, 0);
INSERT INTO `v1_class_routine` VALUES (0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1765987200000, 1765349240208, 1765349240208, 'asd', 21, '', 0, 0);

-- ----------------------------
-- Table structure for v1_class_teacher_relation
-- ----------------------------
DROP TABLE IF EXISTS `v1_class_teacher_relation`;
CREATE TABLE `v1_class_teacher_relation`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `class_id` bigint NOT NULL COMMENT '班级ID',
  `teacher_id` bigint NOT NULL COMMENT '教师ID',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任教科目',
  `is_head_teacher` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否班主任',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '班级教师关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_class_teacher_relation
-- ----------------------------
INSERT INTO `v1_class_teacher_relation` VALUES (0, 1, 3, 3, '语文', 1, 1765336736375, 1765336736375);
INSERT INTO `v1_class_teacher_relation` VALUES (1, 3, 3, 1, '数学', 0, 1765349667597, 1765349667597);
INSERT INTO `v1_class_teacher_relation` VALUES (1, 4, 5, 1, '数学', 1, 1765354562417, 1765354562417);

-- ----------------------------
-- Table structure for v1_evaluation_rule
-- ----------------------------
DROP TABLE IF EXISTS `v1_evaluation_rule`;
CREATE TABLE `v1_evaluation_rule`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `conditions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `score` double NOT NULL,
  `badge_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_evaluation_rule
-- ----------------------------

-- ----------------------------
-- Table structure for v1_habit_record
-- ----------------------------
DROP TABLE IF EXISTS `v1_habit_record`;
CREATE TABLE `v1_habit_record`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `habit_type` int NOT NULL,
  `evaluator_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluator_id` bigint NOT NULL,
  `score_change` double NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evidence_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `record_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_habit_record
-- ----------------------------
INSERT INTO `v1_habit_record` VALUES (0, 3, 30, 0, 'parent', 0, 1, '1', NULL, 111, 1762766409688);
INSERT INTO `v1_habit_record` VALUES (0, 4, 30, 0, 'parent', 0, 1, '1', NULL, 111, 1762766507813);
INSERT INTO `v1_habit_record` VALUES (0, 5, 30, 0, 'parent', 19, 1, '1', NULL, 111, 1762766691661);
INSERT INTO `v1_habit_record` VALUES (0, 6, 30, 0, 'parent', 19, 1, '1', NULL, 111, 1762766726142);
INSERT INTO `v1_habit_record` VALUES (1, 7, 13, 0, 'teacher', 3, 1, '上课表现好', '', 1765267908291, 1765267980959);

-- ----------------------------
-- Table structure for v1_home_visit
-- ----------------------------
DROP TABLE IF EXISTS `v1_home_visit`;
CREATE TABLE `v1_home_visit`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint NOT NULL,
  `class_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `visit_type` int NOT NULL,
  `record_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `case_study` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `video_evidence` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `base_score` int NOT NULL,
  `bonus_score` int NOT NULL,
  `total_score` int NOT NULL,
  `status` int NOT NULL,
  `visit_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `case_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `video_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_home_visit
-- ----------------------------
INSERT INTO `v1_home_visit` VALUES (0, 1, 3, 1, 30, 0, '无', '无', 'video1.mp4', 10, 10, 20, 10, 1765268663, 1765268663, '优秀', '优秀');
INSERT INTO `v1_home_visit` VALUES (0, 2, 1, 1, 31, 1, '无', '无', 'video2.mp4', 10, 2, 12, 0, 1765268831, 1765268831, '良好', '良好');

-- ----------------------------
-- Table structure for v1_member
-- ----------------------------
DROP TABLE IF EXISTS `v1_member`;
CREATE TABLE `v1_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `login_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pay_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `car_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `dealer_id` bigint NOT NULL,
  `second_dealer_id` bigint NOT NULL,
  `dealer_type` bigint NOT NULL,
  `level` int NOT NULL,
  `level_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `station_id` bigint NOT NULL,
  `station_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_type` int NOT NULL,
  `logical_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `physical_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `card_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `vin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `birthday` bigint NOT NULL,
  `birthday_month` int NOT NULL,
  `birthday_day` int NOT NULL,
  `barcode_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `session_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `id_card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dealer_level` bigint NOT NULL,
  `sex` int NOT NULL,
  `receiver_status` int NOT NULL,
  `login_count` int NOT NULL,
  `group_id` bigint NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dept_id` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_member
-- ----------------------------

-- ----------------------------
-- Table structure for v1_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `v1_operation_log`;
CREATE TABLE `v1_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `admin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for v1_parent_student_relation
-- ----------------------------
DROP TABLE IF EXISTS `v1_parent_student_relation`;
CREATE TABLE `v1_parent_student_relation`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `parent_id` bigint NOT NULL COMMENT '家长ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `relationship` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关系类型',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '家长学生关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_parent_student_relation
-- ----------------------------
INSERT INTO `v1_parent_student_relation` VALUES (0, 1, 4, 20, '父亲', 1762744056607, 1762744056607);
INSERT INTO `v1_parent_student_relation` VALUES (0, 2, 5, 20, '母亲', 1762744056632, 1762744056632);
INSERT INTO `v1_parent_student_relation` VALUES (0, 3, 6, 21, '母亲', 1762744056671, 1762744056671);
INSERT INTO `v1_parent_student_relation` VALUES (0, 4, 7, 22, '爷爷', 1762744056706, 1762744056706);
INSERT INTO `v1_parent_student_relation` VALUES (0, 5, 8, 22, '奶奶', 1762744056732, 1762744056732);
INSERT INTO `v1_parent_student_relation` VALUES (0, 6, 9, 23, '父亲', 1762744056772, 1762744056772);
INSERT INTO `v1_parent_student_relation` VALUES (0, 7, 10, 24, '母亲', 1762744056813, 1762744056813);
INSERT INTO `v1_parent_student_relation` VALUES (0, 8, 11, 24, '外公', 1762744056837, 1762744056837);
INSERT INTO `v1_parent_student_relation` VALUES (0, 9, 12, 25, '父亲', 1762744056878, 1762744056878);
INSERT INTO `v1_parent_student_relation` VALUES (0, 10, 13, 25, '外婆', 1762744056902, 1762744056902);
INSERT INTO `v1_parent_student_relation` VALUES (0, 11, 14, 26, '奶奶', 1762744056936, 1762744056936);
INSERT INTO `v1_parent_student_relation` VALUES (0, 12, 15, 27, '父亲', 1762744056971, 1762744056971);
INSERT INTO `v1_parent_student_relation` VALUES (0, 13, 16, 27, '母亲', 1762744057072, 1762744057072);
INSERT INTO `v1_parent_student_relation` VALUES (0, 14, 17, 28, '叔叔', 1762744057120, 1762744057120);
INSERT INTO `v1_parent_student_relation` VALUES (0, 15, 18, 29, '阿姨', 1762744057162, 1762744057162);
INSERT INTO `v1_parent_student_relation` VALUES (0, 16, 4, 30, '父亲', 1762744323419, 1762744323419);
INSERT INTO `v1_parent_student_relation` VALUES (0, 17, 5, 30, '母亲', 1762744323432, 1762744323432);
INSERT INTO `v1_parent_student_relation` VALUES (0, 18, 6, 31, '母亲', 1762744323459, 1762744323459);
INSERT INTO `v1_parent_student_relation` VALUES (0, 19, 7, 32, '爷爷', 1762744323483, 1762744323483);
INSERT INTO `v1_parent_student_relation` VALUES (0, 20, 8, 32, '奶奶', 1762744323495, 1762744323495);
INSERT INTO `v1_parent_student_relation` VALUES (0, 21, 9, 33, '父亲', 1762744323519, 1762744323519);
INSERT INTO `v1_parent_student_relation` VALUES (0, 22, 10, 34, '母亲', 1762744323545, 1762744323545);
INSERT INTO `v1_parent_student_relation` VALUES (0, 23, 11, 34, '外公', 1762744323555, 1762744323555);
INSERT INTO `v1_parent_student_relation` VALUES (0, 24, 12, 35, '父亲', 1762744323580, 1762744323580);
INSERT INTO `v1_parent_student_relation` VALUES (0, 25, 13, 35, '外婆', 1762744323592, 1762744323592);
INSERT INTO `v1_parent_student_relation` VALUES (0, 26, 14, 36, '奶奶', 1762744323618, 1762744323618);
INSERT INTO `v1_parent_student_relation` VALUES (0, 27, 15, 37, '父亲', 1762744323644, 1762744323644);
INSERT INTO `v1_parent_student_relation` VALUES (0, 28, 16, 37, '母亲', 1762744323655, 1762744323655);
INSERT INTO `v1_parent_student_relation` VALUES (0, 29, 17, 38, '叔叔', 1762744323683, 1762744323683);
INSERT INTO `v1_parent_student_relation` VALUES (0, 30, 18, 39, '阿姨', 1762744323709, 1762744323709);
INSERT INTO `v1_parent_student_relation` VALUES (0, 31, 19, 30, '爷爷', 1762766166678, 1762766166678);
INSERT INTO `v1_parent_student_relation` VALUES (0, 32, 23, 45, '父亲', 1765271852641, 1765271852641);
INSERT INTO `v1_parent_student_relation` VALUES (0, 33, 26, 49, '父亲', 1765359732953, 1765359732953);
INSERT INTO `v1_parent_student_relation` VALUES (0, 34, 24, 49, '母亲', 1765359733932, 1765359733932);
INSERT INTO `v1_parent_student_relation` VALUES (0, 35, 26, 50, '父亲', 1765359908072, 1765359908072);
INSERT INTO `v1_parent_student_relation` VALUES (0, 36, 24, 50, '母亲', 1765359908990, 1765359908990);
INSERT INTO `v1_parent_student_relation` VALUES (0, 37, 26, 51, '父亲', 1765360841035, 1765360841035);
INSERT INTO `v1_parent_student_relation` VALUES (0, 38, 24, 51, '母亲', 1765360841770, 1765360841770);
INSERT INTO `v1_parent_student_relation` VALUES (0, 39, 26, 52, '父亲', 1765442570250, 1765442571210);
INSERT INTO `v1_parent_student_relation` VALUES (0, 40, 24, 52, '母亲', 1765442824205, 1765442824205);
INSERT INTO `v1_parent_student_relation` VALUES (0, 41, 26, 53, '父亲', 1765442950005, 1765442950005);
INSERT INTO `v1_parent_student_relation` VALUES (0, 42, 24, 53, '母亲', 1765442950785, 1765442950785);

-- ----------------------------
-- Table structure for v1_school_class
-- ----------------------------
DROP TABLE IF EXISTS `v1_school_class`;
CREATE TABLE `v1_school_class`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `grade` int NOT NULL,
  `head_teacher_id` bigint NOT NULL,
  `academic_score` double NOT NULL,
  `specialty_score` double NOT NULL,
  `routine_score` double NOT NULL,
  `home_visit_score` double NOT NULL,
  `total_score` double NOT NULL,
  `disqualified` tinyint(1) NOT NULL DEFAULT 0,
  `honor_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `teacher_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_num` int NULL DEFAULT 0,
  `deduction_score` double NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_school_class
-- ----------------------------
INSERT INTO `v1_school_class` VALUES (0, 3, '2', 1, 1, 1, 0, 1, 1, 1, 0, '1', 1, '[1,2,3]', 9, 0);

-- ----------------------------
-- Table structure for v1_shop_admin
-- ----------------------------
DROP TABLE IF EXISTS `v1_shop_admin`;
CREATE TABLE `v1_shop_admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `last_time` bigint NOT NULL,
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `org_id` bigint NOT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pinyin_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `bg_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_shop_admin
-- ----------------------------
INSERT INTO `v1_shop_admin` VALUES (1, '11111111111', '11111111111', NULL, 'd77c758f63238837b49b725a7477a594', 1749890764186, 1749890764186, '0:0:0:0:0:0:0:1', '11111111111', 0, 1, NULL, 0, NULL, '科任老师,科任教师', NULL, 1, NULL);
INSERT INTO `v1_shop_admin` VALUES (3, '15005049460', '15005049460', NULL, 'd77c758f63238837b49b725a7477a594', 1752053198788, 1752053198788, '0:0:0:0:0:0:0:1', NULL, 1, 1, NULL, 0, NULL, '班主任 ', '15005049460', 1, NULL);
INSERT INTO `v1_shop_admin` VALUES (4, '13800138001', '张三-父亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138001', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (5, '13800138002', '张三-母亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138002', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (6, '13800138003', '李四-母亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138003', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (7, '13800138004', '王五-爷爷', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138004', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (8, '13800138005', '王五-奶奶', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138005', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (9, '13800138006', '赵六-父亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138006', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (10, '13800138007', '钱七-母亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138007', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (11, '13800138008', '钱七-外公', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138008', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (12, '13800138009', '孙八-父亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138009', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (13, '13800138010', '孙八-外婆', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138010', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (14, '13800138011', '周九-奶奶', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138011', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (15, '13800138012', '吴十-父亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138012', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (16, '13800138013', '吴十-母亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138013', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (17, '13800138014', '郑十一-叔叔', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138014', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (18, '13800138015', '王十二-阿姨', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13800138015', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (19, '15005049461', '张三-爷爷', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '15005049461', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (20, '15005049462', '15005049462', NULL, 'd77c758f63238837b49b725a7477a594', 1752053198788, 1752053198788, '0:0:0:0:0:0:0:1', NULL, 0, 1, NULL, 0, NULL, '科任老师,科任教师', '15005049462', 1, NULL);
INSERT INTO `v1_shop_admin` VALUES (21, '18850793837', '18850793837', NULL, 'd77c758f63238837b49b725a7477a594', 1765178527479, 1765178527479, '112.51.18.109', '18850793837', 1, 1, NULL, 0, NULL, '管理员,德育处,教务处', NULL, 1, NULL);
INSERT INTO `v1_shop_admin` VALUES (22, '13625063671', '13625063671', NULL, 'd77c758f63238837b49b725a7477a594', 1765178539091, 1765178539091, '112.51.18.109', '13625063671', 1, 1, NULL, 0, NULL, '管理员,德育处,教务处', NULL, 1, NULL);
INSERT INTO `v1_shop_admin` VALUES (23, '13825067672', '王小小-父亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13825067672', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);
INSERT INTO `v1_shop_admin` VALUES (24, '13112345678', 'jiang-母亲', NULL, 'd77c758f63238837b49b725a7477a594', 1765343987075, 1765343987075, '112.51.18.109', '13112345678', 0, 1, NULL, 0, NULL, '德育处', NULL, 1, NULL);
INSERT INTO `v1_shop_admin` VALUES (25, '13212345678', '13212345678', NULL, 'd77c758f63238837b49b725a7477a594', 1765343987075, 1765343987075, '112.51.18.109', '13212345678', 0, 1, NULL, 0, NULL, '教务处', NULL, 1, NULL);
INSERT INTO `v1_shop_admin` VALUES (26, '13012345678', 'jiang-父亲', NULL, 'd77c758f63238837b49b725a7477a594', 0, 0, NULL, '13012345678', 0, 0, NULL, 0, NULL, '家长', NULL, 0, NULL);

-- ----------------------------
-- Table structure for v1_specialty_award
-- ----------------------------
DROP TABLE IF EXISTS `v1_specialty_award`;
CREATE TABLE `v1_specialty_award`  (
  `org_id` bigint NOT NULL DEFAULT 0,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `award_level` int NOT NULL,
  `award_grade` int NOT NULL,
  `competition_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `award_score` double NOT NULL,
  `status` int NOT NULL,
  `certificate_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `badge_awarded` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `award_date` bigint NULL DEFAULT 0,
  `update_time` bigint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_specialty_award
-- ----------------------------
INSERT INTO `v1_specialty_award` VALUES (0, 1, 5, 0, 0, '全国数学奥林匹克竞赛', '学科竞赛', 0, 1, 'cert1.jpg', NULL, 1704067200000, 1704067200000, 1704067200000);
INSERT INTO `v1_specialty_award` VALUES (0, 2, 5, 1, 1, '省科技创新大赛', '科技创新', 0, 1, 'cert2.jpg', NULL, 1703980800000, 1703980800000, 1703980800000);
INSERT INTO `v1_specialty_award` VALUES (0, 3, 5, 2, 0, '市艺术节绘画比赛', '艺术', 0, 1, 'cert3.jpg', NULL, 1703894400000, 1703894400000, 1703894400000);
INSERT INTO `v1_specialty_award` VALUES (0, 4, 5, 3, 2, '区运动会田径比赛', '体育', 0, 1, 'cert4.jpg', NULL, 1703808000000, 1703808000000, 1703808000000);
INSERT INTO `v1_specialty_award` VALUES (0, 5, 5, 4, 0, '校作文比赛', '学科竞赛', 0, 1, 'cert5.jpg', NULL, 1703721600000, 1703721600000, 1703721600000);
INSERT INTO `v1_specialty_award` VALUES (0, 6, 6, 0, 1, '全国英语演讲比赛', '学科竞赛', 0, 1, 'cert6.jpg', NULL, 1704067200000, 1704067200000, 1704067200000);
INSERT INTO `v1_specialty_award` VALUES (0, 7, 6, 1, 0, '省物理竞赛', '学科竞赛', 0, 1, 'cert7.jpg', NULL, 1703980800000, 1703980800000, 1703980800000);
INSERT INTO `v1_specialty_award` VALUES (0, 8, 6, 2, 3, '市书法比赛', '艺术', 0, 1, 'cert8.jpg', NULL, 1703894400000, 1703894400000, 1703894400000);
INSERT INTO `v1_specialty_award` VALUES (0, 9, 3, 0, 4, '全国机器人竞赛集体奖', '科技创新', 20, 1, 'cert9.jpg', NULL, 1704067200000, 1704067200000, 1704067200000);
INSERT INTO `v1_specialty_award` VALUES (0, 10, 3, 1, 2, '省合唱比赛', '艺术', 0, 1, 'cert10.jpg', NULL, 1703980800000, 1703980800000, 1703980800000);
INSERT INTO `v1_specialty_award` VALUES (0, 11, 3, 4, 1, '校篮球比赛', '体育', 0, 1, 'cert11.jpg', NULL, 1703894400000, 1703894400000, 1703894400000);
INSERT INTO `v1_specialty_award` VALUES (0, 12, 4, 0, 0, '全国信息学竞赛', '学科竞赛', 0, 1, 'cert12.jpg', NULL, 1704067200000, 1704067200000, 1704067200000);
INSERT INTO `v1_specialty_award` VALUES (0, 13, 4, 2, 0, '市舞蹈比赛', '艺术', 0, 1, 'cert13.jpg', NULL, 1703980800000, 1703980800000, 1703980800000);
INSERT INTO `v1_specialty_award` VALUES (0, 14, 5, 1, 0, '省作文比赛', '学科竞赛', 0, 0, 'cert14.jpg', NULL, 1704067200000, 1704067200000, 1704067200000);

-- ----------------------------
-- Table structure for v1_student
-- ----------------------------
DROP TABLE IF EXISTS `v1_student`;
CREATE TABLE `v1_student`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `class_id` bigint NOT NULL,
  `grade` int NOT NULL,
  `evaluation_scheme` int NOT NULL,
  `class_average_score` double NOT NULL,
  `academic_score` double NOT NULL,
  `specialty_score` double NOT NULL,
  `habit_score` double NOT NULL,
  `total_score` double NOT NULL,
  `badges` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `reward_rank_grade` int NULL DEFAULT NULL,
  `reward_rank_school` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_student
-- ----------------------------
INSERT INTO `v1_student` VALUES (13, '2024011', '冯十三', 3, 3, 0, 76.17, 40, 0, 21, 0, '星辰徽章,星火徽章', 1762324132579, 1765267982494, 0, 0, 0);
INSERT INTO `v1_student` VALUES (14, '2024012', '陈十四', 3, 3, 0, 76.17, 40, 0, 0, 0, '星辰徽章,星火徽章', 1762324132593, 1765258327672, 0, 0, 0);
INSERT INTO `v1_student` VALUES (15, '2024013', '褚十五', 3, 3, 0, 76.17, 40, 0, 0, 0, '星辰徽章,星火徽章', 1762324132604, 1765258327672, 0, 0, 0);
INSERT INTO `v1_student` VALUES (16, '2024014', '卫十六', 3, 3, 0, 76.17, 40, 0, 0, 0, '星辰徽章,星火徽章', 1762324132618, 1765258327672, 0, 0, 0);
INSERT INTO `v1_student` VALUES (17, '2024015', '蒋十七', 3, 3, 0, 76.17, 40, 0, 0, 0, '星辰徽章,星火徽章', 1762324132630, 1765258327672, 0, 0, 0);
INSERT INTO `v1_student` VALUES (30, '2024001', '张三', 1, 1, 0, 0, 0, 0, 20, 0, NULL, 1762744323400, 1762766726157, 0, 0, 0);
INSERT INTO `v1_student` VALUES (31, '2024002', '李四', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323444, 1762744323444, 0, 0, 0);
INSERT INTO `v1_student` VALUES (32, '2024003', '王五', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323471, 1762744323471, 0, 0, 0);
INSERT INTO `v1_student` VALUES (33, '2024004', '赵六', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323507, 1762744323507, 0, 0, 0);
INSERT INTO `v1_student` VALUES (34, '2024005', '钱七', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323531, 1762744323531, 0, 0, 0);
INSERT INTO `v1_student` VALUES (35, '2024006', '孙八', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323567, 1762744323567, 0, 0, 0);
INSERT INTO `v1_student` VALUES (36, '2024007', '周九', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323604, 1762744323604, 0, 0, 0);
INSERT INTO `v1_student` VALUES (37, '2024008', '吴十', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323631, 1762744323631, 0, 0, 0);
INSERT INTO `v1_student` VALUES (38, '2024009', '郑十一', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323669, 1762744323669, 0, 0, 0);
INSERT INTO `v1_student` VALUES (39, '2024010', '王十二', 1, 1, 0, 0, 0, 0, 0, 0, NULL, 1762744323696, 1762744323696, 0, 0, 0);
INSERT INTO `v1_student` VALUES (40, '2024016', '测试1', 4, 3, 0, 76.17, 40, 0, 0, 0, '星辰徽章', 1762744323696, 1765258327672, 0, 0, 0);
INSERT INTO `v1_student` VALUES (41, '2024017', '测试2', 4, 3, 0, 76.17, 40, 0, 0, 0, '星辰徽章', 1762744323700, 1765258327672, 0, 0, 0);
INSERT INTO `v1_student` VALUES (42, '2024018', '测试3', 4, 3, 0, 76.17, 40, 0, 0, 0, '星辰徽章', 1762744323700, 1765258327672, 0, 0, 0);
INSERT INTO `v1_student` VALUES (43, '1008610', '王腾飞', 1, 4, 0, 0, 0, 0, 0, 0, NULL, 1765269047940, 1765269047940, 0, 0, 0);
INSERT INTO `v1_student` VALUES (44, '1', '1', 2, 4, 0, 0, 0, 0, 0, 0, NULL, 1765271081086, 1765271081086, 0, 0, 0);
INSERT INTO `v1_student` VALUES (45, '1008611', '王小小', 3, 4, 0, 0, 0, 0, 0, 0, NULL, 1765271473251, 1765271814464, 0, 0, 0);
INSERT INTO `v1_student` VALUES (46, '2024058', '江腾飞', 3, 3, 0, 0, 0, 0, 0, 0, NULL, 1765336736491, 1765336736491, 0, 0, 0);
INSERT INTO `v1_student` VALUES (47, '461654', 'sugar', 3, 1, 0, 0, 0, 0, 0, 0, NULL, 1765353721037, 1765353721037, 0, 0, 0);
INSERT INTO `v1_student` VALUES (48, '123', '江', 5, 3, 0, 0, 0, 0, 0, 0, NULL, 1765354396119, 1765354396119, 0, 0, 0);
INSERT INTO `v1_student` VALUES (53, '10086', 'jiang', 3, 1, 0, 0, 0, 0, 0, 0, NULL, 1765442949480, 1765442949480, 0, 0, 0);

-- ----------------------------
-- Table structure for v1_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `v1_suggestion`;
CREATE TABLE `v1_suggestion`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uid` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `status` int NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_suggestion
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_config
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_config`;
CREATE TABLE `v1_system_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `org_id` bigint NOT NULL,
  `tab_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_encrypt` tinyint(1) NOT NULL DEFAULT 0,
  `content_type` int NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_system_config
-- ----------------------------

-- ----------------------------
-- Table structure for v1_system_config_template
-- ----------------------------
DROP TABLE IF EXISTS `v1_system_config_template`;
CREATE TABLE `v1_system_config_template`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 0,
  `is_encrypt` tinyint(1) NOT NULL DEFAULT 0,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of v1_system_config_template
-- ----------------------------

-- ----------------------------
-- Procedure structure for kill_sleep_connections
-- ----------------------------
DROP PROCEDURE IF EXISTS `kill_sleep_connections`;
delimiter ;;
CREATE PROCEDURE `kill_sleep_connections`(IN max_idle_time INT)
BEGIN
   /*
    功能：清理空闲时间过长的数据库连接
    参数：max_idle_time - 最大空闲时间（秒）
    */
    
    DECLARE done INT DEFAULT FALSE;
    DECLARE kill_sql VARCHAR(100);
    DECLARE v_id BIGINT;
    DECLARE v_user VARCHAR(50);
    DECLARE v_time INT;
    DECLARE killed_count INT DEFAULT 0;
    
    -- 游标：获取需要清理的连接
    DECLARE cur CURSOR FOR 
        SELECT id, user, time
        FROM information_schema.processlist 
        WHERE command = 'Sleep' 
          AND time > max_idle_time
          AND user != 'system user'  -- 排除系统用户
          AND id != CONNECTION_ID(); -- 排除当前连接
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- 创建临时表记录操作日志
    CREATE TEMPORARY TABLE IF NOT EXISTS kill_log (
        id BIGINT,
        user VARCHAR(50),
        idle_time INT,
        kill_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    
    -- 清空临时表
    DELETE FROM kill_log;
    
    -- 打开游标
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO v_id, v_user, v_time;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 生成KILL语句
        SET kill_sql = CONCAT('KILL ', v_id);
        
        -- 记录到日志
        INSERT INTO kill_log (id, user, idle_time) 
        VALUES (v_id, v_user, v_time);
        
        -- 准备并执行KILL语句
        SET @sql_stmt = kill_sql;
        PREPARE stmt FROM @sql_stmt;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        
        -- 计数
        SET killed_count = killed_count + 1;
        
    END LOOP;
    
    -- 关闭游标
    CLOSE cur;
    
    -- 返回结果
    SELECT 
        CONCAT('已清理 ', killed_count, ' 个空闲连接') as result,
        CONCAT('最大空闲时间设置: ', max_idle_time, ' 秒') as setting;
    
    -- 显示清理详情（可选）
    IF killed_count > 0 THEN
        SELECT '被清理的连接详情:' as info;
        SELECT * FROM kill_log ORDER BY idle_time DESC;
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
