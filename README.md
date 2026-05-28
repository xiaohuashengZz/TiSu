# TiSu (体塑) - 智能健身饮食管理平台

TiSu 是一个全栈健康管理平台，面向减脂、增肌及日常健康管理人群，提供饮食规划、训练计划、身体数据追踪和 AI 智能健康分析等功能。

## 技术栈

**后端**
- Java 17 / Spring Boot 3.2.5
- MyBatis-Plus 3.5.6
- MySQL 8.x
- Redis
- JWT (JJWT 0.12.5)
- Knife4j 4.4.0 (API 文档)

**前端**
- Vue 3.5 / Vite 8
- Element Plus 2.13
- Pinia 3.0 (状态管理)
- Vue Router 4.6
- ECharts 6.0 (数据可视化)

## 项目结构

```
TiSu/
├── TiSu-java/                  # 后端 (Maven 多模块)
│   ├── tisu-app/               # 启动入口、配置文件
│   ├── tisu-api/               # 控制器、拦截器
│   ├── tisu-service/           # 业务逻辑
│   ├── tisu-ai/                # AI 集成模块
│   ├── tisu-dao/               # 数据访问层 (MyBatis-Plus)
│   ├── tisu-model/             # 实体、DTO、VO
│   └── tisu-common/            # 通用工具 (JWT、BCrypt、统一响应)
├── TiSu-vue/                   # 前端 (Vue 3 SPA)
│   └── src/
│       ├── api/                # API 请求模块
│       ├── views/              # 页面组件
│       ├── components/         # 布局组件
│       ├── router/             # 路由配置
│       ├── stores/             # Pinia 状态管理
│       └── utils/              # Axios 拦截器
└── docs/                       # 文档与数据库脚本
    ├── init-data.sql           # 数据库初始化脚本
    ├── requirements.md         # 需求文档
    └── DESIGN.md               # UI 设计规范
```

## 环境要求

| 依赖 | 版本 |
|------|------|
| JDK | 17+ |
| Maven | 3.6+ |
| Node.js | 18+ |
| MySQL | 8.x |
| Redis | 6.x+ |

## 快速开始

### 1. 初始化数据库

```bash
mysql -u root -p < docs/init-data.sql
```

### 2. 启动后端

```bash
cd TiSu-java

# 根据需要修改配置文件中的数据库和 Redis 连接信息
# TiSu-java/tisu-app/src/main/resources/application.yml

mvn clean package
mvn spring-boot:run -pl tisu-app
```

后端默认运行在 `http://localhost:8080`，API 文档地址：`http://localhost:8080/doc.html`

### 3. 启动前端

```bash
cd TiSu-vue
npm install
npm run dev
```

前端默认运行在 `http://localhost:5173`，开发模式下 `/api` 请求自动代理到后端 8080 端口。

## 主要功能

### 用户端

- **仪表盘** - 体重趋势、营养摄入、今日餐食与训练概览
- **饮食管理** - 食物库、菜品管理、每日餐食规划
- **训练管理** - 动作库、训练计划制定、训练日历
- **身体记录** - 体重追踪、身体围度记录
- **AI 分析** - 基于用户健康数据生成周报/月报，支持自定义 AI 模型和 API Key

### 管理端

- **数据看板** - 平台运营数据统计
- **内容管理** - 食物库、菜品、动作库的官方内容维护
- **用户管理** - 用户信息管理
- **操作日志** - 管理员操作审计

## 认证机制

系统采用双 JWT 体系：

- **用户 Token** - 访问 `/api/**` 接口，过期时间 24 小时
- **管理员 Token** - 访问 `/api/admin/**` 接口，过期时间 24 小时

## API 概览

| 模块 | 路径前缀 | 说明 |
|------|----------|------|
| 认证 | `/api/auth/` | 注册、登录 |
| 仪表盘 | `/api/dashboard` | 首页数据 |
| 饮食 | `/api/food/`, `/api/dish/`, `/api/meal-plan/` | 食物、菜品、餐食计划 |
| 训练 | `/api/exercise/`, `/api/training-plan/`, `/api/training-log/` | 动作、训练计划、训练记录 |
| 记录 | `/api/weight-log/`, `/api/body-measurement/` | 体重、身体数据 |
| AI | `/api/ai/` | AI 分析与报告 |
| 管理端 | `/api/admin/` | 管理员相关接口 |

## 构建部署

```bash
# 后端打包
cd TiSu-java && mvn clean package

# 前端打包
cd TiSu-vue && npm run build
```

## 许可证

[Apache License 2.0](LICENSE)
