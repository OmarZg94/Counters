package com.mtg.counters.net

import com.google.gson.annotations.SerializedName
import com.mtg.counters.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

open class NetworkApi {

    companion object {
        private const val SERVICE_URL = "http://187.190.189.67:3000/"
    }

    fun getNetworkService(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val customClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
        /* Enable interceptor only in debug mode */
        if (BuildConfig.DEBUG) {
            customClient.addInterceptor(interceptor)
        }
        val clientBuilder = customClient.build()
        val builder = Retrofit.Builder().baseUrl(SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder).build()
        return builder.create(ApiService::class.java)
    }

    interface ApiService {
        @GET("api/v1/counters/")
        fun getCounters(): Call<MutableList<CounterResponse>>

        @POST("api/v1/counter/")
        fun saveCounter(@Body body: TitleRequest): Call<MutableList<CounterResponse>>

        @POST("api/v1/counter/inc/")
        fun incrementCounter(@Body body: IdRequest): Call<MutableList<CounterResponse>>

        @POST("api/v1/counter/dec/")
        fun decrementCounter(@Body body: IdRequest): Call<MutableList<CounterResponse>>

        @DELETE("api/v1/counter/")
        fun deleteCounter(@Body body: IdRequest): Call<MutableList<CounterResponse>>
    }
}

/* Request Catalogs */
data class TitleRequest(@SerializedName("title") val title: String)
data class IdRequest(@SerializedName("id") val id: String)

/* Response Catalogs */
data class CounterResponse(@SerializedName("id") val id: String,
                           @SerializedName("title") val title: String,
                           @SerializedName("count") val count: Long)