# shoes_houduan - Java 21 升级说明

本项目已升级为使用 Java 21 进行编译与运行（Spring Boot 3.3.4 已兼容 Java 21）。

## 先决条件
- JDK 21（推荐 Microsoft Build of OpenJDK 21 或 Eclipse Temurin 21）
- Maven 3.9+（系统未安装 Maven 时需先安装，或后续添加 Maven Wrapper）

### 使用 winget 快速安装（可选）
```powershell
# 任选其一
winget install -e --id Microsoft.OpenJDK.21
# 或
winget install -e --id EclipseAdoptium.Temurin.21.JDK

# 安装后（当前会话）自动切换到 JDK 21（若默认未切换）
$jdk = (Get-ChildItem 'C:\Program Files\Microsoft' -Directory -ErrorAction SilentlyContinue | Where-Object { $_.Name -like 'jdk-21*' } | Select-Object -First 1).FullName
if (-not $jdk) { $jdk = (Get-ChildItem 'C:\Program Files\Eclipse Adoptium' -Directory -ErrorAction SilentlyContinue | Get-ChildItem -Directory | Where-Object { $_.Name -like 'jdk-21*' } | Select-Object -First 1).FullName }
if ($jdk) { $env:JAVA_HOME = $jdk; $env:Path = "$env:JAVA_HOME\bin;" + $env:Path }
java -version
```

## 快速检查（PowerShell）
```powershell
java -version
mvn -version
```
确保输出中的 Java 为 21，Maven 可用。

## 构建与运行
```powershell
# 进入项目目录
cd "c:\Users\gsy_666\Desktop\shoes-app\shoes_houduan"

# 打包（跳过测试）
mvn -DskipTests package

# 运行
mvn spring-boot:run
```

## 群聊（WebSocket）
后端提供简易群聊端点：

- URL: `ws://localhost:8080/ws/chat?room=public&user=Alice`
- 协议：原生 WebSocket（非 STOMP）
- 消息示例：
  - 客户端发消息：`{"type":"message","content":"hello"}`
  - 服务端广播：`{"type":"message","room":"public","user":"Alice","content":"hello","timestamp":"..."}`

Android 模拟器连接宿主机请使用 `10.0.2.2`：
`ws://10.0.2.2:8080/ws/chat?room=public&user=Alice`

## 常见问题
- 如果出现 “release version 21 not supported”，说明当前 Maven 使用的 JDK 不是 21。
  - 在当前 PowerShell 会话中临时指定：
    ```powershell
    $env:JAVA_HOME = "C:\\Program Files\\Microsoft\\jdk-21"
    $env:Path = "$env:JAVA_HOME\\bin;" + $env:Path
    mvn -version
    ```
  - 或安装 JDK 21 并将 JAVA_HOME 指向 JDK 21，确保 `java -version` 输出为 21。
- 若系统未安装 Maven，可以：
  - 安装 Maven 3.9+ 并加入 PATH；
  - 或者后续在项目中添加 Maven Wrapper（无需全局 Maven）。

## Android 子项目说明
`android-native` 目前使用 AGP 8.13 与 Kotlin 1.9.x，建议继续保持 Java 17 的 `compileOptions` 与 `kotlinOptions`，除非同时升级到支持 Java 21 的 AGP 与 Kotlin 版本。
