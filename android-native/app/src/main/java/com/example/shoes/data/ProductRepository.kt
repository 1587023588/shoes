package com.example.shoes.data

import com.example.shoes.model.Product

object ProductRepository {
    fun list(): List<Product> = listOf(
        Product("1", "千层底布鞋·经典黑", 129.0, 20, craft = "手工纳底", material = "棉布+麻底", sizeRange = "35-44", thumbnail = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_1.png"),
        Product("2", "千层底布鞋·刺绣款", 159.0, 8, craft = "刺绣纹样", material = "棉布+牛筋底", sizeRange = "35-42", thumbnail = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_2.jpg"),
        Product("3", "千层底布鞋·麻底", 139.0, 15, craft = "麻线纳底", material = "棉布+麻底", sizeRange = "36-45", thumbnail = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_3.jpg"),
        Product("4", "千层底布鞋·布面", 119.0, 3, craft = "传统工艺", material = "棉布+橡胶底", sizeRange = "35-43", thumbnail = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_4.jpg"),
        Product("5", "布鞋·童款", 99.0, 12, craft = "软底透气", material = "棉布+软底", sizeRange = "28-35", thumbnail = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_5.jpg"),
        Product("6", "布鞋·文创联名", 199.0, 5, craft = "非遗纹样", material = "帆布+复古底", sizeRange = "36-44", thumbnail = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_6.jpg")
    )

    fun getById(id: String): Product? = list().find { it.id == id }
}
