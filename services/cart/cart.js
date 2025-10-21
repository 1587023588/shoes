import { config } from '../../config/index';
import { asCartGroupData } from './localCart';

/** 获取购物车mock数据 */
function mockFetchCartGroupData(params) {
  const { delay } = require('../_utils/delay');
  const { genCartGroupData } = require('../../model/cart');

  return delay().then(() => genCartGroupData(params));
}

/** 获取购物车数据 */
export function fetchCartGroupData(params) {
  if (config.useMock) {
    return mockFetchCartGroupData(params);
  }

  return new Promise((resolve) => {
    resolve('real api');
  });
}

// 本地真实购物车（替代 mock），用于布鞋改造阶段
export function fetchLocalCartGroupData() {
  return Promise.resolve({ data: asCartGroupData() });
}
