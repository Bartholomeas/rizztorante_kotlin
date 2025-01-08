package com.pam.rizztorante.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pam.rizztorante.model.BottomNavItem

@Composable
fun BottomNavbar(navController: NavController) {
  val items =
          listOf(
                  BottomNavItem("menu", "Menu", Icons.Default.Restaurant),
                  BottomNavItem("cart", "Koszyk", Icons.Default.ShoppingCart)
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
                navController.navigate(item.route) {
                  popUpTo("menu") { saveState = true }
                  launchSingleTop = true
                  restoreState = true
                }
              }
      )
    }
  }
}
