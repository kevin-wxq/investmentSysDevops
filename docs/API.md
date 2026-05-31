# 投资系统运维平台 — API 接口文档

> 基础路径: `http://localhost:8080`  
> 认证方式: JWT Token (Header: `Authorization: Bearer <token>`)  
> 登录接口: `POST /login` — 参数 `{ username, password, code, uuid }`

---

## 1. 运维记录 /ops/issue

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/issue/list` | `ops:issue:list` | 分页查询（参数: pageNum, pageSize, status, issueType, systemId 等） |
| GET | `/ops/issue/{id}` | `ops:issue:query` | 获取详情 |
| GET | `/ops/issue/next-issue-no` | `ops:issue:query` | 获取下一个记录编号 |
| POST | `/ops/issue/add` | `ops:issue:add` | 新增记录 |
| PUT | `/ops/issue/edit` | `ops:issue:edit` | 修改记录 |
| DELETE | `/ops/issue/{ids}` | `ops:issue:remove` | 删除（支持逗号分隔批量） |
| POST | `/ops/issue/export` | `ops:issue:export` | 导出 Excel |
| POST | `/ops/issue/{id}/convert/bug` | `ops:issue:convert` | 转为 Bug |
| POST | `/ops/issue/{id}/convert/requirement` | `ops:issue:convert` | 转为需求 |
| POST | `/ops/issue/{id}/convert/change` | `ops:issue:convert` | 转为变更 |

---

## 2. Bug 管理 /ops/bug

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/bug/list` | `ht:bug:list` | 分页查询 |
| GET | `/ops/bug/{id}` | `ht:bug:query` | 获取详情 |
| GET | `/ops/bug/next-bug-no` | `ht:bug:query` | 获取下一个 Bug 编号 |
| POST | `/ops/bug/add` | `ht:bug:add` | 新增 Bug |
| PUT | `/ops/bug/edit` | `ht:bug:edit` | 修改 Bug |
| DELETE | `/ops/bug/{ids}` | `ht:bug:remove` | 删除 |
| POST | `/ops/bug/export` | `ht:bug:export` | 导出 Excel |
| POST | `/ops/bug/{id}/close` | `ht:bug:edit` | 关闭 Bug |

**关键字段**: `bug_no`（自动生成）, `severity`（S0-S3）, `status`（WAIT_CONFIRM→CONFIRMED→FIXING→FIX_COMPLETE→WAIT_RETEST→VERIFIED→PUBLISHED→CLOSED）, `verifier_name`, `verify_result`, `patch_no`, `online_time`

---

## 3. 需求管理 /ops/requirement

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/requirement/list` | `ht:requirement:list` | 分页查询 |
| GET | `/ops/requirement/{id}` | `ht:requirement:query` | 获取详情 |
| GET | `/ops/requirement/next-req-no` | `ht:requirement:query` | 获取下一个需求编号 |
| POST | `/ops/requirement/add` | `ht:requirement:add` | 新增需求 |
| PUT | `/ops/requirement/edit` | `ht:requirement:edit` | 修改需求 |
| DELETE | `/ops/requirement/{ids}` | `ht:requirement:remove` | 删除 |
| POST | `/ops/requirement/export` | `ht:requirement:export` | 导出 Excel |

**关键字段**: `req_no`（格式: FI-REQ-YYYYMMDD-XXX）, `status`（WAIT_ANALYSIS→ANALYZING→WAIT_CONFIRM→IN_BUSINESS→WAIT_SCHEDULE→DEVELOPING→WAIT_ACCEPT→ONLINE）, `acceptor`（HAN_BAOGUO / YU_RUFEI）, `patch_no`, `analysis_detail`, `acceptance_detail`

---

## 4. 变更记录 /ops/change

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/change/list` | `ops:change:list` | 分页查询 |
| GET | `/ops/change/{id}` | `ops:change:query` | 获取详情 |
| GET | `/ops/change/next-change-no` | `ops:change:query` | 获取下一个变更编号 |
| POST | `/ops/change/add` | `ops:change:add` | 新增变更 |
| PUT | `/ops/change/edit` | `ops:change:edit` | 修改变更 |
| DELETE | `/ops/change/{ids}` | `ops:change:remove` | 删除 |
| POST | `/ops/change/export` | `ops:change:export` | 导出 Excel |

**关键字段**: `change_no`（自动生成）, `status`（APPLYING→WAIT_APPROVE→APPROVED→EXECUTING→WAIT_VERIFY→ARCHIVED）, `patch_no`, `verify_result`

---

## 5. 统一事项 /ops/work-item

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/work-item/list` | `ops:work-item:list` | 分页查询（自动聚合所有模块来源） |
| GET | `/ops/work-item/{id}` | `ops:work-item:query` | 获取详情（含操作日志） |

> 统一事项是**只读**视图，不支持新增/修改/删除。数据自动从各业务模块同步。

---

## 6. 事项关系 /ops/item-relation

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/item-relation/list` | `ops:item-relation:list` | 分页查询 |
| GET | `/ops/item-relation/{id}` | `ops:item-relation:query` | 获取详情 |

> 关系类型: GENERATE / CONVERT_TO / RELATE_TO / BLOCKS / BLOCKED_BY / DUPLICATE / PARENT_CHILD

---

## 7. 报告中心 /ops/report

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/report/work-item/stats` | `ops:report:view` | 按时间范围获取汇总统计 |
| POST | `/ops/report/work-item/export-word` | `ops:report:export` | 导出 Word 报告 |

**导出请求体**: `{ "startTime": "2026-05-01", "endTime": "2026-05-31" }`

---

## 8. 运维知识库 /ops/knowledge

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/knowledge/list` | `ops:knowledge:list` | 分页查询 |
| GET | `/ops/knowledge/{id}` | `ops:knowledge:query` | 获取详情（自动 +1 浏览次数，记录浏览日志） |
| POST | `/ops/knowledge/add` | `ops:knowledge:add` | 新增知识 |
| PUT | `/ops/knowledge/edit` | `ops:knowledge:edit` | 修改知识 |
| DELETE | `/ops/knowledge/{ids}` | `ops:knowledge:remove` | 删除 |
| POST | `/ops/knowledge/export` | `ops:knowledge:export` | 导出 Excel |
| PUT | `/ops/knowledge/{id}/view` | `ops:knowledge:query` | 仅 +1 浏览次数（不返回详情） |

---

## 9. 知识库附件 /ops/knowledge-attach

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/knowledge-attach/list/{knowledgeId}` | `ops:knowledge:query` | 获取知识库的附件列表 |
| POST | `/ops/knowledge-attach/upload/{knowledgeId}` | `ops:knowledge:edit` | 上传附件（multipart） |
| DELETE | `/ops/knowledge-attach/{id}` | `ops:knowledge:edit` | 删除附件 |
| GET | `/ops/knowledge-attach/download/{id}` | `ops:knowledge:query` | 下载附件 |

---

## 10. 系统资产 /ops/system-asset

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/system-asset/list` | `ops:system-asset:list` | 分页查询 |
| GET | `/ops/system-asset/{id}` | `ops:system-asset:query` | 获取详情 |
| GET | `/ops/system-asset/next-code` | `ops:system-asset:query` | 获取下一个系统编码 |
| GET | `/ops/system-asset/{id}/linked-items` | `ops:system-asset:query` | 获取该系统关联的事项统计（issueCount, bugCount, reqCount, changeCount） |
| POST | `/ops/system-asset/add` | `ops:system-asset:add` | 新增系统 |
| PUT | `/ops/system-asset/edit` | `ops:system-asset:edit` | 修改系统 |
| DELETE | `/ops/system-asset/{ids}` | `ops:system-asset:remove` | 删除 |
| POST | `/ops/system-asset/export` | `ops:system-asset:export` | 导出 Excel |

---

## 11. 团队成员 /ops/team-member

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/team-member/list` | `ops:team-member:list` | 分页查询 |
| GET | `/ops/team-member/{id}` | `ops:team-member:query` | 获取详情 |
| POST | `/ops/team-member/add` | `ops:team-member:add` | 新增 |
| PUT | `/ops/team-member/edit` | `ops:team-member:edit` | 修改 |
| DELETE | `/ops/team-member/{ids}` | `ops:team-member:remove` | 删除 |
| POST | `/ops/team-member/export` | `ops:team-member:export` | 导出 Excel |

---

## 12. 供应商 /ops/vendor-contact

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/vendor-contact/list` | `ops:vendor-contact:list` | 分页查询 |
| GET | `/ops/vendor-contact/{id}` | `ops:vendor-contact:query` | 获取详情 |
| POST | `/ops/vendor-contact/add` | `ops:vendor-contact:add` | 新增 |
| PUT | `/ops/vendor-contact/edit` | `ops:vendor-contact:edit` | 修改 |
| DELETE | `/ops/vendor-contact/{ids}` | `ops:vendor-contact:remove` | 删除 |
| POST | `/ops/vendor-contact/export` | `ops:vendor-contact:export` | 导出 Excel |

---

## 13. 巡检记录 /ops/daily-inspection

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/daily-inspection/list` | `daily:inspection:list` | 分页查询 |
| GET | `/ops/daily-inspection/{id}` | `daily:inspection:query` | 获取详情 |
| POST | `/ops/daily-inspection/add` | `daily:inspection:add` | 新增 |
| PUT | `/ops/daily-inspection/edit` | `daily:inspection:edit` | 修改 |
| DELETE | `/ops/daily-inspection/{ids}` | `daily:inspection:remove` | 删除 |
| POST | `/ops/daily-inspection/export` | `daily:inspection:export` | 导出 Excel |

---

## 14. 故障记录 /ops/fault

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/fault/list` | `ops:fault:list` | 分页查询 |
| GET | `/ops/fault/{id}` | `ops:fault:query` | 获取详情 |
| POST | `/ops/fault/add` | `ops:fault:add` | 新增 |
| PUT | `/ops/fault/edit` | `ops:fault:edit` | 修改 |
| DELETE | `/ops/fault/{ids}` | `ops:fault:remove` | 删除 |
| POST | `/ops/fault/export` | `ops:fault:export` | 导出 Excel |

---

## 15. 备份记录 /ops/backup

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/backup/list` | `ops:backup:list` | 分页查询 |
| GET | `/ops/backup/{id}` | `ops:backup:query` | 获取详情 |
| POST | `/ops/backup/add` | `ops:backup:add` | 新增 |
| PUT | `/ops/backup/edit` | `ops:backup:edit` | 修改 |
| DELETE | `/ops/backup/{ids}` | `ops:backup:remove` | 删除 |
| POST | `/ops/backup/export` | `ops:backup:export` | 导出 Excel |

---

## 16. 值班管理 /ops/duty-shift

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/duty-shift/list` | `ops:duty-shift:list` | 分页查询 |
| GET | `/ops/duty-shift/{id}` | `ops:duty-shift:query` | 获取详情 |
| POST | `/ops/duty-shift/add` | `ops:duty-shift:add` | 新增 |
| PUT | `/ops/duty-shift/edit` | `ops:duty-shift:edit` | 修改 |
| DELETE | `/ops/duty-shift/{ids}` | `ops:duty-shift:remove` | 删除 |
| POST | `/ops/duty-shift/export` | `ops:duty-shift:export` | 导出 Excel |

---

## 17. 值班日志 /ops/shift-log

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/shift-log/list` | `ops:shift-log:list` | 分页查询 |
| GET | `/ops/shift-log/{id}` | `ops:shift-log:query` | 获取详情 |
| POST | `/ops/shift-log/add` | `ops:shift-log:add` | 新增 |
| PUT | `/ops/shift-log/edit` | `ops:shift-log:edit` | 修改 |
| DELETE | `/ops/shift-log/{ids}` | `ops:shift-log:remove` | 删除 |
| POST | `/ops/shift-log/export` | `ops:shift-log:export` | 导出 Excel |

---

## 18. 仪表盘 /ops/dashboard

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/ops/dashboard/stats` | `ops:dashboard:view` | 全局统计概览 |

---

## 附录：通用分页参数

所有 list 接口支持以下通用分页参数：

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| pageNum | int | 1 | 页码 |
| pageSize | int | 10 | 每页条数 |
| orderByColumn | string | - | 排序列 |
| isAsc | string | desc | 排序方向 |

**响应格式**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "total": 100,
  "rows": [...]
}
```

## 附录：字典类型索引

| dict_type | 说明 |
|-----------|------|
| `ops_system_type` | 系统资产类型 |
| `ops_importance_level` | 重要等级 |
| `ops_system_status` | 系统运行状态 |
| `ops_team_role` | 团队成员角色 |
| `ops_on_job_status` | 在岗状态 |
| `ops_issue_type` | 运维记录类型（12 种） |
| `ops_issue_status` | 运维记录状态 |
| `ops_handle_method` | 处理方式 |
| `ops_root_cause` | 根因分类 |
| `ops_item_type` | 统一事项类型 |
| `ops_item_priority` | 优先级 |
| `ops_item_status` | 事项状态 |
| `ops_acceptance_result` | 验收结果 |
| `ops_relation_type` | 关系类型 |
| `ht_dept_code` | 需求部门 |
| `ht_req_module` | 需求模块 |
| `ht_req_status` | 需求状态 |
| `ht_analysis_result` | 分析结果 |
| `ht_acceptor` | 验收人 |
| `ht_acceptance_result` | 需求验收结果 |
| `ht_business_flag` | 是否涉及商务 |
| `ht_bug_severity` | Bug 严重程度 |
| `ht_bug_status` | Bug 状态 |
| `ht_verify_result` | Bug 验证结果 |
| `ops_change_type` | 变更类型 |
| `ops_change_status` | 变更状态 |
| `ops_change_result` | 变更执行结果 |
| `ops_risk_level` | 风险等级 |
| `ops_fault_level` | 故障等级 |
| `ops_fault_status` | 故障状态 |
| `ops_backup_type` | 备份类型 |
| `ops_backup_result` | 备份结果 |
| `ops_knowledge_category` | 知识分类 |
| `ops_publish_status` | 发布状态 |
| `ops_shift_type` | 值班班次 |
| `ops_shift_status` | 班次状态 |
| `ops_log_type` | 日志类型 |
| `ops_severity` | 严重程度 |
| `ops_yes_no` | 是否 |
| `ops_health_status` | 巡检状态 |
| `ops_inspection_result` | 巡检结果 |
| `ops_restore_tested` | 恢复验证 |
| `ops_service_scope` | 供应商服务范围 |
| `ops_sla_level` | SLA 等级 |
| `ops_task_status` | 任务状态 |
| `ops_operation_type` | 运维类型 |
| `ops_operation_result` | 执行结果 |

**通用字典接口**（RuoYi 框架自带）：

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/system/dict/data/type/{dictType}` | 按类型获取字典数据 |
