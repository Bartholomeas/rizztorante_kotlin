package com.pam.rizztorante.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pam.rizztorante.model.AddToCartRequest
import com.pam.rizztorante.model.CartItem
import com.pam.rizztorante.model.CartItemResponse
import com.pam.rizztorante.network.api.ApiClient
import com.pam.rizztorante.ui.components.cart.CartItemCard
import kotlinx.coroutines.launch

@Composable
fun CartScreen() {
  var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
  var isLoading by remember { mutableStateOf(true) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val scope = rememberCoroutineScope()
  fun loadCart() {
    scope.launch {
      try {
        val response = ApiClient.apiService.getCart()
        if (response.isSuccessful) {
          val cartResponse = response.body()
          cartItems = cartResponse?.items?.map { it.toCartItem() } ?: emptyList()
        } else {
          errorMessage = "Nie udało się pobrać koszyka"
        }
      } catch (e: Exception) {
        errorMessage = "Błąd połączenia"
      } finally {
        isLoading = false
      }
    }
  }

  fun removeCartItem(cartItemId: String) {
    scope.launch {
      try {
        val response = ApiClient.apiService.removeCartItem(cartItemId)
        if (response.isSuccessful) {
          cartItems = cartItems.filter { it.id != cartItemId }
        } else {
          errorMessage = "Nie udało się usunąć przedmiotu"
        }
      } catch (e: Exception) {
        errorMessage = "Błąd połączenia"
      }
    }
  }

  fun updateCartItemQuantity(cartItemId: String, quantity: Int) {
    scope.launch {
      try {
        val response =
                ApiClient.apiService.updateCartItemQuantity(
                        cartItemId,
                        AddToCartRequest(cartItemId, quantity)
                )
        if (response.isSuccessful) {
          cartItems =
                  cartItems.map { if (it.id == cartItemId) it.copy(quantity = quantity) else it }
        } else {
          errorMessage = "Nie udało się zaktualizować ilości"
        }
      } catch (e: Exception) {
        errorMessage = "Błąd połączenia"
      }
    }
  }
  LaunchedEffect(Unit) { loadCart() }

  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    Text(
            text = "Witamy w aplikacji Rizztorante",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
    )

    if (isLoading) {
      CircularProgressIndicator()
    } else {
      errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

      Text(
              text = "Koszyk",
              style = MaterialTheme.typography.headlineMedium,
              modifier = Modifier.padding(bottom = 16.dp)
      )

      LazyColumn(modifier = Modifier.weight(1f)) {
        items(cartItems) { item ->
          CartItemCard(
                  item = item,
                  onRemove = { removeCartItem(item.id) },
                  onQuantityChange = { quantity -> updateCartItemQuantity(item.id, quantity) }
          )
        }
      }

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Suma:", fontWeight = FontWeight.Bold)
        Text(
                text = "%.2f PLN".format(cartItems.sumOf { it.price * it.quantity } / 100.0),
                fontWeight = FontWeight.Bold
        )
      }

      Button(
              onClick = { println("Payment") },
              modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
      ) { Text("Do płatności") }
    }
  }
}

private fun CartItemResponse.toCartItem(): CartItem {
  return CartItem(
          id = this.id,
          name = this.menuPosition.name,
          price = this.menuPosition.price,
          quantity = this.quantity,
          coreImageUrl = this.menuPosition.coreImage
  )
}
