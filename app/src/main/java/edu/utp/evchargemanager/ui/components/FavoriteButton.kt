package edu.utp.evchargemanager.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Botón de corazón para marcar o desmarcar una estación como favorita.
 * No guarda estado: recibe [isFavorite] y avisa con [onToggle] (estado elevado).
 */
@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    stationName: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onToggle, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isFavorite) {
                "Quitar $stationName de favoritas"
            } else {
                "Agregar $stationName a favoritas"
            },
            tint = if (isFavorite) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}
