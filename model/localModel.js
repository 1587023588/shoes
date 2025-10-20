export function getLocalModel(key) {
  try {
    const raw = wx.getStorageSync(key);
    return raw ? JSON.parse(raw) : null;
  } catch (err) {
    try {
      // 如果已存储为对象
      const obj = wx.getStorageSync(key);
      return obj || null;
    } catch (e) {
      return null;
    }
  }
}

export function saveLocalModel(key, value) {
  try {
    const raw = typeof value === 'string' ? value : JSON.stringify(value || []);
    wx.setStorageSync(key, raw);
  } catch (err) {
    console.error('saveLocalModel error', err);
  }
}
