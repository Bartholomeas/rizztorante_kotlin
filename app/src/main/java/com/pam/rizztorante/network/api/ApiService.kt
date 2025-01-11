package com.pam.rizztorante.network.api

import com.pam.rizztorante.model.AddToCartRequest
import com.pam.rizztorante.model.CartResponse
import com.pam.rizztorante.model.LoginRequest
import com.pam.rizztorante.model.LoginResponse
import com.pam.rizztorante.model.MenuCategoryResponse
import com.pam.rizztorante.model.MenuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST(Endpoints.LOGIN)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET(Endpoints.MENUS)
    suspend fun getMenus(): Response<List<MenuResponse>>

    @GET(Endpoints.MENU_CATEGORIES)
    suspend fun getMenuCategoriesWithItems(
        @Path("menuId") menuId: String,
    ): Response<List<MenuCategoryResponse>>

    @GET(Endpoints.CART)
    suspend fun getCart(): Response<CartResponse>

    @POST(Endpoints.ADD_CART_ITEM)
    suspend fun addToCart(@Body request: AddToCartRequest): Response<Unit>

    @DELETE(Endpoints.REMOVE_CART_ITEM)
    suspend fun removeCartItem(@Path("cartItemId") cartItemId: String): Response<Unit>

    @PUT(Endpoints.UPDATE_CART_ITEM_QUANTITY)
    suspend fun updateCartItemQuantity(
        @Path("cartItemId") cartItemId: String,
        @Body request: AddToCartRequest
    ): Response<Unit>
}
