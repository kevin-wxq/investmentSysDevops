-- ========================================
-- 投资系统运维项目 — 建表脚本 v4（最终版）
-- 覆盖：人员 + 资产 + 供应商 + 周报 + 巡检 + 故障 + 备份 + 变更 + 值班 + 知识库
-- 共 13 张表，按模块分层
-- ========================================

-- ============================================
-- 模块一：组织人员
-- ============================================

-- 1、运维团队成员
CREATE TABLE `ops_team_member` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint(20) DEFAULT NULL COMMENT '关联若依 sys_user.id（如已是系统用户）',
    `real_name` varchar(50) NOT NULL COMMENT '姓名',
    `employee_no` varchar(50) DEFAULT NULL COMMENT '工号',
    `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `team` varchar(50) DEFAULT NULL COMMENT '所属小组',
    `role_type` char(1) DEFAULT '3' COMMENT '角色(1运维负责人 2值班长 3运维工程师 4巡检员)',
    `skill_tags` varchar(500) DEFAULT NULL COMMENT '技能标签，逗号分隔，如"DBA,Linux,网络"',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='运维团队成员表';


-- ============================================
-- 模块二：基础资源
-- ============================================

-- 2、系统资产台账
CREATE TABLE `ops_system_asset` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_code` varchar(50) DEFAULT NULL COMMENT '系统编码，如 INV-001',
    `system_name` varchar(100) NOT NULL COMMENT '系统名称',
    `system_type` char(1) DEFAULT '1' COMMENT '类型(1核心业务 2支撑系统 3基础设施)',
    `importance_level` char(1) DEFAULT '2' COMMENT '重要等级(1核心 2重要 3一般)',
    `system_url` varchar(200) DEFAULT NULL COMMENT '访问地址',
    `server_ip` varchar(200) DEFAULT NULL COMMENT '部署IP，多个用逗号分隔',
    `db_type` varchar(50) DEFAULT NULL COMMENT '数据库类型',
    `dev_lang` varchar(100) DEFAULT NULL COMMENT '开发语言/框架',
    `department` varchar(100) DEFAULT NULL COMMENT '所属部门',
    `business_owner` varchar(50) DEFAULT NULL COMMENT '业务负责人',
    `tech_owner_id` bigint(20) DEFAULT NULL COMMENT '技术负责人ID，关联ops_team_member.id',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统资产台账';

-- 3、供应商/联系人
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='供应商/联系人表';


-- ============================================
-- 模块三：值班管理
-- ============================================

-- 4、值班排班
CREATE TABLE `ops_duty_shift` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `duty_date` date NOT NULL COMMENT '值班日期',
    `shift_type` char(1) DEFAULT '1' COMMENT '班次(1白班 2夜班 3全天)',
    `duty_member_id` bigint(20) NOT NULL COMMENT '值班人ID，关联ops_team_member.id',
    `backup_member_id` bigint(20) DEFAULT NULL COMMENT '备勤人ID，关联ops_team_member.id',
    `duty_phone` varchar(20) DEFAULT NULL COMMENT '值班电话（冗余，方便快速查看）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_date_shift` (`duty_date`, `shift_type`),
    KEY `idx_duty_member` (`duty_member_id`),
    KEY `idx_duty_date` (`duty_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='值班排班表';

-- 5、交接班记录
CREATE TABLE `ops_shift_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `shift_id` bigint(20) NOT NULL COMMENT '关联值班排班ID',
    `handover_from_id` bigint(20) NOT NULL COMMENT '交班人ID，关联ops_team_member.id',
    `handover_to_id` bigint(20) NOT NULL COMMENT '接班人ID，关联ops_team_member.id',
    `handover_time` datetime NOT NULL COMMENT '交接时间',
    `running_status` text COMMENT '当前系统运行概况',
    `pending_items` text COMMENT '待处理事项',
    `important_notice` text COMMENT '重要通知/注意事项',
    `asset_status` varchar(500) DEFAULT NULL COMMENT '资产/设备异常情况',
    `handover_confirm` char(1) DEFAULT '0' COMMENT '接班人确认(0未确认 1已确认)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_shift_id` (`shift_id`),
    KEY `idx_handover_time` (`handover_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='交接班记录表';


-- ============================================
-- 模块四：周报管理
-- ============================================

-- 6、周报主表
CREATE TABLE `weekly_operation_task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_title` varchar(200) DEFAULT NULL COMMENT '周报标题',
    `report_week` varchar(10) DEFAULT NULL COMMENT '周次，如 2026-W22',
    `report_date` date DEFAULT NULL COMMENT '周报日期（周一）',
    `report_type` char(1) DEFAULT '1' COMMENT '类型(1周报 2月报 3季报)',
    `report_status` char(1) DEFAULT '0' COMMENT '状态(0草稿 1已提交 2已审核)',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='周报主表';

-- 7、重点运维工作明细
CREATE TABLE `weekly_key_operation_detail` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `change_id` bigint(20) DEFAULT NULL COMMENT '关联变更记录ID',
    `operation_type` char(1) DEFAULT NULL COMMENT '类型(1重要系统大版本升级 2重要业务支撑 3重要信息化支持 4信息化建设重要节点)',
    `task_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
    `executor_id` bigint(20) DEFAULT NULL COMMENT '执行人ID，关联ops_team_member.id',
    `demander` varchar(100) DEFAULT NULL COMMENT '需求方',
    `propose_date` date DEFAULT NULL COMMENT '提出时间',
    `require_end_date` date DEFAULT NULL COMMENT '要求完成时间',
    `actual_end_date` date DEFAULT NULL COMMENT '实际完成时间',
    `task_status` varchar(50) DEFAULT NULL COMMENT '事项状态(未开展/执行中/已完成/暂停/挂起)',
    `requirement_desc` text COMMENT '需求描述',
    `weekly_progress` text COMMENT '本周推进',
    `issues_risks` text COMMENT '问题及风险',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='重点运维工作明细表';

-- 8、日常运维明细
CREATE TABLE `weekly_daily_operation` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `fault_id` bigint(20) DEFAULT NULL COMMENT '关联故障记录ID',
    `operation_type` char(1) DEFAULT NULL COMMENT '运维类型(1故障处理 2版本升级 3日常巡检 4备份检查)',
    `operation_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
    `executor_id` bigint(20) DEFAULT NULL COMMENT '执行人ID，关联ops_team_member.id',
    `demander` varchar(100) DEFAULT NULL COMMENT '需求方',
    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
    `task_status` varchar(50) DEFAULT NULL COMMENT '事项状态(未开展/执行中/已完成)',
    `issue_desc` text COMMENT '故障描述/升级内容',
    `handle_desc` text COMMENT '处理过程/解决方案',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常运维明细表';

-- 9、巡检记录表
CREATE TABLE `daily_inspection_main` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `daily_operation_id` bigint(20) DEFAULT NULL COMMENT '关联日常运维明细ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `inspection_date` date NOT NULL COMMENT '巡检日期',
    `inspection_time` time NOT NULL COMMENT '巡检时间',
    `inspector_id` bigint(20) DEFAULT NULL COMMENT '巡检人员ID，关联ops_team_member.id',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统/模块名称（冗余，未关联资产时可手填）',
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
    `network_traffic` int(11) DEFAULT NULL COMMENT '网络流量(Mbps)',
    `alarm_info` varchar(500) DEFAULT NULL COMMENT '异常告警',
    `action_taken` varchar(500) DEFAULT NULL COMMENT '处理措施',
    `inspection_result` char(1) DEFAULT '1' COMMENT '巡检结论(0异常 1正常)',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_date_system` (`inspection_date`, `system_name`),
    KEY `idx_inspector_id` (`inspector_id`),
    KEY `idx_report_id` (`report_id`),
    KEY `idx_system_id` (`system_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常巡检记录表';


-- ============================================
-- 模块五：专项记录
-- ============================================

-- 10、故障记录
CREATE TABLE `ops_fault_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `fault_level` char(1) DEFAULT '2' COMMENT '故障等级(1一般 2严重 3紧急)',
    `fault_title` varchar(200) NOT NULL COMMENT '故障标题',
    `fault_desc` text COMMENT '故障现象',
    `root_cause` text COMMENT '根本原因',
    `solution` text COMMENT '解决方案',
    `occur_time` datetime NOT NULL COMMENT '发生时间',
    `recover_time` datetime DEFAULT NULL COMMENT '恢复时间',
    `downtime_minutes` int(11) DEFAULT '0' COMMENT '宕机时长(分钟)',
    `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID，关联ops_team_member.id',
    `fault_status` char(1) DEFAULT '1' COMMENT '状态(1处理中 2已恢复 3已关闭)',
    `is_emergency` char(1) DEFAULT '0' COMMENT '是否紧急变更(0否 1是)',
    `knowledge_id` bigint(20) DEFAULT NULL COMMENT '关联知识库ID（关闭后沉淀）',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='故障记录表';

-- 11、备份记录
CREATE TABLE `ops_backup_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `backup_type` char(1) DEFAULT '1' COMMENT '备份类型(1数据库 2文件 3配置 4全量)',
    `backup_time` datetime NOT NULL COMMENT '备份时间',
    `backup_size_mb` int(11) DEFAULT NULL COMMENT '备份大小(MB)',
    `backup_result` char(1) DEFAULT '1' COMMENT '备份结果(1成功 2失败)',
    `backup_path` varchar(500) DEFAULT NULL COMMENT '备份路径',
    `restore_tested` char(1) DEFAULT '0' COMMENT '恢复验证(0未验证 1已验证)',
    `executor_id` bigint(20) DEFAULT NULL COMMENT '执行人ID，关联ops_team_member.id',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='备份记录表';

-- 12、变更记录
CREATE TABLE `ops_change_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `change_type` char(1) DEFAULT '1' COMMENT '变更类型(1版本升级 2配置变更 3数据库变更 4网络变更 5其他)',
    `change_title` varchar(200) NOT NULL COMMENT '变更标题',
    `change_desc` text COMMENT '变更描述',
    `risk_level` char(1) DEFAULT '2' COMMENT '风险等级(1低 2中 3高)',
    `change_content` text COMMENT '变更内容详情',
    `rollback_plan` text COMMENT '回滚方案',
    `change_time` datetime NOT NULL COMMENT '执行时间',
    `executor_id` bigint(20) DEFAULT NULL COMMENT '执行人ID，关联ops_team_member.id',
    `change_result` char(1) DEFAULT '1' COMMENT '执行结果(1成功 2失败 3部分成功)',
    `approver_id` bigint(20) DEFAULT NULL COMMENT '审批人ID，关联ops_team_member.id',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_system_id` (`system_id`),
    KEY `idx_change_time` (`change_time`),
    KEY `idx_change_type` (`change_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='变更记录表';


-- ============================================
-- 模块六：知识沉淀
-- ============================================

-- 13、知识库
CREATE TABLE `ops_knowledge` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `fault_id` bigint(20) DEFAULT NULL COMMENT '来源故障记录ID',
    `title` varchar(200) NOT NULL COMMENT '知识标题',
    `category` char(1) DEFAULT '1' COMMENT '分类(1故障处理 2操作手册 3应急预案 4FAQ)',
    `tags` varchar(500) DEFAULT NULL COMMENT '标签，逗号分隔',
    `problem_desc` text COMMENT '问题描述',
    `solution_desc` text COMMENT '解决方案/操作步骤',
    `applicable_env` varchar(200) DEFAULT NULL COMMENT '适用环境',
    `author_id` bigint(20) DEFAULT NULL COMMENT '作者ID，关联ops_team_member.id',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='运维知识库';

-- ============================================
-- 代码生成兼容字段
-- 说明：当前 CRUD 代码和页面来自若依代码生成器，部分字段名与上方业务建模字段存在差异。
-- 本段补齐 Mapper 实际读写字段，并放宽少量未由页面提交的 NOT NULL 字段，确保初始化脚本与当前代码可直接联调。
-- 后续业务建模收敛时，应统一保留一套字段并移除兼容冗余字段。
-- ============================================

ALTER TABLE `daily_inspection_main`
    ADD COLUMN `inspector_name` varchar(50) DEFAULT NULL COMMENT '巡检人员姓名' AFTER `inspector_id`,
    ADD COLUMN `server_ip` varchar(50) DEFAULT NULL COMMENT '服务器IP' AFTER `system_name`,
    ADD COLUMN `network_status` char(1) DEFAULT '1' COMMENT '网络状态(0异常 1正常)' AFTER `disk_usage`,
    ADD COLUMN `alarm_count` int(11) DEFAULT '0' COMMENT '告警数量' AFTER `alarm_info`,
    ADD COLUMN `handle_measures` varchar(500) DEFAULT NULL COMMENT '处理措施' AFTER `inspection_result`,
    ADD COLUMN `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID' AFTER `handle_measures`,
    ADD COLUMN `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名' AFTER `handler_id`,
    ADD COLUMN `handle_time` datetime DEFAULT NULL COMMENT '处理时间' AFTER `handler_name`,
    ADD COLUMN `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果' AFTER `handle_time`,
    ADD COLUMN `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)' AFTER `remark`;

ALTER TABLE `ops_duty_shift`
    MODIFY COLUMN `duty_date` date DEFAULT NULL COMMENT '值班日期',
    MODIFY COLUMN `duty_member_id` bigint(20) DEFAULT NULL COMMENT '值班人ID，关联ops_team_member.id',
    ADD COLUMN `shift_name` varchar(100) DEFAULT NULL COMMENT '班次名称' AFTER `id`,
    ADD COLUMN `shift_date` date DEFAULT NULL COMMENT '班次日期' AFTER `shift_name`,
    ADD COLUMN `leader_id` bigint(20) DEFAULT NULL COMMENT '带班领导ID' AFTER `shift_type`,
    ADD COLUMN `leader_name` varchar(50) DEFAULT NULL COMMENT '带班领导姓名' AFTER `leader_id`,
    ADD COLUMN `member_ids` varchar(500) DEFAULT NULL COMMENT '值班成员ID，逗号分隔' AFTER `leader_name`,
    ADD COLUMN `handover_notes` varchar(500) DEFAULT NULL COMMENT '交接备注' AFTER `member_ids`,
    ADD COLUMN `shift_status` char(1) DEFAULT '1' COMMENT '班次状态(1进行中 2已交接 3已结束)' AFTER `handover_notes`,
    ADD COLUMN `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)' AFTER `remark`;

ALTER TABLE `ops_shift_log`
    MODIFY COLUMN `handover_from_id` bigint(20) DEFAULT NULL COMMENT '交班人ID，关联ops_team_member.id',
    MODIFY COLUMN `handover_to_id` bigint(20) DEFAULT NULL COMMENT '接班人ID，关联ops_team_member.id',
    MODIFY COLUMN `handover_time` datetime DEFAULT NULL COMMENT '交接时间',
    ADD COLUMN `log_time` datetime DEFAULT NULL COMMENT '日志时间' AFTER `shift_id`,
    ADD COLUMN `log_type` char(1) DEFAULT '1' COMMENT '日志类型(1日常 2事件 3告警 4交接)' AFTER `log_time`,
    ADD COLUMN `log_content` text COMMENT '日志内容' AFTER `log_type`,
    ADD COLUMN `severity` char(1) DEFAULT '1' COMMENT '严重程度(1普通 2重要 3紧急)' AFTER `log_content`,
    ADD COLUMN `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID' AFTER `severity`,
    ADD COLUMN `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名' AFTER `handler_id`,
    ADD COLUMN `handling_result` varchar(500) DEFAULT NULL COMMENT '处理结果' AFTER `handler_name`,
    ADD COLUMN `is_escalated` char(1) DEFAULT '0' COMMENT '是否升级(0否 1是)' AFTER `handling_result`,
    ADD COLUMN `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)' AFTER `remark`;

ALTER TABLE `ops_fault_record`
    ADD COLUMN `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称' AFTER `system_id`,
    ADD COLUMN `discoverer_id` bigint(20) DEFAULT NULL COMMENT '发现人ID' AFTER `occur_time`,
    ADD COLUMN `discoverer_name` varchar(50) DEFAULT NULL COMMENT '发现人姓名' AFTER `discoverer_id`,
    ADD COLUMN `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名' AFTER `handler_id`,
    ADD COLUMN `duration_minutes` int(11) DEFAULT '0' COMMENT '持续时长(分钟)' AFTER `recover_time`,
    ADD COLUMN `preventive_measures` text COMMENT '预防措施' AFTER `solution`,
    ADD COLUMN `affected_scope` varchar(500) DEFAULT NULL COMMENT '影响范围' AFTER `preventive_measures`;

ALTER TABLE `weekly_key_operation_detail`
    ADD COLUMN `task_id` bigint(20) DEFAULT NULL COMMENT '关联周运维任务ID' AFTER `id`,
    ADD COLUMN `operation_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称' AFTER `operation_type`,
    ADD COLUMN `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称' AFTER `operation_name`,
    ADD COLUMN `operation_content` text COMMENT '运维内容' AFTER `system_name`,
    ADD COLUMN `executor` varchar(50) DEFAULT NULL COMMENT '执行人' AFTER `operation_content`,
    ADD COLUMN `start_time` datetime DEFAULT NULL COMMENT '开始时间' AFTER `executor`,
    ADD COLUMN `end_time` datetime DEFAULT NULL COMMENT '结束时间' AFTER `start_time`,
    ADD COLUMN `operation_result` varchar(500) DEFAULT NULL COMMENT '执行结果' AFTER `end_time`,
    ADD COLUMN `affected_systems` varchar(500) DEFAULT NULL COMMENT '影响系统' AFTER `operation_result`,
    ADD COLUMN `verify_method` varchar(500) DEFAULT NULL COMMENT '验证方式' AFTER `affected_systems`,
    ADD COLUMN `verify_result` varchar(500) DEFAULT NULL COMMENT '验证结果' AFTER `verify_method`,
    ADD COLUMN `issues_found` varchar(500) DEFAULT NULL COMMENT '发现问题' AFTER `verify_result`;

ALTER TABLE `weekly_operation_task`
    ADD COLUMN `report_id` bigint(20) DEFAULT NULL COMMENT '周报ID' AFTER `id`,
    ADD COLUMN `task_name` varchar(200) DEFAULT NULL COMMENT '任务名称' AFTER `report_id`,
    ADD COLUMN `executor` varchar(50) DEFAULT NULL COMMENT '执行人' AFTER `task_name`,
    ADD COLUMN `requester` varchar(100) DEFAULT NULL COMMENT '需求方' AFTER `executor`,
    ADD COLUMN `planned_date` date DEFAULT NULL COMMENT '计划日期' AFTER `requester`,
    ADD COLUMN `task_status` varchar(50) DEFAULT NULL COMMENT '任务状态' AFTER `planned_date`,
    ADD COLUMN `requirement_desc` text COMMENT '需求描述' AFTER `task_status`,
    ADD COLUMN `this_week_progress` text COMMENT '本周进展' AFTER `requirement_desc`,
    ADD COLUMN `next_week_plan` text COMMENT '下周计划' AFTER `this_week_progress`,
    ADD COLUMN `issue_risk` text COMMENT '问题风险' AFTER `next_week_plan`;
