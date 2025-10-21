import { courses } from '../../../model/courses';

Page({
  data: { courses: [] },
  onLoad() {
    this.setData({ courses });
  },
  openCourse(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/courses/detail/index?id=${id}` });
  },
});
