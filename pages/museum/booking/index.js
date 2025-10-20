import { saveAppointment, fetchAppointments } from '../../../services/appointment';

Page({
  data: {
    date: '',
    timeSlots: ['09:00-10:00', '10:00-11:00', '14:00-15:00', '15:00-16:00'],
    selectedTime: '',
    contact: '',
    bookings: [],
  },

  onLoad() {
    this.loadBookings();
  },

  onDateChange(e) {
    this.setData({ date: e.detail.value });
  },

  onTimeChange(e) {
    this.setData({ selectedTime: this.data.timeSlots[e.detail.value] });
  },

  onContactInput(e) {
    this.setData({ contact: e.detail.value });
  },

  async submitBooking() {
    const { date, selectedTime, contact } = this.data;
    if (!date || !selectedTime || !contact) {
      wx.showToast({ title: '请完善信息', icon: 'none' });
      return;
    }
    const payload = {
      id: `b_${Date.now()}`,
      date,
      time: selectedTime,
      contact,
      status: 'pending',
      createdAt: Date.now(),
    };
    await saveAppointment(payload);
    wx.showToast({ title: '预约提交成功', icon: 'success' });
    this.setData({ date: '', selectedTime: '', contact: '' });
    this.loadBookings();
  },

  async loadBookings() {
    const list = await fetchAppointments();
    this.setData({ bookings: list || [] });
  },
});
