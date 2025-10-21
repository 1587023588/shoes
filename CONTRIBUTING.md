# 贡献指南（多人协作）

本仓库采用“受保护主分支 + 短分支开发 + PR 合并”的流程，建议按以下规范协作。

## 分支策略
- 保护分支：`master`
  - 禁止直接 push/force push（在 GitHub 仓库 Settings > Branches 中开启保护）
  - 仅允许通过 PR 合并，且需通过状态检查（CI 通过）
- 开发分支：短分支开发，命名建议：
  - 功能：`feat/<模块>-<简述>`，例如：`feat/android-login`
  - 修复：`fix/<模块>-<简述>`，例如：`fix/backend-auth-npe`
  - 发布/紧急修复可根据需要：`release/*`、`hotfix/*`

## 提交信息（Conventional Commits）
基础格式：`<type>(scope): <subject>`
- type 常见取值：`feat`、`fix`、`docs`、`chore`、`refactor`、`test`、`build`、`ci`
- 示例：`feat(android): 登录页与会话存储`

## PR 规范
- 目标分支：`master`
- 至少 1 名 Reviewer 通过（建议 2 名）
- CI 通过后再合并，推荐使用“Squash and merge”（保持线性历史）
- PR 描述包含：变更点、影响范围、验证方式（如何跑、如何回归）

## 代码与构建
- 不要提交构建产物和本地配置
  - Android：`android-native/.gradle/`、`**/build/`、`local.properties`、`*.apk` 等
  - 后端：`shoes_houduan/target/`
  - 已在根 `.gitignore` 增强忽略
- 后端开发：JDK 17；启动 `ShoesApplication`；默认 DB：`jdbc:mysql://localhost:3306/shoes`（见 `application.yml`）
- Android 开发：基址 `http://10.0.2.2:8080`（模拟器访问宿主机）

## 合并前自检清单
- 本地能跑通（后端启动无错、接口可用；Android 基本流程可走）
- `git status` 仅包含源码变更
- 提交信息清晰、PR 描述完整

## CI 与保护建议
- 开启 GitHub Branch Protection：
  - 保护 `master`，禁止强推，要求 PR 与状态检查
  - 必要时要求 up-to-date 分支（避免旧基线合并）
- CI（已提供后端构建）：
  - 后端：Maven 构建（JDK 17，跳过测试可配置）
  - Android：可按需后续新增 assemble/lint workflow
