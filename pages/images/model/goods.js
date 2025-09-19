import { genGood } from './good';

export function getGoodsList(baseID = 0, length = 10) {
  return new Array(length).fill(0).map((_, idx) => {
    const good = genGood(idx + baseID);
    if (Math.random() < 0.35) { good.ruralSupport = true; }
    return good;
  });
}

export const goodsList = getGoodsList();
