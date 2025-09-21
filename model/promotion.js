import { getGoodsList } from './goods';

export function getPromotion(baseID = 0, length = 10) {
  return {
    list: getGoodsList(baseID, length).map((item) => {
      return {
        spuId: item.spuId,
        thumb: item.primaryImage,
        title: item.title,
        price: item.minSalePrice,
        originPrice: item.maxLinePrice,
        // 使用字符串数组，避免模板中渲染为 [object Object]
        tags: item.spuTagList.map((tag) => tag.title),
      };
    }),
  banner: '/test.jpg',
    time: 1000 * 60 * 60 * 20,
    showBannerDesc: true,
    statusTag: 'running',
  };
}
