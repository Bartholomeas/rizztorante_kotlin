package com.pam.rizztorante.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pam.rizztorante.R
import com.pam.rizztorante.model.CartItem
import com.pam.rizztorante.model.CartItemResponse
import com.pam.rizztorante.model.CartResponse
import com.pam.rizztorante.network.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun CartScreen() {
    var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Text(
                text = "Koszyk",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemCard(
                        item = item,
                        onRemove = { cartItems = cartItems.filter { it.id != item.id } },
                        onQuantityChange = { quantity ->
                            cartItems =
                                cartItems.map {
                                    if (it.id == item.id) it.copy(quantity = quantity) else it
                                }
                        }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) { Text("Do płatności") }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, onRemove: () -> Unit, onQuantityChange: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.coreImageUrl.url,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground)
            )
//            AsyncImage(
//                model = item.coreImageUrl, // Upewnij się, że masz URL do obrazka
//                contentDescription = item.name,
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(RoundedCornerShape(8.dp)),
//                contentScale = ContentScale.Crop,
//                placeholder = painterResource(R.drawable.ic_launcher_foreground),
//                error = painterResource(R.drawable.ic_launcher_foreground)
//            )

            Spacer(modifier = Modifier.width(8.dp))

            // Kolumna z nazwą, ilością i ceną
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }) {
                        Icon(Icons.Default.Remove, contentDescription = "Zmniejsz ilość")
                    }
                    Text(
                        text = item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                        Icon(Icons.Default.Add, contentDescription = "Zwiększ ilość")
                    }
                }
                Text(
                    text = "%.2f PLN".format(item.price * item.quantity / 100.0),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Usuń z koszyka")
            }
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
