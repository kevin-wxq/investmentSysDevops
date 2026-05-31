# 投资系统运维平台 — 数据库设计文档

> 数据库: `devops` | MySQL 5.7+ | 字符集: utf8mb4  
> 分支: `codex/devops-p1-linkage-hardening`

---

## 一、业务表一览

| 序号 | 表名 | 说明 | 核心关联 |
|------|------|------|----------|
| 1 | `ops_team_member` | 团队成员 | — |
| 2 | `ops_system_asset` | 系统资产 | 被所有业务表引用 |
| 3 | `ops_vendor_contact` | 供应商 | system_id→ops_system_asset |
| 4 | `ops_duty_shift` | 值班管理 | — |
| 5 | `ops_shift_log` | 值班日志 | shift_id→ops_duty_shift, issue_id→ops_issue |
| 6 | `daily_inspection_main` | 巡检记录 | system_id→ops_system_asset, issue_id→ops_issue |
| 7 | `ops_fault_record` | 故障记录 | system_id→ops_system_asset, issue_id→ops_issue |
| 8 | `ops_backup_record` | 备份记录 | system_id→ops_system_asset, issue_id→ops_issue |
| 9 | `ops_change_record` | 变更记录 | system_id→ops_system_asset, work_item_id→ops_work_item |
| 10 | `ops_knowledge` | 知识库 | system_id→ops_system_asset |
| 11 | `ops_knowledge_attach` | 知识库附件 | knowledge_id→ops_knowledge |
| 12 | `ops_knowledge_view_log` | 浏览记录 | knowledge_id→ops_knowledge |
| 13 | `ops_work_item` | 统一事项（核心聚合表） | source_module+source_id→各业务表 |
| 14 | `ops_work_item_log` | 事项操作日志 | work_item_id→ops_work_item |
| 15 | `ht_requirement` | 需求记录 | system_id→ops_system_asset, work_item_id→ops_work_item |
| 16 | `ht_bug_record` | Bug 记录 | system_id→ops_system_asset, work_item_id→ops_work_item |
| 17 | `ops_issue` | 运维记录（统一入口） | system_id→ops_system_asset, work_item_id→ops_work_item |
| 18 | `ops_item_relation` | 事项关系 | source/target 指向各业务表 |
| 19 | `weekly_operation_task` | 周报主表 | — |
| 20 | `weekly_key_operation_detail` | 周报明细 | task_id→weekly_operation_task |

---

## 二、核心数据流

```
                      ┌──────────────────┐
                      │  ops_system_asset │ ← 系统资产（基础数据）
                      └────────┬─────────┘
                               │ system_id
          ┌────────────────────┼──────────────────────────┐
          ▼                    ▼                          ▼
  ┌──────────────┐   ┌──────────────┐   ┌──────────────────┐
  │  ops_issue   │   │ht_bug_record │   │  ht_requirement  │
  │  (运维记录)   │   │   (Bug)      │   │    (需求)         │
  └──────┬───────┘   └──────┬───────┘   └────────┬─────────┘
         │      转Bug/转需求/转变更               │
         │                   │                    │
         └───────────────────┼────────────────────┘
                             │ work_item_id
                             ▼
                    ┌────────────────┐
                    │ ops_work_item  │ ← 统一闭环事项（聚合层）
                    └───────┬────────┘
                            │
                    ┌───────┴────────┐
                    ▼                ▼
          ┌───────────────┐  ┌─────────────────┐
          │ops_work_item  │  │ ops_item_relation│
          │    _log       │  │ (关系链)          │
          └───────────────┘  └─────────────────┘

  细节联动：
  ops_issue ←── issue_id ──→ daily_inspection_main
  ops_issue ←── issue_id ──→ ops_fault_record
  ops_issue ←── issue_id ──→ ops_backup_record
  ops_issue ←── issue_id ──→ ops_shift_log
  ops_fault_record ──→ ops_knowledge（关闭后沉淀为知识库文章）
```

---

## 三、各表字段说明

### 3.1 ops_issue（运维记录）— 统一运维入口

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| issue_no | varchar(64) | 记录编号（自动生成） |
| issue_type | varchar(32) | 记录类型（字典: ops_issue_type — 12 种） |
| system_id | bigint | 关联系统 |
| system_name | varchar(100) | 系统名称 |
| module_code | varchar(32) | 所属模块 |
| issue_title | varchar(200) | 标题 |
| issue_desc | text | 描述 |
| source_type | varchar(32) | 发现来源 |
| impact_scope | varchar(500) | 影响范围 |
| priority | varchar(16) | 优先级（P0-P3） |
| founder | varchar(50) | 发现人 |
| found_time | datetime | 发现时间 |
| owner_id | bigint | 负责人 ID |
| owner_name | varchar(50) | 负责人 |
| status | varchar(32) | 状态（字典: ops_issue_status） |
| handle_method | varchar(32) | 处理方式（字典: ops_handle_method） |
| handle_result | text | 处理结果 |
| root_cause | varchar(32) | 根因分类（字典: ops_root_cause） |
| work_item_id | bigint | 关联统一事项 ID |
| related_bug_no | varchar(64) | 关联 Bug 编号 |
| related_req_no | varchar(64) | 关联需求编号 |
| related_change_no | varchar(64) | 关联变更编号 |
| plan_finish_time | datetime | 计划完成时间 |
| actual_finish_time | datetime | 实际完成时间 |
| overdue_flag | char(1) | 是否逾期 |
| del_flag | char(1) | 删除标记 |

### 3.2 ht_bug_record（Bug 记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| bug_no | varchar(64) | Bug 编号（自动生成） |
| requirement_id | bigint | 关联需求 ID |
| requirement_no | varchar(64) | 关联需求编号 |
| system_id | bigint | 关联系统 |
| system_name | varchar(100) | 系统名称 |
| bug_title | varchar(200) | Bug 标题 |
| bug_desc | text | Bug 描述 |
| severity | varchar(16) | 严重程度（S0-S3） |
| priority | varchar(16) | 优先级（P0-P3） |
| status | varchar(32) | 状态（字典: ht_bug_status — 9 种） |
| founder | varchar(50) | 发现人 |
| found_time | datetime | 发现时间 |
| owner_id | bigint | 负责人 ID |
| owner_name | varchar(50) | 负责人 |
| fix_plan | text | 修复计划 |
| fix_result | text | 修复结果 |
| plan_fix_time | date | 计划修复时间 |
| actual_fix_time | date | 实际修复时间 |
| fix_complete_time | datetime | 修复完成时间 |
| tester | varchar(50) | 测试人 |
| test_result | varchar(32) | 测试结果 |
| verifier_id | bigint | 验证人 ID |
| verifier_name | varchar(50) | 验证人 |
| verify_time | datetime | 验证时间 |
| verify_result | varchar(32) | 验证结果（PASS/FAIL/REOPEN） |
| verify_detail | varchar(1000) | 验证说明 |
| patch_no | varchar(64) | 补丁号 |
| online_time | datetime | 实际上线时间 |
| close_time | datetime | 关闭时间 |
| close_desc | varchar(1000) | 关闭说明 |
| work_item_id | bigint | 关联统一事项 ID |

### 3.3 ht_requirement（需求记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| req_no | varchar(64) | 需求编号（格式: FI-REQ-YYYYMMDD-XXX） |
| dept_code | varchar(16) | 提出部门（FI/SH/BJ/SZ/FIN/RM/OM/TD） |
| req_name | varchar(200) | 需求名称 |
| priority | varchar(16) | 优先级（P0-P3） |
| module_code | varchar(32) | 所属模块（TRADE/VALUATION/CLEARING/...） |
| system_id | bigint | 关联系统 |
| system_name | varchar(100) | 系统名称 |
| req_desc | text | 需求描述 |
| business_value | text | 业务价值 |
| business_flag | char(1) | 是否涉及商务（0/1） |
| submitter | varchar(50) | 提出人 |
| submit_time | datetime | 提出时间 |
| expected_online_time | date | 预计上线时间 |
| vendor_analyst | varchar(50) | 厂商分析人 |
| analysis_result | varchar(32) | 分析结果（FEASIBLE/INFEASIBLE/ADJUST） |
| analysis_detail | text | 分析详情 |
| plan_schedule_time | date | 计划排期时间 |
| dev_finish_time | date | 开发完成时间 |
| acceptor | varchar(32) | 验收人（HAN_BAOGUO/YU_RUFEI） |
| acceptance_result | varchar(32) | 验收结果（PASS/FAIL） |
| acceptance_detail | varchar(1000) | 验收详情 |
| online_time | date | 上线时间 |
| patch_no | varchar(64) | 补丁号 |
| status | varchar(32) | 状态（字典: ht_req_status — 9 种） |
| progress | text | 处理进展 |
| work_item_id | bigint | 关联统一事项 ID |

### 3.4 ops_work_item（统一事项）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| item_no | varchar(64) | 事项编号（唯一） |
| item_type | varchar(32) | 事项类型（REQ/BUG/ISSUE/FAULT/CHANGE/OTHER） |
| source_module | varchar(64) | 来源模块（ops_issue/ht_bug/ht_requirement/ops_change） |
| source_id | bigint | 来源记录 ID |
| system_id | bigint | 关联系统 |
| system_name | varchar(100) | 系统名称 |
| title | varchar(200) | 标题 |
| priority | varchar(16) | 优先级 |
| status | varchar(32) | 状态（PENDING/PROCESSING/ACCEPTING/CLOSED/REJECTED） |
| owner_id | bigint | 负责人 ID |
| owner_name | varchar(50) | 负责人 |
| submitter | varchar(50) | 提出人 |
| submit_time | datetime | 提出时间 |
| plan_finish_time | datetime | 计划完成时间 |
| actual_finish_time | datetime | 实际完成时间 |
| progress | text | 处理进展 |
| acceptance_result | varchar(32) | 验收结果 |
| close_desc | varchar(1000) | 关闭说明 |
| overdue_flag | char(1) | 是否逾期 |

### 3.5 ops_item_relation（事项关系）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| source_type | varchar(32) | 来源对象类型 |
| source_id | bigint | 来源对象 ID |
| source_no | varchar(64) | 来源编号 |
| target_type | varchar(32) | 目标对象类型 |
| target_id | bigint | 目标对象 ID |
| target_no | varchar(64) | 目标编号 |
| relation_type | varchar(32) | 关系类型（GENERATE/CONVERT_TO/RELATE_TO/BLOCKS/...） |
| relation_desc | varchar(1000) | 关系说明 |
| relation_time | datetime | 关联时间 |

### 3.6 ops_change_record（变更记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| change_no | varchar(64) | 变更编号（唯一） |
| system_id | bigint | 关联系统 |
| system_name | varchar(100) | 系统名称 |
| change_type | char(1) | 变更类型 |
| change_title | varchar(200) | 变更标题 |
| change_desc | text | 变更描述 |
| risk_level | char(1) | 风险等级（1低 2中 3高） |
| change_content | text | 变更内容详情 |
| rollback_plan | text | 回滚方案 |
| change_time | datetime | 执行时间 |
| executor_id | bigint | 执行人 ID |
| executor_name | varchar(50) | 执行人 |
| change_result | char(1) | 执行结果 |
| approver_id | bigint | 审批人 ID |
| approver_name | varchar(50) | 审批人 |
| status | varchar(32) | 状态（APPLYING→...→ARCHIVED） |
| work_item_id | bigint | 闭环事项 ID |
| patch_no | varchar(64) | 补丁号 |
| verify_result | varchar(32) | 验证结果 |
| verify_detail | varchar(1000) | 验证说明 |

### 3.7 ops_knowledge（知识库）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| system_id | bigint | 关联系统 |
| fault_id | bigint | 来源故障记录 ID |
| title | varchar(200) | 知识标题 |
| category | char(1) | 分类（1故障处理 2操作手册 3应急预案 4FAQ） |
| tags | varchar(500) | 标签 |
| problem_desc | text | 问题描述 |
| solution_desc | text | 解决方案 |
| applicable_env | varchar(200) | 适用环境 |
| author_id | bigint | 作者 ID |
| view_count | int | 浏览计数 |
| is_published | char(1) | 发布状态（0草稿 1已发布） |

### 3.8 其余表

**ops_knowledge_attach**: knowledge_id, file_name, file_path, file_size, file_type, create_by, create_time

**ops_knowledge_view_log**: knowledge_id, viewer_id, viewer_name, view_time

**ops_work_item_log**: work_item_id, item_no, from_status, to_status, action_name, operator_name, action_time, action_remark

**ops_system_asset**: system_code, system_name, system_type(1/2/3), importance_level(1/2/3), system_url, server_ip, db_type, dev_lang, department, business_owner, tech_owner_id, go_live_date, system_status(1/2/3)

**ops_team_member**: user_id, real_name, employee_no, mobile, email, team, role_type(1/2/3/4), skill_tags, is_on_job(1/2/3), entry_date

**ops_vendor_contact**: system_id, vendor_name, contact_person, contact_phone, contact_email, service_scope, contract_no, contract_start, contract_end, sla_level

**ops_duty_shift**: shift_name, shift_date, duty_date, shift_type(1/2/3), leader_id, leader_name, duty_member_id, member_ids, backup_member_id, duty_phone, handover_notes, shift_status(1/2/3)

**ops_shift_log**: issue_id, shift_id, log_time, log_type(1/2/3/4), log_content, severity(1/2/3), handover_from_id, handover_to_id, handover_time, running_status, pending_items, important_notice, asset_status, handover_confirm, handler_id, handler_name, handling_result, is_escalated

**daily_inspection_main**: issue_id, report_id, system_id, inspection_date, inspection_time, inspector_id, inspector_name, system_name, server_ip, ip_address, system_availability, response_time, connectivity_status, service_process_name, process_status, port_status, cpu_usage, memory_usage, disk_usage, disk_io_status, network_status, network_traffic, alarm_count, alarm_info, action_taken, inspection_result, handle_measures, handler_id, handler_name, handle_time, handle_result

**ops_fault_record**: issue_id, system_id, system_name, fault_level(1/2/3), fault_title, fault_desc, root_cause, solution, preventive_measures, affected_scope, occur_time, discoverer_id, discoverer_name, recover_time, downtime_minutes, duration_minutes, handler_id, handler_name, fault_status(1/2/3/4), is_emergency, knowledge_id

**ops_backup_record**: issue_id, system_id, backup_type(1/2/3/4), backup_time, backup_size_mb, backup_result(1/2), backup_path, restore_tested(0/1/2), executor_id, error_msg

---

## 四、状态映射表

### Bug 状态 ↔ 统一事项状态

| ht_bug_status | ops_work_item.status |
|---------------|---------------------|
| WAIT_CONFIRM | PENDING |
| CONFIRMED | PENDING |
| FIXING | PROCESSING |
| FIX_COMPLETE | PROCESSING |
| WAIT_RETEST | ACCEPTING |
| VERIFIED | CLOSED |
| PUBLISHED | CLOSED |
| CLOSED | CLOSED |
| REJECTED | REJECTED |

### 需求状态 ↔ 统一事项状态

| ht_req_status | ops_work_item.status |
|---------------|---------------------|
| WAIT_ANALYSIS | PENDING |
| ANALYZING | PROCESSING |
| WAIT_CONFIRM | PENDING |
| IN_BUSINESS | PROCESSING |
| WAIT_SCHEDULE | PENDING |
| DEVELOPING | PROCESSING |
| WAIT_ACCEPT | ACCEPTING |
| ONLINE | CLOSED |
| REJECTED | REJECTED |

### 运维记录状态 ↔ 统一事项状态

| ops_issue.status | ops_work_item.status |
|------------------|---------------------|
| PENDING | PENDING |
| ANALYZING | PROCESSING |
| PROCESSING | PROCESSING |
| WAIT_VERIFY | ACCEPTING |
| CLOSED | CLOSED |
| REJECTED | REJECTED |
| CONVERTED_BUG/CONVERTED_REQ/CONVERTED_CHANGE | 跟随目标 |

---

## 五、编号规则

| 模块 | 编号格式 | 示例 |
|------|----------|------|
| 运维记录 | ISS-YYYYMMDD-XXX | ISS-20260531-001 |
| Bug | BUG-YYYYMMDD-XXX | BUG-20260531-001 |
| 需求 | [部门]-REQ-YYYYMMDD-XXX | FI-REQ-20260531-001 |
| 变更 | CHG-YYYYMMDD-XXX | CHG-20260531-001 |
| 统一事项 | WI-YYYYMMDD-XXX | WI-20260531-001 |
| 系统资产 | SYS-XXX | SYS-001 |

---

## 六、索引设计要点

各业务表上的索引主要关注以下查询路径：

1. **按系统查询** — 所有业务表都有 `system_id` 索引
2. **按状态过滤** — `ops_issue.status`, `ht_bug_record.status`, `ht_requirement.status`, `ops_work_item.status`
3. **按优先级** — `priority` 索引
4. **按时间范围** — `found_time`, `submit_time`, `create_time` 索引
5. **闭环关联** — `work_item_id` 索引（所有闭环表）
6. **唯一性** — `issue_no`, `bug_no`, `req_no`, `item_no`, `system_code`, `change_no` 均为唯一索引
