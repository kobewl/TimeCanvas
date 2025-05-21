# TimeCanvas (时间画布)

## 项目介绍

TimeCanvas是一个基于AI技术的智能日记生成与管理系统，旨在帮助用户通过AI技术轻松创建、管理和回顾日记内容。
项目采用多模块设计，实现了功能模块的高内聚低耦合，便于维护和扩展。

## 项目结构

```
TimeCanvas
├── ApiModule               # API接口模块
├── ServiceModule           # 服务层模块
├── UtilModule              # 工具类模块
├── ImtrustAgentModule      # Imtrust AI 代理模块
├── JobModule               # 定时任务模块
└── JacocoModule            # 代码覆盖率测试模块
```

## 模块说明

### ApiModule

负责提供RESTful API接口，处理HTTP请求和响应。包含控制器、配置类以及应用程序入口。

### ServiceModule

包含业务逻辑实现、数据库访问对象(DAO)、领域模型等。

### UtilModule

提供各种工具类和公共组件，供其他模块调用。

### JobModule

处理定时任务和批量处理逻辑。

### JacocoModule

用于测试代码覆盖率的模块。

## 技术栈

- **框架**: Spring Boot 3.x
- **数据库**: MySQL
- **ORM**: MyBatis-Plus
- **AI技术**: LangChain4j
- **API文档**: Knife4j (基于Swagger)
- **构建工具**: Maven

## 环境要求

- JDK 19+
- Maven 3.6+
- MySQL 8.0+

## 运行说明

### 1. 克隆代码

```bash
git clone https://github.com/yourusername/TimeCanvas.git
cd TimeCanvas
```

### 2. 编译项目

```bash
mvn clean install
```

### 3. 运行项目

```bash
cd ApiModule
mvn spring-boot:run
```

### 4. 访问API文档

```
http://localhost:8080/doc.html
```

## 开发指南

### 添加新功能

1. 确定功能应该属于哪个模块
2. 在对应模块中实现功能
3. 如需跨模块调用，遵循依赖关系约束

### 模块依赖关系

- ApiModule → ServiceModule → UtilModule
- ImtrustAgentModule → UtilModule
- YundiAgentModule → UtilModule
- JobModule → ServiceModule

### 开发规范

- 控制器层: 仅处理请求和响应，不包含业务逻辑
- 服务层: 实现业务逻辑，不直接访问Controller
- 数据访问层: 仅关注数据访问，不包含业务逻辑
- 工具类: 无状态的公共方法

## 部署

### 打包部署

```bash
mvn clean package -DskipTests
```

打包后的JAR文件位于ApiModule/target/目录下。

### 使用Docker部署

```bash
# TODO: 添加Docker部署说明
```

## 配置说明

应用配置文件位于ApiModule/src/main/resources/目录下：

- application.properties/yml: 主配置文件
- application-dev.properties/yml: 开发环境配置
- application-prod.properties/yml: 生产环境配置

## 贡献指南

1. Fork项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建Pull Request

## 许可证

[根据需要添加许可证信息] 