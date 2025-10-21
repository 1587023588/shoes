import { OrderStatus } from '../config';
import { listOrders, mapStatusToDisplay } from '../../../services/order/localOrder';
import { cosThumb } from '../../../utils/util';

Page({
  page: {
    size: 5,
    num: 1,
  },

  data: {
    tabs: [
      { key: -1, text: '全部' },
      { key: OrderStatus.PENDING_PAYMENT, text: '待付款', info: '' },
      { key: OrderStatus.PENDING_DELIVERY, text: '待发货', info: '' },
      { key: OrderStatus.PENDING_RECEIPT, text: '待收货', info: '' },
      { key: OrderStatus.COMPLETE, text: '已完成', info: '' },
    ],
    curTab: -1,
    orderList: [],
    listLoading: 0,
    pullDownRefreshing: false,
    emptyImg: 'https://tdesign.gtimg.com/miniprogram/template/retail/order/empty-order-list.png',
    backRefresh: false,
    status: -1,
  },

  onLoad(query) {
    let status = parseInt(query.status);
    status = this.data.tabs.map((t) => t.key).includes(status) ? status : -1;
    this.init(status);
    this.pullDownRefresh = this.selectComponent('#wr-pull-down-refresh');
  },

  onShow() {
    if (!this.data.backRefresh) return;
    this.onRefresh();
    this.setData({ backRefresh: false });
  },

  onReachBottom() {
    if (this.data.listLoading === 0) {
      this.getOrderList(this.data.curTab);
    }
  },

  onPageScroll(e) {
    this.pullDownRefresh && this.pullDownRefresh.onPageScroll(e);
  },

  onPullDownRefresh_(e) {
    const { callback } = e.detail;
    this.setData({ pullDownRefreshing: true });
    this.refreshList(this.data.curTab)
      .then(() => {
        this.setData({ pullDownRefreshing: false });
        callback && callback();
      })
      .catch((err) => {
        this.setData({ pullDownRefreshing: false });
        Promise.reject(err);
      });
  },

  init(status) {
    status = status !== undefined ? status : this.data.curTab;
    this.setData({
      status,
    });
    this.refreshList(status);
  },

  getOrderList(statusCode = -1, reset = false) {
    // 优先读取本地订单（真实使用）
    this.setData({ listLoading: 1 });
    const local = listOrders();
    if (local && local.length) {
      // 状态过滤：statusCode 对应 mapping
      let filtered = local;
      if (statusCode !== -1) {
        filtered = local.filter((o) => {
          // 旧模板 statusCode 是数值枚举，我们本地用字符串，做一层映射
          switch (statusCode) {
            case OrderStatus.PENDING_PAYMENT: return o.status === 'pending_pay';
            case OrderStatus.PENDING_DELIVERY: return o.status === 'paid';
            case OrderStatus.PENDING_RECEIPT: return o.status === 'shipped';
            case OrderStatus.COMPLETE: return o.status === 'completed';
            default: return true;
          }
        });
      }
      // 分页模拟
      const start = (this.page.num - 1) * this.page.size;
      const slice = filtered.slice(start, start + this.page.size);
      this.page.num++;
      const orderList = slice.map((o) => {
        const display = mapStatusToDisplay(o.status);
        return {
          id: o.orderNo,
          orderNo: o.orderNo,
          parentOrderNo: o.orderNo,
          storeId: '1000',
          storeName: '布鞋工坊直营店',
          status: o.status, // 保留字符串
          statusDesc: display.text,
            amount: String(o.totalAmount),
          totalAmount: String(o.totalAmount),
          logisticsNo: '',
          createTime: o.createTime,
          goodsList: (o.items || []).map((g, idx) => ({
            id: `${o.orderNo}-${idx}`,
            thumb: cosThumb(g.primaryImage, 70),
            title: g.title,
            skuId: g.skuId,
            spuId: g.spuId,
            specs: (g.specInfo || []).map((s) => s.specValue),
            price: String(g.price),
            num: g.quantity,
            titlePrefixTags: [],
          })),
          buttons: [],
          groupInfoVo: null,
          freightFee: '0',
        };
      });
      return new Promise((resolve) => {
        if (reset) {
          this.setData({ orderList: [] }, () => resolve());
        } else resolve();
      }).then(() => {
        this.setData({
          orderList: this.data.orderList.concat(orderList),
          listLoading: orderList.length > 0 ? 0 : 2,
        });
      });
    }

    // 无本地订单时直接置空态
    return new Promise((resolve) => {
      if (reset) {
        this.setData({ orderList: [] }, () => resolve());
      } else resolve();
    }).then(() => {
      this.setData({ listLoading: 2 }); // 空态
    });
  },

  onReTryLoad() {
    this.getOrderList(this.data.curTab);
  },

  onTabChange(e) {
    const { value } = e.detail;
    this.setData({
      status: value,
    });
    this.refreshList(value);
  },

  getOrdersCount() {
    const local = listOrders() || [];
    const { tabs } = this.data;
    tabs.forEach((tab) => {
      if (tab.key === -1) { tab.info = local.length ? String(local.length) : ''; return; }
      let count = 0;
      local.forEach((o) => {
        switch (tab.key) {
          case OrderStatus.PENDING_PAYMENT: if (o.status === 'pending_pay') count++; break;
          case OrderStatus.PENDING_DELIVERY: if (o.status === 'paid') count++; break;
          case OrderStatus.PENDING_RECEIPT: if (o.status === 'shipped') count++; break;
          case OrderStatus.COMPLETE: if (o.status === 'completed') count++; break;
          default: break;
        }
      });
      tab.info = count ? String(count) : '';
    });
    this.setData({ tabs });
    return Promise.resolve();
  },

  refreshList(status = -1) {
    this.page = {
      size: this.page.size,
      num: 1,
    };
    this.setData({ curTab: status, orderList: [] });

    return Promise.all([this.getOrderList(status, true), this.getOrdersCount()]);
  },

  onRefresh() {
    this.refreshList(this.data.curTab);
  },

  onOrderCardTap(e) {
    const { order } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/order/order-detail/index?orderNo=${order.orderNo}`,
    });
  },
});
