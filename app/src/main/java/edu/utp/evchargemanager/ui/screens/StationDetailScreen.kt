package edu.utp.evchargemanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.model.Station
import edu.utp.evchargemanager.ui.components.AvailabilityBadge
import edu.utp.evchargemanager.ui.components.ChargerRow
import edu.utp.evchargemanager.ui.components.EmptyState
import edu.utp.evchargemanager.ui.components.FavoriteButton
import edu.utp.evchargemanager.ui.components.SectionTitle
import edu.utp.evchargemanager.ui.components.StationImage

/** Detalle de una estación: datos generales y estado de cada punto de carga. */
@Composable
fun StationDetailScreen(
    station: Station?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onPlanCharge: () -> Unit
) {
    // Si la ruta trae un id que no existe, se muestra un mensaje en vez de fallar.
    if (station == null) {
        EmptyState(
            title = "Estación no encontrada",
            message = "Vuelve al listado y selecciona otra estación."
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                StationImage(station = station, modifier = Modifier.fillMaxWidth())
                Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 8.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = station.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = station.city,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        FavoriteButton(
                            isFavorite = isFavorite,
                            stationName = station.name,
                            onToggle = onToggleFavorite
                        )
                    }
                    InfoLine(label = "Dirección", value = station.address)
                    InfoLine(label = "Horario", value = station.openingHours)
                    AvailabilityBadge(
                        availableCount = station.availableCount,
                        totalCount = station.totalChargers,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        item { SectionTitle("Puntos de carga") }

        // Un ChargerRow por cada cargador de la colección (no hay tarjetas escritas a mano).
        items(station.chargers, key = { it.id }) { charger ->
            ChargerRow(charger = charger)
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onPlanCharge,
                    enabled = station.availableCount > 0,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Planificar carga aquí")
                }
                if (station.availableCount == 0) {
                    Text(
                        text = "No hay cargadores disponibles en este momento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

/** Línea "Etiqueta: valor" de la ficha de la estación. */
@Composable
private fun InfoLine(label: String, value: String) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
