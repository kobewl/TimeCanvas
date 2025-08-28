# 🎨 智能时光绘（TimeCanvas）

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5.17-4FC08D.svg)](https://vuejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.8.0-blue.svg)](https://www.typescriptlang.org/)

**智能时光绘** 是一个基于AI的智能日记与生活管理系统，集成现代化技术栈和先进AI能力，帮助用户轻松记录生活、智能生成内容、管理个人事务，提升生活品质和效率。

---

## ✨ 项目亮点

🤖 **AI驱动**: 集成LangChain4j，支持多种主流大模型（DeepSeek、豆包等）  
📝 **智能日记**: 一句话生成完整日记，AI自动润色和优化  
🔍 **语义搜索**: 基于向量嵌入的智能知识库，支持语义搜索和智能问答  
📊 **数据洞察**: 自动分析生成日报/周报/月报，智能提取待办和财务信息  
🎯 **个性化**: 丰富的模板系统，支持自定义模板和个性化设置  
🔐 **安全可靠**: 完整的用户认证体系，数据安全保障  

![日记生成页面演示](./assets/aiDiary.png)

---

## 🏗️ 技术架构

### 后端技术栈
- **框架**: Spring Boot 3.2.5 + Spring Security
- **数据层**: MyBatis-Plus 3.5.11 + MySQL 8.0 + Redis
- **AI集成**: LangChain4j 1.0.0-beta3
- **API文档**: Knife4j 4.5.0 (Swagger)
- **认证**: JWT + Spring Security
- **构建工具**: Maven 3.6+

### 前端技术栈
- **框架**: Vue 3.5.17 + TypeScript 5.8.0
- **UI组件**: Element Plus 2.10.4
- **状态管理**: Pinia 3.0.3
- **路由**: Vue Router 4.5.1
- **构建工具**: Vite 7.0.0
- **HTTP客户端**: Axios 1.10.0

### AI模型支持
- **DeepSeek-V3.1**: 支持深度推理和复杂任务
- **火山引擎豆包**: 中文优化，响应速度快
- **可扩展**: 支持接入其他兼容OpenAI API的模型

---

## 📁 项目结构

```
TimeCanvas/
├── 📁 TimeCanvas_backend/          # 后端项目
│   ├── 📁 ApiModule/              # API接口层
│   │   ├── 📁 controller/         # 控制器
│   │   ├── 📁 config/            # 配置类
│   │   └── 📁 exception/         # 异常处理
│   ├── 📁 ServiceModule/          # 业务服务层
│   │   ├── 📁 service/           # 业务接口
│   │   ├── 📁 service/impl/      # 业务实现
│   │   ├── 📁 domain/            # 实体类
│   │   ├── 📁 dto/               # 数据传输对象
│   │   ├── 📁 vo/                # 视图对象
│   │   └── 📁 mapper/            # 数据访问层
│   ├── 📁 UtilModule/             # 工具模块
│   │   ├── 📁 util/              # 工具类
│   │   ├── 📁 exception/         # 异常定义
│   │   └── 📁 assistant/         # AI助手
│   └── 📁 JobModule/              # 定时任务模块
├── 📁 TimeCanvas_frontend/         # 前端项目
│   ├── 📁 src/
│   │   ├── 📁 views/             # 页面组件
│   │   ├── 📁 components/        # 通用组件
│   │   ├── 📁 api/               # API接口
│   │   ├── 📁 router/            # 路由配置
│   │   └── 📁 assets/            # 静态资源
├── 📄 create_timecanvas_tables.sql # 数据库脚本
└── 📄 README.md                   # 项目文档
```

---

## 🚀 核心功能

### 📝 AI日记系统
- **智能生成**: 输入简单描述，AI生成完整日记
- **模板系统**: 内置多种日记模板，支持自定义
- **情感分析**: 自动识别心情和情感倾向
- **关键词提取**: 智能提取日记关键词和标签

### 🧠 智能知识库
- **分类管理**: 树形分类结构，支持多级分类
- **语义搜索**: 基于向量嵌入的智能搜索
- **智能问答**: 基于知识库内容的AI问答
- **关联推荐**: 智能推荐相关知识条目

### 📊 数据分析
- **自动报告**: 智能生成日报、周报、月报
- **待办提取**: 从日记内容自动提取待办事项
- **财务分析**: 识别收支信息，生成财务报表
- **趋势分析**: 展示个人生活和情感趋势

### 👤 用户系统
- **安全认证**: JWT token认证，密码加密存储
- **权限管理**: 基于角色的访问控制
- **个人中心**: 用户信息管理，偏好设置

---

## 💻 环境要求

### 开发环境
| 技术栈 | 版本要求 | 说明 |
|-------|---------|------|
| **JDK** | 17+ | 推荐使用JDK 19 |
| **Maven** | 3.6+ | 项目构建工具 |
| **MySQL** | 8.0+ | 主数据库 |
| **Redis** | 6.0+ | 缓存和会话存储 |
| **Node.js** | 18+ | 前端运行环境 |
| **IDE** | IntelliJ IDEA / VSCode | 推荐开发工具 |

### AI服务配置
- **DeepSeek API Key**: 注册 [DeepSeek](https://platform.deepseek.com/) 获取
- **火山引擎API Key**: 注册 [火山引擎](https://console.volcengine.com/) 获取

---

## 🚀 快速开始

### 1️⃣ 克隆项目
```bash
git clone https://github.com/your-username/TimeCanvas.git
cd TimeCanvas
```

### 2️⃣ 数据库初始化
```bash
# 1. 创建数据库
mysql -u root -p
CREATE DATABASE timecanvas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. 导入数据表
mysql -u root -p timecanvas_db < create_timecanvas_tables.sql

# 3. 配置数据库连接
# 编辑 TimeCanvas_backend/ApiModule/src/main/resources/application.yml
```

### 3️⃣ 启动后端服务
```bash
cd TimeCanvas_backend

# 安装依赖并启动
mvn clean install
mvn spring-boot:run

# 或者使用IDE直接运行 TimeCanvasApplication.main()
```

**✅ 后端启动成功后访问:**
- 🌐 API接口文档: http://localhost:8080/api/doc.html
- 🔍 健康检查: http://localhost:8080/actuator/health

### 4️⃣ 启动前端应用
```bash
cd TimeCanvas_frontend

# 安装依赖
npm install
# 或使用 pnpm install / yarn install

# 启动开发服务器
npm run dev
```

**✅ 前端启动成功后访问:**
- 🌐 应用首页: http://localhost:5173
- 📝 AI日记: http://localhost:5173/ai-diary
- 🧠 知识库: http://localhost:5173/knowledge

---

## 🔧 配置说明

### 后端配置 (application.yml)
```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/timecanvas_db
    username: your_username
    password: your_password

# AI模型配置
langchain4j:
  open-ai:
    chat-model:
      api-key: ${DEEPSEEK_API_KEY}
      base-url: https://api.deepseek.com/v1
      model-name: deepseek-chat
```

### 前端配置 (vite.config.ts)
```typescript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

---

## 📋 API接口文档

### 用户认证
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/register` | POST | 用户注册 |
| `/api/user/login` | POST | 用户登录 |
| `/api/user/info` | GET | 获取用户信息 |

### AI日记
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/diary/generate` | POST | AI生成日记 |
| `/api/diary/list` | GET | 获取日记列表 |
| `/api/diary/save` | POST | 保存日记 |

### 知识库
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/knowledge/search` | POST | 语义搜索 |
| `/api/knowledge/qa` | POST | 智能问答 |
| `/api/knowledge/save` | POST | 保存知识 |

**📖 完整API文档**: 启动后端后访问 [http://localhost:8080/api/doc.html](http://localhost:8080/api/doc.html)

---

## 🐛 常见问题

<details>
<summary><strong>🔴 后端启动失败</strong></summary>

**问题**: 后端启动时报错数据库连接失败
```
Could not create connection to database server
```

**解决方案**:
1. 检查MySQL服务是否启动
2. 确认数据库连接配置正确
3. 验证数据库用户权限
4. 检查防火墙设置
</details>

<details>
<summary><strong>🟡 前端接口404</strong></summary>

**问题**: 前端调用后端接口返回404
```
GET http://localhost:5173/api/user/info 404 (Not Found)
```

**解决方案**:
1. 确认后端服务已启动（8080端口）
2. 检查Vite代理配置
3. 验证API路径是否正确
</details>

<details>
<summary><strong>🟠 AI功能不可用</strong></summary>

**问题**: AI日记生成失败
```
AI service unavailable
```

**解决方案**:
1. 检查AI API Key配置
2. 确认网络连接正常
3. 验证API配额是否足够
4. 查看后端日志详细错误信息
</details>

---

## 🤝 贡献指南

### 开发流程
1. **Fork** 本仓库到你的GitHub账号
2. **Clone** 你Fork的仓库到本地
3. **创建** 新的功能分支: `git checkout -b feature/amazing-feature`
4. **开发** 新功能并编写测试
5. **提交** 变更: `git commit -m 'Add amazing feature'`
6. **推送** 到分支: `git push origin feature/amazing-feature`
7. **创建** Pull Request

### 代码规范
- **Java**: 遵循Google Java Style Guide
- **Vue/TypeScript**: 遵循Vue官方风格指南
- **提交信息**: 使用[约定式提交](https://www.conventionalcommits.org/)格式

### 问题反馈
- 🐛 **Bug报告**: 使用[Bug模板](.github/ISSUE_TEMPLATE/bug_report.md)
- 💡 **功能建议**: 使用[功能请求模板](.github/ISSUE_TEMPLATE/feature_request.md)
- 📚 **文档改进**: 直接提交PR或创建Issue

---

## 📊 项目统计

![GitHub stars](https://img.shields.io/github/stars/your-username/TimeCanvas?style=social)
![GitHub forks](https://img.shields.io/github/forks/your-username/TimeCanvas?style=social)
![GitHub issues](https://img.shields.io/github/issues/your-username/TimeCanvas)
![GitHub license](https://img.shields.io/github/license/your-username/TimeCanvas)

### 技术债务
- [ ] 添加单元测试覆盖率至80%以上
- [ ] 完善API文档和开发者文档  
- [ ] 添加性能监控和日志聚合
- [ ] 实现容器化部署支持
- [ ] 添加CI/CD流水线

---

## 📄 License

本项目采用 [Apache License 2.0](LICENSE) 开源协议。

---

## 👨‍💻 作者

**王亮** - *项目创建者和主要维护者*

- 📧 Email: wangliang@example.com
- 🐱 GitHub: [@your-username](https://github.com/your-username)
- 💼 LinkedIn: [王亮](https://linkedin.com/in/your-profile)

---

## 🙏 致谢

感谢以下开源项目为TimeCanvas提供支持：

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的Java后端框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架  
- [LangChain4j](https://github.com/langchain4j/langchain4j) - Java版LangChain
- [Element Plus](https://element-plus.org/) - 优秀的Vue 3 UI库
- [MyBatis-Plus](https://baomidou.com/) - 强大的持久层框架

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给个Star支持一下！**

*用AI记录生活，让每一天都闪闪发光* ✨

</div> 