package edu.utp.evchargemanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.ChargeCalculator
import edu.utp.evchargemanager.data.model.Charger
import edu.utp.evchargemanager.data.model.Station
import edu.utp.evchargemanager.navigation.Routes
import edu.utp.evchargemanager.ui.components.SectionTitle
import edu.utp.evchargemanager.ui.components.StatusChip
import edu.utp.evchargemanager.ui.components.specsText
import edu.utp.evchargemanager.util.formatCop
import edu.utp.evchargemanager.util.formatDuration
import kotlin.math.roundToInt

/**
 * Planificador de carga (funcionalidad nueva).
 *
 * El usuario elige estación y cargador disponible, indica la capacidad de su batería y los
 * porcentajes inicial y objetivo, y la pantalla calcula energía, duración y costo en COP.
 * Los datos se validan y el resultado se actualiza al instante con cada cambio.
 *
 * @param initialStationId estación preseleccionada (viene del detalle) o [Routes.NO_STATION].
 */
@Composable
fun PlannerScreen(
    stations: List<Station>,
    initialStationId: Int
) {
    // rememberSaveable conserva lo escrito al rotar la pantalla.
    var selectedStationId by rememberSaveable(initialStationId) { mutableStateOf(initialStationId) }
    var selectedChargerId by rememberSaveable { mutableStateOf(-1) }
    var batteryText by rememberSaveable { mutableStateOf("") }
    var startPercent by rememberSaveable { mutableStateOf(20f) }
    var targetPercent by rememberSaveable { mutableStateOf(80f) }

    val station = stations.firstOrNull { it.id == selectedStationId }
    // Solo cuenta como elegido un cargador que siga disponible en la estación actual.
    val charger = station?.chargers?.firstOrNull { it.id == selectedChargerId && it.isAvailable }

    val start = startPercent.roundToInt()
    val target = targetPercent.roundToInt()
    val capacity = ChargeCalculator.parseBatteryCapacity(batteryText)
    // No se muestra error mientras el campo esté vacío (el usuario aún no escribe).
    val capacityError = if (batteryText.isBlank()) null else ChargeCalculator.validateBatteryCapacity(batteryText)
    val percentError = ChargeCalculator.validatePercentages(start, target)

    val estimate = if (charger != null && capacity != null && capacityError == null && percentError == null) {
        ChargeCalculator.estimate(capacity, start, target, charger)
    } else {
        null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle("1. Elige la estación")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(stations, key = { it.id }) { item ->
                FilterChip(
                    selected = item.id == selectedStationId,
                    onClick = {
                        selectedStationId = item.id
                        selectedChargerId = -1 // el cargador anterior ya no pertenece a esta estación
                    },
                    label = { Text(item.name) }
                )
            }
        }

        SectionTitle("2. Elige un cargador")
        if (station == null) {
            Text(
                text = "Primero selecciona una estación.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            station.chargers.forEach { option ->
                ChargerChoice(
                    charger = option,
                    selected = option.id == selectedChargerId,
                    onSelect = { selectedChargerId = option.id }
                )
            }
        }

        SectionTitle("3. Datos de tu vehículo")
        OutlinedTextField(
            value = batteryText,
            onValueChange = { batteryText = it },
            label = { Text("Capacidad de la batería (kWh)") },
            isError = capacityError != null,
            supportingText = { Text(capacityError ?: "Ejemplo: 60. Entre 10 y 200 kWh.") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Nivel actual de batería: $start %", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = startPercent,
            onValueChange = { startPercent = it },
            valueRange = 0f..100f
        )
        Text("Meta de carga: $target %", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = targetPercent,
            onValueChange = { targetPercent = it },
            valueRange = 0f..100f
        )
        if (percentError != null) {
            Text(
                text = percentError,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        SectionTitle("Resumen")
        if (estimate != null && charger != null && station != null) {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${station.name} · ${charger.name}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("Carga de $start % a $target %", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Energía: ${"%.1f".format(estimate.energyKwh)} kWh",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Tiempo estimado: ${formatDuration(estimate.durationMinutes)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Costo estimado: ${formatCop(estimate.totalCostCop)}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Estimación orientativa; el valor real depende de la curva de carga del vehículo.",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        } else {
            Text(
                text = "Elige un cargador disponible y completa los datos para ver el tiempo y el costo.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        OutlinedButton(
            onClick = {
                selectedStationId = Routes.NO_STATION
                selectedChargerId = -1
                batteryText = ""
                startPercent = 20f
                targetPercent = 80f
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar formulario")
        }
    }
}

/**
 * Opción seleccionable de cargador. Solo los cargadores disponibles se pueden elegir;
 * los demás se muestran deshabilitados con su estado para que se entienda el motivo.
 */
@Composable
private fun ChargerChoice(
    charger: Charger,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                enabled = charger.isAvailable,
                role = Role.RadioButton,
                onClick = onSelect
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // onClick = null: el clic lo gestiona toda la fila (mejor accesibilidad y área táctil).
        RadioButton(selected = selected, onClick = null, enabled = charger.isAvailable)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(text = charger.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = charger.specsText(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        StatusChip(status = charger.status)
    }
}
