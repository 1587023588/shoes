package com.example.shoes

import android.os.Build

object NetEnv {
    /**
     * 判断是否在 Android 模拟器运行（常见特征判断）。
     */
    fun isEmulator(): Boolean {
        val fp = Build.FINGERPRINT?.lowercase() ?: ""
        val model = Build.MODEL?.lowercase() ?: ""
        val brand = Build.BRAND?.lowercase() ?: ""
        val device = Build.DEVICE?.lowercase() ?: ""
        val hw = Build.HARDWARE?.lowercase() ?: ""
        val manufacturer = Build.MANUFACTURER?.lowercase() ?: ""
        return fp.startsWith("generic") || fp.contains("vbox") ||
                model.contains("emulator") || model.contains("sdk") ||
                brand.startsWith("generic") && device.startsWith("generic") ||
                hw.contains("goldfish") || hw.contains("ranchu") ||
                manufacturer.contains("genymotion")
    }

    /**
     * 本地开发后端的宿主机地址：
     * - 模拟器使用 10.0.2.2
     * - 真机使用 127.0.0.1（需配合 `adb reverse tcp:8080 tcp:8080`）
     */
    fun hostForLocalBackend(): String = if (isEmulator()) "10.0.2.2" else "127.0.0.1"
}
