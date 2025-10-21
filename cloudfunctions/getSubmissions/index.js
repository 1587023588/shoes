// 云函数示例：按 spuId 或全部获取 submissions
const cloud = require('wx-server-sdk');
cloud.init();
const db = cloud.database();
const COLLECTION = 'submissions';

exports.main = async (event, context) => {
  const { spuId } = event;
  try {
    let res;
    if (spuId) {
      res = await db.collection(COLLECTION).where({ spuId }).get();
    } else {
      res = await db.collection(COLLECTION).get();
    }
    return { success: true, data: res.data };
  } catch (err) {
    return { success: false, error: err };
  }
};