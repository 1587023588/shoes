import Toast from 'tdesign-miniprogram/toast/index';
import { getPromotion } from '../../model/promotion';
import { addItem, getCartItems } from '../../services/cart/localCart';

Page({
  data: {
    goodsList: [],
    filteredList: [],
    categories: [],
    activeCat: '全部',
    cartCount: 0,
  },

  onShow() {
    this.getTabBar().init?.();
    this.refreshCartCount();
  },

  onLoad() {
    // 使用本地模型生成的布鞋商品作为选购列表
    const promo = getPromotion(0, 20);
    const goodsList = promo.list;
    const categories = Array.from(new Set(goodsList.flatMap(g => g.tags || [])));
    this.setData({ goodsList, filteredList: goodsList, categories, activeCat: '全部' });
    this.refreshCartCount();
  },

  refreshCartCount() {
    const count = (getCartItems() || []).reduce((sum, i) => sum + (i.quantity || 0), 0);
    this.setData({ cartCount: count });
  },

  onGoodsCardClick(e) {
    const { index } = e.detail;
    const item = this.data.goodsList[index];
    if (!item) return;
    wx.navigateTo({ url: `/pages/goods/details/index?spuId=${item.spuId}` });
  },

  onGoodsAddCart(e) {
    const { index } = e.detail;
    const item = this.data.goodsList[index];
    if (!item) return;
    // 简化：默认添加一个基础 sku（用 spuId 结合一个默认 skuId）
    const skuId = `${item.spuId}-default`;
    addItem({
      spuId: item.spuId,
      skuId,
      title: item.title,
      primaryImage: item.thumb,
      quantity: 1,
      price: parseInt(item.price, 10) || 0,
      linePrice: parseInt(item.originPrice, 10) || undefined,
      specInfo: [],
      available: 1,
      storeId: '1000',
    });
    Toast({ context: this, selector: '#t-toast', message: '已加入购物车' });
    this.refreshCartCount();
  },

  onCatTap(e) {
    const cat = e.currentTarget.dataset.cat;
    const { goodsList } = this.data;
    const filtered = cat === '全部' ? goodsList : goodsList.filter(g => (g.tags || []).includes(cat));
    this.setData({ activeCat: cat, filteredList: filtered });
  },

  onToSettle() {
    // 结算前写入结算所需结构（与原购物车一致）
    const items = getCartItems();
    const payload = items.map((i) => ({
      goodsName: i.title,
      spuId: i.spuId,
      skuId: i.skuId,
      quantity: i.quantity,
      price: i.price,
      primaryImage: i.primaryImage,
      specInfo: i.specInfo,
      available: i.available,
      storeId: i.storeId,
    }));
    wx.setStorageSync('order.goodsRequestList', JSON.stringify(payload));
    wx.navigateTo({ url: '/pages/order/order-confirm/index?type=cart' });
  },
});
