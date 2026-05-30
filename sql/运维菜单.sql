-- ========================================
-- 投资系统运维项目 — 菜单初始化 SQL
-- 使用前请确保已导入 ry_20250522.sql
-- ========================================

-- 一级菜单：运维管理
insert ignore into sys_menu values('2200', '运维管理', '0', '5', 'devops', null, null, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', sysdate(), '', null, '运维管理目录');

-- ============================================
-- 组织人员
-- ============================================
insert ignore into sys_menu values('2201', '组织人员', '2200', '1', 'team', null, null, '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2202', '团队成员', '2201', '1', 'teamMember', 'devops/teamMember/index', null, '', 1, 0, 'C', '0', '0', 'ops:team-member:list', '#', 'admin', sysdate(), '', null, '团队成员菜单');
insert ignore into sys_menu values('2203', '团队成员查询', '2202', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2204', '团队成员新增', '2202', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2205', '团队成员修改', '2202', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2206', '团队成员删除', '2202', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2207', '团队成员导出', '2202', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:team-member:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================
-- 基础资源
-- ============================================
insert ignore into sys_menu values('2210', '基础资源', '2200', '2', 'asset', null, null, '', 1, 0, 'M', '0', '0', '', 'tree-table', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2211', '系统资产', '2210', '1', 'systemAsset', 'devops/systemAsset/index', null, '', 1, 0, 'C', '0', '0', 'ops:system-asset:list', '#', 'admin', sysdate(), '', null, '系统资产菜单');
insert ignore into sys_menu values('2212', '系统资产查询', '2211', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2213', '系统资产新增', '2211', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2214', '系统资产修改', '2211', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2215', '系统资产删除', '2211', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2216', '系统资产导出', '2211', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:system-asset:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2220', '供应商联系', '2210', '2', 'vendorContact', 'devops/vendorContact/index', null, '', 1, 0, 'C', '0', '0', 'ops:vendor-contact:list', '#', 'admin', sysdate(), '', null, '供应商联系菜单');
insert ignore into sys_menu values('2221', '供应商联系查询', '2220', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2222', '供应商联系新增', '2220', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2223', '供应商联系修改', '2220', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2224', '供应商联系删除', '2220', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2225', '供应商联系导出', '2220', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:vendor-contact:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================
-- 周报管理
-- ============================================
insert ignore into sys_menu values('2230', '周报管理', '2200', '3', 'weekly', null, null, '', 1, 0, 'M', '0', '0', '', 'form', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2231', '周运维任务', '2230', '1', 'weeklyOperationTask', 'devops/weeklyOperationTask/index', null, '', 1, 0, 'C', '0', '0', 'weekly:operation-task:list', '#', 'admin', sysdate(), '', null, '周运维任务菜单');
insert ignore into sys_menu values('2232', '周运维任务查询', '2231', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:operation-task:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2233', '周运维任务新增', '2231', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:operation-task:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2234', '周运维任务修改', '2231', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:operation-task:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2235', '周运维任务删除', '2231', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:operation-task:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2236', '周运维任务导出', '2231', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:operation-task:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2240', '关键运维明细', '2230', '2', 'weeklyKeyOperationDetail', 'devops/weeklyKeyOperationDetail/index', null, '', 1, 0, 'C', '0', '0', 'weekly:key-operation:list', '#', 'admin', sysdate(), '', null, '关键运维明细菜单');
insert ignore into sys_menu values('2241', '关键运维明细查询', '2240', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:key-operation:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2242', '关键运维明细新增', '2240', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:key-operation:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2243', '关键运维明细修改', '2240', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:key-operation:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2244', '关键运维明细删除', '2240', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:key-operation:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2245', '关键运维明细导出', '2240', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'weekly:key-operation:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================
-- 值班巡检
-- ============================================
insert ignore into sys_menu values('2260', '值班巡检', '2200', '4', 'duty', null, null, '', 1, 0, 'M', '0', '0', '', 'guide', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2261', '值班班次', '2260', '1', 'dutyShift', 'devops/dutyShift/index', null, '', 1, 0, 'C', '0', '0', 'ops:duty-shift:list', '#', 'admin', sysdate(), '', null, '值班班次菜单');
insert ignore into sys_menu values('2262', '值班班次查询', '2261', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2263', '值班班次新增', '2261', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2264', '值班班次修改', '2261', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2265', '值班班次删除', '2261', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2266', '值班班次导出', '2261', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:duty-shift:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2270', '值班日志', '2260', '2', 'shiftLog', 'devops/shiftLog/index', null, '', 1, 0, 'C', '0', '0', 'ops:shift-log:list', '#', 'admin', sysdate(), '', null, '值班日志菜单');
insert ignore into sys_menu values('2271', '值班日志查询', '2270', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2272', '值班日志新增', '2270', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2273', '值班日志修改', '2270', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2274', '值班日志删除', '2270', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2275', '值班日志导出', '2270', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:shift-log:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2280', '巡检记录', '2260', '3', 'dailyInspectionMain', 'devops/dailyInspectionMain/index', null, '', 1, 0, 'C', '0', '0', 'daily:inspection:list', '#', 'admin', sysdate(), '', null, '巡检记录菜单');
insert ignore into sys_menu values('2281', '巡检记录查询', '2280', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2282', '巡检记录新增', '2280', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2283', '巡检记录修改', '2280', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2284', '巡检记录删除', '2280', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2285', '巡检记录导出', '2280', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'daily:inspection:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================
-- 故障变更
-- ============================================
insert ignore into sys_menu values('2290', '故障变更', '2200', '5', 'incident', null, null, '', 1, 0, 'M', '0', '0', '', 'warning', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2291', '故障记录', '2290', '1', 'faultRecord', 'devops/faultRecord/index', null, '', 1, 0, 'C', '0', '0', 'ops:fault:list', '#', 'admin', sysdate(), '', null, '故障记录菜单');
insert ignore into sys_menu values('2292', '故障记录查询', '2291', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2293', '故障记录新增', '2291', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2294', '故障记录修改', '2291', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2295', '故障记录删除', '2291', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2296', '故障记录导出', '2291', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:fault:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2300', '备份记录', '2290', '2', 'backupRecord', 'devops/backupRecord/index', null, '', 1, 0, 'C', '0', '0', 'ops:backup:list', '#', 'admin', sysdate(), '', null, '备份记录菜单');
insert ignore into sys_menu values('2301', '备份记录查询', '2300', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2302', '备份记录新增', '2300', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2303', '备份记录修改', '2300', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2304', '备份记录删除', '2300', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2305', '备份记录导出', '2300', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:backup:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2310', '变更记录', '2290', '3', 'changeRecord', 'devops/changeRecord/index', null, '', 1, 0, 'C', '0', '0', 'ops:change:list', '#', 'admin', sysdate(), '', null, '变更记录菜单');
insert ignore into sys_menu values('2311', '变更记录查询', '2310', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2312', '变更记录新增', '2310', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2313', '变更记录修改', '2310', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2314', '变更记录删除', '2310', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2315', '变更记录导出', '2310', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:change:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================
-- 知识沉淀
-- ============================================
insert ignore into sys_menu values('2320', '知识沉淀', '2200', '6', 'knowledge', null, null, '', 1, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2321', '运维知识库', '2320', '1', 'knowledgeBase', 'devops/knowledge/index', null, '', 1, 0, 'C', '0', '0', 'ops:knowledge:list', '#', 'admin', sysdate(), '', null, '运维知识库菜单');
insert ignore into sys_menu values('2322', '运维知识库查询', '2321', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2323', '运维知识库新增', '2321', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2324', '运维知识库修改', '2321', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2325', '运维知识库删除', '2321', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2326', '运维知识库导出', '2321', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:knowledge:export', '#', 'admin', sysdate(), '', null, '');

-- ============================================
-- 为超级管理员角色分配菜单权限
-- role_id=1 为超级管理员
-- ============================================
insert into sys_role_menu (role_id, menu_id) 
select '1', '2200' from dual where not exists (select 1 from sys_role_menu where role_id='1' and menu_id='2200');

insert into sys_role_menu (role_id, menu_id)
select '1', m.menu_id from (
    select menu_id from sys_menu where menu_id between '2200' and '2326'
) m where not exists (select 1 from sys_role_menu where role_id='1' and menu_id=m.menu_id);


-- 仪表盘菜单
insert ignore into sys_menu values('2330', '运维仪表盘', '2200', '0', 'dashboard', 'devops/dashboard/index', null, '', 1, 0, 'C', '0', '0', 'ops:dashboard:view', 'dashboard', 'admin', sysdate(), '', null, '运维仪表盘');
insert into sys_role_menu (role_id, menu_id) select '1', '2330' from dual where not exists (select 1 from sys_role_menu where role_id='1' and menu_id='2330');

-- ============================================
-- 闭环中心
-- ============================================
insert ignore into sys_menu values('2340', '闭环中心', '2200', '7', 'closedLoop', null, null, '', 1, 0, 'M', '0', '0', '', 'nested', 'admin', sysdate(), '', null, '需求、Bug、问题闭环与报告');

insert ignore into sys_menu values('2341', '事项闭环', '2340', '1', 'workItem', 'devops/workItem/index', null, '', 1, 0, 'C', '0', '0', 'ops:work-item:list', 'list', 'admin', sysdate(), '', null, '统一闭环事项');
insert ignore into sys_menu values('2342', '事项闭环查询', '2341', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:work-item:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2343', '事项闭环新增', '2341', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:work-item:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2344', '事项闭环修改', '2341', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:work-item:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2345', '事项闭环删除', '2341', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:work-item:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2346', '事项闭环导出', '2341', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:work-item:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2350', '衡泰需求管理', '2340', '2', 'htRequirement', 'devops/htRequirement/index', null, '', 1, 0, 'C', '0', '0', 'ht:requirement:list', 'form', 'admin', sysdate(), '', null, '衡泰需求管理');
insert ignore into sys_menu values('2351', '衡泰需求查询', '2350', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2352', '衡泰需求新增', '2350', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2353', '衡泰需求修改', '2350', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2354', '衡泰需求删除', '2350', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2355', '衡泰需求导出', '2350', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:requirement:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2360', 'Bug管理', '2340', '3', 'htBug', 'devops/htBug/index', null, '', 1, 0, 'C', '0', '0', 'ht:bug:list', 'bug', 'admin', sysdate(), '', null, 'Bug管理');
insert ignore into sys_menu values('2361', 'Bug查询', '2360', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2362', 'Bug新增', '2360', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2363', 'Bug修改', '2360', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2364', 'Bug删除', '2360', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2365', 'Bug导出', '2360', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ht:bug:export', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2380', '运维问题', '2340', '4', 'opsIssue', 'devops/opsIssue/index', null, '', 1, 0, 'C', '0', '0', 'ops:issue:list', 'question', 'admin', sysdate(), '', null, '运维问题');
insert ignore into sys_menu values('2381', '运维问题查询', '2380', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:query', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2382', '运维问题新增', '2380', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:add', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2383', '运维问题修改', '2380', '3', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:edit', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2384', '运维问题删除', '2380', '4', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:remove', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2385', '运维问题导出', '2380', '5', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:export', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2386', '运维问题转化', '2380', '6', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:issue:convert', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2387', '事项关系列表', '2380', '7', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:item-relation:list', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2388', '事项关系查询', '2380', '8', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:item-relation:query', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_menu values('2370', '报告中心', '2340', '5', 'reportCenter', 'devops/reportCenter/index', null, '', 1, 0, 'C', '0', '0', 'ops:report:view', 'chart', 'admin', sysdate(), '', null, '闭环报告中心');
insert ignore into sys_menu values('2371', '报告查看', '2370', '1', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:report:view', '#', 'admin', sysdate(), '', null, '');
insert ignore into sys_menu values('2372', '报告导出', '2370', '2', '#', '', null, '', 1, 0, 'F', '0', '0', 'ops:report:export', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu (role_id, menu_id)
select '1', m.menu_id from (
    select menu_id from sys_menu where menu_id between '2340' and '2388'
) m where not exists (select 1 from sys_role_menu where role_id='1' and menu_id=m.menu_id);
