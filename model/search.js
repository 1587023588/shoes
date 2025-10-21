import { getGoodsList } from './goods';

/**
 * @param {number} sort
 * @param {number} pageNum
 * @param {number} pageSize
 * @param {number} minPrice
 * @param {number} maxPrice
 * @param {string} keyword
 */

export function getSearchHistory() {
  return {
    historyWords: [
      '手工布鞋',
      '老北京布鞋',
      '千层底',
      '棉布鞋',
      '防滑软底',
      '妈妈鞋',
      '父亲节礼物',
      '儿童布鞋',
      '养生布鞋',
      '民族风布鞋',
      '加绒布鞋',
      '透气布鞋',
    ],
  };
}

export function getSearchPopular() {
  return {
    popularWords: [
      '手工布鞋',
      '千层底',
      '老北京布鞋',
      '黑色布鞋',
      '棉鞋加厚',
      '民族特色',
      '舒适养生',
      '防滑软底',
      '透气夏款',
      '儿童布鞋',
      '情侣布鞋',
      '助农特惠',
    ],
  };
}

export function getSearchResult() {
  return {
    saasId: null,
    storeId: null,
    pageNum: 1,
    pageSize: 30,
    totalCount: 1,
    spuList: getGoodsList(7),
    algId: 0,
  };
}
