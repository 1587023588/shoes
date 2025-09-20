Page({
  data: {
    // 39张图片：按序号1-39递增
    photos: [
      { id: 1, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/1.jpg' },
      { id: 2, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/2.jpg' },
      { id: 3, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/3.jpg' },
      { id: 4, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/4.jpg' },
      { id: 5, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/5.jpg' },
      { id: 6, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/6.jpg' },
      { id: 7, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/7.jpg' },
      { id: 8, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/8.jpg' },
      { id: 9, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/9.jpg' },
      { id: 10, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/10.jpg' },
      { id: 11, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/11.jpg' },
      { id: 12, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/12.jpg' },
      { id: 13, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/13.jpg' },
      { id: 14, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/14.jpg' },
      { id: 15, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/15.jpg' },
      { id: 16, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/16.jpg' },
      { id: 17, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/17.jpg' },
      { id: 18, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/18.jpg' },
      { id: 19, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/19.jpg' },
      { id: 20, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/20.jpg' },
      { id: 21, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/21.jpg' },
      { id: 22, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/22.jpg' },
      { id: 23, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/23.jpg' },
      { id: 24, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/24.jpg' },
      { id: 25, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/25.jpg' },
      { id: 26, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/26.jpg' },
      { id: 27, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/27.jpg' },
      { id: 28, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/28.jpg' },
      { id: 29, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/29.jpg' },
      { id: 30, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/30.jpg' },
      { id: 31, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/31.jpg' },
      { id: 32, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/32.jpg' },
      { id: 33, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/33.jpg' },
      { id: 34, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/34.jpg' },
      { id: 35, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/35.jpg' },
      { id: 36, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/36.jpg' },
      { id: 37, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/37.jpg' },
      { id: 38, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/38.jpg' },
      { id: 39, url: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/39.jpg' }
    ],
    
    // 轮播控制参数
    currentIndex: 0,
    transitionDuration: 500,
    startX: 0,
    isDragging: false,
    autoplay: true,
    interval: 5000
  },

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
