package com.example.shoes.data
import com.example.shoes.model.Product
import com.example.shoes.R
object ProductRepository {
    fun list(): List<Product> = listOf(
        Product(
            id = "1",
            name = "经典纯色男款——黑色款",
            price = 129.0,
            oldPrice = 179.0,
            stock = 20,
            images = listOf(R.drawable.goods_1),
            intro = "经典纯色男款——黑色款，透气舒适",
            descImages = listOf(R.drawable.goods_1)
        ),
        Product(
            id = "2",
            name = "经典纯色男款——深藏蓝款",
            price = 159.0,
            oldPrice = 209.0,
            stock = 8,
            images = listOf(R.drawable.goods_2),
            intro = "经典纯色男款——深藏蓝款，透气舒适",
            descImages = listOf(R.drawable.goods_2)
        ),
        Product(
            id = "3",
            name = "经典纯色男款——深灰款",
            price = 139.0,
            oldPrice = 189.0,
            stock = 15,
            images = listOf(R.drawable.goods_3),
            intro = "经典纯色男款——深灰款，透气舒适",
            descImages = listOf(R.drawable.goods_3)
        ),
        Product(
            id = "4",
            name = "经典纯色男款——军绿灰款",
            price = 119.0,
            oldPrice = 169.0,
            stock = 3,
            images = listOf(R.drawable.goods_4),
            intro = "经典纯色男款——军绿灰款，透气舒适",
            descImages = listOf(R.drawable.goods_4)
        ),
        Product(
            id = "5",
            name = "经典纯色女款——黑色款",
            price = 99.0,
            oldPrice = 149.0,
            stock = 12,
            images = listOf(R.drawable.goods_5),
            intro = "经典纯色女款——黑色款，透气舒适",
            descImages = listOf(R.drawable.goods_5)
        ),
        Product(
            id = "6",
            name = "经典纯色女款——酒红款",
            price = 199.0,
            oldPrice = 249.0,
            stock = 5,
            images = listOf(R.drawable.goods_6),
            intro = "经典纯色女款——酒红款，透气舒适",
            descImages = listOf(R.drawable.goods_6)
        ),
        Product(
            id = "7",
            name = "经典纯色女款——枣红款",
            price = 129.0,
            oldPrice = 179.0,
            stock = 22,
            images = listOf(R.drawable.goods_7),
            intro = "经典纯色女款——枣红款，透气舒适",
            descImages = listOf(R.drawable.goods_7)
        ),
        Product(
            id = "8",
            name = "经典纯色女款——深紫红款",
            price = 159.0,
            oldPrice = 209.0,
            stock = 10,
            images = listOf(R.drawable.goods_8),
            intro = "经典纯色女款——深紫红款，透气舒适",
            descImages = listOf(R.drawable.goods_8)
        ),
        Product(
            id = "9",
            name = "艾草养生款——艾草绿主色款",
            price = 139.0,
            oldPrice = 189.0,
            stock = 10,
            images = listOf(R.drawable.goods_9),
            intro = "艾草养生款——艾草绿主色款，透气舒适",
            descImages = listOf(R.drawable.goods_9)
        ),
        Product(
            id = "10",
            name = "艾草养生款——灰绿色款",
            price = 119.0,
            oldPrice = 169.0,
            stock = 14,
            images = listOf(R.drawable.goods_10),
            intro = "艾草养生款——灰绿色款，透气舒适",
            descImages = listOf(R.drawable.goods_10)
        ),
        Product(
            id = "11",
            name = "艾草养生款——米白款",
            price = 99.0,
            oldPrice = 149.0,
            stock = 20,
            images = listOf(R.drawable.goods_11),
            intro = "艾草养生款——米白款，透气舒适",
            descImages = listOf(R.drawable.goods_11)
        ),
        Product(
            id = "12",
            name = "艾草养生款——亚麻棕款",
            price = 199.0,
            oldPrice = 249.0,
            stock = 8,
            images = listOf(R.drawable.goods_12),
            intro = "艾草养生款——亚麻棕款，透气舒适",
            descImages = listOf(R.drawable.goods_12)
        ),
        Product(
            id = "13",
            name = "适老安全款——深棕款",
            price = 129.0,
            oldPrice = 179.0,
            stock = 15,
            images = listOf(R.drawable.goods_13),
            intro = "适老安全款——深棕款，透气舒适",
            descImages = listOf(R.drawable.goods_13)
        ),
        Product(
            id = "14",
            name = "适老安全款——黑色款",
            price = 159.0,
            oldPrice = 209.0,
            stock = 3,
            images = listOf(R.drawable.goods_14),
            intro = "适老安全款——黑色款，透气舒适",
            descImages = listOf(R.drawable.goods_14)
        ),
        Product(
            id = "15",
            name = "适老安全款——藏蓝款",
            price = 139.0,
            oldPrice = 189.0,
            stock = 12,
            images = listOf(R.drawable.goods_15),
            intro = "适老安全款——藏蓝款，透气舒适",
            descImages = listOf(R.drawable.goods_15)
        ),
        Product(
            id = "16",
            name = "适老安全款——深酒红款",
            price = 119.0,
            oldPrice = 169.0,
            stock = 5,
            images = listOf(R.drawable.goods_16),
            intro = "适老安全款——深酒红款，透气舒适",
            descImages = listOf(R.drawable.goods_16)
        ),
        Product(
            id = "17",
            name = "廿四福履——春款",
            price = 99.0,
            oldPrice = 149.0,
            stock = 22,
            images = listOf(R.drawable.goods_17),
            intro = "廿四福履——春款，透气舒适",
            descImages = listOf(R.drawable.goods_17)
        ),
        Product(
            id = "18",
            name = "廿四福履——夏款",
            price = 199.0,
            oldPrice = 249.0,
            stock = 10,
            images = listOf(R.drawable.goods_18),
            intro = "廿四福履——夏款，透气舒适",
            descImages = listOf(R.drawable.goods_18)
        ),
        Product(
            id = "19",
            name = "廿四福履——秋款",
            price = 129.0,
            oldPrice = 179.0,
            stock = 10,
            images = listOf(R.drawable.goods_19),
            intro = "廿四福履——秋款，透气舒适",
            descImages = listOf(R.drawable.goods_19)
        ),
        Product(
            id = "20",
            name = "廿四福履——冬款",
            price = 159.0,
            oldPrice = 209.0,
            stock = 14,
            images = listOf(R.drawable.goods_20),
            intro = "廿四福履——冬款，透气舒适",
            descImages = listOf(R.drawable.goods_20)
        ),
        Product(
            id = "21",
            name = "令节清履——春节款",
            price = 139.0,
            oldPrice = 189.0,
            stock = 20,
            images = listOf(R.drawable.goods_21),
            intro = "令节清履——春节款，透气舒适",
            descImages = listOf(R.drawable.goods_21)
        ),
        Product(
            id = "22",
            name = "令节清履——端午款",
            price = 119.0,
            oldPrice = 169.0,
            stock = 8,
            images = listOf(R.drawable.goods_22),
            intro = "令节清履——端午款，透气舒适",
            descImages = listOf(R.drawable.goods_22)
        ),
        Product(
            id = "23",
            name = "令节清履——中秋款",
            price = 99.0,
            oldPrice = 149.0,
            stock = 15,
            images = listOf(R.drawable.goods_23),
            intro = "令节清履——中秋款，透气舒适",
            descImages = listOf(R.drawable.goods_23)
        ),
        Product(
            id = "24",
            name = "令节清履——重阳款",
            price = 199.0,
            oldPrice = 249.0,
            stock = 3,
            images = listOf(R.drawable.goods_24),
            intro = "令节清履——重阳款，透气舒适",
            descImages = listOf(R.drawable.goods_24)
        ),
        Product(
            id = "25",
            name = "个性化定制系列——东方雅奢·月白金款",
            price = 129.0,
            oldPrice = 179.0,
            stock = 12,
            images = listOf(R.drawable.goods_25),
            intro = "个性化定制系列——东方雅奢·月白金款，透气舒适",
            descImages = listOf(R.drawable.goods_25)
        ),
        Product(
            id = "26",
            name = "个性化定制系列——东方雅奢·玉青藕粉款",
            price = 159.0,
            oldPrice = 209.0,
            stock = 5,
            images = listOf(R.drawable.goods_26),
            intro = "个性化定制系列——东方雅奢·玉青藕粉款，透气舒适",
            descImages = listOf(R.drawable.goods_26)
        ),
        Product(
            id = "27",
            name = "个性化定制系列——高定深色·黑金款",
            price = 139.0,
            oldPrice = 189.0,
            stock = 22,
            images = listOf(R.drawable.goods_27),
            intro = "个性化定制系列——高定深色·黑金款，透气舒适",
            descImages = listOf(R.drawable.goods_27)
        ),
        Product(
            id = "28",
            name = "个性化定制系列——高定深色·墨蓝暗红款",
            price = 119.0,
            oldPrice = 169.0,
            stock = 10,
            images = listOf(R.drawable.goods_28),
            intro = "个性化定制系列——高定深色·墨蓝暗红款，透气舒适",
            descImages = listOf(R.drawable.goods_28)
        ),
        Product(
            id = "29",
            name = "外事礼品 / 出海系列——中国礼物线·中国红",
            price = 99.0,
            oldPrice = 149.0,
            stock = 10,
            images = listOf(R.drawable.goods_29),
            intro = "外事礼品 / 出海系列——中国礼物线·中国红，透气舒适",
            descImages = listOf(R.drawable.goods_29)
        ),
        Product(
            id = "30",
            name = "外事礼品 / 出海系列——中国礼物线·宋锦蓝款",
            price = 199.0,
            oldPrice = 249.0,
            stock = 14,
            images = listOf(R.drawable.goods_30),
            intro = "外事礼品 / 出海系列——中国礼物线·宋锦蓝款，透气舒适",
            descImages = listOf(R.drawable.goods_30)
        ),
        Product(
            id = "31",
            name = "外事礼品 / 出海系列——严寒保暖线·雪白冰蓝款",
            price = 129.0,
            oldPrice = 179.0,
            stock = 20,
            images = listOf(R.drawable.goods_31),
            intro = "外事礼品 / 出海系列——严寒保暖线·雪白冰蓝款，透气舒适",
            descImages = listOf(R.drawable.goods_31)
        ),
        Product(
            id = "32",
            name = "外事礼品 / 出海系列——严寒保暖线·雪白冰蓝款(二)",
            price = 159.0,
            oldPrice = 209.0,
            stock = 8,
            images = listOf(R.drawable.goods_31_2),
            intro = "外事礼品 / 出海系列——严寒保暖线·雪白冰蓝款(二)，透气舒适",
            descImages = listOf(R.drawable.goods_31_2)
        ),
        Product(
            id = "33",
            name = "外事礼品 / 出海系列——严寒保暖线·深藏蓝暖棕款",
            price = 139.0,
            oldPrice = 189.0,
            stock = 15,
            images = listOf(R.drawable.goods_32),
            intro = "外事礼品 / 出海系列——严寒保暖线·深藏蓝暖棕款，透气舒适",
            descImages = listOf(R.drawable.goods_32)
        ),
        Product(
            id = "34",
            name = "外事礼品 / 出海系列——主款·青霭葳蕤色",
            price = 119.0,
            oldPrice = 169.0,
            stock = 3,
            images = listOf(R.drawable.goods_32_2),
            intro = "外事礼品 / 出海系列——主款·青霭葳蕤色，透气舒适",
            descImages = listOf(R.drawable.goods_32_2)
        ),
        Product(
            id = "35",
            name = "外事礼品 / 出海系列——暖款·绒暖福泽色",
            price = 99.0,
            oldPrice = 149.0,
            stock = 12,
            images = listOf(R.drawable.goods_32_3),
            intro = "外事礼品 / 出海系列——暖款·绒暖福泽色，透气舒适",
            descImages = listOf(R.drawable.goods_32_3)
        ),
        Product(
            id = "36",
            name = "外事礼品 / 出海系列——雅款·墨竹清妍色",
            price = 199.0,
            oldPrice = 249.0,
            stock = 5,
            images = listOf(R.drawable.goods_32_4),
            intro = "外事礼品 / 出海系列——雅款·墨竹清妍色，透气舒适",
            descImages = listOf(R.drawable.goods_32_4)
        )
    )
    fun getById(id: String): Product? = list().find { it.id == id }
}