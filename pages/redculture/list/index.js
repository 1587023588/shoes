import { redArticles } from '../../../model/redculture';

Page({
  data: {
    articles: [],
  },

  onLoad() {
    this.setData({ articles: redArticles });
  },

  openDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/redculture/detail/index?id=${id}` });
  },
});
