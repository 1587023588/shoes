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

    fun get(tokenProvider: () -> String?): Retrofit {
        val currentBase = baseUrl()
        val existing = retrofit
        if (existing != null) return existing
        return synchronized(this) {
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
            instance
        }
    }
}
