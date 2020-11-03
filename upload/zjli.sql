/*
Navicat MySQL Data Transfer

Source Server         : lr
Source Server Version : 50730
Source Host           : 118.25.104.232:3306
Source Database       : zjli

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-11-03 17:32:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_applycheck
-- ----------------------------
DROP TABLE IF EXISTS `tb_applycheck`;
CREATE TABLE `tb_applycheck` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dynamicId` int(11) DEFAULT NULL,
  `employeeId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_applycheck
-- ----------------------------

-- ----------------------------
-- Table structure for tb_applyrank
-- ----------------------------
DROP TABLE IF EXISTS `tb_applyrank`;
CREATE TABLE `tb_applyrank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `startTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `checkIdList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `checkTimeList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `checkList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `noteList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `endTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `checkNumber` int(11) DEFAULT NULL,
  `dynamicId` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_applyrank
-- ----------------------------

-- ----------------------------
-- Table structure for tb_company
-- ----------------------------
DROP TABLE IF EXISTS `tb_company`;
CREATE TABLE `tb_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `startTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `endTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_company
-- ----------------------------
INSERT INTO `tb_company` VALUES ('14', '木习习有限公司', null, null, null, null, '1');
INSERT INTO `tb_company` VALUES ('15', '临平臻艾堂养身会所', '浙江临平', '08:30', '21:30', '2020-11-03 17:21:57', '1');
INSERT INTO `tb_company` VALUES ('16', '长兴千名轩养生会所', '浙江长兴县花菇山路7476号', '08:30', '21:30', '2020-11-03 17:22:15', '1');
INSERT INTO `tb_company` VALUES ('17', '开化花容美业', '浙江开化县荷花广场', '07:30', '21:30', '2020-11-03 17:22:22', '1');
INSERT INTO `tb_company` VALUES ('18', '慈溪YSSYUDIO遇尚美学馆', '浙江慈溪三北大街279号', '8:00', '21:30', '2020-11-03 17:22:42', '1');
INSERT INTO `tb_company` VALUES ('19', '义乌尚彩美容会所', '浙江义乌市', '8:00', '21:30', '2020-11-03 17:22:59', '1');
INSERT INTO `tb_company` VALUES ('20', '婴儿时代会所', '浙江杭州市临平深川大厦', '9:00', '17:30', '2020-11-03 17:23:10', '1');

-- ----------------------------
-- Table structure for tb_customer
-- ----------------------------
DROP TABLE IF EXISTS `tb_customer`;
CREATE TABLE `tb_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sex` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `birth` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `employeeIdList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `operatorId` int(11) DEFAULT NULL,
  `habit` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `plan` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `money` int(255) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pic` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `activeConsumeTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `activeServiceTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_customer
-- ----------------------------

-- ----------------------------
-- Table structure for tb_customerperformance
-- ----------------------------
DROP TABLE IF EXISTS `tb_customerperformance`;
CREATE TABLE `tb_customerperformance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) DEFAULT NULL,
  `employeeIdList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `operatorId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_customerperformance
-- ----------------------------

-- ----------------------------
-- Table structure for tb_customerproject
-- ----------------------------
DROP TABLE IF EXISTS `tb_customerproject`;
CREATE TABLE `tb_customerproject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) DEFAULT NULL,
  `employeeId` int(11) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `restCount` int(11) DEFAULT NULL,
  `ingCount` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_customerproject
-- ----------------------------

-- ----------------------------
-- Table structure for tb_dict
-- ----------------------------
DROP TABLE IF EXISTS `tb_dict`;
CREATE TABLE `tb_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `stateName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `stateCode` int(11) DEFAULT NULL,
  `companyId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_dict
-- ----------------------------
INSERT INTO `tb_dict` VALUES ('1', '001', '申请', '未提交', '1', '2');
INSERT INTO `tb_dict` VALUES ('2', '001', '申请', '未审核', '2', '2');
INSERT INTO `tb_dict` VALUES ('3', '001', '申请', '审核中', '3', '2');
INSERT INTO `tb_dict` VALUES ('4', '001', '申请', '审核成功', '4', '2');
INSERT INTO `tb_dict` VALUES ('5', '001', '申请', '审核失败', '5', '2');
INSERT INTO `tb_dict` VALUES ('6', '002', '审核', '通过', '1', '2');
INSERT INTO `tb_dict` VALUES ('7', '002', '审核', '驳回', '2', '2');
INSERT INTO `tb_dict` VALUES ('8', '003', '预约', '未开始', '1', '2');
INSERT INTO `tb_dict` VALUES ('9', '003', '预约', '进行中', '2', '2');
INSERT INTO `tb_dict` VALUES ('10', '003', '预约', '已完成', '3', '2');
INSERT INTO `tb_dict` VALUES ('11', '003', '预约', '未完成', '4', '2');
INSERT INTO `tb_dict` VALUES ('15', '005', '数据', '已失效', '0', '2');
INSERT INTO `tb_dict` VALUES ('17', '006', '任务', '任务清单', '1', '2');
INSERT INTO `tb_dict` VALUES ('18', '006', '任务', '赋能思维', '2', '2');
INSERT INTO `tb_dict` VALUES ('19', '007', '任务清单', '日流程', '1', '2');
INSERT INTO `tb_dict` VALUES ('20', '007', '任务清单', '周安排', '2', '2');
INSERT INTO `tb_dict` VALUES ('21', '007', '任务清单', '月计划', '3', '2');
INSERT INTO `tb_dict` VALUES ('22', '008', '行程安排', '内勤', '1', '2');
INSERT INTO `tb_dict` VALUES ('23', '008', '行程安排', '外勤', '2', '2');
INSERT INTO `tb_dict` VALUES ('24', '008', '行程安排', '请假', '3', '2');
INSERT INTO `tb_dict` VALUES ('25', '008', '行程安排', '轮休', '4', '2');
INSERT INTO `tb_dict` VALUES ('26', '009', '请假类型', '事假', '1', '2');
INSERT INTO `tb_dict` VALUES ('27', '009', '请假类型', '病假', '2', '2');
INSERT INTO `tb_dict` VALUES ('28', '009', '请假类型', '轮休', '3', '2');
INSERT INTO `tb_dict` VALUES ('29', '010', '申请类型', '请假', '1', '2');
INSERT INTO `tb_dict` VALUES ('30', '010', '申请类型', '物料', '2', '2');
INSERT INTO `tb_dict` VALUES ('31', '010', '申请类型', '培训', '3', '2');
INSERT INTO `tb_dict` VALUES ('32', '011', '员工状态', '未激活', '1', '2');
INSERT INTO `tb_dict` VALUES ('33', '011', '员工状态', '已激活', '2', '2');
INSERT INTO `tb_dict` VALUES ('34', '005', '数据', '未失效', '1', '2');
INSERT INTO `tb_dict` VALUES ('35', '006', '任务', '待我审批', '3', '2');
INSERT INTO `tb_dict` VALUES ('36', '002', '审核', '未审核', '3', '2');
INSERT INTO `tb_dict` VALUES ('37', '001', '申请', '未申请', '6', '2');
INSERT INTO `tb_dict` VALUES ('38', '012', '业绩类型', '新增顾客', '1', '2');
INSERT INTO `tb_dict` VALUES ('39', '012', '业绩类型', '顾客续费', '2', '2');

-- ----------------------------
-- Table structure for tb_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `tb_dynamic`;
CREATE TABLE `tb_dynamic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tb_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tb_id` int(11) DEFAULT NULL,
  `employeeId` int(11) DEFAULT NULL,
  `checkId` int(11) DEFAULT NULL,
  `companyId` int(11) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=836 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_dynamic
-- ----------------------------

-- ----------------------------
-- Table structure for tb_employee
-- ----------------------------
DROP TABLE IF EXISTS `tb_employee`;
CREATE TABLE `tb_employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sex` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `companyId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `pic` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `leaderIdList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `verficationCode` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `validTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `underIdList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employee
-- ----------------------------
INSERT INTO `tb_employee` VALUES ('2', '17367073440', 'cjmm', 'fh58q2ea6thauof5ikg98fe2ciafh50r', '女', '14', '8', '/upload//20200721140018.jpg', '', '2', '867111', '2020-07-21 15:20:32', '17-');
INSERT INTO `tb_employee` VALUES ('17', '17764585713', '陈佳敏', '7l7ins3to6v3hcgcqri6iid10sfpq3ht', '女', '14', '9', '/upload/20200727191209.jpg', '2-', '2', '402320', '2020-07-21 16:13:20', '');
INSERT INTO `tb_employee` VALUES ('18', '18968074777', '管理员', 'fh58q2ea6thauof5ikg98fe2ciafh50r', null, null, '0', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for tb_employeeapply
-- ----------------------------
DROP TABLE IF EXISTS `tb_employeeapply`;
CREATE TABLE `tb_employeeapply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeId` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `startDate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `startPhase` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `endDate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `endPhase` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employeeapply
-- ----------------------------

-- ----------------------------
-- Table structure for tb_employeeattendance
-- ----------------------------
DROP TABLE IF EXISTS `tb_employeeattendance`;
CREATE TABLE `tb_employeeattendance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pic` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employeeattendance
-- ----------------------------

-- ----------------------------
-- Table structure for tb_employeelogday
-- ----------------------------
DROP TABLE IF EXISTS `tb_employeelogday`;
CREATE TABLE `tb_employeelogday` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `goalAchievement` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `taskAchievement` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `feel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employeelogday
-- ----------------------------

-- ----------------------------
-- Table structure for tb_employeelogtomorrow
-- ----------------------------
DROP TABLE IF EXISTS `tb_employeelogtomorrow`;
CREATE TABLE `tb_employeelogtomorrow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `money` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `customer` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `run` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `person` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employeelogtomorrow
-- ----------------------------

-- ----------------------------
-- Table structure for tb_employeerank
-- ----------------------------
DROP TABLE IF EXISTS `tb_employeerank`;
CREATE TABLE `tb_employeerank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `dynamicId` int(11) DEFAULT NULL,
  `companyId` int(11) DEFAULT NULL,
  `isAdd` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employeerank
-- ----------------------------

-- ----------------------------
-- Table structure for tb_employeerest
-- ----------------------------
DROP TABLE IF EXISTS `tb_employeerest`;
CREATE TABLE `tb_employeerest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `schedule` int(11) DEFAULT NULL,
  `operatorId` int(11) DEFAULT NULL,
  `operatorTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employeerest
-- ----------------------------

-- ----------------------------
-- Table structure for tb_employeetask
-- ----------------------------
DROP TABLE IF EXISTS `tb_employeetask`;
CREATE TABLE `tb_employeetask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employeeId` int(11) DEFAULT NULL,
  `taskId` int(11) DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pics` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_employeetask
-- ----------------------------

-- ----------------------------
-- Table structure for tb_notice
-- ----------------------------
DROP TABLE IF EXISTS `tb_notice`;
CREATE TABLE `tb_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `companyId` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_notice
-- ----------------------------

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) DEFAULT NULL,
  `employeeId` int(11) DEFAULT NULL,
  `customerProjectId` int(11) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  `date` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `startTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `actStartTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `actEndTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `evaluate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pic` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `projectNum` int(11) DEFAULT NULL,
  `applyState` int(11) DEFAULT NULL,
  `orderState` int(11) DEFAULT NULL,
  `applyOrderState` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_order
-- ----------------------------

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `companyId` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES ('1', '公司制度', null, '1');
INSERT INTO `tb_permission` VALUES ('2', '项目管理', null, '1');
INSERT INTO `tb_permission` VALUES ('3', '宣告管理', null, '1');
INSERT INTO `tb_permission` VALUES ('4', '岗位管理', null, '1');
INSERT INTO `tb_permission` VALUES ('5', '员工管理', null, '1');

-- ----------------------------
-- Table structure for tb_pic
-- ----------------------------
DROP TABLE IF EXISTS `tb_pic`;
CREATE TABLE `tb_pic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `companyId` int(11) DEFAULT NULL,
  `pic` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_pic
-- ----------------------------

-- ----------------------------
-- Table structure for tb_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_post`;
CREATE TABLE `tb_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `companyId` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `permissionList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pic` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `leaderPostId` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_post
-- ----------------------------
INSERT INTO `tb_post` VALUES ('8', '14', '经营者', null, null, '1', '1', '0');
INSERT INTO `tb_post` VALUES ('9', '14', '店长', null, null, '1', '1', '8');
INSERT INTO `tb_post` VALUES ('10', '14', '顾问', null, null, '0', '1', '9');
INSERT INTO `tb_post` VALUES ('11', '14', '美容师', null, null, '0', '1', '9-10');
INSERT INTO `tb_post` VALUES ('12', '14', '前台', null, null, '0', '1', '9');
INSERT INTO `tb_post` VALUES ('13', '15', '经营者', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '0');
INSERT INTO `tb_post` VALUES ('14', '15', '店长', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '13');
INSERT INTO `tb_post` VALUES ('15', '15', '顾问', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '14');
INSERT INTO `tb_post` VALUES ('16', '15', '美容师', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '14-15');
INSERT INTO `tb_post` VALUES ('17', '15', '前台', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '14');
INSERT INTO `tb_post` VALUES ('18', '16', '经营者', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '0');
INSERT INTO `tb_post` VALUES ('19', '16', '店长', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '18');
INSERT INTO `tb_post` VALUES ('20', '16', '顾问', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '19');
INSERT INTO `tb_post` VALUES ('21', '16', '美容师', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '19-20');
INSERT INTO `tb_post` VALUES ('22', '16', '前台', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '19');
INSERT INTO `tb_post` VALUES ('23', '17', '经营者', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '0');
INSERT INTO `tb_post` VALUES ('24', '17', '店长', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '23');
INSERT INTO `tb_post` VALUES ('25', '17', '顾问', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '24');
INSERT INTO `tb_post` VALUES ('26', '17', '美容师', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '24-25');
INSERT INTO `tb_post` VALUES ('27', '17', '前台', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '24');
INSERT INTO `tb_post` VALUES ('28', '18', '经营者', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '0');
INSERT INTO `tb_post` VALUES ('29', '18', '店长', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '28');
INSERT INTO `tb_post` VALUES ('30', '18', '顾问', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '29');
INSERT INTO `tb_post` VALUES ('31', '18', '美容师', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '29-30');
INSERT INTO `tb_post` VALUES ('32', '18', '前台', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '29');
INSERT INTO `tb_post` VALUES ('33', '19', '经营者', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '0');
INSERT INTO `tb_post` VALUES ('34', '19', '店长', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '33');
INSERT INTO `tb_post` VALUES ('35', '19', '顾问', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '34');
INSERT INTO `tb_post` VALUES ('36', '19', '美容师', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '34-35');
INSERT INTO `tb_post` VALUES ('37', '19', '前台', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '34');
INSERT INTO `tb_post` VALUES ('38', '20', '经营者', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '0');
INSERT INTO `tb_post` VALUES ('39', '20', '店长', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '38');
INSERT INTO `tb_post` VALUES ('40', '20', '顾问', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '39');
INSERT INTO `tb_post` VALUES ('41', '20', '美容师', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '39-40');
INSERT INTO `tb_post` VALUES ('42', '20', '前台', '1-2-3-4-5', '/upload/tubiao.png', '0', '1', '39');

-- ----------------------------
-- Table structure for tb_posttask
-- ----------------------------
DROP TABLE IF EXISTS `tb_posttask`;
CREATE TABLE `tb_posttask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `postId` int(11) DEFAULT NULL,
  `taskIdListFN` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `taskIdListRWDay` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `taskIdListRWWeek` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `taskIdListRWMon` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_posttask
-- ----------------------------
INSERT INTO `tb_posttask` VALUES ('14', '8', null, '125-126-127-128-129-', '130-', '131-132-133-');
INSERT INTO `tb_posttask` VALUES ('15', '9', null, '90-91-92-93-94-95-96-97-98-', '99-100-101-', '102-103-');
INSERT INTO `tb_posttask` VALUES ('16', '10', null, '104-105-106-107-108-', '109-110-111-', '112-113-');
INSERT INTO `tb_posttask` VALUES ('17', '11', null, '74-75-76-77-78-79-80-81-82-83-84-', '85-86-87-', '88-89-');
INSERT INTO `tb_posttask` VALUES ('18', '12', null, '114-115-116-117-118-119-120-121-', '122-123-', '124-');
INSERT INTO `tb_posttask` VALUES ('19', '13', '', '134-135-136-137-138-', '139-', '140-141-142-');
INSERT INTO `tb_posttask` VALUES ('20', '14', '', '143-144-145-146-147-148-149-150-151-', '152-153-154-', '155-156-');
INSERT INTO `tb_posttask` VALUES ('21', '15', '', '157-158-159-160-161-', '162-163-164-', '165-166-');
INSERT INTO `tb_posttask` VALUES ('22', '16', '', '167-168-169-170-171-172-173-174-175-176-177-', '178-179-180-', '181-182-');
INSERT INTO `tb_posttask` VALUES ('23', '17', '', '183-184-185-186-187-188-189-190-', '191-192-', '193-');
INSERT INTO `tb_posttask` VALUES ('24', '18', '', '194-195-196-197-198-', '199-', '200-201-202-');
INSERT INTO `tb_posttask` VALUES ('25', '19', '', '203-204-205-206-207-208-209-210-211-', '212-213-214-', '215-216-');
INSERT INTO `tb_posttask` VALUES ('26', '20', '', '217-218-219-220-221-', '222-223-224-', '225-226-');
INSERT INTO `tb_posttask` VALUES ('27', '21', '', '227-228-229-230-231-232-233-234-235-236-237-', '238-239-240-', '241-242-');
INSERT INTO `tb_posttask` VALUES ('28', '22', '', '243-244-245-246-247-248-249-250-', '251-252-', '253-');
INSERT INTO `tb_posttask` VALUES ('29', '23', '', '254-255-256-257-258-', '259-', '260-261-262-');
INSERT INTO `tb_posttask` VALUES ('30', '24', '', '263-264-265-266-267-268-269-270-271-', '272-273-274-', '275-276-');
INSERT INTO `tb_posttask` VALUES ('31', '25', '', '277-278-279-280-281-', '282-283-284-', '285-286-');
INSERT INTO `tb_posttask` VALUES ('32', '26', '', '287-288-289-290-291-292-293-294-295-296-297-', '298-299-300-', '301-302-');
INSERT INTO `tb_posttask` VALUES ('33', '27', '', '303-304-305-306-307-308-309-310-', '311-312-', '313-');
INSERT INTO `tb_posttask` VALUES ('34', '28', '', '314-315-316-317-318-', '319-', '320-321-322-');
INSERT INTO `tb_posttask` VALUES ('35', '29', '', '323-324-325-326-327-328-329-330-331-', '332-333-334-', '335-336-');
INSERT INTO `tb_posttask` VALUES ('36', '30', '', '337-338-339-340-341-', '342-343-344-', '345-346-');
INSERT INTO `tb_posttask` VALUES ('37', '31', '', '347-348-349-350-351-352-353-354-355-356-357-', '358-359-360-', '361-362-');
INSERT INTO `tb_posttask` VALUES ('38', '32', '', '363-364-365-366-367-368-369-370-', '371-372-', '373-');
INSERT INTO `tb_posttask` VALUES ('39', '33', '', '374-375-376-377-378-', '379-', '380-381-382-');
INSERT INTO `tb_posttask` VALUES ('40', '34', '', '383-384-385-386-387-388-389-390-391-', '392-393-394-', '395-396-');
INSERT INTO `tb_posttask` VALUES ('41', '35', '', '397-398-399-400-401-', '402-403-404-', '405-406-');
INSERT INTO `tb_posttask` VALUES ('42', '36', '', '407-408-409-410-411-412-413-414-415-416-417-', '418-419-420-', '421-422-');
INSERT INTO `tb_posttask` VALUES ('43', '37', '', '423-424-425-426-427-428-429-430-', '431-432-', '433-');
INSERT INTO `tb_posttask` VALUES ('44', '38', '', '434-435-436-437-438-', '439-', '440-441-442-');
INSERT INTO `tb_posttask` VALUES ('45', '39', '', '443-444-445-446-447-448-449-450-451-', '452-453-454-', '455-456-');
INSERT INTO `tb_posttask` VALUES ('46', '40', '', '457-458-459-460-461-', '462-463-464-', '465-466-');
INSERT INTO `tb_posttask` VALUES ('47', '41', '', '467-468-469-470-471-472-473-474-475-476-477-', '478-479-480-', '481-482-');
INSERT INTO `tb_posttask` VALUES ('48', '42', '', '483-484-485-486-487-488-489-490-', '491-492-', '493-');

-- ----------------------------
-- Table structure for tb_project
-- ----------------------------
DROP TABLE IF EXISTS `tb_project`;
CREATE TABLE `tb_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `companyId` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_project
-- ----------------------------

-- ----------------------------
-- Table structure for tb_rank
-- ----------------------------
DROP TABLE IF EXISTS `tb_rank`;
CREATE TABLE `tb_rank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `companyId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_rank
-- ----------------------------
INSERT INTO `tb_rank` VALUES ('1', '请假', '0', '1', '2');
INSERT INTO `tb_rank` VALUES ('2', '物料', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('3', '培训', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('4', '每日打卡', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('5', '每日行程', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('6', '每日一报', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('9', '新增顾客', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('11', '顾客续费', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('12', '顾客购买项目', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('14', '预约项目', '10', '1', '2');
INSERT INTO `tb_rank` VALUES ('15', '预约完成', '10', '1', '2');

-- ----------------------------
-- Table structure for tb_system
-- ----------------------------
DROP TABLE IF EXISTS `tb_system`;
CREATE TABLE `tb_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `companyId` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_system
-- ----------------------------

-- ----------------------------
-- Table structure for tb_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_task`;
CREATE TABLE `tb_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `companyId` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `prevType` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `step` int(11) DEFAULT NULL,
  `postIdList` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=494 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_task
-- ----------------------------
INSERT INTO `tb_task` VALUES ('74', '14', '整理仪容仪表', '每天8:30前整理好仪容仪表，拍照打卡，申请积分', '3', '1', '1', '1', '11', '1');
INSERT INTO `tb_task` VALUES ('75', '14', '及时整理好环境卫生', '每天上下班要整理好自己所负责区域的卫生，做好顾客整理好房间卫生，拍照申请积分！', '3', '1', '1', '2', '11', '1');
INSERT INTO `tb_task` VALUES ('76', '14', '积极参加晨会', '对昨天的工作进行总结和今天的工作进行计划，明确今天的目标及要做的工作。', '3', '1', '1', '3', '11', '1');
INSERT INTO `tb_task` VALUES ('77', '14', '预约顾客确定护理时间', '每天主动预约顾客，确定客户的护理时间，每预约确定一个客户申请5分！', '5', '1', '1', '4', '11', '1');
INSERT INTO `tb_task` VALUES ('78', '14', '提前为预约客户准备房间', '对每天确定来护理的顾客，准备好客户所需要的产品及房间！', '3', '1', '1', '5', '11', '1');
INSERT INTO `tb_task` VALUES ('79', '14', '每日专业学习成长', '每天学习当月主推产品专业，在群里用语音读出来，每学习一遍可申请5分。', '5', '1', '1', '6', '11', '1');
INSERT INTO `tb_task` VALUES ('80', '14', '演练铺垫话术', '根据每天接待的顾客，组织相互演练铺垫的产品话术，每遍申请5分。', '5', '1', '1', '7', '11', '1');
INSERT INTO `tb_task` VALUES ('81', '14', '售后信息回访', '针对当天的操作客户，进行信息回访！每个客户申请3分。', '3', '1', '1', '8', '11', '1');
INSERT INTO `tb_task` VALUES ('82', '14', '月主推产品手法练习', '每练习一遍手法，每个动作不少与7遍，一个项目整体手法完成加5分。', '5', '1', '1', '9', '11', '1');
INSERT INTO `tb_task` VALUES ('83', '14', '迎宾30分钟', '每天在门口迎宾30分钟，标准化站姿迎接客户，每30分钟申请10分！', '10', '1', '1', '10', '11', '1');
INSERT INTO `tb_task` VALUES ('84', '14', '主动汇报当日工作', '每日下班前在群里发布文字或者电话给老板汇报一天的工作，申请5分！', '5', '1', '1', '11', '11', '1');
INSERT INTO `tb_task` VALUES ('85', '14', '参加周总结计划会议', '每周六召开一次周总结会议，找出自己在这一周中不足的地方，然后如何改正，明确下周目标及业绩来源路经。积分10分。', '10', '1', '2', '1', '11', '1');
INSERT INTO `tb_task` VALUES ('86', '14', '每周一次大扫除', '每周整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '11', '1');
INSERT INTO `tb_task` VALUES ('87', '14', '完成每周的业绩目标', '每周目标明确，一周内完成周目标，即可申请加10分。', '10', '1', '2', '3', '11', '1');
INSERT INTO `tb_task` VALUES ('88', '14', '每月一次目标规划会议', '每月30日做好下个月业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '11', '1');
INSERT INTO `tb_task` VALUES ('89', '14', '完成当月目标', '在一个月内达成月目标，即可申请20分！', '20', '1', '3', '2', '11', '1');
INSERT INTO `tb_task` VALUES ('90', '14', '整理及检查团队仪容仪表', '每天上班第一件事，就是整理好自己的仪容仪表，并检查团队仪容仪表，拍照上传！即可加5分！', '5', '1', '1', '1', '9', '1');
INSERT INTO `tb_task` VALUES ('91', '14', '检查整体卫生', '上班第二件事就是检查整店的环境卫生，物品要摆放整齐，督促区域卫生负责人做好区域卫生！', '3', '1', '1', '2', '9', '1');
INSERT INTO `tb_task` VALUES ('92', '14', '组织团队开晨会', '让每个人目标明确，总结昨天不好的行为及工作方方法，安排好今天的工作！跳个舞结束今天的会议！加10分。', '10', '1', '1', '3', '9', '1');
INSERT INTO `tb_task` VALUES ('93', '14', '督促客户预约', '开完晨会督促美容师做好客户预约，团队每确定预约5个客户加5分！', '5', '1', '1', '4', '9', '1');
INSERT INTO `tb_task` VALUES ('94', '14', '每日专业学习成长', '根据每月主推产品专业，做好榜样在群里用语音朗读，每读一遍加5分。', '5', '1', '1', '5', '9', '1');
INSERT INTO `tb_task` VALUES ('95', '14', '组织演练美容师成长', '跟据每天预约的客户，带动美容师进行铺垫实战演练，每带动一个美容师演练1遍加3分。', '3', '1', '1', '6', '9', '1');
INSERT INTO `tb_task` VALUES ('96', '14', '督促检查信息回访', '督促检查美容师对当天来服务的客户进行回访，确保每天的客户全部回访！加5分。', '5', '1', '1', '7', '9', '1');
INSERT INTO `tb_task` VALUES ('97', '14', '考核美容师专业', '专业美容体现的就是专业，每天都要考核美容师专业知识，让美容师成长！加5分。', '5', '1', '1', '8', '9', '1');
INSERT INTO `tb_task` VALUES ('98', '14', '考核美容师手法', '跟据月主推产品，对美容师进行手法考核，统一美容师手法！加5分。', '5', '1', '1', '9', '9', '1');
INSERT INTO `tb_task` VALUES ('99', '14', '准备及组织周总结计划会', '组织好大家及时开会，让大家清晰上一周不足的地方，及接下来如何改正！再明确下周目标及业绩路径，加10分。', '10', '1', '2', '1', '9', '1');
INSERT INTO `tb_task` VALUES ('100', '14', '组织每周大扫除', '整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '9', '1');
INSERT INTO `tb_task` VALUES ('101', '14', '完成每周目标', '完成每周的业绩目标，即可申请加5分。', '5', '1', '2', '3', '9', '1');
INSERT INTO `tb_task` VALUES ('102', '14', '每月一次目标规划会议', '每月30日做下个月的业绩规划，并设定清晰的目标，有清晰的业路径！', '3', '1', '3', '1', '9', '1');
INSERT INTO `tb_task` VALUES ('103', '14', '完成当月目标', '完成当月的业绩目标，加20分。', '20', '1', '3', '2', '9', '1');
INSERT INTO `tb_task` VALUES ('104', '14', '整理好仪容仪表做考勤', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '10', '1');
INSERT INTO `tb_task` VALUES ('105', '14', '明确日目标', '每天清楚今日的业绩目标，在晨会上再次明细今日业绩路径，带动美容完成业绩目标。', '3', '1', '1', '2', '10', '1');
INSERT INTO `tb_task` VALUES ('106', '14', '跟据接待的客户进行演练', '分析每个客户，检查美容师能否熟练的接待客户，做好铺垫，每个客户来之前都带领接待的美容师进行演练，直到熟练。', '3', '1', '1', '3', '10', '1');
INSERT INTO `tb_task` VALUES ('107', '14', '完成日目标', '完成每日业绩目标，即可申请10分', '10', '1', '1', '4', '10', '1');
INSERT INTO `tb_task` VALUES ('108', '14', '主动汇报当日工作', '跟据每日业绩完成情况电话给老板汇报一天完成业绩工作情况，申请5分！', '5', '1', '1', '5', '10', '1');
INSERT INTO `tb_task` VALUES ('109', '14', '周总结计划会议', '重点总结上周业绩完成情况，及大家配合的默契度，演练如何更好配合；再明确下周业绩目标，让每个人有清晰的路径。', '3', '1', '2', '1', '10', '1');
INSERT INTO `tb_task` VALUES ('110', '14', '完成周目标', '完成每周设定的业绩目标，即加20分', '20', '1', '2', '2', '10', '1');
INSERT INTO `tb_task` VALUES ('111', '14', '卫生大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '3', '10', '1');
INSERT INTO `tb_task` VALUES ('112', '14', '明确每月业绩路径', '确保每个月的业绩路径清晰，业绩明细要到顾客、到品项、到美容师。', '3', '1', '3', '1', '10', '1');
INSERT INTO `tb_task` VALUES ('113', '14', '完成月业绩目标', '完成每月业绩目标，即可申请20分。', '3', '1', '3', '2', '10', '1');
INSERT INTO `tb_task` VALUES ('114', '14', '整理仪容仪表考勤打卡', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '12', '1');
INSERT INTO `tb_task` VALUES ('115', '14', '准备好大厅的茶水', '每天准备好大厅的茶水，确保及时给客户奉上一杯茶。', '3', '1', '1', '2', '12', '1');
INSERT INTO `tb_task` VALUES ('116', '14', '大厅、咨询室卫生', '随时随地保持大厅、咨询室卫生干净整洁。', '3', '1', '1', '3', '12', '1');
INSERT INTO `tb_task` VALUES ('117', '14', '客户生日安排', '每天整理好客户生日，及时汇报及安排客户的生日活动！', '3', '1', '1', '4', '12', '1');
INSERT INTO `tb_task` VALUES ('118', '14', '客户经期特殊关怀', '了解客户的月经周期，建立档案，给客户特殊的关怀，热水袋、红糖水，及短息关怀家里的注意事项！', '3', '1', '1', '5', '12', '1');
INSERT INTO `tb_task` VALUES ('119', '14', '前台时刻保持干净整洁', '每天前台一整天保持干净，每天申请一次积分！', '3', '1', '1', '6', '12', '1');
INSERT INTO `tb_task` VALUES ('120', '14', '打20个招聘电话', '每天打20个招聘电话，申请3分，入职1个同事加3分。', '3', '1', '1', '7', '12', '1');
INSERT INTO `tb_task` VALUES ('121', '14', '售后信息回访', '对当日操作的客户，进行信息回访！', '3', '1', '1', '8', '12', '1');
INSERT INTO `tb_task` VALUES ('122', '14', '周总结计划会', '总结一周的工作内容，找出3点不足之处加以改正，下周工作内容及工作量要明确。', '3', '1', '2', '1', '12', '1');
INSERT INTO `tb_task` VALUES ('123', '14', '每周大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '12', '1');
INSERT INTO `tb_task` VALUES ('124', '14', '月总结计划', '指出3点上个月不足的地方，如何改正，下个月工作任务要清晰，工作量明确。', '3', '1', '3', '1', '12', '1');
INSERT INTO `tb_task` VALUES ('125', '14', '让团队职业化', '让团队懂得规则的重要性，发生的每个问题，都要进行沟通让团队成员明理，懂得按规矩做事！', '3', '1', '1', '1', '8', '1');
INSERT INTO `tb_task` VALUES ('126', '14', '帮助员工成长', '根据美容师目前的能力，设定好学习内容，让美容师每半个月学习一个专业，然后定时考核。', '3', '1', '1', '2', '8', '1');
INSERT INTO `tb_task` VALUES ('127', '14', '帮助美容师完成目标', '通过训练使用美容师的能力，而不是抛开美容师自己做业绩给美容师，要让美容师有做业绩的能力。', '3', '1', '1', '3', '8', '1');
INSERT INTO `tb_task` VALUES ('128', '14', '给员工幸福感', '美容师表现好在总结会议上进行表杨，完成目标了要及时给予奖励，让团队有成就感、幸福感。', '3', '1', '1', '4', '8', '1');
INSERT INTO `tb_task` VALUES ('129', '14', '让团队方向明确', '每天要让团队业绩来自于哪个品项、哪个客户，以问为主，让美容师来回答。通过发问让美容师理清思路。', '3', '1', '1', '5', '8', '1');
INSERT INTO `tb_task` VALUES ('130', '14', '周总结计划会', '每周开一次周总结会议，决定店里哪些行为需要改变的进行标准化，重点表扬上周表现优秀的人，给员工幸福感和成就感。', '3', '1', '2', '1', '8', '1');
INSERT INTO `tb_task` VALUES ('131', '14', '月目标规划会议', '每月25日-30日前做好下个月的业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '8', '1');
INSERT INTO `tb_task` VALUES ('132', '14', '安排好月学习专业及手法', '跟据下个月目标，设定好下月的学习的专业及项目手法，让团队能力得到成长。', '3', '1', '3', '2', '8', '1');
INSERT INTO `tb_task` VALUES ('133', '14', '月总结会议', '找出店里不足的地方，进行标准化改正，重点要表扬美容师后，带动氛围让美容师积分抽奖。', '3', '1', '3', '3', '8', '1');
INSERT INTO `tb_task` VALUES ('134', '15', '让团队职业化', '让团队懂得规则的重要性，发生的每个问题，都要进行沟通让团队成员明理，懂得按规矩做事！', '3', '1', '1', '1', '13', '1');
INSERT INTO `tb_task` VALUES ('135', '15', '帮助员工成长', '根据美容师目前的能力，设定好学习内容，让美容师每半个月学习一个专业，然后定时考核。', '3', '1', '1', '2', '13', '1');
INSERT INTO `tb_task` VALUES ('136', '15', '帮助美容师完成目标', '通过训练使用美容师的能力，而不是抛开美容师自己做业绩给美容师，要让美容师有做业绩的能力。', '3', '1', '1', '3', '13', '1');
INSERT INTO `tb_task` VALUES ('137', '15', '给员工幸福感', '美容师表现好在总结会议上进行表杨，完成目标了要及时给予奖励，让团队有成就感、幸福感。', '3', '1', '1', '4', '13', '1');
INSERT INTO `tb_task` VALUES ('138', '15', '让团队方向明确', '每天要让团队业绩来自于哪个品项、哪个客户，以问为主，让美容师来回答。通过发问让美容师理清思路。', '3', '1', '1', '5', '13', '1');
INSERT INTO `tb_task` VALUES ('139', '15', '周总结计划会', '每周开一次周总结会议，决定店里哪些行为需要改变的进行标准化，重点表扬上周表现优秀的人，给员工幸福感和成就感。', '3', '1', '2', '1', '13', '1');
INSERT INTO `tb_task` VALUES ('140', '15', '月目标规划会议', '每月25日-30日前做好下个月的业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '13', '1');
INSERT INTO `tb_task` VALUES ('141', '15', '安排好月学习专业及手法', '跟据下个月目标，设定好下月的学习的专业及项目手法，让团队能力得到成长。', '3', '1', '3', '2', '13', '1');
INSERT INTO `tb_task` VALUES ('142', '15', '月总结会议', '找出店里不足的地方，进行标准化改正，重点要表扬美容师后，带动氛围让美容师积分抽奖。', '3', '1', '3', '3', '13', '1');
INSERT INTO `tb_task` VALUES ('143', '15', '整理及检查团队仪容仪表', '每天上班第一件事，就是整理好自己的仪容仪表，并检查团队仪容仪表，拍照上传！即可加5分！', '5', '1', '1', '1', '14', '1');
INSERT INTO `tb_task` VALUES ('144', '15', '检查整体卫生', '上班第二件事就是检查整店的环境卫生，物品要摆放整齐，督促区域卫生负责人做好区域卫生！', '3', '1', '1', '2', '14', '1');
INSERT INTO `tb_task` VALUES ('145', '15', '组织团队开晨会', '让每个人目标明确，总结昨天不好的行为及工作方方法，安排好今天的工作！跳个舞结束今天的会议！加10分。', '10', '1', '1', '3', '14', '1');
INSERT INTO `tb_task` VALUES ('146', '15', '督促客户预约', '开完晨会督促美容师做好客户预约，团队每确定预约5个客户加5分！', '5', '1', '1', '4', '14', '1');
INSERT INTO `tb_task` VALUES ('147', '15', '每日专业学习成长', '根据每月主推产品专业，做好榜样在群里用语音朗读，每读一遍加5分。', '5', '1', '1', '5', '14', '1');
INSERT INTO `tb_task` VALUES ('148', '15', '组织演练美容师成长', '跟据每天预约的客户，带动美容师进行铺垫实战演练，每带动一个美容师演练1遍加3分。', '3', '1', '1', '6', '14', '1');
INSERT INTO `tb_task` VALUES ('149', '15', '督促检查信息回访', '督促检查美容师对当天来服务的客户进行回访，确保每天的客户全部回访！加5分。', '5', '1', '1', '7', '14', '1');
INSERT INTO `tb_task` VALUES ('150', '15', '考核美容师专业', '专业美容体现的就是专业，每天都要考核美容师专业知识，让美容师成长！加5分。', '5', '1', '1', '8', '14', '1');
INSERT INTO `tb_task` VALUES ('151', '15', '考核美容师手法', '跟据月主推产品，对美容师进行手法考核，统一美容师手法！加5分。', '5', '1', '1', '9', '14', '1');
INSERT INTO `tb_task` VALUES ('152', '15', '准备及组织周总结计划会', '组织好大家及时开会，让大家清晰上一周不足的地方，及接下来如何改正！再明确下周目标及业绩路径，加10分。', '10', '1', '2', '1', '14', '1');
INSERT INTO `tb_task` VALUES ('153', '15', '组织每周大扫除', '整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '14', '1');
INSERT INTO `tb_task` VALUES ('154', '15', '完成每周目标', '完成每周的业绩目标，即可申请加5分。', '5', '1', '2', '3', '14', '1');
INSERT INTO `tb_task` VALUES ('155', '15', '每月一次目标规划会议', '每月30日做下个月的业绩规划，并设定清晰的目标，有清晰的业路径！', '3', '1', '3', '1', '14', '1');
INSERT INTO `tb_task` VALUES ('156', '15', '完成当月目标', '完成当月的业绩目标，加20分。', '20', '1', '3', '2', '14', '1');
INSERT INTO `tb_task` VALUES ('157', '15', '整理好仪容仪表做考勤', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '15', '1');
INSERT INTO `tb_task` VALUES ('158', '15', '明确日目标', '每天清楚今日的业绩目标，在晨会上再次明细今日业绩路径，带动美容完成业绩目标。', '3', '1', '1', '2', '15', '1');
INSERT INTO `tb_task` VALUES ('159', '15', '跟据接待的客户进行演练', '分析每个客户，检查美容师能否熟练的接待客户，做好铺垫，每个客户来之前都带领接待的美容师进行演练，直到熟练。', '3', '1', '1', '3', '15', '1');
INSERT INTO `tb_task` VALUES ('160', '15', '完成日目标', '完成每日业绩目标，即可申请10分', '10', '1', '1', '4', '15', '1');
INSERT INTO `tb_task` VALUES ('161', '15', '主动汇报当日工作', '跟据每日业绩完成情况电话给老板汇报一天完成业绩工作情况，申请5分！', '5', '1', '1', '5', '15', '1');
INSERT INTO `tb_task` VALUES ('162', '15', '周总结计划会议', '重点总结上周业绩完成情况，及大家配合的默契度，演练如何更好配合；再明确下周业绩目标，让每个人有清晰的路径。', '3', '1', '2', '1', '15', '1');
INSERT INTO `tb_task` VALUES ('163', '15', '完成周目标', '完成每周设定的业绩目标，即加20分', '20', '1', '2', '2', '15', '1');
INSERT INTO `tb_task` VALUES ('164', '15', '卫生大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '3', '15', '1');
INSERT INTO `tb_task` VALUES ('165', '15', '明确每月业绩路径', '确保每个月的业绩路径清晰，业绩明细要到顾客、到品项、到美容师。', '3', '1', '3', '1', '15', '1');
INSERT INTO `tb_task` VALUES ('166', '15', '完成月业绩目标', '完成每月业绩目标，即可申请20分。', '3', '1', '3', '2', '15', '1');
INSERT INTO `tb_task` VALUES ('167', '15', '整理仪容仪表', '每天8:30前整理好仪容仪表，拍照打卡，申请积分', '3', '1', '1', '1', '16', '1');
INSERT INTO `tb_task` VALUES ('168', '15', '及时整理好环境卫生', '每天上下班要整理好自己所负责区域的卫生，做好顾客整理好房间卫生，拍照申请积分！', '3', '1', '1', '2', '16', '1');
INSERT INTO `tb_task` VALUES ('169', '15', '积极参加晨会', '对昨天的工作进行总结和今天的工作进行计划，明确今天的目标及要做的工作。', '3', '1', '1', '3', '16', '1');
INSERT INTO `tb_task` VALUES ('170', '15', '预约顾客确定护理时间', '每天主动预约顾客，确定客户的护理时间，每预约确定一个客户申请5分！', '5', '1', '1', '4', '16', '1');
INSERT INTO `tb_task` VALUES ('171', '15', '提前为预约客户准备房间', '对每天确定来护理的顾客，准备好客户所需要的产品及房间！', '3', '1', '1', '5', '16', '1');
INSERT INTO `tb_task` VALUES ('172', '15', '每日专业学习成长', '每天学习当月主推产品专业，在群里用语音读出来，每学习一遍可申请5分。', '5', '1', '1', '6', '16', '1');
INSERT INTO `tb_task` VALUES ('173', '15', '演练铺垫话术', '根据每天接待的顾客，组织相互演练铺垫的产品话术，每遍申请5分。', '5', '1', '1', '7', '16', '1');
INSERT INTO `tb_task` VALUES ('174', '15', '售后信息回访', '针对当天的操作客户，进行信息回访！每个客户申请3分。', '3', '1', '1', '8', '16', '1');
INSERT INTO `tb_task` VALUES ('175', '15', '月主推产品手法练习', '每练习一遍手法，每个动作不少与7遍，一个项目整体手法完成加5分。', '5', '1', '1', '9', '16', '1');
INSERT INTO `tb_task` VALUES ('176', '15', '迎宾30分钟', '每天在门口迎宾30分钟，标准化站姿迎接客户，每30分钟申请10分！', '10', '1', '1', '10', '16', '1');
INSERT INTO `tb_task` VALUES ('177', '15', '主动汇报当日工作', '每日下班前在群里发布文字或者电话给老板汇报一天的工作，申请5分！', '5', '1', '1', '11', '16', '1');
INSERT INTO `tb_task` VALUES ('178', '15', '参加周总结计划会议', '每周六召开一次周总结会议，找出自己在这一周中不足的地方，然后如何改正，明确下周目标及业绩来源路经。积分10分。', '10', '1', '2', '1', '16', '1');
INSERT INTO `tb_task` VALUES ('179', '15', '每周一次大扫除', '每周整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '16', '1');
INSERT INTO `tb_task` VALUES ('180', '15', '完成每周的业绩目标', '每周目标明确，一周内完成周目标，即可申请加10分。', '10', '1', '2', '3', '16', '1');
INSERT INTO `tb_task` VALUES ('181', '15', '每月一次目标规划会议', '每月30日做好下个月业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '16', '1');
INSERT INTO `tb_task` VALUES ('182', '15', '完成当月目标', '在一个月内达成月目标，即可申请20分！', '20', '1', '3', '2', '16', '1');
INSERT INTO `tb_task` VALUES ('183', '15', '整理仪容仪表考勤打卡', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '17', '1');
INSERT INTO `tb_task` VALUES ('184', '15', '准备好大厅的茶水', '每天准备好大厅的茶水，确保及时给客户奉上一杯茶。', '3', '1', '1', '2', '17', '1');
INSERT INTO `tb_task` VALUES ('185', '15', '大厅、咨询室卫生', '随时随地保持大厅、咨询室卫生干净整洁。', '3', '1', '1', '3', '17', '1');
INSERT INTO `tb_task` VALUES ('186', '15', '客户生日安排', '每天整理好客户生日，及时汇报及安排客户的生日活动！', '3', '1', '1', '4', '17', '1');
INSERT INTO `tb_task` VALUES ('187', '15', '客户经期特殊关怀', '了解客户的月经周期，建立档案，给客户特殊的关怀，热水袋、红糖水，及短息关怀家里的注意事项！', '3', '1', '1', '5', '17', '1');
INSERT INTO `tb_task` VALUES ('188', '15', '前台时刻保持干净整洁', '每天前台一整天保持干净，每天申请一次积分！', '3', '1', '1', '6', '17', '1');
INSERT INTO `tb_task` VALUES ('189', '15', '打20个招聘电话', '每天打20个招聘电话，申请3分，入职1个同事加3分。', '3', '1', '1', '7', '17', '1');
INSERT INTO `tb_task` VALUES ('190', '15', '售后信息回访', '对当日操作的客户，进行信息回访！', '3', '1', '1', '8', '17', '1');
INSERT INTO `tb_task` VALUES ('191', '15', '周总结计划会', '总结一周的工作内容，找出3点不足之处加以改正，下周工作内容及工作量要明确。', '3', '1', '2', '1', '17', '1');
INSERT INTO `tb_task` VALUES ('192', '15', '每周大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '17', '1');
INSERT INTO `tb_task` VALUES ('193', '15', '月总结计划', '指出3点上个月不足的地方，如何改正，下个月工作任务要清晰，工作量明确。', '3', '1', '3', '1', '17', '1');
INSERT INTO `tb_task` VALUES ('194', '16', '让团队职业化', '让团队懂得规则的重要性，发生的每个问题，都要进行沟通让团队成员明理，懂得按规矩做事！', '3', '1', '1', '1', '18', '1');
INSERT INTO `tb_task` VALUES ('195', '16', '帮助员工成长', '根据美容师目前的能力，设定好学习内容，让美容师每半个月学习一个专业，然后定时考核。', '3', '1', '1', '2', '18', '1');
INSERT INTO `tb_task` VALUES ('196', '16', '帮助美容师完成目标', '通过训练使用美容师的能力，而不是抛开美容师自己做业绩给美容师，要让美容师有做业绩的能力。', '3', '1', '1', '3', '18', '1');
INSERT INTO `tb_task` VALUES ('197', '16', '给员工幸福感', '美容师表现好在总结会议上进行表杨，完成目标了要及时给予奖励，让团队有成就感、幸福感。', '3', '1', '1', '4', '18', '1');
INSERT INTO `tb_task` VALUES ('198', '16', '让团队方向明确', '每天要让团队业绩来自于哪个品项、哪个客户，以问为主，让美容师来回答。通过发问让美容师理清思路。', '3', '1', '1', '5', '18', '1');
INSERT INTO `tb_task` VALUES ('199', '16', '周总结计划会', '每周开一次周总结会议，决定店里哪些行为需要改变的进行标准化，重点表扬上周表现优秀的人，给员工幸福感和成就感。', '3', '1', '2', '1', '18', '1');
INSERT INTO `tb_task` VALUES ('200', '16', '月目标规划会议', '每月25日-30日前做好下个月的业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '18', '1');
INSERT INTO `tb_task` VALUES ('201', '16', '安排好月学习专业及手法', '跟据下个月目标，设定好下月的学习的专业及项目手法，让团队能力得到成长。', '3', '1', '3', '2', '18', '1');
INSERT INTO `tb_task` VALUES ('202', '16', '月总结会议', '找出店里不足的地方，进行标准化改正，重点要表扬美容师后，带动氛围让美容师积分抽奖。', '3', '1', '3', '3', '18', '1');
INSERT INTO `tb_task` VALUES ('203', '16', '整理及检查团队仪容仪表', '每天上班第一件事，就是整理好自己的仪容仪表，并检查团队仪容仪表，拍照上传！即可加5分！', '5', '1', '1', '1', '19', '1');
INSERT INTO `tb_task` VALUES ('204', '16', '检查整体卫生', '上班第二件事就是检查整店的环境卫生，物品要摆放整齐，督促区域卫生负责人做好区域卫生！', '3', '1', '1', '2', '19', '1');
INSERT INTO `tb_task` VALUES ('205', '16', '组织团队开晨会', '让每个人目标明确，总结昨天不好的行为及工作方方法，安排好今天的工作！跳个舞结束今天的会议！加10分。', '10', '1', '1', '3', '19', '1');
INSERT INTO `tb_task` VALUES ('206', '16', '督促客户预约', '开完晨会督促美容师做好客户预约，团队每确定预约5个客户加5分！', '5', '1', '1', '4', '19', '1');
INSERT INTO `tb_task` VALUES ('207', '16', '每日专业学习成长', '根据每月主推产品专业，做好榜样在群里用语音朗读，每读一遍加5分。', '5', '1', '1', '5', '19', '1');
INSERT INTO `tb_task` VALUES ('208', '16', '组织演练美容师成长', '跟据每天预约的客户，带动美容师进行铺垫实战演练，每带动一个美容师演练1遍加3分。', '3', '1', '1', '6', '19', '1');
INSERT INTO `tb_task` VALUES ('209', '16', '督促检查信息回访', '督促检查美容师对当天来服务的客户进行回访，确保每天的客户全部回访！加5分。', '5', '1', '1', '7', '19', '1');
INSERT INTO `tb_task` VALUES ('210', '16', '考核美容师专业', '专业美容体现的就是专业，每天都要考核美容师专业知识，让美容师成长！加5分。', '5', '1', '1', '8', '19', '1');
INSERT INTO `tb_task` VALUES ('211', '16', '考核美容师手法', '跟据月主推产品，对美容师进行手法考核，统一美容师手法！加5分。', '5', '1', '1', '9', '19', '1');
INSERT INTO `tb_task` VALUES ('212', '16', '准备及组织周总结计划会', '组织好大家及时开会，让大家清晰上一周不足的地方，及接下来如何改正！再明确下周目标及业绩路径，加10分。', '10', '1', '2', '1', '19', '1');
INSERT INTO `tb_task` VALUES ('213', '16', '组织每周大扫除', '整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '19', '1');
INSERT INTO `tb_task` VALUES ('214', '16', '完成每周目标', '完成每周的业绩目标，即可申请加5分。', '5', '1', '2', '3', '19', '1');
INSERT INTO `tb_task` VALUES ('215', '16', '每月一次目标规划会议', '每月30日做下个月的业绩规划，并设定清晰的目标，有清晰的业路径！', '3', '1', '3', '1', '19', '1');
INSERT INTO `tb_task` VALUES ('216', '16', '完成当月目标', '完成当月的业绩目标，加20分。', '20', '1', '3', '2', '19', '1');
INSERT INTO `tb_task` VALUES ('217', '16', '整理好仪容仪表做考勤', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '20', '1');
INSERT INTO `tb_task` VALUES ('218', '16', '明确日目标', '每天清楚今日的业绩目标，在晨会上再次明细今日业绩路径，带动美容完成业绩目标。', '3', '1', '1', '2', '20', '1');
INSERT INTO `tb_task` VALUES ('219', '16', '跟据接待的客户进行演练', '分析每个客户，检查美容师能否熟练的接待客户，做好铺垫，每个客户来之前都带领接待的美容师进行演练，直到熟练。', '3', '1', '1', '3', '20', '1');
INSERT INTO `tb_task` VALUES ('220', '16', '完成日目标', '完成每日业绩目标，即可申请10分', '10', '1', '1', '4', '20', '1');
INSERT INTO `tb_task` VALUES ('221', '16', '主动汇报当日工作', '跟据每日业绩完成情况电话给老板汇报一天完成业绩工作情况，申请5分！', '5', '1', '1', '5', '20', '1');
INSERT INTO `tb_task` VALUES ('222', '16', '周总结计划会议', '重点总结上周业绩完成情况，及大家配合的默契度，演练如何更好配合；再明确下周业绩目标，让每个人有清晰的路径。', '3', '1', '2', '1', '20', '1');
INSERT INTO `tb_task` VALUES ('223', '16', '完成周目标', '完成每周设定的业绩目标，即加20分', '20', '1', '2', '2', '20', '1');
INSERT INTO `tb_task` VALUES ('224', '16', '卫生大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '3', '20', '1');
INSERT INTO `tb_task` VALUES ('225', '16', '明确每月业绩路径', '确保每个月的业绩路径清晰，业绩明细要到顾客、到品项、到美容师。', '3', '1', '3', '1', '20', '1');
INSERT INTO `tb_task` VALUES ('226', '16', '完成月业绩目标', '完成每月业绩目标，即可申请20分。', '3', '1', '3', '2', '20', '1');
INSERT INTO `tb_task` VALUES ('227', '16', '整理仪容仪表', '每天8:30前整理好仪容仪表，拍照打卡，申请积分', '3', '1', '1', '1', '21', '1');
INSERT INTO `tb_task` VALUES ('228', '16', '及时整理好环境卫生', '每天上下班要整理好自己所负责区域的卫生，做好顾客整理好房间卫生，拍照申请积分！', '3', '1', '1', '2', '21', '1');
INSERT INTO `tb_task` VALUES ('229', '16', '积极参加晨会', '对昨天的工作进行总结和今天的工作进行计划，明确今天的目标及要做的工作。', '3', '1', '1', '3', '21', '1');
INSERT INTO `tb_task` VALUES ('230', '16', '预约顾客确定护理时间', '每天主动预约顾客，确定客户的护理时间，每预约确定一个客户申请5分！', '5', '1', '1', '4', '21', '1');
INSERT INTO `tb_task` VALUES ('231', '16', '提前为预约客户准备房间', '对每天确定来护理的顾客，准备好客户所需要的产品及房间！', '3', '1', '1', '5', '21', '1');
INSERT INTO `tb_task` VALUES ('232', '16', '每日专业学习成长', '每天学习当月主推产品专业，在群里用语音读出来，每学习一遍可申请5分。', '5', '1', '1', '6', '21', '1');
INSERT INTO `tb_task` VALUES ('233', '16', '演练铺垫话术', '根据每天接待的顾客，组织相互演练铺垫的产品话术，每遍申请5分。', '5', '1', '1', '7', '21', '1');
INSERT INTO `tb_task` VALUES ('234', '16', '售后信息回访', '针对当天的操作客户，进行信息回访！每个客户申请3分。', '3', '1', '1', '8', '21', '1');
INSERT INTO `tb_task` VALUES ('235', '16', '月主推产品手法练习', '每练习一遍手法，每个动作不少与7遍，一个项目整体手法完成加5分。', '5', '1', '1', '9', '21', '1');
INSERT INTO `tb_task` VALUES ('236', '16', '迎宾30分钟', '每天在门口迎宾30分钟，标准化站姿迎接客户，每30分钟申请10分！', '10', '1', '1', '10', '21', '1');
INSERT INTO `tb_task` VALUES ('237', '16', '主动汇报当日工作', '每日下班前在群里发布文字或者电话给老板汇报一天的工作，申请5分！', '5', '1', '1', '11', '21', '1');
INSERT INTO `tb_task` VALUES ('238', '16', '参加周总结计划会议', '每周六召开一次周总结会议，找出自己在这一周中不足的地方，然后如何改正，明确下周目标及业绩来源路经。积分10分。', '10', '1', '2', '1', '21', '1');
INSERT INTO `tb_task` VALUES ('239', '16', '每周一次大扫除', '每周整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '21', '1');
INSERT INTO `tb_task` VALUES ('240', '16', '完成每周的业绩目标', '每周目标明确，一周内完成周目标，即可申请加10分。', '10', '1', '2', '3', '21', '1');
INSERT INTO `tb_task` VALUES ('241', '16', '每月一次目标规划会议', '每月30日做好下个月业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '21', '1');
INSERT INTO `tb_task` VALUES ('242', '16', '完成当月目标', '在一个月内达成月目标，即可申请20分！', '20', '1', '3', '2', '21', '1');
INSERT INTO `tb_task` VALUES ('243', '16', '整理仪容仪表考勤打卡', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '22', '1');
INSERT INTO `tb_task` VALUES ('244', '16', '准备好大厅的茶水', '每天准备好大厅的茶水，确保及时给客户奉上一杯茶。', '3', '1', '1', '2', '22', '1');
INSERT INTO `tb_task` VALUES ('245', '16', '大厅、咨询室卫生', '随时随地保持大厅、咨询室卫生干净整洁。', '3', '1', '1', '3', '22', '1');
INSERT INTO `tb_task` VALUES ('246', '16', '客户生日安排', '每天整理好客户生日，及时汇报及安排客户的生日活动！', '3', '1', '1', '4', '22', '1');
INSERT INTO `tb_task` VALUES ('247', '16', '客户经期特殊关怀', '了解客户的月经周期，建立档案，给客户特殊的关怀，热水袋、红糖水，及短息关怀家里的注意事项！', '3', '1', '1', '5', '22', '1');
INSERT INTO `tb_task` VALUES ('248', '16', '前台时刻保持干净整洁', '每天前台一整天保持干净，每天申请一次积分！', '3', '1', '1', '6', '22', '1');
INSERT INTO `tb_task` VALUES ('249', '16', '打20个招聘电话', '每天打20个招聘电话，申请3分，入职1个同事加3分。', '3', '1', '1', '7', '22', '1');
INSERT INTO `tb_task` VALUES ('250', '16', '售后信息回访', '对当日操作的客户，进行信息回访！', '3', '1', '1', '8', '22', '1');
INSERT INTO `tb_task` VALUES ('251', '16', '周总结计划会', '总结一周的工作内容，找出3点不足之处加以改正，下周工作内容及工作量要明确。', '3', '1', '2', '1', '22', '1');
INSERT INTO `tb_task` VALUES ('252', '16', '每周大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '22', '1');
INSERT INTO `tb_task` VALUES ('253', '16', '月总结计划', '指出3点上个月不足的地方，如何改正，下个月工作任务要清晰，工作量明确。', '3', '1', '3', '1', '22', '1');
INSERT INTO `tb_task` VALUES ('254', '17', '让团队职业化', '让团队懂得规则的重要性，发生的每个问题，都要进行沟通让团队成员明理，懂得按规矩做事！', '3', '1', '1', '1', '23', '1');
INSERT INTO `tb_task` VALUES ('255', '17', '帮助员工成长', '根据美容师目前的能力，设定好学习内容，让美容师每半个月学习一个专业，然后定时考核。', '3', '1', '1', '2', '23', '1');
INSERT INTO `tb_task` VALUES ('256', '17', '帮助美容师完成目标', '通过训练使用美容师的能力，而不是抛开美容师自己做业绩给美容师，要让美容师有做业绩的能力。', '3', '1', '1', '3', '23', '1');
INSERT INTO `tb_task` VALUES ('257', '17', '给员工幸福感', '美容师表现好在总结会议上进行表杨，完成目标了要及时给予奖励，让团队有成就感、幸福感。', '3', '1', '1', '4', '23', '1');
INSERT INTO `tb_task` VALUES ('258', '17', '让团队方向明确', '每天要让团队业绩来自于哪个品项、哪个客户，以问为主，让美容师来回答。通过发问让美容师理清思路。', '3', '1', '1', '5', '23', '1');
INSERT INTO `tb_task` VALUES ('259', '17', '周总结计划会', '每周开一次周总结会议，决定店里哪些行为需要改变的进行标准化，重点表扬上周表现优秀的人，给员工幸福感和成就感。', '3', '1', '2', '1', '23', '1');
INSERT INTO `tb_task` VALUES ('260', '17', '月目标规划会议', '每月25日-30日前做好下个月的业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '23', '1');
INSERT INTO `tb_task` VALUES ('261', '17', '安排好月学习专业及手法', '跟据下个月目标，设定好下月的学习的专业及项目手法，让团队能力得到成长。', '3', '1', '3', '2', '23', '1');
INSERT INTO `tb_task` VALUES ('262', '17', '月总结会议', '找出店里不足的地方，进行标准化改正，重点要表扬美容师后，带动氛围让美容师积分抽奖。', '3', '1', '3', '3', '23', '1');
INSERT INTO `tb_task` VALUES ('263', '17', '整理及检查团队仪容仪表', '每天上班第一件事，就是整理好自己的仪容仪表，并检查团队仪容仪表，拍照上传！即可加5分！', '5', '1', '1', '1', '24', '1');
INSERT INTO `tb_task` VALUES ('264', '17', '检查整体卫生', '上班第二件事就是检查整店的环境卫生，物品要摆放整齐，督促区域卫生负责人做好区域卫生！', '3', '1', '1', '2', '24', '1');
INSERT INTO `tb_task` VALUES ('265', '17', '组织团队开晨会', '让每个人目标明确，总结昨天不好的行为及工作方方法，安排好今天的工作！跳个舞结束今天的会议！加10分。', '10', '1', '1', '3', '24', '1');
INSERT INTO `tb_task` VALUES ('266', '17', '督促客户预约', '开完晨会督促美容师做好客户预约，团队每确定预约5个客户加5分！', '5', '1', '1', '4', '24', '1');
INSERT INTO `tb_task` VALUES ('267', '17', '每日专业学习成长', '根据每月主推产品专业，做好榜样在群里用语音朗读，每读一遍加5分。', '5', '1', '1', '5', '24', '1');
INSERT INTO `tb_task` VALUES ('268', '17', '组织演练美容师成长', '跟据每天预约的客户，带动美容师进行铺垫实战演练，每带动一个美容师演练1遍加3分。', '3', '1', '1', '6', '24', '1');
INSERT INTO `tb_task` VALUES ('269', '17', '督促检查信息回访', '督促检查美容师对当天来服务的客户进行回访，确保每天的客户全部回访！加5分。', '5', '1', '1', '7', '24', '1');
INSERT INTO `tb_task` VALUES ('270', '17', '考核美容师专业', '专业美容体现的就是专业，每天都要考核美容师专业知识，让美容师成长！加5分。', '5', '1', '1', '8', '24', '1');
INSERT INTO `tb_task` VALUES ('271', '17', '考核美容师手法', '跟据月主推产品，对美容师进行手法考核，统一美容师手法！加5分。', '5', '1', '1', '9', '24', '1');
INSERT INTO `tb_task` VALUES ('272', '17', '准备及组织周总结计划会', '组织好大家及时开会，让大家清晰上一周不足的地方，及接下来如何改正！再明确下周目标及业绩路径，加10分。', '10', '1', '2', '1', '24', '1');
INSERT INTO `tb_task` VALUES ('273', '17', '组织每周大扫除', '整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '24', '1');
INSERT INTO `tb_task` VALUES ('274', '17', '完成每周目标', '完成每周的业绩目标，即可申请加5分。', '5', '1', '2', '3', '24', '1');
INSERT INTO `tb_task` VALUES ('275', '17', '每月一次目标规划会议', '每月30日做下个月的业绩规划，并设定清晰的目标，有清晰的业路径！', '3', '1', '3', '1', '24', '1');
INSERT INTO `tb_task` VALUES ('276', '17', '完成当月目标', '完成当月的业绩目标，加20分。', '20', '1', '3', '2', '24', '1');
INSERT INTO `tb_task` VALUES ('277', '17', '整理好仪容仪表做考勤', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '25', '1');
INSERT INTO `tb_task` VALUES ('278', '17', '明确日目标', '每天清楚今日的业绩目标，在晨会上再次明细今日业绩路径，带动美容完成业绩目标。', '3', '1', '1', '2', '25', '1');
INSERT INTO `tb_task` VALUES ('279', '17', '跟据接待的客户进行演练', '分析每个客户，检查美容师能否熟练的接待客户，做好铺垫，每个客户来之前都带领接待的美容师进行演练，直到熟练。', '3', '1', '1', '3', '25', '1');
INSERT INTO `tb_task` VALUES ('280', '17', '完成日目标', '完成每日业绩目标，即可申请10分', '10', '1', '1', '4', '25', '1');
INSERT INTO `tb_task` VALUES ('281', '17', '主动汇报当日工作', '跟据每日业绩完成情况电话给老板汇报一天完成业绩工作情况，申请5分！', '5', '1', '1', '5', '25', '1');
INSERT INTO `tb_task` VALUES ('282', '17', '周总结计划会议', '重点总结上周业绩完成情况，及大家配合的默契度，演练如何更好配合；再明确下周业绩目标，让每个人有清晰的路径。', '3', '1', '2', '1', '25', '1');
INSERT INTO `tb_task` VALUES ('283', '17', '完成周目标', '完成每周设定的业绩目标，即加20分', '20', '1', '2', '2', '25', '1');
INSERT INTO `tb_task` VALUES ('284', '17', '卫生大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '3', '25', '1');
INSERT INTO `tb_task` VALUES ('285', '17', '明确每月业绩路径', '确保每个月的业绩路径清晰，业绩明细要到顾客、到品项、到美容师。', '3', '1', '3', '1', '25', '1');
INSERT INTO `tb_task` VALUES ('286', '17', '完成月业绩目标', '完成每月业绩目标，即可申请20分。', '3', '1', '3', '2', '25', '1');
INSERT INTO `tb_task` VALUES ('287', '17', '整理仪容仪表', '每天8:30前整理好仪容仪表，拍照打卡，申请积分', '3', '1', '1', '1', '26', '1');
INSERT INTO `tb_task` VALUES ('288', '17', '及时整理好环境卫生', '每天上下班要整理好自己所负责区域的卫生，做好顾客整理好房间卫生，拍照申请积分！', '3', '1', '1', '2', '26', '1');
INSERT INTO `tb_task` VALUES ('289', '17', '积极参加晨会', '对昨天的工作进行总结和今天的工作进行计划，明确今天的目标及要做的工作。', '3', '1', '1', '3', '26', '1');
INSERT INTO `tb_task` VALUES ('290', '17', '预约顾客确定护理时间', '每天主动预约顾客，确定客户的护理时间，每预约确定一个客户申请5分！', '5', '1', '1', '4', '26', '1');
INSERT INTO `tb_task` VALUES ('291', '17', '提前为预约客户准备房间', '对每天确定来护理的顾客，准备好客户所需要的产品及房间！', '3', '1', '1', '5', '26', '1');
INSERT INTO `tb_task` VALUES ('292', '17', '每日专业学习成长', '每天学习当月主推产品专业，在群里用语音读出来，每学习一遍可申请5分。', '5', '1', '1', '6', '26', '1');
INSERT INTO `tb_task` VALUES ('293', '17', '演练铺垫话术', '根据每天接待的顾客，组织相互演练铺垫的产品话术，每遍申请5分。', '5', '1', '1', '7', '26', '1');
INSERT INTO `tb_task` VALUES ('294', '17', '售后信息回访', '针对当天的操作客户，进行信息回访！每个客户申请3分。', '3', '1', '1', '8', '26', '1');
INSERT INTO `tb_task` VALUES ('295', '17', '月主推产品手法练习', '每练习一遍手法，每个动作不少与7遍，一个项目整体手法完成加5分。', '5', '1', '1', '9', '26', '1');
INSERT INTO `tb_task` VALUES ('296', '17', '迎宾30分钟', '每天在门口迎宾30分钟，标准化站姿迎接客户，每30分钟申请10分！', '10', '1', '1', '10', '26', '1');
INSERT INTO `tb_task` VALUES ('297', '17', '主动汇报当日工作', '每日下班前在群里发布文字或者电话给老板汇报一天的工作，申请5分！', '5', '1', '1', '11', '26', '1');
INSERT INTO `tb_task` VALUES ('298', '17', '参加周总结计划会议', '每周六召开一次周总结会议，找出自己在这一周中不足的地方，然后如何改正，明确下周目标及业绩来源路经。积分10分。', '10', '1', '2', '1', '26', '1');
INSERT INTO `tb_task` VALUES ('299', '17', '每周一次大扫除', '每周整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '26', '1');
INSERT INTO `tb_task` VALUES ('300', '17', '完成每周的业绩目标', '每周目标明确，一周内完成周目标，即可申请加10分。', '10', '1', '2', '3', '26', '1');
INSERT INTO `tb_task` VALUES ('301', '17', '每月一次目标规划会议', '每月30日做好下个月业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '26', '1');
INSERT INTO `tb_task` VALUES ('302', '17', '完成当月目标', '在一个月内达成月目标，即可申请20分！', '20', '1', '3', '2', '26', '1');
INSERT INTO `tb_task` VALUES ('303', '17', '整理仪容仪表考勤打卡', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '27', '1');
INSERT INTO `tb_task` VALUES ('304', '17', '准备好大厅的茶水', '每天准备好大厅的茶水，确保及时给客户奉上一杯茶。', '3', '1', '1', '2', '27', '1');
INSERT INTO `tb_task` VALUES ('305', '17', '大厅、咨询室卫生', '随时随地保持大厅、咨询室卫生干净整洁。', '3', '1', '1', '3', '27', '1');
INSERT INTO `tb_task` VALUES ('306', '17', '客户生日安排', '每天整理好客户生日，及时汇报及安排客户的生日活动！', '3', '1', '1', '4', '27', '1');
INSERT INTO `tb_task` VALUES ('307', '17', '客户经期特殊关怀', '了解客户的月经周期，建立档案，给客户特殊的关怀，热水袋、红糖水，及短息关怀家里的注意事项！', '3', '1', '1', '5', '27', '1');
INSERT INTO `tb_task` VALUES ('308', '17', '前台时刻保持干净整洁', '每天前台一整天保持干净，每天申请一次积分！', '3', '1', '1', '6', '27', '1');
INSERT INTO `tb_task` VALUES ('309', '17', '打20个招聘电话', '每天打20个招聘电话，申请3分，入职1个同事加3分。', '3', '1', '1', '7', '27', '1');
INSERT INTO `tb_task` VALUES ('310', '17', '售后信息回访', '对当日操作的客户，进行信息回访！', '3', '1', '1', '8', '27', '1');
INSERT INTO `tb_task` VALUES ('311', '17', '周总结计划会', '总结一周的工作内容，找出3点不足之处加以改正，下周工作内容及工作量要明确。', '3', '1', '2', '1', '27', '1');
INSERT INTO `tb_task` VALUES ('312', '17', '每周大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '27', '1');
INSERT INTO `tb_task` VALUES ('313', '17', '月总结计划', '指出3点上个月不足的地方，如何改正，下个月工作任务要清晰，工作量明确。', '3', '1', '3', '1', '27', '1');
INSERT INTO `tb_task` VALUES ('314', '18', '让团队职业化', '让团队懂得规则的重要性，发生的每个问题，都要进行沟通让团队成员明理，懂得按规矩做事！', '3', '1', '1', '1', '28', '1');
INSERT INTO `tb_task` VALUES ('315', '18', '帮助员工成长', '根据美容师目前的能力，设定好学习内容，让美容师每半个月学习一个专业，然后定时考核。', '3', '1', '1', '2', '28', '1');
INSERT INTO `tb_task` VALUES ('316', '18', '帮助美容师完成目标', '通过训练使用美容师的能力，而不是抛开美容师自己做业绩给美容师，要让美容师有做业绩的能力。', '3', '1', '1', '3', '28', '1');
INSERT INTO `tb_task` VALUES ('317', '18', '给员工幸福感', '美容师表现好在总结会议上进行表杨，完成目标了要及时给予奖励，让团队有成就感、幸福感。', '3', '1', '1', '4', '28', '1');
INSERT INTO `tb_task` VALUES ('318', '18', '让团队方向明确', '每天要让团队业绩来自于哪个品项、哪个客户，以问为主，让美容师来回答。通过发问让美容师理清思路。', '3', '1', '1', '5', '28', '1');
INSERT INTO `tb_task` VALUES ('319', '18', '周总结计划会', '每周开一次周总结会议，决定店里哪些行为需要改变的进行标准化，重点表扬上周表现优秀的人，给员工幸福感和成就感。', '3', '1', '2', '1', '28', '1');
INSERT INTO `tb_task` VALUES ('320', '18', '月目标规划会议', '每月25日-30日前做好下个月的业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '28', '1');
INSERT INTO `tb_task` VALUES ('321', '18', '安排好月学习专业及手法', '跟据下个月目标，设定好下月的学习的专业及项目手法，让团队能力得到成长。', '3', '1', '3', '2', '28', '1');
INSERT INTO `tb_task` VALUES ('322', '18', '月总结会议', '找出店里不足的地方，进行标准化改正，重点要表扬美容师后，带动氛围让美容师积分抽奖。', '3', '1', '3', '3', '28', '1');
INSERT INTO `tb_task` VALUES ('323', '18', '整理及检查团队仪容仪表', '每天上班第一件事，就是整理好自己的仪容仪表，并检查团队仪容仪表，拍照上传！即可加5分！', '5', '1', '1', '1', '29', '1');
INSERT INTO `tb_task` VALUES ('324', '18', '检查整体卫生', '上班第二件事就是检查整店的环境卫生，物品要摆放整齐，督促区域卫生负责人做好区域卫生！', '3', '1', '1', '2', '29', '1');
INSERT INTO `tb_task` VALUES ('325', '18', '组织团队开晨会', '让每个人目标明确，总结昨天不好的行为及工作方方法，安排好今天的工作！跳个舞结束今天的会议！加10分。', '10', '1', '1', '3', '29', '1');
INSERT INTO `tb_task` VALUES ('326', '18', '督促客户预约', '开完晨会督促美容师做好客户预约，团队每确定预约5个客户加5分！', '5', '1', '1', '4', '29', '1');
INSERT INTO `tb_task` VALUES ('327', '18', '每日专业学习成长', '根据每月主推产品专业，做好榜样在群里用语音朗读，每读一遍加5分。', '5', '1', '1', '5', '29', '1');
INSERT INTO `tb_task` VALUES ('328', '18', '组织演练美容师成长', '跟据每天预约的客户，带动美容师进行铺垫实战演练，每带动一个美容师演练1遍加3分。', '3', '1', '1', '6', '29', '1');
INSERT INTO `tb_task` VALUES ('329', '18', '督促检查信息回访', '督促检查美容师对当天来服务的客户进行回访，确保每天的客户全部回访！加5分。', '5', '1', '1', '7', '29', '1');
INSERT INTO `tb_task` VALUES ('330', '18', '考核美容师专业', '专业美容体现的就是专业，每天都要考核美容师专业知识，让美容师成长！加5分。', '5', '1', '1', '8', '29', '1');
INSERT INTO `tb_task` VALUES ('331', '18', '考核美容师手法', '跟据月主推产品，对美容师进行手法考核，统一美容师手法！加5分。', '5', '1', '1', '9', '29', '1');
INSERT INTO `tb_task` VALUES ('332', '18', '准备及组织周总结计划会', '组织好大家及时开会，让大家清晰上一周不足的地方，及接下来如何改正！再明确下周目标及业绩路径，加10分。', '10', '1', '2', '1', '29', '1');
INSERT INTO `tb_task` VALUES ('333', '18', '组织每周大扫除', '整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '29', '1');
INSERT INTO `tb_task` VALUES ('334', '18', '完成每周目标', '完成每周的业绩目标，即可申请加5分。', '5', '1', '2', '3', '29', '1');
INSERT INTO `tb_task` VALUES ('335', '18', '每月一次目标规划会议', '每月30日做下个月的业绩规划，并设定清晰的目标，有清晰的业路径！', '3', '1', '3', '1', '29', '1');
INSERT INTO `tb_task` VALUES ('336', '18', '完成当月目标', '完成当月的业绩目标，加20分。', '20', '1', '3', '2', '29', '1');
INSERT INTO `tb_task` VALUES ('337', '18', '整理好仪容仪表做考勤', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '30', '1');
INSERT INTO `tb_task` VALUES ('338', '18', '明确日目标', '每天清楚今日的业绩目标，在晨会上再次明细今日业绩路径，带动美容完成业绩目标。', '3', '1', '1', '2', '30', '1');
INSERT INTO `tb_task` VALUES ('339', '18', '跟据接待的客户进行演练', '分析每个客户，检查美容师能否熟练的接待客户，做好铺垫，每个客户来之前都带领接待的美容师进行演练，直到熟练。', '3', '1', '1', '3', '30', '1');
INSERT INTO `tb_task` VALUES ('340', '18', '完成日目标', '完成每日业绩目标，即可申请10分', '10', '1', '1', '4', '30', '1');
INSERT INTO `tb_task` VALUES ('341', '18', '主动汇报当日工作', '跟据每日业绩完成情况电话给老板汇报一天完成业绩工作情况，申请5分！', '5', '1', '1', '5', '30', '1');
INSERT INTO `tb_task` VALUES ('342', '18', '周总结计划会议', '重点总结上周业绩完成情况，及大家配合的默契度，演练如何更好配合；再明确下周业绩目标，让每个人有清晰的路径。', '3', '1', '2', '1', '30', '1');
INSERT INTO `tb_task` VALUES ('343', '18', '完成周目标', '完成每周设定的业绩目标，即加20分', '20', '1', '2', '2', '30', '1');
INSERT INTO `tb_task` VALUES ('344', '18', '卫生大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '3', '30', '1');
INSERT INTO `tb_task` VALUES ('345', '18', '明确每月业绩路径', '确保每个月的业绩路径清晰，业绩明细要到顾客、到品项、到美容师。', '3', '1', '3', '1', '30', '1');
INSERT INTO `tb_task` VALUES ('346', '18', '完成月业绩目标', '完成每月业绩目标，即可申请20分。', '3', '1', '3', '2', '30', '1');
INSERT INTO `tb_task` VALUES ('347', '18', '整理仪容仪表', '每天8:30前整理好仪容仪表，拍照打卡，申请积分', '3', '1', '1', '1', '31', '1');
INSERT INTO `tb_task` VALUES ('348', '18', '及时整理好环境卫生', '每天上下班要整理好自己所负责区域的卫生，做好顾客整理好房间卫生，拍照申请积分！', '3', '1', '1', '2', '31', '1');
INSERT INTO `tb_task` VALUES ('349', '18', '积极参加晨会', '对昨天的工作进行总结和今天的工作进行计划，明确今天的目标及要做的工作。', '3', '1', '1', '3', '31', '1');
INSERT INTO `tb_task` VALUES ('350', '18', '预约顾客确定护理时间', '每天主动预约顾客，确定客户的护理时间，每预约确定一个客户申请5分！', '5', '1', '1', '4', '31', '1');
INSERT INTO `tb_task` VALUES ('351', '18', '提前为预约客户准备房间', '对每天确定来护理的顾客，准备好客户所需要的产品及房间！', '3', '1', '1', '5', '31', '1');
INSERT INTO `tb_task` VALUES ('352', '18', '每日专业学习成长', '每天学习当月主推产品专业，在群里用语音读出来，每学习一遍可申请5分。', '5', '1', '1', '6', '31', '1');
INSERT INTO `tb_task` VALUES ('353', '18', '演练铺垫话术', '根据每天接待的顾客，组织相互演练铺垫的产品话术，每遍申请5分。', '5', '1', '1', '7', '31', '1');
INSERT INTO `tb_task` VALUES ('354', '18', '售后信息回访', '针对当天的操作客户，进行信息回访！每个客户申请3分。', '3', '1', '1', '8', '31', '1');
INSERT INTO `tb_task` VALUES ('355', '18', '月主推产品手法练习', '每练习一遍手法，每个动作不少与7遍，一个项目整体手法完成加5分。', '5', '1', '1', '9', '31', '1');
INSERT INTO `tb_task` VALUES ('356', '18', '迎宾30分钟', '每天在门口迎宾30分钟，标准化站姿迎接客户，每30分钟申请10分！', '10', '1', '1', '10', '31', '1');
INSERT INTO `tb_task` VALUES ('357', '18', '主动汇报当日工作', '每日下班前在群里发布文字或者电话给老板汇报一天的工作，申请5分！', '5', '1', '1', '11', '31', '1');
INSERT INTO `tb_task` VALUES ('358', '18', '参加周总结计划会议', '每周六召开一次周总结会议，找出自己在这一周中不足的地方，然后如何改正，明确下周目标及业绩来源路经。积分10分。', '10', '1', '2', '1', '31', '1');
INSERT INTO `tb_task` VALUES ('359', '18', '每周一次大扫除', '每周整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '31', '1');
INSERT INTO `tb_task` VALUES ('360', '18', '完成每周的业绩目标', '每周目标明确，一周内完成周目标，即可申请加10分。', '10', '1', '2', '3', '31', '1');
INSERT INTO `tb_task` VALUES ('361', '18', '每月一次目标规划会议', '每月30日做好下个月业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '31', '1');
INSERT INTO `tb_task` VALUES ('362', '18', '完成当月目标', '在一个月内达成月目标，即可申请20分！', '20', '1', '3', '2', '31', '1');
INSERT INTO `tb_task` VALUES ('363', '18', '整理仪容仪表考勤打卡', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '32', '1');
INSERT INTO `tb_task` VALUES ('364', '18', '准备好大厅的茶水', '每天准备好大厅的茶水，确保及时给客户奉上一杯茶。', '3', '1', '1', '2', '32', '1');
INSERT INTO `tb_task` VALUES ('365', '18', '大厅、咨询室卫生', '随时随地保持大厅、咨询室卫生干净整洁。', '3', '1', '1', '3', '32', '1');
INSERT INTO `tb_task` VALUES ('366', '18', '客户生日安排', '每天整理好客户生日，及时汇报及安排客户的生日活动！', '3', '1', '1', '4', '32', '1');
INSERT INTO `tb_task` VALUES ('367', '18', '客户经期特殊关怀', '了解客户的月经周期，建立档案，给客户特殊的关怀，热水袋、红糖水，及短息关怀家里的注意事项！', '3', '1', '1', '5', '32', '1');
INSERT INTO `tb_task` VALUES ('368', '18', '前台时刻保持干净整洁', '每天前台一整天保持干净，每天申请一次积分！', '3', '1', '1', '6', '32', '1');
INSERT INTO `tb_task` VALUES ('369', '18', '打20个招聘电话', '每天打20个招聘电话，申请3分，入职1个同事加3分。', '3', '1', '1', '7', '32', '1');
INSERT INTO `tb_task` VALUES ('370', '18', '售后信息回访', '对当日操作的客户，进行信息回访！', '3', '1', '1', '8', '32', '1');
INSERT INTO `tb_task` VALUES ('371', '18', '周总结计划会', '总结一周的工作内容，找出3点不足之处加以改正，下周工作内容及工作量要明确。', '3', '1', '2', '1', '32', '1');
INSERT INTO `tb_task` VALUES ('372', '18', '每周大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '32', '1');
INSERT INTO `tb_task` VALUES ('373', '18', '月总结计划', '指出3点上个月不足的地方，如何改正，下个月工作任务要清晰，工作量明确。', '3', '1', '3', '1', '32', '1');
INSERT INTO `tb_task` VALUES ('374', '19', '让团队职业化', '让团队懂得规则的重要性，发生的每个问题，都要进行沟通让团队成员明理，懂得按规矩做事！', '3', '1', '1', '1', '33', '1');
INSERT INTO `tb_task` VALUES ('375', '19', '帮助员工成长', '根据美容师目前的能力，设定好学习内容，让美容师每半个月学习一个专业，然后定时考核。', '3', '1', '1', '2', '33', '1');
INSERT INTO `tb_task` VALUES ('376', '19', '帮助美容师完成目标', '通过训练使用美容师的能力，而不是抛开美容师自己做业绩给美容师，要让美容师有做业绩的能力。', '3', '1', '1', '3', '33', '1');
INSERT INTO `tb_task` VALUES ('377', '19', '给员工幸福感', '美容师表现好在总结会议上进行表杨，完成目标了要及时给予奖励，让团队有成就感、幸福感。', '3', '1', '1', '4', '33', '1');
INSERT INTO `tb_task` VALUES ('378', '19', '让团队方向明确', '每天要让团队业绩来自于哪个品项、哪个客户，以问为主，让美容师来回答。通过发问让美容师理清思路。', '3', '1', '1', '5', '33', '1');
INSERT INTO `tb_task` VALUES ('379', '19', '周总结计划会', '每周开一次周总结会议，决定店里哪些行为需要改变的进行标准化，重点表扬上周表现优秀的人，给员工幸福感和成就感。', '3', '1', '2', '1', '33', '1');
INSERT INTO `tb_task` VALUES ('380', '19', '月目标规划会议', '每月25日-30日前做好下个月的业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '33', '1');
INSERT INTO `tb_task` VALUES ('381', '19', '安排好月学习专业及手法', '跟据下个月目标，设定好下月的学习的专业及项目手法，让团队能力得到成长。', '3', '1', '3', '2', '33', '1');
INSERT INTO `tb_task` VALUES ('382', '19', '月总结会议', '找出店里不足的地方，进行标准化改正，重点要表扬美容师后，带动氛围让美容师积分抽奖。', '3', '1', '3', '3', '33', '1');
INSERT INTO `tb_task` VALUES ('383', '19', '整理及检查团队仪容仪表', '每天上班第一件事，就是整理好自己的仪容仪表，并检查团队仪容仪表，拍照上传！即可加5分！', '5', '1', '1', '1', '34', '1');
INSERT INTO `tb_task` VALUES ('384', '19', '检查整体卫生', '上班第二件事就是检查整店的环境卫生，物品要摆放整齐，督促区域卫生负责人做好区域卫生！', '3', '1', '1', '2', '34', '1');
INSERT INTO `tb_task` VALUES ('385', '19', '组织团队开晨会', '让每个人目标明确，总结昨天不好的行为及工作方方法，安排好今天的工作！跳个舞结束今天的会议！加10分。', '10', '1', '1', '3', '34', '1');
INSERT INTO `tb_task` VALUES ('386', '19', '督促客户预约', '开完晨会督促美容师做好客户预约，团队每确定预约5个客户加5分！', '5', '1', '1', '4', '34', '1');
INSERT INTO `tb_task` VALUES ('387', '19', '每日专业学习成长', '根据每月主推产品专业，做好榜样在群里用语音朗读，每读一遍加5分。', '5', '1', '1', '5', '34', '1');
INSERT INTO `tb_task` VALUES ('388', '19', '组织演练美容师成长', '跟据每天预约的客户，带动美容师进行铺垫实战演练，每带动一个美容师演练1遍加3分。', '3', '1', '1', '6', '34', '1');
INSERT INTO `tb_task` VALUES ('389', '19', '督促检查信息回访', '督促检查美容师对当天来服务的客户进行回访，确保每天的客户全部回访！加5分。', '5', '1', '1', '7', '34', '1');
INSERT INTO `tb_task` VALUES ('390', '19', '考核美容师专业', '专业美容体现的就是专业，每天都要考核美容师专业知识，让美容师成长！加5分。', '5', '1', '1', '8', '34', '1');
INSERT INTO `tb_task` VALUES ('391', '19', '考核美容师手法', '跟据月主推产品，对美容师进行手法考核，统一美容师手法！加5分。', '5', '1', '1', '9', '34', '1');
INSERT INTO `tb_task` VALUES ('392', '19', '准备及组织周总结计划会', '组织好大家及时开会，让大家清晰上一周不足的地方，及接下来如何改正！再明确下周目标及业绩路径，加10分。', '10', '1', '2', '1', '34', '1');
INSERT INTO `tb_task` VALUES ('393', '19', '组织每周大扫除', '整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '34', '1');
INSERT INTO `tb_task` VALUES ('394', '19', '完成每周目标', '完成每周的业绩目标，即可申请加5分。', '5', '1', '2', '3', '34', '1');
INSERT INTO `tb_task` VALUES ('395', '19', '每月一次目标规划会议', '每月30日做下个月的业绩规划，并设定清晰的目标，有清晰的业路径！', '3', '1', '3', '1', '34', '1');
INSERT INTO `tb_task` VALUES ('396', '19', '完成当月目标', '完成当月的业绩目标，加20分。', '20', '1', '3', '2', '34', '1');
INSERT INTO `tb_task` VALUES ('397', '19', '整理好仪容仪表做考勤', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '35', '1');
INSERT INTO `tb_task` VALUES ('398', '19', '明确日目标', '每天清楚今日的业绩目标，在晨会上再次明细今日业绩路径，带动美容完成业绩目标。', '3', '1', '1', '2', '35', '1');
INSERT INTO `tb_task` VALUES ('399', '19', '跟据接待的客户进行演练', '分析每个客户，检查美容师能否熟练的接待客户，做好铺垫，每个客户来之前都带领接待的美容师进行演练，直到熟练。', '3', '1', '1', '3', '35', '1');
INSERT INTO `tb_task` VALUES ('400', '19', '完成日目标', '完成每日业绩目标，即可申请10分', '10', '1', '1', '4', '35', '1');
INSERT INTO `tb_task` VALUES ('401', '19', '主动汇报当日工作', '跟据每日业绩完成情况电话给老板汇报一天完成业绩工作情况，申请5分！', '5', '1', '1', '5', '35', '1');
INSERT INTO `tb_task` VALUES ('402', '19', '周总结计划会议', '重点总结上周业绩完成情况，及大家配合的默契度，演练如何更好配合；再明确下周业绩目标，让每个人有清晰的路径。', '3', '1', '2', '1', '35', '1');
INSERT INTO `tb_task` VALUES ('403', '19', '完成周目标', '完成每周设定的业绩目标，即加20分', '20', '1', '2', '2', '35', '1');
INSERT INTO `tb_task` VALUES ('404', '19', '卫生大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '3', '35', '1');
INSERT INTO `tb_task` VALUES ('405', '19', '明确每月业绩路径', '确保每个月的业绩路径清晰，业绩明细要到顾客、到品项、到美容师。', '3', '1', '3', '1', '35', '1');
INSERT INTO `tb_task` VALUES ('406', '19', '完成月业绩目标', '完成每月业绩目标，即可申请20分。', '3', '1', '3', '2', '35', '1');
INSERT INTO `tb_task` VALUES ('407', '19', '整理仪容仪表', '每天8:30前整理好仪容仪表，拍照打卡，申请积分', '3', '1', '1', '1', '36', '1');
INSERT INTO `tb_task` VALUES ('408', '19', '及时整理好环境卫生', '每天上下班要整理好自己所负责区域的卫生，做好顾客整理好房间卫生，拍照申请积分！', '3', '1', '1', '2', '36', '1');
INSERT INTO `tb_task` VALUES ('409', '19', '积极参加晨会', '对昨天的工作进行总结和今天的工作进行计划，明确今天的目标及要做的工作。', '3', '1', '1', '3', '36', '1');
INSERT INTO `tb_task` VALUES ('410', '19', '预约顾客确定护理时间', '每天主动预约顾客，确定客户的护理时间，每预约确定一个客户申请5分！', '5', '1', '1', '4', '36', '1');
INSERT INTO `tb_task` VALUES ('411', '19', '提前为预约客户准备房间', '对每天确定来护理的顾客，准备好客户所需要的产品及房间！', '3', '1', '1', '5', '36', '1');
INSERT INTO `tb_task` VALUES ('412', '19', '每日专业学习成长', '每天学习当月主推产品专业，在群里用语音读出来，每学习一遍可申请5分。', '5', '1', '1', '6', '36', '1');
INSERT INTO `tb_task` VALUES ('413', '19', '演练铺垫话术', '根据每天接待的顾客，组织相互演练铺垫的产品话术，每遍申请5分。', '5', '1', '1', '7', '36', '1');
INSERT INTO `tb_task` VALUES ('414', '19', '售后信息回访', '针对当天的操作客户，进行信息回访！每个客户申请3分。', '3', '1', '1', '8', '36', '1');
INSERT INTO `tb_task` VALUES ('415', '19', '月主推产品手法练习', '每练习一遍手法，每个动作不少与7遍，一个项目整体手法完成加5分。', '5', '1', '1', '9', '36', '1');
INSERT INTO `tb_task` VALUES ('416', '19', '迎宾30分钟', '每天在门口迎宾30分钟，标准化站姿迎接客户，每30分钟申请10分！', '10', '1', '1', '10', '36', '1');
INSERT INTO `tb_task` VALUES ('417', '19', '主动汇报当日工作', '每日下班前在群里发布文字或者电话给老板汇报一天的工作，申请5分！', '5', '1', '1', '11', '36', '1');
INSERT INTO `tb_task` VALUES ('418', '19', '参加周总结计划会议', '每周六召开一次周总结会议，找出自己在这一周中不足的地方，然后如何改正，明确下周目标及业绩来源路经。积分10分。', '10', '1', '2', '1', '36', '1');
INSERT INTO `tb_task` VALUES ('419', '19', '每周一次大扫除', '每周整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '36', '1');
INSERT INTO `tb_task` VALUES ('420', '19', '完成每周的业绩目标', '每周目标明确，一周内完成周目标，即可申请加10分。', '10', '1', '2', '3', '36', '1');
INSERT INTO `tb_task` VALUES ('421', '19', '每月一次目标规划会议', '每月30日做好下个月业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '36', '1');
INSERT INTO `tb_task` VALUES ('422', '19', '完成当月目标', '在一个月内达成月目标，即可申请20分！', '20', '1', '3', '2', '36', '1');
INSERT INTO `tb_task` VALUES ('423', '19', '整理仪容仪表考勤打卡', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '37', '1');
INSERT INTO `tb_task` VALUES ('424', '19', '准备好大厅的茶水', '每天准备好大厅的茶水，确保及时给客户奉上一杯茶。', '3', '1', '1', '2', '37', '1');
INSERT INTO `tb_task` VALUES ('425', '19', '大厅、咨询室卫生', '随时随地保持大厅、咨询室卫生干净整洁。', '3', '1', '1', '3', '37', '1');
INSERT INTO `tb_task` VALUES ('426', '19', '客户生日安排', '每天整理好客户生日，及时汇报及安排客户的生日活动！', '3', '1', '1', '4', '37', '1');
INSERT INTO `tb_task` VALUES ('427', '19', '客户经期特殊关怀', '了解客户的月经周期，建立档案，给客户特殊的关怀，热水袋、红糖水，及短息关怀家里的注意事项！', '3', '1', '1', '5', '37', '1');
INSERT INTO `tb_task` VALUES ('428', '19', '前台时刻保持干净整洁', '每天前台一整天保持干净，每天申请一次积分！', '3', '1', '1', '6', '37', '1');
INSERT INTO `tb_task` VALUES ('429', '19', '打20个招聘电话', '每天打20个招聘电话，申请3分，入职1个同事加3分。', '3', '1', '1', '7', '37', '1');
INSERT INTO `tb_task` VALUES ('430', '19', '售后信息回访', '对当日操作的客户，进行信息回访！', '3', '1', '1', '8', '37', '1');
INSERT INTO `tb_task` VALUES ('431', '19', '周总结计划会', '总结一周的工作内容，找出3点不足之处加以改正，下周工作内容及工作量要明确。', '3', '1', '2', '1', '37', '1');
INSERT INTO `tb_task` VALUES ('432', '19', '每周大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '37', '1');
INSERT INTO `tb_task` VALUES ('433', '19', '月总结计划', '指出3点上个月不足的地方，如何改正，下个月工作任务要清晰，工作量明确。', '3', '1', '3', '1', '37', '1');
INSERT INTO `tb_task` VALUES ('434', '20', '让团队职业化', '让团队懂得规则的重要性，发生的每个问题，都要进行沟通让团队成员明理，懂得按规矩做事！', '3', '1', '1', '1', '38', '1');
INSERT INTO `tb_task` VALUES ('435', '20', '帮助员工成长', '根据美容师目前的能力，设定好学习内容，让美容师每半个月学习一个专业，然后定时考核。', '3', '1', '1', '2', '38', '1');
INSERT INTO `tb_task` VALUES ('436', '20', '帮助美容师完成目标', '通过训练使用美容师的能力，而不是抛开美容师自己做业绩给美容师，要让美容师有做业绩的能力。', '3', '1', '1', '3', '38', '1');
INSERT INTO `tb_task` VALUES ('437', '20', '给员工幸福感', '美容师表现好在总结会议上进行表杨，完成目标了要及时给予奖励，让团队有成就感、幸福感。', '3', '1', '1', '4', '38', '1');
INSERT INTO `tb_task` VALUES ('438', '20', '让团队方向明确', '每天要让团队业绩来自于哪个品项、哪个客户，以问为主，让美容师来回答。通过发问让美容师理清思路。', '3', '1', '1', '5', '38', '1');
INSERT INTO `tb_task` VALUES ('439', '20', '周总结计划会', '每周开一次周总结会议，决定店里哪些行为需要改变的进行标准化，重点表扬上周表现优秀的人，给员工幸福感和成就感。', '3', '1', '2', '1', '38', '1');
INSERT INTO `tb_task` VALUES ('440', '20', '月目标规划会议', '每月25日-30日前做好下个月的业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '38', '1');
INSERT INTO `tb_task` VALUES ('441', '20', '安排好月学习专业及手法', '跟据下个月目标，设定好下月的学习的专业及项目手法，让团队能力得到成长。', '3', '1', '3', '2', '38', '1');
INSERT INTO `tb_task` VALUES ('442', '20', '月总结会议', '找出店里不足的地方，进行标准化改正，重点要表扬美容师后，带动氛围让美容师积分抽奖。', '3', '1', '3', '3', '38', '1');
INSERT INTO `tb_task` VALUES ('443', '20', '整理及检查团队仪容仪表', '每天上班第一件事，就是整理好自己的仪容仪表，并检查团队仪容仪表，拍照上传！即可加5分！', '5', '1', '1', '1', '39', '1');
INSERT INTO `tb_task` VALUES ('444', '20', '检查整体卫生', '上班第二件事就是检查整店的环境卫生，物品要摆放整齐，督促区域卫生负责人做好区域卫生！', '3', '1', '1', '2', '39', '1');
INSERT INTO `tb_task` VALUES ('445', '20', '组织团队开晨会', '让每个人目标明确，总结昨天不好的行为及工作方方法，安排好今天的工作！跳个舞结束今天的会议！加10分。', '10', '1', '1', '3', '39', '1');
INSERT INTO `tb_task` VALUES ('446', '20', '督促客户预约', '开完晨会督促美容师做好客户预约，团队每确定预约5个客户加5分！', '5', '1', '1', '4', '39', '1');
INSERT INTO `tb_task` VALUES ('447', '20', '每日专业学习成长', '根据每月主推产品专业，做好榜样在群里用语音朗读，每读一遍加5分。', '5', '1', '1', '5', '39', '1');
INSERT INTO `tb_task` VALUES ('448', '20', '组织演练美容师成长', '跟据每天预约的客户，带动美容师进行铺垫实战演练，每带动一个美容师演练1遍加3分。', '3', '1', '1', '6', '39', '1');
INSERT INTO `tb_task` VALUES ('449', '20', '督促检查信息回访', '督促检查美容师对当天来服务的客户进行回访，确保每天的客户全部回访！加5分。', '5', '1', '1', '7', '39', '1');
INSERT INTO `tb_task` VALUES ('450', '20', '考核美容师专业', '专业美容体现的就是专业，每天都要考核美容师专业知识，让美容师成长！加5分。', '5', '1', '1', '8', '39', '1');
INSERT INTO `tb_task` VALUES ('451', '20', '考核美容师手法', '跟据月主推产品，对美容师进行手法考核，统一美容师手法！加5分。', '5', '1', '1', '9', '39', '1');
INSERT INTO `tb_task` VALUES ('452', '20', '准备及组织周总结计划会', '组织好大家及时开会，让大家清晰上一周不足的地方，及接下来如何改正！再明确下周目标及业绩路径，加10分。', '10', '1', '2', '1', '39', '1');
INSERT INTO `tb_task` VALUES ('453', '20', '组织每周大扫除', '整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '39', '1');
INSERT INTO `tb_task` VALUES ('454', '20', '完成每周目标', '完成每周的业绩目标，即可申请加5分。', '5', '1', '2', '3', '39', '1');
INSERT INTO `tb_task` VALUES ('455', '20', '每月一次目标规划会议', '每月30日做下个月的业绩规划，并设定清晰的目标，有清晰的业路径！', '3', '1', '3', '1', '39', '1');
INSERT INTO `tb_task` VALUES ('456', '20', '完成当月目标', '完成当月的业绩目标，加20分。', '20', '1', '3', '2', '39', '1');
INSERT INTO `tb_task` VALUES ('457', '20', '整理好仪容仪表做考勤', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '40', '1');
INSERT INTO `tb_task` VALUES ('458', '20', '明确日目标', '每天清楚今日的业绩目标，在晨会上再次明细今日业绩路径，带动美容完成业绩目标。', '3', '1', '1', '2', '40', '1');
INSERT INTO `tb_task` VALUES ('459', '20', '跟据接待的客户进行演练', '分析每个客户，检查美容师能否熟练的接待客户，做好铺垫，每个客户来之前都带领接待的美容师进行演练，直到熟练。', '3', '1', '1', '3', '40', '1');
INSERT INTO `tb_task` VALUES ('460', '20', '完成日目标', '完成每日业绩目标，即可申请10分', '10', '1', '1', '4', '40', '1');
INSERT INTO `tb_task` VALUES ('461', '20', '主动汇报当日工作', '跟据每日业绩完成情况电话给老板汇报一天完成业绩工作情况，申请5分！', '5', '1', '1', '5', '40', '1');
INSERT INTO `tb_task` VALUES ('462', '20', '周总结计划会议', '重点总结上周业绩完成情况，及大家配合的默契度，演练如何更好配合；再明确下周业绩目标，让每个人有清晰的路径。', '3', '1', '2', '1', '40', '1');
INSERT INTO `tb_task` VALUES ('463', '20', '完成周目标', '完成每周设定的业绩目标，即加20分', '20', '1', '2', '2', '40', '1');
INSERT INTO `tb_task` VALUES ('464', '20', '卫生大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '3', '40', '1');
INSERT INTO `tb_task` VALUES ('465', '20', '明确每月业绩路径', '确保每个月的业绩路径清晰，业绩明细要到顾客、到品项、到美容师。', '3', '1', '3', '1', '40', '1');
INSERT INTO `tb_task` VALUES ('466', '20', '完成月业绩目标', '完成每月业绩目标，即可申请20分。', '3', '1', '3', '2', '40', '1');
INSERT INTO `tb_task` VALUES ('467', '20', '整理仪容仪表', '每天8:30前整理好仪容仪表，拍照打卡，申请积分', '3', '1', '1', '1', '41', '1');
INSERT INTO `tb_task` VALUES ('468', '20', '及时整理好环境卫生', '每天上下班要整理好自己所负责区域的卫生，做好顾客整理好房间卫生，拍照申请积分！', '3', '1', '1', '2', '41', '1');
INSERT INTO `tb_task` VALUES ('469', '20', '积极参加晨会', '对昨天的工作进行总结和今天的工作进行计划，明确今天的目标及要做的工作。', '3', '1', '1', '3', '41', '1');
INSERT INTO `tb_task` VALUES ('470', '20', '预约顾客确定护理时间', '每天主动预约顾客，确定客户的护理时间，每预约确定一个客户申请5分！', '5', '1', '1', '4', '41', '1');
INSERT INTO `tb_task` VALUES ('471', '20', '提前为预约客户准备房间', '对每天确定来护理的顾客，准备好客户所需要的产品及房间！', '3', '1', '1', '5', '41', '1');
INSERT INTO `tb_task` VALUES ('472', '20', '每日专业学习成长', '每天学习当月主推产品专业，在群里用语音读出来，每学习一遍可申请5分。', '5', '1', '1', '6', '41', '1');
INSERT INTO `tb_task` VALUES ('473', '20', '演练铺垫话术', '根据每天接待的顾客，组织相互演练铺垫的产品话术，每遍申请5分。', '5', '1', '1', '7', '41', '1');
INSERT INTO `tb_task` VALUES ('474', '20', '售后信息回访', '针对当天的操作客户，进行信息回访！每个客户申请3分。', '3', '1', '1', '8', '41', '1');
INSERT INTO `tb_task` VALUES ('475', '20', '月主推产品手法练习', '每练习一遍手法，每个动作不少与7遍，一个项目整体手法完成加5分。', '5', '1', '1', '9', '41', '1');
INSERT INTO `tb_task` VALUES ('476', '20', '迎宾30分钟', '每天在门口迎宾30分钟，标准化站姿迎接客户，每30分钟申请10分！', '10', '1', '1', '10', '41', '1');
INSERT INTO `tb_task` VALUES ('477', '20', '主动汇报当日工作', '每日下班前在群里发布文字或者电话给老板汇报一天的工作，申请5分！', '5', '1', '1', '11', '41', '1');
INSERT INTO `tb_task` VALUES ('478', '20', '参加周总结计划会议', '每周六召开一次周总结会议，找出自己在这一周中不足的地方，然后如何改正，明确下周目标及业绩来源路经。积分10分。', '10', '1', '2', '1', '41', '1');
INSERT INTO `tb_task` VALUES ('479', '20', '每周一次大扫除', '每周整店大扫除要做好整理工作，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '41', '1');
INSERT INTO `tb_task` VALUES ('480', '20', '完成每周的业绩目标', '每周目标明确，一周内完成周目标，即可申请加10分。', '10', '1', '2', '3', '41', '1');
INSERT INTO `tb_task` VALUES ('481', '20', '每月一次目标规划会议', '每月30日做好下个月业绩规划，并设定清晰的目标，有清晰的业绩路径！', '3', '1', '3', '1', '41', '1');
INSERT INTO `tb_task` VALUES ('482', '20', '完成当月目标', '在一个月内达成月目标，即可申请20分！', '20', '1', '3', '2', '41', '1');
INSERT INTO `tb_task` VALUES ('483', '20', '整理仪容仪表考勤打卡', '每天上班前整理好仪容仪表，拍照打卡，完成一天的考勤。', '3', '1', '1', '1', '42', '1');
INSERT INTO `tb_task` VALUES ('484', '20', '准备好大厅的茶水', '每天准备好大厅的茶水，确保及时给客户奉上一杯茶。', '3', '1', '1', '2', '42', '1');
INSERT INTO `tb_task` VALUES ('485', '20', '大厅、咨询室卫生', '随时随地保持大厅、咨询室卫生干净整洁。', '3', '1', '1', '3', '42', '1');
INSERT INTO `tb_task` VALUES ('486', '20', '客户生日安排', '每天整理好客户生日，及时汇报及安排客户的生日活动！', '3', '1', '1', '4', '42', '1');
INSERT INTO `tb_task` VALUES ('487', '20', '客户经期特殊关怀', '了解客户的月经周期，建立档案，给客户特殊的关怀，热水袋、红糖水，及短息关怀家里的注意事项！', '3', '1', '1', '5', '42', '1');
INSERT INTO `tb_task` VALUES ('488', '20', '前台时刻保持干净整洁', '每天前台一整天保持干净，每天申请一次积分！', '3', '1', '1', '6', '42', '1');
INSERT INTO `tb_task` VALUES ('489', '20', '打20个招聘电话', '每天打20个招聘电话，申请3分，入职1个同事加3分。', '3', '1', '1', '7', '42', '1');
INSERT INTO `tb_task` VALUES ('490', '20', '售后信息回访', '对当日操作的客户，进行信息回访！', '3', '1', '1', '8', '42', '1');
INSERT INTO `tb_task` VALUES ('491', '20', '周总结计划会', '总结一周的工作内容，找出3点不足之处加以改正，下周工作内容及工作量要明确。', '3', '1', '2', '1', '42', '1');
INSERT INTO `tb_task` VALUES ('492', '20', '每周大扫除', '每周大扫除要做好5S整理，区分要用的和不用的，把不用的及时处理，把有用的摆放整齐。', '3', '1', '2', '2', '42', '1');
INSERT INTO `tb_task` VALUES ('493', '20', '月总结计划', '指出3点上个月不足的地方，如何改正，下个月工作任务要清晰，工作量明确。', '3', '1', '3', '1', '42', '1');
