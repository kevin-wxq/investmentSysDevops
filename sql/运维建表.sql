-- 1、系统运维主表

create table `weekly_operation_task` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         `report_id` bigint(20) DEFAULT NULL COMMENT '周报ID',
--      `task_name` varchar(200) DEFAULT NULL COMMENT '运维事项名称',
--      `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
--      `demander` varchar(100) DEFAULT NULL COMMENT '需求方',
--      `propose_date` date DEFAULT NULL COMMENT '提出时间',
--      `require_end_date` date DEFAULT NULL COMMENT '要求完成时间',
--      `actual_end_date` date DEFAULT NULL COMMENT '实际完成时间',
--      `task_status` varchar(50) DEFAULT NULL COMMENT '事项状态(未开展/执行中/已完成/暂停/挂起)',
--      `requirement_desc` text COMMENT '需求描述',
--      `weekly_progress` text COMMENT '本周推进',
--      `issues_risks` text COMMENT '问题及风险',
--      `task_type` char(1) DEFAULT '1' COMMENT '任务类型(1重点运维 2日常运维 3需求响应)',
--      `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `creator` varchar(20) default null comment '创建人',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统运维工作主表';


-- 2、重点运维工作
create table `weekly_key_operation_detail` (
                                               `id` bigint(20) not null AUTO_INCREMENT COMMENT '主键ID',
                                               `task_id` bigint(20) DEFAULT NULL COMMENT '运维主表ID',
                                               `operation_type` varchar(2) default null comment '1、重要系统的大版本升级；2、重要业务的支撑工作；
                                                    3、重要的信息化支持；4、信息化建设的重要节点支持' ,
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
                                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                               `creator` varchar(20) default null comment '创建人',
                                               `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                               `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                               PRIMARY KEY (`id`),
                                               KEY `idx_task_id` (`task_id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='重点运维工作明细表';

-- 3、日常运维保障表
create table `weekly_daily_operation` (
                                          `id` bigint(20) not null auto_increment comment '主键ID',
                                          `task_id` bigint(20) default null comment '运维主表ID',
                                          `operation_type` varchar(2) default null comment '运维类型：1、故障处理；2、版本升级；3、日常巡检',
                                          `operation_name` varchar(200) default null,
                                          `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `create_by` varchar(20) default null comment '创建人',
                                          `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                          primary key (`id`),
                                          key `idx_task_id`(`task_id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常运维明细表';

-- 4、巡检明细表
CREATE TABLE `daily_inspection_main` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         `task_id` bigint(20) default null comment '日常运维保障表id',
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
                                         #   `port_status` char(1) DEFAULT '1' COMMENT '端口状态(0关闭 1开放)',
                                         `cpu_usage` decimal(5,2) DEFAULT NULL COMMENT 'CPU使用率(%)',
                                         `memory_usage` decimal(5,2) DEFAULT NULL COMMENT '内存使用率(%)',
                                         `disk_usage` decimal(5,2) DEFAULT NULL COMMENT '磁盘使用率(%)',
                                         `disk_io_status` varchar(50) DEFAULT NULL COMMENT '磁盘IO情况',
                                         `network_traffic` int(11) DEFAULT NULL COMMENT '网络流量(Mbps)',
                                         `alarm_info` varchar(500) DEFAULT NULL COMMENT '异常告警',
                                         `action_taken` varchar(500) DEFAULT NULL COMMENT '处理措施',
                                         `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                         `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_inspection_date` (`inspection_date`),
                                         KEY `idx_system_name` (`system_name`),
                                         KEY `idx_ip_address` (`ip_address`),
                                         KEY `idx_inspector` (`inspector`),
                                         KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日常巡检记录主表';