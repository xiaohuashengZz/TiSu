# 体塑 (TiSu) - 健身管理平台 需求分析文档

> 版本：v1.2 | 日期：2026-05-04

---

## 1. 项目概述

### 1.1 项目名称
**体塑 (TiSu)** - 智能健身饮食管理平台

### 1.2 项目定位
面向减肥、健身人士的一站式健康管理平台，集饮食规划、训练计划、数据追踪、AI 智能分析于一体。

### 1.3 目标用户
- 减肥人群：需要控制饮食、记录体重变化
- 健身爱好者：需要系统化的训练计划和数据追踪
- 健康管理者：希望全面了解自身健康数据趋势

### 1.4 技术栈

| 层级 | 技术选型 | 说明 |
|------|----------|------|
| 前端 | Vue 3 + Vite + Element Plus | 响应式 SPA，组件化开发 |
| 后端 | Spring Boot 3.x | RESTful API，微服务就绪 |
| 数据库 | MySQL 8.x | 关系型数据存储 |
| 缓存 | Redis | 会话管理、热点数据缓存 |
| AI 引擎 | OpenAI / Claude API | 用户可配置 API Key 和模型选择 |
| 对象存储 | MinIO / 本地存储 | 图片、导出文件存储 |

---

## 2. 功能模块详细设计

### 2.1 用户管理模块

#### 2.1.1 注册与登录
- 手机号 / 邮箱注册
- 账号密码登录
- Token 认证（JWT）

#### 2.1.2 个人档案
- 基本信息：昵称、性别、年龄、身高
- 身体数据：当前体重、目标体重、体脂率
- 健身目标：减脂 / 增肌 / 维持 / 塑形
- 活动等级：久坐 / 轻度活动 / 中度活动 / 高强度活动
- 每日热量目标自动计算（基于 BMR + 活动系数）

#### 2.1.3 系统设置
- AI 配置：API Key、模型选择（GPT-4 / Claude 等）
- 通知偏好设置
- 数据单位偏好（kg/lb, cm/inch）

---

### 2.2 菜单管理模块

#### 2.2.1 食物库
- **食物来源分类**：
  - **官方食物**：由后台管理员统一维护的食物数据，所有用户可见，不可编辑/删除
  - **我的食物**：用户个人创建的食物，仅自己可见，可自由编辑/删除
- 内置常见食物数据库（名称、分类、热量、蛋白质、碳水、脂肪、膳食纤维、单位）
- 支持自定义添加食物（自动标记为"我的食物"）
- **食物浏览与搜索**：
  - 按来源筛选：全部 / 官方食物 / 我的食物
  - 按分类筛选：全部 / 类型表中 `food_category` 分组下的所有分类值（动态加载）
  - 关键词搜索食物名称
  - 筛选条件变更时自动重置到第一页
- **分页查询**：默认每页 10 条，支持切换每页条数（10/20/50），显示总条数
- **食物表格展示**：名称、分类、来源标签（官方/用户）、单位、热量、蛋白质、碳水、脂肪
- 用户只能编辑/删除自己创建的食物，官方食物仅可查看
- **与菜品管理联动**：创建菜品时从食物库中选择食材（官方 + 个人）

#### 2.2.2 菜品管理
- **菜品来源分类**：
  - **官方推荐**：由后台管理员统一维护的菜品，所有用户可见，不可编辑/删除
  - **我的菜品**：用户个人创建的菜品，仅自己可见，可自由编辑/删除
- **菜品创建与编辑**：
  - 菜品名称、描述、封面图片
  - 从食物库中选择食材，设置每种食材的用量（g/个/ml）
  - 热量和营养素根据食材用量自动计算汇总
- **菜品浏览与搜索**：
  - 按来源筛选：全部 / 官方推荐 / 我的菜品
  - 按分类筛选：全部 / 类型表中 `dish_category` 分组下的所有分类值（动态加载）
  - 关键词搜索菜品名称和描述
  - 筛选条件变更时自动重置到第一页
- **分页查询**：默认每页 8 条，支持切换每页条数（8/12/24），显示总条数
- **菜品卡片展示**：菜品名称、热量、食材摘要、来源标签（官方/个人），卡片等高排列
- **与饮食计划联动**：在饮食计划中添加菜品时，可从菜品库中选择（官方 + 个人）

#### 2.2.3 饮食计划
- **统一页面**：周菜单详情与月历视图整合为一个页面
- **月历视图**：日历形式展示每日热量概况，点击某天弹出抽屉查看当日完整菜单
- **周菜单详情**：页面下方固定展示本周 7 天的菜单卡片
- **菜单数据结构**：
  - 每日包含三餐（早/午/晚）+ 加餐
  - 每餐由多道**菜品**组成（如"香煎鸡胸肉"、"牛奶燕麦粥"）
  - 每道菜品包含名称、热量，以及**食材明细**（食材名、用量 g/个/ml）
- **菜品与食材**：菜品由食物库中的食材按用量组合而成，自动汇总热量
- **操作功能**：支持复制某天菜单到其他天、月度翻页导航、菜单导出
- **月度统计**：日均摄入、月总计、达标天数、最高热量（基于实际数据计算）

#### 2.2.4 营养素计算与分析
- 每日/每周/每月热量摄入统计
- 三大营养素（蛋白质、碳水、脂肪）占比分析
- 营养摄入 vs 目标对比图表
- 营养素摄入趋势折线图

---

### 2.3 训练计划模块

#### 2.3.1 动作库
- 内置常见健身动作（名称、目标肌群、动作描述、图示/视频链接）
- 动作分类（目标肌群）：类型表中 `muscle_group` 分组下的所有值（动态加载）
- 动作难度等级：类型表中 `difficulty` 分组下的所有值（动态加载）
- 自定义添加动作

#### 2.3.2 训练计划制定
- 创建训练计划：名称、描述、周期（天数）
- 每日训练安排：选择动作、设置组数 × 次数 × 重量
- 训练日与休息日交替配置
- 训练计划模板市场：系统预设 + 用户分享

#### 2.3.3 训练日历
- 月视图日历展示训练安排
- 标记已完成/未完成/休息日
- 点击日期查看当日训练详情
- 支持拖拽调整训练日程

#### 2.3.4 训练记录
- 训练时自动记录每组数据（重量、次数、完成状态）
- 训练时长记录
- 训练备注
- 单次训练总结（消耗热量估算）

---

### 2.4 数据记录模块

#### 2.4.1 体重/体脂记录
- 每日体重打卡
- 体脂率记录（可选）
- BMI 自动计算
- 体重变化趋势图表（折线图）
- 里程碑标记（如减到目标体重）

#### 2.4.2 身体围度记录
- 胸围、腰围、臀围、臂围、腿围
- 围度变化对比图表
- 可选择记录哪些围度

#### 2.4.3 饮食记录
- 实际饮食与计划菜单对比
- 偏离计划时标记提醒
- 水分摄入记录

#### 2.4.4 训练数据记录
- 自动关联训练计划执行记录
- 力量进步趋势（某动作最大重量变化）
- 训练频率统计
- 训练量趋势图

#### 2.4.5 数据看板（Dashboard）
- 今日概览：今日热量摄入/消耗、训练完成情况
- 本周/本月数据汇总卡片
- 体重趋势迷你图
- 目标完成进度条

---

### 2.5 AI 智能分析模块

#### 2.5.1 AI 配置
- 用户配置自己的 AI API Key（OpenAI / Claude）
- 选择模型（GPT-4o / Claude Sonnet / Claude Opus 等）
- API 连接测试

#### 2.5.2 智能分析功能
- **饮食分析**：根据近期饮食数据，分析营养均衡性，给出改善建议
- **训练分析**：分析训练量、恢复情况，建议调整训练强度
- **体重预测**：基于当前趋势预测未来体重变化
- **综合报告**：生成周度/月度健康综合分析报告
- **问答助手**：用户可就健身饮食问题向 AI 提问

#### 2.5.3 分析报告
- 自动生成周报/月报
- 报告包含：数据摘要、趋势分析、改进建议
- 支持保存历史报告
- 报告可导出为 PDF

---

### 2.6 数据导出模块

#### 2.6.1 导出格式
- **CSV**：原始数据表格导出
- **Excel**：带格式和图表的 Excel 报告
- **PDF**：可视化报告导出

#### 2.6.2 可导出数据范围
- 体重/体脂记录
- 饮食记录与菜单
- 训练记录与计划
- 身体围度记录
- AI 分析报告

#### 2.6.3 导出选项
- 选择时间范围（本周/本月/自定义）
- 选择导出字段
- 批量导出所有数据

---

### 2.7 后台管理模块

后台管理独立于用户端，使用单独的布局（AdminLayout）和路由体系（`/admin/*`），供管理员维护平台基础数据和用户。

#### 2.7.1 管理后台首页（AdminDashboard）
- **统计卡片**：注册用户数、菜品总数、食物种类数、训练动作数
- **最近活动列表**：展示平台近期操作动态（用户行为、管理员操作、数据变更等）
- **数据概览**：官方菜品数、用户菜品数、本周菜单天数、食物分类数、动作肌群覆盖数、系统版本

#### 2.7.2 食物管理（AdminFoods）
- 食物库全量 CRUD（管理员可增删改所有食物，包括官方食物和用户食物）
- 来源筛选（全部/官方食物/用户食物）+ 分类筛选（下拉，数据来自类型表 `type_group = 'food_category'`）+ 关键词搜索
- 表格展示：名称、分类、来源标签（官方食物/用户食物）、热量、蛋白质、碳水、脂肪、纤维、单位
- 分页查询（默认 10 条/页，支持 10/20/50 切换）
- 管理员新增的食物自动标记为"官方食物"
- 创建/编辑对话框：名称、分类（下拉选择，数据来自类型表）、热量、营养素（蛋白质/碳水/脂肪/纤维）、单位

#### 2.7.3 菜品管理（AdminDishes）
- 官方菜品全量 CRUD（管理员维护所有官方推荐菜品）
- 分类筛选（下拉，数据来自类型表 `type_group = 'dish_category'`）+ 关键词搜索
- 表格展示：菜品名称、分类、热量、来源标签（官方推荐/用户创建）、食材摘要
- 食材编辑：从食物库选择食材，设置用量（g/个/ml），热量自动计算
- 分页查询（默认 10 条/页）
- 管理员可编辑/删除任何菜品（包括用户创建的）

#### 2.7.4 动作管理（AdminExercises）
- 动作库全量 CRUD
- 肌群筛选（下拉，数据来自类型表 `type_group = 'muscle_group'`）+ 关键词搜索
- 表格展示：动作名称、目标肌群、难度标签（初级/中级/高级）、组数、次数、描述
- 创建/编辑对话框：名称、肌群（下拉选择，数据来自类型表）、难度（下拉选择，数据来自类型表 `type_group = 'difficulty'`）、组数、次数、描述
- 分页查询（默认 10 条/页）

#### 2.7.5 用户管理（AdminUsers）
- 用户列表查询（只读查看，不直接编辑用户资料）
- 状态筛选（全部/正常/已禁用）+ 关键词搜索（昵称、邮箱）
- 表格展示：ID、昵称、邮箱、健身目标、注册时间、状态标签
- 操作：启用/禁用用户账号、删除用户
- 分页查询（默认 10 条/页）

#### 2.7.6 类型管理（AdminTypes）
- 统一管理系统中所有下拉选项的分类数据，替代硬编码的分类列表
- **类型分组**（type_group）：

| type_group | 用途 | 示例值 |
|------------|------|--------|
| `food_category` | 食物分类 | 肉类、主食、蔬菜、海鲜、水果、豆类、乳制品、零食、蛋类、调味品 |
| `dish_category` | 菜品分类 | 早餐、午餐、晚餐、加餐、通用 |
| `muscle_group` | 目标肌群 | 胸、背、肩、臂、腿、核心、有氧 |
| `difficulty` | 动作难度 | 初级、中级、高级 |
| `fitness_goal` | 健身目标 | 减脂、增肌、维持、塑形 |
| `activity_level` | 活动等级 | 久坐、轻度活动、中度活动、高强度活动 |
| `meal_type` | 餐次类型 | 早餐、午餐、晚餐、加餐 |

- **类型 CRUD**：按分组查看类型列表、新增类型、编辑类型名称、删除类型（需校验是否被引用）
- **排序支持**：同组内类型可拖拽排序，`sort_order` 字段控制显示顺序
- **引用校验**：删除类型前检查是否有数据引用（如食物表引用了某分类），有引用则禁止删除并提示
- 表格展示：分组名称、类型值、排序号、被引用次数、操作
- 分组筛选（下拉选择 type_group）+ 关键词搜索类型值

#### 2.7.7 管理后台布局（AdminLayout）
- **侧边栏**：黑色背景（`--surface-black`），包含 6 个菜单项（管理首页、食物管理、菜品管理、动作管理、用户管理、类型管理），底部"返回用户端"按钮
- **顶栏**：毛玻璃效果（`backdrop-filter`），左侧菜单折叠按钮 + 页面标题，右侧"管理员"标签 + 头像
- **响应式**：移动端侧边栏抽屉化，支持遮罩层点击关闭
- **入口**：用户端顶栏下拉菜单 → "管理后台"跳转

---

## 3. 系统架构设计

### 3.1 整体架构

```
┌─────────────────────────────────────────────────────────┐
│                    客户端 (Vue 3)                         │
│  ┌──────────────────────────┐  ┌──────────────────────┐  │
│  │       用户端 (AppLayout)  │  │  管理端 (AdminLayout) │  │
│  │ ┌────┐┌────┐┌────┐┌────┐│  │ ┌────┐┌────┐┌────┐  │  │
│  │ │菜单││训练││数据││AI  ││  │ │食物││菜品││动作│  │  │
│  │ │模块││模块││模块││模块││  │ │管理││管理││管理│  │  │
│  │ └─┬──┘└─┬──┘└─┬──┘└─┬──┘│  │ └─┬──┘└─┬──┘└─┬──┘  │  │
│  └───┼─────┼─────┼─────┼───┘  │   └──────┴──────┘     │  │
└──────┼─────┼─────┼─────┼──────┘                       │  │
       │     │     │     │          ┌────┐┌────┐        │  │
       │     │     │     │          │用户││数据│        │  │
       │     │     │     │          │管理││概览│        │  │
       │     │     │     │          └─┬──┘└─┬──┘        │  │
       │     │     │     │            └──────┘           │  │
       ▼     ▼     ▼     ▼                              ▼  │
┌─────────────────────────────────────────────────────────┐
│                  API Gateway (Nginx)                      │
└─────────────────────────┬───────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────┐
│               Spring Boot 后端服务                        │
│  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐│
│  │ 用户  │ │ 菜单  │ │ 训练  │ │ 数据  │ │ AI   │ │ 管理  ││
│  │ 服务  │ │ 服务  │ │ 服务  │ │ 服务  │ │ 服务  │ │ 服务  ││
│  └──┬───┘ └──┬───┘ └──┬───┘ └──┬───┘ └──┬───┘ └──┬───┘│
└─────┼────────┼────────┼────────┼────────┼────────┼─────┘
      │        │        │        │        │        │
      ▼        ▼        ▼        ▼        ▼        ▼
┌──────────┐  ┌──────────┐  ┌──────────────────┐
│  MySQL   │  │  Redis   │  │  外部 AI API     │
│  数据库   │  │  缓存    │  │  (OpenAI/Claude) │
└──────────┘  └──────────┘  └──────────────────┘
```

### 3.2 后端模块划分

```
tisu-server/
├── tisu-common          # 公共工具类、常量、异常处理
├── tisu-model           # 实体类、DTO、VO
├── tisu-dao             # 数据访问层 (MyBatis-Plus)
├── tisu-service         # 业务逻辑层
├── tisu-api             # 控制器层 (REST API)
├── tisu-ai              # AI 集成服务
└── tisu-app             # 启动模块
```

### 3.3 前端模块划分

```
tisu-web/
├── src/
│   ├── api/                # API 请求封装
│   ├── assets/             # 静态资源
│   ├── components/         # 公共组件
│   │   ├── AppLayout.vue   # 用户端布局（侧边栏+顶栏）
│   │   └── AdminLayout.vue # 管理端布局（独立侧边栏+顶栏）
│   ├── composables/        # 组合式函数
│   ├── router/             # 路由配置（/ 用户端 + /admin 管理端）
│   ├── stores/             # Pinia 状态管理
│   ├── utils/              # 工具函数
│   ├── mock/               # Mock 数据（含 adminUsers）
│   └── views/              # 页面视图
│       ├── auth/           # 登录注册
│       ├── dashboard/      # 数据看板
│       ├── diet/           # 菜单管理（FoodLibrary、DishManagement、DietPlan）
│       ├── training/       # 训练计划（ExerciseLibrary、TrainingPlans、TrainingCalendar）
│       ├── record/         # 数据记录（WeightRecord、BodyRecord）
│       ├── ai/             # AI 分析
│       ├── settings/       # 系统设置
│       └── admin/          # 后台管理页面
│           ├── AdminDashboard.vue   # 管理首页（统计+活动+概览）
│           ├── AdminFoods.vue       # 食物管理（CRUD+筛选+分页）
│           ├── AdminDishes.vue      # 菜品管理（CRUD+食材编辑+分页）
│           ├── AdminExercises.vue   # 动作管理（CRUD+筛选+分页）
│           ├── AdminUsers.vue       # 用户管理（状态管理+分页）
│           └── AdminTypes.vue       # 类型管理（分组CRUD+排序+引用校验）
```

### 3.4 数据库核心表设计

#### 用户相关
- `user` - 用户表（id, username, password, email, phone, avatar, ...）
- `user_profile` - 用户档案（user_id, height, current_weight, target_weight, body_fat, goal, activity_level, ...）
- `user_settings` - 用户设置（user_id, ai_api_key, ai_model, unit_preference, ...）

#### 菜单相关
- `food` - 食物库/食材库（id, name, category, calories, protein, carbs, fat, fiber, unit, source [official/user], user_id, image, created_at, ...）
- `dish` - 菜品库（id, name, description, calories, source [official/user], user_id, image, ...）
- `dish_ingredient` - 菜品食材明细（dish_id, food_id, amount, unit, ...）
- `meal_plan` - 餐次计划（id, user_id, date, meal_type, ...）
- `meal_plan_dish` - 餐次菜品明细（meal_plan_id, dish_id, ...）
- `menu_template` - 菜单模板（id, user_id, name, type, ...）

#### 训练相关
- `exercise` - 动作库（id, name, muscle_group, description, difficulty, ...）
- `training_plan` - 训练计划（id, user_id, name, description, duration_days, ...）
- `training_day` - 训练日安排（plan_id, day_number, is_rest_day, ...）
- `training_exercise` - 训练动作明细（day_id, exercise_id, sets, reps, weight, ...）
- `training_log` - 训练记录（id, user_id, plan_id, date, duration, notes, ...）
- `training_log_detail` - 训练日志明细（log_id, exercise_id, set_number, reps, weight, completed, ...）

#### 数据记录相关
- `weight_log` - 体重记录（id, user_id, date, weight, body_fat, bmi, ...）
- `body_measurement` - 身体围度记录（id, user_id, date, chest, waist, hip, arm, thigh, ...）
- `water_log` - 饮水记录（id, user_id, date, amount_ml, ...）

#### AI 相关
- `ai_report` - AI 分析报告（id, user_id, type, period_start, period_end, content, ...）

#### 通用类型表（全局分类管理）
- `type` - 类型/分类表（id, type_group, type_value, sort_order, created_at, ...）
  - `type_group`：类型分组标识，如 `food_category`、`dish_category`、`muscle_group`、`difficulty`、`fitness_goal`、`activity_level`、`meal_type`
  - `type_value`：类型显示值，如"肉类"、"早餐"、"胸"、"初级"
  - `sort_order`：同组内排序序号
  - 唯一约束：`(type_group, type_value)` 不可重复
- 其他表通过 `type_group` + `type_value` 引用此表，实现分类数据的统一管理
- 预置数据见 SQL 初始化脚本（`docs/init-data.sql`）

#### 管理后台相关
- `admin_user` - 管理员表（id, username, password, nickname, role, status, last_login_time, created_at, ...）
- `admin_role` - 角色表（id, name, description, permissions, ...）
- `admin_permission` - 权限表（id, name, code, type [menu/button/api], parent_id, ...）
- `admin_role_permission` - 角色权限关联（role_id, permission_id）
- `operation_log` - 操作日志（id, admin_id, module, action, target_id, target_name, detail, ip, created_at, ...）

**操作日志记录范围**：食物/菜品/动作的增删改、类型数据的增删改、用户状态变更、管理员登录登出

---

## 4. API 接口设计规范

### 4.1 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": 1714800000000
}
```

### 4.2 核心接口列表

| 模块 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 用户 | `/api/auth/register` | POST | 用户注册 |
| 用户 | `/api/auth/login` | POST | 用户登录 |
| 用户 | `/api/user/profile` | GET/PUT | 获取/更新个人档案 |
| 菜单 | `/api/foods` | GET | 食物列表（支持 source/category/keyword 筛选，分页，返回官方+当前用户食物） |
| 菜单 | `/api/foods` | POST | 添加食物（自动标记为用户个人食物） |
| 菜单 | `/api/foods/{id}` | PUT/DELETE | 编辑/删除食物（仅限个人食物） |
| 菜单 | `/api/dishes` | GET | 菜品列表（支持 source/category/keyword 筛选） |
| 菜单 | `/api/dishes` | POST | 创建菜品（用户个人） |
| 菜单 | `/api/dishes/{id}` | GET/PUT/DELETE | 菜品详情/编辑/删除（仅限个人菜品） |
| 菜单 | `/api/dishes/{id}/ingredients` | GET/PUT | 菜品食材明细查看/编辑 |
| 菜单 | `/api/meal-plans` | GET/POST | 餐次计划管理（含菜品关联） |
| 菜单 | `/api/meal-plans/week` | GET | 获取一周菜单（含菜品与食材明细） |
| 菜单 | `/api/meal-plans/month` | GET | 获取一月菜单（含菜品与食材明细） |
| 训练 | `/api/exercises` | GET/POST | 动作库查询/添加 |
| 训练 | `/api/training-plans` | GET/POST | 训练计划管理 |
| 训练 | `/api/training-logs` | GET/POST | 训练记录管理 |
| 数据 | `/api/weight-logs` | GET/POST | 体重记录 |
| 数据 | `/api/body-measurements` | GET/POST | 围度记录 |
| 数据 | `/api/dashboard` | GET | 数据看板汇总 |
| AI | `/api/ai/analyze` | POST | AI 数据分析 |
| AI | `/api/ai/reports` | GET | AI 报告列表 |
| AI | `/api/ai/test-connection` | POST | 测试 AI API 连通性 |
| 导出 | `/api/export/{type}` | GET | 导出数据 |

### 4.3 管理后台接口

> 所有 `/api/admin/**` 接口需管理员 JWT Token 认证，普通用户无权访问。

#### 4.3.1 管理员认证

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/auth/login` | POST | 管理员登录（返回 admin_token） |
| `/api/admin/auth/logout` | POST | 管理员登出 |
| `/api/admin/auth/info` | GET | 获取当前管理员信息与权限列表 |

#### 4.3.2 数据看板

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/dashboard/stats` | GET | 统计数据（用户数、菜品数、食物数、动作数） |
| `/api/admin/dashboard/activities` | GET | 最近活动列表（分页，支持 limit 参数） |
| `/api/admin/dashboard/overview` | GET | 数据概览（官方/用户食物数、官方/用户菜品数、分类统计等） |

#### 4.3.3 食物管理

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/foods` | GET | 食物列表（支持 source/category/keyword 筛选，分页，返回所有食物） |
| `/api/admin/foods` | POST | 新增官方食物（source 固定为 official） |
| `/api/admin/foods/{id}` | PUT | 编辑食物（管理员可编辑所有食物） |
| `/api/admin/foods/{id}` | DELETE | 删除食物（管理员可删除所有食物） |

#### 4.3.4 菜品管理

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/dishes` | GET | 菜品列表（支持 category/keyword 筛选，分页） |
| `/api/admin/dishes` | POST | 新增菜品（含食材明细） |
| `/api/admin/dishes/{id}` | PUT | 编辑菜品（含食材明细） |
| `/api/admin/dishes/{id}` | DELETE | 删除菜品 |

**请求体示例（POST/PUT）**：
```json
{
  "name": "香煎鸡胸肉",
  "description": "高蛋白低脂的健身餐首选",
  "category": "午餐",
  "ingredients": [
    { "foodId": 1, "amount": 150, "unit": "g" },
    { "foodId": 17, "amount": 5, "unit": "ml" }
  ]
}
```

> 热量由后端根据食材用量自动计算，前端不传。

#### 4.3.5 动作管理

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/exercises` | GET | 动作列表（支持 muscleGroup/keyword 筛选，分页） |
| `/api/admin/exercises` | POST | 新增动作 |
| `/api/admin/exercises/{id}` | PUT | 编辑动作 |
| `/api/admin/exercises/{id}` | DELETE | 删除动作 |

#### 4.3.6 用户管理

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/users` | GET | 用户列表（支持 status/keyword 筛选，分页） |
| `/api/admin/users/{id}/status` | PUT | 启用/禁用用户 |
| `/api/admin/users/{id}` | DELETE | 删除用户（级联删除用户相关数据） |

#### 4.3.7 操作日志

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/operation-logs` | GET | 操作日志列表（支持 admin_id/module/action 筛选，分页） |

#### 4.3.8 类型管理

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/types` | GET | 类型列表（支持 type_group 筛选，按 sort_order 排序） |
| `/api/admin/types` | POST | 新增类型（type_group + type_value） |
| `/api/admin/types/{id}` | PUT | 编辑类型（修改 type_value 或 sort_order） |
| `/api/admin/types/{id}` | DELETE | 删除类型（有数据引用时返回 409 冲突，body 含引用数量） |

**请求体示例（POST）**：
```json
{
  "typeGroup": "food_category",
  "typeName": "海鲜",
  "sortOrder": 4
}
```

**删除冲突响应示例**：
```json
{
  "code": 409,
  "message": "该分类正在被 5 条食物数据引用，无法删除",
  "data": { "referencedCount": 5 }
}
```

#### 4.3.9 公共类型查询（用户端）

> 供用户端页面加载下拉选项数据，无需管理员权限。

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/types/{group}` | GET | 按分组查询类型列表（如 `/api/types/food_category`），返回按 sort_order 排序的类型值数组 |

---

## 5. 非功能性需求

### 5.1 安全性
- 密码 BCrypt 加密存储
- JWT Token 认证，支持过期刷新
- **管理员独立认证**：admin_token 与 user_token 分离，`/api/admin/**` 接口拦截校验管理员身份
- **权限控制**：RBAC 模型，管理员角色绑定权限集合，接口级权限校验
- **操作审计**：所有管理端写操作（增删改）自动记录到 `operation_log` 表
- API Key 加密存储（用户配置的 AI Key）
- SQL 注入防护（MyBatis-Plus 参数化查询）
- XSS 防护（前端输入过滤）
- CORS 跨域配置

### 5.2 性能要求
- 页面首屏加载 < 2s
- API 响应时间 < 500ms（常规接口）
- AI 分析接口支持异步处理，不阻塞前端
- 大数据量分页查询
- Redis 缓存热点数据（食物库、动作库）

### 5.3 可扩展性
- 模块化设计，各功能模块独立
- 预留第三方登录扩展接口
- AI 模块可扩展支持更多 AI 提供商
- 数据导出格式可扩展

### 5.4 兼容性
- 前端支持 Chrome、Firefox、Safari、Edge 最新版
- 响应式布局，适配 PC 端和移动端

---

## 6. 项目里程碑建议

| 阶段 | 内容 | 预估周期 |
|------|------|----------|
| P0 - 基础框架 | 项目初始化、用户管理、基础 UI 框架 | 1 周 |
| P1 - 菜单管理 | 食物库、菜品库、饮食计划（周+月）、营养计算 | 2 周 |
| P2 - 训练计划 | 动作库、训练计划、训练日历 | 2 周 |
| P3 - 数据记录 | 体重/围度记录、数据看板 | 1 周 |
| P4 - AI 分析 | AI 集成、分析报告、问答 | 1 周 |
| P5 - 后台管理 | 管理员体系、食物/菜品/动作/用户管理、操作日志 | 1 周 |
| P6 - 数据导出 | CSV/Excel/PDF 导出 | 3 天 |
| P7 - 优化收尾 | 性能优化、测试、部署 | 1 周 |

---

## 7. 部署方案

### 开发环境
- 前端：Vite Dev Server（端口 5173）
- 后端：Spring Boot 内嵌 Tomcat（端口 8080）
- 数据库：本地 MySQL
- 缓存：本地 Redis

### 生产环境
- 前端：Nginx 静态部署
- 后端：Docker 容器化部署
- 数据库：MySQL 主从
- 反向代理：Nginx（同时代理前端和后端 API）
