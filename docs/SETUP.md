# 投资系统运维平台 — 本地环境搭建指南

> 分支: `codex/devops-p1-linkage-hardening`  
> 仓库: `github.com:kevin-wxq/investmentSysDevops.git`  
> 技术栈: Spring Boot 2.5.15 + Vue 2 + Element UI 2.15.14 + MySQL 5.7+

---

## 一、环境要求

| 组件 | 版本要求 | 检查命令 |
|------|----------|----------|
| JDK | 1.8+ | `java -version` |
| Maven | 3.6+ | `mvn -v` |
| Node.js | 14+ | `node -v` |
| npm | 6+ | `npm -v` |
| MySQL | 5.7+ | `mysql --version` |

---

## 二、从零搭建（5 步）

### 第 1 步：克隆项目

```bash
git clone git@github.com:kevin-wxq/investmentSysDevops.git
cd investmentSysDevops

# 切换到当前分支
git checkout codex/devops-p1-linkage-hardening
```

### 第 2 步：初始化数据库

创建数据库并导入全部表结构和初始化数据：

```bash
mysql -uroot -p < docs/init_db.sql
```

> 这条命令会创建名为 `devops` 的数据库，并导入全部业务表、字典数据、菜单配置。
> 如果你还没有 RuoYi 的系统表（`sys_*`、`qrtz_*` 等），还需要执行：

```bash
mysql -uroot -p devops < sql/ry_20250522.sql   # RuoYi 框架基础表
mysql -uroot -p devops < sql/quartz.sql         # 定时任务表
```

> **数据库连接配置** 在 `ruoyi-admin/src/main/resources/application-druid.yml`：
> - 数据库名：`devops`
> - 用户名：`root`
> - 密码：`root123456`
>
> 如果实际环境不同，修改 `application-druid.yml` 中 `master.url`、`master.username`、`master.password` 三个字段即可。

### 第 3 步：启动后端

```bash
cd ruoyi-admin
mvn spring-boot:run
```

或者先打包再运行：

```bash
mvn clean package -DskipTests
java -jar target/ruoyi-admin.jar
```

> 后端默认端口 `8080`，启动完成后访问 http://localhost:8080 看到 "欢迎使用RuoYi" 即成功。

### 第 4 步：启动前端

在另一个终端中：

```bash
cd ruoyi-ui
npm install       # 仅首次需要
port=8081 npm run dev
```

> 前端开发服务器默认端口 `8081`。

### 第 5 步：登录验证

浏览器打开 http://localhost:8081

| 项目 | 值 |
|------|------|
| 用户名 | `admin` |
| 密码 | `admin123` |
| 验证码 | 已关闭（无需输入） |

左侧菜单展开「运维管理」，即可看到所有功能模块。

---

## 三、项目结构

```
investmentSysDevops/
├── ruoyi-admin/          # Spring Boot 后端
│   └── src/main/java/com/ruoyi/devops/
│       ├── controller/   # API 接口
│       ├── domain/       # 实体类
│       ├── mapper/       # MyBatis Mapper
│       └── service/      # 业务逻辑
├── ruoyi-ui/             # Vue 2 前端
│   └── src/views/devops/ # 各模块页面
│       ├── opsIssue/     # 运维记录
│       ├── htBug/        # Bug 管理
│       ├── htRequirement/# 需求管理
│       ├── changeRecord/ # 变更记录
│       ├── workItem/     # 统一事项
│       ├── reportCenter/ # 报告中心
│       ├── knowledge/    # 知识库
│       ├── dutyShift/    # 值班管理
│       ├── shiftLog/     # 值班日志
│       ├── dailyInspectionMain/ # 巡检记录
│       ├── faultRecord/  # 故障记录
│       ├── backupRecord/ # 备份记录
│       ├── systemAsset/  # 系统资产
│       ├── teamMember/   # 团队成员
│       ├── vendorContact/# 供应商
│       └── dashboard/    # 仪表盘
├── sql/                  # 各版本 SQL 脚本
├── docs/                 # 文档
│   ├── init_db.sql       # 数据库初始化脚本
│   ├── SETUP.md          # 本文档
│   ├── API.md            # 接口文档
│   └── DB.md             # 数据库设计文档
└── pom.xml               # Maven 父 POM
```

---

## 四、常见问题

### Q: 启动后端报数据库连接失败？

检查 MySQL 是否运行，以及 `application-druid.yml` 中的连接信息是否正确。

### Q: 启动前端报模块找不到？

重新安装依赖：
```bash
cd ruoyi-ui && rm -rf node_modules && npm install
```

### Q: 登录后菜单为空？

确认已执行 `docs/init_db.sql`，并检查 `sys_role_menu` 表中 role_id=1 的菜单权限是否齐全。

### Q: 如何切换端口？

- 后端：修改 `application.yml` 中 `server.port`
- 前端：修改启动命令中的 `port=8081` 为其他值

### Q: 后端启动后类找不到？

有时需要 clean 重编译：
```bash
cd ruoyi-admin && mvn clean package -DskipTests
```
