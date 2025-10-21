export const courses = [
  {
    id: 'c1',
    title: '布鞋制作入门',
    cover: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/course/cover1.jpg',
    lessons: [
      { id: 'c1_l1', title: '准备材料', video: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/course/lesson1.mp4' },
      { id: 'c1_l2', title: '缝合技巧', video: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/course/lesson2.mp4' },
    ],
    materials: [
      { skuId: 'mat_needle', title: '针线套装', price: 9.9, image: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/course/needle.jpg' },
      { skuId: 'mat_insole', title: '千层底半成品', price: 29.9, image: 'https://shoes-1379330878.cos.ap-beijing.myqcloud.com/course/insole.jpg' },
    ],
  },
];
