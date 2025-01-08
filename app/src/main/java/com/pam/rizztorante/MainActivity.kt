package com.pam.rizztorante

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pam.rizztorante.ui.components.BottomNavbar
import com.pam.rizztorante.ui.screens.LoginScreen
import com.pam.rizztorante.ui.screens.MenuScreen
import com.pam.rizztorante.ui.theme.RizztoranteTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { RizztoranteTheme { RizztoranteApp() } }
  }
}

@Composable
fun RizztoranteApp() {
  val navController = rememberNavController()
  val currentRoute = navController.currentBackStackEntry?.destination?.route
  val showBottomBar = currentRoute != "login"

  Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            if (showBottomBar) {
              BottomNavbar(navController = navController)
            }
          }
  ) { innerPadding ->
    NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
    ) {
      composable("login") { LoginScreen(navController) }
      composable("menu") { MenuScreen() }
    }
  }
}
