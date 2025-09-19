Page({
  data: {
    item: {},
    name: '',
    idCard: '',
    phone: '',
    peopleIndex: 0,
    peopleCount: 1
  },

  onLoad(options) {
    // 确保参数正确解析
    if (options && options.item) {
      try {
        this.setData({
          item: JSON.parse(decodeURIComponent(options.item))
        });
      } catch (e) {
        console.error('解析参数失败:', e);
        wx.showToast({ title: '数据加载失败', icon: 'none' });
        setTimeout(() => wx.navigateBack(), 1500);
      }
    } else {
      wx.showToast({ title: '缺少参数', icon: 'none' });
      setTimeout(() => wx.navigateBack(), 1500);
    }
  },

  // 返回上一页
  onNavigateBack() {
    wx.navigateBack({
      delta: 1
    });
  },

  onNameChange(e) {
    this.setData({ name: e.detail.value });
  },

  onIdCardChange(e) {
    this.setData({ idCard: e.detail.value });
  },

  onPhoneChange(e) {
    this.setData({ phone: e.detail.value });
  },

  onPeopleChange(e) {
    const index = e.detail.value;
    this.setData({
      peopleIndex: index,
      peopleCount: index + 1
    });
  },

  submitReservation() {
    const { name, idCard, phone, item, peopleCount } = this.data;

    // 简单校验
    if (!name.trim()) {
      return wx.showToast({ title: '请输入姓名', icon: 'none' });
    }
    
    if (!/^\d{17}[\dXx]$/.test(idCard)) {
      return wx.showToast({ title: '请输入正确身份证号', icon: 'none' });
    }
    
    if (!/^1[3-9]\d{9}$/.test(phone)) {
      return wx.showToast({ title: '请输入正确手机号', icon: 'none' });
    }

    // 提交预约（实际项目中替换为真实接口）
    wx.showLoading({ title: '提交中...' });
    setTimeout(() => {
      wx.hideLoading();
      wx.showToast({ 
        title: '预约成功！凭身份证入场', 
        icon: 'success',
        duration: 2000
      });
      setTimeout(() => wx.navigateBack(), 2000);
    }, 1000);
  }
})
    