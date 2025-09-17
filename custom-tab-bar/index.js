import TabMenu from './data';
Component({
  data: {
    active: 0,
    list: TabMenu,
    cartCount: 0,
  },

  methods: {
    onChange(event) {
      this.setData({ active: event.detail.value });
      wx.switchTab({
        url: this.data.list[event.detail.value].url.startsWith('/')
          ? this.data.list[event.detail.value].url
          : `/${this.data.list[event.detail.value].url}`,
      });
    },

    init() {
      const page = getCurrentPages().pop();
      const route = page ? page.route.split('?')[0] : '';
      const active = this.data.list.findIndex(
        (item) =>
          (item.url.startsWith('/') ? item.url.substr(1) : item.url) ===
          `${route}`,
      );
      this.setData({ active });
      this.refreshCartCount();
    },

    refreshCartCount() {
      try {
        const raw = wx.getStorageSync('cart.items');
        if (!raw) { this.setData({ cartCount: 0 }); return; }
        const arr = JSON.parse(raw) || [];
        const total = arr.reduce((sum, i) => sum + (i.quantity || 0), 0);
        this.setData({ cartCount: total });
      } catch (e) {
        this.setData({ cartCount: 0 });
      }
    },
  },
});
