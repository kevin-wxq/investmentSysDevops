-- 运维模块字典项

insert ignore into sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) values
('运维系统类型', 'ops_system_type', '0', 'admin', sysdate(), '运维系统资产类型'),
('运维重要等级', 'ops_importance_level', '0', 'admin', sysdate(), '运维系统重要程度'),
('运维系统状态', 'ops_system_status', '0', 'admin', sysdate(), '系统资产生命周期状态'),
('运维成员角色', 'ops_team_role', '0', 'admin', sysdate(), '运维团队成员角色'),
('运维在岗状态', 'ops_on_job_status', '0', 'admin', sysdate(), '团队成员在岗状态'),
('供应商服务范围', 'ops_service_scope', '0', 'admin', sysdate(), '供应商服务范围'),
('供应商SLA等级', 'ops_sla_level', '0', 'admin', sysdate(), '供应商服务SLA等级'),
('班次类型', 'ops_shift_type', '0', 'admin', sysdate(), '值班班次类型'),
('班次状态', 'ops_shift_status', '0', 'admin', sysdate(), '值班班次状态'),
('日志类型', 'ops_log_type', '0', 'admin', sysdate(), '值班日志类型'),
('严重程度', 'ops_severity', '0', 'admin', sysdate(), '严重程度'),
('是否升级', 'ops_yes_no', '0', 'admin', sysdate(), '是否类字典'),
('故障级别', 'ops_fault_level', '0', 'admin', sysdate(), '故障严重级别'),
('故障状态', 'ops_fault_status', '0', 'admin', sysdate(), '故障处理状态'),
('巡检状态', 'ops_health_status', '0', 'admin', sysdate(), '巡检指标状态'),
('巡检结果', 'ops_inspection_result', '0', 'admin', sysdate(), '巡检整体结果'),
('备份类型', 'ops_backup_type', '0', 'admin', sysdate(), '备份类型'),
('备份结果', 'ops_backup_result', '0', 'admin', sysdate(), '备份执行结果'),
('恢复验证', 'ops_restore_tested', '0', 'admin', sysdate(), '恢复验证结果'),
('变更类型', 'ops_change_type', '0', 'admin', sysdate(), '变更类型'),
('风险等级', 'ops_risk_level', '0', 'admin', sysdate(), '变更风险等级'),
('变更结果', 'ops_change_result', '0', 'admin', sysdate(), '变更执行结果'),
('任务状态', 'ops_task_status', '0', 'admin', sysdate(), '周运维任务状态'),
('运维类型', 'ops_operation_type', '0', 'admin', sysdate(), '关键运维类型'),
('执行结果', 'ops_operation_result', '0', 'admin', sysdate(), '运维执行结果'),
('知识分类', 'ops_knowledge_category', '0', 'admin', sysdate(), '知识库分类'),
('发布状态', 'ops_publish_status', '0', 'admin', sysdate(), '知识发布状态');

delete from sys_dict_data where dict_type in (
  'ops_system_type',
  'ops_importance_level',
  'ops_system_status',
  'ops_team_role',
  'ops_on_job_status',
  'ops_service_scope',
  'ops_sla_level',
  'ops_shift_type',
  'ops_shift_status',
  'ops_log_type',
  'ops_severity',
  'ops_yes_no',
  'ops_fault_level',
  'ops_fault_status',
  'ops_health_status',
  'ops_inspection_result',
  'ops_backup_type',
  'ops_backup_result',
  'ops_restore_tested',
  'ops_change_type',
  'ops_risk_level',
  'ops_change_result',
  'ops_task_status',
  'ops_operation_type',
  'ops_operation_result',
  'ops_knowledge_category',
  'ops_publish_status'
);

insert into sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(1, '核心业务', '1', 'ops_system_type', '', 'danger', 'Y', '0', 'admin', sysdate(), '核心交易、清算等业务系统'),
(2, '支撑系统', '2', 'ops_system_type', '', 'warning', 'N', '0', 'admin', sysdate(), '支撑业务运营的系统'),
(3, '基础设施', '3', 'ops_system_type', '', 'info', 'N', '0', 'admin', sysdate(), '网络、数据库、中间件等基础设施'),

(1, '核心', '1', 'ops_importance_level', '', 'danger', 'Y', '0', 'admin', sysdate(), '核心等级'),
(2, '重要', '2', 'ops_importance_level', '', 'warning', 'N', '0', 'admin', sysdate(), '重要等级'),
(3, '一般', '3', 'ops_importance_level', '', 'info', 'N', '0', 'admin', sysdate(), '一般等级'),

(1, '运行中', '1', 'ops_system_status', '', 'success', 'Y', '0', 'admin', sysdate(), '正常运行'),
(2, '停用', '2', 'ops_system_status', '', 'warning', 'N', '0', 'admin', sysdate(), '暂停使用'),
(3, '下线', '3', 'ops_system_status', '', 'info', 'N', '0', 'admin', sysdate(), '已下线'),

(1, '运维负责人', '1', 'ops_team_role', '', 'danger', 'N', '0', 'admin', sysdate(), '负责整体运维'),
(2, '值班长', '2', 'ops_team_role', '', 'warning', 'N', '0', 'admin', sysdate(), '负责值班协调'),
(3, '运维工程师', '3', 'ops_team_role', '', 'primary', 'Y', '0', 'admin', sysdate(), '负责日常运维'),
(4, '巡检员', '4', 'ops_team_role', '', 'info', 'N', '0', 'admin', sysdate(), '负责巡检执行'),

(1, '在岗', '1', 'ops_on_job_status', '', 'success', 'Y', '0', 'admin', sysdate(), '当前在岗'),
(2, '休假', '2', 'ops_on_job_status', '', 'warning', 'N', '0', 'admin', sysdate(), '休假中'),
(3, '离职', '3', 'ops_on_job_status', '', 'info', 'N', '0', 'admin', sysdate(), '已离职'),

(1, '开发支持', '1', 'ops_service_scope', '', 'primary', 'N', '0', 'admin', sysdate(), '开发与缺陷支持'),
(2, '运维支持', '2', 'ops_service_scope', '', 'success', 'Y', '0', 'admin', sysdate(), '运维保障支持'),
(3, '硬件支持', '3', 'ops_service_scope', '', 'warning', 'N', '0', 'admin', sysdate(), '硬件设备支持'),
(4, '综合支持', '4', 'ops_service_scope', '', 'info', 'N', '0', 'admin', sysdate(), '综合服务支持'),

(1, 'P1', 'P1', 'ops_sla_level', '', 'danger', 'Y', '0', 'admin', sysdate(), '最高服务等级'),
(2, 'P2', 'P2', 'ops_sla_level', '', 'warning', 'N', '0', 'admin', sysdate(), '标准服务等级'),
(3, 'P3', 'P3', 'ops_sla_level', '', 'info', 'N', '0', 'admin', sysdate(), '基础服务等级'),

(1, '白班', '1', 'ops_shift_type', '', 'success', 'Y', '0', 'admin', sysdate(), '白班'),
(2, '夜班', '2', 'ops_shift_type', '', 'primary', 'N', '0', 'admin', sysdate(), '夜班'),
(3, '节假日', '3', 'ops_shift_type', '', 'warning', 'N', '0', 'admin', sysdate(), '节假日值班'),
(1, '进行中', '1', 'ops_shift_status', '', 'success', 'Y', '0', 'admin', sysdate(), '进行中'),
(2, '已交接', '2', 'ops_shift_status', '', 'primary', 'N', '0', 'admin', sysdate(), '已交接'),
(3, '已结束', '3', 'ops_shift_status', '', 'info', 'N', '0', 'admin', sysdate(), '已结束'),

(1, '日常', '1', 'ops_log_type', '', 'info', 'Y', '0', 'admin', sysdate(), '日常日志'),
(2, '事件', '2', 'ops_log_type', '', 'primary', 'N', '0', 'admin', sysdate(), '事件记录'),
(3, '告警', '3', 'ops_log_type', '', 'warning', 'N', '0', 'admin', sysdate(), '告警记录'),
(4, '交接', '4', 'ops_log_type', '', 'success', 'N', '0', 'admin', sysdate(), '交接记录'),
(1, '普通', '1', 'ops_severity', '', 'info', 'Y', '0', 'admin', sysdate(), '普通'),
(2, '重要', '2', 'ops_severity', '', 'warning', 'N', '0', 'admin', sysdate(), '重要'),
(3, '紧急', '3', 'ops_severity', '', 'danger', 'N', '0', 'admin', sysdate(), '紧急'),
(1, '否', '0', 'ops_yes_no', '', 'info', 'Y', '0', 'admin', sysdate(), '否'),
(2, '是', '1', 'ops_yes_no', '', 'danger', 'N', '0', 'admin', sysdate(), '是'),

(1, '一般', '1', 'ops_fault_level', '', 'info', 'Y', '0', 'admin', sysdate(), '一般故障'),
(2, '严重', '2', 'ops_fault_level', '', 'warning', 'N', '0', 'admin', sysdate(), '严重故障'),
(3, '紧急', '3', 'ops_fault_level', '', 'danger', 'N', '0', 'admin', sysdate(), '紧急故障'),
(1, '发现', '1', 'ops_fault_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '已发现'),
(2, '处理中', '2', 'ops_fault_status', '', 'primary', 'N', '0', 'admin', sysdate(), '处理中'),
(3, '已恢复', '3', 'ops_fault_status', '', 'success', 'N', '0', 'admin', sysdate(), '已恢复'),
(4, '已关闭', '4', 'ops_fault_status', '', 'info', 'N', '0', 'admin', sysdate(), '已关闭'),

(1, '正常', '1', 'ops_health_status', '', 'success', 'Y', '0', 'admin', sysdate(), '正常'),
(2, '异常', '2', 'ops_health_status', '', 'danger', 'N', '0', 'admin', sysdate(), '异常'),
(3, '未检查', '3', 'ops_health_status', '', 'info', 'N', '0', 'admin', sysdate(), '未检查'),
(1, '正常', '1', 'ops_inspection_result', '', 'success', 'Y', '0', 'admin', sysdate(), '巡检正常'),
(2, '异常', '2', 'ops_inspection_result', '', 'danger', 'N', '0', 'admin', sysdate(), '巡检异常'),
(3, '已处理', '3', 'ops_inspection_result', '', 'primary', 'N', '0', 'admin', sysdate(), '异常已处理'),

(1, '全量', '1', 'ops_backup_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '全量备份'),
(2, '增量', '2', 'ops_backup_type', '', 'success', 'N', '0', 'admin', sysdate(), '增量备份'),
(3, '差异', '3', 'ops_backup_type', '', 'warning', 'N', '0', 'admin', sysdate(), '差异备份'),
(1, '成功', '1', 'ops_backup_result', '', 'success', 'Y', '0', 'admin', sysdate(), '备份成功'),
(2, '失败', '2', 'ops_backup_result', '', 'danger', 'N', '0', 'admin', sysdate(), '备份失败'),
(3, '部分成功', '3', 'ops_backup_result', '', 'warning', 'N', '0', 'admin', sysdate(), '部分成功'),
(1, '未验证', '0', 'ops_restore_tested', '', 'info', 'Y', '0', 'admin', sysdate(), '未恢复验证'),
(2, '已通过', '1', 'ops_restore_tested', '', 'success', 'N', '0', 'admin', sysdate(), '恢复验证通过'),
(3, '未通过', '2', 'ops_restore_tested', '', 'danger', 'N', '0', 'admin', sysdate(), '恢复验证未通过'),

(1, '功能发布', '1', 'ops_change_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '功能发布'),
(2, '配置调整', '2', 'ops_change_type', '', 'success', 'N', '0', 'admin', sysdate(), '配置调整'),
(3, '数据变更', '3', 'ops_change_type', '', 'warning', 'N', '0', 'admin', sysdate(), '数据变更'),
(4, '应急变更', '4', 'ops_change_type', '', 'danger', 'N', '0', 'admin', sysdate(), '应急变更'),
(1, '低', '1', 'ops_risk_level', '', 'success', 'Y', '0', 'admin', sysdate(), '低风险'),
(2, '中', '2', 'ops_risk_level', '', 'warning', 'N', '0', 'admin', sysdate(), '中风险'),
(3, '高', '3', 'ops_risk_level', '', 'danger', 'N', '0', 'admin', sysdate(), '高风险'),
(1, '成功', '1', 'ops_change_result', '', 'success', 'Y', '0', 'admin', sysdate(), '变更成功'),
(2, '失败', '2', 'ops_change_result', '', 'danger', 'N', '0', 'admin', sysdate(), '变更失败'),
(3, '已回滚', '3', 'ops_change_result', '', 'warning', 'N', '0', 'admin', sysdate(), '已回滚'),

(1, '未开始', '1', 'ops_task_status', '', 'info', 'Y', '0', 'admin', sysdate(), '未开始'),
(2, '进行中', '2', 'ops_task_status', '', 'primary', 'N', '0', 'admin', sysdate(), '进行中'),
(3, '已完成', '3', 'ops_task_status', '', 'success', 'N', '0', 'admin', sysdate(), '已完成'),
(4, '有风险', '4', 'ops_task_status', '', 'warning', 'N', '0', 'admin', sysdate(), '有风险'),
(1, '巡检', '1', 'ops_operation_type', '', 'success', 'Y', '0', 'admin', sysdate(), '巡检'),
(2, '发布', '2', 'ops_operation_type', '', 'primary', 'N', '0', 'admin', sysdate(), '发布'),
(3, '变更', '3', 'ops_operation_type', '', 'warning', 'N', '0', 'admin', sysdate(), '变更'),
(4, '故障处理', '4', 'ops_operation_type', '', 'danger', 'N', '0', 'admin', sysdate(), '故障处理'),
(1, '成功', '1', 'ops_operation_result', '', 'success', 'Y', '0', 'admin', sysdate(), '成功'),
(2, '失败', '2', 'ops_operation_result', '', 'danger', 'N', '0', 'admin', sysdate(), '失败'),
(3, '部分完成', '3', 'ops_operation_result', '', 'warning', 'N', '0', 'admin', sysdate(), '部分完成'),

(1, '故障处理', '1', 'ops_knowledge_category', '', 'danger', 'Y', '0', 'admin', sysdate(), '故障处理'),
(2, '巡检经验', '2', 'ops_knowledge_category', '', 'success', 'N', '0', 'admin', sysdate(), '巡检经验'),
(3, '变更发布', '3', 'ops_knowledge_category', '', 'primary', 'N', '0', 'admin', sysdate(), '变更发布'),
(4, '备份恢复', '4', 'ops_knowledge_category', '', 'warning', 'N', '0', 'admin', sysdate(), '备份恢复'),
(1, '草稿', '0', 'ops_publish_status', '', 'info', 'Y', '0', 'admin', sysdate(), '草稿'),
(2, '已发布', '1', 'ops_publish_status', '', 'success', 'N', '0', 'admin', sysdate(), '已发布');

-- ============================================
-- 闭环事项 / 衡泰需求 / Bug管理字典
-- ============================================
insert ignore into sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) values
('闭环事项类型', 'ops_item_type', '0', 'admin', sysdate(), '统一闭环事项来源类型'),
('闭环优先级', 'ops_item_priority', '0', 'admin', sysdate(), 'P0/P1/P2/P3优先级'),
('闭环事项状态', 'ops_item_status', '0', 'admin', sysdate(), '统一闭环事项状态'),
('闭环验收结果', 'ops_acceptance_result', '0', 'admin', sysdate(), '闭环验收结果'),
('衡泰需求部门', 'ht_dept_code', '0', 'admin', sysdate(), '需求提出部门缩写'),
('衡泰需求模块', 'ht_req_module', '0', 'admin', sysdate(), '需求所属模块'),
('衡泰需求状态', 'ht_req_status', '0', 'admin', sysdate(), '衡泰需求标准状态流转'),
('衡泰分析结果', 'ht_analysis_result', '0', 'admin', sysdate(), '厂商分析结果'),
('衡泰验收人', 'ht_acceptor', '0', 'admin', sysdate(), '固定验收人'),
('衡泰验收结果', 'ht_acceptance_result', '0', 'admin', sysdate(), '需求验收结果'),
('是否涉及商务', 'ht_business_flag', '0', 'admin', sysdate(), '商务流程标识'),
('Bug严重程度', 'ht_bug_severity', '0', 'admin', sysdate(), 'Bug严重程度'),
('Bug状态', 'ht_bug_status', '0', 'admin', sysdate(), 'Bug处理状态');

delete from sys_dict_data where dict_type in (
  'ops_item_type',
  'ops_item_priority',
  'ops_item_status',
  'ops_acceptance_result',
  'ht_dept_code',
  'ht_req_module',
  'ht_req_status',
  'ht_analysis_result',
  'ht_acceptor',
  'ht_acceptance_result',
  'ht_business_flag',
  'ht_bug_severity',
  'ht_bug_status'
);

insert into sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(1, '需求', 'REQ', 'ops_item_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '衡泰需求'),
(2, 'Bug', 'BUG', 'ops_item_type', '', 'danger', 'N', '0', 'admin', sysdate(), 'Bug缺陷'),
(3, '运维问题', 'ISSUE', 'ops_item_type', '', 'warning', 'N', '0', 'admin', sysdate(), '运维问题'),
(4, '故障', 'FAULT', 'ops_item_type', '', 'warning', 'N', '0', 'admin', sysdate(), '故障记录'),
(5, '变更', 'CHANGE', 'ops_item_type', '', 'success', 'N', '0', 'admin', sysdate(), '变更记录'),
(6, '其他', 'OTHER', 'ops_item_type', '', 'info', 'N', '0', 'admin', sysdate(), '其他事项'),

(1, 'P0 紧急', 'P0', 'ops_item_priority', '', 'danger', 'N', '0', 'admin', sysdate(), '2小时响应，24小时内提供方案'),
(2, 'P1 高', 'P1', 'ops_item_priority', '', 'warning', 'N', '0', 'admin', sysdate(), '1个工作日响应，3个工作日反馈排期'),
(3, 'P2 中', 'P2', 'ops_item_priority', '', 'primary', 'Y', '0', 'admin', sysdate(), '3个工作日响应，1周内反馈排期'),
(4, 'P3 低', 'P3', 'ops_item_priority', '', 'info', 'N', '0', 'admin', sysdate(), '纳入需求池规划'),

(1, '待处理', 'PENDING', 'ops_item_status', '', 'info', 'Y', '0', 'admin', sysdate(), '等待处理'),
(2, '处理中', 'PROCESSING', 'ops_item_status', '', 'primary', 'N', '0', 'admin', sysdate(), '处理中'),
(3, '待验收', 'ACCEPTING', 'ops_item_status', '', 'warning', 'N', '0', 'admin', sysdate(), '等待验收'),
(4, '已关闭', 'CLOSED', 'ops_item_status', '', 'success', 'N', '0', 'admin', sysdate(), '闭环完成'),
(5, '已驳回', 'REJECTED', 'ops_item_status', '', 'danger', 'N', '0', 'admin', sysdate(), '已驳回'),

(1, '通过', 'PASS', 'ops_acceptance_result', '', 'success', 'N', '0', 'admin', sysdate(), '验收通过'),
(2, '不通过', 'FAIL', 'ops_acceptance_result', '', 'danger', 'N', '0', 'admin', sysdate(), '验收不通过'),

(1, '固定收益部', 'FI', 'ht_dept_code', '', 'primary', 'Y', '0', 'admin', sysdate(), 'FI'),
(2, '上海部', 'SH', 'ht_dept_code', '', 'info', 'N', '0', 'admin', sysdate(), 'SH'),
(3, '北京部', 'BJ', 'ht_dept_code', '', 'info', 'N', '0', 'admin', sysdate(), 'BJ'),
(4, '深圳部', 'SZ', 'ht_dept_code', '', 'info', 'N', '0', 'admin', sysdate(), 'SZ'),
(5, '金融业务部', 'FIN', 'ht_dept_code', '', 'primary', 'N', '0', 'admin', sysdate(), 'FIN'),
(6, '风险管理部', 'RM', 'ht_dept_code', '', 'warning', 'N', '0', 'admin', sysdate(), 'RM'),
(7, '运营管理部', 'OM', 'ht_dept_code', '', 'success', 'N', '0', 'admin', sysdate(), 'OM'),
(8, '资金托管部', 'TD', 'ht_dept_code', '', 'primary', 'N', '0', 'admin', sysdate(), 'TD'),

(1, '交易', 'TRADE', 'ht_req_module', '', 'primary', 'Y', '0', 'admin', sysdate(), '交易模块'),
(2, '估值', 'VALUATION', 'ht_req_module', '', 'success', 'N', '0', 'admin', sysdate(), '估值模块'),
(3, '清算', 'CLEARING', 'ht_req_module', '', 'warning', 'N', '0', 'admin', sysdate(), '清算模块'),
(4, '对手管理', 'COUNTERPARTY', 'ht_req_module', '', 'info', 'N', '0', 'admin', sysdate(), '对手管理'),
(5, '报表', 'REPORT', 'ht_req_module', '', 'primary', 'N', '0', 'admin', sysdate(), '报表模块'),
(6, '权限', 'AUTH', 'ht_req_module', '', 'danger', 'N', '0', 'admin', sysdate(), '权限模块'),
(7, '其他', 'OTHER', 'ht_req_module', '', 'info', 'N', '0', 'admin', sysdate(), '其他模块'),

(1, '待分析', 'WAIT_ANALYSIS', 'ht_req_status', '', 'info', 'Y', '0', 'admin', sysdate(), '待分析'),
(2, '分析中', 'ANALYZING', 'ht_req_status', '', 'primary', 'N', '0', 'admin', sysdate(), '分析中'),
(3, '待确认', 'WAIT_CONFIRM', 'ht_req_status', '', 'warning', 'N', '0', 'admin', sysdate(), '待确认'),
(4, '商务流程中', 'BUSINESS_PROCESS', 'ht_req_status', '', 'warning', 'N', '0', 'admin', sysdate(), '商务流程中'),
(5, '待排期', 'WAIT_SCHEDULE', 'ht_req_status', '', 'info', 'N', '0', 'admin', sysdate(), '待排期'),
(6, '开发中', 'DEVELOPING', 'ht_req_status', '', 'primary', 'N', '0', 'admin', sysdate(), '开发中'),
(7, '待验收', 'WAIT_ACCEPT', 'ht_req_status', '', 'warning', 'N', '0', 'admin', sysdate(), '待验收'),
(8, '已上线', 'ONLINE', 'ht_req_status', '', 'success', 'N', '0', 'admin', sysdate(), '已上线'),
(9, '已驳回', 'REJECTED', 'ht_req_status', '', 'danger', 'N', '0', 'admin', sysdate(), '已驳回'),

(1, '可行', 'FEASIBLE', 'ht_analysis_result', '', 'success', 'N', '0', 'admin', sysdate(), '可行'),
(2, '不可行', 'INFEASIBLE', 'ht_analysis_result', '', 'danger', 'N', '0', 'admin', sysdate(), '不可行'),
(3, '需调整', 'ADJUST', 'ht_analysis_result', '', 'warning', 'N', '0', 'admin', sysdate(), '需调整'),

(1, '韩宝国', 'HAN_BAOGUO', 'ht_acceptor', '', 'primary', 'N', '0', 'admin', sysdate(), '韩宝国'),
(2, '余如飞', 'YU_RUFEI', 'ht_acceptor', '', 'primary', 'N', '0', 'admin', sysdate(), '余如飞'),

(1, '通过', 'PASS', 'ht_acceptance_result', '', 'success', 'N', '0', 'admin', sysdate(), '通过'),
(2, '不通过', 'FAIL', 'ht_acceptance_result', '', 'danger', 'N', '0', 'admin', sysdate(), '不通过'),
(1, '否', '0', 'ht_business_flag', '', 'info', 'Y', '0', 'admin', sysdate(), '不涉及商务'),
(2, '是', '1', 'ht_business_flag', '', 'danger', 'N', '0', 'admin', sysdate(), '涉及商务流程'),

(1, 'S0 阻断', 'S0', 'ht_bug_severity', '', 'danger', 'N', '0', 'admin', sysdate(), '核心流程阻断'),
(2, 'S1 严重', 'S1', 'ht_bug_severity', '', 'warning', 'N', '0', 'admin', sysdate(), '重要功能异常'),
(3, 'S2 一般', 'S2', 'ht_bug_severity', '', 'primary', 'Y', '0', 'admin', sysdate(), '一般缺陷'),
(4, 'S3 轻微', 'S3', 'ht_bug_severity', '', 'info', 'N', '0', 'admin', sysdate(), '体验或提示问题'),

(1, '待确认', 'WAIT_CONFIRM', 'ht_bug_status', '', 'info', 'Y', '0', 'admin', sysdate(), '待确认'),
(2, '修复中', 'FIXING', 'ht_bug_status', '', 'primary', 'N', '0', 'admin', sysdate(), '修复中'),
(3, '待回归', 'WAIT_RETEST', 'ht_bug_status', '', 'warning', 'N', '0', 'admin', sysdate(), '待回归测试'),
(4, '已关闭', 'CLOSED', 'ht_bug_status', '', 'success', 'N', '0', 'admin', sysdate(), '已关闭'),
(5, '已驳回', 'REJECTED', 'ht_bug_status', '', 'danger', 'N', '0', 'admin', sysdate(), '已驳回');

-- ============================================
-- 运维问题 / 事项关系字典
-- ============================================
insert ignore into sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) values
('运维问题类型', 'ops_issue_type', '0', 'admin', sysdate(), '运维问题分类'),
('运维问题状态', 'ops_issue_status', '0', 'admin', sysdate(), '运维问题处理状态'),
('运维处理方式', 'ops_handle_method', '0', 'admin', sysdate(), '运维问题处理方式'),
('运维根因分类', 'ops_root_cause', '0', 'admin', sysdate(), '运维问题根因分类'),
('事项关系类型', 'ops_relation_type', '0', 'admin', sysdate(), '事项关系类型');

delete from sys_dict_data where dict_type in (
  'ops_issue_type',
  'ops_issue_status',
  'ops_handle_method',
  'ops_root_cause',
  'ops_relation_type'
);

insert into sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(1, '参数配置', 'CONFIG', 'ops_issue_type', '', 'primary', 'N', '0', 'admin', sysdate(), '参数配置类问题'),
(2, '系统功能', 'FUNCTION', 'ops_issue_type', '', 'success', 'N', '0', 'admin', sysdate(), '系统功能类问题'),
(3, '数据异常', 'DATA', 'ops_issue_type', '', 'warning', 'N', '0', 'admin', sysdate(), '数据异常类问题'),
(4, '操作流程', 'PROCESS', 'ops_issue_type', '', 'info', 'N', '0', 'admin', sysdate(), '操作流程类问题'),
(5, '性能', 'PERFORMANCE', 'ops_issue_type', '', 'danger', 'N', '0', 'admin', sysdate(), '性能类问题'),
(6, '权限', 'AUTH', 'ops_issue_type', '', 'warning', 'N', '0', 'admin', sysdate(), '权限类问题'),
(7, '其他', 'OTHER', 'ops_issue_type', '', 'info', 'Y', '0', 'admin', sysdate(), '其他问题'),

(1, '待受理', 'PENDING', 'ops_issue_status', '', 'info', 'Y', '0', 'admin', sysdate(), '等待受理'),
(2, '分析中', 'ANALYZING', 'ops_issue_status', '', 'primary', 'N', '0', 'admin', sysdate(), '问题分析中'),
(3, '处理中', 'PROCESSING', 'ops_issue_status', '', 'primary', 'N', '0', 'admin', sysdate(), '问题处理中'),
(4, '待验证', 'WAIT_VERIFY', 'ops_issue_status', '', 'warning', 'N', '0', 'admin', sysdate(), '等待验证'),
(5, '已转Bug', 'CONVERTED_BUG', 'ops_issue_status', '', 'danger', 'N', '0', 'admin', sysdate(), '已转为Bug'),
(6, '已转需求', 'CONVERTED_REQ', 'ops_issue_status', '', 'success', 'N', '0', 'admin', sysdate(), '已转为需求'),
(7, '已转变更', 'CONVERTED_CHANGE', 'ops_issue_status', '', 'warning', 'N', '0', 'admin', sysdate(), '已转为变更'),
(8, '已关闭', 'CLOSED', 'ops_issue_status', '', 'success', 'N', '0', 'admin', sysdate(), '已关闭'),
(9, '已驳回', 'REJECTED', 'ops_issue_status', '', 'danger', 'N', '0', 'admin', sysdate(), '已驳回'),

(1, '修改配置', 'CONFIG_CHANGE', 'ops_handle_method', '', 'primary', 'N', '0', 'admin', sysdate(), '通过修改配置处理'),
(2, '修改数据库', 'DB_CHANGE', 'ops_handle_method', '', 'warning', 'N', '0', 'admin', sysdate(), '通过修改数据库处理'),
(3, '操作处理', 'OPERATION', 'ops_handle_method', '', 'success', 'Y', '0', 'admin', sysdate(), '通过运维操作处理'),
(4, '转Bug', 'TO_BUG', 'ops_handle_method', '', 'danger', 'N', '0', 'admin', sysdate(), '转为Bug处理'),
(5, '转需求', 'TO_REQ', 'ops_handle_method', '', 'primary', 'N', '0', 'admin', sysdate(), '转为需求处理'),
(6, '转变更', 'TO_CHANGE', 'ops_handle_method', '', 'warning', 'N', '0', 'admin', sysdate(), '转为变更处理'),
(7, '无需处理', 'NO_ACTION', 'ops_handle_method', '', 'info', 'N', '0', 'admin', sysdate(), '无需处理'),
(8, '观察跟踪', 'FOLLOW_UP', 'ops_handle_method', '', 'info', 'N', '0', 'admin', sysdate(), '继续观察跟踪'),

(1, '配置', 'CONFIG', 'ops_root_cause', '', 'primary', 'N', '0', 'admin', sysdate(), '配置原因'),
(2, '数据', 'DATA', 'ops_root_cause', '', 'warning', 'N', '0', 'admin', sysdate(), '数据原因'),
(3, '程序', 'PROGRAM', 'ops_root_cause', '', 'danger', 'N', '0', 'admin', sysdate(), '程序原因'),
(4, '操作', 'OPERATION', 'ops_root_cause', '', 'info', 'N', '0', 'admin', sysdate(), '操作原因'),
(5, '环境', 'ENVIRONMENT', 'ops_root_cause', '', 'warning', 'N', '0', 'admin', sysdate(), '环境原因'),
(6, '权限', 'AUTH', 'ops_root_cause', '', 'primary', 'N', '0', 'admin', sysdate(), '权限原因'),
(7, '业务规则', 'BUSINESS_RULE', 'ops_root_cause', '', 'success', 'N', '0', 'admin', sysdate(), '业务规则原因'),

(1, '由此产生', 'GENERATE', 'ops_relation_type', '', 'primary', 'N', '0', 'admin', sysdate(), '由来源事项产生目标事项'),
(2, '转为', 'CONVERT_TO', 'ops_relation_type', '', 'success', 'N', '0', 'admin', sysdate(), '来源事项转为目标事项'),
(3, '关联', 'RELATE_TO', 'ops_relation_type', '', 'info', 'Y', '0', 'admin', sysdate(), '普通关联'),
(4, '阻塞', 'BLOCKS', 'ops_relation_type', '', 'danger', 'N', '0', 'admin', sysdate(), '来源事项阻塞目标事项'),
(5, '被阻塞', 'BLOCKED_BY', 'ops_relation_type', '', 'warning', 'N', '0', 'admin', sysdate(), '来源事项被目标事项阻塞'),
(6, '重复', 'DUPLICATE', 'ops_relation_type', '', 'info', 'N', '0', 'admin', sysdate(), '重复事项'),
(7, '父子', 'PARENT_CHILD', 'ops_relation_type', '', 'primary', 'N', '0', 'admin', sysdate(), '父子拆分关系'),
(8, '由发布上线', 'RELEASED_BY', 'ops_relation_type', '', 'success', 'N', '0', 'admin', sysdate(), '由发布上线'),
(9, '由变更实施', 'CHANGED_BY', 'ops_relation_type', '', 'warning', 'N', '0', 'admin', sysdate(), '由变更实施');
