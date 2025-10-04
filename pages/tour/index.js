Page({
  data: {
    current: 0,
    slides: [
      { id: 1, image: '/pages/images/test.jpg', title: '展品一', desc: '介绍文案示例', audio: '' },
      { id: 2, image: '/pages/images/test.jpg', title: '展品二', desc: '介绍文案示例', audio: '' },
      { id: 3, image: '/pages/images/test.jpg', title: '展品三', desc: '介绍文案示例', audio: '' }
    ],
    title: '',
    desc: '',
    audioPlaying: false,
    currentHasAudio: false,
  },

  onLoad() {
    this.audio = wx.createInnerAudioContext();
    this.audio.obeyMuteSwitch = true;
    this.audio.autoplay = false;
    this.updateMeta(0);
  },
  onUnload() {
    if (this.audio) {
      this.audio.stop();
      this.audio.destroy();
    }
  },

  updateMeta(i) {
    const s = this.data.slides[i] || {};
    const hasAudio = !!s.audio;
    this.setData({
      current: i,
      title: s.title || '',
      desc: s.desc || '',
      currentHasAudio: hasAudio,
      audioPlaying: false
    });
    if (this.audio) {
      this.audio.stop();
      if (hasAudio) this.audio.src = s.audio;
    }
  },

  onSwiperChange(e) {
    // 若出现非箭头导致的切换（极端机型），阻止前进，强制回退到期望 current
    const nextIndex = e && e.detail ? e.detail.current : 0;
    if (this._navLockManual) {
      // 来自我们主动的切换
      this._navLockManual = false;
      this.updateMeta(nextIndex || 0);
    } else {
      // 非法来源：还原
      this.updateMeta(this.data.current || 0);
    }
  },

  prev() {
    if (this._navLock) return;
    this._navLock = true;
    wx.vibrateShort && wx.vibrateShort({ type: 'light' });
    const i = this.data.current;
    if (i > 0) { this._navLockManual = true; this.updateMeta(i - 1); }
    setTimeout(() => { this._navLock = false; }, 280);
  },
  next() {
    if (this._navLock) return;
    this._navLock = true;
    wx.vibrateShort && wx.vibrateShort({ type: 'light' });
    const i = this.data.current;
    if (i < this.data.slides.length - 1) { this._navLockManual = true; this.updateMeta(i + 1); }
    setTimeout(() => { this._navLock = false; }, 280);
  },

  toggleAudio() {
    if (!this.data.currentHasAudio) {
      wx.showToast({ title: '暂无音频', icon: 'none' });
      return;
    }
    if (this.data.audioPlaying) {
      this.audio.pause();
      this.setData({ audioPlaying: false });
    } else {
      this.audio.play();
      this.setData({ audioPlaying: true });
    }
  },
  // 用于 touch-guard 兜底拦截
  noop() { },
});
