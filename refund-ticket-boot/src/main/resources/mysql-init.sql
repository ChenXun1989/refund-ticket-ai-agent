-- MySQL 数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS refund_ticket DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE refund_ticket;

-- 购票记录表
CREATE TABLE IF NOT EXISTS buy_ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    code VARCHAR(64) NOT NULL COMMENT '购票编号',
    user_name VARCHAR(100) NOT NULL COMMENT '用户名',
    ticket_price DECIMAL(10, 2) NOT NULL COMMENT '票价',
    cinema_name VARCHAR(200) NOT NULL COMMENT '影院名称',
    movie_name VARCHAR(200) NOT NULL COMMENT '电影名称',
    ticket_time DATETIME NOT NULL COMMENT '购票时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_code (code),
    INDEX idx_user_name (user_name),
    INDEX idx_ticket_time (ticket_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购票记录表';

-- 退票记录表
CREATE TABLE IF NOT EXISTS refund_ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    buy_ticket_code VARCHAR(64) NOT NULL COMMENT '购票编号',
    code VARCHAR(64) NOT NULL COMMENT '退票编号',
    user_name VARCHAR(100) NOT NULL COMMENT '用户名',
    status VARCHAR(20) NOT NULL COMMENT '退票状态：PENDING-待处理，PROCESSING-处理中，APPROVED-已通过，REJECTED-已拒绝',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_code (code),
    INDEX idx_buy_ticket_code (buy_ticket_code),
    INDEX idx_user_name (user_name),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退票记录表';

-- 插入测试数据
-- 购票记录测试数据
INSERT INTO buy_ticket (code, user_name, ticket_price, cinema_name, movie_name, ticket_time) VALUES
('BT20231201001', '张三', 58.00, '万达影城（王府井店）', '长津湖之水门桥', '2025-12-01 14:30:00'),
('BT20231201002', '李四', 68.00, '大地影院（朝阳门店）', '流浪地球2', '2025-12-01 19:00:00'),
('BT20231202003', '王五', 48.00, '博纳国际影城（CBD店）', '满江红', '2025-12-02 10:00:00');

-- 退票记录测试数据

