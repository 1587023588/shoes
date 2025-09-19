import { cdnBase } from '../config/index';
const imgPrefix = cdnBase; // 仍保留变量（可能其它地方引用），但当前已统一改用本地占位图

// 详情页兜底图片统一占位
const defaultDesc = ['/pages/images/test.jpg'];

/*
  说明：
  1. 彻底移除原来与布鞋无关的大量 mock（家居/数码等）。
  2. 为保持详情页与规格弹窗的最小可用字段：保留 skuList / specList / desc / spuStockQuantity / isPutOnSale。
  3. 价格字段统一使用字符串，沿用原逻辑 parseInt 解析。
  4. 规格仅保留“尺码”一个维度，简化交互；若后续需要颜色等，可在 buildSpecs 中扩展。
*/

const sizeSpecValues = ['39', '40', '41', '42'];

function buildSpecs(baseSalePrice, baseLinePrice) {
  // 构建尺码规格与对应 sku 列表
  const specId = 'size';
  const specList = [
    {
      specId,
      title: '尺码',
      specValueList: sizeSpecValues.map((v) => ({
        specValueId: v,
        specId,
        saasId: '88888888',
        specValue: v,
        image: '',
      })),
    },
  ];
  let totalStock = 0;
  const skuList = sizeSpecValues.map((v, idx) => {
    const stock = 50 - idx * 5; // 简单递减库存
    totalStock += stock;
    return {
      skuId: `${baseSalePrice}-${v}`,
      skuImage: null,
      specInfo: [
        {
          specId,
          specTitle: '尺码',
          specValueId: v,
          specValue: v,
        },
      ],
      priceInfo: [
        { priceType: 1, price: `${baseSalePrice}`, priceTypeName: '销售价格' },
        { priceType: 2, price: `${baseLinePrice}`, priceTypeName: '划线价格' },
      ],
      stockInfo: {
        stockQuantity: stock,
        safeStockQuantity: 0,
        soldQuantity: 0,
      },
      weight: null,
      volume: null,
      profitPrice: null,
    };
  });
  return { specList, skuList, totalStock };
}

// 基础布鞋元数据（不含规格）
const baseGoodsMeta = [
  {
    spuId: '10001',
    title: '手工千层底老北京布鞋 经典黑色',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg', '/pages/images/test.jpg'],
    salePrice: 12900,
    linePrice: 16900,
    soldNum: 1880,
    tags: ['经典', '手工'],
  },
  {
    spuId: '10002',
    title: '透气布面休闲鞋 夏季凉爽',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    salePrice: 9900,
    linePrice: 13900,
    soldNum: 920,
    tags: ['透气'],
  },
  {
    spuId: '10003',
    title: '加绒保暖布鞋 冬季亲肤',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    salePrice: 15900,
    linePrice: 19900,
    soldNum: 610,
    tags: ['保暖'],
  },
  {
    spuId: '10004',
    title: '儿童软底防滑布鞋 轻便安全',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    salePrice: 8900,
    linePrice: 10900,
    soldNum: 420,
    tags: ['童款'],
  },
  {
    spuId: '10005',
    title: '民族风刺绣布鞋 手工花纹',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    salePrice: 18900,
    linePrice: 22900,
    soldNum: 350,
    tags: ['民族', '刺绣'],
  },
];

// 构建最终商品数据（包含规格）
const allGoods = baseGoodsMeta.map((g) => {
  const { specList, skuList, totalStock } = buildSpecs(g.salePrice, g.linePrice);
  return {
    saasId: '88888888',
    storeId: '1000',
    spuId: g.spuId,
    title: g.title,
    primaryImage: g.primaryImage,
    images: g.images,
    available: 1,
    minSalePrice: `${g.salePrice}`,
    maxSalePrice: `${g.salePrice}`,
    minLinePrice: `${g.linePrice}`,
    maxLinePrice: `${g.linePrice}`,
    isPutOnSale: 1,
    soldNum: g.soldNum,
    spuStockQuantity: totalStock,
    spuTagList: (g.tags || []).map((t, idx) => ({ id: `${g.spuId}-tag-${idx}`, title: t, image: null })),
    skuList,
    specList,
    desc: defaultDesc,
    promotionList: null,
    minProfitPrice: null,
    etitle: '',
  };
});

// 生成器：根据 id 循环取布鞋商品，并允许覆盖上架状态
export function genGood(id, available = 1) {
  const item = allGoods[id % allGoods.length];
  return {
    ...item,
    spuId: `${id}`,
    available,
    desc: item.desc || defaultDesc,
    images: item.images || [item.primaryImage],
  };
}

