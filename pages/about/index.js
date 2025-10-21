Page({
  data: {
    // 视频地址：更新为新的北庄转场视频地址
    videoUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/8%E6%9C%8812%E6%97%A5%20%E5%8C%97%E5%BA%84%E8%BD%AC%E5%9C%BA%EF%BC%88%E6%94%B9%EF%BC%89.mp4',
    // 视频封面图：建议后续替换为新视频对应的封面图
    posterUrl: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E5%8C%97%E5%BA%84%E5%B9%BF%E5%91%8A%E7%89%87%E5%B0%81%E9%9D%A2.jpg',
    isLoading: true, // 是否处于加载中
    isError: false, // 是否加载错误
    errorMsg: '' // 错误提示信息
  },

  // 页面加载时初始化视频
  onLoad() {
    // 监听视频加载完成事件（需通过wx.createVideoContext实现）
    this.videoContext = wx.createVideoContext('video-player'); // 与wxml中video的id对应
  },

  // 视频加载完成（通过video组件的bindloadedmetadata事件触发）
  onVideoLoaded() {
    this.setData({ isLoading: false });
  },

  // 视频加载错误（通过video组件的binderror事件触发）
  onVideoError(e) {
    console.error('视频加载错误：', e.detail);
    this.setData({
      isLoading: false,
      isError: true,
      errorMsg: '宣传片加载失败，请检查网络或稍后重试'
    });
  },

  // 重新加载视频
  reloadVideo() {
    this.setData({ isLoading: true, isError: false });
    // 重新加载视频（通过视频上下文重新播放）
    this.videoContext.src = this.data.videoUrl; // 重置视频地址触发重新加载
    this.videoContext.play(); // 加载完成后自动播放
  },

  // 页面卸载时停止视频播放（避免后台播放浪费流量）
  onUnload() {
    if (this.videoContext) {
      this.videoContext.pause(); // 暂停视频
    }
  }
});