# Chat 模式后端一键启动与连接指南

本指南帮助你在开发阶段一键启动后端（chat 模式，无数据库），并说明前端/Android 的连接方式。

## 一键启动

你有两种方式：

- VS Code 任务：
  1. 打开文件夹 `shoes_houduan`。
  2. 运行任务：Terminal → Run Task… → 选择 `Run Backend (chat)`。
  3. 首次运行会自动下载 Maven（若本机没有），随后启动在 `http://127.0.0.1:8080`。

- PowerShell 脚本：
  1. 右键以 PowerShell 打开 `shoes_houduan/scripts` 目录。
  2. 执行：
     ```powershell
     ./run-chat.ps1
     ```
     若需先打包（可选）：
     ```powershell
     ./run-chat.ps1 -Rebuild
     ```

脚本会自动选择 `mvnw.cmd` 或本机的 `mvn`，如都没有则自动下载 Maven 3.9.11 到 `%USERPROFILE%\.maven` 并使用。

## 登录接口自测

```powershell
# 在 shoes_houduan/scripts 目录执行
./test-login.ps1
```
预期返回：
```json
{"token":"<JWT>"}
```

## Android/前端如何连接

- 本机浏览器/前端：
  - REST: `http://127.0.0.1:8080`
  - WebSocket: `ws://127.0.0.1:8080/ws/chat`
- Android 模拟器：
  - REST: `http://10.0.2.2:8080`
  - WebSocket: `ws://10.0.2.2:8080/ws/chat`
- 真机（同一 Wi-Fi）：
  1. `ipconfig` 查看电脑局域网 IP（如 `192.168.x.x`）。
  2. 用该 IP 访问：`http://192.168.x.x:8080` 或 `ws://192.168.x.x:8080/ws/chat`。
  3. 如访问不通，检查 Windows 防火墙是否允许 8080 入站，或允许 Java 程序通过防火墙。

## 配置说明

- Chat 配置文件：`src/main/resources/application-chat.yml`
  - 已内置强度足够的 JWT 密钥：`app.jwt.secret`（可自行更换）。
  - Token 过期时间：`app.jwt.expires-minutes`（默认 1440 分钟）。
  - 已禁用数据源/JPA 自动配置，chat 模式无需数据库即可启动。

## 常见问题

- 8080 端口不监听/无法连接：
  - 确认任务/脚本窗口未被关闭；重试 `./run-chat.ps1`。
  - 使用 `netstat -ano | findstr :8080` 查看端口是否在 LISTENING。
  - 检查是否被防火墙拦截（尤其是真机访问）。
- 登录报错 WeakKey/签名失败：
  - 确认 `application-chat.yml` 中 `app.jwt.secret` 为长度≥32字节的字符串。
