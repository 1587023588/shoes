import Dialog from 'tdesign-miniprogram/dialog/index';
import Toast from 'tdesign-miniprogram/toast/index';
import { fetchLocalCartGroupData } from '../../services/cart/cart';
import { updateQuantity, removeItem, getCartItems, asCartGroupData } from '../../services/cart/localCart';

Page({
  data: {
    cartGroupData: null,
  },

  onShow() {
    this.getTabBar().init?.();
    this.refreshData();
  },

  onLoad() {
    this.refreshData();
  },

  refreshData() {
    this.getCartGroupData().then((res) => {
      const cartGroupData = res.data;
      // 简化：本地数据结构已经是规整的，只补充缺失字段
      cartGroupData.storeGoods.forEach((store) => {
        store.isSelected = true;
        store.storeStockShortage = false;
        if (!store.shortageGoodsList) store.shortageGoodsList = [];
        store.promotionGoodsList.forEach((act) => {
          act.goodsPromotionList.forEach((g) => {
            g.originPrice = undefined;
          });
        });
      });
      this.setData({ cartGroupData });
    });
  },

  // 改写：总是从本地重新封装
  getCartGroupData() {
    return fetchLocalCartGroupData();
  },

  // 选择商品（本地版本：保持接口但不做单选逻辑，可扩展）
  selectGoodsService({ spuId, skuId, isSelected }) {
    // 预留：本地全选逻辑暂忽略
    return Promise.resolve();
  },

  selectStoreService({ storeId, isSelected }) { return Promise.resolve(); },

  changeQuantityService({ spuId, skuId, quantity }) {
    updateQuantity(spuId, skuId, quantity);
    return Promise.resolve();
  },

  deleteGoodsService({ spuId, skuId }) {
    removeItem(spuId, skuId);
    return Promise.resolve();
  },

  clearInvalidGoodsService() { return Promise.resolve(); },

  findGoods(spuId, skuId) {
    const data = asCartGroupData();
    for (const store of data.storeGoods) {
      for (const act of store.promotionGoodsList) {
        for (const g of act.goodsPromotionList) {
          if (g.spuId === spuId && g.skuId === skuId) {
            return { currentStore: store, currentActivity: act, currentGoods: g };
          }
        }
      }
    }
    return { currentStore: null, currentActivity: null, currentGoods: null };
  },

  onGoodsSelect(e) {
    const { goods: { spuId, skuId }, isSelected } = e.detail;
    const { currentGoods } = this.findGoods(spuId, skuId);
    if (!currentGoods) return;
    Toast({ context: this, selector: '#t-toast', message: `${isSelected ? '选择' : '取消'}"${currentGoods.title}"` });
  },

  onQuantityChange(e) {
    const { goods: { spuId, skuId }, quantity } = e.detail;
    this.changeQuantityService({ spuId, skuId, quantity }).then(() => this.refreshData());
  },

  onGoodsDelete(e) {
    const { goods: { spuId, skuId } } = e.detail;
    Dialog.confirm({ content: '确认删除该商品吗?', confirmBtn: '确定', cancelBtn: '取消' })
      .then(() => {
        this.deleteGoodsService({ spuId, skuId }).then(() => {
          Toast({ context: this, selector: '#t-toast', message: '已删除' });
          this.refreshData();
        });
      });
  },

  onSelectAll() {
    Toast({ context: this, selector: '#t-toast', message: '全选(本地示例)' });
  },

  onToSettle() {
    const items = getCartItems();
    wx.setStorageSync('order.goodsRequestList', JSON.stringify(items.map(i => ({
      goodsName: i.title,
      spuId: i.spuId,
      skuId: i.skuId,
      quantity: i.quantity,
      price: i.price,
      primaryImage: i.primaryImage,
      specInfo: i.specInfo,
      available: i.available,
      storeId: i.storeId,
    }))));
    wx.navigateTo({ url: '/pages/order/order-confirm/index?type=cart' });
  },

  goGoodsDetail(e) {
    const { spuId } = e.detail.goods;
    wx.navigateTo({ url: `/pages/goods/details/index?spuId=${spuId}` });
  },

  onGotoHome() { wx.switchTab({ url: '/pages/home/home' }); },
});
