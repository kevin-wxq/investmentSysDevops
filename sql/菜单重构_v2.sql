-- ========================================
-- 菜单重构 v2：扁平化 + 业务流转排序
-- 原则：删二级目录，所有功能页平铺到一级菜单下；按业务流转排顺序
-- ========================================

-- 1. 删掉所有旧的二级/三级分组目录及其子菜单
-- 删除"组织人员"目录 (2201) 及其子菜单
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE parent_id IN (2201, 2202) OR menu_id = 2201);
DELETE FROM sys_menu WHERE menu_id IN (2201, 2202, 2203, 2204, 2205, 2206, 2207);

-- 删除"基础资源"目录 (2210) 及其子菜单
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE parent_id IN (2210, 2211, 2220) OR menu_id = 2210);
DELETE FROM sys_menu WHERE menu_id IN (2210, 2211, 2212, 2213, 2214, 2215, 2216, 2220, 2221, 2222, 2223, 2224, 2225);

-- 删除"周报管理"目录 (2230) 及其子菜单（全部不用了）
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE parent_id IN (2230, 2231, 2240) OR menu_id = 2230);
DELETE FROM sys_menu WHERE menu_id IN (
    2230, 2231, 2232, 2233, 2234, 2235, 2236,
    2240, 2241, 2242, 2243, 2244, 2245
);

-- 删除"值班巡检"目录 (2260)
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE parent_id = 2260 OR menu_id = 2260);
DELETE FROM sys_menu WHERE menu_id = 2260;

-- 删除"故障变更"目录 (2290)
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE parent_id = 2290 OR menu_id = 2290);
DELETE FROM sys_menu WHERE menu_id = 2290;

-- 删除"知识沉淀"目录 (2320)
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE parent_id = 2320 OR menu_id = 2320);
DELETE FROM sys_menu WHERE menu_id = 2320;

-- 删除"闭环中心"目录 (2340)
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE parent_id = 2340 OR menu_id = 2340);
DELETE FROM sys_menu WHERE menu_id = 2340;


-- 2. 把所有功能页的 parent_id 改为 2200（运维管理），按业务流转重排 order_num

-- 0: 仪表盘
UPDATE sys_menu SET parent_id = 2200, order_num = 0 WHERE menu_id = 2330;

-- === 核心闭环流程 ===
-- 1: 运维问题（入口）
UPDATE sys_menu SET parent_id = 2200, order_num = 1 WHERE menu_id = 2380;
-- 2: Bug管理
UPDATE sys_menu SET parent_id = 2200, order_num = 2 WHERE menu_id = 2360;
-- 3: 需求管理
UPDATE sys_menu SET parent_id = 2200, order_num = 3 WHERE menu_id = 2350;
-- 4: 变更管理
UPDATE sys_menu SET parent_id = 2200, order_num = 4 WHERE menu_id = 2310;
-- 5: 统一事项
UPDATE sys_menu SET parent_id = 2200, order_num = 5 WHERE menu_id = 2341;
-- 6: 报告中心
UPDATE sys_menu SET parent_id = 2200, order_num = 6 WHERE menu_id = 2370;

-- === 日常运维 ===
-- 8: 值班班次
UPDATE sys_menu SET parent_id = 2200, order_num = 8 WHERE menu_id = 2261;
-- 9: 值班日志
UPDATE sys_menu SET parent_id = 2200, order_num = 9 WHERE menu_id = 2270;
-- 10: 巡检记录
UPDATE sys_menu SET parent_id = 2200, order_num = 10 WHERE menu_id = 2280;
-- 11: 故障记录
UPDATE sys_menu SET parent_id = 2200, order_num = 11 WHERE menu_id = 2291;
-- 12: 备份记录
UPDATE sys_menu SET parent_id = 2200, order_num = 12 WHERE menu_id = 2300;
-- 13: 知识库
UPDATE sys_menu SET parent_id = 2200, order_num = 13 WHERE menu_id = 2321;

-- === 基础数据 ===
-- 15: 系统资产
UPDATE sys_menu SET parent_id = 2200, order_num = 15 WHERE menu_id = 2211;
-- 16: 团队成员
UPDATE sys_menu SET parent_id = 2200, order_num = 16 WHERE menu_id = 2202;
-- 17: 供应商联系
UPDATE sys_menu SET parent_id = 2200, order_num = 17 WHERE menu_id = 2220;

-- 3. 给系统资产/团队成员/供应商补回功能权限菜单（之前跟着目录删了但页面菜单还在）
-- 团队成员功能权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) SELECT '1', menu_id FROM sys_menu WHERE menu_id IN (2203,2204,2205,2206,2207);
-- 系统资产功能权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) SELECT '1', menu_id FROM sys_menu WHERE menu_id IN (2212,2213,2214,2215,2216);
-- 供应商功能权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) SELECT '1', menu_id FROM sys_menu WHERE menu_id IN (2221,2222,2223,2224,2225);

-- 4. 重命名"衡泰需求管理"为"需求管理"更简洁
UPDATE sys_menu SET menu_name = '需求管理' WHERE menu_id = 2350;
-- 重命名"事项闭环"为"统一事项"
UPDATE sys_menu SET menu_name = '统一事项' WHERE menu_id = 2341;
-- 重命名"值班班次"为"值班管理"
UPDATE sys_menu SET menu_name = '值班管理' WHERE menu_id = 2261;
