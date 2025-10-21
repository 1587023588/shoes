# Android 打包与运行指南（uni-app）

本目录是从微信小程序迁移到 uni-app 的基线工程骨架，目标：打包 Android App。

## 1. 打开工程
- 使用 HBuilderX：文件 -> 打开 -> 选择本目录（`shoes_app/uni-app`）。

## 2. 运行调试
- 运行 -> 运行到 Android App 基座（或 Android 模拟器/真机）。
- 首次运行会提示安装基座/签名包，按提示完成。

## 3. 云端打包 APK（推荐）
- 发行 -> 原生App-云端打包。
- 填写应用名、包名（如 `com.example.shoes`）、图标、启动图。
- Android：无需本地 SDK；等待云端出包，下载 APK 安装测试。

## 4. 常见问题
- 无法找到页面：确认 `pages.json` 中的路径与 `pages/` 目录一致。
- 图标/启动图：在 manifest 中配置，建议提供多分辨率素材。
- 权限：定位/相机/相册/存储等在 `manifest.json` 的 `app-plus.distribute.android.permissions` 中声明。

## 5. 迁移下一步
- 参考仓库根目录的 `MIGRATE_TO_APP.md`，按转换报告逐步把 `wx.*` 改为 `uni.*`，并替换组件库。
- 将小程序静态资源（如 `/static/tabbar/...`）拷贝到本目录 `static/` 对应路径。

如需我协助把 `shoes_app` 的页面批量迁移到本基线工程，请告诉我优先级页面与是否有支付/登录需求。
