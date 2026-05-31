-- ========================================
-- 投资系统运维平台 — 数据库初始化脚本
-- 分支: codex/devops-p1-linkage-hardening
-- 数据库: devops | MySQL 5.7+ | 字符集: utf8mb4
-- 用法: mysql -uroot -p < init_db.sql
--       或者: mysql -uroot -p -e "source init_db.sql" devops
-- ========================================

-- =====================
-- 创建数据库
-- =====================
CREATE DATABASE IF NOT EXISTS `devops` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE `devops`;

-- ============================================
-- 模块一：组织人员
-- ============================================

-- 1. 运维团队成员
DROP TABLE IF EXISTS `ops_team_member`;
CREATE TABLE `ops_team_member` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint(20) DEFAULT NULL COMMENT '关联若依 sys_user.id',
    `real_name` varchar(50) NOT NULL COMMENT '姓名',
    `employee_no` varchar(50) DEFAULT NULL COMMENT '工号',
    `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `team` varchar(50) DEFAULT NULL COMMENT '所属小组',
    `role_type` char(1) DEFAULT '3' COMMENT '角色(1运维负责人 2值班长 3运维工程师 4巡检员)',
    `skill_tags` varchar(500) DEFAULT NULL COMMENT '技能标签',
    `is_on_job` char(1) DEFAULT '1' COMMENT '在岗状态(1在岗 2休假 3离职)',
    `entry_date` date DEFAULT NULL COMMENT '入职日期',
    `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_employee_no` (`employee_no`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_team` (`team`),
    KEY `idx_role_type` (`role_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运维团队成员表';

-- ============================================
-- 模块二：基础资源
-- ============================================

-- 2. 系统资产台账
DROP TABLE IF EXISTS `ops_system_asset`;
CREATE TABLE `ops_system_asset` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_code` varchar(50) DEFAULT NULL COMMENT '系统编码',
    `system_name` varchar(100) NOT NULL COMMENT '系统名称',
    `system_type` char(1) DEFAULT '1' COMMENT '类型(1核心业务 2支撑系统 3基础设施)',
    `importance_level` char(1) DEFAULT '2' COMMENT '重要等级(1核心 2重要 3一般)',
    `system_url` varchar(200) DEFAULT NULL COMMENT '访问地址',
    `server_ip` varchar(200) DEFAULT NULL COMMENT '部署IP',
    `db_type` varchar(50) DEFAULT NULL COMMENT '数据库类型',
    `dev_lang` varchar(100) DEFAULT NULL COMMENT '开发语言/框架',
    `department` varchar(100) DEFAULT NULL COMMENT '所属部门',
    `business_owner` varchar(50) DEFAULT NULL COMMENT '业务负责人',
    `tech_owner_id` bigint(20) DEFAULT NULL COMMENT '技术负责人ID',
    `go_live_date` date DEFAULT NULL COMMENT '上线日期',
    `system_status` char(1) DEFAULT '1' COMMENT '状态(1运行 2停用 3下线)',
    `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_system_code` (`system_code`),
    KEY `idx_system_type` (`system_type`),
    KEY `idx_system_status` (`system_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统资产台账';

-- 3. 供应商/联系人
DROP TABLE IF EXISTS `ops_vendor_contact`;
CREATE TABLE `ops_vendor_contact` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `vendor_name` varchar(200) NOT NULL COMMENT '供应商名称',
    `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` varchar(30) DEFAULT NULL COMMENT '联系电话',
    `contact_email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `service_scope` char(1) DEFAULT '2' COMMENT '服务范围(1开发 2运维 3硬件 4综合)',
    `contract_no` varchar(100) DEFAULT NULL COMMENT '合同编号',
    `contract_start` date DEFAULT NULL COMMENT '合同开始日期',
    `contract_end` date DEFAULT NULL COMMENT '合同结束日期',
    `sla_level` varchar(20) DEFAULT NULL COMMENT 'SLA等级',
    `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_contract_end` (`contract_end`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商/联系人表';

-- ============================================
-- 模块三：值班管理
-- ============================================

-- 4. 值班排班（含代码生成兼容字段）
DROP TABLE IF EXISTS `ops_duty_shift`;
CREATE TABLE `ops_duty_shift` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `shift_name` varchar(100) DEFAULT NULL COMMENT '班次名称',
    `shift_date` date DEFAULT NULL COMMENT '班次日期',
    `duty_date` date DEFAULT NULL COMMENT '值班日期',
    `shift_type` char(1) DEFAULT '1' COMMENT '班次(1白班 2夜班 3全天)',
    `leader_id` bigint(20) DEFAULT NULL COMMENT '带班领导ID',
    `leader_name` varchar(50) DEFAULT NULL COMMENT '带班领导姓名',
    `duty_member_id` bigint(20) DEFAULT NULL COMMENT '值班人ID',
    `member_ids` varchar(500) DEFAULT NULL COMMENT '值班成员ID逗号分隔',
    `backup_member_id` bigint(20) DEFAULT NULL COMMENT '备勤人ID',
    `duty_phone` varchar(20) DEFAULT NULL COMMENT '值班电话',
    `handover_notes` varchar(500) DEFAULT NULL COMMENT '交接备注',
    `shift_status` char(1) DEFAULT '1' COMMENT '班次状态(1进行中 2已交接 3已结束)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_date_shift` (`duty_date`, `shift_type`),
    KEY `idx_duty_member` (`duty_member_id`),
    KEY `idx_duty_date` (`duty_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='值班排班表';

-- 5. 交接班/值班日志（含代码生成兼容字段 + issue_id）
DROP TABLE IF EXISTS `ops_shift_log`;
CREATE TABLE `ops_shift_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID',
    `shift_id` bigint(20) DEFAULT NULL COMMENT '关联值班排班ID',
    `log_time` datetime DEFAULT NULL COMMENT '日志时间',
    `log_type` char(1) DEFAULT '1' COMMENT '日志类型(1日常 2事件 3告警 4交接)',
    `log_content` text COMMENT '日志内容',
    `severity` char(1) DEFAULT '1' COMMENT '严重程度(1普通 2重要 3紧急)',
    `handover_from_id` bigint(20) DEFAULT NULL COMMENT '交班人ID',
    `handover_to_id` bigint(20) DEFAULT NULL COMMENT '接班人ID',
    `handover_time` datetime DEFAULT NULL COMMENT '交接时间',
    `running_status` text COMMENT '当前系统运行概况',
    `pending_items` text COMMENT '待处理事项',
    `important_notice` text COMMENT '重要通知/注意事项',
    `asset_status` varchar(500) DEFAULT NULL COMMENT '资产/设备异常情况',
    `handover_confirm` char(1) DEFAULT '0' COMMENT '接班人确认(0未确认 1已确认)',
    `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
    `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
    `handling_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
    `is_escalated` char(1) DEFAULT '0' COMMENT '是否升级(0否 1是)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_shift_id` (`shift_id`),
    KEY `idx_handover_time` (`handover_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='值班日志表';

-- ============================================
-- 模块四：周报管理
-- ============================================

-- 6. 周报主表（含代码生成兼容字段）
DROP TABLE IF EXISTS `weekly_operation_task`;
CREATE TABLE `weekly_operation_task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '周报ID',
    `task_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
    `report_title` varchar(200) DEFAULT NULL COMMENT '周报标题',
    `report_week` varchar(10) DEFAULT NULL COMMENT '周次',
    `report_date` date DEFAULT NULL COMMENT '周报日期',
    `report_type` char(1) DEFAULT '1' COMMENT '类型(1周报 2月报 3季报)',
    `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
    `requester` varchar(100) DEFAULT NULL COMMENT '需求方',
    `planned_date` date DEFAULT NULL COMMENT '计划日期',
    `task_status` varchar(50) DEFAULT NULL COMMENT '任务状态',
    `report_status` char(1) DEFAULT '0' COMMENT '状态(0草稿 1已提交 2已审核)',
    `requirement_desc` text COMMENT '需求描述',
    `this_week_progress` text COMMENT '本周进展',
    `next_week_plan` text COMMENT '下周计划',
    `issue_risk` text COMMENT '问题风险',
    `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_report_week` (`report_week`),
    KEY `idx_report_date` (`report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周报主表';

-- 7. 重点运维工作明细（含代码生成兼容字段）
DROP TABLE IF EXISTS `weekly_key_operation_detail`;
CREATE TABLE `weekly_key_operation_detail` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_id` bigint(20) DEFAULT NULL COMMENT '关联周运维任务ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `change_id` bigint(20) DEFAULT NULL COMMENT '关联变更记录ID',
    `operation_type` char(1) DEFAULT NULL COMMENT '运维类型',
    `operation_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称',
    `task_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
    `executor_id` bigint(20) DEFAULT NULL COMMENT '执行人ID',
    `operation_content` text COMMENT '运维内容',
    `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
    `demander` varchar(100) DEFAULT NULL COMMENT '需求方',
    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
    `propose_date` date DEFAULT NULL COMMENT '提出时间',
    `require_end_date` date DEFAULT NULL COMMENT '要求完成时间',
    `actual_end_date` date DEFAULT NULL COMMENT '实际完成时间',
    `task_status` varchar(50) DEFAULT NULL COMMENT '事项状态',
    `requirement_desc` text COMMENT '需求描述',
    `weekly_progress` text COMMENT '本周推进',
    `issues_risks` text COMMENT '问题及风险',
    `operation_result` varchar(500) DEFAULT NULL COMMENT '执行结果',
    `affected_systems` varchar(500) DEFAULT NULL COMMENT '影响系统',
    `verify_method` varchar(500) DEFAULT NULL COMMENT '验证方式',
    `verify_result` varchar(500) DEFAULT NULL COMMENT '验证结果',
    `issues_found` varchar(500) DEFAULT NULL COMMENT '发现问题',
    `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_report_id` (`report_id`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_executor_id` (`executor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重点运维工作明细表';

-- ============================================
-- 模块五：巡检记录（含代码生成兼容字段 + issue_id）
-- ============================================

-- 8. 巡检记录
DROP TABLE IF EXISTS `daily_inspection_main`;
CREATE TABLE `daily_inspection_main` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `inspection_date` date NOT NULL COMMENT '巡检日期',
    `inspection_time` time NOT NULL COMMENT '巡检时间',
    `inspector_id` bigint(20) DEFAULT NULL COMMENT '巡检人员ID',
    `inspector_name` varchar(50) DEFAULT NULL COMMENT '巡检人员姓名',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统/模块名称',
    `server_ip` varchar(50) DEFAULT NULL COMMENT '服务器IP',
    `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
    `system_availability` char(1) DEFAULT '1' COMMENT '系统可用性(0异常 1正常)',
    `response_time` int(11) DEFAULT NULL COMMENT '响应时间(ms)',
    `connectivity_status` char(1) DEFAULT '1' COMMENT '连通性状态(0异常 1正常)',
    `service_process_name` varchar(100) DEFAULT NULL COMMENT '服务/进程名称',
    `process_status` char(1) DEFAULT '1' COMMENT '进程状态(0停止 1运行)',
    `port_status` char(1) DEFAULT '1' COMMENT '端口状态(0关闭 1开放)',
    `cpu_usage` decimal(5,2) DEFAULT NULL COMMENT 'CPU使用率(%)',
    `memory_usage` decimal(5,2) DEFAULT NULL COMMENT '内存使用率(%)',
    `disk_usage` decimal(5,2) DEFAULT NULL COMMENT '磁盘使用率(%)',
    `disk_io_status` varchar(50) DEFAULT NULL COMMENT '磁盘IO情况',
    `network_status` char(1) DEFAULT '1' COMMENT '网络状态(0异常 1正常)',
    `network_traffic` int(11) DEFAULT NULL COMMENT '网络流量(Mbps)',
    `alarm_count` int(11) DEFAULT '0' COMMENT '告警数量',
    `alarm_info` varchar(500) DEFAULT NULL COMMENT '异常告警',
    `action_taken` varchar(500) DEFAULT NULL COMMENT '处理措施',
    `inspection_result` char(1) DEFAULT '1' COMMENT '巡检结论(0异常 1正常)',
    `handle_measures` varchar(500) DEFAULT NULL COMMENT '处理措施',
    `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
    `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
    `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
    `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_date_system` (`inspection_date`, `system_name`),
    KEY `idx_inspector_id` (`inspector_id`),
    KEY `idx_report_id` (`report_id`),
    KEY `idx_system_id` (`system_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日常巡检记录表';

-- ============================================
-- 模块六：故障/备份/变更
-- ============================================

-- 9. 故障记录（含代码生成兼容字段 + issue_id）
DROP TABLE IF EXISTS `ops_fault_record`;
CREATE TABLE `ops_fault_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称',
    `fault_level` char(1) DEFAULT '2' COMMENT '故障等级(1一般 2严重 3紧急)',
    `fault_title` varchar(200) NOT NULL COMMENT '故障标题',
    `fault_desc` text COMMENT '故障现象',
    `root_cause` text COMMENT '根本原因',
    `solution` text COMMENT '解决方案',
    `preventive_measures` text COMMENT '预防措施',
    `affected_scope` varchar(500) DEFAULT NULL COMMENT '影响范围',
    `occur_time` datetime NOT NULL COMMENT '发生时间',
    `discoverer_id` bigint(20) DEFAULT NULL COMMENT '发现人ID',
    `discoverer_name` varchar(50) DEFAULT NULL COMMENT '发现人姓名',
    `recover_time` datetime DEFAULT NULL COMMENT '恢复时间',
    `downtime_minutes` int(11) DEFAULT '0' COMMENT '宕机时长(分钟)',
    `duration_minutes` int(11) DEFAULT '0' COMMENT '持续时长(分钟)',
    `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
    `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
    `fault_status` char(1) DEFAULT '1' COMMENT '状态(1处理中 2已恢复 3已关闭)',
    `is_emergency` char(1) DEFAULT '0' COMMENT '是否紧急变更(0否 1是)',
    `knowledge_id` bigint(20) DEFAULT NULL COMMENT '关联知识库ID',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_occur_time` (`occur_time`),
    KEY `idx_fault_status` (`fault_status`),
    KEY `idx_fault_level` (`fault_level`),
    KEY `idx_handler_id` (`handler_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='故障记录表';

-- 10. 备份记录（含代码生成兼容字段 + issue_id）
DROP TABLE IF EXISTS `ops_backup_record`;
CREATE TABLE `ops_backup_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `backup_type` char(1) DEFAULT '1' COMMENT '备份类型(1数据库 2文件 3配置 4全量)',
    `backup_time` datetime NOT NULL COMMENT '备份时间',
    `backup_size_mb` int(11) DEFAULT NULL COMMENT '备份大小(MB)',
    `backup_result` char(1) DEFAULT '1' COMMENT '备份结果(1成功 2失败)',
    `backup_path` varchar(500) DEFAULT NULL COMMENT '备份路径',
    `restore_tested` char(1) DEFAULT '0' COMMENT '恢复验证(0未验证 1已验证)',
    `executor_id` bigint(20) DEFAULT NULL COMMENT '执行人ID',
    `error_msg` text COMMENT '失败原因',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_backup_time` (`backup_time`),
    KEY `idx_backup_result` (`backup_result`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='备份记录表';

-- 11. 变更记录（含P1闭环扩展字段）
DROP TABLE IF EXISTS `ops_change_record`;
CREATE TABLE `ops_change_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `change_no` varchar(64) DEFAULT NULL COMMENT '变更编号',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称',
    `change_type` char(1) DEFAULT '1' COMMENT '变更类型(1版本升级 2配置变更 3数据库变更 4网络变更 5其他)',
    `change_title` varchar(200) NOT NULL COMMENT '变更标题',
    `change_desc` text COMMENT '变更描述',
    `risk_level` char(1) DEFAULT '2' COMMENT '风险等级(1低 2中 3高)',
    `change_content` text COMMENT '变更内容详情',
    `rollback_plan` text COMMENT '回滚方案',
    `change_time` datetime NOT NULL COMMENT '执行时间',
    `executor_id` bigint(20) DEFAULT NULL COMMENT '执行人ID',
    `executor_name` varchar(50) DEFAULT NULL COMMENT '执行人姓名',
    `change_result` char(1) DEFAULT '1' COMMENT '执行结果(1成功 2失败 3部分成功)',
    `approver_id` bigint(20) DEFAULT NULL COMMENT '审批人ID',
    `approver_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
    `status` varchar(32) DEFAULT 'APPLYING' COMMENT '变更状态(APPLYING/WAIT_APPROVE/APPROVED/EXECUTING/WAIT_VERIFY/ARCHIVED/REJECTED)',
    `work_item_id` bigint(20) DEFAULT NULL COMMENT '闭环事项ID',
    `patch_no` varchar(64) DEFAULT NULL COMMENT '补丁号',
    `verify_result` varchar(32) DEFAULT NULL COMMENT '验证结果',
    `verify_detail` varchar(1000) DEFAULT NULL COMMENT '验证说明',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_change_no` (`change_no`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_change_time` (`change_time`),
    KEY `idx_change_type` (`change_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='变更记录表';

-- ============================================
-- 模块七：知识库
-- ============================================

-- 12. 知识库
DROP TABLE IF EXISTS `ops_knowledge`;
CREATE TABLE `ops_knowledge` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `fault_id` bigint(20) DEFAULT NULL COMMENT '来源故障记录ID',
    `title` varchar(200) NOT NULL COMMENT '知识标题',
    `category` char(1) DEFAULT '1' COMMENT '分类(1故障处理 2操作手册 3应急预案 4FAQ)',
    `tags` varchar(500) DEFAULT NULL COMMENT '标签',
    `problem_desc` text COMMENT '问题描述',
    `solution_desc` text COMMENT '解决方案/操作步骤',
    `applicable_env` varchar(200) DEFAULT NULL COMMENT '适用环境',
    `author_id` bigint(20) DEFAULT NULL COMMENT '作者ID',
    `view_count` int(11) DEFAULT '0' COMMENT '查阅次数',
    `is_published` char(1) DEFAULT '0' COMMENT '发布状态(0草稿 1已发布)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_category` (`category`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_is_published` (`is_published`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运维知识库';

-- 13. 知识库附件
DROP TABLE IF EXISTS `ops_knowledge_attach`;
CREATE TABLE `ops_knowledge_attach` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `knowledge_id` bigint(20) NOT NULL COMMENT '知识库ID',
    `file_name` varchar(200) NOT NULL COMMENT '原始文件名',
    `file_path` varchar(500) NOT NULL COMMENT '存储路径',
    `file_size` bigint(20) DEFAULT '0' COMMENT '文件大小(字节)',
    `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_knowledge_id` (`knowledge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库附件表';

-- 14. 知识库浏览记录
DROP TABLE IF EXISTS `ops_knowledge_view_log`;
CREATE TABLE `ops_knowledge_view_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `knowledge_id` bigint(20) NOT NULL COMMENT '知识库ID',
    `viewer_id` bigint(20) DEFAULT NULL COMMENT '浏览人ID',
    `viewer_name` varchar(50) DEFAULT NULL COMMENT '浏览人姓名',
    `view_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    PRIMARY KEY (`id`),
    KEY `idx_knowledge_id` (`knowledge_id`),
    KEY `idx_view_time` (`view_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库浏览记录表';

-- ============================================
-- 模块八：闭环事项 / Bug / 需求
-- ============================================

-- 15. 统一闭环事项
DROP TABLE IF EXISTS `ops_work_item`;
CREATE TABLE `ops_work_item` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `item_no` varchar(64) NOT NULL COMMENT '事项编号',
    `item_type` varchar(32) DEFAULT 'OTHER' COMMENT '事项类型',
    `source_module` varchar(64) DEFAULT NULL COMMENT '来源模块',
    `source_id` bigint(20) DEFAULT NULL COMMENT '来源记录ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '系统ID',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称',
    `title` varchar(200) NOT NULL COMMENT '标题',
    `priority` varchar(16) DEFAULT 'P2' COMMENT '优先级',
    `status` varchar(32) DEFAULT 'PENDING' COMMENT '状态',
    `owner_id` bigint(20) DEFAULT NULL COMMENT '负责人ID',
    `owner_name` varchar(50) DEFAULT NULL COMMENT '负责人',
    `submitter` varchar(50) DEFAULT NULL COMMENT '提出人',
    `submit_time` datetime DEFAULT NULL COMMENT '提出时间',
    `plan_finish_time` datetime DEFAULT NULL COMMENT '计划完成时间',
    `actual_finish_time` datetime DEFAULT NULL COMMENT '实际完成时间',
    `progress` text COMMENT '处理进展',
    `acceptance_result` varchar(32) DEFAULT NULL COMMENT '验收结果',
    `close_desc` varchar(1000) DEFAULT NULL COMMENT '关闭说明',
    `overdue_flag` char(1) DEFAULT '0' COMMENT '是否逾期(0否 1是)',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_item_no` (`item_no`),
    KEY `idx_item_type` (`item_type`),
    KEY `idx_source` (`source_module`, `source_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`),
    KEY `idx_overdue` (`overdue_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统一闭环事项';

-- 16. 事项流转日志
DROP TABLE IF EXISTS `ops_work_item_log`;
CREATE TABLE `ops_work_item_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `work_item_id` bigint(20) DEFAULT NULL COMMENT '工作项ID',
    `item_no` varchar(64) DEFAULT NULL COMMENT '事项编号',
    `from_status` varchar(32) DEFAULT NULL COMMENT '原状态',
    `to_status` varchar(32) DEFAULT NULL COMMENT '新状态',
    `action_name` varchar(100) DEFAULT NULL COMMENT '动作名称',
    `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人',
    `action_time` datetime DEFAULT NULL COMMENT '操作时间',
    `action_remark` varchar(1000) DEFAULT NULL COMMENT '操作备注',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_work_item_id` (`work_item_id`),
    KEY `idx_item_no` (`item_no`),
    KEY `idx_action_time` (`action_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事项流转日志';

-- 17. 衡泰需求（含P1验收增强字段）
DROP TABLE IF EXISTS `ht_requirement`;
CREATE TABLE `ht_requirement` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `req_no` varchar(64) NOT NULL COMMENT '需求编号',
    `dept_code` varchar(16) NOT NULL COMMENT '提出部门',
    `req_name` varchar(200) NOT NULL COMMENT '需求名称',
    `priority` varchar(16) DEFAULT 'P2' COMMENT '优先级',
    `module_code` varchar(32) DEFAULT NULL COMMENT '所属模块',
    `system_id` bigint(20) DEFAULT NULL COMMENT '系统ID',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称',
    `req_desc` text COMMENT '需求描述',
    `business_value` text COMMENT '业务价值',
    `business_flag` char(1) DEFAULT '0' COMMENT '是否涉及商务',
    `submitter` varchar(50) DEFAULT NULL COMMENT '提出人',
    `submit_time` datetime DEFAULT NULL COMMENT '提出时间',
    `expected_online_time` date DEFAULT NULL COMMENT '预计上线时间',
    `vendor_analyst` varchar(50) DEFAULT NULL COMMENT '厂商分析人',
    `analysis_result` varchar(32) DEFAULT NULL COMMENT '分析结果',
    `analysis_detail` text COMMENT '分析详情',
    `plan_schedule_time` date DEFAULT NULL COMMENT '计划排期时间',
    `dev_finish_time` date DEFAULT NULL COMMENT '开发完成时间',
    `acceptor` varchar(32) DEFAULT NULL COMMENT '验收人',
    `acceptance_result` varchar(32) DEFAULT NULL COMMENT '验收结果',
    `acceptance_detail` varchar(1000) DEFAULT NULL COMMENT '验收详情',
    `online_time` date DEFAULT NULL COMMENT '上线时间',
    `patch_no` varchar(64) DEFAULT NULL COMMENT '补丁号',
    `status` varchar(32) DEFAULT 'WAIT_ANALYSIS' COMMENT '当前状态',
    `progress` text COMMENT '处理进展',
    `work_item_id` bigint(20) DEFAULT NULL COMMENT '闭环事项ID',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_req_no` (`req_no`),
    KEY `idx_dept_code` (`dept_code`),
    KEY `idx_priority` (`priority`),
    KEY `idx_status` (`status`),
    KEY `idx_work_item_id` (`work_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='衡泰需求';

-- 18. Bug记录（含P1验证闭环字段）
DROP TABLE IF EXISTS `ht_bug_record`;
CREATE TABLE `ht_bug_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `bug_no` varchar(64) NOT NULL COMMENT 'Bug编号',
    `requirement_id` bigint(20) DEFAULT NULL COMMENT '关联需求ID',
    `requirement_no` varchar(64) DEFAULT NULL COMMENT '关联需求编号',
    `system_id` bigint(20) DEFAULT NULL COMMENT '系统ID',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称',
    `bug_title` varchar(200) NOT NULL COMMENT 'Bug标题',
    `bug_desc` text COMMENT 'Bug描述',
    `severity` varchar(16) DEFAULT 'S2' COMMENT '严重程度',
    `priority` varchar(16) DEFAULT 'P2' COMMENT '优先级',
    `status` varchar(32) DEFAULT 'WAIT_CONFIRM' COMMENT '状态',
    `founder` varchar(50) DEFAULT NULL COMMENT '发现人',
    `found_time` datetime DEFAULT NULL COMMENT '发现时间',
    `owner_id` bigint(20) DEFAULT NULL COMMENT '负责人ID',
    `owner_name` varchar(50) DEFAULT NULL COMMENT '负责人',
    `fix_plan` text COMMENT '修复计划',
    `fix_result` text COMMENT '修复结果',
    `plan_fix_time` date DEFAULT NULL COMMENT '计划修复时间',
    `actual_fix_time` date DEFAULT NULL COMMENT '实际修复时间',
    `fix_complete_time` datetime DEFAULT NULL COMMENT '修复完成时间',
    `tester` varchar(50) DEFAULT NULL COMMENT '测试人',
    `test_result` varchar(32) DEFAULT NULL COMMENT '验收结果',
    `verifier_id` bigint(20) DEFAULT NULL COMMENT '验证人ID',
    `verifier_name` varchar(50) DEFAULT NULL COMMENT '验证人姓名',
    `verify_time` datetime DEFAULT NULL COMMENT '验证时间',
    `verify_result` varchar(32) DEFAULT NULL COMMENT '验证结果(PASS/FAIL/REOPEN)',
    `verify_detail` varchar(1000) DEFAULT NULL COMMENT '验证说明',
    `patch_no` varchar(64) DEFAULT NULL COMMENT '补丁号',
    `online_time` datetime DEFAULT NULL COMMENT '实际上线时间',
    `close_time` datetime DEFAULT NULL COMMENT '关闭时间',
    `close_desc` varchar(1000) DEFAULT NULL COMMENT '关闭说明',
    `work_item_id` bigint(20) DEFAULT NULL COMMENT '闭环事项ID',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_bug_no` (`bug_no`),
    KEY `idx_requirement_no` (`requirement_no`),
    KEY `idx_severity` (`severity`),
    KEY `idx_priority` (`priority`),
    KEY `idx_status` (`status`),
    KEY `idx_work_item_id` (`work_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Bug记录';

-- ============================================
-- 模块九：运维记录 / 事项关系
-- ============================================

-- 19. 运维记录
DROP TABLE IF EXISTS `ops_issue`;
CREATE TABLE `ops_issue` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `issue_no` varchar(64) NOT NULL COMMENT '记录编号',
    `issue_type` varchar(32) DEFAULT 'OTHER' COMMENT '记录类型',
    `system_id` bigint(20) DEFAULT NULL COMMENT '系统ID',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称',
    `module_code` varchar(32) DEFAULT NULL COMMENT '所属模块',
    `issue_title` varchar(200) NOT NULL COMMENT '标题',
    `issue_desc` text COMMENT '描述',
    `source_type` varchar(32) DEFAULT NULL COMMENT '发现来源',
    `impact_scope` varchar(500) DEFAULT NULL COMMENT '影响范围',
    `priority` varchar(16) DEFAULT 'P2' COMMENT '优先级',
    `founder` varchar(50) DEFAULT NULL COMMENT '发现人',
    `found_time` datetime DEFAULT NULL COMMENT '发现时间',
    `owner_id` bigint(20) DEFAULT NULL COMMENT '负责人ID',
    `owner_name` varchar(50) DEFAULT NULL COMMENT '负责人',
    `status` varchar(32) DEFAULT 'PENDING' COMMENT '当前状态',
    `handle_method` varchar(32) DEFAULT NULL COMMENT '处理方式',
    `handle_result` text COMMENT '处理结果',
    `root_cause` varchar(32) DEFAULT NULL COMMENT '根因分类',
    `work_item_id` bigint(20) DEFAULT NULL COMMENT '关联事项ID',
    `related_bug_no` varchar(64) DEFAULT NULL COMMENT '关联Bug编号',
    `related_req_no` varchar(64) DEFAULT NULL COMMENT '关联需求编号',
    `related_change_no` varchar(64) DEFAULT NULL COMMENT '关联变更编号',
    `plan_finish_time` datetime DEFAULT NULL COMMENT '计划完成时间',
    `actual_finish_time` datetime DEFAULT NULL COMMENT '实际完成时间',
    `overdue_flag` char(1) DEFAULT '0' COMMENT '是否逾期(0否 1是)',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_issue_no` (`issue_no`),
    KEY `idx_issue_type` (`issue_type`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_owner_id` (`owner_id`),
    KEY `idx_found_time` (`found_time`),
    KEY `idx_work_item_id` (`work_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运维记录';

-- 20. 事项关系
DROP TABLE IF EXISTS `ops_item_relation`;
CREATE TABLE `ops_item_relation` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `source_type` varchar(32) NOT NULL COMMENT '来源对象类型',
    `source_id` bigint(20) DEFAULT NULL COMMENT '来源对象ID',
    `source_no` varchar(64) DEFAULT NULL COMMENT '来源编号',
    `target_type` varchar(32) NOT NULL COMMENT '目标对象类型',
    `target_id` bigint(20) DEFAULT NULL COMMENT '目标对象ID',
    `target_no` varchar(64) DEFAULT NULL COMMENT '目标编号',
    `relation_type` varchar(32) NOT NULL COMMENT '关系类型',
    `relation_desc` varchar(1000) DEFAULT NULL COMMENT '关系说明',
    `relation_time` datetime DEFAULT NULL COMMENT '关联时间',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_item_relation` (`source_type`, `source_id`, `target_type`, `target_id`, `relation_type`),
    KEY `idx_source` (`source_type`, `source_id`),
    KEY `idx_source_no` (`source_no`),
    KEY `idx_target` (`target_type`, `target_id`),
    KEY `idx_target_no` (`target_no`),
    KEY `idx_relation_type` (`relation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事项关系表';

-- ============================================
-- 字典类型
-- ============================================
INSERT IGNORE INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) VALUES
('运维系统类型', 'ops_system_type', '0', 'admin', NOW(), '运维系统资产类型'),
('运维重要等级', 'ops_importance_level', '0', 'admin', NOW(), '运维系统重要程度'),
('运维系统状态', 'ops_system_status', '0', 'admin', NOW(), '系统资产生命周期状态'),
('运维成员角色', 'ops_team_role', '0', 'admin', NOW(), '运维团队成员角色'),
('运维在岗状态', 'ops_on_job_status', '0', 'admin', NOW(), '团队成员在岗状态'),
('供应商服务范围', 'ops_service_scope', '0', 'admin', NOW(), '供应商服务范围'),
('供应商SLA等级', 'ops_sla_level', '0', 'admin', NOW(), '供应商服务SLA等级'),
('班次类型', 'ops_shift_type', '0', 'admin', NOW(), '值班班次类型'),
('班次状态', 'ops_shift_status', '0', 'admin', NOW(), '值班班次状态'),
('日志类型', 'ops_log_type', '0', 'admin', NOW(), '值班日志类型'),
('严重程度', 'ops_severity', '0', 'admin', NOW(), '严重程度'),
('是否升级', 'ops_yes_no', '0', 'admin', NOW(), '是否类字典'),
('故障级别', 'ops_fault_level', '0', 'admin', NOW(), '故障严重级别'),
('故障状态', 'ops_fault_status', '0', 'admin', NOW(), '故障处理状态'),
('巡检状态', 'ops_health_status', '0', 'admin', NOW(), '巡检指标状态'),
('巡检结果', 'ops_inspection_result', '0', 'admin', NOW(), '巡检整体结果'),
('备份类型', 'ops_backup_type', '0', 'admin', NOW(), '备份类型'),
('备份结果', 'ops_backup_result', '0', 'admin', NOW(), '备份执行结果'),
('恢复验证', 'ops_restore_tested', '0', 'admin', NOW(), '恢复验证结果'),
('变更类型', 'ops_change_type', '0', 'admin', NOW(), '变更类型'),
('风险等级', 'ops_risk_level', '0', 'admin', NOW(), '变更风险等级'),
('变更结果', 'ops_change_result', '0', 'admin', NOW(), '变更执行结果'),
('变更状态', 'ops_change_status', '0', 'admin', NOW(), '变更状态流转'),
('任务状态', 'ops_task_status', '0', 'admin', NOW(), '周运维任务状态'),
('运维类型', 'ops_operation_type', '0', 'admin', NOW(), '关键运维类型'),
('执行结果', 'ops_operation_result', '0', 'admin', NOW(), '运维执行结果'),
('知识分类', 'ops_knowledge_category', '0', 'admin', NOW(), '知识库分类'),
('发布状态', 'ops_publish_status', '0', 'admin', NOW(), '知识发布状态'),
('闭环事项类型', 'ops_item_type', '0', 'admin', NOW(), '统一闭环事项来源类型'),
('闭环优先级', 'ops_item_priority', '0', 'admin', NOW(), 'P0/P1/P2/P3优先级'),
('闭环事项状态', 'ops_item_status', '0', 'admin', NOW(), '统一闭环事项状态'),
('闭环验收结果', 'ops_acceptance_result', '0', 'admin', NOW(), '闭环验收结果'),
('衡泰需求部门', 'ht_dept_code', '0', 'admin', NOW(), '需求提出部门缩写'),
('衡泰需求模块', 'ht_req_module', '0', 'admin', NOW(), '需求所属模块'),
('衡泰需求状态', 'ht_req_status', '0', 'admin', NOW(), '衡泰需求标准状态流转'),
('衡泰分析结果', 'ht_analysis_result', '0', 'admin', NOW(), '厂商分析结果'),
('衡泰验收人', 'ht_acceptor', '0', 'admin', NOW(), '固定验收人'),
('衡泰验收结果', 'ht_acceptance_result', '0', 'admin', NOW(), '需求验收结果'),
('是否涉及商务', 'ht_business_flag', '0', 'admin', NOW(), '商务流程标识'),
('Bug严重程度', 'ht_bug_severity', '0', 'admin', NOW(), 'Bug严重程度'),
('Bug状态', 'ht_bug_status', '0', 'admin', NOW(), 'Bug处理状态'),
('Bug验证结果', 'ht_verify_result', '0', 'admin', NOW(), '验证通过/验证不通过/退回修复'),
('运维记录类型', 'ops_issue_type', '0', 'admin', NOW(), '运维记录分类'),
('运维记录状态', 'ops_issue_status', '0', 'admin', NOW(), '运维记录处理状态'),
('运维处理方式', 'ops_handle_method', '0', 'admin', NOW(), '运维记录处理方式'),
('运维根因分类', 'ops_root_cause', '0', 'admin', NOW(), '运维记录根因分类'),
('事项关系类型', 'ops_relation_type', '0', 'admin', NOW(), '事项关系类型');


-- ============================================
-- 字典数据
-- ============================================

-- 系统资产基础字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '核心业务', '1', 'ops_system_type', '', 'danger', 'Y', '0', 'admin', NOW(), '核心交易、清算等业务系统'),
(2, '支撑系统', '2', 'ops_system_type', '', 'warning', 'N', '0', 'admin', NOW(), '支撑业务运营的系统'),
(3, '基础设施', '3', 'ops_system_type', '', 'info', 'N', '0', 'admin', NOW(), '网络、数据库、中间件等基础设施'),
(1, '核心', '1', 'ops_importance_level', '', 'danger', 'Y', '0', 'admin', NOW(), '核心等级'),
(2, '重要', '2', 'ops_importance_level', '', 'warning', 'N', '0', 'admin', NOW(), '重要等级'),
(3, '一般', '3', 'ops_importance_level', '', 'info', 'N', '0', 'admin', NOW(), '一般等级'),
(1, '运行中', '1', 'ops_system_status', '', 'success', 'Y', '0', 'admin', NOW(), '正常运行'),
(2, '停用', '2', 'ops_system_status', '', 'warning', 'N', '0', 'admin', NOW(), '暂停使用'),
(3, '下线', '3', 'ops_system_status', '', 'info', 'N', '0', 'admin', NOW(), '已下线');

-- 团队成员字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '运维负责人', '1', 'ops_team_role', '', 'danger', 'N', '0', 'admin', NOW(), '负责整体运维'),
(2, '值班长', '2', 'ops_team_role', '', 'warning', 'N', '0', 'admin', NOW(), '负责值班协调'),
(3, '运维工程师', '3', 'ops_team_role', '', 'primary', 'Y', '0', 'admin', NOW(), '负责日常运维'),
(4, '巡检员', '4', 'ops_team_role', '', 'info', 'N', '0', 'admin', NOW(), '负责巡检执行'),
(1, '在岗', '1', 'ops_on_job_status', '', 'success', 'Y', '0', 'admin', NOW(), '当前在岗'),
(2, '休假', '2', 'ops_on_job_status', '', 'warning', 'N', '0', 'admin', NOW(), '休假中'),
(3, '离职', '3', 'ops_on_job_status', '', 'info', 'N', '0', 'admin', NOW(), '已离职');

-- 供应商字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '开发支持', '1', 'ops_service_scope', '', 'primary', 'N', '0', 'admin', NOW(), '开发与缺陷支持'),
(2, '运维支持', '2', 'ops_service_scope', '', 'success', 'Y', '0', 'admin', NOW(), '运维保障支持'),
(3, '硬件支持', '3', 'ops_service_scope', '', 'warning', 'N', '0', 'admin', NOW(), '硬件设备支持'),
(4, '综合支持', '4', 'ops_service_scope', '', 'info', 'N', '0', 'admin', NOW(), '综合服务支持'),
(1, 'P1', 'P1', 'ops_sla_level', '', 'danger', 'Y', '0', 'admin', NOW(), '最高服务等级'),
(2, 'P2', 'P2', 'ops_sla_level', '', 'warning', 'N', '0', 'admin', NOW(), '标准服务等级'),
(3, 'P3', 'P3', 'ops_sla_level', '', 'info', 'N', '0', 'admin', NOW(), '基础服务等级');

-- 值班/日志/严重程度字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '白班', '1', 'ops_shift_type', '', 'success', 'Y', '0', 'admin', NOW(), '白班'),
(2, '夜班', '2', 'ops_shift_type', '', 'primary', 'N', '0', 'admin', NOW(), '夜班'),
(3, '节假日', '3', 'ops_shift_type', '', 'warning', 'N', '0', 'admin', NOW(), '节假日值班'),
(1, '进行中', '1', 'ops_shift_status', '', 'success', 'Y', '0', 'admin', NOW(), '进行中'),
(2, '已交接', '2', 'ops_shift_status', '', 'primary', 'N', '0', 'admin', NOW(), '已交接'),
(3, '已结束', '3', 'ops_shift_status', '', 'info', 'N', '0', 'admin', NOW(), '已结束'),
(1, '日常', '1', 'ops_log_type', '', 'info', 'Y', '0', 'admin', NOW(), '日常日志'),
(2, '事件', '2', 'ops_log_type', '', 'primary', 'N', '0', 'admin', NOW(), '事件记录'),
(3, '告警', '3', 'ops_log_type', '', 'warning', 'N', '0', 'admin', NOW(), '告警记录'),
(4, '交接', '4', 'ops_log_type', '', 'success', 'N', '0', 'admin', NOW(), '交接记录'),
(1, '普通', '1', 'ops_severity', '', 'info', 'Y', '0', 'admin', NOW(), '普通'),
(2, '重要', '2', 'ops_severity', '', 'warning', 'N', '0', 'admin', NOW(), '重要'),
(3, '紧急', '3', 'ops_severity', '', 'danger', 'N', '0', 'admin', NOW(), '紧急'),
(1, '否', '0', 'ops_yes_no', '', 'info', 'Y', '0', 'admin', NOW(), '否'),
(2, '是', '1', 'ops_yes_no', '', 'danger', 'N', '0', 'admin', NOW(), '是');

-- 故障字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '一般', '1', 'ops_fault_level', '', 'info', 'Y', '0', 'admin', NOW(), '一般故障'),
(2, '严重', '2', 'ops_fault_level', '', 'warning', 'N', '0', 'admin', NOW(), '严重故障'),
(3, '紧急', '3', 'ops_fault_level', '', 'danger', 'N', '0', 'admin', NOW(), '紧急故障'),
(1, '发现', '1', 'ops_fault_status', '', 'warning', 'Y', '0', 'admin', NOW(), '已发现'),
(2, '处理中', '2', 'ops_fault_status', '', 'primary', 'N', '0', 'admin', NOW(), '处理中'),
(3, '已恢复', '3', 'ops_fault_status', '', 'success', 'N', '0', 'admin', NOW(), '已恢复'),
(4, '已关闭', '4', 'ops_fault_status', '', 'info', 'N', '0', 'admin', NOW(), '已关闭');

-- 巡检/备份字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '正常', '1', 'ops_health_status', '', 'success', 'Y', '0', 'admin', NOW(), '正常'),
(2, '异常', '2', 'ops_health_status', '', 'danger', 'N', '0', 'admin', NOW(), '异常'),
(3, '未检查', '3', 'ops_health_status', '', 'info', 'N', '0', 'admin', NOW(), '未检查'),
(1, '正常', '1', 'ops_inspection_result', '', 'success', 'Y', '0', 'admin', NOW(), '巡检正常'),
(2, '异常', '2', 'ops_inspection_result', '', 'danger', 'N', '0', 'admin', NOW(), '巡检异常'),
(3, '已处理', '3', 'ops_inspection_result', '', 'primary', 'N', '0', 'admin', NOW(), '异常已处理'),
(1, '全量', '1', 'ops_backup_type', '', 'primary', 'Y', '0', 'admin', NOW(), '全量备份'),
(2, '增量', '2', 'ops_backup_type', '', 'success', 'N', '0', 'admin', NOW(), '增量备份'),
(3, '差异', '3', 'ops_backup_type', '', 'warning', 'N', '0', 'admin', NOW(), '差异备份'),
(1, '成功', '1', 'ops_backup_result', '', 'success', 'Y', '0', 'admin', NOW(), '备份成功'),
(2, '失败', '2', 'ops_backup_result', '', 'danger', 'N', '0', 'admin', NOW(), '备份失败'),
(3, '部分成功', '3', 'ops_backup_result', '', 'warning', 'N', '0', 'admin', NOW(), '部分成功'),
(1, '未验证', '0', 'ops_restore_tested', '', 'info', 'Y', '0', 'admin', NOW(), '未恢复验证'),
(2, '已通过', '1', 'ops_restore_tested', '', 'success', 'N', '0', 'admin', NOW(), '恢复验证通过'),
(3, '未通过', '2', 'ops_restore_tested', '', 'danger', 'N', '0', 'admin', NOW(), '恢复验证未通过');

-- 变更字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '功能发布', '1', 'ops_change_type', '', 'primary', 'Y', '0', 'admin', NOW(), '功能发布'),
(2, '配置调整', '2', 'ops_change_type', '', 'success', 'N', '0', 'admin', NOW(), '配置调整'),
(3, '数据变更', '3', 'ops_change_type', '', 'warning', 'N', '0', 'admin', NOW(), '数据变更'),
(4, '应急变更', '4', 'ops_change_type', '', 'danger', 'N', '0', 'admin', NOW(), '应急变更'),
(1, '低', '1', 'ops_risk_level', '', 'success', 'Y', '0', 'admin', NOW(), '低风险'),
(2, '中', '2', 'ops_risk_level', '', 'warning', 'N', '0', 'admin', NOW(), '中风险'),
(3, '高', '3', 'ops_risk_level', '', 'danger', 'N', '0', 'admin', NOW(), '高风险'),
(1, '成功', '1', 'ops_change_result', '', 'success', 'Y', '0', 'admin', NOW(), '变更成功'),
(2, '失败', '2', 'ops_change_result', '', 'danger', 'N', '0', 'admin', NOW(), '变更失败'),
(3, '已回滚', '3', 'ops_change_result', '', 'warning', 'N', '0', 'admin', NOW(), '已回滚'),
(1, '申请中', 'APPLYING', 'ops_change_status', '', 'info', 'Y', '0', 'admin', NOW(), ''),
(2, '待审批', 'WAIT_APPROVE', 'ops_change_status', '', 'warning', 'N', '0', 'admin', NOW(), ''),
(3, '已审批', 'APPROVED', 'ops_change_status', '', '', 'N', '0', 'admin', NOW(), ''),
(4, '执行中', 'EXECUTING', 'ops_change_status', '', 'primary', 'N', '0', 'admin', NOW(), ''),
(5, '待验证', 'WAIT_VERIFY', 'ops_change_status', '', 'warning', 'N', '0', 'admin', NOW(), ''),
(6, '已归档', 'ARCHIVED', 'ops_change_status', '', 'success', 'N', '0', 'admin', NOW(), ''),
(7, '已驳回', 'REJECTED', 'ops_change_status', '', 'danger', 'N', '0', 'admin', NOW(), '');

-- 周报/运维任务字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '未开始', '1', 'ops_task_status', '', 'info', 'Y', '0', 'admin', NOW(), '未开始'),
(2, '进行中', '2', 'ops_task_status', '', 'primary', 'N', '0', 'admin', NOW(), '进行中'),
(3, '已完成', '3', 'ops_task_status', '', 'success', 'N', '0', 'admin', NOW(), '已完成'),
(4, '有风险', '4', 'ops_task_status', '', 'warning', 'N', '0', 'admin', NOW(), '有风险'),
(1, '巡检', '1', 'ops_operation_type', '', 'success', 'Y', '0', 'admin', NOW(), '巡检'),
(2, '发布', '2', 'ops_operation_type', '', 'primary', 'N', '0', 'admin', NOW(), '发布'),
(3, '变更', '3', 'ops_operation_type', '', 'warning', 'N', '0', 'admin', NOW(), '变更'),
(4, '故障处理', '4', 'ops_operation_type', '', 'danger', 'N', '0', 'admin', NOW(), '故障处理'),
(1, '成功', '1', 'ops_operation_result', '', 'success', 'Y', '0', 'admin', NOW(), '成功'),
(2, '失败', '2', 'ops_operation_result', '', 'danger', 'N', '0', 'admin', NOW(), '失败'),
(3, '部分完成', '3', 'ops_operation_result', '', 'warning', 'N', '0', 'admin', NOW(), '部分完成');

-- 知识库字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '故障处理', '1', 'ops_knowledge_category', '', 'danger', 'Y', '0', 'admin', NOW(), '故障处理'),
(2, '巡检经验', '2', 'ops_knowledge_category', '', 'success', 'N', '0', 'admin', NOW(), '巡检经验'),
(3, '变更发布', '3', 'ops_knowledge_category', '', 'primary', 'N', '0', 'admin', NOW(), '变更发布'),
(4, '备份恢复', '4', 'ops_knowledge_category', '', 'warning', 'N', '0', 'admin', NOW(), '备份恢复'),
(1, '草稿', '0', 'ops_publish_status', '', 'info', 'Y', '0', 'admin', NOW(), '草稿'),
(2, '已发布', '1', 'ops_publish_status', '', 'success', 'N', '0', 'admin', NOW(), '已发布');

-- 闭环事项字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '需求', 'REQ', 'ops_item_type', '', 'primary', 'Y', '0', 'admin', NOW(), '衡泰需求'),
(2, 'Bug', 'BUG', 'ops_item_type', '', 'danger', 'N', '0', 'admin', NOW(), 'Bug缺陷'),
(3, '运维记录', 'ISSUE', 'ops_item_type', '', 'warning', 'N', '0', 'admin', NOW(), '运维记录'),
(4, '故障', 'FAULT', 'ops_item_type', '', 'warning', 'N', '0', 'admin', NOW(), '故障记录'),
(5, '变更', 'CHANGE', 'ops_item_type', '', 'success', 'N', '0', 'admin', NOW(), '变更记录'),
(6, '其他', 'OTHER', 'ops_item_type', '', 'info', 'N', '0', 'admin', NOW(), '其他事项'),
(1, 'P0 紧急', 'P0', 'ops_item_priority', '', 'danger', 'N', '0', 'admin', NOW(), '2小时响应，24小时内提供方案'),
(2, 'P1 高', 'P1', 'ops_item_priority', '', 'warning', 'N', '0', 'admin', NOW(), '1个工作日响应，3个工作日反馈排期'),
(3, 'P2 中', 'P2', 'ops_item_priority', '', 'primary', 'Y', '0', 'admin', NOW(), '3个工作日响应，1周内反馈排期'),
(4, 'P3 低', 'P3', 'ops_item_priority', '', 'info', 'N', '0', 'admin', NOW(), '纳入需求池规划'),
(1, '待处理', 'PENDING', 'ops_item_status', '', 'info', 'Y', '0', 'admin', NOW(), '等待处理'),
(2, '处理中', 'PROCESSING', 'ops_item_status', '', 'primary', 'N', '0', 'admin', NOW(), '处理中'),
(3, '待验收', 'ACCEPTING', 'ops_item_status', '', 'warning', 'N', '0', 'admin', NOW(), '等待验收'),
(4, '已关闭', 'CLOSED', 'ops_item_status', '', 'success', 'N', '0', 'admin', NOW(), '闭环完成'),
(5, '已驳回', 'REJECTED', 'ops_item_status', '', 'danger', 'N', '0', 'admin', NOW(), '已驳回'),
(1, '通过', 'PASS', 'ops_acceptance_result', '', 'success', 'N', '0', 'admin', NOW(), '验收通过'),
(2, '不通过', 'FAIL', 'ops_acceptance_result', '', 'danger', 'N', '0', 'admin', NOW(), '验收不通过');

-- 衡泰需求部门/模块字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '固定收益部', 'FI', 'ht_dept_code', '', 'primary', 'Y', '0', 'admin', NOW(), 'FI'),
(2, '上海部', 'SH', 'ht_dept_code', '', 'info', 'N', '0', 'admin', NOW(), 'SH'),
(3, '北京部', 'BJ', 'ht_dept_code', '', 'info', 'N', '0', 'admin', NOW(), 'BJ'),
(4, '深圳部', 'SZ', 'ht_dept_code', '', 'info', 'N', '0', 'admin', NOW(), 'SZ'),
(5, '金融业务部', 'FIN', 'ht_dept_code', '', 'primary', 'N', '0', 'admin', NOW(), 'FIN'),
(6, '风险管理部', 'RM', 'ht_dept_code', '', 'warning', 'N', '0', 'admin', NOW(), 'RM'),
(7, '运营管理部', 'OM', 'ht_dept_code', '', 'success', 'N', '0', 'admin', NOW(), 'OM'),
(8, '资金托管部', 'TD', 'ht_dept_code', '', 'primary', 'N', '0', 'admin', NOW(), 'TD'),
(1, '交易', 'TRADE', 'ht_req_module', '', 'primary', 'Y', '0', 'admin', NOW(), '交易模块'),
(2, '估值', 'VALUATION', 'ht_req_module', '', 'success', 'N', '0', 'admin', NOW(), '估值模块'),
(3, '清算', 'CLEARING', 'ht_req_module', '', 'warning', 'N', '0', 'admin', NOW(), '清算模块'),
(4, '对手管理', 'COUNTERPARTY', 'ht_req_module', '', 'info', 'N', '0', 'admin', NOW(), '对手管理'),
(5, '报表', 'REPORT', 'ht_req_module', '', 'primary', 'N', '0', 'admin', NOW(), '报表模块'),
(6, '权限', 'AUTH', 'ht_req_module', '', 'danger', 'N', '0', 'admin', NOW(), '权限模块'),
(7, '其他', 'OTHER', 'ht_req_module', '', 'info', 'N', '0', 'admin', NOW(), '其他模块');

-- 需求状态/分析/验收字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '待分析', 'WAIT_ANALYSIS', 'ht_req_status', '', 'info', 'Y', '0', 'admin', NOW(), '待分析'),
(2, '分析中', 'ANALYZING', 'ht_req_status', '', 'primary', 'N', '0', 'admin', NOW(), '分析中'),
(3, '待确认', 'WAIT_CONFIRM', 'ht_req_status', '', 'warning', 'N', '0', 'admin', NOW(), '待确认'),
(4, '商务流程中', 'IN_BUSINESS', 'ht_req_status', '', 'warning', 'N', '0', 'admin', NOW(), '商务流程中'),
(5, '待排期', 'WAIT_SCHEDULE', 'ht_req_status', '', 'info', 'N', '0', 'admin', NOW(), '待排期'),
(6, '开发中', 'DEVELOPING', 'ht_req_status', '', 'primary', 'N', '0', 'admin', NOW(), '开发中'),
(7, '待验收', 'WAIT_ACCEPT', 'ht_req_status', '', 'warning', 'N', '0', 'admin', NOW(), '待验收'),
(8, '已上线', 'ONLINE', 'ht_req_status', '', 'success', 'N', '0', 'admin', NOW(), '已上线'),
(9, '已驳回', 'REJECTED', 'ht_req_status', '', 'danger', 'N', '0', 'admin', NOW(), '已驳回'),
(1, '可行', 'FEASIBLE', 'ht_analysis_result', '', 'success', 'N', '0', 'admin', NOW(), '可行'),
(2, '不可行', 'INFEASIBLE', 'ht_analysis_result', '', 'danger', 'N', '0', 'admin', NOW(), '不可行'),
(3, '需调整', 'ADJUST', 'ht_analysis_result', '', 'warning', 'N', '0', 'admin', NOW(), '需调整'),
(1, '韩宝国', 'HAN_BAOGUO', 'ht_acceptor', '', 'primary', 'N', '0', 'admin', NOW(), '韩宝国'),
(2, '余如飞', 'YU_RUFEI', 'ht_acceptor', '', 'primary', 'N', '0', 'admin', NOW(), '余如飞'),
(1, '通过', 'PASS', 'ht_acceptance_result', '', 'success', 'N', '0', 'admin', NOW(), '通过'),
(2, '不通过', 'FAIL', 'ht_acceptance_result', '', 'danger', 'N', '0', 'admin', NOW(), '不通过'),
(1, '否', '0', 'ht_business_flag', '', 'info', 'Y', '0', 'admin', NOW(), '不涉及商务'),
(2, '是', '1', 'ht_business_flag', '', 'danger', 'N', '0', 'admin', NOW(), '涉及商务流程');

-- Bug 字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, 'S0 阻断', 'S0', 'ht_bug_severity', '', 'danger', 'N', '0', 'admin', NOW(), '核心流程阻断'),
(2, 'S1 严重', 'S1', 'ht_bug_severity', '', 'warning', 'N', '0', 'admin', NOW(), '重要功能异常'),
(3, 'S2 一般', 'S2', 'ht_bug_severity', '', 'primary', 'Y', '0', 'admin', NOW(), '一般缺陷'),
(4, 'S3 轻微', 'S3', 'ht_bug_severity', '', 'info', 'N', '0', 'admin', NOW(), '体验或提示问题'),
(1, '待确认', 'WAIT_CONFIRM', 'ht_bug_status', '', 'info', 'Y', '0', 'admin', NOW(), 'Bug初始状态'),
(2, '已确认', 'CONFIRMED', 'ht_bug_status', '', '', 'N', '0', 'admin', NOW(), ''),
(3, '修复中', 'FIXING', 'ht_bug_status', '', 'warning', 'N', '0', 'admin', NOW(), ''),
(4, '修复完成', 'FIX_COMPLETE', 'ht_bug_status', '', '', 'N', '0', 'admin', NOW(), ''),
(5, '待验证', 'WAIT_RETEST', 'ht_bug_status', '', 'primary', 'N', '0', 'admin', NOW(), ''),
(6, '验证通过', 'VERIFIED', 'ht_bug_status', '', 'success', 'N', '0', 'admin', NOW(), ''),
(7, '已发布', 'PUBLISHED', 'ht_bug_status', '', '', 'N', '0', 'admin', NOW(), ''),
(8, '已关闭', 'CLOSED', 'ht_bug_status', '', 'success', 'N', '0', 'admin', NOW(), ''),
(9, '已驳回', 'REJECTED', 'ht_bug_status', '', 'danger', 'N', '0', 'admin', NOW(), ''),
(1, '验证通过', 'PASS', 'ht_verify_result', '', 'success', 'N', '0', 'admin', NOW(), ''),
(2, '验证不通过', 'FAIL', 'ht_verify_result', '', 'danger', 'N', '0', 'admin', NOW(), ''),
(3, '退回修复', 'REOPEN', 'ht_verify_result', '', 'warning', 'N', '0', 'admin', NOW(), '');

-- 运维记录字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '问题处理', 'PROBLEM', 'ops_issue_type', '', 'primary', 'N', '0', 'admin', NOW(), '问题排查与处理'),
(2, '巡检异常', 'INSPECTION', 'ops_issue_type', '', 'warning', 'N', '0', 'admin', NOW(), '巡检发现异常'),
(3, '故障处理', 'FAULT', 'ops_issue_type', '', 'danger', 'N', '0', 'admin', NOW(), '故障应急处理'),
(4, '备份操作', 'BACKUP', 'ops_issue_type', '', 'info', 'N', '0', 'admin', NOW(), '备份操作记录'),
(5, '参数配置', 'CONFIG', 'ops_issue_type', '', '', 'N', '0', 'admin', NOW(), '参数修改配置'),
(6, '系统功能', 'FUNCTION', 'ops_issue_type', '', '', 'N', '0', 'admin', NOW(), '功能问题'),
(7, '数据异常', 'DATA', 'ops_issue_type', '', '', 'N', '0', 'admin', NOW(), '数据异常处理'),
(8, '性能问题', 'PERFORMANCE', 'ops_issue_type', '', '', 'N', '0', 'admin', NOW(), '性能优化'),
(9, '变更执行', 'CHANGE', 'ops_issue_type', '', '', 'N', '0', 'admin', NOW(), '变更执行记录'),
(10, '权限管理', 'AUTH', 'ops_issue_type', '', '', 'N', '0', 'admin', NOW(), '权限相关操作'),
(11, '值班事件', 'DUTY', 'ops_issue_type', '', '', 'N', '0', 'admin', NOW(), '值班期间处理'),
(12, '其他', 'OTHER', 'ops_issue_type', '', '', 'Y', '0', 'admin', NOW(), '其他运维活动'),
(1, '待受理', 'PENDING', 'ops_issue_status', '', 'info', 'Y', '0', 'admin', NOW(), '等待受理'),
(2, '分析中', 'ANALYZING', 'ops_issue_status', '', 'primary', 'N', '0', 'admin', NOW(), '问题分析中'),
(3, '处理中', 'PROCESSING', 'ops_issue_status', '', 'primary', 'N', '0', 'admin', NOW(), '问题处理中'),
(4, '待验证', 'WAIT_VERIFY', 'ops_issue_status', '', 'warning', 'N', '0', 'admin', NOW(), '等待验证'),
(5, '已转Bug', 'CONVERTED_BUG', 'ops_issue_status', '', 'danger', 'N', '0', 'admin', NOW(), '已转为Bug'),
(6, '已转需求', 'CONVERTED_REQ', 'ops_issue_status', '', 'success', 'N', '0', 'admin', NOW(), '已转为需求'),
(7, '已转变更', 'CONVERTED_CHANGE', 'ops_issue_status', '', 'warning', 'N', '0', 'admin', NOW(), '已转为变更'),
(8, '已关闭', 'CLOSED', 'ops_issue_status', '', 'success', 'N', '0', 'admin', NOW(), '已关闭'),
(9, '已驳回', 'REJECTED', 'ops_issue_status', '', 'danger', 'N', '0', 'admin', NOW(), '已驳回');

-- 处理方式/根因/关系字典
INSERT IGNORE INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '修改配置', 'CONFIG_CHANGE', 'ops_handle_method', '', 'primary', 'N', '0', 'admin', NOW(), '通过修改配置处理'),
(2, '修改数据库', 'DB_CHANGE', 'ops_handle_method', '', 'warning', 'N', '0', 'admin', NOW(), '通过修改数据库处理'),
(3, '操作处理', 'OPERATION', 'ops_handle_method', '', 'success', 'Y', '0', 'admin', NOW(), '通过运维操作处理'),
(4, '转Bug', 'TO_BUG', 'ops_handle_method', '', 'danger', 'N', '0', 'admin', NOW(), '转为Bug处理'),
(5, '转需求', 'TO_REQ', 'ops_handle_method', '', 'primary', 'N', '0', 'admin', NOW(), '转为需求处理'),
(6, '转变更', 'TO_CHANGE', 'ops_handle_method', '', 'warning', 'N', '0', 'admin', NOW(), '转为变更处理'),
(7, '无需处理', 'NO_ACTION', 'ops_handle_method', '', 'info', 'N', '0', 'admin', NOW(), '无需处理'),
(8, '观察跟踪', 'FOLLOW_UP', 'ops_handle_method', '', 'info', 'N', '0', 'admin', NOW(), '继续观察跟踪'),
(1, '配置', 'CONFIG', 'ops_root_cause', '', 'primary', 'N', '0', 'admin', NOW(), '配置原因'),
(2, '数据', 'DATA', 'ops_root_cause', '', 'warning', 'N', '0', 'admin', NOW(), '数据原因'),
(3, '程序', 'PROGRAM', 'ops_root_cause', '', 'danger', 'N', '0', 'admin', NOW(), '程序原因'),
(4, '操作', 'OPERATION', 'ops_root_cause', '', 'info', 'N', '0', 'admin', NOW(), '操作原因'),
(5, '环境', 'ENVIRONMENT', 'ops_root_cause', '', 'warning', 'N', '0', 'admin', NOW(), '环境原因'),
(6, '权限', 'AUTH', 'ops_root_cause', '', 'primary', 'N', '0', 'admin', NOW(), '权限原因'),
(7, '业务规则', 'BUSINESS_RULE', 'ops_root_cause', '', 'success', 'N', '0', 'admin', NOW(), '业务规则原因'),
(1, '由此产生', 'GENERATE', 'ops_relation_type', '', 'primary', 'N', '0', 'admin', NOW(), '由来源事项产生目标事项'),
(2, '转为', 'CONVERT_TO', 'ops_relation_type', '', 'success', 'N', '0', 'admin', NOW(), '来源事项转为目标事项'),
(3, '关联', 'RELATE_TO', 'ops_relation_type', '', 'info', 'Y', '0', 'admin', NOW(), '普通关联'),
(4, '阻塞', 'BLOCKS', 'ops_relation_type', '', 'danger', 'N', '0', 'admin', NOW(), '来源事项阻塞目标事项'),
(5, '被阻塞', 'BLOCKED_BY', 'ops_relation_type', '', 'warning', 'N', '0', 'admin', NOW(), '来源事项被目标事项阻塞'),
(6, '重复', 'DUPLICATE', 'ops_relation_type', '', 'info', 'N', '0', 'admin', NOW(), '重复事项'),
(7, '父子', 'PARENT_CHILD', 'ops_relation_type', '', 'primary', 'N', '0', 'admin', NOW(), '父子拆分关系'),
(8, '由发布上线', 'RELEASED_BY', 'ops_relation_type', '', 'success', 'N', '0', 'admin', NOW(), '由发布上线'),
(9, '由变更实施', 'CHANGED_BY', 'ops_relation_type', '', 'warning', 'N', '0', 'admin', NOW(), '由变更实施');


-- ============================================
-- 菜单数据（扁平化结构，所有页面平铺在运维管理下）
-- 注意：需要先执行 ry_20250522.sql 建立 sys_menu / sys_role_menu 表
-- ============================================

-- 一级菜单：运维管理
INSERT IGNORE INTO sys_menu VALUES('2200', '运维管理', '0', '5', 'devops', null, null, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', NOW(), '', null, '运维管理目录');

-- === 核心闭环流程（order_num 0-6）===

INSERT IGNORE INTO sys_menu VALUES('2330', '运维仪表盘', '2200', '0', 'dashboard', 'devops/dashboard/index', null, '', 1, 0, 'C', '0', '0', 'ops:dashboard:view', 'dashboard', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2380', '运维记录', '2200', '1', 'opsIssue', 'devops/opsIssue/index', null, '', 1, 0, 'C', '0', '0', 'ops:issue:list', 'question', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2360', 'Bug管理', '2200', '2', 'htBug', 'devops/htBug/index', null, '', 1, 0, 'C', '0', '0', 'ht:bug:list', 'bug', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2350', '需求管理', '2200', '3', 'htRequirement', 'devops/htRequirement/index', null, '', 1, 0, 'C', '0', '0', 'ht:requirement:list', 'form', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2310', '变更记录', '2200', '4', 'changeRecord', 'devops/changeRecord/index', null, '', 1, 0, 'C', '0', '0', 'ops:change:list', 'cascader', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2341', '统一事项', '2200', '5', 'workItem', 'devops/workItem/index', null, '', 1, 0, 'C', '0', '0', 'ops:work-item:list', 'list', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2370', '报告中心', '2200', '6', 'reportCenter', 'devops/reportCenter/index', null, '', 1, 0, 'C', '0', '0', 'ops:report:view', 'chart', 'admin', NOW(), '', null, '');

-- === 日常运维（order_num 8-13）===

INSERT IGNORE INTO sys_menu VALUES('2261', '值班管理', '2200', '8', 'dutyShift', 'devops/dutyShift/index', null, '', 1, 0, 'C', '0', '0', 'ops:duty-shift:list', 'date-range', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2270', '值班日志', '2200', '9', 'shiftLog', 'devops/shiftLog/index', null, '', 1, 0, 'C', '0', '0', 'ops:shift-log:list', 'log', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2280', '巡检记录', '2200', '10', 'dailyInspectionMain', 'devops/dailyInspectionMain/index', null, '', 1, 0, 'C', '0', '0', 'daily:inspection:list', 'eye-open', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2291', '故障记录', '2200', '11', 'faultRecord', 'devops/faultRecord/index', null, '', 1, 0, 'C', '0', '0', 'ops:fault:list', 'warning', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2300', '备份记录', '2200', '12', 'backupRecord', 'devops/backupRecord/index', null, '', 1, 0, 'C', '0', '0', 'ops:backup:list', 'zip', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2321', '运维知识库', '2200', '13', 'knowledgeBase', 'devops/knowledge/index', null, '', 1, 0, 'C', '0', '0', 'ops:knowledge:list', 'education', 'admin', NOW(), '', null, '');

-- === 基础数据（order_num 15-17）===

INSERT IGNORE INTO sys_menu VALUES('2211', '系统资产', '2200', '15', 'systemAsset', 'devops/systemAsset/index', null, '', 1, 0, 'C', '0', '0', 'ops:system-asset:list', 'tree-table', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2202', '团队成员', '2200', '16', 'teamMember', 'devops/teamMember/index', null, '', 1, 0, 'C', '0', '0', 'ops:team-member:list', 'peoples', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2220', '供应商', '2200', '17', 'vendorContact', 'devops/vendorContact/index', null, '', 1, 0, 'C', '0', '0', 'ops:vendor-contact:list', 'international', 'admin', NOW(), '', null, '');

-- === 功能权限按钮（F类）===

-- 运维记录权限
INSERT IGNORE INTO sys_menu VALUES('2381', '运维记录查询', '2380', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2382', '运维记录新增', '2380', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2383', '运维记录修改', '2380', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2384', '运维记录删除', '2380', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2385', '运维记录导出', '2380', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:export', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2386', '运维记录转化', '2380', '6', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:convert', '#', 'admin', NOW(), '', null, '');

-- Bug权限
INSERT IGNORE INTO sys_menu VALUES('2361', 'Bug查询', '2360', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2362', 'Bug新增', '2360', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2363', 'Bug修改', '2360', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2364', 'Bug删除', '2360', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2365', 'Bug导出', '2360', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:export', '#', 'admin', NOW(), '', null, '');

-- 需求权限
INSERT IGNORE INTO sys_menu VALUES('2351', '需求查询', '2350', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2352', '需求新增', '2350', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2353', '需求修改', '2350', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2354', '需求删除', '2350', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2355', '需求导出', '2350', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:export', '#', 'admin', NOW(), '', null, '');

-- 变更权限
INSERT IGNORE INTO sys_menu VALUES('2311', '变更查询', '2310', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2312', '变更新增', '2310', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2313', '变更修改', '2310', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2314', '变更删除', '2310', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2315', '变更导出', '2310', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:export', '#', 'admin', NOW(), '', null, '');

-- 统一事项权限（只读）
INSERT IGNORE INTO sys_menu VALUES('2342', '统一事项查询', '2341', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:work-item:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2345', '统一事项导出', '2341', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:work-item:export', '#', 'admin', NOW(), '', null, '');

-- 报告权限
INSERT IGNORE INTO sys_menu VALUES('2371', '报告查看', '2370', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:report:view', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2372', '报告导出', '2370', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:report:export', '#', 'admin', NOW(), '', null, '');

-- 事项关系权限
INSERT IGNORE INTO sys_menu VALUES('2387', '事项关系列表', '2380', '7', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:item-relation:list', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2388', '事项关系查询', '2380', '8', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:item-relation:query', '#', 'admin', NOW(), '', null, '');

-- 日常运维权限
INSERT IGNORE INTO sys_menu VALUES('2262', '值班管理查询', '2261', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2263', '值班管理新增', '2261', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2264', '值班管理修改', '2261', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2265', '值班管理删除', '2261', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2266', '值班管理导出', '2261', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:export', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2271', '值班日志查询', '2270', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2272', '值班日志新增', '2270', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2273', '值班日志修改', '2270', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2274', '值班日志删除', '2270', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2275', '值班日志导出', '2270', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:export', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2281', '巡检记录查询', '2280', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2282', '巡检记录新增', '2280', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2283', '巡检记录修改', '2280', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2284', '巡检记录删除', '2280', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2285', '巡检记录导出', '2280', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:export', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2292', '故障记录查询', '2291', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2293', '故障记录新增', '2291', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2294', '故障记录修改', '2291', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2295', '故障记录删除', '2291', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2296', '故障记录导出', '2291', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:export', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2301', '备份记录查询', '2300', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2302', '备份记录新增', '2300', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2303', '备份记录修改', '2300', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2304', '备份记录删除', '2300', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2305', '备份记录导出', '2300', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:export', '#', 'admin', NOW(), '', null, '');

-- 知识库权限
INSERT IGNORE INTO sys_menu VALUES('2322', '运维知识库查询', '2321', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2323', '运维知识库新增', '2321', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2324', '运维知识库修改', '2321', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2325', '运维知识库删除', '2321', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2326', '运维知识库导出', '2321', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:export', '#', 'admin', NOW(), '', null, '');

-- 系统资产权限
INSERT IGNORE INTO sys_menu VALUES('2212', '系统资产查询', '2211', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2213', '系统资产新增', '2211', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2214', '系统资产修改', '2211', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2215', '系统资产删除', '2211', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2216', '系统资产导出', '2211', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:export', '#', 'admin', NOW(), '', null, '');

-- 团队成员权限
INSERT IGNORE INTO sys_menu VALUES('2203', '团队成员查询', '2202', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2204', '团队成员新增', '2202', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2205', '团队成员修改', '2202', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2206', '团队成员删除', '2202', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2207', '团队成员导出', '2202', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:export', '#', 'admin', NOW(), '', null, '');

-- 供应商权限
INSERT IGNORE INTO sys_menu VALUES('2221', '供应商查询', '2220', '1', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:query', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2222', '供应商新增', '2220', '2', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:add', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2223', '供应商修改', '2220', '3', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:edit', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2224', '供应商删除', '2220', '4', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:remove', '#', 'admin', NOW(), '', null, '');
INSERT IGNORE INTO sys_menu VALUES('2225', '供应商导出', '2220', '5', '', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:export', '#', 'admin', NOW(), '', null, '');

-- ============================================
-- 超级管理员(role_id=1)菜单权限分配
-- ============================================
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT '1', m.menu_id FROM (
    SELECT menu_id FROM sys_menu WHERE menu_id BETWEEN '2200' AND '2399'
) m;

