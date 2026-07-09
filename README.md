<h1 align="center">🧬 RabGenDB · 后端</h1>

<p align="center"><em>—— 狂犬病毒基因组数据库的 Spring Boot 后端</em></p>

<p align="center">
  <img src="https://img.shields.io/badge/Framework-Spring%20Boot%203.4-6DB33F?style=flat-square" />
  <img src="https://img.shields.io/badge/Language-Java%2023-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/ORM-MyBatis-red?style=flat-square" />
  <img src="https://img.shields.io/badge/DB-MySQL-4479A1?style=flat-square" />
  <img src="https://img.shields.io/badge/Type-Practice%20Project-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Status-Complete-brightgreen?style=flat-square" />
</p>

---

## 项目简介

**RabGenDB**（Rabies Genome Database）是一个狂犬病毒基因组数据库练手项目，支持序列元数据的**提交 → 审核 → 检索**全流程，并按**三级权限**（普通用户 / 管理员 / 超级管理员）划分功能。本仓库为其**后端**，以 REST 接口向前端（配套仓库 `005_SpringbootWeb_RabGenDB_Frontend`）提供服务。

## 技术栈

| 组件                      | 说明                                   |
| :------------------------ | :------------------------------------- |
| Spring Boot 3.4.1         | Web 服务框架（`spring-boot-starter-web`）|
| Java 23                   | 语言版本                               |
| MyBatis 3.0.4             | 持久层框架，映射 SQL 与实体            |
| PageHelper 1.4.6          | 物理分页插件                           |
| MySQL (mysql-connector-j) | 关系型数据库                           |
| Lombok                    | 简化实体样板代码                       |

## 目录结构

```
src/main/java/com/rabies/
├── RabiesApplication.java        # 启动类
├── controller/                   # 接口层：GenomeController、UserController
├── service/ + service/impl/      # 业务逻辑层
├── mapper/                       # MyBatis 映射接口：GenomeMapper、UserMapper
└── pojo/                         # 实体类：Genome、User
src/main/resources/
└── application.properties        # 数据源与 MyBatis 配置
```

## 数据模型

**User**（用户表）

| 字段                        | 说明                                              |
| :-------------------------- | :------------------------------------------------ |
| id / username / password    | 主键 / 用户名 / 密码                              |
| telephone / email / country | 联系方式与国家                                    |
| role                        | 角色：`0` 超级管理员 · `1` 管理员 · `2` 普通用户 |

**Genome**（序列表）

| 字段                                | 说明                    |
| :---------------------------------- | :---------------------- |
| id / accession                      | 主键 / 序列登录号       |
| collectionDate / collectionCountry  | 采集日期 / 采集国家     |
| rawHost / refinedHost               | 原始宿主 / 规范化宿主   |
| isSubmit                            | 审核状态标记            |
| username                            | 提交者                  |
| message                             | 审核意见 / 打回原因     |

## API 一览

**用户 · UserController**

| 方法 | 路径             | 功能                              |
| :--- | :--------------- | :-------------------------------- |
| POST | `/login`         | 登录校验                          |
| GET  | `/list`          | 列出所有用户                      |
| GET  | `/listPage`      | 用户分页查询（按 username / country）|
| POST | `/save`          | 新增用户                          |
| POST | `/mod`           | 修改用户信息                      |
| POST | `/modAuthority`  | 修改用户权限（role）              |
| GET  | `/del`           | 删除用户（按 username）           |

**序列 · GenomeController**

| 方法 | 路径                         | 功能                                    |
| :--- | :--------------------------- | :-------------------------------------- |
| POST | `/sequenceSave`              | 提交一条序列                            |
| POST | `/sequenceAccept`            | 审核通过                                |
| POST | `/sequenceReject`            | 审核打回                                |
| POST | `/reSubmit`                  | 重新提交被打回的序列                    |
| GET  | `/delSequence`               | 删除序列（按 accession）                |
| GET  | `/sequenceWaitingForCheck`   | 某用户的待审核序列（分页）              |
| GET  | `/rejectedApplications`      | 某用户被打回的序列（分页）              |
| GET  | `/acceptedApplications`      | 某用户已通过的序列（分页）              |
| GET  | `/genomePreciseSearchPage`   | 按 accession 精确检索（已通过）         |
| GET  | `/genomeFlexSearchPage`      | 按 country + refinedHost 模糊检索（已通过）|
| GET  | `/listGenomePage`            | 列出全部待审核序列（管理员）            |

## 运行

1. **建库**：在 MySQL 中创建数据库 `rabies`，并建好 `user`、`genome` 两张表。
2. **配置**：按本机情况修改 `src/main/resources/application.properties` 的数据源（默认 `localhost:3306/rabies`，账号 `root`）。
3. **启动**：
   ```bash
   mvn spring-boot:run
   ```
   服务运行在 `http://localhost:9090`，需配合前端仓库 `005_SpringbootWeb_RabGenDB_Frontend` 使用。
