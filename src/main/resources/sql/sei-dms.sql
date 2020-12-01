/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : sei-dms

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 01/12/2020 21:36:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cf_datasource
-- ----------------------------
DROP TABLE IF EXISTS `cf_datasource`;
CREATE TABLE `cf_datasource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(11) NOT NULL COMMENT '1. Mysql 2. Sqlserver 3. mongodb 4. Redis  5. mq',
  `ip` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `port` int(11) NULL DEFAULT NULL,
  `db` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator_account` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  `query_switch` int(11) NOT NULL DEFAULT 1 COMMENT '1 开启 -1 关闭',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cf_datasource
-- ----------------------------
INSERT INTO `cf_datasource` VALUES (1, 'localhost', NULL, 1, '127.0.0.1', 3306, NULL, 'root', '123456', '管理员', 'admin', '2020-11-24 10:57:15', '2020-11-24 10:57:36', 1);

-- ----------------------------
-- Table structure for rs_sql_exe_record
-- ----------------------------
DROP TABLE IF EXISTS `rs_sql_exe_record`;
CREATE TABLE `rs_sql_exe_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sql_text` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `datasource_id` int(11) NOT NULL,
  `db` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '1. 执行中  2. 执行完成',
  `create_account` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rs_sql_exe_result
-- ----------------------------
DROP TABLE IF EXISTS `rs_sql_exe_result`;
CREATE TABLE `rs_sql_exe_result`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sql_exe_record_id` int(11) NOT NULL,
  `sql_text` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `result` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '1. 执行中 -1 执行失败 2. 执行成功',
  `syntax_error_type` int(11) NULL DEFAULT NULL,
  `syntax_error_sql` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `creator_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator_account` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sql_option_type` int(11) NULL DEFAULT NULL,
  `datasource_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `datasource_type` int(11) NULL DEFAULT NULL,
  `db` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `datasource_id` int(11) NULL DEFAULT NULL,
  `group_id` int(11) NULL DEFAULT NULL,
  `group_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `table_name_list` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
