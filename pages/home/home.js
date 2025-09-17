import { fetchHome } from '../../services/home/home';
import { fetchGoodsList } from '../../services/good/fetchGoods';
import Toast from 'tdesign-miniprogram/toast/index';
import { getRandomSlogan, brandCTA } from '../../config/brand';

Page({
  data: {
    imgSrcs: [],
    tabList: [],
    goodsList: [],
    goodsListLoadStatus: 0,
    pageLoading: false,
    loadError: false,
    errorMsg: '',
    current: 1,
    autoplay: true,
    duration: '500',
    interval: 5000,
    navigation: { type: 'dots' },
    swiperImageProps: { mode: 'scaleToFill' },
    slogan: '',
    ctaText: brandCTA,
    // 新增模块数据
    quickNavList: [],
    brandStory: null,
    craftSteps: [],
    projectHighlight: null,
    recommendSlice: [],
  },

  goodListPagination: {
    index: 0,
    num: 20,
  },

  privateData: {
    tabIndex: 0,
  },

  onShow() {
    const bar = this.getTabBar && this.getTabBar();
    if (bar && bar.init) { bar.init(); }
  },

  onLoad() {
    this.init();
    this.setData({
      slogan: getRandomSlogan(),
    });
    this.bootstrapLocalModules();
  },

  onReachBottom() {
    if (this.data.goodsListLoadStatus === 0) {
      this.loadGoodsList();
    }
  },

  onPullDownRefresh() {
    this.init();
    this.setData({
      slogan: getRandomSlogan(),
    });
  },

  init() {
    this.loadHomePage();
  },

  /** 本地静态模块初始化 */
  bootstrapLocalModules() {
    this.setData({
      quickNavList: [
  { icon: '/pages/images/test.jpg', text: '新品' },
  { icon: '/pages/images/test.jpg', text: '热卖' },
  { icon: '/pages/images/test.jpg', text: '助农' },
  { icon: '/pages/images/test.jpg', text: '童鞋' },
  { icon: '/pages/images/test.jpg', text: '养生' },
      ],
      brandStory: {
        title: '百年布鞋 · 乡土匠心',
        desc: '源自北方乡村的手工布鞋，坚持棉麻天然材质与千层纳底工法，每一针脚都承载乡亲的朴实生活与新农村振兴的希望。',
  image: '/pages/images/test.jpg',
      },
      craftSteps: [
        { step: '选布', desc: '甄选本地棉麻' },
        { step: '裁片', desc: '手工裁剪版型' },
        { step: '纳底', desc: '千层棉线加固' },
        { step: '成型', desc: '贴合脚型走线' },
        { step: '晾晒', desc: '自然风干收型' },
      ],
      projectHighlight: {
        title: '助农计划',
        sub: '每卖出一双布鞋 · 返助乡村种植基金',
        stat: { value: 3287, unit: '双已助力' },
  image: '/pages/images/test.jpg',
      },
      recommendSlice: [
  { title: '四季透气款', tag: '热卖', cover: '/pages/images/test.jpg' },
  { title: '加绒养生鞋', tag: '保暖', cover: '/pages/images/test.jpg' },
  { title: '儿童软底', tag: '童款', cover: '/pages/images/test.jpg' },
      ],
    });
  },

  loadHomePage() {
    wx.stopPullDownRefresh();
    // 生成本次请求的序号用于避免竞态
    this._homeReqId = (this._homeReqId || 0) + 1;
    const reqId = this._homeReqId;

    this.setData({
      pageLoading: true,
      loadError: false,
      errorMsg: '',
    });

    const TIMEOUT = 6000; // 6s 超时兜底
    let timeoutHandle;
    const timeoutPromise = new Promise((_, reject) => {
      timeoutHandle = setTimeout(() => reject(new Error('HOME_TIMEOUT')), TIMEOUT);
    });

    Promise.race([fetchHome(), timeoutPromise])
      .then((res) => {
        if (reqId !== this._homeReqId) return; // 过期结果忽略
        const { swiper = [], tabList = [] } = (res && typeof res === 'object') ? res : {};
        this.setData({
          tabList,
          imgSrcs: swiper,
          pageLoading: false,
        });
        this.loadGoodsList(true);
      })
      .catch((err) => {
        if (reqId !== this._homeReqId) return;
        console.error('[home] fetchHome failed:', err);
        const isTimeout = err && err.message === 'HOME_TIMEOUT';
        this.setData({
          pageLoading: false,
          loadError: true,
          errorMsg: isTimeout ? '首页加载超时，请重试' : '首页加载失败',
        });
      })
      .finally(() => {
        clearTimeout(timeoutHandle);
        // 兜底：若仍处于 loading（理论不该发生），强制关闭
        if (this.data.pageLoading && !this.data.loadError) {
          this.setData({ pageLoading: false });
        }
      });
  },

  tabChangeHandle(e) {
    if (!this.privateData) { this.privateData = { tabIndex: 0 }; }
    const nextIndex = e && typeof e.detail === 'number' ? e.detail : 0;
    this.privateData.tabIndex = nextIndex;
    this.loadGoodsList(true);
  },

  onReTry() {
    this.loadGoodsList();
  },

  async loadGoodsList(fresh = false) {
    if (fresh) {
      wx.pageScrollTo({
        scrollTop: 0,
      });
    }

    this.setData({ goodsListLoadStatus: 1 });

    const pageSize = this.goodListPagination.num;
    let pageIndex = this.privateData.tabIndex * pageSize + this.goodListPagination.index + 1;
    if (fresh) {
      pageIndex = 0;
    }

    try {
      const nextList = await fetchGoodsList(pageIndex, pageSize);
      this.setData({
        goodsList: fresh ? nextList : this.data.goodsList.concat(nextList),
        goodsListLoadStatus: 0,
      });

      this.goodListPagination.index = pageIndex;
      this.goodListPagination.num = pageSize;
    } catch (err) {
      this.setData({ goodsListLoadStatus: 3 });
    }
  },

  goodListClickHandle(e) {
    const { index } = e.detail;
    const { spuId } = this.data.goodsList[index];
    wx.navigateTo({
      url: `/pages/goods/details/index?spuId=${spuId}`,
    });
  },

  goodListAddCartHandle() {
    Toast({
      context: this,
      selector: '#t-toast',
      message: '加入布鞋篮（示例）',
    });
  },

  navToSearchPage() {
    wx.navigateTo({ url: '/pages/goods/search/index' });
  },

  navToActivityDetail({ detail }) {
    const { index: promotionID = 0 } = detail || {};
    wx.navigateTo({
      url: `/pages/promotion/promotion-detail/index?promotion_id=${promotionID}`,
    });
  },

  supportRural() {
    Toast({
      context: this,
      selector: '#t-toast',
      message: '感谢支持乡村振兴！',
    });
  },
});
