package pl.agh.coronatracker.config

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
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

const val CONTENT_TYPE = "application/json"
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json {
        ignoreUnknownKeys = true
    }.asConverterFactory(CONTENT_TYPE.toMediaType()))
    .addConverterFactory(MoshiConverterFactory.create())
    .client(client)
    .build()

val coronaApi = retrofit.create(CoronaApi::class.java)