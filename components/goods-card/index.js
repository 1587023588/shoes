Component({
  options: {
    addGlobalClass: true,
  },

  properties: {
    id: {
      type: String,
      value: '',
      observer(id) {
        this.genIndependentID(id);
        if (this.properties.thresholds?.length) {
          this.createIntersectionObserverHandle();
        }
      },
    },
    data: {
      type: Object,
      observer(data) {
        if (!data) {
          return;
        }
        let isValidityLinePrice = true;
        if (data.originPrice && data.price && data.originPrice < data.price) {
          isValidityLinePrice = false;
        }
        this.setData({ goods: data, isValidityLinePrice });
        this.updateThumbSrc(data);
      },
    },
    currency: {
      type: String,
      value: '¥',
    },

    thresholds: {
      type: Array,
      value: [],
      observer(thresholds) {
        if (thresholds && thresholds.length) {
          this.createIntersectionObserverHandle();
        } else {
          this.clearIntersectionObserverHandle();
        }
      },
    },
  },

  data: {
    independentID: '',
    goods: { id: '' },
    isValidityLinePrice: false,
    // 计算后的缩略图地址（含兜底）
    thumbSrc: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/1.png',
  },

  lifetimes: {
    ready() {
      this.init();
    },
    detached() {
      this.clear();
    },
  },

  pageLifeTimes: {},

  methods: {
    clickHandle() {
      this.triggerEvent('click', { goods: this.data.goods });
    },

    clickThumbHandle() {
      this.triggerEvent('thumb', { goods: this.data.goods });
    },

    addCartHandle(e) {
      const { id } = e.currentTarget;
      const { id: cardID } = e.currentTarget.dataset;
      this.triggerEvent('add-cart', {
        ...e.detail,
        id,
        cardID,
        goods: this.data.goods,
      });
    },

    genIndependentID(id) {
      let independentID;
      if (id) {
        independentID = id;
      } else {
        independentID = `goods-card-${~~(Math.random() * 10 ** 8)}`;
      }
      this.setData({ independentID });
    },

    init() {
      const { thresholds, id } = this.properties;
      this.genIndependentID(id);
      if (thresholds && thresholds.length) {
        this.createIntersectionObserverHandle();
      }
      // 初始化缩略图
      this.updateThumbSrc(this.data.goods);
    },

    clear() {
      this.clearIntersectionObserverHandle();
    },

    intersectionObserverContext: null,

    createIntersectionObserverHandle() {
      if (this.intersectionObserverContext || !this.data.independentID) {
        return;
      }
      this.intersectionObserverContext = this.createIntersectionObserver({
        thresholds: this.properties.thresholds,
      }).relativeToViewport();

      this.intersectionObserverContext.observe(
        `#${this.data.independentID}`,
        (res) => {
          this.intersectionObserverCB(res);
        },
      );
    },

    intersectionObserverCB() {
      this.triggerEvent('ob', {
        goods: this.data.goods,
        context: this.intersectionObserverContext,
      });
    },

    clearIntersectionObserverHandle() {
      if (this.intersectionObserverContext) {
        try {
          this.intersectionObserverContext.disconnect();
        } catch (e) { }
        this.intersectionObserverContext = null;
      }
    },

    // 根据传入的 goods 计算缩略图地址，提供兜底
    updateThumbSrc(goods = {}) {
      const candidate = goods.thumb || goods.primaryImage || '';
      // 若为空或明显非法，则使用兜底
      const fallback = 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/1.png';
      const localFallback = '/static/tabbar/帆布鞋-copy.png';
      const use = candidate && typeof candidate === 'string' ? candidate : fallback;
      this.setData({ thumbSrc: use, _fallbackTried: false, _localFallback: localFallback });
    },

    onThumbError() {
      // 1) 优先尝试在 jpg/png 之间切换一次
      const { thumbSrc, _triedAltExt } = this.data;
      console.warn('[goods-card] image error, src =', thumbSrc);
      // 如果 URL 上带了处理参数（如 imageMogr2），先尝试去掉参数重试一次
      if (!_triedAltExt && /\?/.test(thumbSrc)) {
        const clean = thumbSrc.split('?')[0];
        if (clean) {
          console.warn('[goods-card] try clean url:', clean);
          this.setData({ thumbSrc: clean, _triedAltExt: true });
          return;
        }
      }
      const isJpg = /\.jpg(\?.*)?$/i.test(thumbSrc);
      const isPng = /\.png(\?.*)?$/i.test(thumbSrc);
      if (!_triedAltExt && (isJpg || isPng)) {
        const alt = isJpg ? thumbSrc.replace(/\.jpg(\?.*)?$/i, '.png$1') : thumbSrc.replace(/\.png(\?.*)?$/i, '.jpg$1');
        if (alt && alt !== thumbSrc) {
          console.warn('[goods-card] try alt ext:', alt);
          this.setData({ thumbSrc: alt, _triedAltExt: true });
          return;
        }
      }

      // 2) 兜底：CDN 小图 -> 本地小图
      const fallback = 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/1.png';
      const localFallback = this.data._localFallback || '/static/tabbar/帆布鞋-copy.png';
      const { _fallbackTried } = this.data;
      if (thumbSrc !== fallback && !_fallbackTried) {
        console.warn('[goods-card] fallback to cdn small:', fallback);
        this.setData({ thumbSrc: fallback, _fallbackTried: true });
      } else if (thumbSrc !== localFallback) {
        console.warn('[goods-card] fallback to local:', localFallback);
        this.setData({ thumbSrc: localFallback });
      }
    },
  },
});
