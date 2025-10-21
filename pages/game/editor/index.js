import { shoeTemplates, stickers as stickerAssets } from '../../../model/gameAssets.js';
import { saveLocalModel, getLocalModel } from '../../../model/localModel.js';

Page({
  data: {
    templates: shoeTemplates.map((item) => ({ ...item })),
    stickers: stickerAssets.map((item) => ({ ...item })),
    selectedTemplate: null,
    stickerInstances: [],
    assetFallback: 'https://tdesign.gtimg.com/miniprogram/images/default-img.png',
  },

  onReady() {
    const query = wx.createSelectorQuery().in(this);
    query
      .select('#editorCanvas')
      .fields({ node: true, size: true })
      .exec((res) => {
        if (!res || !res[0] || !res[0].node) {
          console.error('[editor] canvas node not found', res);
          wx.showToast({ title: '画布初始化失败', icon: 'none' });
          return;
        }

        const { node: canvas, width, height } = res[0];
        const dpr = wx.getSystemInfoSync().pixelRatio || 1;
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.dpr = dpr;
        canvas.width = width * dpr;
        canvas.height = height * dpr;
        this.ctx.scale(dpr, dpr);
        this.draw();
      });
  },

  selectTemplate(e) {
    const src = e.currentTarget.dataset.src;
    this.setData({ selectedTemplate: src });
    this.draw();
  },

  addSticker(e) {
    const src = e.currentTarget.dataset.src;
    const inst = { id: `i_${Date.now()}`, src, x: 150, y: 150, w: 100, h: 100 };
    const arr = this.data.stickerInstances.slice();
    arr.push(inst);
    this.setData({ stickerInstances: arr }, () => this.draw());
  },

  onAssetError(e) {
    const { collection, index } = e.currentTarget.dataset;
    if (!collection || index == null) return;
    const fallback = this.data.assetFallback;
    if (!fallback) return;
    const key = `${collection}[${index}].image`;
    const current = this.data[collection] || [];
    const original = current[index] && current[index].image;
    if (!original || original === fallback) return;

    const updates = { [key]: fallback };
    if (collection === 'templates' && this.data.selectedTemplate === original) {
      updates.selectedTemplate = fallback;
      this._selectedFromFallback = true;
    }
    if (collection === 'stickers') {
      const replacement = (this.data.stickerInstances || []).map((it) =>
        it.src === original ? { ...it, src: fallback } : it
      );
      updates.stickerInstances = replacement;
    }

    this.setData(updates, () => {
      if (this._selectedFromFallback) {
        this._selectedFromFallback = false;
        this.draw();
      }
    });
    wx.showToast({ title: '素材已替换为默认图', icon: 'none' });
  },

  // very simple drag implementation
  onTouchStart(e) {
    const t = e.touches[0];
    this.dragStart = { x: t.x, y: t.y };
    // find topmost sticker under point
    const idx = this._hitTest(t.x, t.y);
    this.dragIndex = idx;
  },

  onTouchMove(e) {
    if (this.dragIndex == null) return;
    const t = e.touches[0];
    const dx = t.x - this.dragStart.x;
    const dy = t.y - this.dragStart.y;
    const arr = this.data.stickerInstances.slice();
    arr[this.dragIndex].x += dx;
    arr[this.dragIndex].y += dy;
    this.dragStart = { x: t.x, y: t.y };
    this.setData({ stickerInstances: arr }, () => this.draw());
  },

  onTouchEnd() {
    this.dragIndex = null;
  },

  _hitTest(px, py) {
    const arr = this.data.stickerInstances;
    for (let i = arr.length - 1; i >= 0; i--) {
      const it = arr[i];
      if (px >= it.x && px <= it.x + it.w && py >= it.y && py <= it.y + it.h) return i;
    }
    return null;
  },

  draw() {
    if (!this.ctx) return;
    const ctx = this.ctx;
    const width = this.canvasWidth || 300;
    const height = this.canvasHeight || 300;
    ctx.clearRect(0, 0, width, height);
    // draw template
    if (this.data.selectedTemplate) {
      this._createImage(this.data.selectedTemplate, (img) => {
        ctx.drawImage(img, 0, 0, width, height);
        this._drawStickers(ctx);
      });
    } else {
      // empty background
      ctx.fillStyle = '#fff';
      ctx.fillRect(0, 0, width, height);
      this._drawStickers(ctx);
    }
  },

  _drawStickers(ctx) {
    const arr = this.data.stickerInstances || [];
    if (!arr.length) return;
    arr.forEach((it) => {
      this._createImage(it.src, (img) => {
        ctx.drawImage(img, it.x, it.y, it.w, it.h);
      });
    });
  },

  _createImage(src, onload) {
    if (!this.canvas || typeof this.canvas.createImage !== 'function') {
      console.warn('[editor] createImage not available');
      return null;
    }
    const img = this.canvas.createImage();
    img.src = src;
    img.onload = () => typeof onload === 'function' && onload(img);
    img.onerror = () => {
      console.error('[editor] image load failed', src);
      wx.showToast({ title: '素材加载失败', icon: 'none' });
    };
    return img;
  },

  exportImage() {
    // export canvas to temp file and save as draft
    if (!this.canvas) {
      wx.showToast({ title: '画布尚未准备好', icon: 'none' });
      return;
    }
    wx.canvasToTempFilePath({
      canvasId: 'editorCanvas',
      canvas: this.canvas,
      success: (res) => {
        const tempFile = res.tempFilePath;
        // save as draft in local storage
        const drafts = getLocalModel('designDrafts') || [];
        drafts.unshift({ id: `d_${Date.now()}`, image: tempFile, createdAt: Date.now() });
        saveLocalModel('designDrafts', drafts);
        wx.showToast({ title: '已保存为稿件', icon: 'success' });
      },
      fail(err) {
        console.error('export fail', err);
        wx.showToast({ title: '导出失败', icon: 'none' });
      }
    }, this);
  }
});
