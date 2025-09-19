Page({
  data: {
    // 预约数据（展览类型 → 具体展览 → 场次）
    list: [
      {
        id: 1,
        name: '常设展览',
        children: [
          {
            id: 11,
            name: 'xxxx展',
            children: [
              {
                id: 111,
                name: '上午场',
                date: '2024-06-10',
                time: '09:00-12:00',
                quota: 50,
                enrolled: 35,
                thumbnail: '/pages/images/test.jpg',
                location: 'xxxx',
                notice: '需携带身份证',
              },
              {
                id: 112,
                name: '下午场',
                date: '2024-06-10',
                time: '14:00-17:00',
                quota: 50,
                enrolled: 12,
                thumbnail: '/pages/images/test.jpg',
                location: 'xxxx',
                notice: '需携带身份证',
              },
            ],
          },
          {
            id: 12,
            name: 'xxxx展',
            children: [
              {
                id: 121,
                name: '全天场',
                date: '2024-06-10',
                time: '09:00-17:00',
                quota: 100,
                enrolled: 45,
                thumbnail: '/pages/images/test.jpg',
                location: '中馆2层',
                notice: '免费参观',
              },
            ],
          },
        ],
      },
      {
        id: 2,
        name: '临时特展',
        children: [
          {
            id: 21,
            name: 'xxxx特展',
            children: [
              {
                id: 211,
                name: '上午场',
                date: '2024-06-10',
                time: '09:30-12:30',
                quota: 30,
                enrolled: 28,
                thumbnail: '/pages/images/test.jpg',
                location: '西馆2层',
                notice: '需单独购票',
              },
              {
                id: 212,
                name: '下午场',
                date: '2024-06-10',
                time: '13:30-16:30',
                quota: 30,
                enrolled: 15,
                thumbnail: '/pages/images/test.jpg',
                location: '西馆2层',
                notice: '需单独购票',
              },
            ],
          },
        ],
      },
    ],
    // 初始化默认选中项
    activeKey: 0,
    subActiveKey: 0,
  },

  onLoad() {
    // 确保初始数据正确加载
    if (this.data.list && this.data.list.length > 0) {
      this.setData({
        activeKey: 0,
        subActiveKey: 0,
      });
    }
  },

  onShow() {
    // 安全调用自定义 TabBar 的 init
    try {
      const tab = this.getTabBar && this.getTabBar();
      tab && typeof tab.init === 'function' && tab.init();
    } catch (e) {}
  },

  // 处理场次选择
  onChange(e) {
    const { item } = e.detail || {};
    if (!item) return;
    const remain = (item.quota || 0) - (item.enrolled || 0);
    if (remain <= 0) {
      return wx.showToast({ title: '该场次已约满', icon: 'none' });
    }
    // 跳转预约表单页（安全编码参数）
    wx.navigateTo({
      url: `/pages/reservation/form/index?item=${encodeURIComponent(JSON.stringify(item))}`,
    });
  },
});
