package edu.utp.evchargemanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.model.Station
import edu.utp.evchargemanager.ui.components.BoltLogo
import edu.utp.evchargemanager.ui.components.QuickActionCard
import edu.utp.evchargemanager.ui.components.SectionTitle
import edu.utp.evchargemanager.ui.components.StationCard
import edu.utp.evchargemanager.ui.components.SummaryTile

/**
 * Pantalla de inicio: identidad de la app, resumen de disponibilidad, accesos rápidos
 * y las estaciones con más cargadores libres.
 */
@Composable
fun HomeScreen(
    stations: List<Station>,
    favoriteIds: List<Int>,
    onStationClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onGoToStations: () -> Unit,
    onGoToPlanner: () -> Unit,
    onGoToFavorites: () -> Unit
) {
    // Cifras del resumen, calculadas a partir de los datos (no escritas a mano).
    val totalChargers = stations.sumOf { it.totalChargers }
    val availableChargers = stations.sumOf { it.availableCount }
    val fastStations = stations.count { station -> station.chargers.any { it.isFastCharger } }
    val mostAvailable = stations.sortedByDescending { it.availableCount }.take(3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BoltLogo(logoSize = 56.dp)
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "Carga sin sorpresas",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Consulta estaciones, cargadores y costos antes de salir.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryTile(
                value = stations.size.toString(),
                label = "Estaciones",
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                value = "$availableChargers/$totalChargers",
                label = "Cargadores disponibles",
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                value = fastStations.toString(),
                label = "Con carga rápida",
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = "Datos de ejemplo: no reflejan la disponibilidad en tiempo real.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SectionTitle("¿Qué quieres hacer?")
        QuickActionCard(
            title = "Buscar estaciones",
            description = "Filtra por ciudad, conector o disponibilidad",
            icon = Icons.Filled.Search,
            onClick = onGoToStations
        )
        QuickActionCard(
            title = "Planificar una carga",
            description = "Calcula tiempo y costo según tu batería",
            icon = Icons.Filled.Build,
            onClick = onGoToPlanner
        )
        QuickActionCard(
            title = "Mis estaciones favoritas",
            description = "${favoriteIds.size} guardadas",
            icon = Icons.Filled.Favorite,
            onClick = onGoToFavorites
        )

        SectionTitle("Más cargadores libres")
        mostAvailable.forEach { station ->
            StationCard(
                station = station,
                isFavorite = station.id in favoriteIds,
                onClick = { onStationClick(station.id) },
                onToggleFavorite = { onToggleFavorite(station.id) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
