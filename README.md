<<<<<<< HEAD
<!-- Project quick start appended by tooling -->

## 快速开始（本仓库定制）

运行后端（Spring Boot + MySQL）
- 数据库：在本地创建 `shoes` 数据库，并在 `shoes_houduan/src/main/resources/application.yml` 配置用户名/密码
- 启动：在 IDE 运行 `org.example.shoes.ShoesApplication`
- 首次启动会插入：用户 `test/123456` 与两条商品

运行 Android（android-native 模块）
- 使用 Android Studio/IDE 打开 `android-native`
- 设备：Android 模拟器（或真机），App 会访问 `http://10.0.2.2:8080`
- 登录：在“我的”页点击登录，使用 `test/123456`

Git 提交流程建议
- 不提交构建产物：已在根 `.gitignore` 加了全局忽略（含 android-native/.gradle、app/build、local.properties、后端 target）
- 提交前执行：`git status` 确认仅源代码被跟踪
- 同步远端：`git pull --ff-only`，必要时 `git rebase origin/master` 再 `git push`

---

<p align="center">
  <a href="https://tdesign.tencent.com/" target="_blank">
    <img alt="TDesign Logo" width="200" src="https://tdesign.gtimg.com/site/TDesign.png">
  </a>
</p>
//111
<p align="center">
  <a href="https://img.shields.io/github/stars/Tencent/tdesign-miniprogram-starter-retail">
    <img src="https://img.shields.io/github/stars/Tencent/tdesign-miniprogram-starter-retail" alt="License">
  </a>  
  <a href="https://github.com/Tencent/tdesign-miniprogram-starter-retail/issues">
    <img src="https://img.shields.io/github/issues/Tencent/tdesign-miniprogram-starter-retail" alt="License">
  </a>  
  <a href="https://github.com/Tencent/tdesign-miniprogram-starter-retail/LICENSE">
    <img src="https://img.shields.io/github/license/Tencent/tdesign-miniprogram-starter-retail" alt="License">
  </a>
  <a href="https://www.npmjs.com/package/tdesign-miniprogram">
    <img src="https://img.shields.io/npm/v/tdesign-miniprogram.svg?sanitize=true" alt="Version">
  </a>
  <a href="https://www.npmjs.com/package/tdesign-miniprogram">
    <img src="https://img.shields.io/npm/dw/tdesign-miniprogram" alt="Downloads">
  </a>
</p>

# TDesign 零售行业模版示例小程序

TDesign 零售模版示例小程序采用 [TDesign 企业级设计体系小程序解决方案](https://tdesign.tencent.com/miniprogram/overview) 进行搭建，依赖 [TDesign 微信小程序组件库](https://github.com/Tencent/tdesign-miniprogram)，涵盖完整的基本零售场景需求。

## :high_brightness: 预览

<p>请使用微信扫描以下二维码：</p>

 <img src="https://we-retail-static-1300977798.cos.ap-guangzhou.myqcloud.com/retail-mp/common/qrcode.jpeg" width = "200" height = "200" alt="模版小程序二维码" align=center />

## :pushpin: 项目介绍

### 1. 业务介绍

零售行业模版小程序是个经典的单店版电商小程序，涵盖了电商的黄金链路流程，从商品->购物车->结算->订单等。小程序总共包含 28 个完整的页面，涵盖首页，商品详情页，个人中心，售后流程等基础页面。采用 mock 数据进行展示，提供了完整的零售商品展示、交易与售后流程。页面详情：

<img src="https://tdesign.gtimg.com/miniprogram/template/retail/tdesign-starter-readmeV1.png" width = "650" height = "900" alt="模版小程序页面详情" align=center />

主要页面截图如下：

<p align="center">
    <img alt="example-home" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v1/home.png" />
    <img alt="example-sort" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v2/sort.png" />
    <img alt="example-cart" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v1/cart.png" />
    <img alt="example-user-center" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v1/user-center.png" />
    <img alt="example-goods-detail" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v1/goods-detail.png" />
    <img alt="example-pay" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v1/pay.png" />
    <img alt="example-order" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v1/order.png" />
    <img alt="example-order-detail" width="200" src="https://tdesign.gtimg.com/miniprogram/template/retail/example/v2/order.png" />
</p>

### 2. 项目构成

零售行业模版小程序采用基础的 JavaScript + WXSS + ESLint 进行构建，降低了使用门槛。

项目目录结构如下：

```
|-- tdesign-miniprogram-starter
    |-- README.md
    |-- app.js
    |-- app.json
    |-- app.wxss
    |-- components	//	公共组件库
    |-- config	//	基础配置
    |-- custom-tab-bar	//	自定义 tabbar
    |-- model	//	mock 数据
    |-- pages
    |   |-- cart	//	购物车相关页面
    |   |-- coupon	//	优惠券相关页面
    |   |-- goods	//	商品相关页面
    |   |-- home	//	首页
    |   |-- order	//	订单售后相关页面
    |   |-- promotion-detail	//	营销活动页面
    |   |-- usercenter	//	个人中心及收货地址相关页面
    |-- services	//	请求接口
    |-- style	//	公共样式与iconfont
    |-- utils	//	工具库
```

### 3. 数据模拟

零售小程序采用真实的接口数据，模拟后端返回逻辑，在小程序展示完整的购物场景与购物体验逻辑。

### 4. 添加新页面

1. 在 `pages `目录下创建对应的页面文件夹
2. 在 `app.json` 文件中的 ` "pages"` 数组中加上页面路径
3. [可选] 在 `project.config.json` 文件的 `"miniprogram-list"` 下添加页面配置

## :hammer: 构建运行

1. `npm install`
2. 小程序开发工具中引入工程
3. 构建 npm

## :art: 代码风格控制

`eslint` `prettier`

## :iphone: 基础库版本

最低基础库版本`^2.6.5`

## :dart: 反馈

企业微信群
TDesign 团队会及时在企业微信大群中同步发布版本、问题修复信息，也会有一些关于组件库建设的讨论，欢迎微信或企业微信扫码入群交流：

<img src="https://oteam-tdesign-1258344706.cos.ap-guangzhou.myqcloud.com/site/doc/TDesign%20IM.png" width = "200" height = "200" alt="模版小程序页面详情" align=center />

邮件联系：tdesign@tencent.com

## :link: TDesign 其他技术栈实现

- 移动端 小程序 实现：[mobile-miniprogram](https://github.com/Tencent/tdesign-miniprogram)
- 桌面端 Vue 2 实现：[web-vue](https://github.com/Tencent/tdesign-vue)
- 桌面端 Vue 3 实现：[web-vue-next](https://github.com/Tencent/tdesign-vue-next)
- 桌面端 React 实现：[web-react](https://github.com/Tencent/tdesign-react)

## :page_with_curl: 开源协议

TDesign 遵循 [MIT 协议](https://github.com/Tencent/tdesign-miniprogram-starter-retail/LICENSE)。
=======
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
>>>>>>> fe7c71a1076f5e699278a45d09d983b4089747e4
