names_and_res = [
    ("经典纯色男款——黑色款", "goods_1"),
    ("经典纯色男款——深藏蓝款", "goods_2"),
    ("经典纯色男款——深灰款", "goods_3"),
    ("经典纯色男款——军绿灰款", "goods_4"),
    ("经典纯色女款——黑色款", "goods_5"),
    ("经典纯色女款——酒红款", "goods_6"),
    ("经典纯色女款——枣红款", "goods_7"),
    ("经典纯色女款——深紫红款", "goods_8"),
    ("艾草养生款——艾草绿主色款", "goods_9"),
    ("艾草养生款——灰绿色款", "goods_10"),
    ("艾草养生款——米白款", "goods_11"),
    ("艾草养生款——亚麻棕款", "goods_12"),
    ("适老安全款——深棕款", "goods_13"),
    ("适老安全款——黑色款", "goods_14"),
    ("适老安全款——藏蓝款", "goods_15"),
    ("适老安全款——深酒红款", "goods_16"),
    ("廿四福履——春款", "goods_17"),
    ("廿四福履——夏款", "goods_18"),
    ("廿四福履——秋款", "goods_19"),
    ("廿四福履——冬款", "goods_20"),
    ("令节清履——春节款", "goods_21"),
    ("令节清履——端午款", "goods_22"),
    ("令节清履——中秋款", "goods_23"),
    ("令节清履——重阳款", "goods_24"),
    ("个性化定制系列——东方雅奢·月白金款", "goods_25"),
    ("个性化定制系列——东方雅奢·玉青藕粉款", "goods_26"),
    ("个性化定制系列——高定深色·黑金款", "goods_27"),
    ("个性化定制系列——高定深色·墨蓝暗红款", "goods_28"),
    ("外事礼品 / 出海系列——中国礼物线·中国红", "goods_29"),
    ("外事礼品 / 出海系列——中国礼物线·宋锦蓝款", "goods_30"),
    ("外事礼品 / 出海系列——严寒保暖线·雪白冰蓝款", "goods_31"),
    ("外事礼品 / 出海系列——严寒保暖线·雪白冰蓝款(二)", "goods_31_2"),
    ("外事礼品 / 出海系列——严寒保暖线·深藏蓝暖棕款", "goods_32"),
    ("外事礼品 / 出海系列——主款·青霭葳蕤色", "goods_32_2"),
    ("外事礼品 / 出海系列——暖款·绒暖福泽色", "goods_32_3"),
    ("外事礼品 / 出海系列——雅款·墨竹清妍色", "goods_32_4")
]

prices = [129.0, 159.0, 139.0, 119.0, 99.0, 199.0]
stocks = [20, 8, 15, 3, 12, 5, 22, 10, 10, 14]

out_str = []
out_str.append('package com.example.shoes.data')
out_str.append('import com.example.shoes.model.Product')
out_str.append('import com.example.shoes.R')
out_str.append('object ProductRepository {')
out_str.append('    fun list(): List<Product> = listOf(')

for idx, (name, res) in enumerate(names_and_res):
    price = prices[idx % len(prices)]
    stock = stocks[idx % len(stocks)]
    p_str = f'''        Product(
            id = "{idx+1}",
            name = "{name}",
            price = {price},
            oldPrice = {price + 50.0},
            stock = {stock},
            images = listOf(R.drawable.{res}),
            intro = "{name}，透气舒适",
            descImages = listOf(R.drawable.{res}),
            primaryImageUrl = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/{res}.png",
            imagesUrls = listOf("https://shoes-1379330878.cos.ap-beijing.myqcloud.com/{res}.png")
        )'''
    if idx < len(names_and_res) - 1:
        p_str += ","
    out_str.append(p_str)

out_str.append('    )')
out_str.append('    fun getById(id: String): Product? = list().find { it.id == id }')
out_str.append('}')

with open('app/src/main/java/com/example/shoes/data/ProductRepository.kt', 'w', encoding='utf-8') as f:
    f.write('\n'.join(out_str))
