package com.pam.rizztorante.network.api

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
  private val okHttpClient =
          OkHttpClient.Builder()
                  .addInterceptor(
                          HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                          }
                  )
                  .connectTimeout(Endpoints.CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                  .readTimeout(Endpoints.RECEIVE_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                  .build()

  private val retrofit =
          Retrofit.Builder()
                  .baseUrl(Endpoints.BASE_URL)
                  .client(okHttpClient)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build()

  val apiService: ApiService = retrofit.create(ApiService::class.java)
}
