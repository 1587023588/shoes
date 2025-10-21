Page({
  data: {
    // 与页面同目录，使用相对路径更稳妥
    videoUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/video_yongjun.mp4',
      posterUrl: '/pages/images/test.jpg',
    isLoading: true,
    isError: false,
    errorMsg: ''
  },

  onLoad() {
    this.videoContext = wx.createVideoContext('video-player');
  },

  onVideoLoaded() {
    this.setData({ isLoading: false });
  },

  onVideoError(e) {
    const errMsg = (e && e.detail && e.detail.errMsg) || '未知错误';
    console.error('视频加载错误：', errMsg, e && e.detail);
    this.setData({ isLoading: false, isError: true, errorMsg: `视频加载失败：${errMsg}` });
  },

  reloadVideo() {
    this.setData({ isLoading: true, isError: false });
    // 触发重新加载
    if (this.videoContext) {
      this.videoContext.stop && this.videoContext.stop();
      this.videoContext.play();
    }
  },

  onUnload() {
    if (this.videoContext) {
      this.videoContext.pause();
    }
  }
});
