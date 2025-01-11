package com.pam.rizztorante.ui.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MenuHeader(name: String, description: String?, isExpanded: Boolean, onExpandClick: () -> Unit) {
  Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(text = name, style = MaterialTheme.typography.titleLarge)
      if (description != null) {
        Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
    IconButton(onClick = onExpandClick) {
      Icon(
              imageVector =
                      if (isExpanded) Icons.Default.KeyboardArrowUp
                      else Icons.Default.KeyboardArrowDown,
              contentDescription = if (isExpanded) "Zwiń" else "Rozwiń"
      )
    }
  }
}
