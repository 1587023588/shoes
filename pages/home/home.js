import { fetchHome } from '../../services/home/home';
import Toast from 'tdesign-miniprogram/toast/index';

Page({
  data: {
    // 页面基础状态
    pageLoading: false,
    loadError: false,
    errorMsg: '',
    // 顶部视频（HTTPS 远程资源）
    // 注意：需确保该域名已纳入小程序「downloadFile 合法域名」白名单
    // 与 WXML 中保持一致，默认使用已存在的 COS 资源（文件名含中文，已 URL 编码）
    videoSrc: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/9%E6%9C%8820%E6%97%A5.mp4',
    videoAutoplay: true,
    videoMuted: true,
    videoLoop: true,
    // 英雄图（来自用户提供的 CDN）
    heroImage: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/hero.jpg',
    // 功能宫格（2x4）
    funcList: [
      { iconName: 'image', text: '红色底蕴', url: '/pages/category/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/1.png' },
      { iconName: 'info-circle', text: '北庄宣传片', url: '/pages/about/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/2.png' },
      { iconName: 'star', text: '云游村史馆', url: '/pages/museum/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/3.png' },
      { iconName: 'image', text: '在线讲解', url: '/pages/training/online/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/4.png' },
      { iconName: 'usergroup', text: '手工坊介绍', url: '/pages/workshop/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/5.png' },
      { iconName: 'user', text: '非遗之窗', url: '/pages/heritage/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/6.png' },
      { iconName: 'user-circle', text: '拥军记忆', url: '/pages/military/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/7.png' },
      { iconName: 'usergroup-add', text: '布鞋展示', url: '/pages/show/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/8.png' },
      // 将“换装试玩”放在数组末尾，配合 .func-grid .func-item:last-child 的样式使其居中且更显眼
      { iconName: 'skin', text: '换装试玩', url: '/pages/game/editor/index', iconUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/9.png' }
    ],
    // 资讯列表已移除
    showDebug: false, // 开发时设为 true
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
    // 注意：原代码中 getRandomSlogan 未定义，暂时注释避免报错
    // this.setData({ slogan: getRandomSlogan() });
  },

  init() {
    this.loadHomePage();
  },

  /** 若后续需要本地初始化可在此扩展 */
  bootstrapLocalModules() { },

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

  // 宫格点击（修复跳转逻辑）
  onFuncTap(e) {
    const { index } = e.currentTarget.dataset;
    const item = this.data.funcList[index];
    if (!item) return;

    if (item.url) {
      // 定义所有 tabBar 页面路径（与app.json中的tabBar配置保持一致）
      const tabBarPages = [
        '/pages/home/home',
        '/pages/category/index',
        '/pages/cart/index',
        '/pages/usercenter/index'
      ];

      // 判断是否为 tabBar 页面，使用对应的跳转方法
      if (tabBarPages.includes(item.url)) {
        wx.switchTab({ url: item.url });
      } else {
        wx.navigateTo({ url: item.url });
      }
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
    const msg = (e && e.detail && e.detail.errMsg) ? e.detail.errMsg : '视频加载失败，稍后重试';
    Toast({ context: this, selector: '#t-toast', message: msg });
  },
});