-- ========================================
-- 运维问题 → 运维记录 重构
-- ========================================

-- 1. 菜单重命名
UPDATE sys_menu SET menu_name = '运维记录' WHERE menu_id = 2380;
UPDATE sys_menu SET menu_name = '运维记录查询' WHERE menu_id = 2381;
UPDATE sys_menu SET menu_name = '运维记录新增' WHERE menu_id = 2382;
UPDATE sys_menu SET menu_name = '运维记录修改' WHERE menu_id = 2383;
UPDATE sys_menu SET menu_name = '运维记录删除' WHERE menu_id = 2384;
UPDATE sys_menu SET menu_name = '运维记录导出' WHERE menu_id = 2385;

-- 2. 菜单顺序重排：把明细模块往后排，核心闭环在前
UPDATE sys_menu SET order_num = 0 WHERE menu_id = 2330; -- 仪表盘
UPDATE sys_menu SET order_num = 1 WHERE menu_id = 2380; -- 运维记录（入口）
UPDATE sys_menu SET order_num = 2 WHERE menu_id = 2360; -- Bug管理
UPDATE sys_menu SET order_num = 3 WHERE menu_id = 2350; -- 需求管理
UPDATE sys_menu SET order_num = 4 WHERE menu_id = 2310; -- 变更记录
UPDATE sys_menu SET order_num = 5 WHERE menu_id = 2341; -- 统一事项
UPDATE sys_menu SET order_num = 6 WHERE menu_id = 2370; -- 报告中心
-- 分隔
UPDATE sys_menu SET order_num = 8 WHERE menu_id = 2261; -- 值班管理
UPDATE sys_menu SET order_num = 9 WHERE menu_id = 2270; -- 值班日志
UPDATE sys_menu SET order_num = 10 WHERE menu_id = 2280; -- 巡检记录（明细）
UPDATE sys_menu SET order_num = 11 WHERE menu_id = 2291; -- 故障记录（明细）
UPDATE sys_menu SET order_num = 12 WHERE menu_id = 2300; -- 备份记录（明细）
UPDATE sys_menu SET order_num = 13 WHERE menu_id = 2321; -- 知识库
UPDATE sys_menu SET order_num = 15 WHERE menu_id = 2211; -- 系统资产
UPDATE sys_menu SET order_num = 16 WHERE menu_id = 2202; -- 团队成员
UPDATE sys_menu SET order_num = 17 WHERE menu_id = 2220; -- 供应商

-- 3. 扩展运维记录类型字典（原 ops_issue_type）
-- 新增巡检异常/故障处理/备份操作/变更执行
INSERT IGNORE INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_time, remark) VALUES
(1, '问题处理', 'PROBLEM', 'ops_issue_type', '', 'primary', '0', '0', NOW(), '问题排查与处理'),
(2, '巡检异常', 'INSPECTION', 'ops_issue_type', '', 'warning', '0', '0', NOW(), '巡检发现异常'),
(3, '故障处理', 'FAULT', 'ops_issue_type', '', 'danger', '0', '0', NOW(), '故障应急处理'),
(4, '备份操作', 'BACKUP', 'ops_issue_type', '', 'info', '0', '0', NOW(), '备份操作记录'),
(5, '参数配置', 'CONFIG', 'ops_issue_type', '', '', '0', '0', NOW(), '参数修改配置'),
(6, '系统功能', 'FUNCTION', 'ops_issue_type', '', '', '0', '0', NOW(), '功能问题'),
(7, '数据异常', 'DATA', 'ops_issue_type', '', '', '0', '0', NOW(), '数据异常处理'),
(8, '性能问题', 'PERFORMANCE', 'ops_issue_type', '', '', '0', '0', NOW(), '性能优化'),
(9, '变更执行', 'CHANGE', 'ops_issue_type', '', '', '0', '0', NOW(), '变更执行记录'),
(10, '其他', 'OTHER', 'ops_issue_type', '', '', '0', '0', NOW(), '其他运维活动');

-- 4. 详情表增加 issue_id 外键，关联运维记录
ALTER TABLE `daily_inspection_main`
    ADD COLUMN `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID' AFTER `id`;

ALTER TABLE `ops_fault_record`
    ADD COLUMN `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID' AFTER `id`;

ALTER TABLE `ops_backup_record`
    ADD COLUMN `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID' AFTER `id`;

ALTER TABLE `ops_shift_log`
    ADD COLUMN `issue_id` bigint(20) DEFAULT NULL COMMENT '关联运维记录ID' AFTER `id`;
