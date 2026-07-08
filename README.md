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
- [外部访问](#外部访问)
- [演示数据](#演示数据)
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
| MyBatis-Plus | 3.5.3.1 | ORM 框架 |
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
- ✅ 文章点赞（点赞/取消点赞切换，防重复）
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

### 移动端适配
- ✅ 响应式三栏布局（桌面端）→ 单栏 + 抽屉菜单（移动端）
- ✅ 帖子预览 2 行截断
- ✅ 分类标签移动端切换为下拉选择器

---

## 项目结构

```
pet-blog-system/
├── backend/                          # 后端 Spring Boot 项目
│   ├── pom.xml                       # Maven 依赖配置
│   └── src/main/java/com/petblog/
│       ├── PetBlogApplication.java   # 主启动类
│       ├── config/                   # 配置类
│       ├── entity/                   # 实体类
│       ├── mapper/                   # Mapper 接口
│       ├── service/                  # Service 接口 + 实现
│       ├── controller/               # REST API
│       ├── dto/                      # 数据传输对象
│       └── util/                     # 工具类
├── frontend/                         # 前端 Vue 项目
│   ├── src/
│   │   ├── api/                      # API 请求封装
│   │   ├── components/               # 公共组件
│   │   ├── router/                   # 路由配置
│   │   ├── store/                    # Vuex 状态管理
│   │   ├── styles/                   # 全局样式
│   │   └── views/                    # 页面视图（11 个）
│   └── vue.config.js                 # 代理 + 开发服务器配置
├── demo-assets/                      # 演示用图片（git 跟踪）
├── setup-demo.ps1                    # 演示数据部署脚本
├── sql/
│   ├── init.sql                      # 数据库初始化脚本
│   └── seed_content.sql              # 扩展种子数据
├── uploads/                          # 用户上传文件（git 忽略）
├── start-tunnel.ps1                  # cloudflared 隧道脚本
├── DEVLOG.md                         # 开发操作记录
└── README.md                         # 项目说明文档
```

---

## 环境要求

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

```bash
# 确保 MySQL 服务已启动
mysql -u root -p < sql/init.sql

# 可选：导入扩展种子数据
mysql -u root -p < sql/seed_content.sql
```

### 第二步：部署演示数据（可选）

```powershell
# 将 demo-assets/ 中的演示图片复制到 uploads/
.\setup-demo.ps1
```

### 第三步：启动后端

```bash
cd backend
mvn spring-boot:run
```

### 第四步：启动前端

```bash
cd frontend
npm install
npm run serve
```

### 第五步：访问系统

- 本地访问：http://localhost:8081
- 后端 API：http://localhost:8080

---

## 外部访问

如需让其他人通过手机或远程访问，可使用 cloudflared 隧道：

```powershell
.\start-tunnel.ps1
```

终端会输出类似 `https://xxx-xxx.trycloudflare.com` 的公网地址。

> 每次启动会生成新地址，隧道关闭后失效。

---

## 演示数据

### 问题

`uploads/` 目录被 `.gitignore` 排除（避免提交用户上传的大文件），但演示时需要预置图片。

### 解决方案

项目中维护 `demo-assets/` 目录，存放演示用图片，该目录会被 git 跟踪。

```
demo-assets/
├── image/          # 演示文章封面图
└── avatar/         # 演示用户头像
```

演示前执行 `setup-demo.ps1` 脚本即可自动复制到 `uploads/` 目录。

如需将新的上传文件加入演示数据：

```powershell
# 将新上传的图片复制到 demo-assets
Copy-Item uploads/image/xxx.jpg demo-assets/image/
git add demo-assets/
git commit -m "feat: 添加演示图片"
```

---

## 数据库设计

### 数据表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `user` | 用户表 | id, username, password, nickname, avatar, role |
| `article` | 文章表 | id, title, content, category_id, user_id, like_count, view_count |
| `category` | 分类表 | id, name, description |
| `comment` | 评论表 | id, content, article_id, user_id |
| `article_like` | 点赞表 | id, article_id, user_id（联合唯一索引） |
| `post` | 帖子表 | id, title, content, user_id |
| `reply` | 回复表 | id, content, post_id, user_id |
| `report` | 举报表 | id, article_id, user_id, reason, status |

---

## API 接口文档

### 用户接口

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| POST | `/api/user/login` | 用户登录 | ❌ |
| POST | `/api/user/register` | 用户注册 | ❌ |
| GET | `/api/user/info` | 获取用户信息 | ✅ |
| PUT | `/api/user/info` | 更新用户信息 | ✅ |

### 文章接口

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/article/list` | 文章列表 | ❌ |
| GET | `/api/article/detail/{id}` | 文章详情 | ❌ |
| POST | `/api/article` | 发布文章 | ✅ |
| PUT | `/api/article` | 更新文章 | ✅ |
| DELETE | `/api/article/{id}` | 删除文章 | ✅ |
| POST | `/api/article/like/{id}` | 点赞/取消点赞 | ✅ |
| GET | `/api/article/my` | 我的文章 | ✅ |

### 分类接口

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/category/list` | 分类列表 | ❌ |
| POST | `/api/category` | 添加分类 | ✅（管理员） |

### 评论接口

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/comment/list/{articleId}` | 评论列表 | ❌ |
| POST | `/api/comment` | 发表评论 | ✅ |

### 帖子接口

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | `/api/post/list` | 帖子列表 | ❌ |
| GET | `/api/post/detail/{id}` | 帖子详情 | ❌ |
| POST | `/api/post` | 发布帖子 | ✅ |
| POST | `/api/post/reply` | 发表回复 | ✅ |

### 媒体接口

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| POST | `/api/media/upload/image` | 上传图片 | ✅ |
| POST | `/api/media/upload/video` | 上传视频 | ✅ |

---

## 测试账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 可访问后台管理 |
| 普通用户 | testuser | 123456 | 普通用户账号 |

---

## 常见问题

### Q1: 后端启动报错 Communications link failure
**原因**：MySQL 服务未启动或连接配置错误
**解决**：确认 MySQL 已启动，检查 `application.yml` 中的数据库配置

### Q2: 前端启动报错 Module not found
**原因**：依赖未安装
**解决**：`cd frontend && rm -rf node_modules && npm install`

### Q3: 前端请求后端报 404
**原因**：代理配置问题
**解决**：确认 `vue.config.js` 中代理目标为 `http://localhost:8080`

### Q4: 数据库中文乱码
**原因**：数据库编码不是 UTF-8
**解决**：建库时使用 `utf8mb4` 编码

### Q5: 手机通过隧道访问报 Invalid Host header
**原因**：webpack-dev-server Host 检查
**解决**：`vue.config.js` 中已配置 `allowedHosts: 'all'`，重启前端即可

### Q6: 演示时图片不显示
**原因**：uploads/ 未提交到 git
**解决**：执行 `.\setup-demo.ps1` 部署演示图片

---

**© 2024 赣南科技学院 - 宠物博客系统课程设计**