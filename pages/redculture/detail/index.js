import { redArticles } from '../../../model/redculture';

Page({
  data: {
    article: {},
    isPlaying: false,
  },

  onLoad(query) {
    const { id } = query;
    const a = redArticles.find((x) => x.id === id) || redArticles[0];
    this.setData({ article: a });
    this.audioCtx = wx.createInnerAudioContext();
    this.audioCtx.onEnded(() => this.setData({ isPlaying: false }));
  },

  togglePlay() {
    const { isPlaying, article } = this.data;
    if (!this.audioCtx) return;
    if (!isPlaying) {
      this.audioCtx.src = article.audio;
      this.audioCtx.play();
      this.setData({ isPlaying: true });
    } else {
      this.audioCtx.pause();
      this.setData({ isPlaying: false });
    }
  },
});
