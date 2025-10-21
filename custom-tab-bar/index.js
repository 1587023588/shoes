Page({
  data: {
    active: 0, // 默认选中首页
    cartCount: 3, // 购物车数量示例
    list: [
      {
        text: '首页',
        icon: 'home',
        imgUrl: '/static/tabbar/北.png', // 首页图片路径
        pagePath: '/pages/home/home'
      },
      {
        text: '精神谱系',
        icon: 'sort',
        imgUrl: '/static/tabbar/庄.png', // 分类图片路径
        pagePath: '/pages/category/index'
      },
      {
        text: '购物车',
        icon: 'cart',
        imgUrl: '/static/tabbar/布.png', // 购物车图片路径
        pagePath: '/pages/cart/index'
      },
      {
        text: '我的',
        icon: 'user',
        imgUrl: '/static/tabbar/鞋.png', // 我的页面图片路径
        pagePath: '/pages/usercenter/index'
      }
    ]
  },

  // 切换TabBar项时触发
  onChange(e) {
    const index = e.detail.value;
    this.setData({ active: index });
    
    // 跳转到对应页面
    wx.switchTab({
      url: this.data.list[index].pagePath
    });
  },

  // 页面显示时更新选中状态（解决跳转后状态不一致问题）
  onShow() {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];
    const currentPath = currentPage.route;
    
    // 匹配当前页面路径，更新active状态
    this.data.list.forEach((item, index) => {
      if (item.pagePath.includes(currentPath)) {
        this.setData({ active: index });
      }
    });
  }
});
    