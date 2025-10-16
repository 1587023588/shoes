package com.example.shoes.data

import com.example.shoes.model.Product

object ProductRepository {
    fun list(): List<Product> = listOf(
        Product("1", "千层底布鞋·经典黑", 129.0, 20, craft = "手工纳底", material = "棉布+麻底", sizeRange = "35-44"),
        Product("2", "千层底布鞋·刺绣款", 159.0, 8, craft = "刺绣纹样", material = "棉布+牛筋底", sizeRange = "35-42"),
        Product("3", "千层底布鞋·麻底", 139.0, 15, craft = "麻线纳底", material = "棉布+麻底", sizeRange = "36-45"),
        Product("4", "千层底布鞋·布面", 119.0, 3, craft = "传统工艺", material = "棉布+橡胶底", sizeRange = "35-43"),
        Product("5", "布鞋·童款", 99.0, 12, craft = "软底透气", material = "棉布+软底", sizeRange = "28-35"),
        Product("6", "布鞋·文创联名", 199.0, 5, craft = "非遗纹样", material = "帆布+复古底", sizeRange = "36-44")
    )

    fun getById(id: String): Product? = list().find { it.id == id }
}
