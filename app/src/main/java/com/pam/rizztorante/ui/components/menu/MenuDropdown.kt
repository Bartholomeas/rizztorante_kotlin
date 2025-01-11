package com.pam.rizztorante.ui.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pam.rizztorante.model.MenuCategoryResponse
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
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
