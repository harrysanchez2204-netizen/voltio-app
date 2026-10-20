package edu.utp.evchargemanager.util

/** Da formato de pesos colombianos con punto como separador de miles: 45000 -> "$45.000". */
fun formatCop(amount: Long): String {
    val grouped = amount.toString().reversed().chunked(3).joinToString(".").reversed()
    return "$$grouped"
}

/** Convierte minutos a un texto legible: 85 -> "1 h 25 min", 120 -> "2 h", 40 -> "40 min". */
fun formatDuration(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours == 0 -> "$minutes min"
        minutes == 0 -> "$hours h"
        else -> "$hours h $minutes min"
    }
}
