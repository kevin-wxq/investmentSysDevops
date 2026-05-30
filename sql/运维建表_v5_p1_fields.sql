-- ========================================
-- P1 联动增强：字段扩展与变更闭环
-- 分支：codex/devops-p1-linkage-hardening
-- 执行顺序：先执行运维建表_v4.sql，再执行本脚本
-- ========================================

-- =====================
-- T5: Bug 验证闭环 — 增加验证/补丁/上线字段
-- =====================
ALTER TABLE `ht_bug_record`
    ADD COLUMN `verifier_id` bigint(20) DEFAULT NULL COMMENT '验证人ID' AFTER `test_result`,
    ADD COLUMN `verifier_name` varchar(50) DEFAULT NULL COMMENT '验证人姓名' AFTER `verifier_id`,
    ADD COLUMN `verify_time` datetime DEFAULT NULL COMMENT '验证时间' AFTER `verifier_name`,
    ADD COLUMN `verify_result` varchar(32) DEFAULT NULL COMMENT '验证结果(PASS/FAIL/REOPEN)' AFTER `verify_time`,
    ADD COLUMN `verify_detail` varchar(1000) DEFAULT NULL COMMENT '验证说明' AFTER `verify_result`,
    ADD COLUMN `patch_no` varchar(64) DEFAULT NULL COMMENT '补丁号' AFTER `verify_detail`,
    ADD COLUMN `fix_complete_time` datetime DEFAULT NULL COMMENT '修复完成时间' AFTER `actual_fix_time`,
    ADD COLUMN `online_time` datetime DEFAULT NULL COMMENT '实际上线时间' AFTER `patch_no`;

-- Bug 状态字典扩展（如未创建则插入）
INSERT IGNORE INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_time, remark) VALUES
(1, '待确认', 'WAIT_CONFIRM', 'ht_bug_status', '', 'info', '0', '0', NOW(), 'Bug初始状态'),
(2, '已确认', 'CONFIRMED', 'ht_bug_status', '', '', '0', '0', NOW(), ''),
(3, '修复中', 'FIXING', 'ht_bug_status', '', 'warning', '0', '0', NOW(), ''),
(4, '修复完成', 'FIX_COMPLETE', 'ht_bug_status', '', '', '0', '0', NOW(), ''),
(5, '待验证', 'WAIT_RETEST', 'ht_bug_status', '', 'primary', '0', '0', NOW(), ''),
(6, '验证通过', 'VERIFIED', 'ht_bug_status', '', 'success', '0', '0', NOW(), ''),
(7, '已发布', 'PUBLISHED', 'ht_bug_status', '', '', '0', '0', NOW(), ''),
(8, '已关闭', 'CLOSED', 'ht_bug_status', '', 'success', '0', '0', NOW(), ''),
(9, '已驳回', 'REJECTED', 'ht_bug_status', '', 'danger', '0', '0', NOW(), '');

-- 验证结果字典
INSERT IGNORE INTO sys_dict_type (dict_name, dict_type, status, create_time, remark) VALUES
('Bug验证结果', 'ht_verify_result', '0', NOW(), '验证通过/验证不通过/退回修复');

INSERT IGNORE INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_time, remark) VALUES
(1, '验证通过', 'PASS', 'ht_verify_result', '', 'success', '0', '0', NOW(), ''),
(2, '验证不通过', 'FAIL', 'ht_verify_result', '', 'danger', '0', '0', NOW(), ''),
(3, '退回修复', 'REOPEN', 'ht_verify_result', '', 'warning', '0', '0', NOW(), '');


-- =====================
-- T6: 需求验收增强 — 增加补丁号/分析详情/验收详情
-- =====================
ALTER TABLE `ht_requirement`
    ADD COLUMN `patch_no` varchar(64) DEFAULT NULL COMMENT '补丁号' AFTER `online_time`,
    ADD COLUMN `analysis_detail` text COMMENT '分析详情' AFTER `analysis_result`,
    ADD COLUMN `acceptance_detail` varchar(1000) DEFAULT NULL COMMENT '验收详情' AFTER `acceptance_result`;

-- 需求状态字典扩展
INSERT IGNORE INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_time, remark) VALUES
(1, '待分析', 'WAIT_ANALYSIS', 'ht_req_status', '', 'info', '0', '0', NOW(), ''),
(2, '分析中', 'ANALYZING', 'ht_req_status', '', '', '0', '0', NOW(), ''),
(3, '待确认', 'WAIT_CONFIRM', 'ht_req_status', '', 'warning', '0', '0', NOW(), ''),
(4, '商务流程中', 'IN_BUSINESS', 'ht_req_status', '', '', '0', '0', NOW(), ''),
(5, '待排期', 'WAIT_SCHEDULE', 'ht_req_status', '', '', '0', '0', NOW(), ''),
(6, '开发中', 'DEVELOPING', 'ht_req_status', '', 'primary', '0', '0', NOW(), ''),
(7, '待验收', 'WAIT_ACCEPT', 'ht_req_status', '', 'warning', '0', '0', NOW(), ''),
(8, '已上线', 'ONLINE', 'ht_req_status', '', 'success', '0', '0', NOW(), ''),
(9, '已驳回', 'REJECTED', 'ht_req_status', '', 'danger', '0', '0', NOW(), '');


-- =====================
-- T7: 变更记录 — 增加编号/状态/验证/闭环字段
-- =====================
ALTER TABLE `ops_change_record`
    ADD COLUMN `change_no` varchar(64) DEFAULT NULL COMMENT '变更编号' AFTER `id`,
    ADD COLUMN `system_name` varchar(100) DEFAULT NULL COMMENT '系统名称' AFTER `system_id`,
    ADD COLUMN `status` varchar(32) DEFAULT 'APPLYING' COMMENT '变更状态' AFTER `approver_id`,
    ADD COLUMN `executor_name` varchar(50) DEFAULT NULL COMMENT '执行人姓名' AFTER `executor_id`,
    ADD COLUMN `approver_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名' AFTER `approver_id`,
    ADD COLUMN `work_item_id` bigint(20) DEFAULT NULL COMMENT '闭环事项ID' AFTER `status`,
    ADD COLUMN `patch_no` varchar(64) DEFAULT NULL COMMENT '补丁号' AFTER `work_item_id`,
    ADD COLUMN `verify_result` varchar(32) DEFAULT NULL COMMENT '验证结果' AFTER `patch_no`,
    ADD COLUMN `verify_detail` varchar(1000) DEFAULT NULL COMMENT '验证说明' AFTER `verify_result`,
    ADD COLUMN `del_flag` char(1) DEFAULT '0' COMMENT '删除标记(0正常 1删除)' AFTER `remark`,
    ADD UNIQUE KEY `uk_change_no` (`change_no`);

-- 变更状态字典
INSERT IGNORE INTO sys_dict_type (dict_name, dict_type, status, create_time, remark) VALUES
('变更状态', 'ops_change_status', '0', NOW(), '变更记录状态流转');

INSERT IGNORE INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_time, remark) VALUES
(1, '申请中', 'APPLYING', 'ops_change_status', '', 'info', '0', '0', NOW(), ''),
(2, '待审批', 'WAIT_APPROVE', 'ops_change_status', '', 'warning', '0', '0', NOW(), ''),
(3, '已审批', 'APPROVED', 'ops_change_status', '', '', '0', '0', NOW(), ''),
(4, '执行中', 'EXECUTING', 'ops_change_status', '', 'primary', '0', '0', NOW(), ''),
(5, '待验证', 'WAIT_VERIFY', 'ops_change_status', '', 'warning', '0', '0', NOW(), ''),
(6, '已归档', 'ARCHIVED', 'ops_change_status', '', 'success', '0', '0', NOW(), ''),
(7, '已驳回', 'REJECTED', 'ops_change_status', '', 'danger', '0', '0', NOW(), '');

-- 变更验证结果字典（复用 ht_verify_result）
-- 已在 Bug 验证闭环中创建，无需重复

-- 更新变更记录表，将原有的软删除从 DELETE 改为 UPDATE（兼容新 del_flag 字段）
-- 注意：旧数据无 del_flag，查询时需兼容
