/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : asany

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 22/12/2022 15:54:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for act_app_appdef
-- ----------------------------
DROP TABLE IF EXISTS `act_app_appdef`;
CREATE TABLE `act_app_appdef`
(
    `ID_`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`           int(11)                                 NOT NULL,
    `NAME_`          varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `KEY_`           varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `VERSION_`       int(11)                                 NOT NULL,
    `CATEGORY_`      varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DEPLOYMENT_ID_` varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `RESOURCE_NAME_` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DESCRIPTION_`   varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_APP_DEF_DPLY` (`DEPLOYMENT_ID_`),
    CONSTRAINT `ACT_FK_APP_DEF_DPLY` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_app_deployment` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_app_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_app_databasechangelog`;
CREATE TABLE `act_app_databasechangelog`
(
    `ID`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `AUTHOR`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FILENAME`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATEEXECUTED`  datetime                                NOT NULL,
    `ORDEREXECUTED` int(11)                                 NOT NULL,
    `EXECTYPE`      varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `MD5SUM`        varchar(35) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DESCRIPTION`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `COMMENTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TAG`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LIQUIBASE`     varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `CONTEXTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LABELS`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID` varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_app_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_app_databasechangeloglock`;
CREATE TABLE `act_app_databasechangeloglock`
(
    `ID`          int(11) NOT NULL,
    `LOCKED`      bit(1)  NOT NULL,
    `LOCKGRANTED` datetime                                DEFAULT NULL,
    `LOCKEDBY`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_app_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_app_deployment`;
CREATE TABLE `act_app_deployment`
(
    `ID_`          varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CATEGORY_`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `KEY_`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOY_TIME_` datetime(3)                             DEFAULT NULL,
    `TENANT_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_app_deployment_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_app_deployment_resource`;
CREATE TABLE `act_app_deployment_resource`
(
    `ID_`             varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID_`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `RESOURCE_BYTES_` longblob,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_APP_RSRC_DPL` (`DEPLOYMENT_ID_`),
    CONSTRAINT `ACT_FK_APP_RSRC_DPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_app_deployment` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_casedef
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_casedef`;
CREATE TABLE `act_cmmn_casedef`
(
    `ID_`                     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`                    int(11)                                 NOT NULL,
    `NAME_`                   varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `KEY_`                    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `VERSION_`                int(11)                                 NOT NULL,
    `CATEGORY_`               varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DEPLOYMENT_ID_`          varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `RESOURCE_NAME_`          varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DESCRIPTION_`            varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `HAS_GRAPHICAL_NOTATION_` bit(1)                                   DEFAULT NULL,
    `TENANT_ID_`              varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT '',
    `DGRM_RESOURCE_NAME_`     varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `HAS_START_FORM_KEY_`     bit(1)                                   DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_CASE_DEF_DPLY` (`DEPLOYMENT_ID_`),
    CONSTRAINT `ACT_FK_CASE_DEF_DPLY` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_cmmn_deployment` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_databasechangelog`;
CREATE TABLE `act_cmmn_databasechangelog`
(
    `ID`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `AUTHOR`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FILENAME`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATEEXECUTED`  datetime                                NOT NULL,
    `ORDEREXECUTED` int(11)                                 NOT NULL,
    `EXECTYPE`      varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `MD5SUM`        varchar(35) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DESCRIPTION`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `COMMENTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TAG`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LIQUIBASE`     varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `CONTEXTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LABELS`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID` varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_databasechangeloglock`;
CREATE TABLE `act_cmmn_databasechangeloglock`
(
    `ID`          int(11) NOT NULL,
    `LOCKED`      bit(1)  NOT NULL,
    `LOCKGRANTED` datetime                                DEFAULT NULL,
    `LOCKEDBY`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_deployment`;
CREATE TABLE `act_cmmn_deployment`
(
    `ID_`                   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CATEGORY_`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `KEY_`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOY_TIME_`          datetime(3)                             DEFAULT NULL,
    `PARENT_DEPLOYMENT_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_deployment_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_deployment_resource`;
CREATE TABLE `act_cmmn_deployment_resource`
(
    `ID_`             varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID_`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `RESOURCE_BYTES_` longblob,
    `GENERATED_`      bit(1)                                  DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_CMMN_RSRC_DPL` (`DEPLOYMENT_ID_`),
    CONSTRAINT `ACT_FK_CMMN_RSRC_DPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_cmmn_deployment` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_hi_case_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_hi_case_inst`;
CREATE TABLE `act_cmmn_hi_case_inst`
(
    `ID_`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`           int(11)                                 NOT NULL,
    `BUSINESS_KEY_`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `NAME_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `PARENT_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CASE_DEF_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STATE_`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `START_TIME_`    datetime(3)                             DEFAULT NULL,
    `END_TIME_`      datetime(3)                             DEFAULT NULL,
    `START_USER_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CALLBACK_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CALLBACK_TYPE_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_hi_mil_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_hi_mil_inst`;
CREATE TABLE `act_cmmn_hi_mil_inst`
(
    `ID_`           varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`          int(11)                                 NOT NULL,
    `NAME_`         varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TIME_STAMP_`   datetime(3)                             DEFAULT NULL,
    `CASE_INST_ID_` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `CASE_DEF_ID_`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `ELEMENT_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TENANT_ID_`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_hi_plan_item_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_hi_plan_item_inst`;
CREATE TABLE `act_cmmn_hi_plan_item_inst`
(
    `ID_`                   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`                  int(11)                                 NOT NULL,
    `NAME_`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STATE_`                varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CASE_DEF_ID_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CASE_INST_ID_`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STAGE_INST_ID_`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `IS_STAGE_`             bit(1)                                  DEFAULT NULL,
    `ELEMENT_ID_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ITEM_DEFINITION_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ITEM_DEFINITION_TYPE_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CREATED_TIME_`         datetime(3)                             DEFAULT NULL,
    `LAST_AVAILABLE_TIME_`  datetime(3)                             DEFAULT NULL,
    `LAST_ENABLED_TIME_`    datetime(3)                             DEFAULT NULL,
    `LAST_DISABLED_TIME_`   datetime(3)                             DEFAULT NULL,
    `LAST_STARTED_TIME_`    datetime(3)                             DEFAULT NULL,
    `LAST_SUSPENDED_TIME_`  datetime(3)                             DEFAULT NULL,
    `COMPLETED_TIME_`       datetime(3)                             DEFAULT NULL,
    `OCCURRED_TIME_`        datetime(3)                             DEFAULT NULL,
    `TERMINATED_TIME_`      datetime(3)                             DEFAULT NULL,
    `EXIT_TIME_`            datetime(3)                             DEFAULT NULL,
    `ENDED_TIME_`           datetime(3)                             DEFAULT NULL,
    `LAST_UPDATED_TIME_`    datetime(3)                             DEFAULT NULL,
    `START_USER_ID_`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `REFERENCE_ID_`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `REFERENCE_TYPE_`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_ru_case_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_case_inst`;
CREATE TABLE `act_cmmn_ru_case_inst`
(
    `ID_`              varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`             int(11)                                 NOT NULL,
    `BUSINESS_KEY_`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `NAME_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `PARENT_ID_`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CASE_DEF_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STATE_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `START_TIME_`      datetime(3)                             DEFAULT NULL,
    `START_USER_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CALLBACK_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CALLBACK_TYPE_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    `LOCK_TIME_`       datetime(3)                             DEFAULT NULL,
    `IS_COMPLETEABLE_` bit(1)                                  DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_CASE_INST_CASE_DEF` (`CASE_DEF_ID_`),
    KEY `ACT_IDX_CASE_INST_PARENT` (`PARENT_ID_`),
    CONSTRAINT `ACT_FK_CASE_INST_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_ru_mil_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_mil_inst`;
CREATE TABLE `act_cmmn_ru_mil_inst`
(
    `ID_`           varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`         varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TIME_STAMP_`   datetime(3)                             DEFAULT NULL,
    `CASE_INST_ID_` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `CASE_DEF_ID_`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `ELEMENT_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TENANT_ID_`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_MIL_CASE_DEF` (`CASE_DEF_ID_`),
    KEY `ACT_IDX_MIL_CASE_INST` (`CASE_INST_ID_`),
    CONSTRAINT `ACT_FK_MIL_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`),
    CONSTRAINT `ACT_FK_MIL_CASE_INST` FOREIGN KEY (`CASE_INST_ID_`) REFERENCES `act_cmmn_ru_case_inst` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_ru_plan_item_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_plan_item_inst`;
CREATE TABLE `act_cmmn_ru_plan_item_inst`
(
    `ID_`                     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`                    int(11)                                 NOT NULL,
    `CASE_DEF_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CASE_INST_ID_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STAGE_INST_ID_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `IS_STAGE_`               bit(1)                                  DEFAULT NULL,
    `ELEMENT_ID_`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `NAME_`                   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STATE_`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `START_TIME_`             datetime(3)                             DEFAULT NULL,
    `START_USER_ID_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `REFERENCE_ID_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `REFERENCE_TYPE_`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`              varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
    `ITEM_DEFINITION_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ITEM_DEFINITION_TYPE_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `IS_COMPLETEABLE_`        bit(1)                                  DEFAULT NULL,
    `IS_COUNT_ENABLED_`       bit(1)                                  DEFAULT NULL,
    `VAR_COUNT_`              int(11)                                 DEFAULT NULL,
    `SENTRY_PART_INST_COUNT_` int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_PLAN_ITEM_CASE_DEF` (`CASE_DEF_ID_`),
    KEY `ACT_IDX_PLAN_ITEM_CASE_INST` (`CASE_INST_ID_`),
    KEY `ACT_IDX_PLAN_ITEM_STAGE_INST` (`STAGE_INST_ID_`),
    CONSTRAINT `ACT_FK_PLAN_ITEM_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`),
    CONSTRAINT `ACT_FK_PLAN_ITEM_CASE_INST` FOREIGN KEY (`CASE_INST_ID_`) REFERENCES `act_cmmn_ru_case_inst` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_cmmn_ru_sentry_part_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_sentry_part_inst`;
CREATE TABLE `act_cmmn_ru_sentry_part_inst`
(
    `ID_`                varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REV_`               int(11)                                 NOT NULL,
    `CASE_DEF_ID_`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CASE_INST_ID_`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `PLAN_ITEM_INST_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ON_PART_ID_`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `IF_PART_ID_`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TIME_STAMP_`        datetime(3)                             DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_SENTRY_CASE_DEF` (`CASE_DEF_ID_`),
    KEY `ACT_IDX_SENTRY_CASE_INST` (`CASE_INST_ID_`),
    KEY `ACT_IDX_SENTRY_PLAN_ITEM` (`PLAN_ITEM_INST_ID_`),
    CONSTRAINT `ACT_FK_SENTRY_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`),
    CONSTRAINT `ACT_FK_SENTRY_CASE_INST` FOREIGN KEY (`CASE_INST_ID_`) REFERENCES `act_cmmn_ru_case_inst` (`ID_`),
    CONSTRAINT `ACT_FK_SENTRY_PLAN_ITEM` FOREIGN KEY (`PLAN_ITEM_INST_ID_`) REFERENCES `act_cmmn_ru_plan_item_inst` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_co_content_item
-- ----------------------------
DROP TABLE IF EXISTS `act_co_content_item`;
CREATE TABLE `act_co_content_item`
(
    `ID_`                 varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`               varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `MIME_TYPE_`          varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `TASK_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `PROC_INST_ID_`       varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `CONTENT_STORE_ID_`   varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `CONTENT_STORE_NAME_` varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `FIELD_`              varchar(400) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `CONTENT_AVAILABLE_`  bit(1)                                       DEFAULT b'0',
    `CREATED_`            timestamp(6)                            NULL DEFAULT NULL,
    `CREATED_BY_`         varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `LAST_MODIFIED_`      timestamp(6)                            NULL DEFAULT NULL,
    `LAST_MODIFIED_BY_`   varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `CONTENT_SIZE_`       bigint(20)                                   DEFAULT '0',
    `TENANT_ID_`          varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `SCOPE_ID_`           varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `SCOPE_TYPE_`         varchar(255) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `idx_contitem_taskid` (`TASK_ID_`),
    KEY `idx_contitem_procid` (`PROC_INST_ID_`),
    KEY `idx_contitem_scope` (`SCOPE_ID_`, `SCOPE_TYPE_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_co_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_co_databasechangelog`;
CREATE TABLE `act_co_databasechangelog`
(
    `ID`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `AUTHOR`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FILENAME`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATEEXECUTED`  datetime                                NOT NULL,
    `ORDEREXECUTED` int(11)                                 NOT NULL,
    `EXECTYPE`      varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `MD5SUM`        varchar(35) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DESCRIPTION`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `COMMENTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TAG`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LIQUIBASE`     varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `CONTEXTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LABELS`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID` varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_co_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_co_databasechangeloglock`;
CREATE TABLE `act_co_databasechangeloglock`
(
    `ID`          int(11) NOT NULL,
    `LOCKED`      bit(1)  NOT NULL,
    `LOCKGRANTED` datetime                                DEFAULT NULL,
    `LOCKEDBY`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_de_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_de_databasechangelog`;
CREATE TABLE `act_de_databasechangelog`
(
    `ID`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `AUTHOR`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FILENAME`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATEEXECUTED`  datetime                                NOT NULL,
    `ORDEREXECUTED` int(11)                                 NOT NULL,
    `EXECTYPE`      varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `MD5SUM`        varchar(35) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DESCRIPTION`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `COMMENTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TAG`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LIQUIBASE`     varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `CONTEXTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LABELS`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID` varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_de_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_de_databasechangeloglock`;
CREATE TABLE `act_de_databasechangeloglock`
(
    `ID`          int(11) NOT NULL,
    `LOCKED`      bit(1)  NOT NULL,
    `LOCKGRANTED` datetime                                DEFAULT NULL,
    `LOCKEDBY`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_de_model
-- ----------------------------
DROP TABLE IF EXISTS `act_de_model`;
CREATE TABLE `act_de_model`
(
    `id`                varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name`              varchar(400) COLLATE utf8mb4_unicode_ci NOT NULL,
    `model_key`         varchar(400) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description`       varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `model_comment`     varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created`           datetime(6)                              DEFAULT NULL,
    `created_by`        varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `last_updated`      datetime(6)                              DEFAULT NULL,
    `last_updated_by`   varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `version`           int(11)                                  DEFAULT NULL,
    `model_editor_json` longtext COLLATE utf8mb4_unicode_ci,
    `thumbnail`         longblob,
    `model_type`        int(11)                                  DEFAULT NULL,
    `tenant_id`         varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_proc_mod_created` (`created_by`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_de_model_history
-- ----------------------------
DROP TABLE IF EXISTS `act_de_model_history`;
CREATE TABLE `act_de_model_history`
(
    `id`                varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name`              varchar(400) COLLATE utf8mb4_unicode_ci NOT NULL,
    `model_key`         varchar(400) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description`       varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `model_comment`     varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created`           datetime(6)                              DEFAULT NULL,
    `created_by`        varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `last_updated`      datetime(6)                              DEFAULT NULL,
    `last_updated_by`   varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `removal_date`      datetime(6)                              DEFAULT NULL,
    `version`           int(11)                                  DEFAULT NULL,
    `model_editor_json` longtext COLLATE utf8mb4_unicode_ci,
    `model_id`          varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `model_type`        int(11)                                  DEFAULT NULL,
    `tenant_id`         varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_proc_mod_history_proc` (`model_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_de_model_relation
-- ----------------------------
DROP TABLE IF EXISTS `act_de_model_relation`;
CREATE TABLE `act_de_model_relation`
(
    `id`              varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `parent_model_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `model_id`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `relation_type`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_relation_parent` (`parent_model_id`),
    KEY `fk_relation_child` (`model_id`),
    CONSTRAINT `fk_relation_child` FOREIGN KEY (`model_id`) REFERENCES `act_de_model` (`id`),
    CONSTRAINT `fk_relation_parent` FOREIGN KEY (`parent_model_id`) REFERENCES `act_de_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_dmn_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_databasechangelog`;
CREATE TABLE `act_dmn_databasechangelog`
(
    `ID`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `AUTHOR`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FILENAME`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATEEXECUTED`  datetime                                NOT NULL,
    `ORDEREXECUTED` int(11)                                 NOT NULL,
    `EXECTYPE`      varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `MD5SUM`        varchar(35) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DESCRIPTION`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `COMMENTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TAG`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LIQUIBASE`     varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `CONTEXTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LABELS`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID` varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_dmn_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_databasechangeloglock`;
CREATE TABLE `act_dmn_databasechangeloglock`
(
    `ID`          int(11) NOT NULL,
    `LOCKED`      bit(1)  NOT NULL,
    `LOCKGRANTED` datetime                                DEFAULT NULL,
    `LOCKEDBY`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_dmn_decision_table
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_decision_table`;
CREATE TABLE `act_dmn_decision_table`
(
    `ID_`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `VERSION_`       int(11)                                 DEFAULT NULL,
    `KEY_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CATEGORY_`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `RESOURCE_NAME_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DESCRIPTION_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_dmn_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_deployment`;
CREATE TABLE `act_dmn_deployment`
(
    `ID_`                   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CATEGORY_`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOY_TIME_`          datetime(3)                             DEFAULT NULL,
    `TENANT_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `PARENT_DEPLOYMENT_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_dmn_deployment_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_deployment_resource`;
CREATE TABLE `act_dmn_deployment_resource`
(
    `ID_`             varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID_`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `RESOURCE_BYTES_` longblob,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_dmn_hi_decision_execution
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_hi_decision_execution`;
CREATE TABLE `act_dmn_hi_decision_execution`
(
    `ID_`                     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DECISION_DEFINITION_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `START_TIME_`             datetime(3)                             DEFAULT NULL,
    `END_TIME_`               datetime(3)                             DEFAULT NULL,
    `INSTANCE_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `EXECUTION_ID_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ACTIVITY_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `FAILED_`                 bit(1)                                  DEFAULT b'0',
    `TENANT_ID_`              varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `EXECUTION_JSON_`         longtext COLLATE utf8mb4_unicode_ci,
    `SCOPE_TYPE_`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_evt_log
-- ----------------------------
DROP TABLE IF EXISTS `act_evt_log`;
CREATE TABLE `act_evt_log`
(
    `LOG_NR_`       bigint(20)   NOT NULL AUTO_INCREMENT,
    `TYPE_`         varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `PROC_DEF_ID_`  varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `PROC_INST_ID_` varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `EXECUTION_ID_` varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `TASK_ID_`      varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `TIME_STAMP_`   timestamp(3) NOT NULL         DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `USER_ID_`      varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `DATA_`         longblob,
    `LOCK_OWNER_`   varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `LOCK_TIME_`    timestamp(3) NULL             DEFAULT NULL,
    `IS_PROCESSED_` tinyint(4)                    DEFAULT '0',
    PRIMARY KEY (`LOG_NR_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_fo_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_databasechangelog`;
CREATE TABLE `act_fo_databasechangelog`
(
    `ID`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `AUTHOR`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FILENAME`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATEEXECUTED`  datetime                                NOT NULL,
    `ORDEREXECUTED` int(11)                                 NOT NULL,
    `EXECTYPE`      varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `MD5SUM`        varchar(35) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DESCRIPTION`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `COMMENTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TAG`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LIQUIBASE`     varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `CONTEXTS`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LABELS`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID` varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_fo_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_databasechangeloglock`;
CREATE TABLE `act_fo_databasechangeloglock`
(
    `ID`          int(11) NOT NULL,
    `LOCKED`      bit(1)  NOT NULL,
    `LOCKGRANTED` datetime                                DEFAULT NULL,
    `LOCKEDBY`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_fo_form_definition
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_definition`;
CREATE TABLE `act_fo_form_definition`
(
    `ID_`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `VERSION_`       int(11)                                 DEFAULT NULL,
    `KEY_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CATEGORY_`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `RESOURCE_NAME_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DESCRIPTION_`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_fo_form_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_deployment`;
CREATE TABLE `act_fo_form_deployment`
(
    `ID_`                   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `CATEGORY_`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOY_TIME_`          datetime(3)                             DEFAULT NULL,
    `TENANT_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `PARENT_DEPLOYMENT_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_fo_form_instance
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_instance`;
CREATE TABLE `act_fo_form_instance`
(
    `ID_`                  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FORM_DEFINITION_ID_`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TASK_ID_`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `PROC_INST_ID_`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `PROC_DEF_ID_`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `SUBMITTED_DATE_`      datetime(3)                             DEFAULT NULL,
    `SUBMITTED_BY_`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `FORM_VALUES_ID_`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TENANT_ID_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_fo_form_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_resource`;
CREATE TABLE `act_fo_form_resource`
(
    `ID_`             varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `NAME_`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID_`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `RESOURCE_BYTES_` longblob,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ge_bytearray
-- ----------------------------
DROP TABLE IF EXISTS `act_ge_bytearray`;
CREATE TABLE `act_ge_bytearray`
(
    `ID_`            varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`           int(11)                       DEFAULT NULL,
    `NAME_`          varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `BYTES_`         longblob,
    `GENERATED_`     tinyint(4)                    DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_FK_BYTEARR_DEPL` (`DEPLOYMENT_ID_`),
    CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ge_property
-- ----------------------------
DROP TABLE IF EXISTS `act_ge_property`;
CREATE TABLE `act_ge_property`
(
    `NAME_`  varchar(64) COLLATE utf8_bin NOT NULL,
    `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL,
    `REV_`   int(11)                       DEFAULT NULL,
    PRIMARY KEY (`NAME_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_actinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_actinst`;
CREATE TABLE `act_hi_actinst`
(
    `ID_`                varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`               int(11)                        DEFAULT '1',
    `PROC_DEF_ID_`       varchar(64) COLLATE utf8_bin  NOT NULL,
    `PROC_INST_ID_`      varchar(64) COLLATE utf8_bin  NOT NULL,
    `EXECUTION_ID_`      varchar(64) COLLATE utf8_bin  NOT NULL,
    `ACT_ID_`            varchar(255) COLLATE utf8_bin NOT NULL,
    `TASK_ID_`           varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `CALL_PROC_INST_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `ACT_NAME_`          varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `ACT_TYPE_`          varchar(255) COLLATE utf8_bin NOT NULL,
    `ASSIGNEE_`          varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `START_TIME_`        datetime(3)                   NOT NULL,
    `END_TIME_`          datetime(3)                    DEFAULT NULL,
    `DURATION_`          bigint(20)                     DEFAULT NULL,
    `DELETE_REASON_`     varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `TENANT_ID_`         varchar(255) COLLATE utf8_bin  DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_HI_ACT_INST_START` (`START_TIME_`),
    KEY `ACT_IDX_HI_ACT_INST_END` (`END_TIME_`),
    KEY `ACT_IDX_HI_ACT_INST_PROCINST` (`PROC_INST_ID_`, `ACT_ID_`),
    KEY `ACT_IDX_HI_ACT_INST_EXEC` (`EXECUTION_ID_`, `ACT_ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_attachment
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_attachment`;
CREATE TABLE `act_hi_attachment`
(
    `ID_`           varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`          int(11)                        DEFAULT NULL,
    `USER_ID_`      varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `NAME_`         varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `DESCRIPTION_`  varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `TYPE_`         varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `TASK_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `PROC_INST_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `URL_`          varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `CONTENT_ID_`   varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `TIME_`         datetime(3)                    DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_comment
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_comment`;
CREATE TABLE `act_hi_comment`
(
    `ID_`           varchar(64) COLLATE utf8_bin NOT NULL,
    `TYPE_`         varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `TIME_`         datetime(3)                  NOT NULL,
    `USER_ID_`      varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `TASK_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `PROC_INST_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `ACTION_`       varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `MESSAGE_`      varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `FULL_MSG_`     longblob,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_detail
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_detail`;
CREATE TABLE `act_hi_detail`
(
    `ID_`           varchar(64) COLLATE utf8_bin  NOT NULL,
    `TYPE_`         varchar(255) COLLATE utf8_bin NOT NULL,
    `PROC_INST_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `EXECUTION_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `TASK_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `ACT_INST_ID_`  varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `NAME_`         varchar(255) COLLATE utf8_bin NOT NULL,
    `VAR_TYPE_`     varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `REV_`          int(11)                        DEFAULT NULL,
    `TIME_`         datetime(3)                   NOT NULL,
    `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `DOUBLE_`       double                         DEFAULT NULL,
    `LONG_`         bigint(20)                     DEFAULT NULL,
    `TEXT_`         varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `TEXT2_`        varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_HI_DETAIL_PROC_INST` (`PROC_INST_ID_`),
    KEY `ACT_IDX_HI_DETAIL_ACT_INST` (`ACT_INST_ID_`),
    KEY `ACT_IDX_HI_DETAIL_TIME` (`TIME_`),
    KEY `ACT_IDX_HI_DETAIL_NAME` (`NAME_`),
    KEY `ACT_IDX_HI_DETAIL_TASK_ID` (`TASK_ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_identitylink
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_identitylink`;
CREATE TABLE `act_hi_identitylink`
(
    `ID_`                  varchar(64) COLLATE utf8_bin NOT NULL,
    `GROUP_ID_`            varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `TYPE_`                varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `USER_ID_`             varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `TASK_ID_`             varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `CREATE_TIME_`         datetime(3)                   DEFAULT NULL,
    `PROC_INST_ID_`        varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_HI_IDENT_LNK_USER` (`USER_ID_`),
    KEY `ACT_IDX_HI_IDENT_LNK_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_HI_IDENT_LNK_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_HI_IDENT_LNK_TASK` (`TASK_ID_`),
    KEY `ACT_IDX_HI_IDENT_LNK_PROCINST` (`PROC_INST_ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_procinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_procinst`;
CREATE TABLE `act_hi_procinst`
(
    `ID_`                        varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`                       int(11)                        DEFAULT '1',
    `PROC_INST_ID_`              varchar(64) COLLATE utf8_bin NOT NULL,
    `BUSINESS_KEY_`              varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `PROC_DEF_ID_`               varchar(64) COLLATE utf8_bin NOT NULL,
    `START_TIME_`                datetime(3)                  NOT NULL,
    `END_TIME_`                  datetime(3)                    DEFAULT NULL,
    `DURATION_`                  bigint(20)                     DEFAULT NULL,
    `START_USER_ID_`             varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `START_ACT_ID_`              varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `END_ACT_ID_`                varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SUPER_PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `DELETE_REASON_`             varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `TENANT_ID_`                 varchar(255) COLLATE utf8_bin  DEFAULT '',
    `NAME_`                      varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `CALLBACK_ID_`               varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `CALLBACK_TYPE_`             varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    UNIQUE KEY `PROC_INST_ID_` (`PROC_INST_ID_`),
    KEY `ACT_IDX_HI_PRO_INST_END` (`END_TIME_`),
    KEY `ACT_IDX_HI_PRO_I_BUSKEY` (`BUSINESS_KEY_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_taskinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_taskinst`;
CREATE TABLE `act_hi_taskinst`
(
    `ID_`                  varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`                 int(11)                        DEFAULT '1',
    `PROC_DEF_ID_`         varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `TASK_DEF_ID_`         varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `TASK_DEF_KEY_`        varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `PROC_INST_ID_`        varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `EXECUTION_ID_`        varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SUB_SCOPE_ID_`        varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `NAME_`                varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `PARENT_TASK_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `DESCRIPTION_`         varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `OWNER_`               varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `ASSIGNEE_`            varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `START_TIME_`          datetime(3)                  NOT NULL,
    `CLAIM_TIME_`          datetime(3)                    DEFAULT NULL,
    `END_TIME_`            datetime(3)                    DEFAULT NULL,
    `DURATION_`            bigint(20)                     DEFAULT NULL,
    `DELETE_REASON_`       varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `PRIORITY_`            int(11)                        DEFAULT NULL,
    `DUE_DATE_`            datetime(3)                    DEFAULT NULL,
    `FORM_KEY_`            varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `CATEGORY_`            varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `TENANT_ID_`           varchar(255) COLLATE utf8_bin  DEFAULT '',
    `LAST_UPDATED_TIME_`   datetime(3)                    DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_HI_TASK_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_HI_TASK_SUB_SCOPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_HI_TASK_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_HI_TASK_INST_PROCINST` (`PROC_INST_ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_hi_varinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_varinst`;
CREATE TABLE `act_hi_varinst`
(
    `ID_`                varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`               int(11)                        DEFAULT '1',
    `PROC_INST_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `EXECUTION_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `TASK_ID_`           varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `NAME_`              varchar(255) COLLATE utf8_bin NOT NULL,
    `VAR_TYPE_`          varchar(100) COLLATE utf8_bin  DEFAULT NULL,
    `SCOPE_ID_`          varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SUB_SCOPE_ID_`      varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SCOPE_TYPE_`        varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `BYTEARRAY_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `DOUBLE_`            double                         DEFAULT NULL,
    `LONG_`              bigint(20)                     DEFAULT NULL,
    `TEXT_`              varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `TEXT2_`             varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `CREATE_TIME_`       datetime(3)                    DEFAULT NULL,
    `LAST_UPDATED_TIME_` datetime(3)                    DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_HI_PROCVAR_NAME_TYPE` (`NAME_`, `VAR_TYPE_`),
    KEY `ACT_IDX_HI_VAR_SCOPE_ID_TYPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_HI_VAR_SUB_ID_TYPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_HI_PROCVAR_PROC_INST` (`PROC_INST_ID_`),
    KEY `ACT_IDX_HI_PROCVAR_TASK_ID` (`TASK_ID_`),
    KEY `ACT_IDX_HI_PROCVAR_EXE` (`EXECUTION_ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_bytearray
-- ----------------------------
DROP TABLE IF EXISTS `act_id_bytearray`;
CREATE TABLE `act_id_bytearray`
(
    `ID_`    varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`   int(11)                       DEFAULT NULL,
    `NAME_`  varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `BYTES_` longblob,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_group
-- ----------------------------
DROP TABLE IF EXISTS `act_id_group`;
CREATE TABLE `act_id_group`
(
    `ID_`   varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`  int(11)                       DEFAULT NULL,
    `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_info
-- ----------------------------
DROP TABLE IF EXISTS `act_id_info`;
CREATE TABLE `act_id_info`
(
    `ID_`        varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`       int(11)                       DEFAULT NULL,
    `USER_ID_`   varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `TYPE_`      varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `KEY_`       varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `VALUE_`     varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `PASSWORD_`  longblob,
    `PARENT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_membership
-- ----------------------------
DROP TABLE IF EXISTS `act_id_membership`;
CREATE TABLE `act_id_membership`
(
    `USER_ID_`  varchar(64) COLLATE utf8_bin NOT NULL,
    `GROUP_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`USER_ID_`, `GROUP_ID_`),
    KEY `ACT_FK_MEMB_GROUP` (`GROUP_ID_`),
    CONSTRAINT `ACT_FK_MEMB_GROUP` FOREIGN KEY (`GROUP_ID_`) REFERENCES `act_id_group` (`ID_`),
    CONSTRAINT `ACT_FK_MEMB_USER` FOREIGN KEY (`USER_ID_`) REFERENCES `act_id_user` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_priv
-- ----------------------------
DROP TABLE IF EXISTS `act_id_priv`;
CREATE TABLE `act_id_priv`
(
    `ID_`   varchar(64) COLLATE utf8_bin  NOT NULL,
    `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`ID_`),
    UNIQUE KEY `ACT_UNIQ_PRIV_NAME` (`NAME_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_priv_mapping
-- ----------------------------
DROP TABLE IF EXISTS `act_id_priv_mapping`;
CREATE TABLE `act_id_priv_mapping`
(
    `ID_`       varchar(64) COLLATE utf8_bin NOT NULL,
    `PRIV_ID_`  varchar(64) COLLATE utf8_bin NOT NULL,
    `USER_ID_`  varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `GROUP_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_FK_PRIV_MAPPING` (`PRIV_ID_`),
    KEY `ACT_IDX_PRIV_USER` (`USER_ID_`),
    KEY `ACT_IDX_PRIV_GROUP` (`GROUP_ID_`),
    CONSTRAINT `ACT_FK_PRIV_MAPPING` FOREIGN KEY (`PRIV_ID_`) REFERENCES `act_id_priv` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_property
-- ----------------------------
DROP TABLE IF EXISTS `act_id_property`;
CREATE TABLE `act_id_property`
(
    `NAME_`  varchar(64) COLLATE utf8_bin NOT NULL,
    `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL,
    `REV_`   int(11)                       DEFAULT NULL,
    PRIMARY KEY (`NAME_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_token
-- ----------------------------
DROP TABLE IF EXISTS `act_id_token`;
CREATE TABLE `act_id_token`
(
    `ID_`          varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`         int(11)                               DEFAULT NULL,
    `TOKEN_VALUE_` varchar(255) COLLATE utf8_bin         DEFAULT NULL,
    `TOKEN_DATE_`  timestamp(3)                 NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `IP_ADDRESS_`  varchar(255) COLLATE utf8_bin         DEFAULT NULL,
    `USER_AGENT_`  varchar(255) COLLATE utf8_bin         DEFAULT NULL,
    `USER_ID_`     varchar(255) COLLATE utf8_bin         DEFAULT NULL,
    `TOKEN_DATA_`  varchar(2000) COLLATE utf8_bin        DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_id_user
-- ----------------------------
DROP TABLE IF EXISTS `act_id_user`;
CREATE TABLE `act_id_user`
(
    `ID_`           varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`          int(11)                       DEFAULT NULL,
    `FIRST_`        varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `LAST_`         varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `DISPLAY_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `EMAIL_`        varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `PWD_`          varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `PICTURE_ID_`   varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `TENANT_ID_`    varchar(255) COLLATE utf8_bin DEFAULT '',
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_procdef_info
-- ----------------------------
DROP TABLE IF EXISTS `act_procdef_info`;
CREATE TABLE `act_procdef_info`
(
    `ID_`           varchar(64) COLLATE utf8_bin NOT NULL,
    `PROC_DEF_ID_`  varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`          int(11)                      DEFAULT NULL,
    `INFO_JSON_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    UNIQUE KEY `ACT_UNIQ_INFO_PROCDEF` (`PROC_DEF_ID_`),
    KEY `ACT_IDX_INFO_PROCDEF` (`PROC_DEF_ID_`),
    KEY `ACT_FK_INFO_JSON_BA` (`INFO_JSON_ID_`),
    CONSTRAINT `ACT_FK_INFO_JSON_BA` FOREIGN KEY (`INFO_JSON_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_INFO_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_re_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_re_deployment`;
CREATE TABLE `act_re_deployment`
(
    `ID_`                   varchar(64) COLLATE utf8_bin NOT NULL,
    `NAME_`                 varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `CATEGORY_`             varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `KEY_`                  varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `TENANT_ID_`            varchar(255) COLLATE utf8_bin     DEFAULT '',
    `DEPLOY_TIME_`          timestamp(3)                 NULL DEFAULT NULL,
    `DERIVED_FROM_`         varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `DERIVED_FROM_ROOT_`    varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `PARENT_DEPLOYMENT_ID_` varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `ENGINE_VERSION_`       varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_re_model
-- ----------------------------
DROP TABLE IF EXISTS `act_re_model`;
CREATE TABLE `act_re_model`
(
    `ID_`                           varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`                          int(11)                           DEFAULT NULL,
    `NAME_`                         varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `KEY_`                          varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `CATEGORY_`                     varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `CREATE_TIME_`                  timestamp(3)                 NULL DEFAULT NULL,
    `LAST_UPDATE_TIME_`             timestamp(3)                 NULL DEFAULT NULL,
    `VERSION_`                      int(11)                           DEFAULT NULL,
    `META_INFO_`                    varchar(4000) COLLATE utf8_bin    DEFAULT NULL,
    `DEPLOYMENT_ID_`                varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `EDITOR_SOURCE_VALUE_ID_`       varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `TENANT_ID_`                    varchar(255) COLLATE utf8_bin     DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_FK_MODEL_SOURCE` (`EDITOR_SOURCE_VALUE_ID_`),
    KEY `ACT_FK_MODEL_SOURCE_EXTRA` (`EDITOR_SOURCE_EXTRA_VALUE_ID_`),
    KEY `ACT_FK_MODEL_DEPLOYMENT` (`DEPLOYMENT_ID_`),
    CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`),
    CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_re_procdef
-- ----------------------------
DROP TABLE IF EXISTS `act_re_procdef`;
CREATE TABLE `act_re_procdef`
(
    `ID_`                     varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`                    int(11)                                DEFAULT NULL,
    `CATEGORY_`               varchar(255) COLLATE utf8_bin          DEFAULT NULL,
    `NAME_`                   varchar(255) COLLATE utf8_bin          DEFAULT NULL,
    `KEY_`                    varchar(255) COLLATE utf8_bin NOT NULL,
    `VERSION_`                int(11)                       NOT NULL,
    `DEPLOYMENT_ID_`          varchar(64) COLLATE utf8_bin           DEFAULT NULL,
    `RESOURCE_NAME_`          varchar(4000) COLLATE utf8_bin         DEFAULT NULL,
    `DGRM_RESOURCE_NAME_`     varchar(4000) COLLATE utf8_bin         DEFAULT NULL,
    `DESCRIPTION_`            varchar(4000) COLLATE utf8_bin         DEFAULT NULL,
    `HAS_START_FORM_KEY_`     tinyint(4)                             DEFAULT NULL,
    `HAS_GRAPHICAL_NOTATION_` tinyint(4)                             DEFAULT NULL,
    `SUSPENSION_STATE_`       int(11)                                DEFAULT NULL,
    `TENANT_ID_`              varchar(255) COLLATE utf8_bin          DEFAULT '',
    `ENGINE_VERSION_`         varchar(255) COLLATE utf8_bin          DEFAULT NULL,
    `DERIVED_FROM_`           varchar(64) COLLATE utf8_bin           DEFAULT NULL,
    `DERIVED_FROM_ROOT_`      varchar(64) COLLATE utf8_bin           DEFAULT NULL,
    `DERIVED_VERSION_`        int(11)                       NOT NULL DEFAULT '0',
    PRIMARY KEY (`ID_`),
    UNIQUE KEY `ACT_UNIQ_PROCDEF` (`KEY_`, `VERSION_`, `DERIVED_VERSION_`, `TENANT_ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_deadletter_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_deadletter_job`;
CREATE TABLE `act_ru_deadletter_job`
(
    `ID_`                  varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`                 int(11)                            DEFAULT NULL,
    `TYPE_`                varchar(255) COLLATE utf8_bin NOT NULL,
    `EXCLUSIVE_`           tinyint(1)                         DEFAULT NULL,
    `EXECUTION_ID_`        varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROC_DEF_ID_`         varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SUB_SCOPE_ID_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `EXCEPTION_STACK_ID_`  varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `EXCEPTION_MSG_`       varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `DUEDATE_`             timestamp(3)                  NULL DEFAULT NULL,
    `REPEAT_`              varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_TYPE_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_CFG_`         varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `CUSTOM_VALUES_ID_`    varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `CREATE_TIME_`         timestamp(3)                  NULL DEFAULT NULL,
    `TENANT_ID_`           varchar(255) COLLATE utf8_bin      DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_DEADLETTER_JOB_EXCEPTION_STACK_ID` (`EXCEPTION_STACK_ID_`),
    KEY `ACT_IDX_DEADLETTER_JOB_CUSTOM_VALUES_ID` (`CUSTOM_VALUES_ID_`),
    KEY `ACT_IDX_DJOB_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_DJOB_SUB_SCOPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_DJOB_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_FK_DEADLETTER_JOB_EXECUTION` (`EXECUTION_ID_`),
    KEY `ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`),
    KEY `ACT_FK_DEADLETTER_JOB_PROC_DEF` (`PROC_DEF_ID_`),
    CONSTRAINT `ACT_FK_DEADLETTER_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_DEADLETTER_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_DEADLETTER_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_DEADLETTER_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_event_subscr
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_event_subscr`;
CREATE TABLE `act_ru_event_subscr`
(
    `ID_`            varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`           int(11)                                DEFAULT NULL,
    `EVENT_TYPE_`    varchar(255) COLLATE utf8_bin NOT NULL,
    `EVENT_NAME_`    varchar(255) COLLATE utf8_bin          DEFAULT NULL,
    `EXECUTION_ID_`  varchar(64) COLLATE utf8_bin           DEFAULT NULL,
    `PROC_INST_ID_`  varchar(64) COLLATE utf8_bin           DEFAULT NULL,
    `ACTIVITY_ID_`   varchar(64) COLLATE utf8_bin           DEFAULT NULL,
    `CONFIGURATION_` varchar(255) COLLATE utf8_bin          DEFAULT NULL,
    `CREATED_`       timestamp(3)                  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `PROC_DEF_ID_`   varchar(64) COLLATE utf8_bin           DEFAULT NULL,
    `TENANT_ID_`     varchar(255) COLLATE utf8_bin          DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_EVENT_SUBSCR_CONFIG_` (`CONFIGURATION_`),
    KEY `ACT_FK_EVENT_EXEC` (`EXECUTION_ID_`),
    CONSTRAINT `ACT_FK_EVENT_EXEC` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_execution
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_execution`;
CREATE TABLE `act_ru_execution`
(
    `ID_`                   varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`                  int(11)                           DEFAULT NULL,
    `PROC_INST_ID_`         varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `BUSINESS_KEY_`         varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `PARENT_ID_`            varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `PROC_DEF_ID_`          varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `SUPER_EXEC_`           varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `ROOT_PROC_INST_ID_`    varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `ACT_ID_`               varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `IS_ACTIVE_`            tinyint(4)                        DEFAULT NULL,
    `IS_CONCURRENT_`        tinyint(4)                        DEFAULT NULL,
    `IS_SCOPE_`             tinyint(4)                        DEFAULT NULL,
    `IS_EVENT_SCOPE_`       tinyint(4)                        DEFAULT NULL,
    `IS_MI_ROOT_`           tinyint(4)                        DEFAULT NULL,
    `SUSPENSION_STATE_`     int(11)                           DEFAULT NULL,
    `CACHED_ENT_STATE_`     int(11)                           DEFAULT NULL,
    `TENANT_ID_`            varchar(255) COLLATE utf8_bin     DEFAULT '',
    `NAME_`                 varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `START_ACT_ID_`         varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `START_TIME_`           datetime(3)                       DEFAULT NULL,
    `START_USER_ID_`        varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `LOCK_TIME_`            timestamp(3)                 NULL DEFAULT NULL,
    `IS_COUNT_ENABLED_`     tinyint(4)                        DEFAULT NULL,
    `EVT_SUBSCR_COUNT_`     int(11)                           DEFAULT NULL,
    `TASK_COUNT_`           int(11)                           DEFAULT NULL,
    `JOB_COUNT_`            int(11)                           DEFAULT NULL,
    `TIMER_JOB_COUNT_`      int(11)                           DEFAULT NULL,
    `SUSP_JOB_COUNT_`       int(11)                           DEFAULT NULL,
    `DEADLETTER_JOB_COUNT_` int(11)                           DEFAULT NULL,
    `VAR_COUNT_`            int(11)                           DEFAULT NULL,
    `ID_LINK_COUNT_`        int(11)                           DEFAULT NULL,
    `CALLBACK_ID_`          varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `CALLBACK_TYPE_`        varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_EXEC_BUSKEY` (`BUSINESS_KEY_`),
    KEY `ACT_IDC_EXEC_ROOT` (`ROOT_PROC_INST_ID_`),
    KEY `ACT_FK_EXE_PROCINST` (`PROC_INST_ID_`),
    KEY `ACT_FK_EXE_PARENT` (`PARENT_ID_`),
    KEY `ACT_FK_EXE_SUPER` (`SUPER_EXEC_`),
    KEY `ACT_FK_EXE_PROCDEF` (`PROC_DEF_ID_`),
    CONSTRAINT `ACT_FK_EXE_PARENT` FOREIGN KEY (`PARENT_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE,
    CONSTRAINT `ACT_FK_EXE_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
    CONSTRAINT `ACT_FK_EXE_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `ACT_FK_EXE_SUPER` FOREIGN KEY (`SUPER_EXEC_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_history_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_history_job`;
CREATE TABLE `act_ru_history_job`
(
    `ID_`                 varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`                int(11)                           DEFAULT NULL,
    `LOCK_EXP_TIME_`      timestamp(3)                 NULL DEFAULT NULL,
    `LOCK_OWNER_`         varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `RETRIES_`            int(11)                           DEFAULT NULL,
    `EXCEPTION_STACK_ID_` varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `EXCEPTION_MSG_`      varchar(4000) COLLATE utf8_bin    DEFAULT NULL,
    `HANDLER_TYPE_`       varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `HANDLER_CFG_`        varchar(4000) COLLATE utf8_bin    DEFAULT NULL,
    `CUSTOM_VALUES_ID_`   varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `ADV_HANDLER_CFG_ID_` varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `CREATE_TIME_`        timestamp(3)                 NULL DEFAULT NULL,
    `SCOPE_TYPE_`         varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `TENANT_ID_`          varchar(255) COLLATE utf8_bin     DEFAULT '',
    PRIMARY KEY (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_identitylink
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_identitylink`;
CREATE TABLE `act_ru_identitylink`
(
    `ID_`                  varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`                 int(11)                       DEFAULT NULL,
    `GROUP_ID_`            varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `TYPE_`                varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `USER_ID_`             varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `TASK_ID_`             varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `PROC_INST_ID_`        varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `PROC_DEF_ID_`         varchar(64) COLLATE utf8_bin  DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_IDENT_LNK_USER` (`USER_ID_`),
    KEY `ACT_IDX_IDENT_LNK_GROUP` (`GROUP_ID_`),
    KEY `ACT_IDX_IDENT_LNK_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_IDENT_LNK_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_ATHRZ_PROCEDEF` (`PROC_DEF_ID_`),
    KEY `ACT_FK_TSKASS_TASK` (`TASK_ID_`),
    KEY `ACT_FK_IDL_PROCINST` (`PROC_INST_ID_`),
    CONSTRAINT `ACT_FK_ATHRZ_PROCEDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
    CONSTRAINT `ACT_FK_IDL_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_TSKASS_TASK` FOREIGN KEY (`TASK_ID_`) REFERENCES `act_ru_task` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_job`;
CREATE TABLE `act_ru_job`
(
    `ID_`                  varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`                 int(11)                            DEFAULT NULL,
    `TYPE_`                varchar(255) COLLATE utf8_bin NOT NULL,
    `LOCK_EXP_TIME_`       timestamp(3)                  NULL DEFAULT NULL,
    `LOCK_OWNER_`          varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `EXCLUSIVE_`           tinyint(1)                         DEFAULT NULL,
    `EXECUTION_ID_`        varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROC_DEF_ID_`         varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SUB_SCOPE_ID_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `RETRIES_`             int(11)                            DEFAULT NULL,
    `EXCEPTION_STACK_ID_`  varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `EXCEPTION_MSG_`       varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `DUEDATE_`             timestamp(3)                  NULL DEFAULT NULL,
    `REPEAT_`              varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_TYPE_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_CFG_`         varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `CUSTOM_VALUES_ID_`    varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `CREATE_TIME_`         timestamp(3)                  NULL DEFAULT NULL,
    `TENANT_ID_`           varchar(255) COLLATE utf8_bin      DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_JOB_EXCEPTION_STACK_ID` (`EXCEPTION_STACK_ID_`),
    KEY `ACT_IDX_JOB_CUSTOM_VALUES_ID` (`CUSTOM_VALUES_ID_`),
    KEY `ACT_IDX_JOB_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_JOB_SUB_SCOPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_JOB_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_FK_JOB_EXECUTION` (`EXECUTION_ID_`),
    KEY `ACT_FK_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`),
    KEY `ACT_FK_JOB_PROC_DEF` (`PROC_DEF_ID_`),
    CONSTRAINT `ACT_FK_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_suspended_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_suspended_job`;
CREATE TABLE `act_ru_suspended_job`
(
    `ID_`                  varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`                 int(11)                            DEFAULT NULL,
    `TYPE_`                varchar(255) COLLATE utf8_bin NOT NULL,
    `EXCLUSIVE_`           tinyint(1)                         DEFAULT NULL,
    `EXECUTION_ID_`        varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROC_DEF_ID_`         varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SUB_SCOPE_ID_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `RETRIES_`             int(11)                            DEFAULT NULL,
    `EXCEPTION_STACK_ID_`  varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `EXCEPTION_MSG_`       varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `DUEDATE_`             timestamp(3)                  NULL DEFAULT NULL,
    `REPEAT_`              varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_TYPE_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_CFG_`         varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `CUSTOM_VALUES_ID_`    varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `CREATE_TIME_`         timestamp(3)                  NULL DEFAULT NULL,
    `TENANT_ID_`           varchar(255) COLLATE utf8_bin      DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_SUSPENDED_JOB_EXCEPTION_STACK_ID` (`EXCEPTION_STACK_ID_`),
    KEY `ACT_IDX_SUSPENDED_JOB_CUSTOM_VALUES_ID` (`CUSTOM_VALUES_ID_`),
    KEY `ACT_IDX_SJOB_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_SJOB_SUB_SCOPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_SJOB_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_FK_SUSPENDED_JOB_EXECUTION` (`EXECUTION_ID_`),
    KEY `ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`),
    KEY `ACT_FK_SUSPENDED_JOB_PROC_DEF` (`PROC_DEF_ID_`),
    CONSTRAINT `ACT_FK_SUSPENDED_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_SUSPENDED_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_SUSPENDED_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_SUSPENDED_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_task
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_task`;
CREATE TABLE `act_ru_task`
(
    `ID_`                  varchar(64) COLLATE utf8_bin NOT NULL,
    `REV_`                 int(11)                           DEFAULT NULL,
    `EXECUTION_ID_`        varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `PROC_INST_ID_`        varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `PROC_DEF_ID_`         varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `TASK_DEF_ID_`         varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `SUB_SCOPE_ID_`        varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `NAME_`                varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `PARENT_TASK_ID_`      varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `DESCRIPTION_`         varchar(4000) COLLATE utf8_bin    DEFAULT NULL,
    `TASK_DEF_KEY_`        varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `OWNER_`               varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `ASSIGNEE_`            varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `DELEGATION_`          varchar(64) COLLATE utf8_bin      DEFAULT NULL,
    `PRIORITY_`            int(11)                           DEFAULT NULL,
    `CREATE_TIME_`         timestamp(3)                 NULL DEFAULT NULL,
    `DUE_DATE_`            datetime(3)                       DEFAULT NULL,
    `CATEGORY_`            varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `SUSPENSION_STATE_`    int(11)                           DEFAULT NULL,
    `TENANT_ID_`           varchar(255) COLLATE utf8_bin     DEFAULT '',
    `FORM_KEY_`            varchar(255) COLLATE utf8_bin     DEFAULT NULL,
    `CLAIM_TIME_`          datetime(3)                       DEFAULT NULL,
    `IS_COUNT_ENABLED_`    tinyint(4)                        DEFAULT NULL,
    `VAR_COUNT_`           int(11)                           DEFAULT NULL,
    `ID_LINK_COUNT_`       int(11)                           DEFAULT NULL,
    `SUB_TASK_COUNT_`      int(11)                           DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_TASK_CREATE` (`CREATE_TIME_`),
    KEY `ACT_IDX_TASK_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_TASK_SUB_SCOPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_TASK_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_FK_TASK_EXE` (`EXECUTION_ID_`),
    KEY `ACT_FK_TASK_PROCINST` (`PROC_INST_ID_`),
    KEY `ACT_FK_TASK_PROCDEF` (`PROC_DEF_ID_`),
    CONSTRAINT `ACT_FK_TASK_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_TASK_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
    CONSTRAINT `ACT_FK_TASK_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_timer_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_timer_job`;
CREATE TABLE `act_ru_timer_job`
(
    `ID_`                  varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`                 int(11)                            DEFAULT NULL,
    `TYPE_`                varchar(255) COLLATE utf8_bin NOT NULL,
    `LOCK_EXP_TIME_`       timestamp(3)                  NULL DEFAULT NULL,
    `LOCK_OWNER_`          varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `EXCLUSIVE_`           tinyint(1)                         DEFAULT NULL,
    `EXECUTION_ID_`        varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `PROC_DEF_ID_`         varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `SCOPE_ID_`            varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SUB_SCOPE_ID_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_TYPE_`          varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `SCOPE_DEFINITION_ID_` varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `RETRIES_`             int(11)                            DEFAULT NULL,
    `EXCEPTION_STACK_ID_`  varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `EXCEPTION_MSG_`       varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `DUEDATE_`             timestamp(3)                  NULL DEFAULT NULL,
    `REPEAT_`              varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_TYPE_`        varchar(255) COLLATE utf8_bin      DEFAULT NULL,
    `HANDLER_CFG_`         varchar(4000) COLLATE utf8_bin     DEFAULT NULL,
    `CUSTOM_VALUES_ID_`    varchar(64) COLLATE utf8_bin       DEFAULT NULL,
    `CREATE_TIME_`         timestamp(3)                  NULL DEFAULT NULL,
    `TENANT_ID_`           varchar(255) COLLATE utf8_bin      DEFAULT '',
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_TIMER_JOB_EXCEPTION_STACK_ID` (`EXCEPTION_STACK_ID_`),
    KEY `ACT_IDX_TIMER_JOB_CUSTOM_VALUES_ID` (`CUSTOM_VALUES_ID_`),
    KEY `ACT_IDX_TJOB_SCOPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_TJOB_SUB_SCOPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_TJOB_SCOPE_DEF` (`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_FK_TIMER_JOB_EXECUTION` (`EXECUTION_ID_`),
    KEY `ACT_FK_TIMER_JOB_PROCESS_INSTANCE` (`PROCESS_INSTANCE_ID_`),
    KEY `ACT_FK_TIMER_JOB_PROC_DEF` (`PROC_DEF_ID_`),
    CONSTRAINT `ACT_FK_TIMER_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_TIMER_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_TIMER_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_TIMER_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_TIMER_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for act_ru_variable
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_variable`;
CREATE TABLE `act_ru_variable`
(
    `ID_`           varchar(64) COLLATE utf8_bin  NOT NULL,
    `REV_`          int(11)                        DEFAULT NULL,
    `TYPE_`         varchar(255) COLLATE utf8_bin NOT NULL,
    `NAME_`         varchar(255) COLLATE utf8_bin NOT NULL,
    `EXECUTION_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `PROC_INST_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `TASK_ID_`      varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `SCOPE_ID_`     varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SUB_SCOPE_ID_` varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `SCOPE_TYPE_`   varchar(255) COLLATE utf8_bin  DEFAULT NULL,
    `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `DOUBLE_`       double                         DEFAULT NULL,
    `LONG_`         bigint(20)                     DEFAULT NULL,
    `TEXT_`         varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `TEXT2_`        varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`ID_`),
    KEY `ACT_IDX_RU_VAR_SCOPE_ID_TYPE` (`SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_IDX_RU_VAR_SUB_ID_TYPE` (`SUB_SCOPE_ID_`, `SCOPE_TYPE_`),
    KEY `ACT_FK_VAR_BYTEARRAY` (`BYTEARRAY_ID_`),
    KEY `ACT_IDX_VARIABLE_TASK_ID` (`TASK_ID_`),
    KEY `ACT_FK_VAR_EXE` (`EXECUTION_ID_`),
    KEY `ACT_FK_VAR_PROCINST` (`PROC_INST_ID_`),
    CONSTRAINT `ACT_FK_VAR_BYTEARRAY` FOREIGN KEY (`BYTEARRAY_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
    CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
    CONSTRAINT `ACT_FK_VAR_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `auth_access_token`;
CREATE TABLE `auth_access_token`
(
    `id`                   bigint(20)                             NOT NULL,
    `name`                 varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `token`                varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `token_type`           varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `refresh_token`        varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `client_id`            varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `expires_at`           datetime                                DEFAULT NULL,
    `issued_at`            datetime                                DEFAULT NULL,
    `last_location`        varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `last_used_time`       datetime                                DEFAULT NULL,
    `scopes`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`              bigint(20)                              DEFAULT NULL,
    `client_device`        varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `client_ip`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `client_last_ip`       varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `client_last_location` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `client_location`      varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at`           datetime                                DEFAULT NULL,
    `created_by`           bigint(20)                              DEFAULT NULL,
    `updated_at`           datetime                                DEFAULT NULL,
    `updated_by`           bigint(20)                              DEFAULT NULL,
    `client_secret`        varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UN_AUTH_ACCESS_TOKEN_UNIQUE` (`token`),
    KEY `FK_ACCESS_TOKEN_USER` (`user_id`),
    CONSTRAINT `FK_ACCESS_TOKEN_USER` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_auth_permission_resource
-- ----------------------------
DROP TABLE IF EXISTS `auth_auth_permission_resource`;
CREATE TABLE `auth_auth_permission_resource`
(
    `permission_id` varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `resource`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`permission_id`, `resource`),
    CONSTRAINT `FK_PERMISSION_RESOURCE_PID` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_client_secret
-- ----------------------------
DROP TABLE IF EXISTS `auth_client_secret`;
CREATE TABLE `auth_client_secret`
(
    `id`         bigint(20)                             NOT NULL,
    `client_id`  varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`       varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `secret`     varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_CLIENT_ID` (`client_id`),
    UNIQUE KEY `UK_qwfyqqb9ipbhejj1ycvjwk3cd` (`secret`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_comment
-- ----------------------------
DROP TABLE IF EXISTS `auth_comment`;
CREATE TABLE `auth_comment`
(
    `id`             bigint(20)                              NOT NULL,
    `created_at`     datetime                                 DEFAULT NULL,
    `created_by`     bigint(20)                               DEFAULT NULL,
    `updated_at`     datetime                                 DEFAULT NULL,
    `updated_by`     bigint(20)                               DEFAULT NULL,
    `content`        text COLLATE utf8mb4_unicode_ci         NOT NULL,
    `path`           varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status`         varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `target_id`      varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `target_type`    varchar(20) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `title`          varchar(255) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `uid`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `for_comment_id` bigint(20)                               DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_COMMENT_FOR_COMMENT` (`for_comment_id`),
    CONSTRAINT `FK_COMMENT_FOR_COMMENT` FOREIGN KEY (`for_comment_id`) REFERENCES `auth_comment` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_grant_permission
-- ----------------------------
DROP TABLE IF EXISTS `auth_grant_permission`;
CREATE TABLE `auth_grant_permission`
(
    `id`           bigint(20)                              NOT NULL,
    `created_at`   datetime   DEFAULT NULL,
    `created_by`   bigint(20) DEFAULT NULL,
    `updated_at`   datetime   DEFAULT NULL,
    `updated_by`   bigint(20) DEFAULT NULL,
    `expires_at`   datetime   DEFAULT NULL,
    `grantee_type` varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `value`        varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `permission`   varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_SECURE_GRANT_PERMISSION_PID` (`permission`),
    CONSTRAINT `FK_SECURE_GRANT_PERMISSION_PID` FOREIGN KEY (`permission`) REFERENCES `auth_permission` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_permission
-- ----------------------------
DROP TABLE IF EXISTS `auth_permission`;
CREATE TABLE `auth_permission`
(
    `id`            varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`    datetime                                DEFAULT NULL,
    `created_by`    bigint(20)                              DEFAULT NULL,
    `updated_at`    datetime                                DEFAULT NULL,
    `updated_by`    bigint(20)                              DEFAULT NULL,
    `description`   varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`       bit(1)                                  NOT NULL,
    `grant_type`    varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `sort`          int(11)                                 NOT NULL,
    `name`          varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `pid`           varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `resource_type` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`          varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_PERMISSION_PARENT` (`pid`),
    KEY `FK_PERMISSION_RESOURCE_TYPE` (`resource_type`),
    KEY `FK_PERMISSION_TYPE` (`type`),
    CONSTRAINT `FK_PERMISSION_PARENT` FOREIGN KEY (`pid`) REFERENCES `auth_permission` (`id`),
    CONSTRAINT `FK_PERMISSION_RESOURCE_TYPE` FOREIGN KEY (`resource_type`) REFERENCES `auth_resource_type` (`id`),
    CONSTRAINT `FK_PERMISSION_TYPE` FOREIGN KEY (`type`) REFERENCES `auth_permission_type` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_permission_token_type
-- ----------------------------
DROP TABLE IF EXISTS `auth_permission_token_type`;
CREATE TABLE `auth_permission_token_type`
(
    `permission_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `token_type`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`permission_id`, `token_type`),
    CONSTRAINT `FK_PERMISSION_TOKEN_TYPE_PID` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_permission_type
-- ----------------------------
DROP TABLE IF EXISTS `auth_permission_type`;
CREATE TABLE `auth_permission_type`
(
    `id`          varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`        int(11)                                 DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_resource_type
-- ----------------------------
DROP TABLE IF EXISTS `auth_resource_type`;
CREATE TABLE `auth_resource_type`
(
    `id`         varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `name`       varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role`
(
    `id`              bigint(20)                             NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `code`            varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description`     varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`         bit(1)                                 NOT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`            varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `organization_id` bigint(20)                             NOT NULL,
    `space`           bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKbib6cbk8lt2dvsd677sm9gdno` (`code`, `organization_id`),
    UNIQUE KEY `UK_AUTH_ROLE_CODE_ORGANIZATION` (`code`, `organization_id`),
    KEY `FK_ROLE_ORGANIZATION` (`organization_id`),
    KEY `FK_ROLE_SCOPE` (`space`),
    CONSTRAINT `FK_ROLE_ORGANIZATION` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_ROLE_SCOPE` FOREIGN KEY (`space`) REFERENCES `auth_role_space` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_role_play
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_play`;
CREATE TABLE `auth_role_play`
(
    `id`         varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `player`     varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `role_id`    bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKo0y5qjb9rdqtjqmwfxhj67kdk` (`role_id`),
    CONSTRAINT `FKo0y5qjb9rdqtjqmwfxhj67kdk` FOREIGN KEY (`role_id`) REFERENCES `auth_role` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_role_space
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_space`;
CREATE TABLE `auth_role_space`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                               DEFAULT NULL,
    `created_by`      bigint(20)                             DEFAULT NULL,
    `updated_at`      datetime                               DEFAULT NULL,
    `updated_by`      bigint(20)                             DEFAULT NULL,
    `code`            varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `organization_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_AUTH_ROLE_SPACE_CODE` (`code`, `organization_id`),
    KEY `FK_ORGANIZATION_ROLE_SPACE` (`organization_id`),
    CONSTRAINT `FK_ORGANIZATION_ROLE_SPACE` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_role_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_user`;
CREATE TABLE `auth_role_user`
(
    `user_id`   bigint(20) NOT NULL,
    `role_code` bigint(20) NOT NULL,
    KEY `FK_ROLE_USER_RCODE` (`role_code`),
    KEY `FK_ROLE_USER_UID` (`user_id`),
    CONSTRAINT `FK_ROLE_USER_RCODE` FOREIGN KEY (`role_code`) REFERENCES `auth_role` (`id`),
    CONSTRAINT `FK_ROLE_USER_UID` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user`
(
    `id`                      bigint(20)                             NOT NULL,
    `username`                varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `password`                varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `user_type`               varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `nick_name`               varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `title`                   varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sex`                     varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `bio`                     varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `location`                varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `birthday`                date                                    DEFAULT NULL,
    `avatar`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `company`                 varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`                 bit(1)                                  DEFAULT NULL,
    `non_expired`             bit(1)                                  DEFAULT NULL,
    `non_locked`              bit(1)                                  DEFAULT NULL,
    `credentials_non_expired` bit(1)                                  DEFAULT NULL,
    `email_address`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `email_status`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `last_login_time`         datetime                                DEFAULT NULL,
    `lock_time`               datetime                                DEFAULT NULL,
    `phone_number`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `phone_status`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `properties`              text COLLATE utf8mb4_unicode_ci,
    `created_at`              datetime                                DEFAULT NULL,
    `created_by`              bigint(20)                              DEFAULT NULL,
    `updated_at`              datetime                                DEFAULT NULL,
    `updated_by`              bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_t1iph3dfc25ukwcl9xemtnojn` (`username`),
    UNIQUE KEY `UK_AUTH_USER_USERNAME` (`username`),
    UNIQUE KEY `UK_AUTH_USER_PHONE` (`user_type`, `phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for auth_user_status
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_status`;
CREATE TABLE `auth_user_status`
(
    `user_id`      bigint(20) NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `availability` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `emoji`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `expires_at`   datetime                                DEFAULT NULL,
    `message`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cardhop_contact
-- ----------------------------
DROP TABLE IF EXISTS `cardhop_contact`;
CREATE TABLE `cardhop_contact`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime                                 DEFAULT NULL,
    `created_by`  bigint(20)                               DEFAULT NULL,
    `updated_at`  datetime                                 DEFAULT NULL,
    `updated_by`  bigint(20)                               DEFAULT NULL,
    `avatar`      varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `company`     varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `department`  varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `email`       varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `job_number`  varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `mobile`      varchar(20) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `name`        varchar(20) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `sex`         varchar(20) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `title`       varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `website`     varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `book_id`     bigint(20)                               DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKiqmu7tabkctg2t9n2xx6a4j52` (`book_id`),
    CONSTRAINT `FKiqmu7tabkctg2t9n2xx6a4j52` FOREIGN KEY (`book_id`) REFERENCES `cardhop_contact_book` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cardhop_contact_address
-- ----------------------------
DROP TABLE IF EXISTS `cardhop_contact_address`;
CREATE TABLE `cardhop_contact_address`
(
    `id`               bigint(20) NOT NULL,
    `created_at`       datetime                                DEFAULT NULL,
    `created_by`       bigint(20)                              DEFAULT NULL,
    `updated_at`       datetime                                DEFAULT NULL,
    `updated_by`       bigint(20)                              DEFAULT NULL,
    `city`             varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `country`          varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `district`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `postal_code`      varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `province`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `street`           varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `label`            varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `is_primary`       bit(1)     NOT NULL,
    `contact_id`       bigint(20) NOT NULL,
    `detailed_address` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `CARDHOP_CONTACT_ADDRESS_CID` (`contact_id`),
    CONSTRAINT `CARDHOP_CONTACT_ADDRESS_CID` FOREIGN KEY (`contact_id`) REFERENCES `cardhop_contact` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cardhop_contact_book
-- ----------------------------
DROP TABLE IF EXISTS `cardhop_contact_book`;
CREATE TABLE `cardhop_contact_book`
(
    `id`         bigint(20)                             NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `name`       varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `owner`      bigint(20)                             DEFAULT NULL,
    `owner_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`       varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cardhop_contact_email
-- ----------------------------
DROP TABLE IF EXISTS `cardhop_contact_email`;
CREATE TABLE `cardhop_contact_email`
(
    `id`         bigint(20)                             NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `address`    varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
    `status`     varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `label`      varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_primary` bit(1)                                 NOT NULL,
    `contact_id` bigint(20)                             NOT NULL,
    PRIMARY KEY (`id`),
    KEY `CARDHOP_CONTACT_EMAIL_CID` (`contact_id`),
    CONSTRAINT `CARDHOP_CONTACT_EMAIL_CID` FOREIGN KEY (`contact_id`) REFERENCES `cardhop_contact` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cardhop_contact_group
-- ----------------------------
DROP TABLE IF EXISTS `cardhop_contact_group`;
CREATE TABLE `cardhop_contact_group`
(
    `id`          bigint(20)                              NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`        int(11)                                 DEFAULT NULL,
    `level`       int(11)                                 DEFAULT NULL,
    `name`        varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `namespace`   varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `path`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `book_id`     bigint(20)                              DEFAULT NULL,
    `pid`         bigint(20)                              DEFAULT NULL,
    `count`       bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CONTACTS_GROUP_BID` (`book_id`),
    KEY `FK_CONTACTS_GROUP_PID` (`pid`),
    CONSTRAINT `FK_CONTACTS_GROUP_PID` FOREIGN KEY (`pid`) REFERENCES `cardhop_contact_group` (`id`),
    CONSTRAINT `FK_CONTACTS_GROUP_BID` FOREIGN KEY (`book_id`) REFERENCES `cardhop_contact_book` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cardhop_contact_group_contact
-- ----------------------------
DROP TABLE IF EXISTS `cardhop_contact_group_contact`;
CREATE TABLE `cardhop_contact_group_contact`
(
    `group_id`   bigint(20) NOT NULL,
    `contact_id` bigint(20) NOT NULL,
    KEY `FK_CONTACT_GROUP_CONTACT_CID` (`contact_id`),
    KEY `FK_CONTACT_GROUP_CONTACT_GID` (`group_id`),
    CONSTRAINT `FK_CONTACT_GROUP_CONTACT_GID` FOREIGN KEY (`group_id`) REFERENCES `cardhop_contact_group` (`id`),
    CONSTRAINT `FK_CONTACT_GROUP_CONTACT_CID` FOREIGN KEY (`contact_id`) REFERENCES `cardhop_contact` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cardhop_contact_phone_number
-- ----------------------------
DROP TABLE IF EXISTS `cardhop_contact_phone_number`;
CREATE TABLE `cardhop_contact_phone_number`
(
    `id`         bigint(20)                             NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `label`      varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `number`     varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
    `status`     varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `is_primary` bit(1)                                 NOT NULL,
    `contact_id` bigint(20)                             NOT NULL,
    PRIMARY KEY (`id`),
    KEY `CARDHOP_CONTACT_PHONE_CID` (`contact_id`),
    CONSTRAINT `CARDHOP_CONTACT_PHONE_CID` FOREIGN KEY (`contact_id`) REFERENCES `cardhop_contact` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cloud_drive
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive`;
CREATE TABLE `cloud_drive`
(
    `id`         bigint(20)                             NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `name`       varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `owner_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `owner`      bigint(20)                             DEFAULT NULL,
    `type`       varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `space_id`   varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_STORAGE_CLOUD_DRIVE` (`owner_type`, `owner`, `type`),
    KEY `FK_CLOUD_DRIVE_SID` (`space_id`),
    CONSTRAINT `FK_CLOUD_DRIVE_SID` FOREIGN KEY (`space_id`) REFERENCES `storage_space` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cloud_drive_quota
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive_quota`;
CREATE TABLE `cloud_drive_quota`
(
    `drive_id`   bigint(20) NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    `count`      int(11)    DEFAULT NULL,
    `size`       bigint(11) DEFAULT NULL,
    `usage`      bigint(11) DEFAULT NULL,
    PRIMARY KEY (`drive_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article
-- ----------------------------
DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article`
(
    `id`                  bigint(20)                             NOT NULL,
    `slug`                varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status`              varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `summary`             varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `author`              varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `store_template_id`   varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `body_id`             bigint(20)                              DEFAULT NULL,
    `image`               json                                    DEFAULT NULL,
    `attachments`         json                                    DEFAULT NULL,
    `last_comment_time`   datetime                                DEFAULT NULL,
    `published_at`        datetime                                DEFAULT NULL,
    `validity`            bit(1)                                  DEFAULT NULL,
    `validity_end_date`   datetime                                DEFAULT NULL,
    `validity_start_date` datetime                                DEFAULT NULL,
    `category_id`         bigint(20)                             NOT NULL,
    `organization_id`     bigint(20)                             NOT NULL,
    `created_at`          datetime                                DEFAULT NULL,
    `created_by`          bigint(20)                              DEFAULT NULL,
    `updated_at`          datetime                                DEFAULT NULL,
    `updated_by`          bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ARTICLE_SLUG` (`organization_id`, `slug`),
    KEY `FK_CMS_ARTICLE_CATEGORY` (`category_id`),
    KEY `FK_ARTICLE_STORE_TEMPLATE` (`store_template_id`),
    CONSTRAINT `FK_ARTICLE_ORGANIZATION_ID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_ARTICLE_STORE_TEMPLATE` FOREIGN KEY (`store_template_id`) REFERENCES `cms_article_store_template` (`id`),
    CONSTRAINT `FK_CMS_ARTICLE_CATEGORY` FOREIGN KEY (`category_id`) REFERENCES `cms_article_category` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_author
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_author`;
CREATE TABLE `cms_article_author`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime                               DEFAULT NULL,
    `created_by`  bigint(20)                             DEFAULT NULL,
    `updated_at`  datetime                               DEFAULT NULL,
    `updated_by`  bigint(20)                             DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `article_id`  bigint(20)                             DEFAULT NULL,
    `employee_id` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ARTICLE_AUTHOR_AID` (`article_id`),
    KEY `FK_ARTICLE_AUTHOR_EID` (`employee_id`),
    CONSTRAINT `FK_ARTICLE_AUTHOR_AID` FOREIGN KEY (`article_id`) REFERENCES `cms_article` (`id`),
    CONSTRAINT `FK_ARTICLE_AUTHOR_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_category`;
CREATE TABLE `cms_article_category`
(
    `id`                bigint(20)                              NOT NULL,
    `slug`              varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `image`             json                                    DEFAULT NULL,
    `description`       varchar(400) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `icon`              varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`              int(11)                                 DEFAULT NULL,
    `level`             int(11)                                 DEFAULT NULL,
    `meta_data`         json                                    DEFAULT NULL,
    `name`              varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
    `path`              varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `pid`               bigint(20)                              DEFAULT NULL,
    `organization_id`   bigint(20)                              NOT NULL,
    `store_template_id` varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `page_enabled`      bit(1)                                  DEFAULT NULL,
    `route_id`          bigint(20)                              DEFAULT NULL,
    `component_id`      bigint(20)                              DEFAULT NULL,
    `created_at`        datetime                                DEFAULT NULL,
    `created_by`        bigint(20)                              DEFAULT NULL,
    `updated_at`        datetime                                DEFAULT NULL,
    `updated_by`        bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ARTICLE_CATEGORY_SLUG` (`organization_id`, `slug`),
    KEY `FK_ARTICLE_CATEGORY_PARENT` (`pid`),
    KEY `FK_ARTICLE_CATEGORY_STORE_TEMPLATE` (`store_template_id`),
    KEY `FK_ARTICLE_CATEGORY_PAGE_COMPONENT` (`component_id`),
    KEY `FK_ARTICLE_CATEGORY_PAGE_ROUTE` (`route_id`),
    CONSTRAINT `FK_ARTICLE_CATEGORY_ORGANIZATION_ID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_ARTICLE_CATEGORY_PAGE_COMPONENT` FOREIGN KEY (`component_id`) REFERENCES `nuwa_component` (`id`),
    CONSTRAINT `FK_ARTICLE_CATEGORY_PAGE_ROUTE` FOREIGN KEY (`route_id`) REFERENCES `nuwa_application_route` (`id`),
    CONSTRAINT `FK_ARTICLE_CATEGORY_PARENT` FOREIGN KEY (`pid`) REFERENCES `cms_article_category` (`id`),
    CONSTRAINT `FK_ARTICLE_CATEGORY_STORE_TEMPLATE` FOREIGN KEY (`store_template_id`) REFERENCES `cms_article_store_template` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_category_meta_field
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_category_meta_field`;
CREATE TABLE `cms_article_category_meta_field`
(
    `id`                  bigint(20) NOT NULL,
    `created_at`          datetime(6)                             DEFAULT NULL,
    `created_by`          bigint(20)                              DEFAULT NULL,
    `updated_at`          datetime(6)                             DEFAULT NULL,
    `updated_by`          bigint(20)                              DEFAULT NULL,
    `description`         varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `meta_key`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `namespace`           varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`                varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `meta_value`          varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `article_category_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ARTICLE_CATEGORY_META_FIELD` (`article_category_id`, `namespace`, `meta_key`),
    CONSTRAINT `FK_ARTICLE_CATEGORY_META_FIELD_ARTICLE_ID` FOREIGN KEY (`article_category_id`) REFERENCES `cms_article_category` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_feature
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_feature`;
CREATE TABLE `cms_article_feature`
(
    `id`            bigint(20)                              NOT NULL,
    `created_at`    datetime                                DEFAULT NULL,
    `created_by`    bigint(20)                              DEFAULT NULL,
    `updated_at`    datetime                                DEFAULT NULL,
    `updated_by`    bigint(20)                              DEFAULT NULL,
    `code`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`          varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
    `enable_review` bit(1)                                  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_feature_item
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_feature_item`;
CREATE TABLE `cms_article_feature_item`
(
    `article_id` bigint(20) NOT NULL,
    `feature_id` bigint(20) NOT NULL,
    KEY `FK_ARTICLE_FEATURE_ITEM_FID` (`feature_id`),
    KEY `FK_ARTICLE_FEATURE_ITEM_ARTICLE` (`article_id`),
    CONSTRAINT `FK_ARTICLE_FEATURE_ITEM_ARTICLE` FOREIGN KEY (`article_id`) REFERENCES `cms_article` (`id`),
    CONSTRAINT `FK_ARTICLE_FEATURE_ITEM_FID` FOREIGN KEY (`feature_id`) REFERENCES `cms_article_feature` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_meta_field
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_meta_field`;
CREATE TABLE `cms_article_meta_field`
(
    `id`          bigint(20) NOT NULL,
    `meta_key`    varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `meta_value`  varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `namespace`   varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `article_id`  bigint(20) NOT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime(6)                             DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ARTICLE_META_FIELD` (`article_id`, `namespace`, `meta_key`),
    CONSTRAINT `FK_CMS_ARTICLE_META_FIELD_ARTICLE_ID` FOREIGN KEY (`article_id`) REFERENCES `cms_article` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_meta_field_definition
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_meta_field_definition`;
CREATE TABLE `cms_article_meta_field_definition`
(
    `id`                  bigint(20) NOT NULL,
    `created_at`          datetime(6)                             DEFAULT NULL,
    `created_by`          bigint(20)                              DEFAULT NULL,
    `updated_at`          datetime(6)                             DEFAULT NULL,
    `updated_by`          bigint(20)                              DEFAULT NULL,
    `description`         varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `meta_key`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `namespace`           varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`                varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `article_category_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ARTICLE_META_FIELD_DEFINITION` (`article_category_id`, `namespace`, `meta_key`),
    CONSTRAINT `FK_CMS_ARTICLE_META_FIELD_DEFINITION_ARTICLE_CATEGORY_ID` FOREIGN KEY (`article_category_id`) REFERENCES `cms_article_category` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_store_template
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_store_template`;
CREATE TABLE `cms_article_store_template`
(
    `id`              varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`            int(11)                                 DEFAULT NULL,
    `storage_class`   varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `added_component` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `edit_component`  varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `list_component`  varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `view_component`  varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at`      datetime(6)                             DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime(6)                             DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_tag`;
CREATE TABLE `cms_article_tag`
(
    `id`                   bigint(20)                              NOT NULL,
    `created_at`           datetime                                DEFAULT NULL,
    `created_by`           bigint(20)                              DEFAULT NULL,
    `updated_at`           datetime                                DEFAULT NULL,
    `updated_by`           bigint(20)                              DEFAULT NULL,
    `code`                 varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `cover`                json                                    DEFAULT NULL,
    `description`          varchar(400) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`                 int(11)                                 DEFAULT NULL,
    `meta_data`            json                                    DEFAULT NULL,
    `name`                 varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
    `ownership_type`       varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `ownership_id`         bigint(20)                              DEFAULT NULL,
    `path`                 varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `pid`                  bigint(20)                              DEFAULT NULL,
    `slug`                 varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `metadata_description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `metadata_image`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `metadata_title`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `metadata_url`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `image`                json                                    DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ARTICLE_TAG_CODE` (`code`),
    UNIQUE KEY `UK_ARTICLE_TAG_SLUG` (`slug`),
    KEY `FK_ARTICLE_TAG_PARENT` (`pid`),
    CONSTRAINT `FK_ARTICLE_TAG_PARENT` FOREIGN KEY (`pid`) REFERENCES `cms_article_tag` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_article_tag_item
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_tag_item`;
CREATE TABLE `cms_article_tag_item`
(
    `article_id` bigint(20) NOT NULL,
    `tag_id`     bigint(20) NOT NULL,
    KEY `FK_ARTICLE_TAG_ITEM_TAG` (`tag_id`),
    KEY `FK_ARTICLE_TAG_ITEM_ARTICLE` (`article_id`),
    CONSTRAINT `FK_ARTICLE_TAG_ITEM_ARTICLE` FOREIGN KEY (`article_id`) REFERENCES `cms_article` (`id`),
    CONSTRAINT `FK_ARTICLE_TAG_ITEM_TAG` FOREIGN KEY (`tag_id`) REFERENCES `cms_article_tag` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_banner
-- ----------------------------
DROP TABLE IF EXISTS `cms_banner`;
CREATE TABLE `cms_banner`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `background`      tinytext COLLATE utf8mb4_unicode_ci,
    `background_type` varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `button_text`     varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description`     varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`         bit(1)                                  DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `subtitle`        varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title`           varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `url`             varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_circle_member
-- ----------------------------
DROP TABLE IF EXISTS `cms_circle_member`;
CREATE TABLE `cms_circle_member`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    `circle_id`  bigint(20) NOT NULL,
    `user_id`    bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_CIRCLE_MEMBER` (`circle_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_content
-- ----------------------------
DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content`
(
    `id`         bigint(20) NOT NULL,
    `type`       varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `text`       mediumtext COLLATE utf8mb4_unicode_ci,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_course
-- ----------------------------
DROP TABLE IF EXISTS `cms_course`;
CREATE TABLE `cms_course`
(
    `id`                bigint(20) NOT NULL,
    `created_at`        datetime                                DEFAULT NULL,
    `created_by`        bigint(20)                              DEFAULT NULL,
    `updated_at`        datetime                                DEFAULT NULL,
    `updated_by`        bigint(20)                              DEFAULT NULL,
    `control_type`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `cover`             varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `duration`          float                                   DEFAULT NULL,
    `introduction`      longtext COLLATE utf8mb4_unicode_ci,
    `name`              varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `notification_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `publish_date`      varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `employee`          bigint(20) NOT NULL,
    `top`               tinyint(4) NOT NULL                     DEFAULT '0',
    `type_id`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_course_learner
-- ----------------------------
DROP TABLE IF EXISTS `cms_course_learner`;
CREATE TABLE `cms_course_learner`
(
    `id`                bigint(20)                             NOT NULL,
    `created_at`        datetime   DEFAULT NULL,
    `created_by`        bigint(20) DEFAULT NULL,
    `updated_at`        datetime   DEFAULT NULL,
    `updated_by`        bigint(20) DEFAULT NULL,
    `employee`          bigint(20)                             NOT NULL,
    `learning_progress` int(11)    DEFAULT NULL,
    `type`              varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `course_id`         bigint(20)                             NOT NULL,
    PRIMARY KEY (`id`),
    KEY `CMS_SPECIAL_SUBSCRIBER_ID` (`course_id`),
    CONSTRAINT `CMS_SPECIAL_SUBSCRIBER_ID` FOREIGN KEY (`course_id`) REFERENCES `cms_course` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_course_learner_scope
-- ----------------------------
DROP TABLE IF EXISTS `cms_course_learner_scope`;
CREATE TABLE `cms_course_learner_scope`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime                                DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime                                DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    `scope`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `course_id`  bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `CMS_COURSE_LEARNER_SCOPE_CID` (`course_id`),
    CONSTRAINT `CMS_COURSE_LEARNER_SCOPE_CID` FOREIGN KEY (`course_id`) REFERENCES `cms_course` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_course_lesson
-- ----------------------------
DROP TABLE IF EXISTS `cms_course_lesson`;
CREATE TABLE `cms_course_lesson`
(
    `id`                bigint(20) NOT NULL,
    `created_at`        datetime                                DEFAULT NULL,
    `created_by`        bigint(20)                              DEFAULT NULL,
    `updated_at`        datetime                                DEFAULT NULL,
    `updated_by`        bigint(20)                              DEFAULT NULL,
    `lesson_type`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `article_id`        bigint(20) NOT NULL,
    `course_id`         bigint(20) NOT NULL,
    `course_section_id` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `CMS_COURSE_LESSON_AID` (`article_id`),
    KEY `CMS_COURSE_LESSON_CID` (`course_id`),
    KEY `CMS_COURSE_LESSON_SID` (`course_section_id`),
    CONSTRAINT `CMS_COURSE_LESSON_AID` FOREIGN KEY (`article_id`) REFERENCES `cms_article` (`id`),
    CONSTRAINT `CMS_COURSE_LESSON_CID` FOREIGN KEY (`course_id`) REFERENCES `cms_course` (`id`),
    CONSTRAINT `CMS_COURSE_LESSON_SID` FOREIGN KEY (`course_section_id`) REFERENCES `cms_course_section` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_course_lesson_record
-- ----------------------------
DROP TABLE IF EXISTS `cms_course_lesson_record`;
CREATE TABLE `cms_course_lesson_record`
(
    `id`                       bigint(20) NOT NULL,
    `created_at`               datetime                               DEFAULT NULL,
    `created_by`               bigint(20)                             DEFAULT NULL,
    `updated_at`               datetime                               DEFAULT NULL,
    `updated_by`               bigint(20)                             DEFAULT NULL,
    `lesson_learning_progress` int(11)                                DEFAULT NULL,
    `lesson_schedule`          varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `course`                   bigint(20) NOT NULL,
    `learner`                  bigint(20) NOT NULL,
    `lesson`                   bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_COURSE_LESSON_RECORD_ID` (`course`),
    KEY `FK_LEARNER_LESSON_RECORD_ID` (`learner`),
    KEY `FK_LESSON_LESSON_RECORD_ID` (`lesson`),
    CONSTRAINT `FK_COURSE_LESSON_RECORD_ID` FOREIGN KEY (`course`) REFERENCES `cms_course` (`id`),
    CONSTRAINT `FK_LEARNER_LESSON_RECORD_ID` FOREIGN KEY (`learner`) REFERENCES `cms_course_learner` (`id`),
    CONSTRAINT `FK_LESSON_LESSON_RECORD_ID` FOREIGN KEY (`lesson`) REFERENCES `cms_course_lesson` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_course_section
-- ----------------------------
DROP TABLE IF EXISTS `cms_course_section`;
CREATE TABLE `cms_course_section`
(
    `id`          bigint(20) NOT NULL,
    `content`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `lesson_type` int(11)                                 DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `course_id`   bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `CMS_COURSE_COURSE_SECTION_CID` (`course_id`),
    CONSTRAINT `CMS_COURSE_COURSE_SECTION_CID` FOREIGN KEY (`course_id`) REFERENCES `cms_course` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_special
-- ----------------------------
DROP TABLE IF EXISTS `cms_special`;
CREATE TABLE `cms_special`
(
    `id`               varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`       datetime                                 DEFAULT NULL,
    `created_by`       bigint(20)                               DEFAULT NULL,
    `updated_at`       datetime                                 DEFAULT NULL,
    `updated_by`       bigint(20)                               DEFAULT NULL,
    `cover`            varchar(500) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `introduction`     longtext COLLATE utf8mb4_unicode_ci,
    `issn`             varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `name`             varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `publish_date`     datetime                                 DEFAULT NULL,
    `subscriber_count` bigint(20)                               DEFAULT NULL,
    `summary`          varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_special_article
-- ----------------------------
DROP TABLE IF EXISTS `cms_special_article`;
CREATE TABLE `cms_special_article`
(
    `id`           bigint(20)                              NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `cover`        varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `periodical`   varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `publish_date` varchar(15) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `summary`      varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `title`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `article_id`   bigint(20)                              NOT NULL,
    `special_id`   varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_SPECIALARTICLE_ARTICLE` (`article_id`),
    KEY `FK_SPECIALARTICLE_SPECIAL` (`special_id`),
    CONSTRAINT `FK_SPECIALARTICLE_ARTICLE` FOREIGN KEY (`article_id`) REFERENCES `cms_article` (`id`),
    CONSTRAINT `FK_SPECIALARTICLE_SPECIAL` FOREIGN KEY (`special_id`) REFERENCES `cms_special` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cms_subscriber
-- ----------------------------
DROP TABLE IF EXISTS `cms_subscriber`;
CREATE TABLE `cms_subscriber`
(
    `id`         bigint(20)                             NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    `expires`    datetime                               NOT NULL,
    `name`       varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`       varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`      varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
    `special_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_SUBSCRIBER_UNIQUE` (`special_id`, `type`, `value`),
    CONSTRAINT `CMS_SPECIAL_SUBSCRIBER` FOREIGN KEY (`special_id`) REFERENCES `cms_special` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cn_asany_test_account
-- ----------------------------
DROP TABLE IF EXISTS `cn_asany_test_account`;
CREATE TABLE `cn_asany_test_account`
(
    `id`         bigint(20) NOT NULL,
    `created_by` bigint(20)  DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `updated_by` bigint(20)  DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for cn_asany_test_user
-- ----------------------------
DROP TABLE IF EXISTS `cn_asany_test_user`;
CREATE TABLE `cn_asany_test_user`
(
    `id`         bigint(20) NOT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `created_at` datetime(6)                             DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime(6)                             DEFAULT NULL,
    `name`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for data_set_config
-- ----------------------------
DROP TABLE IF EXISTS `data_set_config`;
CREATE TABLE `data_set_config`
(
    `id`            bigint(20) NOT NULL,
    `created_at`    datetime                               DEFAULT NULL,
    `created_by`    bigint(20)                             DEFAULT NULL,
    `updated_at`    datetime                               DEFAULT NULL,
    `updated_by`    bigint(20)                             DEFAULT NULL,
    `config_store`  json                                   DEFAULT NULL,
    `fields_store`  json                                   DEFAULT NULL,
    `filters_store` json                                   DEFAULT NULL,
    `name`          varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `datasource_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_DATASET_CONFIG_DATASOURCE` (`datasource_id`),
    CONSTRAINT `FK_DATASET_CONFIG_DATASOURCE` FOREIGN KEY (`datasource_id`) REFERENCES `data_source_config` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for data_source_config
-- ----------------------------
DROP TABLE IF EXISTS `data_source_config`;
CREATE TABLE `data_source_config`
(
    `id`           bigint(20) NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `description`  varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `config_store` json                                    DEFAULT NULL,
    `name`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`         varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`
(
    `next_val` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for hibernate_sequences
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequences`;
CREATE TABLE `hibernate_sequences`
(
    `sequence_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `next_val`      bigint(20) DEFAULT NULL,
    PRIMARY KEY (`sequence_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_domain
-- ----------------------------
DROP TABLE IF EXISTS `james_domain`;
CREATE TABLE `james_domain`
(
    `domain_name`     varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`      datetime   DEFAULT NULL,
    `created_by`      bigint(20) DEFAULT NULL,
    `updated_at`      datetime   DEFAULT NULL,
    `updated_by`      bigint(20) DEFAULT NULL,
    `organization_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`domain_name`),
    KEY `FK_JAMES_DOMAIN_ORG` (`organization_id`),
    CONSTRAINT `FK_JAMES_DOMAIN_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_mail
-- ----------------------------
DROP TABLE IF EXISTS `james_mail`;
CREATE TABLE `james_mail`
(
    `mail_uid`                  bigint(20) NOT NULL,
    `mailbox_id`                bigint(20) NOT NULL,
    `mail_is_answered`          bit(1)     NOT NULL,
    `mail_body_start_octet`     int(11)    NOT NULL,
    `mail_content_octets_count` bigint(20) NOT NULL,
    `mail_is_deleted`           bit(1)     NOT NULL,
    `mail_is_draft`             bit(1)     NOT NULL,
    `mail_is_flagged`           bit(1)     NOT NULL,
    `mail_date`                 datetime   NOT NULL,
    `mail_mime_type`            varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `mail_modseq`               bigint(20)                              DEFAULT NULL,
    `mail_is_recent`            bit(1)     NOT NULL,
    `mail_is_seen`              bit(1)     NOT NULL,
    `mail_mime_subtype`         varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `mail_textual_line_count`   bigint(20)                              DEFAULT NULL,
    `mail_bytes`                longblob   NOT NULL,
    `header_bytes`              longblob   NOT NULL,
    `created_at`                datetime                                DEFAULT NULL,
    `created_by`                bigint(20)                              DEFAULT NULL,
    `updated_at`                datetime                                DEFAULT NULL,
    `updated_by`                bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`mail_uid`, `mailbox_id`),
    KEY `FK_JAMES_MAIL_MAILBOX` (`mailbox_id`),
    CONSTRAINT `FK_JAMES_MAIL_MAILBOX` FOREIGN KEY (`mailbox_id`) REFERENCES `james_mailbox` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_mail_property
-- ----------------------------
DROP TABLE IF EXISTS `james_mail_property`;
CREATE TABLE `james_mail_property`
(
    `id`          bigint(20)                               NOT NULL,
    `line_number` int(11)                                  NOT NULL,
    `local_name`  varchar(500) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `mailbox_id`  bigint(20) DEFAULT NULL,
    `name_space`  varchar(500) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `mail_uid`    bigint(20) DEFAULT NULL,
    `value`       varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_JAMES_MAIL_PROPERTIES` (`mail_uid`, `mailbox_id`),
    CONSTRAINT `FK_JAMES_MAIL_PROPERTIES` FOREIGN KEY (`mail_uid`, `mailbox_id`) REFERENCES `james_mail` (`mail_uid`, `mailbox_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_mail_settings
-- ----------------------------
DROP TABLE IF EXISTS `james_mail_settings`;
CREATE TABLE `james_mail_settings`
(
    `user_id`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_mail_settings_mailboxes
-- ----------------------------
DROP TABLE IF EXISTS `james_mail_settings_mailboxes`;
CREATE TABLE `james_mail_settings_mailboxes`
(
    `user_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `mailbox` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    KEY `FK_MAIL_SETTINGS_MAILBOXES` (`user_id`),
    CONSTRAINT `FK_MAIL_SETTINGS_MAILBOXES` FOREIGN KEY (`user_id`) REFERENCES `james_mail_settings` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_mail_userflag
-- ----------------------------
DROP TABLE IF EXISTS `james_mail_userflag`;
CREATE TABLE `james_mail_userflag`
(
    `userflag_id`   bigint(20)                              NOT NULL,
    `mailbox_id`    bigint(20) DEFAULT NULL,
    `userflag_name` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `mail_uid`      bigint(20) DEFAULT NULL,
    PRIMARY KEY (`userflag_id`),
    KEY `FK_JAMES_MAIL_USERFLAGS` (`mail_uid`, `mailbox_id`),
    CONSTRAINT `FK_JAMES_MAIL_USERFLAGS` FOREIGN KEY (`mail_uid`, `mailbox_id`) REFERENCES `james_mail` (`mail_uid`, `mailbox_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_mailbox
-- ----------------------------
DROP TABLE IF EXISTS `james_mailbox`;
CREATE TABLE `james_mailbox`
(
    `id`             bigint(20)                              NOT NULL,
    `highest_modseq` bigint(20)                              NOT NULL,
    `last_uid`       bigint(20)                              NOT NULL,
    `name`           varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `namespace`      varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `uid_validity`   bigint(20)                              NOT NULL,
    `user_name`      varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at`     datetime                                DEFAULT NULL,
    `created_by`     bigint(20)                              DEFAULT NULL,
    `updated_at`     datetime                                DEFAULT NULL,
    `updated_by`     bigint(20)                              DEFAULT NULL,
    `sort`           int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UK_MAILBOX_NAME` (`namespace`, `name`, `user_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_mailbox_annotation
-- ----------------------------
DROP TABLE IF EXISTS `james_mailbox_annotation`;
CREATE TABLE `james_mailbox_annotation`
(
    `annotation_key` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `mailbox_id`     bigint(20)                              NOT NULL,
    `value`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`annotation_key`, `mailbox_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_max_domain_message_count
-- ----------------------------
DROP TABLE IF EXISTS `james_max_domain_message_count`;
CREATE TABLE `james_max_domain_message_count`
(
    `domain` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`  bigint(20) DEFAULT NULL,
    PRIMARY KEY (`domain`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_max_domain_storage
-- ----------------------------
DROP TABLE IF EXISTS `james_max_domain_storage`;
CREATE TABLE `james_max_domain_storage`
(
    `domain` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`  bigint(20) DEFAULT NULL,
    PRIMARY KEY (`domain`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_max_global_message_count
-- ----------------------------
DROP TABLE IF EXISTS `james_max_global_message_count`;
CREATE TABLE `james_max_global_message_count`
(
    `quotaroot_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`        bigint(20) DEFAULT NULL,
    PRIMARY KEY (`quotaroot_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_max_global_storage
-- ----------------------------
DROP TABLE IF EXISTS `james_max_global_storage`;
CREATE TABLE `james_max_global_storage`
(
    `quotaroot_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`        bigint(20) DEFAULT NULL,
    PRIMARY KEY (`quotaroot_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_max_user_message_count
-- ----------------------------
DROP TABLE IF EXISTS `james_max_user_message_count`;
CREATE TABLE `james_max_user_message_count`
(
    `quotaroot_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`        bigint(20) DEFAULT NULL,
    PRIMARY KEY (`quotaroot_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_max_user_storage
-- ----------------------------
DROP TABLE IF EXISTS `james_max_user_storage`;
CREATE TABLE `james_max_user_storage`
(
    `quotaroot_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`        bigint(20) DEFAULT NULL,
    PRIMARY KEY (`quotaroot_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_quota_currentquota
-- ----------------------------
DROP TABLE IF EXISTS `james_quota_currentquota`;
CREATE TABLE `james_quota_currentquota`
(
    `currentquota_quotaroot`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `currentquota_messagecount` bigint(20) DEFAULT NULL,
    `currentquota_size`         bigint(20) DEFAULT NULL,
    PRIMARY KEY (`currentquota_quotaroot`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_recipient_rewrite
-- ----------------------------
DROP TABLE IF EXISTS `james_recipient_rewrite`;
CREATE TABLE `james_recipient_rewrite`
(
    `domain_name`    varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `user_name`      varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `target_address` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`domain_name`, `user_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_subscription
-- ----------------------------
DROP TABLE IF EXISTS `james_subscription`;
CREATE TABLE `james_subscription`
(
    `subscription_id` bigint(20)                              NOT NULL,
    `mailbox_name`    varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `user_name`       varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`subscription_id`),
    UNIQUE KEY `UK23ymxek1coli31efotkmfridi` (`user_name`, `mailbox_name`),
    UNIQUE KEY `UK_JAMES_SUBSCRIPTION_USER_MAILBOX` (`user_name`, `mailbox_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for james_user
-- ----------------------------
DROP TABLE IF EXISTS `james_user`;
CREATE TABLE `james_user`
(
    `user_name`               varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`              datetime                                DEFAULT NULL,
    `created_by`              bigint(20)                              DEFAULT NULL,
    `updated_at`              datetime                                DEFAULT NULL,
    `updated_by`              bigint(20)                              DEFAULT NULL,
    `password_hash_algorithm` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `password`                varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
    `domain_id`               varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `employee_id`             bigint(20)                              DEFAULT NULL,
    `organization_id`         bigint(20)                              DEFAULT NULL,
    `user_id`                 bigint(20)                              NOT NULL,
    `full_name`               varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`user_name`),
    KEY `FK_JAMES_USER_EMPLOYEE` (`domain_id`),
    KEY `FK_JAMES_USER_ORG` (`organization_id`),
    KEY `FK_JAMES_USER_UID` (`user_id`),
    CONSTRAINT `FK_JAMES_USER_EMPLOYEE` FOREIGN KEY (`domain_id`) REFERENCES `james_domain` (`domain_name`),
    CONSTRAINT `FK_JAMES_USER_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_JAMES_USER_UID` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application`;
CREATE TABLE `nuwa_application`
(
    `id`           bigint(20)                             NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `callback_url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `client_id`    varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`      bit(1)                                  DEFAULT NULL,
    `logo`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`         varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `title`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`         varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `url`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ownership`    bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_APPLICATION_CLIENT_ID` (`client_id`),
    UNIQUE KEY `UK_APPLICATION_NAME` (`name`),
    KEY `FK_APPLICATION_OWNERSHIP` (`ownership`),
    CONSTRAINT `FK_APPLICATION_OWNERSHIP` FOREIGN KEY (`ownership`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_dependency
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_dependency`;
CREATE TABLE `nuwa_application_dependency`
(
    `id`             bigint(20) NOT NULL,
    `name`           varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`           varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `value`          varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `version`        varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `application_id` bigint(20) NOT NULL,
    `created_at`     datetime(6)                            DEFAULT NULL,
    `created_by`     bigint(20)                             DEFAULT NULL,
    `updated_at`     datetime(6)                            DEFAULT NULL,
    `updated_by`     bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_APPLICATION_SUBSCRIPTION_ORG` (`application_id`),
    CONSTRAINT `FK_APPLICATION_DEPENDENCY_APPID` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_licence
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_licence`;
CREATE TABLE `nuwa_application_licence`
(
    `id`             bigint(20) NOT NULL,
    `created_at`     datetime                                 DEFAULT NULL,
    `created_by`     bigint(20)                               DEFAULT NULL,
    `updated_at`     datetime                                 DEFAULT NULL,
    `updated_by`     bigint(20)                               DEFAULT NULL,
    `licence_key`    varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`           varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `application_id` bigint(20)                               DEFAULT NULL,
    `status`         varchar(50) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `ownership`      bigint(20)                               DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_APPLICATION_LICENCE_APP` (`application_id`),
    KEY `FK_APPLICATION_LICENCE_OWNERSHIP` (`ownership`),
    CONSTRAINT `FK_APPLICATION_LICENCE_APP` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`),
    CONSTRAINT `FK_APPLICATION_LICENCE_OWNERSHIP` FOREIGN KEY (`ownership`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_menu
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_menu`;
CREATE TABLE `nuwa_application_menu`
(
    `id`                    bigint(20) NOT NULL,
    `created_at`            datetime                                DEFAULT NULL,
    `created_by`            bigint(20)                              DEFAULT NULL,
    `updated_at`            datetime                                DEFAULT NULL,
    `updated_by`            bigint(20)                              DEFAULT NULL,
    `authority`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `authorized`            bit(1)                                  DEFAULT NULL,
    `badge`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`               bit(1)                                  DEFAULT NULL,
    `hide_in_breadcrumb`    bit(1)                                  DEFAULT NULL,
    `icon`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`                  int(11)                                 DEFAULT NULL,
    `level`                 int(11)                                 DEFAULT NULL,
    `name`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `path`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `application_id`        bigint(20) NOT NULL,
    `pid`                   bigint(20)                              DEFAULT NULL,
    `component_id`          bigint(20)                              DEFAULT NULL,
    `hide_children_in_menu` bit(1)                                  DEFAULT NULL,
    `hide_in_menu`          bit(1)                                  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_APPLICATION_MENU_APPID` (`application_id`),
    KEY `FK_APPLICATION_MENU_PID` (`pid`),
    KEY `FK_APPLICATION_MENU_COMPONENT` (`component_id`),
    CONSTRAINT `FK_APPLICATION_MENU_APPID` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`),
    CONSTRAINT `FK_APPLICATION_MENU_COMPONENT` FOREIGN KEY (`component_id`) REFERENCES `nuwa_component` (`id`),
    CONSTRAINT `FK_APPLICATION_MENU_PID` FOREIGN KEY (`pid`) REFERENCES `nuwa_application_menu` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_pricing_plan
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_pricing_plan`;
CREATE TABLE `nuwa_application_pricing_plan`
(
    `id`             bigint(20) NOT NULL,
    `created_at`     datetime                                DEFAULT NULL,
    `created_by`     bigint(20)                              DEFAULT NULL,
    `updated_at`     datetime                                DEFAULT NULL,
    `updated_by`     bigint(20)                              DEFAULT NULL,
    `name`           varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `payment_cycle`  int(11)                                 DEFAULT NULL,
    `price`          decimal(19, 2)                          DEFAULT NULL,
    `status`         varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `application_id` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_APPLICATION_PRICING_PLAN_APP` (`application_id`),
    CONSTRAINT `FK_APPLICATION_PRICING_PLAN_APP` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_route
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_route`;
CREATE TABLE `nuwa_application_route`
(
    `id`                 bigint(20)                             NOT NULL,
    `name`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `access`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `authorized`         bit(1)                                  DEFAULT NULL,
    `enabled`            bit(1)                                  DEFAULT NULL,
    `hide_in_breadcrumb` bit(1)                                  DEFAULT NULL,
    `icon`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`               int(11)                                 DEFAULT NULL,
    `level`              int(11)                                 DEFAULT NULL,
    `path`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `redirect`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `application_id`     bigint(20)                             NOT NULL,
    `component_id`       bigint(20)                              DEFAULT NULL,
    `breadcrumb_id`      bigint(20)                              DEFAULT NULL,
    `pid`                bigint(20)                              DEFAULT NULL,
    `space`              varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `layout_pure`        bit(1)                                  DEFAULT NULL,
    `hide_menu`          bit(1)                                  DEFAULT NULL,
    `created_at`         datetime                                DEFAULT NULL,
    `created_by`         bigint(20)                              DEFAULT NULL,
    `updated_at`         datetime                                DEFAULT NULL,
    `updated_by`         bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_APPLICATION_ROUTE_APPID` (`application_id`),
    KEY `FK_APPLICATION_ROUTE_COMPONENT` (`component_id`),
    KEY `FK_APPLICATION_ROUTE_PID` (`pid`),
    KEY `FK_APPLICATION_ROUTE_SPACE` (`space`),
    KEY `FK_APPLICATION_ROUTE_BREADCRUMB` (`breadcrumb_id`),
    CONSTRAINT `FK_APPLICATION_ROUTE_APPID` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`),
    CONSTRAINT `FK_APPLICATION_ROUTE_BREADCRUMB` FOREIGN KEY (`breadcrumb_id`) REFERENCES `nuwa_component` (`id`),
    CONSTRAINT `FK_APPLICATION_ROUTE_COMPONENT` FOREIGN KEY (`component_id`) REFERENCES `nuwa_component` (`id`),
    CONSTRAINT `FK_APPLICATION_ROUTE_PID` FOREIGN KEY (`pid`) REFERENCES `nuwa_application_route` (`id`),
    CONSTRAINT `FK_APPLICATION_ROUTE_SPACE` FOREIGN KEY (`space`) REFERENCES `nuwa_routespace` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_routespace
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_routespace`;
CREATE TABLE `nuwa_application_routespace`
(
    `routespace_id`  varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `application_id` bigint(20)                             NOT NULL,
    KEY `FK_APPLICATION_ROUTESPACE_APPID` (`application_id`),
    KEY `FK_APPLICATION_ROUTESPACE_SPACEID` (`routespace_id`),
    CONSTRAINT `FK_APPLICATION_ROUTESPACE_APPID` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`),
    CONSTRAINT `FK_APPLICATION_ROUTESPACE_SPACEID` FOREIGN KEY (`routespace_id`) REFERENCES `nuwa_routespace` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_subscription
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_subscription`;
CREATE TABLE `nuwa_application_subscription`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                               DEFAULT NULL,
    `created_by`      bigint(20)                             DEFAULT NULL,
    `updated_at`      datetime                               DEFAULT NULL,
    `updated_by`      bigint(20)                             DEFAULT NULL,
    `status`          varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `application_id`  bigint(20)                             DEFAULT NULL,
    `organization_id` bigint(20)                             DEFAULT NULL,
    `type`            int(11)                                DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_APPLICATION_SUBSCRIPTION_ORG` (`application_id`, `organization_id`),
    KEY `FK_APPLICATION_SUBSCRIPTION_ORG` (`organization_id`),
    CONSTRAINT `FK_APPLICATION_SUBSCRIPTION_APP` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`),
    CONSTRAINT `FK_APPLICATION_SUBSCRIPTION_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_template
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_template`;
CREATE TABLE `nuwa_application_template`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_template_menu
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_template_menu`;
CREATE TABLE `nuwa_application_template_menu`
(
    `id`             bigint(20) NOT NULL,
    `created_at`     datetime                                DEFAULT NULL,
    `created_by`     bigint(20)                              DEFAULT NULL,
    `updated_at`     datetime                                DEFAULT NULL,
    `updated_by`     bigint(20)                              DEFAULT NULL,
    `authority`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `authorized`     bit(1)                                  DEFAULT NULL,
    `badge`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`        bit(1)                                  DEFAULT NULL,
    `icon`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`           bigint(20)                              DEFAULT NULL,
    `level`          int(11)                                 DEFAULT NULL,
    `name`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `path`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `application_id` bigint(20) NOT NULL,
    `pid`            bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_APPLICATION_TEMPLATE_MENU_APPID` (`application_id`),
    KEY `FK_APPLICATION_TEMPLATE_MENU_PID` (`pid`),
    CONSTRAINT `FK_APPLICATION_TEMPLATE_MENU_APPID` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application_template` (`id`),
    CONSTRAINT `FK_APPLICATION_TEMPLATE_MENU_PID` FOREIGN KEY (`pid`) REFERENCES `nuwa_application_template_menu` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_application_template_route
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_application_template_route`;
CREATE TABLE `nuwa_application_template_route`
(
    `id`                    bigint(20) NOT NULL,
    `created_at`            datetime                                DEFAULT NULL,
    `created_by`            bigint(20)                              DEFAULT NULL,
    `updated_at`            datetime                                DEFAULT NULL,
    `updated_by`            bigint(20)                              DEFAULT NULL,
    `authority`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `authorized`            bit(1)                                  DEFAULT NULL,
    `enabled`               bit(1)                                  DEFAULT NULL,
    `hide_children_in_menu` bit(1)                                  DEFAULT NULL,
    `hide_in_breadcrumb`    bit(1)                                  DEFAULT NULL,
    `hide_in_menu`          bit(1)                                  DEFAULT NULL,
    `icon`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`                  bigint(20)                              DEFAULT NULL,
    `level`                 int(11)                                 DEFAULT NULL,
    `name`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `path`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `redirect`              varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `application_id`        bigint(20) NOT NULL,
    `component_id`          bigint(20)                              DEFAULT NULL,
    `pid`                   bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_APPLICATION_TEMPLATE_ROUTE_APPID` (`application_id`),
    KEY `FK_APPLICATION_TEMPLATE_ROUTE_COMPONENT` (`component_id`),
    KEY `FK_APPLICATION_TEMPLATE_ROUTE_PID` (`pid`),
    CONSTRAINT `FK_APPLICATION_TEMPLATE_ROUTE_APPID` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application_template` (`id`),
    CONSTRAINT `FK_APPLICATION_TEMPLATE_ROUTE_COMPONENT` FOREIGN KEY (`component_id`) REFERENCES `nuwa_component` (`id`),
    CONSTRAINT `FK_APPLICATION_TEMPLATE_ROUTE_PID` FOREIGN KEY (`pid`) REFERENCES `nuwa_application_template_route` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_component
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_component`;
CREATE TABLE `nuwa_component`
(
    `id`          bigint(20)                             NOT NULL,
    `type`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `scope`       varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `title`       varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `image`       json                                    DEFAULT NULL,
    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `template`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `blocks`      json                                    DEFAULT NULL,
    `metadata`    text COLLATE utf8mb4_unicode_ci,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for nuwa_routespace
-- ----------------------------
DROP TABLE IF EXISTS `nuwa_routespace`;
CREATE TABLE `nuwa_routespace`
(
    `id`              varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`      datetime                               DEFAULT NULL,
    `created_by`      bigint(20)                             DEFAULT NULL,
    `updated_at`      datetime                               DEFAULT NULL,
    `updated_by`      bigint(20)                             DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `app_template_id` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ROUTESPACE_APP_TEMP_ID` (`app_template_id`),
    CONSTRAINT `FK_ROUTESPACE_APP_TEMP_ID` FOREIGN KEY (`app_template_id`) REFERENCES `nuwa_application_template` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for open_api_config
-- ----------------------------
DROP TABLE IF EXISTS `open_api_config`;
CREATE TABLE `open_api_config`
(
    `id`           bigint(20)                              NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `description`  varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `config_store` json                                    DEFAULT NULL,
    `name`         varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`         varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `appid`        varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_OPEN_API_CONFIG_APPID` (`type`, `appid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_department
-- ----------------------------
DROP TABLE IF EXISTS `org_department`;
CREATE TABLE `org_department`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `description`     varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`         bit(1)                                  DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `path`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sn`              varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`            int(11)                                 DEFAULT NULL,
    `dimension_id`    bigint(20) NOT NULL,
    `organization_id` bigint(20) NOT NULL,
    `pid`             bigint(20)                              DEFAULT NULL,
    `type`            bigint(20)                              DEFAULT NULL,
    `code`            varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `level`           int(11)                                 DEFAULT NULL,
    `count`           bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_DEPARTMENT_CODE` (`organization_id`, `dimension_id`, `code`),
    KEY `FK_DEPARTMENT_ORGANIZATION_DIMENSION` (`dimension_id`),
    KEY `FK_AUTH_DEPARTMENT_PID` (`pid`),
    KEY `FK_ORG_DEPARTMENT_TID` (`type`),
    CONSTRAINT `FK_AUTH_DEPARTMENT_PID` FOREIGN KEY (`pid`) REFERENCES `org_department` (`id`),
    CONSTRAINT `FK_DEPARTMENT_ORGANIZATION` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_DEPARTMENT_ORGANIZATION_DIMENSION` FOREIGN KEY (`dimension_id`) REFERENCES `org_organization_dimension` (`id`),
    CONSTRAINT `FK_ORG_DEPARTMENT_TID` FOREIGN KEY (`type`) REFERENCES `org_department_type` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_department_attribute
-- ----------------------------
DROP TABLE IF EXISTS `org_department_attribute`;
CREATE TABLE `org_department_attribute`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `attribute_name`  varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `attribute_value` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description`     varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `department_id`   bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_AUTH_DEPARTMENT_ATTRIBUTE_ID` (`department_id`),
    CONSTRAINT `FK_AUTH_DEPARTMENT_ATTRIBUTE_ID` FOREIGN KEY (`department_id`) REFERENCES `org_department` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_department_link
-- ----------------------------
DROP TABLE IF EXISTS `org_department_link`;
CREATE TABLE `org_department_link`
(
    `id`            bigint(20) NOT NULL,
    `created_at`    datetime                               DEFAULT NULL,
    `created_by`    bigint(20)                             DEFAULT NULL,
    `updated_at`    datetime                               DEFAULT NULL,
    `updated_by`    bigint(20)                             DEFAULT NULL,
    `link_id`       varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`          varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `department_id` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_DEPARTMENT_LINK_EID` (`department_id`),
    CONSTRAINT `FK_DEPARTMENT_LINK_EID` FOREIGN KEY (`department_id`) REFERENCES `org_department` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_department_type
-- ----------------------------
DROP TABLE IF EXISTS `org_department_type`;
CREATE TABLE `org_department_type`
(
    `id`              bigint(20) NOT NULL,
    `and_post`        bigint(20)                             DEFAULT NULL,
    `code`            varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `multi_sectoral`  bit(1)                                 DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `organization_id` bigint(20)                             DEFAULT NULL,
    `dimension_id`    bigint(20) NOT NULL,
    `created_at`      datetime                               DEFAULT NULL,
    `created_by`      bigint(20)                             DEFAULT NULL,
    `updated_at`      datetime                               DEFAULT NULL,
    `updated_by`      bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ORG_DEPARTMENT_TYPE_OID` (`organization_id`),
    KEY `FK_DEPARTMENT_TYPE_ORGANIZATION_DIMENSION` (`dimension_id`),
    CONSTRAINT `FK_DEPARTMENT_TYPE_ORGANIZATION_DIMENSION` FOREIGN KEY (`dimension_id`) REFERENCES `org_organization_dimension` (`id`),
    CONSTRAINT `FK_ORG_DEPARTMENT_TYPE_OID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee
-- ----------------------------
DROP TABLE IF EXISTS `org_employee`;
CREATE TABLE `org_employee`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `avatar`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `birthday`        date                                    DEFAULT NULL,
    `name`            varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sex`             varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `tags`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`         bigint(20)                              DEFAULT NULL,
    `organization_id` bigint(20) NOT NULL,
    `invite_status`   varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `job_number`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_EMPLOYEE_JOB_NUMBER` (`organization_id`, `job_number`),
    KEY `FK_EMPLOYEE_ORGANIZATION` (`organization_id`),
    KEY `FK_ORG_EMPLOYEE_USER` (`user_id`),
    CONSTRAINT `FK_EMPLOYEE_ORGANIZATION` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_ORG_EMPLOYEE_USER` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_address
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_address`;
CREATE TABLE `org_employee_address`
(
    `id`               bigint(20) NOT NULL,
    `city`             varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `country`          varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `district`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `label`            varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `postal_code`      varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `is_primary`       bit(1)     NOT NULL,
    `province`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `street`           varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `employee_id`      bigint(20) NOT NULL,
    `detailed_address` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `ORG_EMPLOYEE_ADDRESS_EID` (`employee_id`),
    CONSTRAINT `ORG_EMPLOYEE_ADDRESS_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_email
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_email`;
CREATE TABLE `org_employee_email`
(
    `id`          bigint(20)                             NOT NULL,
    `created_at`  datetime                               DEFAULT NULL,
    `created_by`  bigint(20)                             DEFAULT NULL,
    `updated_at`  datetime                               DEFAULT NULL,
    `updated_by`  bigint(20)                             DEFAULT NULL,
    `label`       varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_primary`  bit(1)                                 NOT NULL,
    `status`      varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `employee_id` bigint(20)                             NOT NULL,
    `address`     varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `ORG_EMPLOYEE_EMAIL_EID` (`employee_id`),
    CONSTRAINT `ORG_EMPLOYEE_EMAIL_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_group
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_group`;
CREATE TABLE `org_employee_group`
(
    `id`          bigint(20)                              NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`     bit(1)                                  DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `scope`       varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_EMPLOYEE_GROUP_SCOPEID` (`scope`),
    CONSTRAINT `FK_EMPLOYEE_GROUP_SCOPEID` FOREIGN KEY (`scope`) REFERENCES `org_employee_group_scope` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_group_employee
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_group_employee`;
CREATE TABLE `org_employee_group_employee`
(
    `employee_id` bigint(20) NOT NULL,
    `group_id`    bigint(20) NOT NULL,
    KEY `FK_EMPLOYEE_GROUP_GID` (`group_id`),
    KEY `FK_EMPLOYEE_GROUP_EID` (`employee_id`),
    CONSTRAINT `FK_EMPLOYEE_GROUP_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`),
    CONSTRAINT `FK_EMPLOYEE_GROUP_GID` FOREIGN KEY (`group_id`) REFERENCES `org_employee_group` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_group_scope
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_group_scope`;
CREATE TABLE `org_employee_group_scope`
(
    `id`              varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `name`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `organization_id` bigint(20)                              NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_GROUP_SCOPE_OID` (`organization_id`),
    CONSTRAINT `FK_GROUP_SCOPE_OID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_identity
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_identity`;
CREATE TABLE `org_employee_identity`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime   DEFAULT NULL,
    `created_by`      bigint(20) DEFAULT NULL,
    `updated_at`      datetime   DEFAULT NULL,
    `updated_by`      bigint(20) DEFAULT NULL,
    `department_id`   bigint(20) DEFAULT NULL,
    `dimension_id`    bigint(20) NOT NULL,
    `employee_id`     bigint(20) NOT NULL,
    `organization_id` bigint(20) NOT NULL,
    `position_id`     bigint(20) DEFAULT NULL,
    `status_id`       bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_EMPLOYEE_IDENTITY_UNIQUE` (`organization_id`, `dimension_id`, `employee_id`),
    KEY `FK_ORGANIZATION_DIMENSION_MEMBER_DID` (`department_id`),
    KEY `FK_ORGANIZATION_DIMENSION_MEMBER_EID` (`employee_id`),
    KEY `FK_ORGANIZATION_DIMENSION_MEMBER_PID` (`position_id`),
    KEY `FK_ORGANIZATION_DIMENSION_MEMBER_SID` (`status_id`),
    KEY `FK_ORGANIZATION_DIMENSION_MEMBER_DIMENSION` (`dimension_id`),
    CONSTRAINT `FK_ORGANIZATION_DIMENSION_MEMBER_DID` FOREIGN KEY (`department_id`) REFERENCES `org_department` (`id`),
    CONSTRAINT `FK_ORGANIZATION_DIMENSION_MEMBER_DIMENSION` FOREIGN KEY (`dimension_id`) REFERENCES `org_organization_dimension` (`id`),
    CONSTRAINT `FK_ORGANIZATION_DIMENSION_MEMBER_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`),
    CONSTRAINT `FK_ORGANIZATION_DIMENSION_MEMBER_OID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_ORGANIZATION_DIMENSION_MEMBER_PID` FOREIGN KEY (`position_id`) REFERENCES `org_position` (`id`),
    CONSTRAINT `FK_ORGANIZATION_DIMENSION_MEMBER_SID` FOREIGN KEY (`status_id`) REFERENCES `org_employee_status` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_link
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_link`;
CREATE TABLE `org_employee_link`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime                               DEFAULT NULL,
    `created_by`  bigint(20)                             DEFAULT NULL,
    `updated_at`  datetime                               DEFAULT NULL,
    `updated_by`  bigint(20)                             DEFAULT NULL,
    `link_id`     varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`        varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `employee_id` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_EMPLOYEE_LINK_EID` (`employee_id`),
    CONSTRAINT `FK_EMPLOYEE_LINK_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_phone_number
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_phone_number`;
CREATE TABLE `org_employee_phone_number`
(
    `id`          bigint(20)                             NOT NULL,
    `created_at`  datetime                               DEFAULT NULL,
    `created_by`  bigint(20)                             DEFAULT NULL,
    `updated_at`  datetime                               DEFAULT NULL,
    `updated_by`  bigint(20)                             DEFAULT NULL,
    `label`       varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `number`      varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
    `status`      varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `is_primary`  bit(1)                                 NOT NULL,
    `employee_id` bigint(20)                             NOT NULL,
    PRIMARY KEY (`id`),
    KEY `ORG_EMPLOYEE_PHONE_EID` (`employee_id`),
    CONSTRAINT `ORG_EMPLOYEE_PHONE_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_position
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_position`;
CREATE TABLE `org_employee_position`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime   DEFAULT NULL,
    `created_by`      bigint(20) DEFAULT NULL,
    `updated_at`      datetime   DEFAULT NULL,
    `updated_by`      bigint(20) DEFAULT NULL,
    `is_primary`      bit(1)     DEFAULT NULL,
    `department_id`   bigint(20) NOT NULL,
    `employee_id`     bigint(20) NOT NULL,
    `organization_id` bigint(20) NOT NULL,
    `position_id`     bigint(20) DEFAULT NULL,
    `dimension_id`    bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `ORG_EMPLOYEE_POSITION_DID` (`department_id`),
    KEY `ORG_EMPLOYEE_POSITION_EID` (`employee_id`),
    KEY `FK_EMPLOYEE_POSITION_OID` (`organization_id`),
    KEY `ORG_EMPLOYEE_POSITION_PID` (`position_id`),
    KEY `FK_EMPLOYEE_POSITION_DIMENSION` (`dimension_id`),
    CONSTRAINT `FK_EMPLOYEE_POSITION_DIMENSION` FOREIGN KEY (`dimension_id`) REFERENCES `org_organization_dimension` (`id`),
    CONSTRAINT `FK_EMPLOYEE_POSITION_OID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `ORG_EMPLOYEE_POSITION_DID` FOREIGN KEY (`department_id`) REFERENCES `org_department` (`id`),
    CONSTRAINT `ORG_EMPLOYEE_POSITION_EID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`),
    CONSTRAINT `ORG_EMPLOYEE_POSITION_PID` FOREIGN KEY (`position_id`) REFERENCES `org_position` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_employee_status
-- ----------------------------
DROP TABLE IF EXISTS `org_employee_status`;
CREATE TABLE `org_employee_status`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `code`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_default`      bit(1)                                  DEFAULT NULL,
    `name`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `dimension_id`    bigint(20) NOT NULL,
    `organization_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_EMPLOYEE_STATUS_CODE` (`organization_id`, `dimension_id`, `code`),
    KEY `FK_EMPLOYEE_STATUS_DIMENSION` (`dimension_id`),
    CONSTRAINT `FK_EMPLOYEE_STATUS_DIMENSION` FOREIGN KEY (`dimension_id`) REFERENCES `org_organization_dimension` (`id`),
    CONSTRAINT `FK_EMPLOYEE_STATUS_ORGANIZATION` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_job
-- ----------------------------
DROP TABLE IF EXISTS `org_job`;
CREATE TABLE `org_job`
(
    `id`              bigint(20)                             NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `description`     varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `level`           bigint(20)                              DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `organization_id` bigint(20)                             NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ORG_JOB_OID` (`organization_id`),
    CONSTRAINT `FK_ORG_JOB_OID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_organization
-- ----------------------------
DROP TABLE IF EXISTS `org_organization`;
CREATE TABLE `org_organization`
(
    `id`               bigint(20) NOT NULL,
    `code`             varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `name`             varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description`      varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`             int(11)                                 DEFAULT NULL,
    `ownership`        bigint(20)                              DEFAULT NULL,
    `created_at`       datetime                                DEFAULT NULL,
    `created_by`       bigint(20)                              DEFAULT NULL,
    `updated_at`       datetime                                DEFAULT NULL,
    `updated_by`       bigint(20)                              DEFAULT NULL,
    `ownership_id`     bigint(20)                              DEFAULT NULL,
    `logo`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `email`            varchar(25) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `city`             varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `country`          varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `detailed_address` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `district`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `postal_code`      varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `province`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `street`           varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `url`              varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ORGANIZATION_CODE` (`code`),
    KEY `FKsdtqghhkbc2fylngqye5vxw01` (`ownership_id`),
    CONSTRAINT `FKsdtqghhkbc2fylngqye5vxw01` FOREIGN KEY (`ownership_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_organization_dimension
-- ----------------------------
DROP TABLE IF EXISTS `org_organization_dimension`;
CREATE TABLE `org_organization_dimension`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `code`            varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description`     varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`            int(11)                                 DEFAULT NULL,
    `organization_id` bigint(20) NOT NULL,
    `count`           bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ORGANIZATION_DIMENSION_CODE` (`organization_id`, `code`),
    CONSTRAINT `FK_ORGANIZATION_DIMENSION_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_organization_invite
-- ----------------------------
DROP TABLE IF EXISTS `org_organization_invite`;
CREATE TABLE `org_organization_invite`
(
    `id`              bigint(20)                             NOT NULL,
    `created_at`      datetime   DEFAULT NULL,
    `created_by`      bigint(20) DEFAULT NULL,
    `updated_at`      datetime   DEFAULT NULL,
    `updated_by`      bigint(20) DEFAULT NULL,
    `status`          varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `organization_id` bigint(20)                             NOT NULL,
    `role_id`         bigint(20)                             NOT NULL,
    `user_id`         bigint(20)                             NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ORGANIZATION_INVITE_USER` (`user_id`, `organization_id`),
    KEY `FK_ORGANIZATION_INVITE_ORG` (`organization_id`),
    KEY `FK_ORGANIZATION_INVITE_ROLE` (`role_id`),
    CONSTRAINT `FK_ORGANIZATION_INVITE_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_ORGANIZATION_INVITE_ROLE` FOREIGN KEY (`role_id`) REFERENCES `auth_role` (`id`),
    CONSTRAINT `FK_ORGANIZATION_INVITE_USER` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_organization_member
-- ----------------------------
DROP TABLE IF EXISTS `org_organization_member`;
CREATE TABLE `org_organization_member`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime   DEFAULT NULL,
    `created_by`      bigint(20) DEFAULT NULL,
    `updated_at`      datetime   DEFAULT NULL,
    `updated_by`      bigint(20) DEFAULT NULL,
    `organization_id` bigint(20) NOT NULL,
    `role_id`         bigint(20) NOT NULL,
    `user_id`         bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ORGANIZATION_MEMBER_OUID` (`user_id`, `organization_id`),
    KEY `FK_ORGANIZATION_MEMBER_ORG` (`organization_id`),
    KEY `FK_ORGANIZATION_MEMBER_ROLE` (`role_id`),
    CONSTRAINT `FK_ORGANIZATION_MEMBER_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_ORGANIZATION_MEMBER_ROLE` FOREIGN KEY (`role_id`) REFERENCES `auth_role` (`id`),
    CONSTRAINT `FK_ORGANIZATION_MEMBER_USER` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_position
-- ----------------------------
DROP TABLE IF EXISTS `org_position`;
CREATE TABLE `org_position`
(
    `id`              bigint(20)                             NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `description`     varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `department_id`   bigint(20)                              DEFAULT NULL,
    `job_id`          bigint(20)                              DEFAULT NULL,
    `organization_id` bigint(20)                             NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ORG_POSITION_NAME` (`department_id`, `name`),
    KEY `FK_POSITION_JID` (`job_id`),
    KEY `FK_ORG_POSITION_OID` (`organization_id`),
    CONSTRAINT `FK_ORG_POSITION_OID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_POSITION_JID` FOREIGN KEY (`job_id`) REFERENCES `org_job` (`id`),
    CONSTRAINT `FK_POSITION_PID` FOREIGN KEY (`department_id`) REFERENCES `org_department` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_star
-- ----------------------------
DROP TABLE IF EXISTS `org_star`;
CREATE TABLE `org_star`
(
    `id`           bigint(20)                              NOT NULL,
    `created_at`   datetime   DEFAULT NULL,
    `created_by`   bigint(20) DEFAULT NULL,
    `updated_at`   datetime   DEFAULT NULL,
    `updated_by`   bigint(20) DEFAULT NULL,
    `value`        varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `reading_time` bigint(20) DEFAULT NULL,
    `type`         varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `employee_id`  bigint(20)                              NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ORG_STAR` (`type`, `value`, `employee_id`),
    KEY `FK_STAR_EMPLOYEE` (`employee_id`),
    CONSTRAINT `FK3kts3ro8xhpbn9d19nl0kincj` FOREIGN KEY (`type`) REFERENCES `org_star_type` (`id`),
    CONSTRAINT `FK_STAR_EMPLOYEE` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_star_type
-- ----------------------------
DROP TABLE IF EXISTS `org_star_type`;
CREATE TABLE `org_star_type`
(
    `id`         varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `name`       varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `value_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_team
-- ----------------------------
DROP TABLE IF EXISTS `org_team`;
CREATE TABLE `org_team`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                               DEFAULT NULL,
    `created_by`      bigint(20)                             DEFAULT NULL,
    `updated_at`      datetime                               DEFAULT NULL,
    `updated_by`      bigint(20)                             DEFAULT NULL,
    `code`            varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `organization_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_TEAM_ORG` (`organization_id`),
    CONSTRAINT `FK_TEAM_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for org_team_member
-- ----------------------------
DROP TABLE IF EXISTS `org_team_member`;
CREATE TABLE `org_team_member`
(
    `id`              bigint(20) NOT NULL,
    `created_at`      datetime                                DEFAULT NULL,
    `created_by`      bigint(20)                              DEFAULT NULL,
    `updated_at`      datetime                                DEFAULT NULL,
    `updated_by`      bigint(20)                              DEFAULT NULL,
    `introduction`    varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`            int(11)                                 DEFAULT NULL,
    `title`           varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `organization_id` bigint(20) NOT NULL,
    `avatar`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `team_id`         bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_TEAM_MEMBER_ORG` (`organization_id`),
    KEY `FK_TEAM_MEMBER_TEAM` (`team_id`),
    CONSTRAINT `FK_TEAM_MEMBER_ORG` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`),
    CONSTRAINT `FK_TEAM_MEMBER_TEAM` FOREIGN KEY (`team_id`) REFERENCES `org_team` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue`;
CREATE TABLE `pm_issue`
(
    `id`            bigint(20) NOT NULL,
    `created_at`    datetime(6)                             DEFAULT NULL,
    `created_by`    bigint(20)                              DEFAULT NULL,
    `updated_at`    datetime(6)                             DEFAULT NULL,
    `updated_by`    bigint(20)                              DEFAULT NULL,
    `deleted`       bit(1)                                  DEFAULT NULL,
    `assignee`      bigint(20)                              DEFAULT NULL,
    `attachments`   json                                    DEFAULT NULL,
    `description`   varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `due_date`      datetime(6)                             DEFAULT NULL,
    `end_time`      datetime(6)                             DEFAULT NULL,
    `name`          varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `progress`      int(11)                                 DEFAULT NULL,
    `start_time`    datetime(6)                             DEFAULT NULL,
    `priority_id`   bigint(20)                              DEFAULT NULL,
    `project_id`    bigint(20) NOT NULL,
    `reporter`      bigint(20)                              DEFAULT NULL,
    `resolution_id` bigint(20)                              DEFAULT NULL,
    `status_id`     bigint(20)                              DEFAULT NULL,
    `time_track_id` bigint(20)                              DEFAULT NULL,
    `type_id`       bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ISSUE_PRIORITY_ISSUE` (`priority_id`),
    KEY `FK_ISSUE_PROJECT` (`project_id`),
    KEY `FKrqxrwnihrfwgned6g6i9a940q` (`reporter`),
    KEY `FK_ISSUE_RESOLUTION_ISSUE` (`resolution_id`),
    KEY `FK_ISSUE_STATUS_ISSUE` (`status_id`),
    KEY `FK_ISSUE_TIME_TRACK_ISSUE` (`time_track_id`),
    KEY `FK_ISSUE_TYPE_ISSUE` (`type_id`),
    CONSTRAINT `FK_ISSUE_PRIORITY_ISSUE` FOREIGN KEY (`priority_id`) REFERENCES `pm_issue_priority` (`id`),
    CONSTRAINT `FK_ISSUE_PROJECT` FOREIGN KEY (`project_id`) REFERENCES `pm_project` (`id`),
    CONSTRAINT `FK_ISSUE_RESOLUTION_ISSUE` FOREIGN KEY (`resolution_id`) REFERENCES `pm_issue_resolution` (`id`),
    CONSTRAINT `FK_ISSUE_STATUS_ISSUE` FOREIGN KEY (`status_id`) REFERENCES `pm_issue_status` (`id`),
    CONSTRAINT `FK_ISSUE_TIME_TRACK_ISSUE` FOREIGN KEY (`time_track_id`) REFERENCES `pm_issue_time_track` (`id`),
    CONSTRAINT `FK_ISSUE_TYPE_ISSUE` FOREIGN KEY (`type_id`) REFERENCES `pm_issue_type` (`id`),
    CONSTRAINT `FKrqxrwnihrfwgned6g6i9a940q` FOREIGN KEY (`reporter`) REFERENCES `pm_project_member` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_comment
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_comment`;
CREATE TABLE `pm_issue_comment`
(
    `id`           bigint(20) NOT NULL,
    `created_at`   datetime(6)                             DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime(6)                             DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `attachments`  json                                    DEFAULT NULL,
    `content`      varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `content_date` datetime(6)                             DEFAULT NULL,
    `uid`          bigint(20)                              DEFAULT NULL,
    `issue_id`     bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ISSUE_ANNOTATE_ISSUE` (`issue_id`),
    CONSTRAINT `FK_ISSUE_ANNOTATE_ISSUE` FOREIGN KEY (`issue_id`) REFERENCES `pm_issue` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_follow
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_follow`;
CREATE TABLE `pm_issue_follow`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `created_by` bigint(20)  DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` bigint(20)  DEFAULT NULL,
    `deleted`    bit(1)      DEFAULT NULL,
    `uid`        bigint(20)  DEFAULT NULL,
    `issue_id`   bigint(20)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ISSUE_FOLLOW_ISSUE` (`issue_id`),
    CONSTRAINT `FK_ISSUE_FOLLOW_ISSUE` FOREIGN KEY (`issue_id`) REFERENCES `pm_issue` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_priority
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_priority`;
CREATE TABLE `pm_issue_priority`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime(6)                             DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `color`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `icon`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`        int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_priority_scheme
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_priority_scheme`;
CREATE TABLE `pm_issue_priority_scheme`
(
    `id`               bigint(20) NOT NULL,
    `created_at`       datetime(6)                             DEFAULT NULL,
    `created_by`       bigint(20)                              DEFAULT NULL,
    `updated_at`       datetime(6)                             DEFAULT NULL,
    `updated_by`       bigint(20)                              DEFAULT NULL,
    `description`      varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`             varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `default_priority` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ISSUE_PRIORITY_SCHEME_DEFAULT` (`default_priority`),
    CONSTRAINT `FK_ISSUE_PRIORITY_SCHEME_DEFAULT` FOREIGN KEY (`default_priority`) REFERENCES `pm_issue_priority` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_priority_scheme_item
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_priority_scheme_item`;
CREATE TABLE `pm_issue_priority_scheme_item`
(
    `scheme_id`   bigint(20) NOT NULL,
    `priority_id` bigint(20) NOT NULL,
    KEY `FKkeu7nftv8a2dfbnkmp0089ma` (`priority_id`),
    KEY `FK_ISSUE_PRIORITY_SCHEME_ITEM_PRIORITY` (`scheme_id`),
    CONSTRAINT `FK_ISSUE_PRIORITY_SCHEME_ITEM_PRIORITY` FOREIGN KEY (`scheme_id`) REFERENCES `pm_issue_priority_scheme` (`id`),
    CONSTRAINT `FKkeu7nftv8a2dfbnkmp0089ma` FOREIGN KEY (`priority_id`) REFERENCES `pm_issue_priority` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_resolution
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_resolution`;
CREATE TABLE `pm_issue_resolution`
(
    `id`            bigint(20) NOT NULL,
    `created_at`    datetime(6)                             DEFAULT NULL,
    `created_by`    bigint(20)                              DEFAULT NULL,
    `updated_at`    datetime(6)                             DEFAULT NULL,
    `updated_by`    bigint(20)                              DEFAULT NULL,
    `description`   varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`          varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`          int(11)                                 DEFAULT NULL,
    `issue_project` bigint(20)                              DEFAULT NULL,
    `project`       bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ISSUE_RESOLUTION_PROJECT` (`issue_project`),
    CONSTRAINT `FK_ISSUE_RESOLUTION_PROJECT` FOREIGN KEY (`issue_project`) REFERENCES `pm_project` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_status
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_status`;
CREATE TABLE `pm_issue_status`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime(6)                             DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `category`    varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `enabled`     bit(1)                                  DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`        int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_time_track
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_time_track`;
CREATE TABLE `pm_issue_time_track`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `created_by` bigint(20)  DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` bigint(20)  DEFAULT NULL,
    `estimated`  int(11)     DEFAULT NULL,
    `logged`     bigint(20)  DEFAULT NULL,
    `remaining`  int(11)     DEFAULT NULL,
    `track_time` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_type
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_type`;
CREATE TABLE `pm_issue_type`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime(6)                             DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `deleted`     bit(1)                                  DEFAULT NULL,
    `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sort`        int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_type_scheme
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_type_scheme`;
CREATE TABLE `pm_issue_type_scheme`
(
    `id`                 bigint(20) NOT NULL,
    `created_at`         datetime(6)                             DEFAULT NULL,
    `created_by`         bigint(20)                              DEFAULT NULL,
    `updated_at`         datetime(6)                             DEFAULT NULL,
    `updated_by`         bigint(20)                              DEFAULT NULL,
    `description`        varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`               varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `default_issue_type` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_GD_TYPE_SCHEME_DEFAULT_ISSUE_TYPE` (`default_issue_type`),
    CONSTRAINT `FK_GD_TYPE_SCHEME_DEFAULT_ISSUE_TYPE` FOREIGN KEY (`default_issue_type`) REFERENCES `pm_issue_type` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_type_scheme_item
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_type_scheme_item`;
CREATE TABLE `pm_issue_type_scheme_item`
(
    `scheme_id` bigint(20) NOT NULL,
    `type_id`   bigint(20) NOT NULL,
    KEY `FKcqafhseghpmupbexicuqeodtf` (`type_id`),
    KEY `FK_ISSUE_TYPE_SCHEME_ITEM_TID` (`scheme_id`),
    CONSTRAINT `FK_ISSUE_TYPE_SCHEME_ITEM_TID` FOREIGN KEY (`scheme_id`) REFERENCES `pm_issue_type_scheme` (`id`),
    CONSTRAINT `FKcqafhseghpmupbexicuqeodtf` FOREIGN KEY (`type_id`) REFERENCES `pm_issue_type` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_issue_worklog
-- ----------------------------
DROP TABLE IF EXISTS `pm_issue_worklog`;
CREATE TABLE `pm_issue_worklog`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime(6)                            DEFAULT NULL,
    `created_by`  bigint(20)                             DEFAULT NULL,
    `updated_at`  datetime(6)                            DEFAULT NULL,
    `updated_by`  bigint(20)                             DEFAULT NULL,
    `attachments` json                                   DEFAULT NULL,
    `content`     varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `log_time`    datetime(6)                            DEFAULT NULL,
    `uid`         bigint(20)                             DEFAULT NULL,
    `issue_id`    bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ISSUE_LOG_ISSUE` (`issue_id`),
    CONSTRAINT `FK_ISSUE_LOG_ISSUE` FOREIGN KEY (`issue_id`) REFERENCES `pm_issue` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_project
-- ----------------------------
DROP TABLE IF EXISTS `pm_project`;
CREATE TABLE `pm_project`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime(6)                             DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `logo`        json                                    DEFAULT NULL,
    `name`        varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for pm_project_member
-- ----------------------------
DROP TABLE IF EXISTS `pm_project_member`;
CREATE TABLE `pm_project_member`
(
    `id`          bigint(20)                             NOT NULL,
    `created_at`  datetime(6)                            DEFAULT NULL,
    `created_by`  bigint(20)                             DEFAULT NULL,
    `updated_at`  datetime(6)                            DEFAULT NULL,
    `updated_by`  bigint(20)                             DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`        varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `employee_id` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_PROJECT_MEMBER_EMPLOYEE_ID` (`employee_id`),
    CONSTRAINT `FK_PROJECT_MEMBER_EMPLOYEE_ID` FOREIGN KEY (`employee_id`) REFERENCES `org_employee` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`
(
    `SCHED_NAME`    varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_NAME`  varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `BLOB_DATA`     blob,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
    KEY `SCHED_NAME` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
    CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`
(
    `SCHED_NAME`    varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `CALENDAR_NAME` varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `CALENDAR`      blob                                    NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`
(
    `SCHED_NAME`      varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_NAME`    varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_GROUP`   varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `CRON_EXPRESSION` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TIME_ZONE_ID`    varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
    CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`
(
    `SCHED_NAME`        varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `ENTRY_ID`          varchar(95) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `TRIGGER_NAME`      varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_GROUP`     varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `INSTANCE_NAME`     varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `FIRED_TIME`        bigint(13)                              NOT NULL,
    `SCHED_TIME`        bigint(13)                              NOT NULL,
    `PRIORITY`          int(11)                                 NOT NULL,
    `STATE`             varchar(16) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `JOB_NAME`          varchar(190) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `JOB_GROUP`         varchar(190) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `IS_NONCONCURRENT`  varchar(1) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `REQUESTS_RECOVERY` varchar(1) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`),
    KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`, `INSTANCE_NAME`),
    KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`),
    KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
    KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`, `JOB_GROUP`),
    KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
    KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`
(
    `SCHED_NAME`        varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `JOB_NAME`          varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `JOB_GROUP`         varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DESCRIPTION`       varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `JOB_CLASS_NAME`    varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
    `IS_DURABLE`        varchar(1) COLLATE utf8mb4_unicode_ci   NOT NULL,
    `IS_NONCONCURRENT`  varchar(1) COLLATE utf8mb4_unicode_ci   NOT NULL,
    `IS_UPDATE_DATA`    varchar(1) COLLATE utf8mb4_unicode_ci   NOT NULL,
    `REQUESTS_RECOVERY` varchar(1) COLLATE utf8mb4_unicode_ci   NOT NULL,
    `JOB_DATA`          blob,
    PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
    KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`, `REQUESTS_RECOVERY`),
    KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`, `JOB_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`
(
    `SCHED_NAME` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `LOCK_NAME`  varchar(40) COLLATE utf8mb4_unicode_ci  NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`
(
    `SCHED_NAME`    varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`
(
    `SCHED_NAME`        varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `INSTANCE_NAME`     varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `LAST_CHECKIN_TIME` bigint(13)                              NOT NULL,
    `CHECKIN_INTERVAL`  bigint(13)                              NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`
(
    `SCHED_NAME`      varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_NAME`    varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_GROUP`   varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `REPEAT_COUNT`    bigint(7)                               NOT NULL,
    `REPEAT_INTERVAL` bigint(12)                              NOT NULL,
    `TIMES_TRIGGERED` bigint(10)                              NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
    CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`
(
    `SCHED_NAME`    varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_NAME`  varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `STR_PROP_1`    varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STR_PROP_2`    varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `STR_PROP_3`    varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `INT_PROP_1`    int(11)                                 DEFAULT NULL,
    `INT_PROP_2`    int(11)                                 DEFAULT NULL,
    `LONG_PROP_1`   bigint(20)                              DEFAULT NULL,
    `LONG_PROP_2`   bigint(20)                              DEFAULT NULL,
    `DEC_PROP_1`    decimal(13, 4)                          DEFAULT NULL,
    `DEC_PROP_2`    decimal(13, 4)                          DEFAULT NULL,
    `BOOL_PROP_1`   varchar(1) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    `BOOL_PROP_2`   varchar(1) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
    CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`
(
    `SCHED_NAME`     varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_NAME`   varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TRIGGER_GROUP`  varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `JOB_NAME`       varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `JOB_GROUP`      varchar(190) COLLATE utf8mb4_unicode_ci NOT NULL,
    `DESCRIPTION`    varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `NEXT_FIRE_TIME` bigint(13)                              DEFAULT NULL,
    `PREV_FIRE_TIME` bigint(13)                              DEFAULT NULL,
    `PRIORITY`       int(11)                                 DEFAULT NULL,
    `TRIGGER_STATE`  varchar(16) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `TRIGGER_TYPE`   varchar(8) COLLATE utf8mb4_unicode_ci   NOT NULL,
    `START_TIME`     bigint(13)                              NOT NULL,
    `END_TIME`       bigint(13)                              DEFAULT NULL,
    `CALENDAR_NAME`  varchar(190) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `MISFIRE_INSTR`  smallint(2)                             DEFAULT NULL,
    `JOB_DATA`       blob,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
    KEY `IDX_QRTZ_T_J` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
    KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`, `JOB_GROUP`),
    KEY `IDX_QRTZ_T_C` (`SCHED_NAME`, `CALENDAR_NAME`),
    KEY `IDX_QRTZ_T_G` (`SCHED_NAME`, `TRIGGER_GROUP`),
    KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`, `TRIGGER_STATE`),
    KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`),
    KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`),
    KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`, `NEXT_FIRE_TIME`),
    KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`),
    KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`),
    KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`),
    KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`,
                                         `TRIGGER_STATE`),
    CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_gateway
-- ----------------------------
DROP TABLE IF EXISTS `sh_gateway`;
CREATE TABLE `sh_gateway`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime                                DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime                                DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    `host`       varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`       varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_gamgqtqrdpr7shnygrlv6id86` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model
-- ----------------------------
DROP TABLE IF EXISTS `sh_model`;
CREATE TABLE `sh_model`
(
    `id`          bigint(20) NOT NULL,
    `code`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`        varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `labels`      json                                    DEFAULT NULL,
    `name`        varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status`      varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `module_id`   bigint(20)                              DEFAULT NULL,
    `service_id`  bigint(20)                              DEFAULT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_4l9uoqoa4vfbwbyrdx8nc50ys` (`code`),
    UNIQUE KEY `UK_MODEL_CODE` (`module_id`, `code`),
    KEY `FK_MODEL_SID` (`service_id`),
    CONSTRAINT `FK_MODEL_MODULE_ID` FOREIGN KEY (`module_id`) REFERENCES `sh_module` (`id`),
    CONSTRAINT `FK_MODEL_SID` FOREIGN KEY (`service_id`) REFERENCES `sh_service` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_delegate
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_delegate`;
CREATE TABLE `sh_model_delegate`
(
    `id`                  bigint(20) NOT NULL,
    `type`                varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `name`                varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description`         varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `delegate_class_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `service_id`          bigint(20)                              DEFAULT NULL,
    `created_at`          datetime                                DEFAULT NULL,
    `created_by`          bigint(20)                              DEFAULT NULL,
    `updated_at`          datetime                                DEFAULT NULL,
    `updated_by`          bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_MODEL_ENDPOINT_DELEGATE_SID` (`service_id`),
    CONSTRAINT `FK_MODEL_ENDPOINT_DELEGATE_SID` FOREIGN KEY (`service_id`) REFERENCES `sh_service` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_endpoint
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_endpoint`;
CREATE TABLE `sh_model_endpoint`
(
    `id`          bigint(20)                              NOT NULL,
    `code`        varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name`        varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`        varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `delegate_id` bigint(20)                              DEFAULT NULL,
    `model_id`    bigint(20)                              NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_MODEL_ENDPOINT_KEY` (`model_id`, `type`, `code`),
    KEY `FK_MODEL_ENDPOINT_DID` (`delegate_id`),
    CONSTRAINT `FK_MODEL_ENDPOINT_DID` FOREIGN KEY (`delegate_id`) REFERENCES `sh_model_delegate` (`id`),
    CONSTRAINT `FK_MODEL_ENDPOINT_MID` FOREIGN KEY (`model_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_endpoint_argument
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_endpoint_argument`;
CREATE TABLE `sh_model_endpoint_argument`
(
    `id`            bigint(20)                              NOT NULL,
    `name`          varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`          varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `is_required`   bit(1)                                  DEFAULT NULL,
    `is_list`       bit(1)                                  DEFAULT NULL,
    `type_id`       bigint(20)                              NOT NULL,
    `endpoint_id`   bigint(20)                              NOT NULL,
    `description`   varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`          int(11)                                 NOT NULL,
    `default_value` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_MODEL_ENDPOINT_ARGUMENT_EID` (`endpoint_id`),
    KEY `FK_MODEL_ENDPOINT_ARGUMENT_TID` (`type_id`),
    CONSTRAINT `FK_MODEL_ENDPOINT_ARGUMENT_EID` FOREIGN KEY (`endpoint_id`) REFERENCES `sh_model_endpoint` (`id`),
    CONSTRAINT `FK_MODEL_ENDPOINT_ARGUMENT_TID` FOREIGN KEY (`type_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_endpoint_return_type
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_endpoint_return_type`;
CREATE TABLE `sh_model_endpoint_return_type`
(
    `endpoint_id` bigint(20) NOT NULL,
    `is_list`     bit(1) DEFAULT NULL,
    `is_required` bit(1) DEFAULT NULL,
    `type_id`     bigint(20) NOT NULL,
    PRIMARY KEY (`endpoint_id`),
    KEY `FK_MODEL_ENDPOINT_RETURN_TYPE_TID` (`type_id`),
    CONSTRAINT `FK_MODEL_ENDPOINT_RETURN_TYPE_TID` FOREIGN KEY (`type_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_feature
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_feature`;
CREATE TABLE `sh_model_feature`
(
    `id`         varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime                               DEFAULT NULL,
    `created_by` bigint(20)                             DEFAULT NULL,
    `updated_at` datetime                               DEFAULT NULL,
    `updated_by` bigint(20)                             DEFAULT NULL,
    `name`       varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_feature_relation
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_feature_relation`;
CREATE TABLE `sh_model_feature_relation`
(
    `feature_id` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
    `model_id`   bigint(20)                             NOT NULL,
    PRIMARY KEY (`model_id`, `feature_id`),
    KEY `FK_MODEL_FEATURE_RELATION_FID` (`feature_id`),
    CONSTRAINT `FK_MODEL_FEATURE_RELATION_FID` FOREIGN KEY (`feature_id`) REFERENCES `sh_model_feature` (`id`),
    CONSTRAINT `FK_MODEL_FEATURE_RELATION_MID` FOREIGN KEY (`model_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_field
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_field`;
CREATE TABLE `sh_model_field`
(
    `id`             bigint(20)                             NOT NULL,
    `code`           varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`           varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `default_value`  varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description`    varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_list`        bit(1)                                  DEFAULT NULL,
    `name`           varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_primary_key` bit(1)                                 NOT NULL,
    `is_required`    bit(1)                                  DEFAULT NULL,
    `sort`           int(11)                                 DEFAULT NULL,
    `is_system`      bit(1)                                  DEFAULT NULL,
    `is_unique`      bit(1)                                  DEFAULT NULL,
    `delegate_id`    bigint(20)                              DEFAULT NULL,
    `model_id`       bigint(20)                             NOT NULL,
    `type_id`        bigint(20)                              DEFAULT NULL,
    `created_at`     datetime                                DEFAULT NULL,
    `created_by`     bigint(20)                              DEFAULT NULL,
    `updated_at`     datetime                                DEFAULT NULL,
    `updated_by`     bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_MODEL_FIELD_CODE` (`model_id`, `code`),
    KEY `FK_MODEL_FIELD_DID` (`delegate_id`),
    KEY `FK_MODEL_FIELD_TID` (`type_id`),
    CONSTRAINT `FK_MODEL_FIELD_DID` FOREIGN KEY (`delegate_id`) REFERENCES `sh_model_delegate` (`id`),
    CONSTRAINT `FK_MODEL_FIELD_MODEL_ID` FOREIGN KEY (`model_id`) REFERENCES `sh_model` (`id`),
    CONSTRAINT `FK_MODEL_FIELD_TID` FOREIGN KEY (`type_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_field_argument
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_field_argument`;
CREATE TABLE `sh_model_field_argument`
(
    `id`            bigint(20)                              NOT NULL,
    `default_value` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description`   varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`          varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `is_required`   bit(1)                                  DEFAULT NULL,
    `field_id`      bigint(20)                              NOT NULL,
    `type_id`       bigint(20)                              NOT NULL,
    `type`          varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_MODEL_FIELD_ARGUMENT_EID` (`field_id`),
    KEY `FK_MODEL_FIELD_ARGUMENT_TID` (`type_id`),
    CONSTRAINT `FK_MODEL_FIELD_ARGUMENT_EID` FOREIGN KEY (`field_id`) REFERENCES `sh_model_field` (`id`),
    CONSTRAINT `FK_MODEL_FIELD_ARGUMENT_TID` FOREIGN KEY (`type_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_field_metadata
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_field_metadata`;
CREATE TABLE `sh_model_field_metadata`
(
    `field_id`             bigint(20) NOT NULL,
    `database_column_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `filters`              json                                    DEFAULT NULL,
    `insertable`           bit(1)                                  DEFAULT NULL,
    `is_unique`            bit(1)                                  DEFAULT NULL,
    `updatable`            bit(1)                                  DEFAULT NULL,
    `sortable`             bit(1)                                  DEFAULT NULL,
    PRIMARY KEY (`field_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_group
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_group`;
CREATE TABLE `sh_model_group`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`        int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_group_item
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_group_item`;
CREATE TABLE `sh_model_group_item`
(
    `id`            bigint(20) NOT NULL,
    `created_at`    datetime                               DEFAULT NULL,
    `created_by`    bigint(20)                             DEFAULT NULL,
    `updated_at`    datetime                               DEFAULT NULL,
    `updated_by`    bigint(20)                             DEFAULT NULL,
    `resource_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `resource_id`   bigint(20)                             DEFAULT NULL,
    `sort`          int(11)                                DEFAULT NULL,
    `group_id`      bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_MODEL_GROUP_ITEM_GID` (`group_id`),
    CONSTRAINT `FK_MODEL_GROUP_ITEM_GID` FOREIGN KEY (`group_id`) REFERENCES `sh_model_group` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_implement
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_implement`;
CREATE TABLE `sh_model_implement`
(
    `model_id`     bigint(20) NOT NULL,
    `interface_id` bigint(20) NOT NULL,
    PRIMARY KEY (`model_id`, `interface_id`),
    KEY `FK_MODEL_IMPLEMENT_IID` (`interface_id`),
    CONSTRAINT `FK_MODEL_IMPLEMENT_IID` FOREIGN KEY (`interface_id`) REFERENCES `sh_model` (`id`),
    CONSTRAINT `FK_MODEL_IMPLEMENT_MID` FOREIGN KEY (`model_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_member_type
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_member_type`;
CREATE TABLE `sh_model_member_type`
(
    `model_id`    bigint(20) NOT NULL,
    `member_type` bigint(20) NOT NULL,
    PRIMARY KEY (`model_id`, `member_type`),
    KEY `FK_MODEL_MEMBER_TYPE_TID` (`member_type`),
    CONSTRAINT `FK_MODEL_MEMBER_TYPE_MID` FOREIGN KEY (`model_id`) REFERENCES `sh_model` (`id`),
    CONSTRAINT `FK_MODEL_MEMBER_TYPE_TID` FOREIGN KEY (`member_type`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_metadata
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_metadata`;
CREATE TABLE `sh_model_metadata`
(
    `model_id`            bigint(20) NOT NULL,
    `database_table_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `hibernate_mapping`   text COLLATE utf8mb4_unicode_ci,
    PRIMARY KEY (`model_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_relation
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_relation`;
CREATE TABLE `sh_model_relation`
(
    `id`         bigint(20) NOT NULL,
    `type`       varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `relation`   varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `model_id`   bigint(20) NOT NULL,
    `inverse`    bigint(20) NOT NULL,
    `created_at` datetime                                DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime                                DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_MODEL_RELATION_INVERSE` (`inverse`),
    KEY `FK_SH_MODEL_FIELD_MODEL_ID` (`model_id`),
    CONSTRAINT `FK_MODEL_RELATION_INVERSE` FOREIGN KEY (`inverse`) REFERENCES `sh_model` (`id`),
    CONSTRAINT `FK_SH_MODEL_FIELD_MODEL_ID` FOREIGN KEY (`model_id`) REFERENCES `sh_model` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_model_view
-- ----------------------------
DROP TABLE IF EXISTS `sh_model_view`;
CREATE TABLE `sh_model_view`
(
    `id`           bigint(20) NOT NULL,
    `name`         varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_default`   bit(1)                                  DEFAULT NULL,
    `component_id` bigint(20) NOT NULL,
    `model_id`     bigint(20) NOT NULL,
    `created_at`   datetime(6)                             DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime(6)                             DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_MODEL_VIEW_NAME` (`name`, `model_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_module
-- ----------------------------
DROP TABLE IF EXISTS `sh_module`;
CREATE TABLE `sh_module`
(
    `id`          bigint(20) NOT NULL,
    `code`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime(6)                             DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `picture`     varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_thxnchsqdx00f5una7esnkyxt` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_service
-- ----------------------------
DROP TABLE IF EXISTS `sh_service`;
CREATE TABLE `sh_service`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `code`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `host`        varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `path`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `port`        int(11)                                 DEFAULT NULL,
    `protocol`    varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_eor86986lhkl1yhimox836v5e` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_service_schema
-- ----------------------------
DROP TABLE IF EXISTS `sh_service_schema`;
CREATE TABLE `sh_service_schema`
(
    `service_id` bigint(20) NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    `schema`     mediumtext COLLATE utf8mb4_unicode_ci,
    PRIMARY KEY (`service_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_service_schema_version
-- ----------------------------
DROP TABLE IF EXISTS `sh_service_schema_version`;
CREATE TABLE `sh_service_schema_version`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime                                DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime                                DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    `schema`     mediumtext COLLATE utf8mb4_unicode_ci,
    `md5`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `name`       varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `schema_id`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_SERVICE_SCHEMA_SID` (`schema_id`),
    CONSTRAINT `FK_SERVICE_SCHEMA_SID` FOREIGN KEY (`schema_id`) REFERENCES `sh_service_schema` (`service_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sh_service_schema_version_patch
-- ----------------------------
DROP TABLE IF EXISTS `sh_service_schema_version_patch`;
CREATE TABLE `sh_service_schema_version_patch`
(
    `id`                bigint(20) NOT NULL,
    `created_at`        datetime                                DEFAULT NULL,
    `created_by`        bigint(20)                              DEFAULT NULL,
    `updated_at`        datetime                                DEFAULT NULL,
    `updated_by`        bigint(20)                              DEFAULT NULL,
    `description`       varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `installed`         bit(1)                                  DEFAULT NULL,
    `name`              varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status`            varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`              varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `schema_version_id` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_SERVICE_SCHEMA_VERSION_PATCH_SID` (`schema_version_id`),
    CONSTRAINT `FK_SERVICE_SCHEMA_VERSION_PATCH_SID` FOREIGN KEY (`schema_version_id`) REFERENCES `sh_service_schema_version` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sms_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sms_captcha`;
CREATE TABLE `sms_captcha`
(
    `id`         varchar(32) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `phone`      varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `retry`      int(11)                                 DEFAULT NULL,
    `session_id` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`      varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `config_id`  varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `created_at` datetime(6)                             DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime(6)                             DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CONFIG_CAPTCHA` (`config_id`),
    CONSTRAINT `FK_CONFIG_CAPTCHA` FOREIGN KEY (`config_id`) REFERENCES `sms_captcha_config` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sms_captcha_config
-- ----------------------------
DROP TABLE IF EXISTS `sms_captcha_config`;
CREATE TABLE `sms_captcha_config`
(
    `id`          varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `retry`       int(11)                                DEFAULT NULL,
    `active`      int(11)                                DEFAULT NULL,
    `expires`     int(11)                                DEFAULT NULL,
    `random_word` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `word_length` int(11)                                DEFAULT NULL,
    `template_id` bigint(20)                             DEFAULT NULL,
    `created_at`  datetime(6)                            DEFAULT NULL,
    `created_by`  bigint(20)                             DEFAULT NULL,
    `updated_at`  datetime(6)                            DEFAULT NULL,
    `updated_by`  bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CAPTCHA_CONFIG_TEMPLATE_ID` (`template_id`),
    CONSTRAINT `FK_CAPTCHA_CONFIG_TEMPLATE_ID` FOREIGN KEY (`template_id`) REFERENCES `sms_template` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sms_message
-- ----------------------------
DROP TABLE IF EXISTS `sms_message`;
CREATE TABLE `sms_message`
(
    `id`          bigint(20) NOT NULL,
    `sign`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `delay`       bigint(20) NOT NULL,
    `status`      varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `content`     varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `notes`       text COLLATE utf8mb4_unicode_ci,
    `config_id`   varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `template_id` bigint(20)                              DEFAULT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime(6)                             DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_SMS_CONFIG` (`config_id`),
    KEY `FK_MESSAGE_TEMPLATE_ID` (`template_id`),
    CONSTRAINT `FK_MESSAGE_TEMPLATE_ID` FOREIGN KEY (`template_id`) REFERENCES `sms_template` (`id`),
    CONSTRAINT `FK_SMS_CONFIG` FOREIGN KEY (`config_id`) REFERENCES `sms_captcha_config` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sms_message_phones
-- ----------------------------
DROP TABLE IF EXISTS `sms_message_phones`;
CREATE TABLE `sms_message_phones`
(
    `msg_id` bigint(20) NOT NULL,
    `phone`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    KEY `FK_SMS_MESSAGE_PHONE` (`msg_id`),
    CONSTRAINT `FK_SMS_MESSAGE_PHONE` FOREIGN KEY (`msg_id`) REFERENCES `sms_message` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sms_template
-- ----------------------------
DROP TABLE IF EXISTS `sms_template`;
CREATE TABLE `sms_template`
(
    `id`         bigint(20)                             NOT NULL,
    `code`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`       varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `sign`       varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `content`    text COLLATE utf8mb4_unicode_ci,
    `created_at` datetime(6)                             DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime(6)                             DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for storage_config
-- ----------------------------
DROP TABLE IF EXISTS `storage_config`;
CREATE TABLE `storage_config`
(
    `id`           varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `description`  varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `config_store` json                                    DEFAULT NULL,
    `name`         varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`         varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `quota`        bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for storage_fileobject
-- ----------------------------
DROP TABLE IF EXISTS `storage_fileobject`;
CREATE TABLE `storage_fileobject`
(
    `id`                  bigint(20)                              NOT NULL,
    `name`                varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `path`                varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
    `extension`           varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `is_directory`        bit(1)                                  NOT NULL,
    `md5`                 varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `limits`              bigint(20)                              DEFAULT NULL,
    `parent_id`           bigint(20)                              DEFAULT NULL,
    `storage_id`          varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `store_path`          varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description`         varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `mime_type`           varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `hidden`              bit(1)                                  NOT NULL,
    `length`              bigint(20)                              NOT NULL,
    `allowed_extensions`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `allows_subdirectory` bit(1)                                  DEFAULT NULL,
    `created_at`          datetime                                DEFAULT NULL,
    `created_by`          bigint(20)                              DEFAULT NULL,
    `updated_at`          datetime                                DEFAULT NULL,
    `updated_by`          bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_STORAGE_FILEOBJECT_PATH` (`path`),
    UNIQUE KEY `UK_STORAGE_FILE_NAME` (`name`, `parent_id`),
    KEY `FK_STORAGE_FILEOBJECT_PARENT` (`parent_id`),
    KEY `FK_STORAGE_FILEOBJECT_STORAGE` (`storage_id`),
    CONSTRAINT `FK_STORAGE_FILEOBJECT_PARENT` FOREIGN KEY (`parent_id`) REFERENCES `storage_fileobject` (`id`),
    CONSTRAINT `FK_STORAGE_FILEOBJECT_STORAGE` FOREIGN KEY (`storage_id`) REFERENCES `storage_config` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for storage_fileobject_label
-- ----------------------------
DROP TABLE IF EXISTS `storage_fileobject_label`;
CREATE TABLE `storage_fileobject_label`
(
    `id`         bigint(20)                              NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    `name`       varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name_space` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `file_id`    bigint(20)                              NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_STORAGE_FILEOBJECT_LABEL_FID` (`file_id`),
    CONSTRAINT `FK_STORAGE_FILEOBJECT_LABEL_FID` FOREIGN KEY (`file_id`) REFERENCES `storage_fileobject` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for storage_multipart_upload
-- ----------------------------
DROP TABLE IF EXISTS `storage_multipart_upload`;
CREATE TABLE `storage_multipart_upload`
(
    `id`             bigint(20)                              NOT NULL,
    `created_at`     datetime   DEFAULT NULL,
    `created_by`     bigint(20) DEFAULT NULL,
    `updated_at`     datetime   DEFAULT NULL,
    `updated_by`     bigint(20) DEFAULT NULL,
    `chunk_length`   int(11)    DEFAULT NULL,
    `chunk_size`     bigint(20) DEFAULT NULL,
    `hash`           varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `mime_type`      varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `path`           varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
    `size`           bigint(20)                              NOT NULL,
    `space_id`       varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `storage_id`     varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `upload_id`      varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `uploaded_parts` int(11)    DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_MULTIPART_UPLOAD` (`storage_id`, `path`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for storage_multipart_upload_chunk
-- ----------------------------
DROP TABLE IF EXISTS `storage_multipart_upload_chunk`;
CREATE TABLE `storage_multipart_upload_chunk`
(
    `id`         bigint(20)                              NOT NULL,
    `created_at` datetime                                DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime                                DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    `etag`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `hash`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `paer_index` int(11)                                 DEFAULT NULL,
    `path`       varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
    `length`     bigint(20)                              DEFAULT NULL,
    `upload_id`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_MULTIPART_UPLOAD_CHUNK_HASH` (`upload_id`, `hash`),
    UNIQUE KEY `UK_MULTIPART_UPLOAD_CHUNK_INDEX` (`upload_id`, `paer_index`),
    CONSTRAINT `FK_STORAGE_MULTIPART_UPLOAD_CHUNK_UID` FOREIGN KEY (`upload_id`) REFERENCES `storage_multipart_upload` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for storage_space
-- ----------------------------
DROP TABLE IF EXISTS `storage_space`;
CREATE TABLE `storage_space`
(
    `id`         varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime                                DEFAULT NULL,
    `created_by` bigint(20)                              DEFAULT NULL,
    `updated_at` datetime                                DEFAULT NULL,
    `updated_by` bigint(20)                              DEFAULT NULL,
    `name`       varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `plugins`    varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `folder_id`  bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_STORAGE_SPACE_FOLDER_ID` (`folder_id`),
    CONSTRAINT `FK_STORAGE_SPACE_FOLDER_ID` FOREIGN KEY (`folder_id`) REFERENCES `storage_fileobject` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for storage_thumbnail
-- ----------------------------
DROP TABLE IF EXISTS `storage_thumbnail`;
CREATE TABLE `storage_thumbnail`
(
    `id`         bigint(20)                              NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    `size`       varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `file_id`    bigint(20) DEFAULT NULL,
    `source_id`  bigint(20)                              NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_STORAGE_THUMBNAIL_SIZE` (`source_id`, `size`),
    KEY `FK_STORAGE_THUMBNAIL_FILE_ID` (`file_id`),
    CONSTRAINT `FK_STORAGE_THUMBNAIL_FILE_ID` FOREIGN KEY (`file_id`) REFERENCES `storage_fileobject` (`id`),
    CONSTRAINT `FK_STORAGE_THUMBNAIL_SOURCE_ID` FOREIGN KEY (`source_id`) REFERENCES `storage_fileobject` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_calendar
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_calendar`;
CREATE TABLE `sunrise_calendar`
(
    `id`               bigint(20)                             NOT NULL,
    `created_at`       datetime                                DEFAULT NULL,
    `created_by`       bigint(20)                              DEFAULT NULL,
    `updated_at`       datetime                                DEFAULT NULL,
    `updated_by`       bigint(20)                              DEFAULT NULL,
    `color`            varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description`      varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`             varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `refresh`          varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`             varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `url`              varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `version`          bigint(20)                              DEFAULT NULL,
    `calendar_account` bigint(20)                              DEFAULT NULL,
    `sort`             int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CALENDAR_CALENDAR_ACCOUNT_ID` (`calendar_account`),
    CONSTRAINT `FK_CALENDAR_CALENDAR_ACCOUNT_ID` FOREIGN KEY (`calendar_account`) REFERENCES `sunrise_calendar_account` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_calendar_account
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_calendar_account`;
CREATE TABLE `sunrise_calendar_account`
(
    `id`          bigint(20)                             NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `enabled`     bit(1)                                  DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `provider`    varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`        varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `owner_id`    bigint(20)                              DEFAULT NULL,
    `description` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`        int(11)                                 DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CALENDAR_ACCOUNT_OWNER` (`owner_id`),
    CONSTRAINT `FK_CALENDAR_ACCOUNT_OWNER` FOREIGN KEY (`owner_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_calendar_event
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_calendar_event`;
CREATE TABLE `sunrise_calendar_event`
(
    `id`                 bigint(20) NOT NULL,
    `created_at`         datetime                                DEFAULT NULL,
    `created_by`         bigint(20)                              DEFAULT NULL,
    `updated_at`         datetime                                DEFAULT NULL,
    `updated_by`         bigint(20)                              DEFAULT NULL,
    `all_day`            bit(1)                                  DEFAULT NULL,
    `ends`               datetime                                DEFAULT NULL,
    `starts`             datetime                                DEFAULT NULL,
    `location`           varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `notes`              text COLLATE utf8mb4_unicode_ci,
    `alert`              varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `alert_on_date`      datetime                                DEFAULT NULL,
    `alert_times`        int(11)                                 DEFAULT NULL,
    `repeat_end_on_date` datetime                                DEFAULT NULL,
    `repeat`             varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `repeat_end`         int(11)                                 DEFAULT NULL,
    `repeat_end_times`   int(11)                                 DEFAULT NULL,
    `title`              varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `url`                varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `calendar_id`        bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKla5psghdt7akive9ct3wn8pe9` (`calendar_id`),
    CONSTRAINT `FKla5psghdt7akive9ct3wn8pe9` FOREIGN KEY (`calendar_id`) REFERENCES `sunrise_calendar` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_calendar_event_date
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_calendar_event_date`;
CREATE TABLE `sunrise_calendar_event_date`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime   DEFAULT NULL,
    `created_by` bigint(20) DEFAULT NULL,
    `updated_at` datetime   DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    `date`       date       DEFAULT NULL,
    `event_id`   bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK61pr68nhf7y1u25c0lg0dpgr8` (`event_id`),
    CONSTRAINT `FK61pr68nhf7y1u25c0lg0dpgr8` FOREIGN KEY (`event_id`) REFERENCES `sunrise_calendar_event` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_calendar_preference
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_calendar_preference`;
CREATE TABLE `sunrise_calendar_preference`
(
    `id`               bigint(20) NOT NULL,
    `created_at`       datetime   DEFAULT NULL,
    `created_by`       bigint(20) DEFAULT NULL,
    `updated_at`       datetime   DEFAULT NULL,
    `updated_by`       bigint(20) DEFAULT NULL,
    `default_calendar` bigint(20) DEFAULT NULL,
    `owner_id`         bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CALENDAR_PREFERENCE_DEFAULT_CALENDAR` (`default_calendar`),
    KEY `FK_CALENDAR_PREFERENCE_OWNER_ID` (`owner_id`),
    CONSTRAINT `FK_CALENDAR_PREFERENCE_DEFAULT_CALENDAR` FOREIGN KEY (`default_calendar`) REFERENCES `sunrise_calendar` (`id`),
    CONSTRAINT `FK_CALENDAR_PREFERENCE_OWNER_ID` FOREIGN KEY (`owner_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_calendar_set
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_calendar_set`;
CREATE TABLE `sunrise_calendar_set`
(
    `id`               bigint(20) NOT NULL,
    `created_at`       datetime                               DEFAULT NULL,
    `created_by`       bigint(20)                             DEFAULT NULL,
    `updated_at`       datetime                               DEFAULT NULL,
    `updated_by`       bigint(20)                             DEFAULT NULL,
    `name`             varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `owner_id`         bigint(20)                             DEFAULT NULL,
    `sort`             int(11)                                DEFAULT NULL,
    `default_calendar` bigint(20)                             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CALENDAR_SET_OWNER_ID` (`owner_id`),
    KEY `FK_CALENDAR_SET_DEFAULT_CALENDAR` (`default_calendar`),
    CONSTRAINT `FK_CALENDAR_SET_DEFAULT_CALENDAR` FOREIGN KEY (`default_calendar`) REFERENCES `sunrise_calendar` (`id`),
    CONSTRAINT `FK_CALENDAR_SET_OWNER_ID` FOREIGN KEY (`owner_id`) REFERENCES `auth_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_calendar_set_items
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_calendar_set_items`;
CREATE TABLE `sunrise_calendar_set_items`
(
    `set_id`      bigint(20) NOT NULL,
    `calendar_id` bigint(20) NOT NULL,
    KEY `FK_SUNRISE_CALENDAR_SET_CID` (`calendar_id`),
    KEY `FK_SUNRISE_CALENDAR_SET_SID` (`set_id`),
    CONSTRAINT `FK_SUNRISE_CALENDAR_SET_CID` FOREIGN KEY (`calendar_id`) REFERENCES `sunrise_calendar` (`id`),
    CONSTRAINT `FK_SUNRISE_CALENDAR_SET_SID` FOREIGN KEY (`set_id`) REFERENCES `sunrise_calendar_set` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_todo
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_todo`;
CREATE TABLE `sunrise_todo`
(
    `id`           bigint(20)                             NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `deadline`     datetime                                DEFAULT NULL,
    `due_date`     datetime                                DEFAULT NULL,
    `location`     varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `notes`        text COLLATE utf8mb4_unicode_ci,
    `status`       varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
    `title`        varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `url`          varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `when_on_date` datetime                                DEFAULT NULL,
    `when`         varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_todo_tags
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_todo_tags`;
CREATE TABLE `sunrise_todo_tags`
(
    `todo_id` bigint(20) NOT NULL,
    `tag`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    KEY `FK_SUNRISE_TODO_TAGS_TAG` (`todo_id`),
    CONSTRAINT `FK_SUNRISE_TODO_TAGS_TAG` FOREIGN KEY (`todo_id`) REFERENCES `sunrise_todo` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sunrise_todolist
-- ----------------------------
DROP TABLE IF EXISTS `sunrise_todolist`;
CREATE TABLE `sunrise_todolist`
(
    `id`          bigint(20) NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`
(
    `code`        varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `type`        varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`        int(11)                                 NOT NULL,
    `level`       int(11)                                 NOT NULL,
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `path`        varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `pcode`       varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `ptype`       varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`code`, `type`),
    KEY `FK_DICT_TYPE` (`type`),
    KEY `FK_DICT_PARENT` (`pcode`, `ptype`),
    CONSTRAINT `FK_DICT_PARENT` FOREIGN KEY (`pcode`, `ptype`) REFERENCES `sys_dict` (`code`, `type`),
    CONSTRAINT `FK_DICT_TYPE` FOREIGN KEY (`type`) REFERENCES `sys_dict_type` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `code`        varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `created_at`  datetime                                 DEFAULT NULL,
    `created_by`  bigint(20)                               DEFAULT NULL,
    `updated_at`  datetime                                 DEFAULT NULL,
    `updated_by`  bigint(20)                               DEFAULT NULL,
    `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sort`        int(11)                                 NOT NULL,
    `level`       int(11)                                 NOT NULL,
    `name`        varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `path`        varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `pcode`       varchar(20) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    PRIMARY KEY (`code`),
    KEY `FK_DICT_TYPE_PCODE` (`pcode`),
    CONSTRAINT `FK_DICT_TYPE_PCODE` FOREIGN KEY (`pcode`) REFERENCES `sys_dict_type` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sys_language
-- ----------------------------
DROP TABLE IF EXISTS `sys_language`;
CREATE TABLE `sys_language`
(
    `id`              bigint(20) NOT NULL,
    `message_content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `message_key`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `locale`          varchar(2) COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sys_oplog
-- ----------------------------
DROP TABLE IF EXISTS `sys_oplog`;
CREATE TABLE `sys_oplog`
(
    `id`                bigint(20)                             NOT NULL,
    `created_at`        datetime                                DEFAULT NULL,
    `created_by`        bigint(20)                              DEFAULT NULL,
    `updated_at`        datetime                                DEFAULT NULL,
    `updated_by`        bigint(20)                              DEFAULT NULL,
    `clazz`             varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `data`              text COLLATE utf8mb4_unicode_ci,
    `entity_name`       varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `operation`         varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `primary_key_name`  varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `primary_key_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `table_name`        varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sys_oplog_owner
-- ----------------------------
DROP TABLE IF EXISTS `sys_oplog_owner`;
CREATE TABLE `sys_oplog_owner`
(
    `log_id` bigint(20) NOT NULL,
    `owner`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    KEY `FK_OPLOG_SCOPE` (`log_id`),
    CONSTRAINT `FK_OPLOG_SCOPE` FOREIGN KEY (`log_id`) REFERENCES `sys_oplog` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sys_sequence
-- ----------------------------
DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sys_sequence`
(
    `gen_name`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `gen_value` bigint(20)                              NOT NULL,
    PRIMARY KEY (`gen_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ui_icon
-- ----------------------------
DROP TABLE IF EXISTS `ui_icon`;
CREATE TABLE `ui_icon`
(
    `id`          bigint(20)                             NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `content`     text COLLATE utf8mb4_unicode_ci,
    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `metadata`    text COLLATE utf8mb4_unicode_ci,
    `name`        varchar(60) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `type`        varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `unicode`     varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ui_library
-- ----------------------------
DROP TABLE IF EXISTS `ui_library`;
CREATE TABLE `ui_library`
(
    `id`             bigint(20)                             NOT NULL,
    `name`           varchar(60) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `description`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ownership_type` varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `ownership_id`   bigint(20)                              DEFAULT NULL,
    `type`           varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`     datetime                                DEFAULT NULL,
    `created_by`     bigint(20)                              DEFAULT NULL,
    `updated_at`     datetime                                DEFAULT NULL,
    `updated_by`     bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_LIBRARY_NAME` (`type`, `name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ui_library_item
-- ----------------------------
DROP TABLE IF EXISTS `ui_library_item`;
CREATE TABLE `ui_library_item`
(
    `id`            bigint(20) NOT NULL,
    `created_at`    datetime                               DEFAULT NULL,
    `created_by`    bigint(20)                             DEFAULT NULL,
    `updated_at`    datetime                               DEFAULT NULL,
    `updated_by`    bigint(20)                             DEFAULT NULL,
    `resource_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `resource_id`   bigint(20)                             DEFAULT NULL,
    `library_id`    bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_LIBRARY_ITEM_LID` (`library_id`),
    CONSTRAINT `FK_LIBRARY_ITEM_LID` FOREIGN KEY (`library_id`) REFERENCES `ui_library` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for ui_library_item_tags
-- ----------------------------
DROP TABLE IF EXISTS `ui_library_item_tags`;
CREATE TABLE `ui_library_item_tags`
(
    `item_id` bigint(20) NOT NULL,
    `tag`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    KEY `FK_LIBRARY_ITEM_TAG` (`item_id`),
    CONSTRAINT `FK_LIBRARY_ITEM_TAG` FOREIGN KEY (`item_id`) REFERENCES `ui_library_item` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for website
-- ----------------------------
DROP TABLE IF EXISTS `website`;
CREATE TABLE `website`
(
    `id`                 bigint(20)                              NOT NULL,
    `created_at`         datetime(6)                             DEFAULT NULL,
    `created_by`         bigint(20)                              DEFAULT NULL,
    `updated_at`         datetime(6)                             DEFAULT NULL,
    `updated_by`         bigint(20)                              DEFAULT NULL,
    `description`        varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `logo`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`               varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `article_channel_id` bigint(20)                              NOT NULL,
    `organization_id`    bigint(20)                              NOT NULL,
    `application_id`     bigint(20)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_WEBSITE_ARTICLE_CHANNEL_ID` (`article_channel_id`),
    KEY `FK_WEBSITE_ORGANIZATION_ID` (`organization_id`),
    KEY `FK_WEBSITE_APPLICATION_ID` (`application_id`),
    CONSTRAINT `FK_WEBSITE_APPLICATION_ID` FOREIGN KEY (`application_id`) REFERENCES `nuwa_application` (`id`),
    CONSTRAINT `FK_WEBSITE_ARTICLE_CHANNEL_ID` FOREIGN KEY (`article_channel_id`) REFERENCES `cms_article_category` (`id`),
    CONSTRAINT `FK_WEBSITE_ORGANIZATION_ID` FOREIGN KEY (`organization_id`) REFERENCES `org_organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for website_landing_page
-- ----------------------------
DROP TABLE IF EXISTS `website_landing_page`;
CREATE TABLE `website_landing_page`
(
    `id`               bigint(20)                              NOT NULL,
    `created_at`       datetime                                DEFAULT NULL,
    `created_by`       bigint(20)                              DEFAULT NULL,
    `updated_at`       datetime                                DEFAULT NULL,
    `updated_by`       bigint(20)                              DEFAULT NULL,
    `description`      varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `ends`             datetime                                DEFAULT NULL,
    `name`             varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `starts`           datetime                                DEFAULT NULL,
    `status`           varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `poster_id`        bigint(20)                              DEFAULT NULL,
    `meta_description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `meta_thumb`       varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `meta_title`       varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_WEBSITE_LANDING_PAGE_POSTER` (`poster_id`),
    CONSTRAINT `FK_WEBSITE_LANDING_PAGE_POSTER` FOREIGN KEY (`poster_id`) REFERENCES `website_landing_poster` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for website_landing_page_store
-- ----------------------------
DROP TABLE IF EXISTS `website_landing_page_store`;
CREATE TABLE `website_landing_page_store`
(
    `page_id`  bigint(20) NOT NULL,
    `store_id` bigint(20) NOT NULL,
    KEY `FKaf605gegep0ru6nur631lfglq` (`store_id`),
    KEY `FK_WEBSITE_LANDING_PAGE_STORE_SID` (`page_id`),
    CONSTRAINT `FK_WEBSITE_LANDING_PAGE_STORE_SID` FOREIGN KEY (`page_id`) REFERENCES `website_landing_page` (`id`),
    CONSTRAINT `FKaf605gegep0ru6nur631lfglq` FOREIGN KEY (`store_id`) REFERENCES `website_landing_store` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for website_landing_poster
-- ----------------------------
DROP TABLE IF EXISTS `website_landing_poster`;
CREATE TABLE `website_landing_poster`
(
    `id`          bigint(20)                              NOT NULL,
    `created_at`  datetime                                DEFAULT NULL,
    `created_by`  bigint(20)                              DEFAULT NULL,
    `updated_at`  datetime                                DEFAULT NULL,
    `updated_by`  bigint(20)                              DEFAULT NULL,
    `content`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `background`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `music`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for website_landing_store
-- ----------------------------
DROP TABLE IF EXISTS `website_landing_store`;
CREATE TABLE `website_landing_store`
(
    `id`               bigint(20)                              NOT NULL,
    `created_at`       datetime                                DEFAULT NULL,
    `created_by`       bigint(20)                              DEFAULT NULL,
    `updated_at`       datetime                                DEFAULT NULL,
    `updated_by`       bigint(20)                              DEFAULT NULL,
    `leader`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `city`             varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `country`          varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `district`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `postal_code`      varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `province`         varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `street`           varchar(30) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `name`             varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `qr_code`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description`      varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `detailed_address` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `code`             varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `geo_latitude`     decimal(11, 6)                          DEFAULT NULL,
    `geo_longitude`    decimal(11, 6)                          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for weixin_app
-- ----------------------------
DROP TABLE IF EXISTS `weixin_app`;
CREATE TABLE `weixin_app`
(
    `id`           varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`   datetime                                DEFAULT NULL,
    `created_by`   bigint(20)                              DEFAULT NULL,
    `updated_at`   datetime                                DEFAULT NULL,
    `updated_by`   bigint(20)                              DEFAULT NULL,
    `aes_key`      varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `agent_id`     int(11)                                 DEFAULT NULL,
    `name`         varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `primitive_id` varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `app_secret`   varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `token_name`   varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`         varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for weixin_user
-- ----------------------------
DROP TABLE IF EXISTS `weixin_user`;
CREATE TABLE `weixin_user`
(
    `appid`             varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `openid`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at`        datetime                                DEFAULT NULL,
    `created_by`        bigint(20)                              DEFAULT NULL,
    `updated_at`        datetime                                DEFAULT NULL,
    `updated_by`        bigint(20)                              DEFAULT NULL,
    `avatar`            varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `city`              varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `country`           varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `language`          varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `last_look_time`    bigint(20)                              DEFAULT NULL,
    `last_message_time` bigint(20)                              DEFAULT NULL,
    `nickname`          varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `province`          varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `sex`               varchar(10) COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `subscribe`         bit(1)                                  DEFAULT NULL,
    `subscribe_time`    bigint(20)                              DEFAULT NULL,
    `union_id`          varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`appid`, `openid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
