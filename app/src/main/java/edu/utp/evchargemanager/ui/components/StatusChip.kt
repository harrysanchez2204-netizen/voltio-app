package edu.utp.evchargemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.utp.evchargemanager.data.model.ChargerStatus
import edu.utp.evchargemanager.ui.theme.StatusAvailableBackground
import edu.utp.evchargemanager.ui.theme.StatusAvailableContent
import edu.utp.evchargemanager.ui.theme.StatusMaintenanceBackground
import edu.utp.evchargemanager.ui.theme.StatusMaintenanceContent
import edu.utp.evchargemanager.ui.theme.StatusOccupiedBackground
import edu.utp.evchargemanager.ui.theme.StatusOccupiedContent
import edu.utp.evchargemanager.ui.theme.StatusOutOfServiceBackground
import edu.utp.evchargemanager.ui.theme.StatusOutOfServiceContent

/** Par de colores (fondo, texto) asociado a cada estado de un cargador. */
private fun statusColors(status: ChargerStatus): Pair<Color, Color> = when (status) {
    ChargerStatus.AVAILABLE -> StatusAvailableBackground to StatusAvailableContent
    ChargerStatus.OCCUPIED -> StatusOccupiedBackground to StatusOccupiedContent
    ChargerStatus.MAINTENANCE -> StatusMaintenanceBackground to StatusMaintenanceContent
    ChargerStatus.OUT_OF_SERVICE -> StatusOutOfServiceBackground to StatusOutOfServiceContent
}

/**
 * Indicador reutilizable del estado de un cargador: punto de color + texto.
 * El texto garantiza que el estado se entienda aunque no se distingan los colores.
 */
@Composable
fun StatusChip(status: ChargerStatus, modifier: Modifier = Modifier) {
    val (background, content) = statusColors(status)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(content)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelMedium,
            color = content
        )
    }
}
