Component({
  externalClasses: ['custom-class'],

  properties: {
    category: {
      type: Array,
<<<<<<< HEAD
      value: []
=======
>>>>>>> 8a4f608c58ff387d6453daf1e3f9e1e5f76f2f7f
    },
    initActive: {
      type: Array,
      value: [],
      observer(newVal, oldVal) {
        if (newVal[0] !== oldVal[0]) {
          this.setActiveKey(newVal[0], 0);
        }
      },
    },
    isSlotRight: {
      type: Boolean,
      value: false,
    },
    level: {
      type: Number,
      value: 3,
    },
  },
  data: {
    activeKey: 0,
    subActiveKey: 0,
  },
  attached() {
<<<<<<< HEAD
    // 确保初始化时数据安全
=======
>>>>>>> 8a4f608c58ff387d6453daf1e3f9e1e5f76f2f7f
    if (this.properties.initActive && this.properties.initActive.length > 0) {
      this.setData({
        activeKey: this.properties.initActive[0],
        subActiveKey: this.properties.initActive[1] || 0,
      });
<<<<<<< HEAD
    } else if (this.properties.category.length > 0) {
      this.setData({
        activeKey: 0,
        subActiveKey: 0
      });
=======
>>>>>>> 8a4f608c58ff387d6453daf1e3f9e1e5f76f2f7f
    }
  },
  methods: {
    onParentChange(event) {
      this.setActiveKey(event.detail.index, 0).then(() => {
        this.triggerEvent('change', [this.data.activeKey, this.data.subActiveKey]);
      });
    },
    onChildChange(event) {
      this.setActiveKey(this.data.activeKey, event.detail.index).then(() => {
        this.triggerEvent('change', [this.data.activeKey, this.data.subActiveKey]);
      });
    },
    changCategory(event) {
      const { item } = event.currentTarget.dataset;
<<<<<<< HEAD
      // 确保item存在
      if (item) {
        this.triggerEvent('changeCategory', {
          item,
        });
      }
=======
      this.triggerEvent('changeCategory', {
        item,
      });
>>>>>>> 8a4f608c58ff387d6453daf1e3f9e1e5f76f2f7f
    },
    setActiveKey(key, subKey) {
      return new Promise((resolve) => {
        this.setData(
          {
            activeKey: key,
            subActiveKey: subKey,
          },
          () => {
            resolve();
          },
        );
      });
    },
  },
});
