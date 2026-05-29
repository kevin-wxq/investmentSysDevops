-- ========================================
-- 投资系统运维项目 — 建表脚本 v2
-- 基于初版优化：重新定位主表、补齐字段、优化索引
-- ========================================

-- 1、周报主表（重新定位：从"任务表"改为"周报头表"）
CREATE TABLE `weekly_operation_task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_title` varchar(200) DEFAULT NULL COMMENT '周报标题',
    `report_week` varchar(10) DEFAULT NULL COMMENT '周次，如 2026-W22',
    `report_date` date DEFAULT NULL COMMENT '周报日期（周一）',
    `report_type` char(1) DEFAULT '1' COMMENT '类型(1周报 2月报 3季报)',
    `report_status` char(1) DEFAULT '0' COMMENT '状态(0草稿 1已提交 2已审核)',
    `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `creator` varchar(20) DEFAULT NULL COMMENT '创建人',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_report_week` (`report_week`),
    KEY `idx_report_date` (`report_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='周报主表';


-- 2、重点运维工作明细
CREATE TABLE `weekly_key_operation_detail` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
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
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `creator` varchar(20) DEFAULT NULL COMMENT '创建人',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='重点运维工作明细表';


-- 3、日常运维明细
CREATE TABLE `weekly_daily_operation` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `operation_type` char(1) DEFAULT NULL COMMENT '运维类型(1故障处理 2版本升级 3日常巡检)',
    `operation_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
    `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
    `demander` varchar(100) DEFAULT NULL COMMENT '需求方',
    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
    `task_status` varchar(50) DEFAULT NULL COMMENT '事项状态(未开展/执行中/已完成)',
    `issue_desc` text COMMENT '故障描述/升级内容',
    `handle_desc` text COMMENT '处理过程/解决方案',
    `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)',
    PRIMARY KEY (`id`),
    KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常运维明细表';


-- 4、巡检记录表
CREATE TABLE `daily_inspection_main` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_id` bigint(20) DEFAULT NULL COMMENT '关联周报主表ID',
    `daily_operation_id` bigint(20) DEFAULT NULL COMMENT '关联日常运维明细ID',
    `inspection_date` date NOT NULL COMMENT '巡检日期',
    `inspection_time` time NOT NULL COMMENT '巡检时间',
    `inspector` varchar(50) NOT NULL COMMENT '巡检人员',
    `system_name` varchar(100) NOT NULL COMMENT '系统/模块名称',
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
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_date_system` (`inspection_date`, `system_name`),
    KEY `idx_inspector_date` (`inspector`, `inspection_date`),
    KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常巡检记录表';
