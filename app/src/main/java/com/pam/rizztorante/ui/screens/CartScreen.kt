package com.pam.rizztorante.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pam.rizztorante.model.CartItem

@Composable
fun CartScreen() {
    var cartItems by remember { mutableStateOf(listOf<CartItem>()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
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
                text = "%.2f PLN".format(cartItems.sumOf { it.price * it.quantity }),
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { /* TODO: Implement navigation to payment */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) { Text("Do płatności") }
    }
}

@Composable
fun CartItemCard(item: CartItem, onRemove: () -> Unit, onQuantityChange: (Int) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }) {
                Icon(Icons.Default.Remove, contentDescription = "Zmniejsz ilość")
            }
            Text(text = item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Zwiększ ilość")
            }
            Text(
                text = "%.2f PLN".format(item.price * item.quantity),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Remove, contentDescription = "Usuń z koszyka")
            }
        }
    }
}
