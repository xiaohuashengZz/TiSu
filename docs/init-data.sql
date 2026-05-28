-- ============================================================
-- TiSu (体塑) 数据库初始化脚本
-- 版本：v1.0 | 日期：2026-05-04
-- 说明：包含建表语句和前端 mock 数据对应的初始数据
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------
-- 1. 类型/分类表（全局分类管理）
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `type_group` VARCHAR(50) NOT NULL COMMENT '类型分组标识',
  `type_value` VARCHAR(50) NOT NULL COMMENT '类型显示值',
  `sort_order` INT DEFAULT 0 COMMENT '同组内排序序号',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_value` (`type_group`, `type_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类型/分类表';

-- -----------------------------------------------------------
-- 2. 用户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(100) NOT NULL COMMENT 'BCrypt加密',
  `email` VARCHAR(100) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `nickname` VARCHAR(50) DEFAULT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  `status` TINYINT DEFAULT 1 COMMENT '1=正常 0=禁用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -----------------------------------------------------------
-- 3. 用户档案表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `gender` VARCHAR(10) DEFAULT NULL,
  `age` INT DEFAULT NULL,
  `height` DECIMAL(5,1) DEFAULT NULL COMMENT 'cm',
  `current_weight` DECIMAL(5,1) DEFAULT NULL COMMENT 'kg',
  `target_weight` DECIMAL(5,1) DEFAULT NULL COMMENT 'kg',
  `body_fat` DECIMAL(4,1) DEFAULT NULL COMMENT '体脂率%',
  `fitness_goal` VARCHAR(20) DEFAULT NULL COMMENT '减脂/增肌/维持/塑形',
  `activity_level` VARCHAR(20) DEFAULT NULL COMMENT '活动等级',
  `daily_calorie_target` INT DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户档案表';

-- -----------------------------------------------------------
-- 4. 用户设置表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `user_settings`;
CREATE TABLE `user_settings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `ai_api_key` VARCHAR(255) DEFAULT NULL COMMENT '加密存储',
  `ai_model` VARCHAR(50) DEFAULT NULL,
  `unit_preference` VARCHAR(20) DEFAULT 'metric' COMMENT 'metric/imperial',
  `notification_enabled` TINYINT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户设置表';

-- -----------------------------------------------------------
-- 5. 食物库表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `category` VARCHAR(50) NOT NULL COMMENT '食物分类',
  `calories` INT NOT NULL COMMENT '热量(kcal/100g)',
  `protein` DECIMAL(6,1) DEFAULT 0 COMMENT '蛋白质(g)',
  `carbs` DECIMAL(6,1) DEFAULT 0 COMMENT '碳水(g)',
  `fat` DECIMAL(6,1) DEFAULT 0 COMMENT '脂肪(g)',
  `fiber` DECIMAL(6,1) DEFAULT 0 COMMENT '膳食纤维(g)',
  `unit` VARCHAR(20) DEFAULT '100g',
  `source` VARCHAR(20) NOT NULL DEFAULT 'official' COMMENT 'official=官方食物 user=用户食物',
  `user_id` BIGINT DEFAULT NULL COMMENT '创建者ID（source=user时）',
  `image` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_source` (`source`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食物库表';

-- -----------------------------------------------------------
-- 6. 菜品库表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(500) DEFAULT NULL,
  `calories` INT DEFAULT 0 COMMENT '总热量(kcal)',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '菜品分类',
  `source` VARCHAR(20) NOT NULL DEFAULT 'official' COMMENT 'official=官方推荐 user=用户创建',
  `user_id` BIGINT DEFAULT NULL COMMENT '创建者ID（source=user时）',
  `image` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_source` (`source`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品库表';

-- -----------------------------------------------------------
-- 7. 菜品食材明细表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `dish_ingredient`;
CREATE TABLE `dish_ingredient` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `dish_id` BIGINT NOT NULL,
  `food_id` BIGINT NOT NULL,
  `amount` DECIMAL(8,1) NOT NULL COMMENT '用量',
  `unit` VARCHAR(20) NOT NULL COMMENT 'g/个/ml',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_dish_id` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品食材明细表';

-- -----------------------------------------------------------
-- 8. 餐次计划表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `meal_plan`;
CREATE TABLE `meal_plan` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `date` DATE NOT NULL COMMENT '日期',
  `meal_type` VARCHAR(20) NOT NULL COMMENT '早餐/午餐/晚餐/加餐',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐次计划表';

-- -----------------------------------------------------------
-- 9. 餐次菜品明细表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `meal_plan_dish`;
CREATE TABLE `meal_plan_dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `meal_plan_id` BIGINT NOT NULL,
  `dish_id` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_meal_plan_id` (`meal_plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐次菜品明细表';

-- -----------------------------------------------------------
-- 10. 动作库表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `exercise`;
CREATE TABLE `exercise` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `muscle_group` VARCHAR(50) NOT NULL COMMENT '目标肌群',
  `difficulty` VARCHAR(20) DEFAULT NULL COMMENT '难度等级',
  `description` VARCHAR(500) DEFAULT NULL,
  `sets` INT DEFAULT NULL COMMENT '默认组数',
  `reps` INT DEFAULT NULL COMMENT '默认次数',
  `image` VARCHAR(255) DEFAULT NULL,
  `video_url` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_muscle_group` (`muscle_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动作库表';

-- -----------------------------------------------------------
-- 11. 训练计划表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `training_plan`;
CREATE TABLE `training_plan` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT DEFAULT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(500) DEFAULT NULL,
  `duration_days` INT DEFAULT NULL COMMENT '周期天数',
  `level` VARCHAR(20) DEFAULT NULL COMMENT '难度等级',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练计划表';

-- -----------------------------------------------------------
-- 12. 训练日安排表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `training_day`;
CREATE TABLE `training_day` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `plan_id` BIGINT NOT NULL,
  `day_number` INT NOT NULL COMMENT '第几天',
  `label` VARCHAR(50) DEFAULT NULL COMMENT '如：胸+三头',
  `is_rest_day` TINYINT DEFAULT 0 COMMENT '1=休息日',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练日安排表';

-- -----------------------------------------------------------
-- 13. 训练动作明细表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `training_exercise`;
CREATE TABLE `training_exercise` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `day_id` BIGINT NOT NULL,
  `exercise_id` BIGINT NOT NULL,
  `sets` INT DEFAULT NULL,
  `reps` INT DEFAULT NULL,
  `weight` VARCHAR(20) DEFAULT NULL COMMENT '重量描述',
  `sort_order` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_day_id` (`day_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练动作明细表';

-- -----------------------------------------------------------
-- 14. 训练记录表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `training_log`;
CREATE TABLE `training_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `plan_id` BIGINT DEFAULT NULL,
  `date` DATE NOT NULL,
  `duration` INT DEFAULT 0 COMMENT '时长(分钟)',
  `notes` VARCHAR(500) DEFAULT NULL,
  `calories_burned` INT DEFAULT 0 COMMENT '消耗热量估算',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练记录表';

-- -----------------------------------------------------------
-- 15. 训练日志明细表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `training_log_detail`;
CREATE TABLE `training_log_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `log_id` BIGINT NOT NULL,
  `exercise_id` BIGINT NOT NULL,
  `set_number` INT NOT NULL,
  `reps` INT DEFAULT NULL,
  `weight` DECIMAL(8,2) DEFAULT NULL COMMENT 'kg',
  `completed` TINYINT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_log_id` (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='训练日志明细表';

-- -----------------------------------------------------------
-- 16. 体重记录表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `weight_log`;
CREATE TABLE `weight_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  `weight` DECIMAL(5,1) NOT NULL COMMENT 'kg',
  `body_fat` DECIMAL(4,1) DEFAULT NULL COMMENT '体脂率%',
  `bmi` DECIMAL(4,1) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体重记录表';

-- -----------------------------------------------------------
-- 17. 身体围度记录表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `body_measurement`;
CREATE TABLE `body_measurement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  `chest` DECIMAL(5,1) DEFAULT NULL COMMENT '胸围cm',
  `waist` DECIMAL(5,1) DEFAULT NULL COMMENT '腰围cm',
  `hip` DECIMAL(5,1) DEFAULT NULL COMMENT '臀围cm',
  `arm` DECIMAL(5,1) DEFAULT NULL COMMENT '臂围cm',
  `thigh` DECIMAL(5,1) DEFAULT NULL COMMENT '腿围cm',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='身体围度记录表';

-- -----------------------------------------------------------
-- 18. 饮水记录表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `water_log`;
CREATE TABLE `water_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  `amount_ml` INT NOT NULL COMMENT '饮水量ml',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='饮水记录表';

-- -----------------------------------------------------------
-- 19. AI 分析报告表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `ai_report`;
CREATE TABLE `ai_report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `type` VARCHAR(20) NOT NULL COMMENT 'weekly/monthly',
  `period_start` DATE DEFAULT NULL,
  `period_end` DATE DEFAULT NULL,
  `summary` VARCHAR(500) DEFAULT NULL,
  `content` TEXT COMMENT '报告正文(Markdown)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_type` (`user_id`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI分析报告表';

-- -----------------------------------------------------------
-- 20. 管理员表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(100) NOT NULL COMMENT 'BCrypt加密',
  `nickname` VARCHAR(50) DEFAULT NULL,
  `role` VARCHAR(20) DEFAULT 'admin' COMMENT 'admin/superadmin',
  `status` TINYINT DEFAULT 1 COMMENT '1=正常 0=禁用',
  `last_login_time` DATETIME DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- -----------------------------------------------------------
-- 21. 管理员角色表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(200) DEFAULT NULL,
  `permissions` TEXT COMMENT '权限JSON',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员角色表';

-- -----------------------------------------------------------
-- 22. 操作日志表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `admin_id` BIGINT NOT NULL,
  `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
  `action` VARCHAR(50) NOT NULL COMMENT '操作类型',
  `target_id` BIGINT DEFAULT NULL,
  `target_name` VARCHAR(100) DEFAULT NULL,
  `detail` VARCHAR(500) DEFAULT NULL,
  `ip` VARCHAR(50) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_admin_id` (`admin_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ============================================================
-- 初始数据插入
-- ============================================================

-- -----------------------------------------------------------
-- 类型表初始数据
-- -----------------------------------------------------------
INSERT INTO `type` (`type_group`, `type_value`, `sort_order`) VALUES
-- 食物分类
('food_category', '肉类', 1),
('food_category', '主食', 2),
('food_category', '蔬菜', 3),
('food_category', '海鲜', 4),
('food_category', '水果', 5),
('food_category', '豆类', 6),
('food_category', '乳制品', 7),
('food_category', '零食', 8),
('food_category', '蛋类', 9),
('food_category', '调味品', 10),
-- 菜品分类
('dish_category', '早餐', 1),
('dish_category', '午餐', 2),
('dish_category', '晚餐', 3),
('dish_category', '加餐', 4),
('dish_category', '通用', 5),
-- 目标肌群
('muscle_group', '胸', 1),
('muscle_group', '背', 2),
('muscle_group', '肩', 3),
('muscle_group', '臂', 4),
('muscle_group', '腿', 5),
('muscle_group', '核心', 6),
('muscle_group', '有氧', 7),
-- 动作难度
('difficulty', '初级', 1),
('difficulty', '中级', 2),
('difficulty', '高级', 3),
-- 健身目标
('fitness_goal', '减脂', 1),
('fitness_goal', '增肌', 2),
('fitness_goal', '维持', 3),
('fitness_goal', '塑形', 4),
-- 活动等级
('activity_level', '久坐', 1),
('activity_level', '轻度活动', 2),
('activity_level', '中度活动', 3),
('activity_level', '高强度活动', 4),
-- 餐次类型
('meal_type', '早餐', 1),
('meal_type', '午餐', 2),
('meal_type', '晚餐', 3),
('meal_type', '加餐', 4);

-- -----------------------------------------------------------
-- 管理员初始数据
-- -----------------------------------------------------------
INSERT INTO `admin_user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', '超级管理员', 'superadmin');

-- -----------------------------------------------------------
-- 用户初始数据（模拟注册用户）
-- 密码统一为 123456 的 BCrypt 加密
-- -----------------------------------------------------------
INSERT INTO `user` (`id`, `username`, `password`, `email`, `nickname`, `status`) VALUES
(1, 'fitness01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'fitness@example.com', '健身达人', 1),
(2, 'wang01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'wang@example.com', '减脂小王', 1),
(3, 'muscle01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'muscle@example.com', '增肌计划', 1),
(4, 'health01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'health@example.com', '健康生活', 1),
(5, 'runner01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'runner@example.com', '跑步爱好者', 1),
(6, 'shape01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'shape@example.com', '塑形达人', 0),
(7, 'strength01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'strength@example.com', '力量训练', 1),
(8, 'yoga01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'yoga@example.com', '瑜伽爱好者', 1),
(9, 'newbie01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'newbie@example.com', '新手小白', 0),
(10, 'protein01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'protein@example.com', '蛋白粉达人', 1),
(11, 'light01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'light@example.com', '轻食主义', 1),
(12, 'coach01', '$2a$10$N.ZOn9MHQb288bBjBuRxEOjlQQL2Vl/6G2VBtPMkxbWJbYqXKbKGu', 'coach@example.com', '健身教练', 1);

-- -----------------------------------------------------------
-- 用户档案初始数据（用户1为主要演示用户）
-- -----------------------------------------------------------
INSERT INTO `user_profile` (`user_id`, `gender`, `age`, `height`, `current_weight`, `target_weight`, `body_fat`, `fitness_goal`, `activity_level`, `daily_calorie_target`) VALUES
(1, '男', 28, 175.0, 72.0, 68.0, 18.5, '减脂', '中度活动', 1800),
(2, '女', 25, 162.0, 60.0, 55.0, 28.0, '减脂', '轻度活动', 1500),
(3, '男', 30, 180.0, 75.0, 80.0, 15.0, '增肌', '高强度活动', 2800);

-- -----------------------------------------------------------
-- 用户设置初始数据
-- -----------------------------------------------------------
INSERT INTO `user_settings` (`user_id`, `ai_model`, `unit_preference`) VALUES
(1, 'gpt-4o', 'metric'),
(2, 'claude-sonnet', 'metric');

-- -----------------------------------------------------------
-- 食物库初始数据
-- ID 1-18: 官方食物 (source=official)
-- ID 19-22: 用户食物 (source=user, user_id=1)
-- ID 23-36: 菜品中引用的额外食材 (source=official)
-- -----------------------------------------------------------
INSERT INTO `food` (`id`, `name`, `category`, `calories`, `protein`, `carbs`, `fat`, `fiber`, `unit`, `source`, `user_id`) VALUES
-- 官方食物
(1, '鸡胸肉', '肉类', 165, 31.0, 0.0, 3.6, 0.0, '100g', 'official', NULL),
(2, '糙米饭', '主食', 123, 2.7, 25.6, 0.9, 1.6, '100g', 'official', NULL),
(3, '西兰花', '蔬菜', 34, 2.8, 6.6, 0.4, 2.6, '100g', 'official', NULL),
(4, '鸡蛋', '蛋类', 144, 13.3, 1.1, 9.5, 0.0, '100g', 'official', NULL),
(5, '三文鱼', '海鲜', 208, 20.4, 0.0, 13.4, 0.0, '100g', 'official', NULL),
(6, '红薯', '主食', 86, 1.6, 20.1, 0.1, 3.0, '100g', 'official', NULL),
(7, '牛油果', '水果', 160, 2.0, 8.5, 14.7, 6.7, '100g', 'official', NULL),
(8, '燕麦', '主食', 389, 16.9, 66.3, 6.9, 10.6, '100g', 'official', NULL),
(9, '牛肉(瘦)', '肉类', 106, 20.2, 0.0, 2.3, 0.0, '100g', 'official', NULL),
(10, '豆腐', '豆类', 76, 8.1, 1.9, 3.7, 0.3, '100g', 'official', NULL),
(11, '香蕉', '水果', 89, 1.1, 22.8, 0.3, 2.6, '100g', 'official', NULL),
(12, '菠菜', '蔬菜', 23, 2.9, 3.6, 0.4, 2.2, '100g', 'official', NULL),
(13, '虾仁', '海鲜', 99, 24.0, 0.2, 0.3, 0.0, '100g', 'official', NULL),
(14, '全麦面包', '主食', 247, 13.0, 41.0, 3.4, 7.0, '100g', 'official', NULL),
(15, '希腊酸奶', '乳制品', 97, 9.0, 3.6, 5.0, 0.0, '100g', 'official', NULL),
(16, '坚果混合', '零食', 607, 20.0, 21.0, 54.0, 7.0, '100g', 'official', NULL),
(17, '生菜', '蔬菜', 15, 1.4, 2.9, 0.2, 1.3, '100g', 'official', NULL),
(18, '胡萝卜', '蔬菜', 41, 0.9, 9.6, 0.2, 2.8, '100g', 'official', NULL),
-- 用户食物 (用户1: fitness01)
(19, '荞麦面', '主食', 134, 4.5, 26.0, 1.1, 1.8, '100g', 'user', 1),
(20, '鸡腿肉', '肉类', 181, 26.0, 0.0, 8.0, 0.0, '100g', 'user', 1),
(21, '藜麦', '主食', 120, 4.4, 21.3, 1.9, 2.8, '100g', 'user', 1),
(22, '毛豆', '豆类', 122, 11.9, 8.9, 5.2, 5.2, '100g', 'user', 1),
-- 菜品中引用的额外食材
(23, '橄榄油', '调味品', 884, 0.0, 0.0, 100.0, 0.0, '100ml', 'official', NULL),
(24, '蒜', '调味品', 149, 6.4, 33.1, 0.5, 2.1, '100g', 'official', NULL),
(25, '姜', '调味品', 80, 1.8, 17.8, 0.8, 2.0, '100g', 'official', NULL),
(26, '酱油', '调味品', 53, 5.6, 5.6, 0.1, 0.0, '100ml', 'official', NULL),
(27, '蜂蜜', '调味品', 304, 0.3, 82.4, 0.0, 0.2, '100ml', 'official', NULL),
(28, '醋', '调味品', 18, 0.0, 3.6, 0.0, 0.0, '100ml', 'official', NULL),
(29, '柠檬', '水果', 29, 1.1, 9.3, 0.3, 2.8, '100g', 'official', NULL),
(30, '青椒', '蔬菜', 20, 0.9, 4.6, 0.2, 1.7, '100g', 'official', NULL),
(31, '糙米', '主食', 348, 7.4, 72.9, 2.7, 3.4, '100g', 'official', NULL),
(32, '面条', '主食', 138, 4.5, 25.5, 2.1, 1.2, '100g', 'official', NULL),
(33, '番茄', '蔬菜', 18, 0.9, 3.9, 0.2, 1.2, '100g', 'official', NULL),
(34, '紫薯', '主食', 82, 1.6, 18.0, 0.1, 3.0, '100g', 'official', NULL),
(35, '牛奶', '乳制品', 42, 3.4, 5.0, 1.0, 0.0, '100ml', 'official', NULL),
(36, '土豆', '主食', 77, 2.0, 17.5, 0.1, 2.2, '100g', 'official', NULL);

-- -----------------------------------------------------------
-- 菜品库初始数据
-- ID 1-24: 官方推荐 (source=official)
-- ID 25-27: 用户创建 (source=user, user_id=1)
-- ID 28: 一周菜单中的额外菜品 (source=official)
-- -----------------------------------------------------------
INSERT INTO `dish` (`id`, `name`, `description`, `calories`, `category`, `source`, `user_id`) VALUES
-- 官方推荐 - 早餐
(1, '牛奶燕麦粥', '高纤维低GI早餐，提供持久饱腹感', 250, '早餐', 'official', NULL),
(2, '水煮蛋', '优质蛋白质来源，简单易做', 144, '早餐', 'official', NULL),
(3, '牛油果吐司', '健康脂肪与复合碳水的完美组合', 315, '早餐', 'official', NULL),
(4, '希腊酸奶碗', '高蛋白低脂，搭配水果更佳', 146, '早餐', 'official', NULL),
-- 官方推荐 - 午餐
(5, '香煎鸡胸肉', '高蛋白低脂的健身餐首选', 265, '午餐', 'official', NULL),
(6, '青椒炒牛肉', '富含铁质和蛋白质', 218, '午餐', 'official', NULL),
(7, '西兰花炒牛肉', '高蛋白高纤维，营养均衡', 225, '午餐', 'official', NULL),
(8, '照烧鸡胸肉', '日式风味，低脂美味', 280, '午餐', 'official', NULL),
-- 官方推荐 - 通用
(9, '糙米饭', '低GI主食，富含膳食纤维', 185, '通用', 'official', NULL),
(10, '清炒西兰花', '高纤维低热量蔬菜', 58, '通用', 'official', NULL),
-- 官方推荐 - 晚餐
(11, '香煎三文鱼', '富含Omega-3脂肪酸', 270, '晚餐', 'official', NULL),
(12, '柠香烤三文鱼', '清新柠檬风味，低脂高蛋白', 260, '晚餐', 'official', NULL),
(13, '清炒虾仁', '高蛋白低脂海鲜菜品', 155, '晚餐', 'official', NULL),
(14, '家常豆腐', '植物蛋白丰富，口感嫩滑', 130, '晚餐', 'official', NULL),
(15, '土豆炖牛肉', '经典家常菜，饱腹感强', 260, '晚餐', 'official', NULL),
(16, '白灼鸡胸肉', '清淡少油，保留原味', 248, '晚餐', 'official', NULL),
(17, '烤红薯', '低GI优质碳水', 150, '通用', 'official', NULL),
(18, '蒜蓉菠菜', '富含铁质和维生素', 30, '通用', 'official', NULL),
(19, '蔬菜沙拉', '低热量高纤维', 45, '通用', 'official', NULL),
(20, '凉拌生菜', '清爽开胃，低热量', 30, '通用', 'official', NULL),
(21, '清炒胡萝卜丝', '富含β-胡萝卜素', 55, '通用', 'official', NULL),
-- 官方推荐 - 加餐
(22, '酸奶坚果碗', '蛋白质与健康脂肪的完美加餐', 267, '加餐', 'official', NULL),
(23, '坚果小食', '便携高能加餐', 121, '加餐', 'official', NULL),
(24, '香蕉', '快速补充能量', 90, '加餐', 'official', NULL),
-- 用户自创菜品
(25, '番茄鸡蛋面', '家常快手面，酸甜可口', 380, '午餐', 'user', 1),
(26, '紫薯燕麦奶昔', '高纤维早餐奶昔', 220, '早餐', 'user', 1),
(27, '鸡胸肉沙拉', '低脂高蛋白减脂餐', 290, '午餐', 'user', 1),
-- 一周菜单中的额外菜品
(28, '酸奶香蕉碗', '酸甜可口的蛋白质加餐', 236, '加餐', 'official', NULL);

-- -----------------------------------------------------------
-- 菜品食材明细
-- food_id 参照: 鸡胸肉=1, 糙米饭=2, 西兰花=3, 鸡蛋=4, 三文鱼=5, 红薯=6,
--   牛油果=7, 燕麦=8, 牛肉(瘦)=9, 豆腐=10, 香蕉=11, 菠菜=12, 虾仁=13,
--   全麦面包=14, 希腊酸奶=15, 坚果混合=16, 生菜=17, 胡萝卜=18,
--   橄榄油=23, 蒜=24, 姜=25, 酱油=26, 蜂蜜=27, 醋=28, 柠檬=29,
--   青椒=30, 糙米=31, 面条=32, 番茄=33, 紫薯=34, 牛奶=35, 土豆=36
-- -----------------------------------------------------------
INSERT INTO `dish_ingredient` (`dish_id`, `food_id`, `amount`, `unit`) VALUES
-- 1: 牛奶燕麦粥
(1, 8, 50, 'g'), (1, 35, 250, 'ml'),
-- 2: 水煮蛋
(2, 4, 2, '个'),
-- 3: 牛油果吐司
(3, 14, 60, 'g'), (3, 7, 50, 'g'), (3, 4, 1, '个'),
-- 4: 希腊酸奶碗
(4, 15, 150, 'g'),
-- 5: 香煎鸡胸肉
(5, 1, 150, 'g'), (5, 23, 5, 'ml'),
-- 6: 青椒炒牛肉
(6, 9, 150, 'g'), (6, 30, 50, 'g'), (6, 23, 5, 'ml'),
-- 7: 西兰花炒牛肉
(7, 9, 150, 'g'), (7, 3, 100, 'g'), (7, 23, 5, 'ml'),
-- 8: 照烧鸡胸肉
(8, 1, 150, 'g'), (8, 26, 10, 'ml'), (8, 27, 5, 'ml'),
-- 9: 糙米饭
(9, 31, 80, 'g'),
-- 10: 清炒西兰花
(10, 3, 150, 'g'), (10, 24, 5, 'g'),
-- 11: 香煎三文鱼
(11, 5, 120, 'g'), (11, 23, 5, 'ml'),
-- 12: 柠香烤三文鱼
(12, 5, 120, 'g'), (12, 29, 1, '个'),
-- 13: 清炒虾仁
(13, 13, 150, 'g'), (13, 23, 3, 'ml'),
-- 14: 家常豆腐
(14, 10, 150, 'g'), (14, 26, 5, 'ml'),
-- 15: 土豆炖牛肉
(15, 9, 150, 'g'), (15, 36, 100, 'g'),
-- 16: 白灼鸡胸肉
(16, 1, 150, 'g'), (16, 25, 5, 'g'),
-- 17: 烤红薯
(17, 6, 200, 'g'),
-- 18: 蒜蓉菠菜
(18, 12, 120, 'g'), (18, 24, 5, 'g'),
-- 19: 蔬菜沙拉
(19, 17, 80, 'g'), (19, 18, 30, 'g'),
-- 20: 凉拌生菜
(20, 17, 100, 'g'), (20, 28, 5, 'ml'),
-- 21: 清炒胡萝卜丝
(21, 18, 100, 'g'), (21, 23, 3, 'ml'),
-- 22: 酸奶坚果碗
(22, 15, 150, 'g'), (22, 16, 20, 'g'),
-- 23: 坚果小食
(23, 16, 20, 'g'),
-- 24: 香蕉
(24, 11, 1, '根'),
-- 25: 番茄鸡蛋面
(25, 32, 100, 'g'), (25, 4, 2, '个'), (25, 33, 150, 'g'),
-- 26: 紫薯燕麦奶昔
(26, 34, 100, 'g'), (26, 8, 30, 'g'), (26, 35, 200, 'ml'),
-- 27: 鸡胸肉沙拉
(27, 1, 120, 'g'), (27, 17, 100, 'g'), (27, 7, 30, 'g'), (27, 23, 5, 'ml'),
-- 28: 酸奶香蕉碗
(28, 15, 150, 'g'), (28, 11, 1, '根');

-- -----------------------------------------------------------
-- 动作库初始数据
-- -----------------------------------------------------------
INSERT INTO `exercise` (`id`, `name`, `muscle_group`, `difficulty`, `description`, `sets`, `reps`) VALUES
(1, '卧推', '胸', '中级', '平躺于卧推凳，双手握杠铃，推起至手臂伸直后缓慢下放。', 4, 10),
(2, '深蹲', '腿', '中级', '双脚与肩同宽，屈膝下蹲至大腿平行地面后站起。', 4, 10),
(3, '硬拉', '背', '高级', '双脚与肩同宽，弯腰握杠铃，挺直腰背站起。', 4, 8),
(4, '引体向上', '背', '中级', '双手正握杠，身体上拉至下巴过杠后缓慢下放。', 3, 8),
(5, '肩推', '肩', '中级', '坐姿，双手持哑铃从肩部推至头顶伸直。', 4, 10),
(6, '弯举', '臂', '初级', '站立，双手持哑铃，以肘为轴弯举至肩前。', 3, 12),
(7, '俯卧撑', '胸', '初级', '双手撑地，身体下压至胸部接近地面后推起。', 3, 15),
(8, '平板支撑', '核心', '初级', '双肘撑地，身体保持一条直线，核心收紧。', 3, 60),
(9, '腿举', '腿', '初级', '坐于腿举机上，双脚蹬踏板至腿伸直。', 4, 12),
(10, '飞鸟', '胸', '中级', '平躺，双手持哑铃向两侧打开后合拢至胸前。', 3, 12),
(11, '划船', '背', '中级', '俯身，双手持哑铃向腰部方向拉起。', 4, 10),
(12, '侧平举', '肩', '初级', '站立，双手持哑铃向两侧平举至肩高。', 3, 15),
(13, '跑步', '有氧', '初级', '中等配速持续跑步，保持心率在有氧区间。', 1, 30),
(14, '跳绳', '有氧', '初级', '双脚交替或并脚跳跃，保持节奏。', 3, 100);

-- -----------------------------------------------------------
-- 训练计划初始数据
-- -----------------------------------------------------------
INSERT INTO `training_plan` (`id`, `name`, `description`, `duration_days`, `level`) VALUES
(1, '新手减脂计划', '适合健身新手的4周减脂训练计划，以复合动作为主，搭配有氧训练。', 28, '初级'),
(2, '进阶增肌计划', '针对有一定基础的训练者，采用推拉腿分化训练。', 56, '中级');

-- 训练日安排
INSERT INTO `training_day` (`id`, `plan_id`, `day_number`, `label`, `is_rest_day`) VALUES
-- 新手减脂计划
(1, 1, 1, '胸+三头', 0),
(2, 1, 2, '背+二头', 0),
(3, 1, 3, '休息日', 1),
(4, 1, 4, '腿+核心', 0),
(5, 1, 5, '肩+有氧', 0),
(6, 1, 6, '有氧日', 0),
(7, 1, 7, '休息日', 1),
-- 进阶增肌计划
(8, 2, 1, '推(胸/肩/三头)', 0),
(9, 2, 2, '拉(背/二头)', 0),
(10, 2, 3, '腿', 0),
(11, 2, 4, '休息日', 1),
(12, 2, 5, '推(胸/肩/三头)', 0),
(13, 2, 6, '拉(背/二头)', 0),
(14, 2, 7, '休息日', 1);

-- 训练动作明细
-- exercise_id: 卧推=1, 深蹲=2, 硬拉=3, 引体向上=4, 肩推=5, 弯举=6,
--   俯卧撑=7, 平板支撑=8, 腿举=9, 飞鸟=10, 划船=11, 侧平举=12, 跑步=13, 跳绳=14
INSERT INTO `training_exercise` (`day_id`, `exercise_id`, `sets`, `reps`, `weight`, `sort_order`) VALUES
-- 新手减脂计划 Day1: 胸+三头
(1, 1, 4, 10, '40kg', 1), (1, 10, 3, 12, '10kg', 2), (1, 7, 3, 15, '自重', 3),
-- Day2: 背+二头
(2, 4, 3, 8, '自重', 1), (2, 11, 4, 10, '20kg', 2), (2, 6, 3, 12, '10kg', 3),
-- Day3: 休息日 (无动作)
-- Day4: 腿+核心
(4, 2, 4, 10, '50kg', 1), (4, 9, 4, 12, '80kg', 2), (4, 8, 3, 60, '自重', 3),
-- Day5: 肩+有氧
(5, 5, 4, 10, '15kg', 1), (5, 12, 3, 15, '5kg', 2), (5, 13, 1, 30, '-', 3),
-- Day6: 有氧日
(6, 13, 1, 40, '-', 1), (6, 14, 3, 100, '-', 2),
-- Day7: 休息日 (无动作)

-- 进阶增肌计划 Day1: 推(胸/肩/三头)
(8, 1, 5, 5, '70kg', 1), (8, 5, 4, 8, '25kg', 2), (8, 10, 3, 12, '15kg', 3),
-- Day2: 拉(背/二头)
(9, 3, 5, 5, '100kg', 1), (9, 4, 4, 8, '自重', 2), (9, 6, 3, 10, '15kg', 3),
-- Day3: 腿
(10, 2, 5, 5, '80kg', 1), (10, 9, 4, 10, '120kg', 2),
-- Day4: 休息日 (无动作)
-- Day5: 推(胸/肩/三头)
(12, 1, 4, 8, '65kg', 1), (12, 5, 4, 10, '20kg', 2), (12, 12, 3, 15, '8kg', 3),
-- Day6: 拉(背/二头)
(13, 11, 4, 8, '30kg', 1), (13, 4, 4, 10, '自重', 2), (13, 6, 3, 12, '12kg', 3);
-- Day7: 休息日 (无动作)

-- -----------------------------------------------------------
-- 一周菜单初始数据 (meal_plan + meal_plan_dish)
-- 用户1，日期: 2026-04-28(周一) ~ 2026-05-04(周日)
-- dish_id 参照菜品表
-- -----------------------------------------------------------

-- 周一 2026-04-28
INSERT INTO `meal_plan` (`id`, `user_id`, `date`, `meal_type`) VALUES
(1, 1, '2026-04-28', '早餐'),
(2, 1, '2026-04-28', '午餐'),
(3, 1, '2026-04-28', '晚餐'),
(4, 1, '2026-04-28', '加餐');
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`) VALUES
(1, 1), (1, 2), (1, 24),  -- 牛奶燕麦粥、水煮蛋、香蕉
(2, 5), (2, 10), (2, 9),  -- 香煎鸡胸肉、清炒西兰花、糙米饭
(3, 11), (3, 17), (3, 19), -- 香煎三文鱼、烤红薯、蔬菜沙拉
(4, 22);                    -- 酸奶坚果碗

-- 周二 2026-04-29
INSERT INTO `meal_plan` (`id`, `user_id`, `date`, `meal_type`) VALUES
(5, 1, '2026-04-29', '早餐'),
(6, 1, '2026-04-29', '午餐'),
(7, 1, '2026-04-29', '晚餐'),
(8, 1, '2026-04-29', '加餐');
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`) VALUES
(5, 3), (5, 2),           -- 牛油果吐司、水煮蛋
(6, 6), (6, 18), (6, 9),  -- 青椒炒牛肉、蒜蓉菠菜、糙米饭
(7, 13), (7, 14), (7, 21), -- 清炒虾仁、家常豆腐、清炒胡萝卜丝
(8, 4);                    -- 希腊酸奶碗

-- 周三 2026-04-30
INSERT INTO `meal_plan` (`id`, `user_id`, `date`, `meal_type`) VALUES
(9, 1, '2026-04-30', '早餐'),
(10, 1, '2026-04-30', '午餐'),
(11, 1, '2026-04-30', '晚餐'),
(12, 1, '2026-04-30', '加餐');
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`) VALUES
(9, 1), (9, 2), (9, 24),   -- 牛奶燕麦粥、水煮蛋、香蕉
(10, 8), (10, 10), (10, 17), -- 照烧鸡胸肉、清炒西兰花、烤红薯
(11, 12), (11, 18), (11, 9), -- 柠香烤三文鱼、蒜蓉菠菜、糙米饭
(12, 23);                    -- 坚果小食

-- 周四 2026-05-01
INSERT INTO `meal_plan` (`id`, `user_id`, `date`, `meal_type`) VALUES
(13, 1, '2026-05-01', '早餐'),
(14, 1, '2026-05-01', '午餐'),
(15, 1, '2026-05-01', '晚餐'),
(16, 1, '2026-05-01', '加餐');
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`) VALUES
(13, 3), (13, 2),          -- 牛油果吐司、水煮蛋
(14, 7), (14, 9),          -- 西兰花炒牛肉、糙米饭
(15, 16), (15, 20),        -- 白灼鸡胸肉、凉拌生菜
(16, 28);                   -- 酸奶香蕉碗

-- 周五 2026-05-02
INSERT INTO `meal_plan` (`id`, `user_id`, `date`, `meal_type`) VALUES
(17, 1, '2026-05-02', '早餐'),
(18, 1, '2026-05-02', '午餐'),
(19, 1, '2026-05-02', '晚餐'),
(20, 1, '2026-05-02', '加餐');
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`) VALUES
(17, 1), (17, 2), (17, 24),  -- 牛奶燕麦粥、水煮蛋、香蕉
(18, 13), (18, 21), (18, 9), -- 清炒虾仁、清炒胡萝卜丝、糙米饭
(19, 11), (19, 17), (19, 18), -- 香煎三文鱼、烤红薯、蒜蓉菠菜
(20, 23);                    -- 坚果小食

-- 周六 2026-05-03
INSERT INTO `meal_plan` (`id`, `user_id`, `date`, `meal_type`) VALUES
(21, 1, '2026-05-03', '早餐'),
(22, 1, '2026-05-03', '午餐'),
(23, 1, '2026-05-03', '晚餐'),
(24, 1, '2026-05-03', '加餐');
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`) VALUES
(21, 3), (21, 2),           -- 牛油果吐司、水煮蛋
(22, 5), (22, 10), (22, 9), -- 香煎鸡胸肉、清炒西兰花、糙米饭
(23, 15), (23, 20),         -- 土豆炖牛肉、凉拌生菜
(24, 4);                    -- 希腊酸奶碗

-- 周日 2026-05-04
INSERT INTO `meal_plan` (`id`, `user_id`, `date`, `meal_type`) VALUES
(25, 1, '2026-05-04', '早餐'),
(26, 1, '2026-05-04', '午餐'),
(27, 1, '2026-05-04', '晚餐'),
(28, 1, '2026-05-04', '加餐');
INSERT INTO `meal_plan_dish` (`meal_plan_id`, `dish_id`) VALUES
(25, 1), (25, 2), (25, 24),  -- 牛奶燕麦粥、水煮蛋、香蕉
(26, 12), (26, 18), (26, 9), -- 柠香烤三文鱼、蒜蓉菠菜、糙米饭
(27, 13), (27, 14), (27, 10), -- 清炒虾仁、家常豆腐、清炒西兰花
(28, 23);                    -- 坚果小食

-- -----------------------------------------------------------
-- 体重记录初始数据 (用户1)
-- -----------------------------------------------------------
INSERT INTO `weight_log` (`user_id`, `date`, `weight`, `body_fat`) VALUES
(1, '2026-04-01', 78.5, 22.0),
(1, '2026-04-05', 78.0, 21.8),
(1, '2026-04-10', 77.3, 21.5),
(1, '2026-04-15', 76.8, 21.2),
(1, '2026-04-20', 76.2, 20.8),
(1, '2026-04-25', 75.5, 20.5),
(1, '2026-04-30', 75.0, 20.1),
(1, '2026-05-01', 74.8, 19.8),
(1, '2026-05-02', 74.5, 19.5),
(1, '2026-05-03', 74.3, 19.3),
(1, '2026-05-04', 72.0, 18.5);

-- -----------------------------------------------------------
-- 身体围度记录初始数据 (用户1)
-- -----------------------------------------------------------
INSERT INTO `body_measurement` (`user_id`, `date`, `chest`, `waist`, `hip`, `arm`, `thigh`) VALUES
(1, '2026-04-01', 98.0, 86.0, 100.0, 32.0, 56.0),
(1, '2026-04-08', 98.0, 85.0, 99.0, 32.0, 55.5),
(1, '2026-04-15', 97.5, 84.0, 99.0, 31.5, 55.0),
(1, '2026-04-22', 97.0, 83.0, 98.0, 31.5, 54.5),
(1, '2026-04-29', 97.0, 82.0, 98.0, 31.0, 54.0),
(1, '2026-05-04', 96.5, 81.0, 97.0, 31.0, 53.5);

-- -----------------------------------------------------------
-- AI 分析报告初始数据 (用户1)
-- -----------------------------------------------------------
INSERT INTO `ai_report` (`id`, `user_id`, `title`, `type`, `period_start`, `period_end`, `summary`, `content`) VALUES
(1, 1, '第1周健康分析报告', 'weekly', '2026-04-01', '2026-04-07',
 '本周体重下降1.2kg，饮食热量控制良好，训练完成率85%。',
 '## 饮食分析\n本周平均每日摄入热量约1400kcal，低于目标值2100kcal。蛋白质摄入充足，但碳水化合物略低。建议适当增加复合碳水摄入。\n\n## 训练分析\n本周完成5次训练，训练量适中。胸肌和背肌训练表现良好，腿部训练需要加强。\n\n## 体重趋势\n体重从78.5kg降至77.3kg，下降速度合理。体脂率从22.0%降至21.5%，脂肪减少明显。\n\n## 建议\n1. 每日增加100-200kcal碳水摄入\n2. 腿部训练增加深蹲组数\n3. 保持当前有氧频率'),
(2, 1, '第2周健康分析报告', 'weekly', '2026-04-08', '2026-04-14',
 '体重继续下降0.8kg，围度变化明显，训练强度有所提升。',
 '## 饮食分析\n本周饮食结构改善，热量摄入稳定在1500kcal左右。蛋白质占比提高至30%，营养均衡性提升。\n\n## 训练分析\n训练完成率提升至100%，卧推重量提升5kg，深蹲提升10kg。力量进步明显。\n\n## 体重趋势\n体重从77.3kg降至76.5kg，腰围减少2cm，减脂效果显著。\n\n## 建议\n1. 可适当提高训练重量\n2. 增加训练前的热身时间\n3. 保持饮食计划的执行力'),
(3, 1, '4月月度分析报告', 'monthly', '2026-04-01', '2026-04-30',
 '本月减重3.5kg，体脂下降1.9%，各项围度均有改善。',
 '## 月度总览\n本月体重从78.5kg降至75.0kg，共减重3.5kg。体脂率从22.0%降至20.1%，下降1.9个百分点。\n\n## 饮食回顾\n月均每日摄入热量约1450kcal，蛋白质日均摄入120g，表现优秀。周末饮食控制需要加强。\n\n## 训练回顾\n本月共完成22次训练，完成率78.6%。力量指标全面提升：\n- 卧推：40kg → 50kg\n- 深蹲：50kg → 65kg\n- 硬拉：60kg → 80kg\n\n## 身体围度变化\n- 腰围：86cm → 82cm（-4cm）\n- 胸围：98cm → 97cm（-1cm）\n- 大腿：56cm → 54cm（-2cm）\n\n## 下月建议\n1. 目标减重2-3kg\n2. 增加HIIT训练频率\n3. 注意周末饮食控制\n4. 适当增加蛋白质摄入至130g/日');

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 初始化完成
-- ============================================================
