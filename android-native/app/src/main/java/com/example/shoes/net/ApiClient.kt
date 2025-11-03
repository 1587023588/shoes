package com.example.shoes.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.shoes.NetEnv

object ApiClient {
    // 根据运行环境自动选择：模拟器 10.0.2.2 / 真机 127.0.0.1（需 adb reverse）
    private fun baseUrl(): String = "http://${NetEnv.hostForLocalBackend()}:8080/"

    @Volatile private var retrofit: Retrofit? = null
    @Volatile private var lastBaseUrl: String? = null

    fun get(tokenProvider: () -> String?): Retrofit {
        val currentBase = baseUrl()
        val existing = retrofit
        // 如果已有实例且 base 未变化，直接复用；否则重建，解决从模拟器切到真机(或反之)后无法访问的问题
        if (existing != null && lastBaseUrl == currentBase) return existing
        return synchronized(this) {
            if (retrofit != null && lastBaseUrl == currentBase) return@synchronized retrofit!!
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            val auth = Interceptor { chain ->
                val reqBuilder = chain.request().newBuilder()
                tokenProvider()?.let { t -> reqBuilder.addHeader("Authorization", "Bearer $t") }
                chain.proceed(reqBuilder.build())
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(auth)
                .addInterceptor(logging)
                .build()
            val instance = Retrofit.Builder()
                .baseUrl(currentBase)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit = instance
            lastBaseUrl = currentBase
            instance
        }
    }

    fun invalidate() {
        synchronized(this) {
            retrofit = null
            lastBaseUrl = null
        }
    }
}
