package com.pam.rizztorante.ui.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pam.rizztorante.R
import com.pam.rizztorante.model.MenuPosition

@Composable
fun MenuItem(position: MenuPosition) {
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

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
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