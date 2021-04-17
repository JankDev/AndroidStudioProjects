package pl.agh.coronatracker.config

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.agh.coronatracker.domain.CoronaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.covid19api.com/"

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BASIC
}

val client: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(loggingInterceptor)
}.build()


val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create())
    .client(client)
    .build()

val coronaApi = retrofit.create(CoronaApi::class.java)