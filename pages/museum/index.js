Page({
  data: {
    // 33张图片：纪念馆展项图及名称
    photos: [
      { id: 1, title: '纪念展项 1', url: '/images/museum/m1.jpg' },
      { id: 2, title: '纪念展项 2', url: '/images/museum/m2.jpg' },
      { id: 3, title: '纪念展项 3', url: '/images/museum/m3.jpg' },
      { id: 4, title: '纪念展项 4', url: '/images/museum/m4.jpg' },
      { id: 5, title: '纪念展项 5', url: '/images/museum/m5.jpg' },
      { id: 6, title: '纪念展项 6', url: '/images/museum/m6.jpg' },
      { id: 7, title: '纪念展项 7', url: '/images/museum/m7.jpg' },
      { id: 8, title: '纪念展项 8', url: '/images/museum/m8.jpg' },
      { id: 9, title: '纪念展项 9', url: '/images/museum/m9.jpg' },
      { id: 10, title: '纪念展项 10', url: '/images/museum/m10.jpg' },
      { id: 11, title: '纪念展项 11', url: '/images/museum/m11.jpg' },
      { id: 12, title: '纪念展项 12', url: '/images/museum/m12.jpg' },
      { id: 13, title: '纪念展项 13', url: '/images/museum/m13.jpg' },
      { id: 14, title: '纪念展项 14', url: '/images/museum/m14.jpg' },
      { id: 15, title: '纪念展项 15', url: '/images/museum/m15.jpg' },
      { id: 16, title: '纪念展项 16', url: '/images/museum/m16.jpg' },
      { id: 17, title: '纪念展项 17', url: '/images/museum/m17.jpg' },
      { id: 18, title: '纪念展项 18', url: '/images/museum/m18.jpg' },
      { id: 19, title: '纪念展项 19', url: '/images/museum/m19.jpg' },
      { id: 20, title: '纪念展项 20', url: '/images/museum/m20.jpg' },
      { id: 21, title: '纪念展项 21', url: '/images/museum/m21.jpg' },
      { id: 22, title: '纪念展项 22', url: '/images/museum/m22.jpg' },
      { id: 23, title: '纪念展项 23', url: '/images/museum/m23.jpg' },
      { id: 24, title: '纪念展项 24', url: '/images/museum/m24.jpg' },
      { id: 25, title: '纪念展项 25', url: '/images/museum/m25.jpg' },
      { id: 26, title: '纪念展项 26', url: '/images/museum/m26.jpg' },
      { id: 27, title: '纪念展项 27', url: '/images/museum/m27.jpg' },
      { id: 28, title: '纪念展项 28', url: '/images/museum/m28.jpg' },
      { id: 29, title: '纪念展项 29', url: '/images/museum/m29.jpg' },
      { id: 30, title: '纪念展项 30', url: '/images/museum/m30.jpg' },
      { id: 31, title: '纪念展项 31', url: '/images/museum/m31.jpg' },
      { id: 32, title: '纪念展项 32', url: '/images/museum/m32.jpg' },
      { id: 33, title: '纪念展项 33', url: '/images/museum/m33.jpg' }
    ],

    // 轮播控制参数
    currentIndex: 0,
    transitionDuration: 500,
    startX: 0,
    isDragging: false,
    autoplay: true,
    interval: 5000
  },

  // audio context will be created on demand
  audioCtx: null,
  isPlaying: false,
  currentAudioUrl: '',

  onLoad() {
    this.startAutoCarousel();
  },

  startAutoCarousel() {
    if (this.data.autoplay) {
      this.carouselInterval = setInterval(() => {
        this.nextSlide();
      }, this.data.interval);
    }
  },

  playAudioFor(index) {
    const exhibitAudioMap = {
      // sample mapping: photo index -> sample audio stored in COS (user said images are COS linked)
      1: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/audio/1.mp3',
      2: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/audio/2.mp3',
    };
    const audioUrl = exhibitAudioMap[index] || exhibitAudioMap[1];
    if (!this.audioCtx) {
      this.audioCtx = wx.createInnerAudioContext();
      this.audioCtx.onError((err) => console.error('audio error', err));
      this.audioCtx.onEnded(() => this.setData({ isPlaying: false }));
    }
    if (this.data.isPlaying && this.data.currentAudioUrl === audioUrl) {
      this.audioCtx.pause();
      this.setData({ isPlaying: false });
      return;
    }
    this.audioCtx.src = audioUrl;
    this.audioCtx.play();
    this.setData({ isPlaying: true, currentAudioUrl: audioUrl });
  },

  goToBooking() {
    wx.navigateTo({ url: '/pages/museum/booking/index' });
  },

  nextSlide() {
    const { currentIndex, photos } = this.data;
    const newIndex = (currentIndex + 1) % photos.length;
    this.setData({ currentIndex: newIndex });
  },

  prevSlide() {
    const { currentIndex, photos } = this.data;
    const newIndex = (currentIndex - 1 + photos.length) % photos.length;
    this.setData({ currentIndex: newIndex });
  },

  switchSlide(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({ currentIndex: index });
  },

  touchStart(e) {
    clearInterval(this.carouselInterval);
    this.setData({
      startX: e.touches[0].clientX,
      isDragging: true,
      transitionDuration: 0
    });
  },

  touchEnd(e) {
    if (!this.data.isDragging) return;

    const endX = e.changedTouches[0].clientX;
    const diffX = endX - this.data.startX;

    if (diffX > 50) {
      this.prevSlide();
    } else if (diffX < -50) {
      this.nextSlide();
    }

    this.setData({
      isDragging: false,
      transitionDuration: 500
    });
    this.startAutoCarousel();
  },

  toggleAutoplay() {
    const { autoplay } = this.data;
    this.setData({ autoplay: !autoplay });

    clearInterval(this.carouselInterval);
    if (!autoplay) {
      this.startAutoCarousel();
    }
  },

  onUnload() {
    clearInterval(this.carouselInterval);
  }
});
