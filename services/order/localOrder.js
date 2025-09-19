// 本地订单存储与查询（布鞋助农场景）
// 结构：[{ orderNo, createTime, status, items:[{spuId,skuId,title,primaryImage,quantity,price,specInfo:[] }], totalAmount, totalQuantity, address, remark }]

const STORAGE_KEY = 'orders.items';

function loadRaw() {
  try {
    const raw = wx.getStorageSync(STORAGE_KEY);
    if (!raw) return [];
    const list = JSON.parse(raw);
    return Array.isArray(list) ? list : [];
  } catch (e) {
    return [];
  }
}

function saveRaw(list) {
  wx.setStorageSync(STORAGE_KEY, JSON.stringify(list || []));
}

function genOrderNo() {
  const ts = Date.now();
  const rand = Math.floor(Math.random() * 9000 + 1000); // 4 位随机
  return String(ts) + String(rand);
}

export function listOrders() {
  return loadRaw();
}

export function getOrder(orderNo) {
  return loadRaw().find((o) => o.orderNo === orderNo) || null;
}

export function createOrder({ items, address = null, remark = '' }) {
  if (!Array.isArray(items) || !items.length) {
    throw new Error('createOrder: empty items');
  }
  const orderNo = genOrderNo();
  let totalAmount = 0;
  let totalQuantity = 0;
  const normItems = items.map((i) => {
    const quantity = i.quantity || 1;
    totalQuantity += quantity;
    totalAmount += (i.price || 0) * quantity;
    return {
      spuId: i.spuId,
      skuId: i.skuId,
      title: i.title,
      primaryImage: i.primaryImage,
      quantity,
      price: i.price || 0,
      specInfo: i.specInfo || [],
    };
  });
  const order = {
    orderNo,
    createTime: Date.now(),
    status: 'paid', // 简化：直接已支付
    items: normItems,
    totalAmount,
    totalQuantity,
    address,
    remark,
  };
  const list = loadRaw();
  list.unshift(order); // 新订单置顶
  saveRaw(list);
  return order;
}

export function updateOrderStatus(orderNo, status) {
  const list = loadRaw();
  const idx = list.findIndex((o) => o.orderNo === orderNo);
  if (idx > -1) {
    list[idx].status = status;
    saveRaw(list);
    return list[idx];
  }
  return null;
}

export function clearOrders() {
  saveRaw([]);
  return [];
}

export function mapStatusToDisplay(status) {
  switch (status) {
    case 'paid': return { text: '待发货', colorClass: 'rural-tag-accent' };
    case 'shipped': return { text: '配送中', colorClass: 'rural-badge-aid' };
    case 'completed': return { text: '已完成', colorClass: 'rural-tag-accent' };
    case 'canceled': return { text: '已取消', colorClass: 'rural-old-price' };
    case 'pending_pay': return { text: '待付款', colorClass: 'rural-price' };
    default: return { text: status, colorClass: 'rural-tag-accent' };
  }
}
