package com.ruoyi.devops.enums;

/**
 * 运维系统统一枚举定义
 */
public class OpsEnums {

    /** 团队成员角色 */
    public enum MemberRole {
        LEADER("1", "运维负责人"),
        SHIFT_LEADER("2", "值班长"),
        ENGINEER("3", "运维工程师"),
        INSPECTOR("4", "巡检员");

        private final String code;
        private final String desc;
        MemberRole(String code, String desc) { this.code = code; this.desc = desc; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 在岗状态 */
    public enum OnJobStatus {
        ON("1", "在岗"), VACATION("2", "休假"), RESIGNED("3", "离职");
        private final String code, desc;
        OnJobStatus(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 系统类型 */
    public enum SystemType {
        CORE("1", "核心业务"), SUPPORT("2", "支撑系统"), INFRA("3", "基础设施");
        private final String code, desc;
        SystemType(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 重要等级 */
    public enum ImportanceLevel {
        CRITICAL("1", "核心"), IMPORTANT("2", "重要"), NORMAL("3", "一般");
        private final String code, desc;
        ImportanceLevel(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 系统状态 */
    public enum SystemStatus {
        RUNNING("1", "运行"), STOPPED("2", "停用"), OFFLINE("3", "下线");
        private final String code, desc;
        SystemStatus(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 服务范围 */
    public enum ServiceScope {
        DEV("1", "开发"), OPS("2", "运维"), HARDWARE("3", "硬件"), ALL("4", "综合");
        private final String code, desc;
        ServiceScope(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 任务状态 */
    public enum TaskStatus {
        TODO("1", "待开始"), IN_PROGRESS("2", "进行中"), DONE("3", "已完成"), DELAYED("4", "已延期");
        private final String code, desc;
        TaskStatus(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 班次类型 */
    public enum ShiftType {
        DAY("1", "白班"), NIGHT("2", "夜班"), HOLIDAY("3", "节假日");
        private final String code, desc;
        ShiftType(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 班次状态 */
    public enum ShiftStatus {
        ONGOING("1", "进行中"), HANDED_OVER("2", "已交接"), ENDED("3", "已结束");
        private final String code, desc;
        ShiftStatus(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 日志类型 */
    public enum LogType {
        DAILY("1", "日常"), INCIDENT("2", "事件"), ALARM("3", "告警"), HANDOVER("4", "交接");
        private final String code, desc;
        LogType(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 严重程度 */
    public enum Severity {
        NORMAL("1", "普通"), IMPORTANT("2", "重要"), URGENT("3", "紧急");
        private final String code, desc;
        Severity(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 故障等级 */
    public enum FaultLevel {
        GENERAL("1", "一般"), SERIOUS("2", "严重"), EMERGENCY("3", "紧急");
        private final String code, desc;
        FaultLevel(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 故障状态 */
    public enum FaultStatus {
        FOUND("1", "发现"), PROCESSING("2", "处理中"), RECOVERED("3", "已恢复"), CLOSED("4", "已关闭");
        private final String code, desc;
        FaultStatus(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 备份类型 */
    public enum BackupType {
        DB("1", "数据库"), FILE("2", "文件"), CONFIG("3", "配置"), FULL("4", "全量");
        private final String code, desc;
        BackupType(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 备份结果 */
    public enum BackupResult {
        SUCCESS("1", "成功"), FAILED("2", "失败");
        private final String code, desc;
        BackupResult(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 变更类型 */
    public enum ChangeType {
        UPGRADE("1", "版本升级"), CONFIG("2", "配置变更"), DB("3", "数据库变更"), NETWORK("4", "网络变更"), OTHER("5", "其他");
        private final String code, desc;
        ChangeType(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 风险等级 */
    public enum RiskLevel {
        LOW("1", "低"), MEDIUM("2", "中"), HIGH("3", "高");
        private final String code, desc;
        RiskLevel(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 变更结果 */
    public enum ChangeResult {
        SUCCESS("1", "成功"), FAILED("2", "失败"), PARTIAL("3", "部分成功");
        private final String code, desc;
        ChangeResult(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 知识分类 */
    public enum KnowledgeCategory {
        FAULT("1", "故障处理"), MANUAL("2", "操作手册"), PLAN("3", "应急预案"), FAQ("4", "FAQ");
        private final String code, desc;
        KnowledgeCategory(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 发布状态 */
    public enum PublishStatus {
        DRAFT("0", "草稿"), PUBLISHED("1", "已发布");
        private final String code, desc;
        PublishStatus(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }

    /** 巡检结果 */
    public enum InspectionResult {
        NORMAL("1", "正常"), ABNORMAL("2", "异常"), REPAIRED("3", "已修复");
        private final String code, desc;
        InspectionResult(String c, String d) { code=c; desc=d; }
        public String getCode() { return code; }
        public String getDesc() { return desc; }
    }
}
