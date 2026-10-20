package edu.utp.evchargemanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.model.Station
import edu.utp.evchargemanager.ui.components.EmptyState

/**
 * Estaciones marcadas como favoritas (funcionalidad nueva). Al quitar el corazón de una
 * tarjeta, la estación desaparece de esta lista de inmediato.
 */
@Composable
fun FavoritesScreen(
    favoriteStations: List<Station>,
    onStationClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onExploreStations: () -> Unit
) {
    if (favoriteStations.isEmpty()) {
        EmptyState(
            title = "Aún no tienes favoritas",
            message = "Toca el corazón de una estación para guardarla y encontrarla rápido aquí.",
            actionLabel = "Explorar estaciones",
            onAction = onExploreStations
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "${favoriteStations.size} estaciones guardadas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            StationGrid(
                stations = favoriteStations,
                // Todas las de esta lista son favoritas, por eso el corazón siempre aparece lleno.
                favoriteIds = favoriteStations.map { it.id },
                onStationClick = onStationClick,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}
