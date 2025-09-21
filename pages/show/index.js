Page({
  data: {
    photos: [],
    currentIndex: 0,
    transitionDuration: 500,
    autoplay: false
  },

  onLoad() {
    // 静态列出 shoes 目录图片（小程序运行时不支持动态读目录）
    const files = [
      'mmexport1758337863086.jpg',
      '861005e944040b04c5cc6f7d89467ed5.jpg',
      'mmexport1758338004881.jpg',
      'mmexport1758337885947.jpg',
      'mmexport1758337869549.jpg',
      'mmexport1758338116363.jpg',
      'mmexport1758338125394.jpg',
      'mmexport1758338127257.jpg',
      'mmexport1758338119769.jpg',
      'mmexport1758338164591.jpg',
      'mmexport1758338169716.jpg',
      'mmexport1758338166673.jpg',
      'mmexport1758338109370.jpg',
      'mmexport1758347505596.jpg',
      'mmexport1758347483558.jpg',
      'mmexport1758347471796.jpg',
      'mmexport1758347463118.jpg',
      'mmexport1758338478219.jpg',
      'mmexport1758347528731.jpg',
      'mmexport1758338195322.jpg',
      'mmexport1758338193233.jpg',
      'mmexport1758338180340.jpg',
      'mmexport1758338103327.jpg',
      'mmexport1758338091957.jpg',
      'mmexport1758338076678.jpg',
      'mmexport1758338057023.jpg',
      'mmexport1758338015904.jpg',
      'mmexport1758338009535.jpg',
      'mmexport1758338007205.jpg',
      'mmexport1758347574137.jpg',
      'mmexport1758347543369.jpg',
    ];
    const photos = files.map((name, i) => ({ id: i + 1, url: `/pages/shoes/${name}` }));
    this.setData({ photos });
  },

  nextSlide() {
    const { currentIndex, photos } = this.data;
    if (!photos || photos.length === 0) return;
    this.setData({ currentIndex: (currentIndex + 1) % photos.length });
  },
  prevSlide() {
    const { currentIndex, photos } = this.data;
    if (!photos || photos.length === 0) return;
    this.setData({ currentIndex: (currentIndex - 1 + photos.length) % photos.length });
  },
  switchSlide(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({ currentIndex: index });
  },
  toggleAutoplay() {
    // 预留：如需自动播放可在此开启
  },
});
