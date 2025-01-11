package com.pam.rizztorante.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
