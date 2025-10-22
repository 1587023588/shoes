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

### WebSocket 查询参数

- 基本参数：
  - `room`: 房间名，默认 `public`
  - `user`: 用户名，未登录时可随意取值
- 可选鉴权：
  - `token`: 携带 JWT（`Authorization: Bearer` 的同一 Token），服务端将优先使用 Token 中的 subject 作为用户名。
  - 未携带或 Token 非法时，连接仍然允许（开发模式便于调试），仅用户名回退为 `user` 或 `Anonymous`。

示例：

```
ws://10.0.2.2:8080/ws/chat?room=public&user=Alice
ws://10.0.2.2:8080/ws/chat?room=public&token=<JWT>

### 绑定到“会话”（社群/私聊，需数据库）

当 `room` 为数字（如 `123`），或使用前缀 `conv:123` 时，服务端会将其视为持久化会话（Conversation）：

- 需要携带有效 `token`，并且用户必须是该会话的成员，否则连接会被拒绝（1008 NOT_MEMBER）。
- 通过该连接发送的消息会被持久化到数据库，并广播给会话内在线成员。
- 历史分页可通过 REST 获取：`GET /api/chat/conversations/{id}/messages?size=20&before=<ISO-8601>`。

如何获得会话 ID：

- 列出我的会话：`GET /api/chat/conversations`（需 `Authorization: Bearer <JWT>`）。
- 确保私聊存在（若无则新建）：`POST /api/chat/conversations/dm`，Body：`{"userId": 2}`。
- 新建群组：`POST /api/chat/conversations/group`，Body：`{"name":"小组","memberIds":[2,3]}`。

拿到 `id` 后，WS 连接示例：

```
ws://10.0.2.2:8080/ws/chat?room=123&token=<JWT>
ws://10.0.2.2:8080/ws/chat?room=conv:123&token=<JWT>
```
```

## 配置说明

- Chat 配置文件：`src/main/resources/application-chat.yml`
  - 已内置强度足够的 JWT 密钥：`app.jwt.secret`（可自行更换）。
  - Token 过期时间：`app.jwt.expires-minutes`（默认 1440 分钟）。
  - 已禁用数据源/JPA 自动配置，chat 模式无需数据库即可启动。

## 客户端健壮性

- Android 端已开启 OkHttp 心跳（pingInterval=30s），断线后采用指数退避（1s→2s→4s…，最大10s）自动重连，最多重试 5 次。
- 首次进入房间与重连成功后，会自动回放该房间最近 50 条历史消息（仅驻内存，用于开发调试）。

## 常见问题

- 8080 端口不监听/无法连接：
  - 确认任务/脚本窗口未被关闭；重试 `./run-chat.ps1`。
  - 使用 `netstat -ano | findstr :8080` 查看端口是否在 LISTENING。
  - 检查是否被防火墙拦截（尤其是真机访问）。
- 登录报错 WeakKey/签名失败：
  - 确认 `application-chat.yml` 中 `app.jwt.secret` 为长度≥32字节的字符串。
