package com.pam.rizztorante.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pam.rizztorante.model.MenuItem
import com.pam.rizztorante.model.MenuResponse
import com.pam.rizztorante.network.api.ApiClient
import com.pam.rizztorante.ui.components.menu.MenuDropdown
import kotlinx.coroutines.launch

@Composable
fun MenuScreen() {
    var menus by remember { mutableStateOf<List<MenuResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = ApiClient.apiService.getMenus()
                if (response.isSuccessful) {
                    menus = response.body() ?: emptyList()
                } else {
                    error = "Nie udało się pobrać menu"
                }
            } catch (e: Exception) {
                error = "Błąd połączenia z serwerem"
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
        Text(
            text = "Wybierz pozycję z menu",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error!!, color = MaterialTheme.colorScheme.error)
                }
            }

            menus.isEmpty() -> {
                Text(
                    text = "Brak dostępnych pozycji w menu",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            else -> {
                menus.forEach { menu -> MenuDropdown(menu) }
            }
        }
    }
}

@Composable
fun MenuSection(
    title: String,
    subtitle: String? = null,
    expanded: Boolean = false,
    items: List<MenuItem> = emptyList()
) {
    var isExpanded by remember { mutableStateOf(expanded) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (isExpanded) 8.dp else 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                    subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector =
                        if (isExpanded) {
                            androidx.compose.material.icons.Icons.Default.ArrowUpward
                        } else {
                            androidx.compose.material.icons.Icons.Default.ArrowDownward
                        },
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }

            if (isExpanded && items.isNotEmpty()) {
                items.forEach { item -> MenuItemCard(item = item) }
            }
        }
    }
}

@Composable
fun MenuItemCard(item: MenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "%.2f PLN".format(item.price),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = { /* TODO: Implement add to cart */ },
                modifier = Modifier.padding(top = 4.dp)
            ) { Text("Dodaj do koszyka") }
        }
    }
}
