import { fetchHome } from '../../services/home/home';
import Toast from 'tdesign-miniprogram/toast/index';

Page({
  data: {
  // 页面基础状态
  pageLoading: false,
  loadError: false,
  errorMsg: '',
    // 顶部视频
    videoSrc: '/pages/images/index_video.mp4',
    videoAutoplay: true,
    videoMuted: true,
    videoLoop: true,
    // 英雄图
    heroImage: '/pages/images/test.jpg',
    // 功能宫格（2x4）
    defaultFuncIcon: '/pages/images/test.jpg',
    funcList: [
      { iconUrl: '/pages/images/test.jpg', text: '公告通知', url: '/pages/notice/index' },
      { iconUrl: '/pages/images/test.jpg', text: '本馆介绍', url: '/pages/about/index' },
      { iconUrl: '/pages/images/test.jpg', text: '非遗之窗', url: '/pages/heritage/index' },
      { iconUrl: '/pages/images/test.jpg', text: '培训风采', url: '/pages/training/showcase/index' },
      { iconUrl: '/pages/images/test.jpg', text: '青少年培训', url: '/pages/training/teen/index' },
      { iconUrl: '/pages/images/test.jpg', text: '成人培训', url: '/pages/training/adult/index' },
      { iconUrl: '/pages/images/test.jpg', text: '老年人培训', url: '/pages/training/senior/index' },
      { iconUrl: '/pages/images/test.jpg', text: '文艺团队招募', url: '/pages/recruit/index' },
    ],
    // 资讯列表
    newsList: [
      { id: 1, title: '国庆假期开馆公告', desc: '盛世华诞，举国同庆。南昌市文化馆国庆期间（10月1日——7...）', cover: '/pages/images/test.jpg', tag: '公告通知' },
      { id: 2, title: '文化馆通知', desc: '关于加强“48小时常态化核酸检测”场所码联动查验通知...', cover: '/pages/images/test.jpg', tag: '公告通知' },
    ],
  },

  // 兼容保留：如后续有分页需求可在此扩展
  privateData: {},

  onShow() {
    const bar = this.getTabBar && this.getTabBar();
    if (bar && bar.init) { bar.init(); }
  },

  onLoad() {
    this.init();
    // 若需从服务端拉取英雄图/宫格/资讯，可在此请求
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

  /** 若后续需要本地初始化可在此扩展 */
  bootstrapLocalModules() {},

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
        // 可在此用远端数据填充 heroImage/funcList/newsList
        this.setData({ pageLoading: false });
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

  // 资讯点击
  onNewsTap(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({ url: `/pages/notice/detail/index?id=${id}` });
  },
  // 宫格点击
  onFuncTap(e) {
    const { index } = e.currentTarget.dataset;
    const item = this.data.funcList[index];
    if (!item) return;
    if (item.url) {
      wx.navigateTo({ url: item.url });
    } else {
      Toast({ context: this, selector: '#t-toast', message: item.text });
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

  onVideoError(e) {
    console.error('[home] video error:', e && e.detail);
    // 出错时回退为不自动播放，避免反复报错（可选）
    this.setData({ videoAutoplay: false });
    // 如果资源不支持，给出提示
    Toast({ context: this, selector: '#t-toast', message: '视频加载失败，稍后重试' });
  },
});
