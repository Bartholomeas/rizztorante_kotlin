package com.pam.rizztorante.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pam.rizztorante.R
import com.pam.rizztorante.model.MenuCategoryResponse
import com.pam.rizztorante.model.MenuPosition
import com.pam.rizztorante.model.MenuResponse
import com.pam.rizztorante.network.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MenuDropdown(menu: MenuResponse) {
    var isExpanded by remember { mutableStateOf(false) }
    var categories by remember { mutableStateOf<List<MenuCategoryResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            MenuHeader(
                name = menu.name,
                description = menu.description,
                isExpanded = isExpanded,
                onExpandClick = {
                    isExpanded = !isExpanded
                    if (isExpanded && categories.isEmpty()) {
                        loadCategories(scope, menu.id) { newCategories, errorMessage ->
                            categories = newCategories
                            error = errorMessage
                            isLoading = false
                        }
                    }
                }
            )

            if (isExpanded) {
                MenuContent(isLoading = isLoading, error = error, categories = categories)
            }
        }
    }
}

@Composable
private fun MenuHeader(
    name: String,
    description: String?,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
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

@Composable
private fun MenuContent(
    isLoading: Boolean,
    error: String?,
    categories: List<MenuCategoryResponse>
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        categories.isEmpty() -> {
            Text(text = "Brak dostępnych kategorii", modifier = Modifier.padding(8.dp))
        }

        else -> {
            categories.forEach { category -> CategoryItem(category) }
        }
    }
}

@Composable
private fun CategoryItem(category: MenuCategoryResponse) {


    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (category.description != null) {
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        
        }

            category.positions.forEach { position -> MenuItem(position) }
    }
}

@Composable
private fun MenuItem(position: MenuPosition) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = position.coreImage?.url,
            contentDescription = position.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground)
        )

        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)) {
            Text(text = position.name, fontWeight = FontWeight.Bold)
            Text(text = position.description, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Row(modifier = Modifier.padding(top = 4.dp)) {
                if (position.isVegetarian) Text("Wege ")
                if (position.isVegan) Text("Wegańskie ")
                if (position.isGlutenFree) Text("Bez glutenu")
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = "${position.price / 100.0} PLN", fontWeight = FontWeight.Bold)
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.padding(top = 4.dp)
            ) { Text("Dodaj") }
        }
    }
}

private fun loadCategories(
    scope: CoroutineScope,
    menuId: String,
    onResult: (List<MenuCategoryResponse>, String?) -> Unit
) {
    scope.launch {
        try {
            val response = ApiClient.apiService.getMenuCategoriesWithItems(menuId)
            if (response.isSuccessful) {
                onResult(response.body() ?: emptyList(), null)
            } else {
                onResult(emptyList(), "Nie udało się pobrać menu")
            }
        } catch (e: Exception) {
            onResult(emptyList(), "Błąd połączenia")
        }
    }
}
