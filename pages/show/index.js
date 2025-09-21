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
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/01.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/02.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/03.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/04.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/05.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/06.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/07.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/08.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/09.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/10.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/11.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/12.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/13.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/14.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/15.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/16.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/17.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/18.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/19.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/20.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/21.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/22.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/23.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/24.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/25.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/26.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/27.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/28.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/29.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/30.jpg',
      'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/31.jpg',
    ];
    const photos = files.map((name, i) => {
      const isFull = /^https?:\/\//i.test(name);
      const url = isFull ? name : `/pages/shoes/${name}`;
      return { id: i + 1, url };
    });
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

  // 图片加载失败兜底：切换扩展名或替换为本地占位，防止空白
  onImgError(e) {
    const idx = e.currentTarget.dataset.index;
    if (idx === undefined || idx === null) return;
    const photos = this.data.photos.slice();
    const failed = photos[idx];
    if (!failed) return;
    if (failed._errorHandled) return;

    const originalUrl = failed.url || '';
    let alt = '';
    if (/\.jpg$/i.test(originalUrl)) {
      alt = originalUrl.replace(/\.jpg$/i, '.png');
    } else if (/\.png$/i.test(originalUrl)) {
      alt = originalUrl.replace(/\.png$/i, '.jpg');
    }

    // 本地占位：项目根已有 test.jpg，可确保离线也能显示
    const localFallback = '/test.jpg';
    const nextUrl = alt || localFallback;

    photos[idx] = { ...failed, url: nextUrl, _errorHandled: true };
    this.setData({ photos });
  }
});
