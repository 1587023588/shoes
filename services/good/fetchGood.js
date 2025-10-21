import { config } from '../../config/index';

/** 获取商品列表 */
function mockFetchGood(ID = 0) {
  const { delay } = require('../_utils/delay');
  const { genGood, findSpuIndexBySkuId } = require('../../model/good');
  // 支持传入 skuId 或 spuId（ID 可为 skuId 字符串或数字索引）
  return delay().then(() => {
    let idToUse = ID;
    try {
      // 如果是字符串并包含 '-' 很可能是 skuId（生成 skuId 时用 `${baseSalePrice}-${v}`）
      if (typeof ID === 'string' && ID.indexOf('-') > -1) {
        const idx = findSpuIndexBySkuId(ID);
        idToUse = idx >= 0 ? idx : 0;
      }
      // 若 ID 是数字字符串，尝试转为数字
      if (typeof idToUse === 'string' && /^\\d+$/.test(idToUse)) {
        idToUse = parseInt(idToUse, 10);
      }
    } catch (e) {
      // 回退为 0
      idToUse = 0;
    }
    return genGood(idToUse);
  });
}

/** 获取商品列表 */
export function fetchGood(ID = 0) {
  if (config.useMock) {
    return mockFetchGood(ID);
  }
  return new Promise((resolve) => {
    resolve('real api');
  });
}
