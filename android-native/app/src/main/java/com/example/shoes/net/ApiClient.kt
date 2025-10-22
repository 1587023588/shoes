package com.example.shoes.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // 模拟器访问宿主机：10.0.2.2
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Volatile private var retrofit: Retrofit? = null

    fun get(tokenProvider: () -> String?): Retrofit {
        return retrofit ?: synchronized(this) {
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
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit = instance
            instance
        }
    }
}
