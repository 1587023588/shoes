import { courses } from '../../../model/courses';
import { addItem } from '../../../services/cart/localCart';

Page({
  data: { course: {}, lesson: {} },
  onLoad(query) {
    const { id } = query;
    const course = courses.find((c) => c.id === id) || courses[0];
    this.setData({ course, lesson: course.lessons[0] });
  },

  playLesson(e) {
    const video = e.currentTarget.dataset.video;
    this.setData({ lesson: { video } });
    const videoContext = wx.createVideoContext('courseVideo', this);
    videoContext.play();
  },

  buyMaterial(e) {
    const sku = JSON.parse(e.currentTarget.dataset.sku);
    addItem({
      spuId: sku.skuId,
      skuId: sku.skuId,
      title: sku.title,
      primaryImage: sku.image,
      quantity: 1,
      price: sku.price,
      linePrice: sku.price,
      specInfo: [],
      available: true,
      putOnSale: 1,
      storeId: '1000',
    });
    wx.showToast({ title: '已加入购物车', icon: 'success' });
  },
});
