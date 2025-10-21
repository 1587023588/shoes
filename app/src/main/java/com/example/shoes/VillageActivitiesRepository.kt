package com.example.shoes

import com.example.shoes.model.VillageActivity

object VillageActivitiesRepository {
    fun list(): List<VillageActivity> = listOf(
        VillageActivity(
            id = "va1",
            title = "农产品展销会",
            time = "2025-10-28 09:00",
            location = "村文化广场",
            brief = "集中展示本地农特产品，支持本土品牌，欢迎采购。",
            status = "报名中"
        ),
        VillageActivity(
            id = "va2",
            title = "非遗布鞋手作体验",
            time = "2025-11-03 14:00",
            location = "传承人工作坊",
            brief = "体验千层底布鞋的制作工艺，亲手上阵纳底缝合。",
            status = "进行中"
        ),
        VillageActivity(
            id = "va3",
            title = "冬季志愿清雪行动",
            time = "2025-12-10 08:30",
            location = "全村道路",
            brief = "志愿者分组清理路面积雪，保障出行安全。",
            status = "已结束"
        )
    )
}
