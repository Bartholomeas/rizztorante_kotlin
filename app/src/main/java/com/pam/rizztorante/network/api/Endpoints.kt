package com.pam.rizztorante.network.api

object Endpoints {
  const val BASE_URL = "http://162.19.153.39:3002/api/v1/"

  const val RECEIVE_TIMEOUT = 5000
  const val CONNECTION_TIMEOUT = 3000

  const val LOGIN = "${BASE_URL}auth/login"
  const val MENUS = "${BASE_URL}menus"
  const val CART = "${BASE_URL}cart"
  const val ADD_TO_CART = "${BASE_URL}cart/item"

  fun menuCategory(menuId: String) = "${BASE_URL}menus/$menuId/categories"
}
