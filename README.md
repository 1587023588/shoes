# ShoesNative（Android 原生示例）

这是一个可直接在 Android Studio 导入并运行的轻量级原生示例工程：
- Kotlin + ViewBinding
- BottomNavigationView 四个 Tab（首页/精神谱系/购物车/我的）
- 简单 Fragment 占位布局，便于你逐步把小程序页面重写为 Android 原生布局 XML

## 如何打开
1. 打开 Android Studio -> Open -> 选择 `shoes-app/android-native`。
2. 首次会自动下载 Gradle 依赖，等待 Sync 完成。
3. 选择一个模拟器或插上安卓手机，点击 Run ▶️ 即可。

> 同步失败小贴士：
> - 若提示找不到 `@mipmap/ic_launcher`，请确保已拉取 `res/mipmap-anydpi-v26/ic_launcher.xml` 和 `res/drawable/ic_launcher_foreground.xml`。
> - 若 ViewBinding 类缺失（如 `ActivityMainBinding`），在 AS 中执行 Build -> Rebuild Project 以触发生成。
> - 代理或网络慢导致依赖下载失败，可在 Gradle 设置里配置国内镜像。

## 结构
- app/src/main/java/com/example/shoes/
  - MainActivity.kt：承载底部导航与 Fragment 切换
  - HomeFragment.kt / CategoryFragment.kt / CartFragment.kt / MineFragment.kt：四个占位页面
- app/src/main/res/layout/
  - activity_main.xml：主界面布局（Fragment 容器 + BottomNavigationView）
  - fragment_simple.xml：片段通用占位布局
- app/src/main/res/menu/bottom_nav_menu.xml：底部导航菜单

## 群聊（社群）快速体验
- 主界面底部“消息”按钮或“+”按钮，均可进入群聊 `ChatActivity`。
- 群聊会连接后端 WebSocket：`ws://10.0.2.2:8080/ws/chat?room=public&user=你的昵称`（模拟器访问宿主机）。
- 需要先启动后端（见下文“后端启动”）。

## 后端启动（Spring Boot）
- 推荐使用 JDK 21；在 `shoes_houduan` 目录运行：
  - `mvn -DskipTests clean package`
  - `java -jar target/shoes_houduan-1.0.0.jar --spring.profiles.active=chat`
- `chat` profile 会禁用数据源/JPA，便于只体验群聊功能。

## 下一步：把页面重写为原生
- 在 res/layout 下新增你的页面 XML（如 `fragment_home.xml`），使用 ConstraintLayout/RecyclerView 等组件。
- 在对应 Fragment 中用新的 ViewBinding 绑定新布局。
- 图片放入 res/drawable 或 mipmap；颜色写在 res/values/colors.xml。
- 网络请求可加 Retrofit/OkHttp；列表用 RecyclerView；状态管理可用 ViewModel + LiveData。

如需我把你的小程序“首页”按图搭一个原生 XML + 适配器的网格/轮播模板，请告诉我页面元素，我可以直接在此工程内补上。
