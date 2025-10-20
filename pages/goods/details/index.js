import Toast from 'tdesign-miniprogram/toast/index';
import { fetchGood } from '../../../services/good/fetchGood';
import { fetchActivityList } from '../../../services/activity/fetchActivityList';
import {
  getGoodsDetailsCommentList,
  getGoodsDetailsCommentsCount,
} from '../../../services/good/fetchGoodsDetailsComments';
import { addItem, getCartItems } from '../../../services/cart/localCart';

import { cdnBase } from '../../../config/index';

const imgPrefix = `${cdnBase}/`;

const recLeftImg = `${imgPrefix}common/rec-left.png`;
const recRightImg = `${imgPrefix}common/rec-right.png`;
const obj2Params = (obj = {}, encode = false) => {
  const result = [];
  Object.keys(obj).forEach((key) => result.push(`${key}=${encode ? encodeURIComponent(obj[key]) : obj[key]}`));

  return result.join('&');
};

Page({
  data: {
    commentsList: [],
    commentsStatistics: {
      badCount: 0,
      commentCount: 0,
      goodCount: 0,
      goodRate: 0,
      hasImageCount: 0,
      middleCount: 0,
    },
    isShowPromotionPop: false,
    activityList: [],
    recLeftImg,
    recRightImg,
    // Provide safe default structure to avoid template errors when data is loading
    details: {
      spuId: '',
      title: '',
      images: [],
      desc: [],
      specList: [],
      skuList: [],
      primaryImage: '',
      intro: '',
      minSalePrice: 0,
      maxLinePrice: 0,
      spuStockQuantity: 0,
      available: true,
      isPutOnSale: 1,
    },
    goodsTabArray: [
      {
        name: '商品',
        value: '', // 空字符串代表置顶
      },
      {
        name: '详情',
        value: 'goods-page',
      },
    ],
    storeLogo: `${imgPrefix}common/store-logo.png`,
    storeName: '北庄布鞋旗舰店',
    jumpArray: [
      {
        title: '首页',
        url: '/pages/home/home',
        iconName: 'home',
      },
      {
        title: '购物车',
        url: '/pages/cart/index',
        iconName: 'cart',
        showCartNum: true,
      },
    ],
    isStock: true,
    cartNum: 0,
    soldout: false,
    buttonType: 1,
    buyNum: 1,
    selectedAttrStr: '',
    skuArray: [],
    primaryImage: '',
    specImg: '',
    isSpuSelectPopupShow: false,
    isAllSelectedSku: false,
    buyType: 0,
    outOperateStatus: false, // 是否外层加入购物车
    operateType: 0,
    selectSkuSellsPrice: 0,
    maxLinePrice: 0,
    minSalePrice: 0,
    maxSalePrice: 0,
    list: [],
    spuId: '',
    navigation: { type: 'fraction' },
    current: 0,
    autoplay: true,
    duration: 500,
    interval: 5000,
    soldNum: 0, // 已售数量
    // 简介：从详情数据派生，便于在模板中使用 {{intro}}
    intro: '',
    // 详情图加载失败时的兜底图
    descFallback: '/test.jpg',
    submissions: [],
    // debug helpers
    debugStep: '',
    debugMsg: '',
    debugError: '',
  },

  handlePopupHide() {
    this.setData({
      isSpuSelectPopupShow: false,
    });
  },

  // 详情图加载失败兜底：将对应项替换为 fallback，避免空白
  onDescImageError(e) {
    const { index } = e.currentTarget.dataset;
    const { details, descFallback } = this.data;
    const desc = (details.desc || []).slice();
    if (typeof index === 'number' && desc[index] !== descFallback) {
      desc[index] = descFallback;
      this.setData({ 'details.desc': desc });
    }
  },

  showSkuSelectPopup(type) {
    this.setData({
      buyType: type || 0,
      outOperateStatus: type >= 1,
      isSpuSelectPopupShow: true,
    });
  },

  buyItNow() {
    this.showSkuSelectPopup(1);
  },

  toAddCart() {
    this.showSkuSelectPopup(2);
  },

  toNav(e) {
    const { url } = e.detail;
    wx.switchTab({
      url: url,
    });
  },

  showCurImg(e) {
    const { index } = e.detail;
    const { images } = this.data.details;
    wx.previewImage({
      current: images[index],
      urls: images, // 需要预览的图片http链接列表
    });
  },

  onPageScroll({ scrollTop }) {
    const goodsTab = this.selectComponent('#goodsTab');
    goodsTab && goodsTab.onScroll(scrollTop);
  },

  chooseSpecItem(e) {
    const { specList } = this.data.details;
    const { selectedSku, isAllSelectedSku } = e.detail;
    if (!isAllSelectedSku) {
      this.setData({
        selectSkuSellsPrice: 0,
      });
    }
    this.setData({
      isAllSelectedSku,
    });
    this.getSkuItem(specList, selectedSku);
  },

  getSkuItem(specList, selectedSku) {
    const { skuArray, primaryImage } = this.data;
    const selectedSkuValues = this.getSelectedSkuValues(specList, selectedSku);
    let selectedAttrStr = ` 件  `;
    selectedSkuValues.forEach((item) => {
      selectedAttrStr += `，${item.specValue}  `;
    });
    // eslint-disable-next-line array-callback-return
    const skuItem = skuArray.filter((item) => {
      let status = true;
      (item.specInfo || []).forEach((subItem) => {
        if (!selectedSku[subItem.specId] || selectedSku[subItem.specId] !== subItem.specValueId) {
          status = false;
        }
      });
      if (status) return item;
    });
    this.selectSpecsName(selectedSkuValues.length > 0 ? selectedAttrStr : '');
    if (skuItem) {
      this.setData({
        selectItem: skuItem,
        selectSkuSellsPrice: skuItem.price || 0,
      });
    } else {
      this.setData({
        selectItem: null,
        selectSkuSellsPrice: 0,
      });
    }
    this.setData({
      specImg: skuItem && skuItem.skuImage ? skuItem.skuImage : primaryImage,
    });
  },

  // 获取已选择的sku名称
  getSelectedSkuValues(skuTree, selectedSku) {
    const normalizedTree = this.normalizeSkuTree(skuTree);
    return Object.keys(selectedSku).reduce((selectedValues, skuKeyStr) => {
      const skuValues = normalizedTree[skuKeyStr];
      const skuValueId = selectedSku[skuKeyStr];
      if (skuValueId !== '') {
        const skuValue = skuValues.filter((value) => {
          return value.specValueId === skuValueId;
        })[0];
        skuValue && selectedValues.push(skuValue);
      }
      return selectedValues;
    }, []);
  },

  normalizeSkuTree(skuTree) {
    const normalizedTree = {};
    skuTree.forEach((treeItem) => {
      normalizedTree[treeItem.specId] = treeItem.specValueList;
    });
    return normalizedTree;
  },

  selectSpecsName(selectSpecsName) {
    if (selectSpecsName) {
      this.setData({
        selectedAttrStr: selectSpecsName,
      });
    } else {
      this.setData({
        selectedAttrStr: '',
      });
    }
  },

  addCart() {
    const { isAllSelectedSku, details, selectItem, buyNum } = this.data;
    // 如果存在规格但未全部选择，提示
    if (details.specList && details.specList.length > 0 && !isAllSelectedSku) {
      Toast({ context: this, selector: '#t-toast', message: '请选择尺码', duration: 1200 });
      return;
    }
    const sku = selectItem || (details.skuList ? details.skuList[0] : null);
    const priceInfo = (sku && sku.priceInfo) || [];
    const sale = priceInfo.find((p) => p.priceType === 1);
    const line = priceInfo.find((p) => p.priceType === 2);
    const price = sale ? parseInt(sale.price) : parseInt(details.minSalePrice) || 0;
    const linePrice = line ? parseInt(line.price) : parseInt(details.maxLinePrice) || price;
    const specInfo = sku && sku.specInfo ? sku.specInfo.map((s) => ({ specTitle: s.specTitle || '尺码', specValue: s.specValue })) : [];

    addItem({
      spuId: details.spuId,
      skuId: sku ? sku.skuId : `${details.spuId}-default`,
      title: details.title,
      primaryImage: details.primaryImage,
      quantity: buyNum || 1,
      price,
      linePrice,
      specInfo,
      available: details.available,
      putOnSale: details.isPutOnSale,
      storeId: details.storeId || '1000',
    });

    // 刷新 tabBar 角标
    const tabBar = this.getTabBar && this.getTabBar();
    if (tabBar && typeof tabBar.refreshCartCount === 'function') {
      tabBar.refreshCartCount();
    }

    Toast({ context: this, selector: '#t-toast', message: '已加入购物车', icon: 'check', duration: 600 });

    // 自动跳转到购物车（可根据需要改为延迟或配置）
    setTimeout(() => {
      wx.switchTab({ url: '/pages/cart/index' });
    }, 500);
  },

  gotoBuy(type) {
    const { isAllSelectedSku, buyNum } = this.data;
    if (!isAllSelectedSku) {
      Toast({
        context: this,
        selector: '#t-toast',
        message: '请选择规格',
        icon: '',
        duration: 1000,
      });
      return;
    }
    this.handlePopupHide();
    const query = {
      quantity: buyNum,
      storeId: '1',
      spuId: this.data.spuId,
      goodsName: this.data.details.title,
      skuId: type === 1 ? this.data.skuList[0].skuId : this.data.selectItem.skuId,
      available: this.data.details.available,
      price: this.data.details.minSalePrice,
      specInfo: this.data.details.specList?.map((item) => ({
        specTitle: item.title,
        specValue: item.specValueList[0].specValue,
      })),
      primaryImage: this.data.details.primaryImage,
      spuId: this.data.details.spuId,
      thumb: this.data.details.primaryImage,
      title: this.data.details.title,
    };
    let urlQueryStr = obj2Params({
      goodsRequestList: JSON.stringify([query]),
    });
    urlQueryStr = urlQueryStr ? `?${urlQueryStr}` : '';
    const path = `/pages/order/order-confirm/index${urlQueryStr}`;
    wx.navigateTo({
      url: path,
    });
  },

  specsConfirm() {
    const { buyType } = this.data;
    if (buyType === 1) {
      this.gotoBuy();
    } else {
      this.addCart();
    }
    // this.handlePopupHide();
  },

  changeNum(e) {
    this.setData({
      buyNum: e.detail.buyNum,
    });
  },

  closePromotionPopup() {
    this.setData({
      isShowPromotionPop: false,
    });
  },

  promotionChange(e) {
    const { index } = e.detail;
    wx.navigateTo({
      url: `/pages/promotion/promotion-detail/index?promotion_id=${index}`,
    });
  },

  showPromotionPopup() {
    this.setData({
      isShowPromotionPop: true,
    });
  },

  getDetail(spuId) {
    this.setData({ debugStep: 'fetching detail', debugMsg: `spuId=${spuId}` });
    console.log('[detail] start fetch', spuId);
    Promise.all([fetchGood(spuId), fetchActivityList()]).then((res) => {
      const [details, activityList] = res;
      console.log('[detail] fetch success', details);
      this.setData({ debugStep: 'fetch success', debugMsg: `got details for ${details.spuId}` });
      const skuArray = [];
      const { skuList, primaryImage, isPutOnSale, minSalePrice, maxSalePrice, maxLinePrice, soldNum } = details;
      skuList.forEach((item) => {
        skuArray.push({
          skuId: item.skuId,
          quantity: item.stockInfo ? item.stockInfo.stockQuantity : 0,
          specInfo: item.specInfo,
        });
      });
      const promotionArray = [];
      activityList.forEach((item) => {
        promotionArray.push({
          tag: item.promotionSubCode === 'MYJ' ? '满减' : '满折',
          label: '满100元减99.9元',
        });
      });
      this.setData({
        details,
        activityList,
        isStock: details.spuStockQuantity > 0,
        maxSalePrice: maxSalePrice ? parseInt(maxSalePrice) : 0,
        maxLinePrice: maxLinePrice ? parseInt(maxLinePrice) : 0,
        minSalePrice: minSalePrice ? parseInt(minSalePrice) : 0,
        list: promotionArray,
        skuArray: skuArray,
        primaryImage,
        soldout: isPutOnSale === 0,
        soldNum,
        // 将简介写入 data，优先用详情里的 intro；没有则回落为标题
        intro: details.intro || details.title || '',
      });
      // load related submissions (dynamic require to avoid runtime module-not-found crash)
      try {
        // eslint-disable-next-line global-require
        const { fetchSubmissionsBySpuId } = require('../../../services/submission');
        if (typeof fetchSubmissionsBySpuId === 'function') {
          fetchSubmissionsBySpuId(details.spuId).then((subs) => {
            this.setData({ submissions: subs || [] });
          }).catch((err) => {
            console.warn('fetchSubmissionsBySpuId failed:', err);
            this.setData({ submissions: [] });
          });
        } else {
          this.setData({ submissions: [] });
        }
      } catch (err) {
        console.warn('require services/submission failed:', err);
        this.setData({ submissions: [] });
      }
    }).catch((err) => {
      console.error('[detail] fetch error', err);
      this.setData({ debugStep: 'fetch error', debugError: String(err && err.message ? err.message : err) });
    });
  },

  async getCommentsList() {
    try {
      const code = 'Success';
      const data = await getGoodsDetailsCommentList();
      const { homePageComments } = data;
      if (code.toUpperCase() === 'SUCCESS') {
        const nextState = {
          commentsList: homePageComments.map((item) => {
            return {
              goodsSpu: item.spuId,
              userName: item.userName || '',
              commentScore: item.commentScore,
              commentContent: item.commentContent || '用户未填写评价',
              userHeadUrl: item.isAnonymity ? this.anonymityAvatar : item.userHeadUrl || this.anonymityAvatar,
            };
          }),
        };
        this.setData(nextState);
      }
    } catch (error) {
      console.error('comments error:', error);
    }
  },

  onShareAppMessage() {
    // 自定义的返回信息
    const { selectedAttrStr } = this.data;
    let shareSubTitle = '';
    if (selectedAttrStr.indexOf('件') > -1) {
      const count = selectedAttrStr.indexOf('件');
      shareSubTitle = selectedAttrStr.slice(count + 1, selectedAttrStr.length);
    }
    const customInfo = {
      imageUrl: this.data.details.primaryImage,
      title: this.data.details.title + shareSubTitle,
      path: `/pages/goods/details/index?spuId=${this.data.spuId}`,
    };
    return customInfo;
  },

  /** 获取评价统计 */
  async getCommentsStatistics() {
    try {
      const code = 'Success';
      const data = await getGoodsDetailsCommentsCount();
      if (code.toUpperCase() === 'SUCCESS') {
        const { badCount, commentCount, goodCount, goodRate, hasImageCount, middleCount } = data;
        const nextState = {
          commentsStatistics: {
            badCount: parseInt(`${badCount}`),
            commentCount: parseInt(`${commentCount}`),
            goodCount: parseInt(`${goodCount}`),
            /** 后端返回百分比后数据但没有限制位数 */
            goodRate: Math.floor(goodRate * 10) / 10,
            hasImageCount: parseInt(`${hasImageCount}`),
            middleCount: parseInt(`${middleCount}`),
          },
        };
        this.setData(nextState);
      }
    } catch (error) {
      console.error('comments statiistics error:', error);
    }
  },

  /** 跳转到评价列表 */
  navToCommentsListPage() {
    wx.navigateTo({
      url: `/pages/goods/comments/index?spuId=${this.data.spuId}`,
    });
  },

  onLoad(query) {
    let { spuId, skuId } = query || {};
    // 如果未传 spuId，但传了 skuId，允许兼容处理（mock fetchGood 支持 skuId->spu 映射）
    if (!spuId && skuId) {
      spuId = skuId;
    }
    if (!spuId) {
      Toast({ context: this, selector: '#t-toast', message: '商品参数缺失，无法打开详情' });
      // 防止空白页，稍后返回上一页
      setTimeout(() => wx.navigateBack(), 800);
      return;
    }
    this.setData({ spuId });
    this.getDetail(spuId);
    this.getCommentsList(spuId);
    this.getCommentsStatistics(spuId);
  },

  toSubmissionPage() {
    const { spuId } = this.data;
    wx.navigateTo({ url: `/pages/creative-submission/index?spuId=${spuId}` });
  },
});
