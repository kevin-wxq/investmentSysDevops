-- ========================================
-- 清理旧“系统运维管理 / 日常运维明细”模块
-- 说明：该模块为早期 weekly_daily_operation 生成页面，已被新闭环规划替代。
-- ========================================

delete from sys_role_menu
where menu_id in (2000, 2001, 2002, 2003, 2004, 2005, 2006, 2250, 2251, 2252, 2253, 2254, 2255);

delete from sys_menu
where menu_id in (2000, 2001, 2002, 2003, 2004, 2005, 2006, 2250, 2251, 2252, 2253, 2254, 2255)
   or perms like 'daily-operation:daily-operation:%'
   or component = 'daily-operation/daily-operation/index';
