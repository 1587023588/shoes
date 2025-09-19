// rural-theme brand constants
export const brandSlogans = [
  '北庄布鞋 · 脚下的乡土温度',
  '一针一线 织就乡村振兴',
  '走稳每一步 助力北庄新生活',
  '匠心手作 守护足下健康',
];

export function getRandomSlogan() {
  return brandSlogans[Math.floor(Math.random() * brandSlogans.length)];
}

export const brandCTA = '支持乡村';
