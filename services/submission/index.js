import { getLocalModel, saveLocalModel } from '../../model/localModel';

const KEY = 'submissions';

export async function saveSubmission(payload) {
  const list = getLocalModel(KEY) || [];
  list.unshift(payload);
  saveLocalModel(KEY, list);
  return payload;
}

export async function fetchSubmissionsByUser() {
  // For MVP we return all submissions
  return getLocalModel(KEY) || [];
}

export async function fetchSubmissionsBySpuId(spuId) {
  const list = getLocalModel(KEY) || [];
  if (!spuId) return list;
  return list.filter((s) => s.spuId === spuId);
}

export async function uploadAndSaveSubmission(payload) {
  // payload.images are local tempFilePaths
  try {
    if (!wx.cloud || !wx.cloud.uploadFile) {
      // cloud not available, fallback
      return saveSubmission(payload);
    }
    const uploadPromises = (payload.images || []).map((p, idx) => {
      const cloudPath = `submissions/${payload.id || Date.now()}_${idx}.jpg`;
      return wx.cloud.uploadFile({ cloudPath, filePath: p });
    });
    const res = await Promise.all(uploadPromises);
    const fileIDs = res.map((r) => r.fileID);
    const cloudPayload = Object.assign({}, payload, { images: fileIDs });
    // try to call cloud function to persist (if configured)
    if (wx.cloud && wx.cloud.callFunction) {
      try {
        await wx.cloud.callFunction({ name: 'saveSubmission', data: { submission: cloudPayload } });
        return cloudPayload;
      } catch (err) {
        // fallback to local
        console.warn('cloud call failed, fallback to local save', err);
        return saveSubmission(cloudPayload);
      }
    }
    return saveSubmission(cloudPayload);
  } catch (err) {
    console.error('uploadAndSaveSubmission error', err);
    return saveSubmission(payload);
  }
}
