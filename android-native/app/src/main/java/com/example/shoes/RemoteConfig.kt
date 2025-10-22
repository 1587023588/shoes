package com.example.shoes

object RemoteConfig {
    // 来自小程序源码的 COS 链接
    // 首页海报（也作为视频封面兜底）
    const val bannerUrl: String = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/hero.jpg"
    // 首页视频
    const val homeVideoUrl: String = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes.mp4"
    // 首页视频海报
    const val homeVideoPoster: String = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/hero.jpg"

    // 底部栏图标（COS）
    const val tabHome: String = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/1.png"
    const val tabCategory: String = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/2.png"
    const val tabMessage: String = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/3.png"
    const val tabMine: String = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/img/4.png"

    // 聊天 WebSocket 地址（本地开发：Android 模拟器连宿主机用 10.0.2.2）
    // 生产环境请改为 wss://your-domain/ws/chat 并在网络安全策略中放行。
    const val chatWsUrl: String = "ws://10.0.2.2:8080/ws/chat"
}
