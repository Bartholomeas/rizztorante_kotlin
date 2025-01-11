package com.pam.rizztorante.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.pam.rizztorante.R
import com.pam.rizztorante.model.MenuPositionDetailsResponse
import com.pam.rizztorante.network.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun PositionDetailsScreen(positionId: String, navController: NavController) {
  var position by remember { mutableStateOf<MenuPositionDetailsResponse?>(null) }
  var isLoading by remember { mutableStateOf(true) }
  var error by remember { mutableStateOf<String?>(null) }
  val scope = rememberCoroutineScope()

  LaunchedEffect(positionId) {
    scope.launch {
      try {
        val response = ApiClient.apiService.getPositionDetails(positionId)
        if (response.isSuccessful) {
          position = response.body()
        } else {
          error = "Nie udało się pobrać szczegółów pozycji"
        }
      } catch (e: Exception) {
        error = "Błąd połączenia"
      } finally {
        isLoading = false
      }
    }
  }

  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(bottom = 16.dp)
    ) { Text("Cofnij do menu") }

    if (isLoading) {
      CircularProgressIndicator()
    } else {
      error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

      position?.let {
        AsyncImage(
                model = it.menuPosition.coreImage?.url,
                contentDescription = it.menuPosition.name,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = it.menuPosition.name, style = MaterialTheme.typography.headlineMedium)
        Text(text = it.menuPosition.description)
        Text(
                text = "${it.menuPosition.price / 100.0} PLN",
                style = MaterialTheme.typography.titleMedium
        )
        Text(text = "Kalorie: ${it.calories}")
        Text(text = "Długie opisy: ${it.longDescription}")

        if (it.allergens.isNotEmpty()) {
          Text(text = "Alergeny: ${it.allergens.joinToString(", ")}")
        }

        Text(text = "Białko: ${it.nutritionalInfo.protein}g")
        Text(text = "Węglowodany: ${it.nutritionalInfo.carbs}g")
        Text(text = "Tłuszcz: ${it.nutritionalInfo.fat}g")
        Text(text = "Błonnik: ${it.nutritionalInfo.fiber}g")
      }
    }
  }
}
