package com.pam.rizztorante.network.api

import java.util.concurrent.TimeUnit
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
  private val client =
          OkHttpClient.Builder()
                  .cookieJar(
                          object : CookieJar {
                            private var cookies = listOf<Cookie>()

                            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                              this.cookies = cookies
                            }

                            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                              return cookies
                            }
                          }
                  )
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
                  .client(client)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build()

  val apiService: ApiService = retrofit.create(ApiService::class.java)
}
