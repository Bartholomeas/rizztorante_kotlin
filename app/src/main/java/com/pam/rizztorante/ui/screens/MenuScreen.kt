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

@Composable
fun MenuScreen() {
  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    Text(
            text = "Wybierz pozycję z menu",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
    )

    MenuSection(title = "Lunch Menu", subtitle = "Lunch Menu", expanded = true)

    MenuSection(title = "Appetizers", expanded = false)

    MenuSection(title = "Dinner Menu", subtitle = "Dinner Menu", expanded = false)

    MenuSection(
            title = "Main Courses",
            expanded = true,
            items =
                    listOf(
                            MenuItem(
                                    name = "Spaghetti Carbonara",
                                    description =
                                            "Classic Italian pasta dish with eggs, cheese, pancetta, and black pepper",
                                    price = 15.00,
                                    category = "Main Courses"
                            )
                    )
    )
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

  Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
              modifier = Modifier.fillMaxWidth().padding(bottom = if (isExpanded) 8.dp else 0.dp),
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
          modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
              onClick = { /* TODO: Implement add to cart */},
              modifier = Modifier.padding(top = 4.dp)
      ) { Text("Dodaj do koszyka") }
    }
  }
}
