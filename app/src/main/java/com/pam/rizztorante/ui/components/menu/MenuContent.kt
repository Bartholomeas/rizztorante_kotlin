package com.pam.rizztorante.ui.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pam.rizztorante.model.MenuCategoryResponse

@Composable
fun MenuContent(isLoading: Boolean, error: String?, categories: List<MenuCategoryResponse>) {
  when {
    isLoading -> {
      Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    }
    error != null -> {
      Text(text = error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
    }
    categories.isEmpty() -> {
      Text(text = "Brak dostępnych kategorii", modifier = Modifier.padding(8.dp))
    }
    else -> {
      categories.forEach { category -> CategoryItem(category) }
    }
  }
}
