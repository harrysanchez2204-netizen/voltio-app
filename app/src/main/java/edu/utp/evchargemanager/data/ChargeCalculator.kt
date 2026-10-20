package edu.utp.evchargemanager.data

import edu.utp.evchargemanager.data.model.Charger
import kotlin.math.ceil
import kotlin.math.roundToLong

/** Resultado de planificar una carga. */
data class ChargeEstimate(
    val energyKwh: Double,
    val durationMinutes: Int,
    val totalCostCop: Long
)

/**
 * Cálculo y validación del planificador de carga (funcionalidad nueva).
 * Es lógica pura (sin Compose) para poder verificarla con tests unitarios.
 */
object ChargeCalculator {

    const val MIN_BATTERY_KWH = 10.0
    const val MAX_BATTERY_KWH = 200.0

    /**
     * Rendimiento supuesto del proceso de carga: parte de la energía se pierde en calor,
     * por eso cargar tarda un poco más que "energía ÷ potencia". Es una estimación simple.
     */
    private const val CHARGING_EFFICIENCY = 0.9

    /** Convierte el texto escrito a número; acepta coma o punto como separador decimal. */
    fun parseBatteryCapacity(text: String): Double? =
        text.trim().replace(',', '.').toDoubleOrNull()

    /** Devuelve el mensaje de error, o null si la capacidad es válida. */
    fun validateBatteryCapacity(text: String): String? {
        val capacity = parseBatteryCapacity(text)
            ?: return "Escribe un número, por ejemplo 60"
        return if (capacity < MIN_BATTERY_KWH || capacity > MAX_BATTERY_KWH) {
            "La capacidad debe estar entre 10 y 200 kWh"
        } else {
            null
        }
    }

    /** Devuelve el mensaje de error, o null si la meta es mayor que el nivel actual. */
    fun validatePercentages(startPercent: Int, targetPercent: Int): String? =
        if (targetPercent <= startPercent) {
            "La meta de carga debe ser mayor que el nivel actual"
        } else {
            null
        }

    /**
     * Estima energía, duración y costo para pasar de [startPercent] a [targetPercent]
     * en el [charger] elegido. Se asume que los datos ya fueron validados.
     */
    fun estimate(
        batteryKwh: Double,
        startPercent: Int,
        targetPercent: Int,
        charger: Charger
    ): ChargeEstimate {
        val energyKwh = batteryKwh * (targetPercent - startPercent) / 100.0
        val hours = energyKwh / (charger.powerKw * CHARGING_EFFICIENCY)
        val minutes = ceil(hours * 60).toInt()
        val cost = (energyKwh * charger.pricePerKwh).roundToLong()
        return ChargeEstimate(energyKwh, minutes, cost)
    }
}
