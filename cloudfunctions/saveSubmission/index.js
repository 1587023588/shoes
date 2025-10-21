// 云函数示例：保存 submission 到云数据库（wx cloud）
// 部署前请在微信开发者工具开启云环境并在函数中替换 env 或 collection 名称
const cloud = require('wx-server-sdk');
cloud.init();
const db = cloud.database();
const COLLECTION = 'submissions';

exports.main = async (event, context) => {
  const { submission } = event;
  if (!submission || !submission.id) {
    return { success: false, message: 'invalid payload' };
  }
  try {
    await db.collection(COLLECTION).add({ data: submission });
    return { success: true };
  } catch (err) {
    return { success: false, error: err };
  }
};