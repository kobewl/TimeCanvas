# 智绘时光（Intelligent Painting of Time）

智绘时光是一个以日记为核心，以AI大模型为驱动的智能日记管理系统。本项目深度集成主流大模型，提供一句话生成日记、自动分析生成报告、智能待办生成等功能，帮助用户轻松记录和管理生活点滴。

## 项目简介

**智绘时光** 通过AI技术解决以下问题：
- 写日记难：只需一句话，AI即可生成完整、详细的日记内容
- 工作总结繁琐：自动分析日记，一键生成日报、周报、月报
- 待办管理复杂：AI分析日记内容，自动提取和生成待办事项
- 财务记录麻烦：自动识别日记中的收支信息，生成财务报告

## 技术栈

### 后端
- **核心框架**：Spring Boot 3.1.5
- **安全框架**：Spring Security + JWT
- **数据访问**：Spring Data JPA + MyBatis-Plus
- **数据库**：MySQL
- **缓存**：Redis
- **API文档**：Knife4j (基于OpenAPI 3)
- **AI集成**：LangChain4j、DeepSeek、火山引擎
- **工具库**：Lombok、Hutool

### 前端（计划中）
- **核心框架**：Vue 3
- **UI组件库**：Element Plus
- **HTTP客户端**：Axios
- **构建工具**：Vite

## 项目结构

```
IPT_backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ipt/
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

## 快速开始

### 环境准备

1. 安装JDK 17+
2. 安装MySQL 8.0+
3. 安装Redis
4. 克隆代码库

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE ipt_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改`application.yml`中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ipt_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 启动项目

```bash
cd IPT_backend
mvn spring-boot:run
```

启动成功后访问: http://localhost:8080/api/doc.html 查看API文档

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

## 贡献指南

欢迎贡献代码，请遵循以下步骤：

1. Fork 仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交变更 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建Pull Request

## 许可证

本项目采用 Apache 2.0 许可证

## 后端接口对接指南

### 概述
本项目的前端已集成后端API调用，使用Axios库连接Spring Boot后端。以下是快速指南：

- **后端启动**：在TimeCanvas_backend目录下运行`mvn spring-boot:run` (假设已安装Maven)。
- **前端配置**：确保后端运行在http://localhost:8080/api，确保前端能访问。
- **测试步骤**：在前端运行`npm run dev`，然后访问http://localhost:5173/查看数据。

### 常见问题
- 如果出现连接错误，请检查后端端口和CORS设置。
- 确保Node.js版本>=18。 