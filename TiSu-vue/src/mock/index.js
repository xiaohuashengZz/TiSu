// ==================== 食物库 ====================
export const foodList = [
  // 官方食物
  { id: 1, name: '鸡胸肉', category: '肉类', calories: 165, protein: 31, carbs: 0, fat: 3.6, fiber: 0, unit: '100g', source: 'official' },
  { id: 2, name: '糙米饭', category: '主食', calories: 123, protein: 2.7, carbs: 25.6, fat: 0.9, fiber: 1.6, unit: '100g', source: 'official' },
  { id: 3, name: '西兰花', category: '蔬菜', calories: 34, protein: 2.8, carbs: 6.6, fat: 0.4, fiber: 2.6, unit: '100g', source: 'official' },
  { id: 4, name: '鸡蛋', category: '蛋类', calories: 144, protein: 13.3, carbs: 1.1, fat: 9.5, fiber: 0, unit: '100g', source: 'official' },
  { id: 5, name: '三文鱼', category: '海鲜', calories: 208, protein: 20.4, carbs: 0, fat: 13.4, fiber: 0, unit: '100g', source: 'official' },
  { id: 6, name: '红薯', category: '主食', calories: 86, protein: 1.6, carbs: 20.1, fat: 0.1, fiber: 3, unit: '100g', source: 'official' },
  { id: 7, name: '牛油果', category: '水果', calories: 160, protein: 2, carbs: 8.5, fat: 14.7, fiber: 6.7, unit: '100g', source: 'official' },
  { id: 8, name: '燕麦', category: '主食', calories: 389, protein: 16.9, carbs: 66.3, fat: 6.9, fiber: 10.6, unit: '100g', source: 'official' },
  { id: 9, name: '牛肉(瘦)', category: '肉类', calories: 106, protein: 20.2, carbs: 0, fat: 2.3, fiber: 0, unit: '100g', source: 'official' },
  { id: 10, name: '豆腐', category: '豆类', calories: 76, protein: 8.1, carbs: 1.9, fat: 3.7, fiber: 0.3, unit: '100g', source: 'official' },
  { id: 11, name: '香蕉', category: '水果', calories: 89, protein: 1.1, carbs: 22.8, fat: 0.3, fiber: 2.6, unit: '100g', source: 'official' },
  { id: 12, name: '菠菜', category: '蔬菜', calories: 23, protein: 2.9, carbs: 3.6, fat: 0.4, fiber: 2.2, unit: '100g', source: 'official' },
  { id: 13, name: '虾仁', category: '海鲜', calories: 99, protein: 24, carbs: 0.2, fat: 0.3, fiber: 0, unit: '100g', source: 'official' },
  { id: 14, name: '全麦面包', category: '主食', calories: 247, protein: 13, carbs: 41, fat: 3.4, fiber: 7, unit: '100g', source: 'official' },
  { id: 15, name: '希腊酸奶', category: '乳制品', calories: 97, protein: 9, carbs: 3.6, fat: 5, fiber: 0, unit: '100g', source: 'official' },
  { id: 16, name: '坚果混合', category: '零食', calories: 607, protein: 20, carbs: 21, fat: 54, fiber: 7, unit: '100g', source: 'official' },
  { id: 17, name: '生菜', category: '蔬菜', calories: 15, protein: 1.4, carbs: 2.9, fat: 0.2, fiber: 1.3, unit: '100g', source: 'official' },
  { id: 18, name: '胡萝卜', category: '蔬菜', calories: 41, protein: 0.9, carbs: 9.6, fat: 0.2, fiber: 2.8, unit: '100g', source: 'official' },
  // 用户自创食物
  { id: 19, name: '荞麦面', category: '主食', calories: 134, protein: 4.5, carbs: 26, fat: 1.1, fiber: 1.8, unit: '100g', source: 'user' },
  { id: 20, name: '鸡腿肉', category: '肉类', calories: 181, protein: 26, carbs: 0, fat: 8, fiber: 0, unit: '100g', source: 'user' },
  { id: 21, name: '藜麦', category: '主食', calories: 120, protein: 4.4, carbs: 21.3, fat: 1.9, fiber: 2.8, unit: '100g', source: 'user' },
  { id: 22, name: '毛豆', category: '豆类', calories: 122, protein: 11.9, carbs: 8.9, fat: 5.2, fiber: 5.2, unit: '100g', source: 'user' },
]

// ==================== 菜品库 ====================
export const dishList = [
  // 官方推荐 - 早餐
  { id: 1, name: '牛奶燕麦粥', description: '高纤维低GI早餐，提供持久饱腹感', calories: 250, category: '早餐', source: 'official', ingredients: [{ name: '燕麦', amount: 50, unit: 'g' }, { name: '牛奶', amount: 250, unit: 'ml' }] },
  { id: 2, name: '水煮蛋', description: '优质蛋白质来源，简单易做', calories: 144, category: '早餐', source: 'official', ingredients: [{ name: '鸡蛋', amount: 2, unit: '个' }] },
  { id: 3, name: '牛油果吐司', description: '健康脂肪与复合碳水的完美组合', calories: 315, category: '早餐', source: 'official', ingredients: [{ name: '全麦面包', amount: 60, unit: 'g' }, { name: '牛油果', amount: 50, unit: 'g' }, { name: '鸡蛋', amount: 1, unit: '个' }] },
  { id: 4, name: '希腊酸奶碗', description: '高蛋白低脂，搭配水果更佳', calories: 146, category: '早餐', source: 'official', ingredients: [{ name: '希腊酸奶', amount: 150, unit: 'g' }] },
  // 官方推荐 - 午餐
  { id: 5, name: '香煎鸡胸肉', description: '高蛋白低脂的健身餐首选', calories: 265, category: '午餐', source: 'official', ingredients: [{ name: '鸡胸肉', amount: 150, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
  { id: 6, name: '青椒炒牛肉', description: '富含铁质和蛋白质', calories: 218, category: '午餐', source: 'official', ingredients: [{ name: '牛肉', amount: 150, unit: 'g' }, { name: '青椒', amount: 50, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
  { id: 7, name: '西兰花炒牛肉', description: '高蛋白高纤维，营养均衡', calories: 225, category: '午餐', source: 'official', ingredients: [{ name: '牛肉', amount: 150, unit: 'g' }, { name: '西兰花', amount: 100, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
  { id: 8, name: '照烧鸡胸肉', description: '日式风味，低脂美味', calories: 280, category: '午餐', source: 'official', ingredients: [{ name: '鸡胸肉', amount: 150, unit: 'g' }, { name: '酱油', amount: 10, unit: 'ml' }, { name: '蜂蜜', amount: 5, unit: 'ml' }] },
  { id: 9, name: '糙米饭', description: '低GI主食，富含膳食纤维', calories: 185, category: '通用', source: 'official', ingredients: [{ name: '糙米', amount: 80, unit: 'g' }] },
  { id: 10, name: '清炒西兰花', description: '高纤维低热量蔬菜', calories: 58, category: '通用', source: 'official', ingredients: [{ name: '西兰花', amount: 150, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
  // 官方推荐 - 晚餐
  { id: 11, name: '香煎三文鱼', description: '富含Omega-3脂肪酸', calories: 270, category: '晚餐', source: 'official', ingredients: [{ name: '三文鱼', amount: 120, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
  { id: 12, name: '柠香烤三文鱼', description: '清新柠檬风味，低脂高蛋白', calories: 260, category: '晚餐', source: 'official', ingredients: [{ name: '三文鱼', amount: 120, unit: 'g' }, { name: '柠檬', amount: 1, unit: '个' }] },
  { id: 13, name: '清炒虾仁', description: '高蛋白低脂海鲜菜品', calories: 155, category: '晚餐', source: 'official', ingredients: [{ name: '虾仁', amount: 150, unit: 'g' }, { name: '橄榄油', amount: 3, unit: 'ml' }] },
  { id: 14, name: '家常豆腐', description: '植物蛋白丰富，口感嫩滑', calories: 130, category: '晚餐', source: 'official', ingredients: [{ name: '豆腐', amount: 150, unit: 'g' }, { name: '酱油', amount: 5, unit: 'ml' }] },
  { id: 15, name: '土豆炖牛肉', description: '经典家常菜，饱腹感强', calories: 260, category: '晚餐', source: 'official', ingredients: [{ name: '牛肉', amount: 150, unit: 'g' }, { name: '土豆', amount: 100, unit: 'g' }] },
  { id: 16, name: '白灼鸡胸肉', description: '清淡少油，保留原味', calories: 248, category: '晚餐', source: 'official', ingredients: [{ name: '鸡胸肉', amount: 150, unit: 'g' }, { name: '姜', amount: 5, unit: 'g' }] },
  { id: 17, name: '烤红薯', description: '低GI优质碳水', calories: 150, category: '通用', source: 'official', ingredients: [{ name: '红薯', amount: 200, unit: 'g' }] },
  { id: 18, name: '蒜蓉菠菜', description: '富含铁质和维生素', calories: 30, category: '通用', source: 'official', ingredients: [{ name: '菠菜', amount: 120, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
  { id: 19, name: '蔬菜沙拉', description: '低热量高纤维', calories: 45, category: '通用', source: 'official', ingredients: [{ name: '生菜', amount: 80, unit: 'g' }, { name: '胡萝卜', amount: 30, unit: 'g' }] },
  { id: 20, name: '凉拌生菜', description: '清爽开胃，低热量', calories: 30, category: '通用', source: 'official', ingredients: [{ name: '生菜', amount: 100, unit: 'g' }, { name: '醋', amount: 5, unit: 'ml' }] },
  { id: 21, name: '清炒胡萝卜丝', description: '富含β-胡萝卜素', calories: 55, category: '通用', source: 'official', ingredients: [{ name: '胡萝卜', amount: 100, unit: 'g' }, { name: '橄榄油', amount: 3, unit: 'ml' }] },
  // 官方推荐 - 加餐
  { id: 22, name: '酸奶坚果碗', description: '蛋白质与健康脂肪的完美加餐', calories: 267, category: '加餐', source: 'official', ingredients: [{ name: '希腊酸奶', amount: 150, unit: 'g' }, { name: '混合坚果', amount: 20, unit: 'g' }] },
  { id: 23, name: '坚果小食', description: '便携高能加餐', calories: 121, category: '加餐', source: 'official', ingredients: [{ name: '混合坚果', amount: 20, unit: 'g' }] },
  { id: 24, name: '香蕉', description: '快速补充能量', calories: 90, category: '加餐', source: 'official', ingredients: [{ name: '香蕉', amount: 1, unit: '根' }] },
  // 用户自创菜品
  { id: 25, name: '番茄鸡蛋面', description: '家常快手面，酸甜可口', calories: 380, category: '午餐', source: 'user', ingredients: [{ name: '面条', amount: 100, unit: 'g' }, { name: '鸡蛋', amount: 2, unit: '个' }, { name: '番茄', amount: 150, unit: 'g' }] },
  { id: 26, name: '紫薯燕麦奶昔', description: '高纤维早餐奶昔', calories: 220, category: '早餐', source: 'user', ingredients: [{ name: '紫薯', amount: 100, unit: 'g' }, { name: '燕麦', amount: 30, unit: 'g' }, { name: '牛奶', amount: 200, unit: 'ml' }] },
  { id: 27, name: '鸡胸肉沙拉', description: '低脂高蛋白减脂餐', calories: 290, category: '午餐', source: 'user', ingredients: [{ name: '鸡胸肉', amount: 120, unit: 'g' }, { name: '生菜', amount: 100, unit: 'g' }, { name: '牛油果', amount: 30, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
]

// ==================== 一周菜单 ====================
export const weekPlan = [
  {
    day: '周一',
    meals: [
      { type: '早餐', dishes: [
        { name: '牛奶燕麦粥', calories: 250, ingredients: [{ name: '燕麦', amount: 50, unit: 'g' }, { name: '牛奶', amount: 250, unit: 'ml' }] },
        { name: '水煮蛋', calories: 144, ingredients: [{ name: '鸡蛋', amount: 2, unit: '个' }] },
        { name: '香蕉', calories: 90, ingredients: [{ name: '香蕉', amount: 1, unit: '根' }] }
      ]},
      { type: '午餐', dishes: [
        { name: '香煎鸡胸肉', calories: 265, ingredients: [{ name: '鸡胸肉', amount: 150, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
        { name: '清炒西兰花', calories: 58, ingredients: [{ name: '西兰花', amount: 150, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
        { name: '糙米饭', calories: 185, ingredients: [{ name: '糙米', amount: 80, unit: 'g' }] }
      ]},
      { type: '晚餐', dishes: [
        { name: '香煎三文鱼', calories: 270, ingredients: [{ name: '三文鱼', amount: 120, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
        { name: '烤红薯', calories: 150, ingredients: [{ name: '红薯', amount: 200, unit: 'g' }] },
        { name: '蔬菜沙拉', calories: 45, ingredients: [{ name: '生菜', amount: 80, unit: 'g' }, { name: '胡萝卜', amount: 30, unit: 'g' }] }
      ]},
      { type: '加餐', dishes: [
        { name: '酸奶坚果碗', calories: 267, ingredients: [{ name: '希腊酸奶', amount: 150, unit: 'g' }, { name: '混合坚果', amount: 20, unit: 'g' }] }
      ]}
    ],
    totalCalories: 1429
  },
  {
    day: '周二',
    meals: [
      { type: '早餐', dishes: [
        { name: '牛油果吐司', calories: 315, ingredients: [{ name: '全麦面包', amount: 60, unit: 'g' }, { name: '牛油果', amount: 50, unit: 'g' }, { name: '鸡蛋', amount: 1, unit: '个' }] },
        { name: '水煮蛋', calories: 72, ingredients: [{ name: '鸡蛋', amount: 1, unit: '个' }] }
      ]},
      { type: '午餐', dishes: [
        { name: '青椒炒牛肉', calories: 218, ingredients: [{ name: '牛肉', amount: 150, unit: 'g' }, { name: '青椒', amount: 50, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
        { name: '蒜蓉菠菜', calories: 30, ingredients: [{ name: '菠菜', amount: 120, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
        { name: '糙米饭', calories: 185, ingredients: [{ name: '糙米', amount: 80, unit: 'g' }] }
      ]},
      { type: '晚餐', dishes: [
        { name: '清炒虾仁', calories: 155, ingredients: [{ name: '虾仁', amount: 150, unit: 'g' }, { name: '橄榄油', amount: 3, unit: 'ml' }] },
        { name: '家常豆腐', calories: 130, ingredients: [{ name: '豆腐', amount: 150, unit: 'g' }, { name: '酱油', amount: 5, unit: 'ml' }] },
        { name: '清炒胡萝卜丝', calories: 55, ingredients: [{ name: '胡萝卜', amount: 100, unit: 'g' }, { name: '橄榄油', amount: 3, unit: 'ml' }] }
      ]},
      { type: '加餐', dishes: [
        { name: '希腊酸奶', calories: 146, ingredients: [{ name: '希腊酸奶', amount: 150, unit: 'g' }] }
      ]}
    ],
    totalCalories: 1306
  },
  {
    day: '周三',
    meals: [
      { type: '早餐', dishes: [
        { name: '牛奶燕麦粥', calories: 250, ingredients: [{ name: '燕麦', amount: 50, unit: 'g' }, { name: '牛奶', amount: 250, unit: 'ml' }] },
        { name: '水煮蛋', calories: 144, ingredients: [{ name: '鸡蛋', amount: 2, unit: '个' }] },
        { name: '香蕉', calories: 90, ingredients: [{ name: '香蕉', amount: 1, unit: '根' }] }
      ]},
      { type: '午餐', dishes: [
        { name: '照烧鸡胸肉', calories: 280, ingredients: [{ name: '鸡胸肉', amount: 150, unit: 'g' }, { name: '酱油', amount: 10, unit: 'ml' }, { name: '蜂蜜', amount: 5, unit: 'ml' }] },
        { name: '清炒西兰花', calories: 58, ingredients: [{ name: '西兰花', amount: 150, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
        { name: '蒸红薯', calories: 129, ingredients: [{ name: '红薯', amount: 150, unit: 'g' }] }
      ]},
      { type: '晚餐', dishes: [
        { name: '柠香烤三文鱼', calories: 260, ingredients: [{ name: '三文鱼', amount: 120, unit: 'g' }, { name: '柠檬', amount: 1, unit: '个' }] },
        { name: '蒜蓉菠菜', calories: 30, ingredients: [{ name: '菠菜', amount: 120, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
        { name: '糙米饭', calories: 123, ingredients: [{ name: '糙米', amount: 60, unit: 'g' }] }
      ]},
      { type: '加餐', dishes: [
        { name: '坚果小食', calories: 121, ingredients: [{ name: '混合坚果', amount: 20, unit: 'g' }] }
      ]}
    ],
    totalCalories: 1285
  },
  {
    day: '周四',
    meals: [
      { type: '早餐', dishes: [
        { name: '牛油果吐司', calories: 315, ingredients: [{ name: '全麦面包', amount: 60, unit: 'g' }, { name: '牛油果', amount: 50, unit: 'g' }, { name: '鸡蛋', amount: 1, unit: '个' }] },
        { name: '水煮蛋', calories: 72, ingredients: [{ name: '鸡蛋', amount: 1, unit: '个' }] }
      ]},
      { type: '午餐', dishes: [
        { name: '西兰花炒牛肉', calories: 225, ingredients: [{ name: '牛肉', amount: 150, unit: 'g' }, { name: '西兰花', amount: 100, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
        { name: '糙米饭', calories: 185, ingredients: [{ name: '糙米', amount: 80, unit: 'g' }] }
      ]},
      { type: '晚餐', dishes: [
        { name: '白灼鸡胸肉', calories: 248, ingredients: [{ name: '鸡胸肉', amount: 150, unit: 'g' }, { name: '姜', amount: 5, unit: 'g' }] },
        { name: '凉拌生菜', calories: 30, ingredients: [{ name: '生菜', amount: 100, unit: 'g' }, { name: '醋', amount: 5, unit: 'ml' }] }
      ]},
      { type: '加餐', dishes: [
        { name: '酸奶香蕉碗', calories: 236, ingredients: [{ name: '希腊酸奶', amount: 150, unit: 'g' }, { name: '香蕉', amount: 1, unit: '根' }] }
      ]}
    ],
    totalCalories: 1311
  },
  {
    day: '周五',
    meals: [
      { type: '早餐', dishes: [
        { name: '牛奶燕麦粥', calories: 250, ingredients: [{ name: '燕麦', amount: 50, unit: 'g' }, { name: '牛奶', amount: 250, unit: 'ml' }] },
        { name: '水煮蛋', calories: 144, ingredients: [{ name: '鸡蛋', amount: 2, unit: '个' }] },
        { name: '香蕉', calories: 90, ingredients: [{ name: '香蕉', amount: 1, unit: '根' }] }
      ]},
      { type: '午餐', dishes: [
        { name: '清炒虾仁', calories: 155, ingredients: [{ name: '虾仁', amount: 150, unit: 'g' }, { name: '橄榄油', amount: 3, unit: 'ml' }] },
        { name: '清炒胡萝卜丝', calories: 55, ingredients: [{ name: '胡萝卜', amount: 100, unit: 'g' }, { name: '橄榄油', amount: 3, unit: 'ml' }] },
        { name: '糙米饭', calories: 185, ingredients: [{ name: '糙米', amount: 80, unit: 'g' }] }
      ]},
      { type: '晚餐', dishes: [
        { name: '香煎三文鱼', calories: 270, ingredients: [{ name: '三文鱼', amount: 120, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
        { name: '烤红薯', calories: 150, ingredients: [{ name: '红薯', amount: 200, unit: 'g' }] },
        { name: '蒜蓉菠菜', calories: 30, ingredients: [{ name: '菠菜', amount: 120, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] }
      ]},
      { type: '加餐', dishes: [
        { name: '坚果小食', calories: 121, ingredients: [{ name: '混合坚果', amount: 20, unit: 'g' }] }
      ]}
    ],
    totalCalories: 1450
  },
  {
    day: '周六',
    meals: [
      { type: '早餐', dishes: [
        { name: '牛油果吐司', calories: 315, ingredients: [{ name: '全麦面包', amount: 60, unit: 'g' }, { name: '牛油果', amount: 50, unit: 'g' }, { name: '鸡蛋', amount: 1, unit: '个' }] },
        { name: '水煮蛋', calories: 72, ingredients: [{ name: '鸡蛋', amount: 1, unit: '个' }] }
      ]},
      { type: '午餐', dishes: [
        { name: '香煎鸡胸肉', calories: 265, ingredients: [{ name: '鸡胸肉', amount: 150, unit: 'g' }, { name: '橄榄油', amount: 5, unit: 'ml' }] },
        { name: '清炒西兰花', calories: 58, ingredients: [{ name: '西兰花', amount: 150, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
        { name: '糙米饭', calories: 185, ingredients: [{ name: '糙米', amount: 80, unit: 'g' }] }
      ]},
      { type: '晚餐', dishes: [
        { name: '土豆炖牛肉', calories: 260, ingredients: [{ name: '牛肉', amount: 150, unit: 'g' }, { name: '土豆', amount: 100, unit: 'g' }] },
        { name: '凉拌生菜', calories: 30, ingredients: [{ name: '生菜', amount: 100, unit: 'g' }, { name: '醋', amount: 5, unit: 'ml' }] }
      ]},
      { type: '加餐', dishes: [
        { name: '希腊酸奶', calories: 146, ingredients: [{ name: '希腊酸奶', amount: 150, unit: 'g' }] }
      ]}
    ],
    totalCalories: 1331
  },
  {
    day: '周日',
    meals: [
      { type: '早餐', dishes: [
        { name: '牛奶燕麦粥', calories: 250, ingredients: [{ name: '燕麦', amount: 50, unit: 'g' }, { name: '牛奶', amount: 250, unit: 'ml' }] },
        { name: '水煮蛋', calories: 144, ingredients: [{ name: '鸡蛋', amount: 2, unit: '个' }] },
        { name: '香蕉', calories: 90, ingredients: [{ name: '香蕉', amount: 1, unit: '根' }] }
      ]},
      { type: '午餐', dishes: [
        { name: '柠香烤三文鱼', calories: 260, ingredients: [{ name: '三文鱼', amount: 120, unit: 'g' }, { name: '柠檬', amount: 1, unit: '个' }] },
        { name: '蒜蓉菠菜', calories: 30, ingredients: [{ name: '菠菜', amount: 120, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] },
        { name: '糙米饭', calories: 185, ingredients: [{ name: '糙米', amount: 80, unit: 'g' }] }
      ]},
      { type: '晚餐', dishes: [
        { name: '清炒虾仁', calories: 155, ingredients: [{ name: '虾仁', amount: 150, unit: 'g' }, { name: '橄榄油', amount: 3, unit: 'ml' }] },
        { name: '家常豆腐', calories: 130, ingredients: [{ name: '豆腐', amount: 150, unit: 'g' }, { name: '酱油', amount: 5, unit: 'ml' }] },
        { name: '清炒西兰花', calories: 58, ingredients: [{ name: '西兰花', amount: 150, unit: 'g' }, { name: '蒜', amount: 5, unit: 'g' }] }
      ]},
      { type: '加餐', dishes: [
        { name: '坚果小食', calories: 121, ingredients: [{ name: '混合坚果', amount: 20, unit: 'g' }] }
      ]}
    ],
    totalCalories: 1423
  }
]

// ==================== 动作库 ====================
export const exerciseList = [
  { id: 1, name: '卧推', muscleGroup: '胸', difficulty: '中级', description: '平躺于卧推凳，双手握杠铃，推起至手臂伸直后缓慢下放。', sets: 4, reps: 10 },
  { id: 2, name: '深蹲', muscleGroup: '腿', difficulty: '中级', description: '双脚与肩同宽，屈膝下蹲至大腿平行地面后站起。', sets: 4, reps: 10 },
  { id: 3, name: '硬拉', muscleGroup: '背', difficulty: '高级', description: '双脚与肩同宽，弯腰握杠铃，挺直腰背站起。', sets: 4, reps: 8 },
  { id: 4, name: '引体向上', muscleGroup: '背', difficulty: '中级', description: '双手正握杠，身体上拉至下巴过杠后缓慢下放。', sets: 3, reps: 8 },
  { id: 5, name: '肩推', muscleGroup: '肩', difficulty: '中级', description: '坐姿，双手持哑铃从肩部推至头顶伸直。', sets: 4, reps: 10 },
  { id: 6, name: '弯举', muscleGroup: '臂', difficulty: '初级', description: '站立，双手持哑铃，以肘为轴弯举至肩前。', sets: 3, reps: 12 },
  { id: 7, name: '俯卧撑', muscleGroup: '胸', difficulty: '初级', description: '双手撑地，身体下压至胸部接近地面后推起。', sets: 3, reps: 15 },
  { id: 8, name: '平板支撑', muscleGroup: '核心', difficulty: '初级', description: '双肘撑地，身体保持一条直线，核心收紧。', sets: 3, reps: 60 },
  { id: 9, name: '腿举', muscleGroup: '腿', difficulty: '初级', description: '坐于腿举机上，双脚蹬踏板至腿伸直。', sets: 4, reps: 12 },
  { id: 10, name: '飞鸟', muscleGroup: '胸', difficulty: '中级', description: '平躺，双手持哑铃向两侧打开后合拢至胸前。', sets: 3, reps: 12 },
  { id: 11, name: '划船', muscleGroup: '背', difficulty: '中级', description: '俯身，双手持哑铃向腰部方向拉起。', sets: 4, reps: 10 },
  { id: 12, name: '侧平举', muscleGroup: '肩', difficulty: '初级', description: '站立，双手持哑铃向两侧平举至肩高。', sets: 3, reps: 15 },
  { id: 13, name: '跑步', muscleGroup: '有氧', difficulty: '初级', description: '中等配速持续跑步，保持心率在有氧区间。', sets: 1, reps: 30 },
  { id: 14, name: '跳绳', muscleGroup: '有氧', difficulty: '初级', description: '双脚交替或并脚跳跃，保持节奏。', sets: 3, reps: 100 },
]

// ==================== 训练计划 ====================
export const trainingPlans = [
  {
    id: 1,
    name: '新手减脂计划',
    description: '适合健身新手的4周减脂训练计划，以复合动作为主，搭配有氧训练。',
    duration: 28,
    level: '初级',
    days: [
      { day: 1, label: '胸+三头', exercises: [{ name: '卧推', sets: 4, reps: 10, weight: '40kg' }, { name: '飞鸟', sets: 3, reps: 12, weight: '10kg' }, { name: '俯卧撑', sets: 3, reps: 15, weight: '自重' }] },
      { day: 2, label: '背+二头', exercises: [{ name: '引体向上', sets: 3, reps: 8, weight: '自重' }, { name: '划船', sets: 4, reps: 10, weight: '20kg' }, { name: '弯举', sets: 3, reps: 12, weight: '10kg' }] },
      { day: 3, label: '休息日', exercises: [] },
      { day: 4, label: '腿+核心', exercises: [{ name: '深蹲', sets: 4, reps: 10, weight: '50kg' }, { name: '腿举', sets: 4, reps: 12, weight: '80kg' }, { name: '平板支撑', sets: 3, reps: 60, weight: '自重' }] },
      { day: 5, label: '肩+有氧', exercises: [{ name: '肩推', sets: 4, reps: 10, weight: '15kg' }, { name: '侧平举', sets: 3, reps: 15, weight: '5kg' }, { name: '跑步', sets: 1, reps: 30, weight: '-' }] },
      { day: 6, label: '有氧日', exercises: [{ name: '跑步', sets: 1, reps: 40, weight: '-' }, { name: '跳绳', sets: 3, reps: 100, weight: '-' }] },
      { day: 7, label: '休息日', exercises: [] }
    ]
  },
  {
    id: 2,
    name: '进阶增肌计划',
    description: '针对有一定基础的训练者，采用推拉腿分化训练。',
    duration: 56,
    level: '中级',
    days: [
      { day: 1, label: '推(胸/肩/三头)', exercises: [{ name: '卧推', sets: 5, reps: 5, weight: '70kg' }, { name: '肩推', sets: 4, reps: 8, weight: '25kg' }, { name: '飞鸟', sets: 3, reps: 12, weight: '15kg' }] },
      { day: 2, label: '拉(背/二头)', exercises: [{ name: '硬拉', sets: 5, reps: 5, weight: '100kg' }, { name: '引体向上', sets: 4, reps: 8, weight: '自重' }, { name: '弯举', sets: 3, reps: 10, weight: '15kg' }] },
      { day: 3, label: '腿', exercises: [{ name: '深蹲', sets: 5, reps: 5, weight: '80kg' }, { name: '腿举', sets: 4, reps: 10, weight: '120kg' }] },
      { day: 4, label: '休息日', exercises: [] },
      { day: 5, label: '推(胸/肩/三头)', exercises: [{ name: '卧推', sets: 4, reps: 8, weight: '65kg' }, { name: '肩推', sets: 4, reps: 10, weight: '20kg' }, { name: '侧平举', sets: 3, reps: 15, weight: '8kg' }] },
      { day: 6, label: '拉(背/二头)', exercises: [{ name: '划船', sets: 4, reps: 8, weight: '30kg' }, { name: '引体向上', sets: 4, reps: 10, weight: '自重' }, { name: '弯举', sets: 3, reps: 12, weight: '12kg' }] },
      { day: 7, label: '休息日', exercises: [] }
    ]
  }
]

// ==================== 训练日历数据 ====================
export const trainingCalendar = {
  '2026-05-01': { plan: '胸+三头', completed: true, duration: 65 },
  '2026-05-02': { plan: '背+二头', completed: true, duration: 58 },
  '2026-05-03': { plan: '休息日', completed: false, duration: 0 },
  '2026-05-04': { plan: '腿+核心', completed: true, duration: 72 },
  '2026-05-05': { plan: '肩+有氧', completed: false, duration: 0 },
  '2026-05-06': { plan: '有氧日', completed: false, duration: 0 },
  '2026-05-07': { plan: '休息日', completed: false, duration: 0 },
  '2026-05-08': { plan: '胸+三头', completed: false, duration: 0 },
  '2026-05-09': { plan: '背+二头', completed: false, duration: 0 },
  '2026-05-10': { plan: '休息日', completed: false, duration: 0 },
  '2026-05-11': { plan: '腿+核心', completed: false, duration: 0 },
  '2026-05-12': { plan: '肩+有氧', completed: false, duration: 0 },
  '2026-05-13': { plan: '有氧日', completed: false, duration: 0 },
  '2026-05-14': { plan: '休息日', completed: false, duration: 0 },
}

// ==================== 体重记录 ====================
export const weightLogs = [
  { date: '2026-04-01', weight: 78.5, bodyFat: 22.0 },
  { date: '2026-04-05', weight: 78.0, bodyFat: 21.8 },
  { date: '2026-04-10', weight: 77.3, bodyFat: 21.5 },
  { date: '2026-04-15', weight: 76.8, bodyFat: 21.2 },
  { date: '2026-04-20', weight: 76.2, bodyFat: 20.8 },
  { date: '2026-04-25', weight: 75.5, bodyFat: 20.5 },
  { date: '2026-04-30', weight: 75.0, bodyFat: 20.1 },
  { date: '2026-05-01', weight: 74.8, bodyFat: 19.8 },
  { date: '2026-05-02', weight: 74.5, bodyFat: 19.5 },
  { date: '2026-05-03', weight: 74.3, bodyFat: 19.3 },
  { date: '2026-05-04', weight: 72.0, bodyFat: 18.5 },
]

// ==================== 围度记录 ====================
export const bodyMeasurements = [
  { date: '2026-04-01', chest: 98, waist: 86, hip: 100, arm: 32, thigh: 56 },
  { date: '2026-04-08', chest: 98, waist: 85, hip: 99, arm: 32, thigh: 55.5 },
  { date: '2026-04-15', chest: 97.5, waist: 84, hip: 99, arm: 31.5, thigh: 55 },
  { date: '2026-04-22', chest: 97, waist: 83, hip: 98, arm: 31.5, thigh: 54.5 },
  { date: '2026-04-29', chest: 97, waist: 82, hip: 98, arm: 31, thigh: 54 },
  { date: '2026-05-04', chest: 96.5, waist: 81, hip: 97, arm: 31, thigh: 53.5 },
]

// ==================== AI 分析报告 ====================
export const aiReports = [
  {
    id: 1,
    title: '第1周健康分析报告',
    date: '2026-04-07',
    type: 'weekly',
    summary: '本周体重下降1.2kg，饮食热量控制良好，训练完成率85%。',
    content: `## 饮食分析
本周平均每日摄入热量约1400kcal，低于目标值2100kcal。蛋白质摄入充足，但碳水化合物略低。建议适当增加复合碳水摄入。

## 训练分析
本周完成5次训练，训练量适中。胸肌和背肌训练表现良好，腿部训练需要加强。

## 体重趋势
体重从78.5kg降至77.3kg，下降速度合理。体脂率从22.0%降至21.5%，脂肪减少明显。

## 建议
1. 每日增加100-200kcal碳水摄入
2. 腿部训练增加深蹲组数
3. 保持当前有氧频率`
  },
  {
    id: 2,
    title: '第2周健康分析报告',
    date: '2026-04-14',
    type: 'weekly',
    summary: '体重继续下降0.8kg，围度变化明显，训练强度有所提升。',
    content: `## 饮食分析
本周饮食结构改善，热量摄入稳定在1500kcal左右。蛋白质占比提高至30%，营养均衡性提升。

## 训练分析
训练完成率提升至100%，卧推重量提升5kg，深蹲提升10kg。力量进步明显。

## 体重趋势
体重从77.3kg降至76.5kg，腰围减少2cm，减脂效果显著。

## 建议
1. 可适当提高训练重量
2. 增加训练前的热身时间
3. 保持饮食计划的执行力`
  },
  {
    id: 3,
    title: '4月月度分析报告',
    date: '2026-04-30',
    type: 'monthly',
    summary: '本月减重3.5kg，体脂下降1.9%，各项围度均有改善。',
    content: `## 月度总览
本月体重从78.5kg降至75.0kg，共减重3.5kg。体脂率从22.0%降至20.1%，下降1.9个百分点。

## 饮食回顾
月均每日摄入热量约1450kcal，蛋白质日均摄入120g，表现优秀。周末饮食控制需要加强。

## 训练回顾
本月共完成22次训练，完成率78.6%。力量指标全面提升：
- 卧推：40kg → 50kg
- 深蹲：50kg → 65kg
- 硬拉：60kg → 80kg

## 身体围度变化
- 腰围：86cm → 82cm（-4cm）
- 胸围：98cm → 97cm（-1cm）
- 大腿：56cm → 54cm（-2cm）

## 下月建议
1. 目标减重2-3kg
2. 增加HIIT训练频率
3. 注意周末饮食控制
4. 适当增加蛋白质摄入至130g/日`
  }
]

// ==================== 管理后台 - 用户列表 ====================
export const adminUsers = [
  { id: 1, nickname: '健身达人', email: 'fitness@example.com', goal: '减脂', registerDate: '2026-03-15', status: 'active' },
  { id: 2, nickname: '减脂小王', email: 'wang@example.com', goal: '减脂', registerDate: '2026-03-20', status: 'active' },
  { id: 3, nickname: '增肌计划', email: 'muscle@example.com', goal: '增肌', registerDate: '2026-03-22', status: 'active' },
  { id: 4, nickname: '健康生活', email: 'health@example.com', goal: '维持', registerDate: '2026-04-01', status: 'active' },
  { id: 5, nickname: '跑步爱好者', email: 'runner@example.com', goal: '减脂', registerDate: '2026-04-05', status: 'active' },
  { id: 6, nickname: '塑形达人', email: 'shape@example.com', goal: '塑形', registerDate: '2026-04-10', status: 'disabled' },
  { id: 7, nickname: '力量训练', email: 'strength@example.com', goal: '增肌', registerDate: '2026-04-12', status: 'active' },
  { id: 8, nickname: '瑜伽爱好者', email: 'yoga@example.com', goal: '塑形', registerDate: '2026-04-15', status: 'active' },
  { id: 9, nickname: '新手小白', email: 'newbie@example.com', goal: '减脂', registerDate: '2026-04-20', status: 'disabled' },
  { id: 10, nickname: '蛋白粉达人', email: 'protein@example.com', goal: '增肌', registerDate: '2026-04-25', status: 'active' },
  { id: 11, nickname: '轻食主义', email: 'light@example.com', goal: '减脂', registerDate: '2026-04-28', status: 'active' },
  { id: 12, nickname: '健身教练', email: 'coach@example.com', goal: '维持', registerDate: '2026-05-01', status: 'active' }
]
