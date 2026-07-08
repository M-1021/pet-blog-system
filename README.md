# 🐾 宠物博客系统（Pet Blog System）

> 赣南科技学院《Web项目案例综合实训》课程设计项目

一个基于 **Spring Boot + Vue2 + MyBatis-Plus + MySQL** 的宠物知识分享与在线交流平台。

---

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [系统功能](#系统功能)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速启动](#快速启动)
- [数据库设计](#数据库设计)
- [API 接口文档](#api-接口文档)
- [系统架构图](#系统架构图)
- [测试账号](#测试账号)
- [常见问题](#常见问题)

---

## 项目简介

宠物博客系统是一个面向宠物爱好者的在线平台，用户可以：
- 📖 浏览和发布宠物知识文章
- 💬 在社区中在线交流养宠心得
- 👍 对喜欢的文章点赞和评论
- 🚩 举报违规内容，维护社区环境
- 👤 管理个人信息和发布的文章

管理员可以：
- 管理用户账号
- 管理文章分类
- 审核举报内容，处理违规文章

---

## 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 1.8+ | 编程语言 |
| Spring Boot | 2.7.18 | Web 应用框架 |
| MyBatis-Plus | 3.5.3.1 | ORM 框架（简化数据库操作） |
| MySQL | 5.0+ | 关系型数据库 |
| JWT (jjwt) | 0.9.1 | 用户认证 Token |
| Lombok | - | 简化实体类代码 |
| Maven | - | 项目构建工具 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 2.7 | 前端框架 |
| Vue Router | 3.6 | 前端路由管理 |
| Vuex | 3.6 | 全局状态管理 |
| Element UI | 2.15 | UI 组件库 |
| Axios | 0.27 | HTTP 请求库 |

---

## 系统功能

### 用户管理模块
- ✅ 用户注册（用户名 + 密码）
- ✅ 用户登录（JWT Token 认证）
- ✅ 个人信息查看与修改
- ✅ 管理员查看/删除用户

### 宠物知识管理模块
- ✅ 文章列表浏览（支持分页、分类筛选、关键词搜索）
- ✅ 文章详情查看
- ✅ 发布/编辑/删除文章
- ✅ 文章点赞（防重复点赞）
- ✅ 文章评论

### 宠物知识分类管理模块
- ✅ 分类列表查看
- ✅ 管理员添加/编辑/删除分类

### 在线交流模块
- ✅ 发布交流帖子
- ✅ 查看帖子列表
- ✅ 对帖子进行回复

### 举报违规功能模块
- ✅ 用户举报违规文章
- ✅ 管理员查看举报列表
- ✅ 管理员驳举报或删除违规文章

---

## 项目结构

```
pet-blog-system/
├── backend/                          # 后端 Spring Boot 项目
│   ├── pom.xml                       # Maven 依赖配置
│   └── src/main/
│       ├── java/com/petblog/
│       │   ├── PetBlogApplication.java    # 主启动类
│       │   ├── config/                    # 配置类
│       │   │   ├── WebConfig.java         # 跨域 + 拦截器配置
│       │   │   ├── LoginInterceptor.java  # 登录验证拦截器
│       │   │   ├── MyMetaObjectHandler.java # 自动填充处理器
│       │   │   └── GlobalExceptionHandler.java # 全局异常处理
│       │   ├── entity/                    # 实体类（对应数据库表）
│       │   │   ├── User.java              # 用户
│       │   │   ├── Article.java           # 文章
│       │   │   ├── Category.java          # 分类
│       │   │   ├── Comment.java           # 评论
│       │   │   ├── ArticleLike.java       # 点赞
│       │   │   ├── Post.java              # 帖子
│       │   │   ├── Reply.java             # 回复
│       │   │   └── Report.java            # 举报
│       │   ├── mapper/                    # Mapper 接口（数据库操作）
│       │   ├── service/                   # Service 接口
│       │   │   └── impl/                  # Service 实现类
│       │   ├── controller/                # Controller（REST API）
│       │   │   ├── UserController.java    # 用户接口
│       │   │   ├── ArticleController.java # 文章接口
│       │   │   ├── CategoryController.java # 分类接口
│       │   │   ├── CommentController.java # 评论接口
│       │   │   ├── PostController.java    # 帖子接口
│       │   │   └── ReportController.java  # 举报接口
│       │   ├── dto/                       # 数据传输对象
│       │   │   ├── Result.java            # 统一响应封装
│       │   │   ├── LoginDTO.java          # 登录请求
│       │   │   └── RegisterDTO.java       # 注册请求
│       │   └── util/                      # 工具类
│       │       ├── JwtUtil.java           # JWT Token 工具
│       │       └── PasswordUtil.java      # 密码加密工具
│       └── resources/
│           └── application.yml            # 应用配置文件
│
├── frontend/                         # 前端 Vue 项目
│   ├── package.json                  # npm 依赖配置
│   ├── vue.config.js                 # Vue CLI 配置（代理）
│   ├── public/index.html             # HTML 入口
│   └── src/
│       ├── main.js                   # Vue 入口文件
│       ├── App.vue                   # 根组件（导航栏）
│       ├── router/index.js           # 路由配置
│       ├── store/index.js            # Vuex 状态管理
│       ├── api/index.js              # API 接口封装
│       └── views/                    # 页面组件
│           ├── Home.vue              # 首页
│           ├── Login.vue             # 登录页
│           ├── Register.vue          # 注册页
│           ├── Knowledge.vue         # 宠物知识列表
│           ├── ArticleDetail.vue     # 文章详情
│           ├── Community.vue         # 在线交流
│           ├── PostDetail.vue        # 帖子详情
│           ├── Profile.vue           # 个人中心
│           ├── MyArticles.vue        # 我的文章
│           ├── Publish.vue           # 发布文章
│           └── Admin.vue             # 后台管理
│
├── sql/
│   └── init.sql                      # 数据库初始化脚本
│
└── README.md                         # 项目说明文档
```

---

## 环境要求

在运行项目之前，请确保已安装以下环境：

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 1.8 或以上 | Java 开发环境 |
| Maven | 3.6 或以上 | 后端构建工具 |
| Node.js | 14 或以上 | 前端运行环境 |
| npm | 6 或以上 | 前端包管理工具 |
| MySQL | 5.0 或以上 | 数据库 |

---

## 快速启动

### 第一步：初始化数据库

1. 确保 MySQL 服务已启动
2. 执行初始化脚本创建数据库和表：

```bash
# 方式一：命令行执行
mysql -u root -p < sql/init.sql

# 方式二：在 MySQL 客户端中执行
source /path/to/sql/init.sql
```

3. 如果数据库密码不是 `root`，请修改 `backend/src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pet_blog?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password  # 修改为你的数据库密码
```

### 第二步：启动后端

```bash
# 进入后端目录
cd backend

# 使用 Maven 启动（方式一）
mvn spring-boot:run

# 或者先打包再运行（方式二）
mvn clean package -DskipTests
java -jar target/pet-blog-backend-1.0.0.jar
```

✅ 后端启动成功后，控制台会显示：`Started PetBlogApplication in x.xx seconds`
✅ 后端服务地址：`http://localhost:8080`

### 第三步：启动前端

```bash
# 进入前端目录（新开一个终端）
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

✅ 前端启动成功后，控制台会显示：`Local: http://localhost:8081`
✅ 在浏览器中打开 `http://localhost:8081` 即可访问系统

---

## 数据库设计

### ER 图（实体关系图）

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│   user   │     │ article  │     │ category │
│──────────│     │──────────│     │──────────│
│ id (PK)  │◄──┐ │ id (PK)  │     │ id (PK)  │
│ username │   │ │ title    │     │ name     │
│ password │   │ │ content  │     │ desc     │
│ nickname │   │ │ user_id  │──┐  │ sort     │
│ avatar   │   │ │ cat_id   │──┼──│          │
│ email    │   │ │ likes    │  │  └──────────┘
│ phone    │   │ │ comments │  │
│ bio      │   │ │ status   │  │
│ role     │   │ └──────────┘  │
└──────────┘   │               │
     │         │ ┌──────────┐  │
     │         │ │ comment  │  │
     │         ├─│──────────│  │
     │         │ │ id (PK)  │  │
     │         │ │ content  │  │
     │         │ │ art_id   │──┤
     │         │ │ user_id  │◄─┤
     │         │ │ parent_id│  │
     │         │ └──────────┘  │
     │         │               │
     │         │ ┌──────────┐  │
     ├────────►│ │post      │  │
     │         │ │──────────│  │
     │         │ │ id (PK)  │  │
     │         │ │ title    │  │
     │         │ │ content  │  │
     │         │ │ user_id  │◄─┘
     │         │ └──────────┘
     │         │
     │         │ ┌──────────┐
     ├────────►│ │ reply    │
     │         │ │──────────│
     │         │ │ id (PK)  │
     │         │ │ content  │
     │         │ │ post_id  │
     │         │ │ user_id  │
     │         │ └──────────┘
     │         │
     │         │ ┌──────────┐
     └────────►│ │ report   │
               │ │──────────│
               │ │ id (PK)  │
               │ │ art_id   │
               │ │ user_id  │
               │ │ reason   │
               │ │ status   │
               │ └──────────┘
```

### 表结构说明

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `user` | 用户表 | id, username, password, nickname, avatar, role |
| `article` | 文章表 | id, title, content, category_id, user_id, like_count, status |
| `category` | 分类表 | id, name, description, sort_order |
| `comment` | 评论表 | id, content, article_id, user_id, parent_id |
| `article_like` | 点赞表 | id, article_id, user_id（联合唯一索引） |
| `post` | 帖子表 | id, title, content, user_id, reply_count |
| `reply` | 回复表 | id, content, post_id, user_id |
| `report` | 举报表 | id, article_id, user_id, reason, status |

---

## API 接口文档

### 用户接口 `/api/user`

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| POST | `/api/user/register` | 用户注册 | ❌ |
| POST | `/api/user/login` | 用户登录 | ❌ |
| GET | `/api/user/info` | 获取当前用户信息 | ✅ |
| PUT | `/api/user/info` | 更新用户信息 | ✅ |
| GET | `/api/user/list` | 获取用户列表（分页） | ✅ 管理员 |
| DELETE | `/api/user/{id}` | 删除用户 | ✅ 管理员 |

### 文章接口 `/api/article`

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/article/list` | 文章列表（分页、筛选） | ❌ |
| GET | `/api/article/detail/{id}` | 文章详情 | ❌ |
| POST | `/api/article` | 发布文章 | ✅ |
| PUT | `/api/article` | 编辑文章 | ✅ |
| DELETE | `/api/article/{id}` | 删除文章 | ✅ |
| POST | `/api/article/like/{id}` | 点赞/取消点赞 | ✅ |
| GET | `/api/article/my` | 我的文章列表 | ✅ |

### 分类接口 `/api/category`

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/category/list` | 分类列表 | ❌ |
| POST | `/api/category` | 添加分类 | ✅ 管理员 |
| PUT | `/api/category` | 编辑分类 | ✅ 管理员 |
| DELETE | `/api/category/{id}` | 删除分类 | ✅ 管理员 |

### 评论接口 `/api/comment`

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/comment/list/{articleId}` | 文章评论列表 | ❌ |
| POST | `/api/comment` | 发表评论 | ✅ |
| DELETE | `/api/comment/{id}` | 删除评论 | ✅ |

### 帖子接口 `/api/post`

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/post/list` | 帖子列表（分页） | ❌ |
| GET | `/api/post/detail/{id}` | 帖子详情 | ❌ |
| POST | `/api/post` | 发布帖子 | ✅ |
| DELETE | `/api/post/{id}` | 删除帖子 | ✅ |
| GET | `/api/post/reply/{postId}` | 帖子回复列表 | ❌ |
| POST | `/api/post/reply` | 发表回复 | ✅ |

### 举报接口 `/api/report`

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| POST | `/api/report` | 举报文章 | ✅ |
| GET | `/api/report/list` | 举报列表（分页） | ✅ 管理员 |
| PUT | `/api/report/handle/{id}` | 处理举报 | ✅ 管理员 |

---

## 系统架构图

```
┌─────────────────────────────────────────────────────────┐
│                      浏览器 (Chrome/Firefox/Edge)         │
│                    ┌─────────────────────┐               │
│                    │   Vue2 + Element UI │               │
│                    │  ┌───────────────┐  │               │
│                    │  │  Vue Router   │  │               │
│                    │  │  Vuex Store   │  │               │
│                    │  │  Axios (API)  │  │               │
│                    │  └───────┬───────┘  │               │
│                    └──────────┼──────────┘               │
└──────────────────────────────┼───────────────────────────┘
                               │ HTTP (localhost:8081 → 代理 → 8080)
                               ▼
┌──────────────────────────────────────────────────────────┐
│                  Spring Boot 后端 (localhost:8080)        │
│                                                          │
│  ┌────────────┐  ┌────────────┐  ┌───────────────────┐  │
│  │ Controller │→ │  Service   │→ │   Mapper (MyBatis) │  │
│  │ (REST API) │  │ (业务逻辑)  │  │   (数据库操作)      │  │
│  └────────────┘  └────────────┘  └────────┬──────────┘  │
│       ↑                                    │             │
│  ┌────┴──────────┐                         │             │
│  │ LoginInterceptor│                       │             │
│  │ (JWT 验证)     │                         │             │
│  └───────────────┘                         │             │
└────────────────────────────────────────────┼─────────────┘
                                             │
                                             ▼
                                    ┌─────────────────┐
                                    │  MySQL 数据库    │
                                    │  (pet_blog)     │
                                    │                 │
                                    │ user    article │
                                    │ comment category│
                                    │ post    reply   │
                                    │ report  like    │
                                    └─────────────────┘
```

---

## 测试账号

系统初始化后包含以下测试账号：

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 可访问后台管理 |
| 普通用户 | testuser | 123456 | 普通用户账号 |

---

## 常见问题

### Q1: 后端启动报错 `Communications link failure`
**原因**：MySQL 服务未启动或连接配置错误
**解决**：
1. 确认 MySQL 服务已启动
2. 检查 `application.yml` 中的数据库地址、端口、用户名和密码

### Q2: 前端启动报错 `Module not found`
**原因**：依赖未安装
**解决**：
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Q3: 前端请求后端报 404
**原因**：代理配置问题
**解决**：确认 `vue.config.js` 中的代理目标地址为 `http://localhost:8080`

### Q4: 数据库中文乱码
**原因**：数据库编码不是 UTF-8
**解决**：确保创建数据库时使用 `utf8mb4` 编码（已在 init.sql 中配置）

---

## 项目亮点

1. **分层架构清晰**：Controller → Service → Mapper 三层分离，职责明确
2. **JWT 无状态认证**：使用 Token 机制，无需服务端存储会话
3. **MyBatis-Plus 简化开发**：内置 CRUD 方法，减少重复代码
4. **统一响应格式**：所有接口返回统一的 `Result<T>` 格式
5. **完善的中文注释**：每个类和方法都有详细的中文注释说明
6. **逻辑删除**：数据不物理删除，通过 `deleted` 字段标记
7. **自动填充**：创建时间和更新时间自动维护
8. **前端组件化**：Vue 单文件组件，模块化清晰

---

## 课程设计时间安排参考

| 阶段 | 时间 | 内容 |
|------|------|------|
| 需求分析 | 第1天 | 确定功能需求，编写需求文档 |
| 总体设计 | 第2天 | 系统架构设计，绘制架构图 |
| 数据库设计 | 第3天 | 设计表结构，编写数据库文档 |
| 详细设计与编码 | 第4-8天 | 分模块编码实现 |
| 测试 | 第9天 | 单元测试、集成测试、系统测试 |
| 答辩 | 第10天 | 项目演示与答辩 |

---

**© 2024 赣南科技学院 - 宠物博客系统课程设计**
