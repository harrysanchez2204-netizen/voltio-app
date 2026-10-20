package edu.utp.evchargemanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.filterStations
import edu.utp.evchargemanager.data.model.ConnectorType
import edu.utp.evchargemanager.data.model.Station
import edu.utp.evchargemanager.ui.components.EmptyState
import edu.utp.evchargemanager.ui.components.StationCard

/**
 * Listado de estaciones con búsqueda por texto y filtros (funcionalidad trabajada en clase,
 * adaptada al negocio). El estado de los filtros vive en el nivel superior para que no se
 * pierda al abrir un detalle y volver.
 */
@Composable
fun StationsScreen(
    stations: List<Station>,
    query: String,
    onQueryChange: (String) -> Unit,
    connectorFilter: ConnectorType?,
    onConnectorFilterChange: (ConnectorType?) -> Unit,
    onlyAvailable: Boolean,
    onOnlyAvailableChange: (Boolean) -> Unit,
    favoriteIds: List<Int>,
    onStationClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    // Solo se recalcula cuando cambia alguna de las entradas del filtro.
    val filtered = remember(stations, query, connectorFilter, onlyAvailable) {
        filterStations(stations, query, connectorFilter, onlyAvailable)
    }
    val hasActiveFilters = query.isNotBlank() || connectorFilter != null || onlyAvailable

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Buscar por nombre, ciudad o dirección") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Chips de filtro: todos los conectores + "solo disponibles".
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = connectorFilter == null,
                        onClick = { onConnectorFilterChange(null) },
                        label = { Text("Todos") }
                    )
                }
                items(ConnectorType.entries) { connector ->
                    FilterChip(
                        selected = connectorFilter == connector,
                        onClick = {
                            // Tocar de nuevo el chip activo quita el filtro.
                            onConnectorFilterChange(if (connectorFilter == connector) null else connector)
                        },
                        label = { Text(connector.label) }
                    )
                }
                item {
                    FilterChip(
                        selected = onlyAvailable,
                        onClick = { onOnlyAvailableChange(!onlyAvailable) },
                        label = { Text("Solo con disponibilidad") }
                    )
                }
            }

            Text(
                text = "${filtered.size} de ${stations.size} estaciones",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (filtered.isEmpty()) {
            val clearFilters: () -> Unit = {
                onQueryChange("")
                onConnectorFilterChange(null)
                onOnlyAvailableChange(false)
            }
            EmptyState(
                title = "Sin resultados",
                message = "Ninguna estación coincide con la búsqueda y los filtros actuales.",
                actionLabel = if (hasActiveFilters) "Limpiar filtros" else null,
                onAction = if (hasActiveFilters) clearFilters else null
            )
        } else {
            StationGrid(
                stations = filtered,
                favoriteIds = favoriteIds,
                onStationClick = onStationClick,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}

/**
 * Cuadrícula adaptable: en un teléfono muestra una columna y en pantallas anchas
 * (tablet o teléfono horizontal) agrega columnas automáticamente.
 */
@Composable
fun StationGrid(
    stations: List<Station>,
    favoriteIds: List<Int>,
    onStationClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 320.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stations, key = { it.id }) { station ->
            StationCard(
                station = station,
                isFavorite = station.id in favoriteIds,
                onClick = { onStationClick(station.id) },
                onToggleFavorite = { onToggleFavorite(station.id) }
            )
        }
    }
}
