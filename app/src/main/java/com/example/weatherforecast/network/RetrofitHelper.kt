package com.example.weatherforecast.network

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper{
//    private fun provideOkHttpClient(): OkHttpClient {
//        val httpClient = OkHttpClient.Builder()
//            .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
//            .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
//            .writeTimeout(30, TimeUnit.SECONDS)
//        httpClient.addInterceptor { chain ->
//            val original: Request = chain.request()
//            val originalHttpUrl: HttpUrl = original.url()
//            val url = originalHttpUrl.newBuilder()
//                .addQueryParameter("appid", "375d11598481406538e244d548560243")
//                .addQueryParameter("units", "metric")
//
//                .build()
//            val requestBuilder: Request.Builder = original.newBuilder()
//                .url(url)
//            val request: Request = requestBuilder.build()
//            chain.proceed(request)
//        }
//        return httpClient.build()
//    }
    private const val BASE_URL = "https://api.openweathermap.org/"
    private val retrofitInstance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        //.client(provideOkHttpClient())
        .build()
    val service = retrofitInstance.create(ApiService::class.java)

}
