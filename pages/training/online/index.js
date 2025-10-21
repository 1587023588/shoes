Page({
  data: {
    // 音频列表 - 请检查并修正这些CDN链接
    audios: [
      { 
        url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E6%83%85%E5%86%B5%E4%BB%8B%E7%BB%8D%E5%BA%8F%E5%8E%85.MP3', 
        title: '红色基因传承讲解',
        loaded: false // 新增：标记是否已加载
      },
      { 
        url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/123456.MP3', 
        title: '非遗布鞋工艺讲解',
        loaded: false
      },
      { 
        url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E7%BB%93%E8%AF%AD.MP3', 
        title: '村史馆展品讲解',
        loaded: false
      }
    ],
    currentIndex: -1,
    isPlaying: false,
    playTime: "00:00",
    audioCtx: null,
    timer: null
  },

  onLoad() {
    // 初始化音频上下文
    const audioCtx = wx.createInnerAudioContext();
    audioCtx.srcType = 'http';
    
    // 监听音频可以播放事件（新增）
    audioCtx.onCanplay(() => {
      console.log('音频可以播放了');
      // 标记当前音频已加载
      const { currentIndex, audios } = this.data;
      if (currentIndex !== -1) {
        audios[currentIndex].loaded = true;
        this.setData({ audios });
      }
    });
    
    audioCtx.onPlay(() => this.setData({ isPlaying: true }));
    audioCtx.onPause(() => this.setData({ isPlaying: false }));
    audioCtx.onStop(() => {
      this.setData({ isPlaying: false, playTime: "00:00" });
      clearInterval(this.data.timer);
    });
    audioCtx.onEnded(() => {
      this.setData({ isPlaying: false, playTime: "00:00" });
      clearInterval(this.data.timer);
    });
    audioCtx.onError((res) => {
      console.error("音频错误:", res);
      let errorMsg = "播放失败，请重试";
      if (res.errMsg.includes("404")) {
        errorMsg = "音频文件不存在";
      } else if (res.errMsg.includes("decode")) {
        errorMsg = "无法解码音频文件";
      }
      wx.showToast({ title: errorMsg, icon: "none" });
    });
    
    this.setData({ audioCtx });
  },

  toggleAudio(e) {
    const { index } = e.currentTarget.dataset;
    const { currentIndex, audioCtx, isPlaying, audios } = this.data;

    if (currentIndex === index) {
      // 同一音频：切换播放状态
      if (isPlaying) {
        audioCtx.pause();
        this.updatePlayTimer("pause");
      } else {
        // 修复：检查play()返回值是否存在再调用catch
        const playPromise = audioCtx.play();
        if (playPromise && typeof playPromise.catch === 'function') {
          playPromise.catch(err => {
            console.error("播放失败:", err);
            wx.showToast({ title: "请点击卡片重试", icon: "none" });
          });
        }
        this.updatePlayTimer("start");
      }
    } else {
      // 切换音频：停止当前，加载新音频
      if (currentIndex !== -1) {
        audioCtx.stop();
        clearInterval(this.data.timer);
      }
      
      // 检查音频链接是否有效
      const audioUrl = audios[index].url;
      if (!audioUrl || audioUrl.trim() === '') {
        wx.showToast({ title: "音频链接无效", icon: "none" });
        return;
      }
      
      // 显示加载中提示
      wx.showLoading({ title: '加载音频中...' });
      
      // 设置新音频地址
      audioCtx.src = audioUrl;
      this.setData({ currentIndex: index, isPlaying: false });
      
      // 监听加载完成
      setTimeout(() => {
        wx.hideLoading();
        
        // 修复：检查play()返回值是否存在
        const playPromise = audioCtx.play();
        if (playPromise && typeof playPromise.catch === 'function') {
          playPromise.catch(err => {
            console.error("播放失败:", err);
            wx.showToast({ title: "请点击卡片重试", icon: "none" });
          });
        }
        
        this.setData({ isPlaying: true });
        this.updatePlayTimer("start");
      }, 1000); // 延长加载等待时间
    }
  },

  updatePlayTimer(type) {
    const { audioCtx, timer } = this.data;
    if (type === "start") {
      this.setData({
        timer: setInterval(() => {
          // 增加异常处理
          try {
            const time = Math.floor(audioCtx.currentTime || 0);
            const min = Math.floor(time / 60).toString().padStart(2, "0");
            const sec = (time % 60).toString().padStart(2, "0");
            this.setData({ playTime: `${min}:${sec}` });
          } catch (e) {
            console.error("更新播放时间失败:", e);
          }
        }, 1000)
      });
    } else {
      clearInterval(timer);
    }
  },

  // 新增：预加载音频方法
  preloadAudio(index) {
    const { audios } = this.data;
    if (index >= 0 && index < audios.length && !audios[index].loaded) {
      const tempAudio = wx.createInnerAudioContext();
      tempAudio.src = audios[index].url;
      tempAudio.srcType = 'http';
      tempAudio.load(); // 预加载，这里的load()是临时音频对象，不影响主逻辑
      
      // 预加载完成后标记
      tempAudio.onCanplay(() => {
        audios[index].loaded = true;
        this.setData({ audios });
        tempAudio.destroy(); // 销毁临时对象
      });
    }
  },

  onUnload() {
    const { audioCtx, timer } = this.data;
    audioCtx.stop();
    audioCtx.destroy();
    clearInterval(timer);
  }
});
    