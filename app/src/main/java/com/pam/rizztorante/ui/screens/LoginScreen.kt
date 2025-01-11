package com.pam.rizztorante.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pam.rizztorante.model.LoginRequest
import com.pam.rizztorante.network.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
  var email by remember { mutableStateOf("test@gmail.com") }
  var password by remember { mutableStateOf("!23Haslo") }
  var isLoading by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf<String?>(null) }

  val scope = rememberCoroutineScope()

  Column(
          modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (error != null) {
      Text(
              text = error!!,
              color = MaterialTheme.colorScheme.error,
              modifier = Modifier.padding(vertical = 8.dp)
      )
    }

    OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    )

    OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    )

    Button(
            onClick = {
              scope.launch {
                isLoading = true
                error = null
                try {
                  val response = ApiClient.apiService.login(LoginRequest(email, password))
                  if (response.isSuccessful) {
                    navController.navigate("menu") { popUpTo("login") { inclusive = true } }
                  } else {
                    error = "Błędne dane logowania"
                  }
                } catch (e: Exception) {
                  println("ERROR::: ${e.message}")
                  error = "Błąd połączenia z serwerem"
                } finally {
                  isLoading = false
                }
              }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            enabled = !isLoading
    ) {
      if (isLoading) {
        CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
        )
      } else {
        Text("Zaloguj się")
      }
    }
  }
}
