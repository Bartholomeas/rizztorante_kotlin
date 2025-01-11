package com.pam.rizztorante.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pam.rizztorante.model.BottomNavItem
import com.pam.rizztorante.network.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun BottomNavbar(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun logout(navController: NavController) {
        scope.launch {
            try {
                val response = ApiClient.apiService.logout()
                if (response.isSuccessful) {
                    showToast(context, "Pomyślnie wylogowano")
                    navController.navigate("login") { popUpTo("menu") { inclusive = true } }
                } else {
                    showToast(context, "Nie udało się wylogować")
                }
            } catch (e: Exception) {
                showToast(context, "Błąd połączenia")
            }
        }
    }

    val items =
        listOf(
            BottomNavItem("menu", "Menu", Icons.Default.Restaurant),
            BottomNavItem("cart", "Koszyk", Icons.Default.ShoppingCart),
            BottomNavItem("logout", "Wyloguj", Icons.Default.Logout)
        )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (item.route == "logout") {
                        logout(navController)
                    } else {
                        navController.navigate(item.route) {
                            popUpTo("menu") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
