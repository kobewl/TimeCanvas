# TimeCanvas

TimeCanvas is an intelligent diary management system powered by AI. This project deeply integrates with mainstream large language models to provide features like one-sentence diary generation, automatic analysis reports, and smart to-do generation, helping users easily record and manage their daily lives.

## Project Introduction

**TimeCanvas** solves the following problems through AI technology:
- Difficulty in journaling: Generate complete, detailed diary content with just one sentence using AI
- Tedious work summaries: Automatically analyze diaries and generate daily, weekly, and monthly reports with one click
- Complex task management: AI analyzes diary content to automatically extract and generate to-do items
- Hassle-free financial tracking: Automatically identify income and expenses from diary entries and generate financial reports

## Tech Stack

### Backend
- **Core Framework**: Spring Boot 3.1.5
- **Security**: Spring Security + JWT
- **Data Access**: Spring Data JPA + MyBatis-Plus
- **Database**: MySQL
- **Caching**: Redis
- **API Documentation**: Knife4j (Based on OpenAPI 3)
- **AI Integration**: LangChain4j, DeepSeek, Volcano Engine
- **Utility Libraries**: Lombok, Hutool

### Frontend
- **Core Framework**: Vue 3
- **UI Component Library**: Element Plus
- **HTTP Client**: Axios
- **Build Tool**: Vite

## Project Structure

```
TimeCanvas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── timecanvas/
│   │   │           └── backend/
│   │   │               ├── ai/          # AI相关处理
│   │   │               ├── common/      # 通用组件
│   │   │               ├── config/      # 配置类
│   │   │               ├── controller/  # 控制器
│   │   │               ├── dao/         # 数据访问层
│   │   │               ├── model/       # 数据模型
│   │   │               ├── service/     # 服务层
│   │   │               └── utils/       # 工具类
│   │   └── resources/                   # 资源文件
│   └── test/                            # 测试代码
└── pom.xml                              # Maven配置
```

## 开发环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- IDE推荐：IntelliJ IDEA

## 开发进度

### 已完成
- [x] 项目基础架构搭建
- [x] 通用响应对象和错误码定义
- [x] Knife4j集成配置
- [x] 用户相关API接口设计
- [x] 日记生成API接口设计

### 进行中
- [ ] 用户注册与登录功能实现 (P1)
- [ ] DeepSeek大模型集成 (P0)
- [ ] 日记生成功能实现 (P0)

### 待开发
- [ ] 日记模版管理
- [ ] 报告生成功能
- [ ] 待办事项生成
- [ ] 财务信息分析

## Knife4j API文档使用

本项目集成了Knife4j作为API文档和测试工具，提供了美观易用的接口文档，便于开发和测试。

### 访问方式

1. 启动项目后，访问：http://localhost:8080/api/doc.html
2. 页面展示所有已定义的API接口，包括：
   - 接口说明
   - 请求参数
   - 响应结果
   - 在线调试功能

### 接口测试步骤

1. 在Knife4j页面找到需要测试的接口
2. 填写请求参数（需要认证的接口先调用登录接口获取token）
3. 点击"执行"按钮发送请求
4. 查看响应结果，包括状态码、响应头和响应体

### 认证接口测试

1. 调用登录接口获取token
2. 点击右上角"Authorize"按钮
3. 在弹出框中输入`Bearer your_token_here`格式的认证信息
4. 确认后，后续请求会自动携带认证信息

## Quick Start

### Prerequisites

1. Install JDK 17+
2. Install MySQL 8.0+
3. Install Redis
4. Clone the repository

### Database Configuration

1. Create a new database:
```sql
CREATE DATABASE timecanvas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Update the database configuration in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/timecanvas_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### Running the Project

```bash
cd TimeCanvas_backend
mvn spring-boot:run
```

After successful startup, access the API documentation at: http://localhost:8080/api/doc.html

## AI配置说明

本项目支持多种AI模型，默认配置为：

```yaml
ai:
  deepseek:
    enabled: true
    api-key: your-api-key-here
    model: deepseek-chat
  huoshan:
    enabled: true
    api-key: your-api-key-here
```

使用前请替换为实际的API密钥。

## 开发日志

### 2023-05-08 项目初始化
- 初始化项目结构
- 创建基础框架
- 集成Knife4j文档

### 2023-05-09 基础功能开发
- 创建通用响应对象和错误码
- 设计数据库结构
- 创建用户相关API接口
- 创建日记生成API接口

## Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Create a Pull Request

## License

This project is licensed under the Apache 2.0 License

## 后端接口对接指南

### 概述
本项目的前端已集成后端API调用，使用Axios库连接Spring Boot后端。以下是快速指南：

- **后端启动**：在TimeCanvas_backend目录下运行`mvn spring-boot:run` (假设已安装Maven)。
- **前端配置**：确保后端运行在http://localhost:8080/api，确保前端能访问。
- **测试步骤**：在前端运行`npm run dev`，然后访问http://localhost:5173/查看数据。

### 常见问题
- 如果出现连接错误，请检查后端端口和CORS设置。
- 确保Node.js版本>=18。 