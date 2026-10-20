package edu.utp.evchargemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.model.Station
import edu.utp.evchargemanager.ui.theme.StatusAvailableBackground
import edu.utp.evchargemanager.ui.theme.StatusAvailableContent
import edu.utp.evchargemanager.ui.theme.StatusOccupiedBackground
import edu.utp.evchargemanager.ui.theme.StatusOccupiedContent

/**
 * Tarjeta reutilizable que resume una estación. Se usa en Inicio, Estaciones y Favoritas.
 * Toda la tarjeta es táctil (abre el detalle) y el corazón alterna el favorito.
 */
@Composable
fun StationCard(
    station: Station,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        StationImage(station = station, modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = station.city,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                AvailabilityBadge(
                    availableCount = station.availableCount,
                    totalCount = station.totalChargers
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hasta ${station.maxPowerKw} kW · " +
                        station.connectorTypes.joinToString(", ") { it.label },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            FavoriteButton(
                isFavorite = isFavorite,
                stationName = station.name,
                onToggle = onToggleFavorite
            )
        }
    }
}

/** Resumen "2 de 4 disponibles"; cambia de color cuando no queda ningún cargador libre. */
@Composable
fun AvailabilityBadge(
    availableCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val hasAvailability = availableCount > 0
    val background = if (hasAvailability) StatusAvailableBackground else StatusOccupiedBackground
    val content = if (hasAvailability) StatusAvailableContent else StatusOccupiedContent
    Text(
        text = if (hasAvailability) {
            "$availableCount de $totalCount disponibles"
        } else {
            "Sin cargadores disponibles"
        },
        style = MaterialTheme.typography.labelMedium,
        color = content,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
