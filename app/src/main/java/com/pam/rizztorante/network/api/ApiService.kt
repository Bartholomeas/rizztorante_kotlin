package com.pam.rizztorante.network.api

import com.pam.rizztorante.model.LoginRequest
import com.pam.rizztorante.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
  @POST(Endpoints.LOGIN)
  suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
