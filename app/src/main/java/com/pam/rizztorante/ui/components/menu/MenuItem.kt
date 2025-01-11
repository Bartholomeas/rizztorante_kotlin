package com.pam.rizztorante.ui.components.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pam.rizztorante.R
import com.pam.rizztorante.model.AddToCartRequest
import com.pam.rizztorante.model.MenuPosition
import com.pam.rizztorante.network.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun MenuItem(position: MenuPosition) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = position.coreImage?.url,
            contentDescription = position.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = position.name, fontWeight = FontWeight.Bold)
            Text(text = position.description, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Row(modifier = Modifier.padding(top = 4.dp)) {
                if (position.isVegetarian) Text("Wege ")
                if (position.isVegan) Text("Wegańskie ")
                if (position.isGlutenFree) Text("Bez glutenu")
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = "${position.price / 100.0} PLN", fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Zmniejsz ilość"
                    )
                }
                Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { quantity++ }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Zwiększ ilość")
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val response =
                                ApiClient.apiService.addToCart(
                                    AddToCartRequest(
                                        menuPositionId = position.id,
                                        quantity = quantity
                                    )
                                )
                            if (response.isSuccessful) {
                                showToast(context, "${position.name} został dodany do koszyka")
                            } else {
                                showToast(context, "Nie udało się dodać przedmiotu do koszyka")
                            }
                        } catch (e: Exception) {
                            showToast(context, "Błąd połączenia")
                        }
                    }
                },
                modifier = Modifier.padding(top = 4.dp)
            ) { Text("Dodaj") }
        }
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
