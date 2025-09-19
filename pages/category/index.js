import { getCategoryList } from '../../services/good/fetchCategoryList';

Page({
  data: {
    list: [],
  },

  async init() {
    try {
      const result = await getCategoryList();
      this.setData({
        list: result,
      });
    } catch (error) {
      console.error('category init error:', error);
    }
  },

  onShow() {
    // 安全调用自定义 TabBar 的 init
    try {
      const tab = this.getTabBar && this.getTabBar();
      tab && typeof tab.init === 'function' && tab.init();
    } catch (e) {}
  },

  // 选中分类后跳转商品列表
  onChange(e) {
    // 可按需从 e.detail.item 读取选中项并透传参数
    // const { item } = e.detail || {};
    wx.navigateTo({
      url: '/pages/goods/list/index',
    });
  },

  onLoad() {
    this.init();
  },
});
