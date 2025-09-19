<<<<<<< HEAD
Page({
  data: {
    // 博物馆预约数据（展览类型→具体展览→场次）
    list: [
      {
        id: 1,
        name: "常设展览", // 侧边栏一级分类
        children: [
          {
            id: 11,
            name: "xxxx展", // 顶部标签栏二级分类
            children: [
              { 
                id: 111,
                name: "上午场",
                date: "2024-06-10",
                time: "09:00-12:00",
                quota: 50,
                enrolled: 35,
                thumbnail: "/images/exhibition-ancient.png",
                location: "xxxx",
                notice: "需携带身份证"
              },
              { 
                id: 112,
                name: "下午场",
                date: "2024-06-10",
                time: "14:00-17:00",
                quota: 50,
                enrolled: 12,
                thumbnail: "/images/exhibition-ancient.png",
                location: "xxxx",
                notice: "需携带身份证"
              }
            ]
          },
          {
            id: 12,
            name: "xxxx展",
            children: [
              { 
                id: 121,
                name: "全天场",
                date: "2024-06-10",
                time: "09:00-17:00",
                quota: 100,
                enrolled: 45,
                thumbnail: "/images/exhibition-modern.png",
                location: "中馆2层",
                notice: "免费参观"
              }
            ]
          }
        ]
      },
      {
        id: 2,
        name: "临时特展",
        children: [
          {
            id: 21,
            name: "xxxx特展",
            children: [
              { 
                id: 211,
                name: "上午场",
                date: "2024-06-10",
                time: "09:30-12:30",
                quota: 30,
                enrolled: 28,
                thumbnail: "/images/exhibition-dunhuang.png",
                location: "西馆2层",
                notice: "需单独购票"
              },
              { 
                id: 212,
                name: "下午场",
                date: "2024-06-10",
                time: "13:30-16:30",
                quota: 30,
                enrolled: 15,
                thumbnail: "/images/exhibition-dunhuang.png",
                location: "西馆2层",
                notice: "需单独购票"
              }
            ]
          }
        ]
      }
    ],
    // 初始化默认选中项，避免undefined问题
    activeKey: 0,
    subActiveKey: 0
  },

  onLoad() {
    // 确保初始数据正确加载
    if (this.data.list.length > 0) {
      this.setData({
        activeKey: 0,
        subActiveKey: 0
      });
    }
  },

  // 处理场次选择
  onChange(e) {
    const { item } = e.detail;
    // 检查是否约满
    if (item.quota - item.enrolled <= 0) {
      return wx.showToast({ title: '该场次已约满', icon: 'none' });
    }
    // 跳转预约表单页（安全编码参数）
    wx.navigateTo({
      url: `/pages/reservation/form/index?item=${encodeURIComponent(JSON.stringify(item))}`
    });
  }
=======
import { getCategoryList } from '../../services/good/fetchCategoryList';
Page({
  data: {
    list: [],
  },
  async init() {
    try {
      const result = await getCategoryList();
      this.setData({
        list: result,
      });
    } catch (error) {
      console.error('err:', error);
    }
  },

  onShow() {
    this.getTabBar().init();
  },
  onChange() {
    wx.navigateTo({
      url: '/pages/goods/list/index',
    });
  },
  onLoad() {
    this.init(true);
  },
>>>>>>> 8a4f608c58ff387d6453daf1e3f9e1e5f76f2f7f
});
