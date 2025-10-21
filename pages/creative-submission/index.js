Page({
  data: {
    spuId: '',
    images: [],
    description: '',
    submissions: [],
  },

  onLoad(query) {
    if (query && query.spuId) {
      this.setData({ spuId: query.spuId });
    }
    this.loadMySubmissions();
  },

  chooseImage() {
    const that = this;
    wx.chooseImage({
      count: 4,
      success(res) {
        that.setData({ images: that.data.images.concat(res.tempFilePaths) });
      },
    });
  },

  removeImage(e) {
    const idx = e.currentTarget.dataset.index;
    const arr = this.data.images.slice();
    arr.splice(idx, 1);
    this.setData({ images: arr });
  },

  onDescriptionInput(e) {
    this.setData({ description: e.detail.value });
  },

  async submit() {
    const { spuId, images, description } = this.data;
    if (!images.length) {
      wx.showToast({ title: '请添加设计图片', icon: 'none' });
      return;
    }
    const payload = {
      id: `s_${Date.now()}`,
      spuId,
      images,
      description,
      status: 'pending',
      createdAt: Date.now(),
    };
    try {
      // Dynamically require submission service to avoid runtime module resolution errors
      let saveFn = null;
      try {
        // eslint-disable-next-line global-require
        const svc = require('../../services/submission');
        saveFn = (svc && svc.uploadAndSaveSubmission && typeof svc.uploadAndSaveSubmission === 'function')
          ? svc.uploadAndSaveSubmission
          : (svc && svc.saveSubmission && typeof svc.saveSubmission === 'function')
            ? svc.saveSubmission
            : null;
      } catch (e) {
        console.warn('require submission service failed in submit:', e);
      }
      if (saveFn) {
        await saveFn(payload);
      } else {
        // fallback to local storage via on-the-fly implementation
        const { getLocalModel, saveLocalModel } = require('../../model/localModel');
        const KEY = 'submissions';
        const list = getLocalModel(KEY) || [];
        list.unshift(payload);
        saveLocalModel(KEY, list);
      }

      wx.showToast({ title: '投稿成功', icon: 'success' });
      this.setData({ images: [], description: '' });
      this.loadMySubmissions();
    } catch (err) {
      wx.showToast({ title: '投稿失败', icon: 'none' });
      console.error(err);
    }
  },

  async loadMySubmissions() {
    try {
      let fetchFn = null;
      try {
        // eslint-disable-next-line global-require
        const svc = require('../../services/submission');
        fetchFn = svc && svc.fetchSubmissionsByUser;
      } catch (e) {
        console.warn('require submission service failed in loadMySubmissions:', e);
      }
      if (typeof fetchFn === 'function') {
        const subs = await fetchFn();
        this.setData({ submissions: subs || [] });
      } else {
        const { getLocalModel } = require('../../model/localModel');
        const subs = getLocalModel('submissions') || [];
        this.setData({ submissions: subs });
      }
    } catch (err) {
      console.error('loadMySubmissions error:', err);
      this.setData({ submissions: [] });
    }
  },
});
