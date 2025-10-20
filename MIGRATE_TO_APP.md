# 将微信小程序 `shoes_app` 打包为原生 App（Android/iOS）指南

本项目为微信小程序（包含 `app.json`、`project.config.json`、`tdesign-miniprogram` 等）。要变成真正的手机 App（可以安装 APK/上架 App Store），推荐的主路线是：使用 DCloud 的 uni-app + HBuilderX 的「小程序转 uni-app」功能进行迁移，然后打包原生 App。

下文提供完整步骤、注意事项与替代方案（无需改动现有 Node 服务端）。

---

## 路线 A（推荐）：小程序转 uni-app，再打包 App

适用对象：当前 `shoes_app` 微信小程序。最终产物：Android APK / iOS IPA。

核心思路：
- 使用 HBuilderX 导入「小程序转 uni-app」，把 WXML/WXSS/WX API 转为 uni-app 规范（Vue 语法、`uni.*` API）。
- 按转换报告修复不兼容点（尤其是三方组件、API 差异）。
- 在 HBuilderX 中运行到手机/模拟器调试，最后通过「发行 -> 原生App-云端打包」产出安装包。

### 步骤 1：准备环境（Windows）
1) 下载并安装 HBuilderX（含 App 开发版）
- 官网：https://www.dcloud.io/hbuilderx.html
- 建议下载「包含Android打包环境」版本以便真机/模拟器运行。

2) 注册 DCloud 账号（用于云端打包）。

3) Android 打包：云端打包无需本地 Android SDK；若需本地离线打包，安装 JDK、Android SDK，并在 HBuilderX 配置路径。

4) iOS 打包：需要 Apple 开发者账号、证书与描述文件，走云端打包即可，无需本地 macOS。

### 步骤 2：导入并转换为 uni-app
1) 打开 HBuilderX -> 文件 -> 从本地目录导入 -> 选择本项目 `shoes_app` 目录。
2) 工具 -> 小程序转 uni-app -> 选择本项目（类型选择「微信小程序」）。
3) 等待转换完成，HBuilderX 会在新目录生成 uni-app 工程（通常是 `uni-app` 风格的 src 结构，并标注转换建议）。
4) 阅读转换报告：
- 不支持的 API/组件会给出 TODO 注释。
- 自定义组件、分包、tabBar 会转换为 `pages.json` 配置。

### 步骤 3：修复与适配要点
- tdesign-miniprogram：这是「小程序版」TDesign 组件库，转成 uni-app 后需替换为适配 uni-app 的 UI 库（如 uView、ThorUI、uni-ui）或采用 TDesign Vue（Mobile）重构对应页面。
- 自定义 tabBar：uni-app 支持自定义 tabbar；需把现有 `custom-tab-bar` 逻辑迁移为 `pages/tabbar` 组件并在 `pages.json` 中配置。
- 分包 independent：小程序的 independent 分包 uni-app 不支持原语义，统一转为普通分包或合并页面。
- 微信专属 API：
  - 登录/用户信息：`wx.login`/`getUserProfile` 不适用于 App，需要改为自有账号体系、手机验证码、或第三方 OAuth（微信/支付宝/Apple）能力，参考 `uni.login`、`uni.getUserInfo` 插件市场方案。
  - 地址选择：小程序 `chooseAddress`（`requiredPrivateInfos`）在 App 端不可用，改为自建地址表单或使用三方地址库。
  - 地图/定位：请改用 `uni.getLocation`，并在 `manifest.json` 勾选定位权限，iOS 需配置用途说明。
  - 支付：App 端需接入微信/支付宝 App 支付（插件市场提供），接口与小程序支付不同。
- 网络请求：`wx.request` 替换为 `uni.request`，检查 baseURL、cookie、跨域策略。
- 文件/媒体：核对 `uni.chooseImage`、`uni.saveImageToPhotosAlbum` 等权限与平台差异。
- 路由：`wx.navigateTo` -> `uni.navigateTo` 等价替换。

### 步骤 4：配置 App 打包信息（manifest.json）
在 uni-app 工程的根目录打开 `manifest.json`，完成以下设置：
- 应用名称、应用标识（AppID/包名）
- 图标与启动图（多分辨率）
- Android 签名（keystore）与 iOS 证书（云端打包引导填写）
- 权限：定位、相册、相机、网络等
- 启动页面样式、横竖屏

HBuilderX -> 发行 -> 原生App-云端打包：填写必要信息后提交，等待云端生成 APK/IPA。

### 步骤 5：调试与验收
- 运行 -> 运行到手机或模拟器：连接 Android 设备或使用内置模拟器预览。
- 日志：HBuilderX 控制台 + 真机远程调试。
- 核对：启动、登录/个人中心、购物车、下单、支付（若已接）等关键路径。

---

## 路线 B：用 Taro/RN 重构（备选）
思路：使用 Taro 将小程序页面逐步迁移到 React 语法，目标端选择 RN（React Native），最终打包原生 App。

优点：多端同构能力强；RN 原生体验更好。
缺点：`tdesign-miniprogram` 不可直接复用，需要改为 TDesign React 或 RN UI 组件；改造成本高、周期长。
适用：计划中长期演进为 RN 生态；有 React 技术栈团队支持。

---

## 路线 C：Web 封装（仅适用于 Web 项目）
如果是纯 Web 项目（例如 `news_center/public` 静态站点），可用 Capacitor/Cordova 将 H5 站点封装成 App。

注意：当前 `shoes_app` 是微信小程序，并不能直接以 WebView 运行；必须先转换为 H5（如 kbone）再封装，但兼容与成本不如路线 A。

---

## 时间评估（经验值）
- 小型电商/展示类小程序 -> uni-app：1-3 天可跑通主路径，另需 2-7 天处理 UI 库替换与支付/登录改造。
- 上架准备：Android 0.5-1 天；iOS 需开发者账号与审核，1-2 周。

---

## 常见问题与解决
- 组件库替换：优先选 uView/uni-ui 等成熟方案，逐页替换。复杂组件（如自定义画布、图表）用原生插件或 H5 组件嵌入。
- 微信支付迁移：改为 App 端微信/支付宝支付（DCloud 插件市场）。服务端下单与签名逻辑需要根据渠道调整。
- 登录鉴权：移除小程序 openid 依赖，采用手机/验证码或三方 OAuth。服务端用户表要支持多来源。
- 地图与定位权限：Android 在 manifest 勾选权限；iOS 配置 `NSLocationWhenInUseUsageDescription`。
- 分包体积：uni-app 支持分包，按需在 `pages.json` 配置 `subPackages`。

---

## 交付清单（建议）
- uni-app 项目（完成首页、分类、购物车、个人中心四条主链路）
- manifest.json 完整配置（图标/启动图/权限/签名）
- 支付与登录方案落地（如需）
- 打包产物：Android APK、iOS 测试包（TestFlight）

---

## 我们可以帮你做的（下一步）
如需，我可以：
1) 基于你当前仓库，提供「小程序转 uni-app」后的初版骨架；
2) 标注所有需要替换的 `tdesign-miniprogram` 组件位置，列清单；
3) 补齐 `pages.json`、`manifest.json` 初始配置；
4) 指导你在 HBuilderX 中云端打包，产出 APK/IPA。

你只需确认目标平台（Android/iOS）与是否需要支付/登录等能力，我就可以开始落地迁移。

---

附录 A：`pages.json` 示例片段（供转换后参考）
```json
{
  "pages": [
    { "path": "pages/home/home", "style": { "navigationBarTitleText": "首页" } },
    { "path": "pages/category/index", "style": { "navigationBarTitleText": "精神谱系" } },
    { "path": "pages/cart/index", "style": { "navigationBarTitleText": "购物车" } },
    { "path": "pages/usercenter/index", "style": { "navigationBarTitleText": "我的" } }
  ],
  "tabBar": {
    "color": "#666666",
    "selectedColor": "#FF5F15",
    "backgroundColor": "#ffffff",
    "list": [
      { "pagePath": "pages/home/home", "text": "首页", "iconPath": "static/tabbar/首页.png", "selectedIconPath": "static/tabbar/首页.png" },
      { "pagePath": "pages/category/index", "text": "精神谱系", "iconPath": "static/tabbar/帆布鞋-copy.png", "selectedIconPath": "static/tabbar/帆布鞋-copy.png" },
      { "pagePath": "pages/cart/index", "text": "购物车", "iconPath": "static/tabbar/购物车.png", "selectedIconPath": "static/tabbar/购物车.png" },
      { "pagePath": "pages/usercenter/index", "text": "我的", "iconPath": "static/tabbar/用户中心-copy.png", "selectedIconPath": "static/tabbar/用户中心-copy.png" }
    ]
  }
}
```

附录 B：manifest 关键项提示
- Android 包名：例如 `com.example.shoes`
- iOS Bundle ID：例如 `com.example.shoes`
- 权限：定位、相机、相册、网络、存储
- 签名：Android keystore / iOS 证书与描述文件

---

如果需要我直接开始在本仓库内创建 uni-app 基线工程，请告诉我目标平台与是否需要支付/登录等功能，我会按以上步骤落地并在文档中持续同步改造清单。