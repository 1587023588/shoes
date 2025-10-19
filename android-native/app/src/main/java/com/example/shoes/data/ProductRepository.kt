package com.example.shoes.data

import com.example.shoes.model.Product
import com.example.shoes.R

object ProductRepository {
    fun list(): List<Product> = listOf(
        Product(
            id = "1",
            name = "千层底布鞋·经典黑",
            price = 129.0,
            oldPrice = 199.0,
            stock = 20,
            craft = "手工纳底",
            material = "棉布+麻底",
            sizeRange = "35-44",
            images = listOf(R.drawable.goods_1, R.drawable.goods_2, R.drawable.goods_3),
            intro = "精选棉布，手工纳底，透气舒适",
            descImages = listOf(R.drawable.goods_4, R.drawable.goods_5),
            primaryImageUrl = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_1.png",
            imagesUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_1.png"
            ),
            descImageUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_datail/goods_detail.jpg"
            )
        ),
        Product(
            id = "2",
            name = "千层底布鞋·刺绣款",
            price = 159.0,
            oldPrice = 229.0,
            stock = 8,
            craft = "刺绣纹样",
            material = "棉布+牛筋底",
            sizeRange = "35-42",
            images = listOf(R.drawable.goods_2, R.drawable.goods_3, R.drawable.goods_4),
            intro = "手工刺绣，精致纹样",
            descImages = listOf(R.drawable.goods_6, R.drawable.goods_7),
            primaryImageUrl = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_2.jpg",
            imagesUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_2.jpg"
            ),
            descImageUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_datail/goods_detail.jpg"
            )
        ),
        Product(
            id = "3",
            name = "千层底布鞋·麻底",
            price = 139.0,
            oldPrice = 189.0,
            stock = 15,
            craft = "麻线纳底",
            material = "棉布+麻底",
            sizeRange = "36-45",
            images = listOf(R.drawable.goods_3, R.drawable.goods_4, R.drawable.goods_5),
            intro = "轻便耐穿，夏季首选",
            descImages = listOf(R.drawable.goods_8, R.drawable.goods_9),
            primaryImageUrl = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_3.jpg",
            imagesUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_3.jpg"
            ),
            descImageUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_datail/goods_detail.jpg"
            )
        ),
        Product(
            id = "4",
            name = "千层底布鞋·布面",
            price = 119.0,
            oldPrice = 169.0,
            stock = 3,
            craft = "传统工艺",
            material = "棉布+橡胶底",
            sizeRange = "35-43",
            images = listOf(R.drawable.goods_4, R.drawable.goods_5, R.drawable.goods_6),
            intro = "经典百搭，柔软贴合",
            descImages = listOf(R.drawable.goods_1, R.drawable.goods_2),
            primaryImageUrl = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_4.jpg",
            imagesUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_4.jpg"
            ),
            descImageUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_datail/goods_detail.jpg"
            )
        ),
        Product(
            id = "5",
            name = "布鞋·童款",
            price = 99.0,
            oldPrice = 129.0,
            stock = 12,
            craft = "软底透气",
            material = "棉布+软底",
            sizeRange = "28-35",
            images = listOf(R.drawable.goods_5, R.drawable.goods_6, R.drawable.goods_7),
            intro = "轻盈舒适，呵护童足",
            descImages = listOf(R.drawable.goods_3, R.drawable.goods_4),
            primaryImageUrl = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_5.jpg",
            imagesUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_5.jpg"
            ),
            descImageUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_datail/goods_detail.jpg"
            )
        ),
        Product(
            id = "6",
            name = "布鞋·文创联名",
            price = 199.0,
            oldPrice = 269.0,
            stock = 5,
            craft = "非遗纹样",
            material = "帆布+复古底",
            sizeRange = "36-44",
            images = listOf(R.drawable.goods_6, R.drawable.goods_7, R.drawable.goods_8),
            intro = "联名限定，收藏之选",
            descImages = listOf(R.drawable.goods_9, R.drawable.goods_10),
            primaryImageUrl = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_6.jpg",
            imagesUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_6.jpg"
            ),
            descImageUrls = listOf(
                "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/goods_primaryImage/goods_datail/goods_detail.jpg"
            )
        )
    )

    fun getById(id: String): Product? = list().find { it.id == id }
}
