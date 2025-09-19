// 本地购物车服务：纯布鞋场景最小实现
// 存储结构：[{ spuId, skuId, title, primaryImage, quantity, price, linePrice, specInfo: [{specTitle,specValue}], available, putOnSale, storeId }]

const STORAGE_KEY = 'cart.items';

function loadCart() {
  try {
    const raw = wx.getStorageSync(STORAGE_KEY);
    if (!raw) return [];
    const arr = JSON.parse(raw);
    return Array.isArray(arr) ? arr : [];
  } catch (e) {
    return [];
  }
}

function saveCart(list) {
  wx.setStorageSync(STORAGE_KEY, JSON.stringify(list || []));
}

function findIndex(list, spuId, skuId) {
  return list.findIndex((i) => i.spuId === spuId && i.skuId === skuId);
}

export function addItem(item) {
  const list = loadCart();
  const idx = findIndex(list, item.spuId, item.skuId);
  if (idx > -1) {
    list[idx].quantity += item.quantity || 1;
  } else {
    list.push({ ...item, quantity: item.quantity || 1 });
  }
  saveCart(list);
  return list;
}

export function updateQuantity(spuId, skuId, quantity) {
  const list = loadCart();
  const idx = findIndex(list, spuId, skuId);
  if (idx > -1) {
    list[idx].quantity = quantity <= 0 ? 1 : quantity;
    saveCart(list);
  }
  return list;
}

export function removeItem(spuId, skuId) {
  const list = loadCart().filter((i) => !(i.spuId === spuId && i.skuId === skuId));
  saveCart(list);
  return list;
}

export function clearCart() {
  saveCart([]);
  return [];
}

export function getCartItems() {
  return loadCart();
}

export function computeSummary(list = loadCart()) {
  let totalQuantity = 0;
  let totalAmount = 0; // 分
  list.forEach((i) => {
    totalQuantity += i.quantity;
    totalAmount += i.price * i.quantity;
  });
  return { totalQuantity, totalAmount };
}

// 将本地列表映射为页面当前结构（单店 + 单 promotion 容器）
export function asCartGroupData() {
  const items = loadCart();
  const { totalQuantity, totalAmount } = computeSummary(items);
  const groupData = {
    storeGoods: [
      {
        storeId: '1000',
        storeName: '布鞋工坊直营店',
        storeStatus: 1,
        promotionGoodsList: [
          {
            title: '布鞋馆',
            promotionCode: 'LOCAL',
            promotionSubCode: 'NONE',
            promotionId: null,
            tagText: null,
            promotionStatus: 0,
            tag: null,
            description: null,
            doorSillRemain: null,
            isNeedAddOnShop: 0,
            goodsPromotionList: items.map((x) => ({
              uid: 'local-user',
              saasId: '88888888',
              storeId: x.storeId || '1000',
              spuId: x.spuId,
              skuId: x.skuId,
              isSelected: 1,
              thumb: x.primaryImage,
              title: x.title,
              primaryImage: x.primaryImage,
              quantity: x.quantity,
              stockStatus: true,
              stockQuantity: 9999,
              price: String(x.price),
              originPrice: x.linePrice ? String(x.linePrice) : undefined,
              tagPrice: null,
              titlePrefixTags: null,
              roomId: null,
              specInfo: x.specInfo || [],
              joinCartTime: Date.now(),
              available: x.available ?? 1,
              putOnSale: x.putOnSale ?? 1,
              etitle: null,
            })),
            lastJoinTime: Date.now(),
          },
        ],
        shortageGoodsList: [],
        lastJoinTime: Date.now(),
        postageFreePromotionVo: {},
      },
    ],
    invalidGoodItems: [],
    isNotEmpty: items.length > 0,
    selectedGoodsCount: totalQuantity,
    totalAmount: String(totalAmount),
    totalDiscountAmount: '0',
  };
  return groupData;
}
