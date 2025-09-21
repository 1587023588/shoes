import { cdnBase } from '../config/index';
const imgPrefix = cdnBase; // 仍保留变量（可能其它地方引用），但当前已统一改用本地占位图

// 详情页兜底图片统一占位
const defaultDesc = ['/pages/images/test.jpg'];

// 本地主图资源清单（与商品编号一一对应）
// 注意：文件名与扩展名需与实际文件一致
const goodsPrimaryList = [
  '/goods_primaryImage/goods_1.png',
  '/goods_primaryImage/goods_2.jpg',
  '/goods_primaryImage/goods_3.jpg',
  '/goods_primaryImage/goods_4.jpg',
  '/goods_primaryImage/goods_5.jpg',
  '/goods_primaryImage/goods_6.jpg',
  '/goods_primaryImage/goods_7.jpg',
  '/goods_primaryImage/goods_8.jpg',
  '/goods_primaryImage/goods_9.jpg',
  '/goods_primaryImage/goods_10.jpg',
];

// 统一的详情图（若不存在，将在页面层面回退到 test.jpg）
// 已按你的新目录调整到 goods_primaryImage/goods_datail/goods_detail.jpg
const unifiedDetailImage = '/goods_primaryImage/goods_datail/goods_detail.jpg';

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
    title: '男款布鞋 手工真千层底 加棉加厚',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    // 商品简介（可自行修改）
    intro: '经典款式兼具传统工艺与保暖实用性',
    // 详情图（可替换为你的商品详情长图列表）
    desc: ['/pages/images/test.jpg'],
    salePrice: 12900,
    linePrice: 16900,
    soldNum: 1880,
    tags: ['加棉', '加厚','保暖'],
  },
  {
    spuId: '10002',
    title: '女款布鞋 荷花印花浅口款式',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '传统工艺打造 尽显国风雅致',
    desc: ['/pages/images/test.jpg'],
    salePrice: 9900,
    linePrice: 13900,
    soldNum: 920,
    tags: ['荷花','雅致'],
  },
  {
    spuId: '10003',
    title: '孩童款布鞋 虎头刺绣搭系带设计',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '传统民俗元素融合童趣风格 尽显萌趣',
    desc: ['/pages/images/test.jpg'],
    salePrice: 15900,
    linePrice: 19900,
    soldNum: 610,
    tags: ['虎头','童趣'],
  },
  {
    spuId: '10004',
    title: '女款布鞋 亮黄色提花面料搭配棕色系带蝴蝶结设计',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '复古风格中尽显俏皮时尚 传统工艺融合个性穿搭感',
    desc: ['/pages/images/test.jpg'],
    salePrice: 8900,
    linePrice: 10900,
    soldNum: 420,
    tags: ['亮色','蝴蝶结','时尚'],
  },
  {
    spuId: '10005',
    title: '女款布鞋 网纱刺绣花卉搭配浅口设计',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '红饰点缀尽显婉约 传统工艺融合清新雅致风格',
    desc: ['/pages/images/test.jpg'],
    salePrice: 18900,
    linePrice: 22900,
    soldNum: 350,
    tags: ['婉约', '清新脱俗'],
  },
  {
    spuId: '10006',
    title: '女款布鞋 紫色布艺浅口款式',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '传统工艺打造 典雅舒适',
    desc: ['/pages/images/test.jpg'],
    salePrice: 18900,
    linePrice: 22900,
    soldNum: 350,
    tags: ['典雅', '紫色'],
  },
  {
    spuId: '10007',
    title: '男款布鞋 经典黑色一脚蹬款式',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '尽显传统布鞋的简约大气与舒适质感',
    desc: ['/pages/images/test.jpg'],
    salePrice: 18900,
    linePrice: 22900,
    soldNum: 350,
    tags: ['经典', '简约', '质朴'],
  },
  {
    spuId: '10008',
    title: '女款布鞋 多色浅口传统款式',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '多样选择 便于搭配 传统工艺与丰富选择兼具',
    desc: ['/pages/images/test.jpg'],
    salePrice: 18900,
    linePrice: 22900,
    soldNum: 350,
    tags: ['浅色系', '颜色多样'],
  },
  {
    spuId: '10009',
    title: '男款布鞋 纯手工千层底亚麻材质',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '经典浅口款式 吸汗透气',
    desc: ['/pages/images/test.jpg'],
    salePrice: 18900,
    linePrice: 22900,
    soldNum: 350,
    tags: ['亚麻', '透气', '吸汗'],
  },
  {
    spuId: '10010',
    title: '女款布鞋 刺绣花卉搭配浅口设计',
  primaryImage: '/pages/images/test.jpg',
  images: ['/pages/images/test.jpg'],
    intro: '网纱材质透气舒适',
    desc: ['/pages/images/test.jpg'],
    salePrice: 18900,
    linePrice: 22900,
    soldNum: 350,
    tags: ['透气', '刺绣'],
  },
];

// 构建最终商品数据（包含规格）
const allGoods = baseGoodsMeta.map((g, baseIndex) => {
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
    // 支持按商品覆盖详情图
    desc: g.desc && g.desc.length ? g.desc : defaultDesc,
    // 支持商品简介
    intro: g.intro || '',
    promotionList: null,
    minProfitPrice: null,
    etitle: '',
    // 基于元数据顺序的索引，用于稳定的资源映射
    baseIndex,
  };
});

// 生成器：根据 id 循环取布鞋商品，并允许覆盖上架状态
export function genGood(id, available = 1) {
  const item = allGoods[id % allGoods.length];
  // 按元数据原始顺序映射到本地主图，保证顺序稳定
  const listLen = goodsPrimaryList.length;
  const idx = item.baseIndex % listLen;
  const mappedPrimary = goodsPrimaryList[idx] || item.primaryImage;
  const isDefault = (p) => !p || p === '/pages/images/test.jpg';
  // 主图：优先用手动设置，其次回退到映射主图
  const primaryImage = isDefault(item.primaryImage) ? mappedPrimary : item.primaryImage;
  // 轮播图：若已有有效 images 则保留，否则使用 [primaryImage]
  const hasValidImages = Array.isArray(item.images) && item.images.some((s) => s && !isDefault(s));
  const images = hasValidImages ? item.images : [primaryImage];
  // 详情图：优先保留手动设置，否则使用统一 goods_detail（页面层面仍有兜底）
  const hasValidDesc = Array.isArray(item.desc) && item.desc.some((s) => s && !isDefault(s));
  const desc = hasValidDesc ? item.desc : [unifiedDetailImage];
  return {
    ...item,
    spuId: `${id}`,
    available,
    primaryImage,
    images,
    desc,
    intro: item.intro || '',
  };
}

