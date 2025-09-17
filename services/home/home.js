import { config } from '../../config/index';

/** 获取首页数据 */
function mockFetchHome() {
  const { delay } = require('../_utils/delay');
  const { genSwiperImageList } = require('../../model/swiper');
  return delay().then(() => {
    return {
      swiper: genSwiperImageList(),
      tabList: [
        { text: '手工布鞋', key: 0 },
        { text: '老北京布鞋', key: 1 },
        { text: '童款布鞋', key: 2 },
        { text: '养生舒适', key: 3 },
        { text: '加厚棉鞋', key: 4 },
        { text: '民族特色', key: 5 },
        { text: '助农特惠', key: 6 },
      ],
      // activityImg 字段移除（布鞋主题暂不需要默认活动横幅）
    };
  });
}

/** 获取首页数据 */
export function fetchHome() {
  if (config.useMock) {
    return mockFetchHome();
  }
  return new Promise((resolve) => {
    resolve('real api');
  });
}
