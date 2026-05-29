-- ========================================
-- 投资系统运维项目 — 建表脚本 v3（全量）
-- 包含：资产台账 + 供应商 + 周报 + 明细 + 巡检 + 故障 + 备份 + 变更
-- ========================================

-- ============================================
-- 一、基础数据层
-- ============================================

-- 1、系统资产台账
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
    `tech_owner` varchar(50) DEFAULT NULL COMMENT '技术负责人',
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


-- 2、供应商/联系人
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
-- 二、周报管理层
-- ============================================

-- 3、周报主表
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


-- ============================================
-- 三、周报明细层
-- ============================================

-- 4、重点运维工作明细
CREATE TABLE `weekly_key_operation_detail` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `change_id` bigint(20) DEFAULT NULL COMMENT '关联变更记录ID（如是版本升级）',
    `operation_type` char(1) DEFAULT NULL COMMENT '重点运维类型(1重要系统大版本升级 2重要业务支撑工作 3重要信息化支持 4信息化建设重要节点支持)',
    `task_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
    `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
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
    KEY `idx_system_id` (`system_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='重点运维工作明细表';


-- 5、日常运维明细
CREATE TABLE `weekly_daily_operation` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `fault_id` bigint(20) DEFAULT NULL COMMENT '关联故障记录ID（如是故障处理）',
    `operation_type` char(1) DEFAULT NULL COMMENT '运维类型(1故障处理 2版本升级 3日常巡检 4备份检查)',
    `operation_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
    `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
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
    KEY `idx_system_id` (`system_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常运维明细表';


-- 6、巡检记录表
CREATE TABLE `daily_inspection_main` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `daily_operation_id` bigint(20) DEFAULT NULL COMMENT '关联日常运维明细ID',
    `system_id` bigint(20) DEFAULT NULL COMMENT '关联系统资产ID',
    `inspection_date` date NOT NULL COMMENT '巡检日期',
    `inspection_time` time NOT NULL COMMENT '巡检时间',
    `inspector` varchar(50) NOT NULL COMMENT '巡检人员',
    `system_name` varchar(100) DEFAULT NULL COMMENT '系统/模块名称（冗余，方便未关联资产时手填）',
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
    KEY `idx_inspector_date` (`inspector`, `inspection_date`),
    KEY `idx_report_id` (`report_id`),
    KEY `idx_system_id` (`system_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常巡检记录表';


-- ============================================
-- 四、专项记录层
-- ============================================

-- 7、故障记录
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
    `handler` varchar(50) DEFAULT NULL COMMENT '处理人',
    `fault_status` char(1) DEFAULT '1' COMMENT '状态(1处理中 2已恢复 3已关闭)',
    `is_emergency` char(1) DEFAULT '0' COMMENT '是否紧急变更(0否 1是)',
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
    KEY `idx_fault_level` (`fault_level`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='故障记录表';


-- 8、备份记录
CREATE TABLE `ops_backup_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_id` bigint(20) NOT NULL COMMENT '关联系统资产ID',
    `backup_type` char(1) DEFAULT '1' COMMENT '备份类型(1数据库 2文件 3配置 4全量)',
    `backup_time` datetime NOT NULL COMMENT '备份时间',
    `backup_size_mb` int(11) DEFAULT NULL COMMENT '备份大小(MB)',
    `backup_result` char(1) DEFAULT '1' COMMENT '备份结果(1成功 2失败)',
    `backup_path` varchar(500) DEFAULT NULL COMMENT '备份路径',
    `restore_tested` char(1) DEFAULT '0' COMMENT '是否做过恢复验证(0否 1是)',
    `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
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


-- 9、变更记录
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
    `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
    `change_result` char(1) DEFAULT '1' COMMENT '执行结果(1成功 2失败 3部分成功)',
    `approver` varchar(50) DEFAULT NULL COMMENT '审批人',
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
