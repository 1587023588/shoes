import Toast from '../../miniprogram_npm/tdesign-miniprogram/toast/index';

Page({
  data: {
    activeCategory: 0,
    categories: [
      { id: 0, name: '西柏坡精神' },
      { id: 1, name: '北庄故事' },
      { id: 2, name: '拥军记忆' },
      { id: 3, name: '红色传承' }
    ],
    spiritDetails: [
      {
        title: '西柏坡精神',
        corePoints: [
          '谦虚谨慎、不骄不躁',
          '艰苦奋斗、攻坚克难',
          '实事求是、求真务实',
          '一心为民、服务群众'
        ],
        content: `西柏坡精神是中国革命历史的重要精神遗产，由毛泽东在西柏坡召开的中国共产党七届二中全会上提出，是党中央驻西柏坡期间形成的革命精神。其核心内涵包括"两个务必"：务必使同志们继续地保持谦虚、谨慎、不骄、不躁的作风，务必使同志们继续地保持艰苦奋斗的作风。这些精神至今仍是我们工作和生活的重要指导思想。`,
        historicalBackground: '西柏坡时期（1948-1949）是中国革命的重要转折点，党中央在此指挥了辽沈、淮海、平津三大战役，召开了七届二中全会，为新中国成立奠定了基础。'
      },
      {
        title: '北庄故事',
        corePoints: [
          '军民团结、鱼水情深',
          '自力更生、艰苦奋斗',
          '红色传承、不忘初心'
        ],
        content: `北庄作为革命老区，传承了西柏坡精神的核心内涵，形成了独特的乡村红色文化。抗战时期，北庄群众积极支援八路军，形成了"军民团结如一人"的生动局面；新时代，北庄人继承艰苦奋斗精神，发展特色产业，实现了乡村振兴。`,
        historicalBackground: '北庄位于革命老区，曾是八路军抗日根据地的重要组成部分，见证了军民共同抗敌的历史，如今已成为红色旅游与乡村振兴示范村。'
      },
      {
        title: '拥军记忆',
        corePoints: [
          '军爱民、民拥军',
          '爱国爱军、无私奉献',
          '军民团结、共筑长城'
        ],
        content: `拥军优属是西柏坡精神中"一心为民"理念的延伸，体现了军队与人民的血肉联系。从革命年代群众踊跃参军、支援前线，到和平时期关心军人、优待军属，拥军传统代代相传，形成了独特的军民文化。`,
        historicalBackground: '拥军传统源于革命战争时期，在西柏坡等革命根据地形成并发展，是中国共产党领导下军民关系的生动体现。'
      },
      {
        title: '红色传承',
        corePoints: [
          '赓续红色血脉',
          '弘扬革命精神',
          '践行初心使命'
        ],
        content: `红色传承以包括西柏坡精神在内的革命精神为核心，通过教育、宣传、实践等方式，让红色基因融入新时代发展。传承红色精神，就是要学习革命先辈的坚定信念、奋斗精神和为民情怀。`,
        historicalBackground: '红色传承是新时代文化建设的重要内容，旨在通过保护红色资源、讲述红色故事，让革命精神代代相传。'
      }
    ]
  },

  // 分类切换事件
  onChangeCategory(e) {
    const targetId = e.detail.value;
    this.setData({ activeCategory: targetId });
    
    Toast({
      context: this,
      selector: '#t-toast',
      message: `当前显示：${this.data.categories[targetId].name}`,
      duration: 1500,
      icon: 'info-circle'
    });
  },

  // 返回首页
  navToHome() {
    wx.redirectTo({
      url: '/pages/index/index'
    });
  },

  // 页面加载
  onLoad(options) {
    // 替换废弃的wx.getSystemInfoSync
    const windowInfo = wx.getWindowInfo();
    const deviceInfo = wx.getDeviceInfo();
    console.log('窗口信息', windowInfo);
    console.log('设备信息', deviceInfo);

    if (options.categoryId !== undefined) {
      const targetId = Number(options.categoryId);
      if (targetId >= 0 && targetId < this.data.categories.length) {
        this.setData({ activeCategory: targetId });
      }
    }
  }
});
