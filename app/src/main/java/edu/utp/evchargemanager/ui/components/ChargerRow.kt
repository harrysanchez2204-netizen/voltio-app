package edu.utp.evchargemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.model.Charger
import edu.utp.evchargemanager.util.formatCop

/** Texto con las características técnicas de un cargador: conector, potencia y tarifa. */
fun Charger.specsText(): String =
    "${connector.label} · $powerKw kW · ${formatCop(pricePerKwh.toLong())}/kWh"

/** Fila reutilizable que muestra un cargador con su estado (pantalla de detalle). */
@Composable
fun ChargerRow(charger: Charger, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = charger.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = charger.specsText(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (charger.isFastCharger) {
                    Text(
                        text = "Carga rápida",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            StatusChip(status = charger.status)
        }
    }
}
