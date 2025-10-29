# 购买功能（本地可跑通版）

本项目已内置一套“本地可跑通”的购买闭环，无需后端/支付账号即可体验：

- 商品详情页：`/pages/goods/details/index`
  - 选择尺码等规格后，支持【加入购物车】或【立即购买】
- 购物车页：`/pages/cart/index`
  - 展示本地购物车（使用 `wx.setStorageSync` 持久化）
  - 点击【去结算】进入结算页
- 结算页：`/pages/order/order-confirm/index`
  - 支持两种下单方式：
    - 本地下单：不走网络，直接创建本地订单（推荐新手体验）
    - 模拟接口下单：调用 mock 接口并走模拟微信支付（无需商户）
- 支付结果：`/pages/order/pay-result/index`
- 订单列表/详情：`/pages/order/order-list/index`、`/pages/order/order-detail/index`
  - 数据读取自本地订单存储

## 如何开启/体验

1. 保持配置 `config/index.js` 中：

```js
export const config = { useMock: true };
```

2. 在微信开发者工具中运行小程序：
   - 首页/商品列表 -> 任意商品进入详情 -> 选择规格 -> 加入购物车
   - 购物车 -> 去结算 -> 结算页点击【本地下单】
   - 自动跳转到订单详情，可在订单列表看到该订单

> 注：Android 原生模块未接入购买；本闭环依赖小程序/uni-app 端实现。

## 技术实现要点

- 购物车：`services/cart/localCart.js`
  - 使用 `wx.getStorageSync`/`wx.setStorageSync` 存取数组结构
  - 对外提供 `addItem/getCartItems/clearCart/updateQuantity` 等方法
- 订单：`services/order/localOrder.js`
  - 生成 `orderNo`、持久化订单数组
  - `createOrder/listOrders/getOrder/updateOrderStatus`
- 结算：`pages/order/order-confirm/index.js`
  - `onSubmitLocalOrder()` 走本地创建订单并清理购物车中已下单项
  - 也保留 `submitOrder()` 模拟接口下单与支付
- 支付：`pages/order/order-confirm/pay.js`
  - `wechatPayOrder()` 直接回调 `paySuccess()` 模拟支付成功

## 接入真实后端与微信支付（可选）

- 后端：对接 `shoes_houduan`（Spring Boot）或你的现有服务，按如下接口改造：
  - 结算明细：`services/order/orderConfirm.fetchSettleDetail`
  - 提交订单：`services/order/orderConfirm.dispatchCommitPay`
  - 订单列表/详情：`services/order/orderList.js`/`services/order/orderDetail.js`
- 微信支付：在 `pay.js` 中用 `wx.requestPayment` 替换当前模拟逻辑，并按商户配置签名。
- 切换到真实 API：把 `config.useMock` 置为 `false`，并在对应 `services/*` 内实现真实 HTTP 请求。

## 常见问题

- 结算页金额为 0？请确认加入购物车的商品 `price` 字段是否为分（整数）。
- 无法滚动/适配差异：优先使用 `scroll-view` 或容器 `min-height: 100%`，避免全局 `overflow: hidden`。
- 模拟器 ADB 离线：请确保 Android SDK 的 `platform-tools` 在 PATH 中，或优先用真机/微信开发者工具调试小程序。
