import { getLocalModel, saveLocalModel } from '../../model/localModel';

const KEY = 'appointments';

export async function saveAppointment(payload) {
  const list = getLocalModel(KEY) || [];
  list.unshift(payload);
  saveLocalModel(KEY, list);
  return payload;
}

export async function fetchAppointments() {
  return getLocalModel(KEY) || [];
}
