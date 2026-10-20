package edu.utp.evchargemanager.data.model

/** Estados posibles de un punto de carga. [label] es el texto que ve el usuario. */
enum class ChargerStatus(val label: String) {
    AVAILABLE("Disponible"),
    OCCUPIED("Ocupado"),
    MAINTENANCE("En mantenimiento"),
    OUT_OF_SERVICE("Fuera de servicio")
}

/** Tipos de conector de carga que manejan las estaciones. */
enum class ConnectorType(val label: String) {
    TYPE_2("Tipo 2"),
    CCS2("CCS2"),
    CHADEMO("CHAdeMO")
}

/**
 * Un punto de carga (cargador) dentro de una estación.
 *
 * @property powerKw potencia máxima en kilovatios.
 * @property pricePerKwh tarifa en pesos colombianos (COP) por kWh.
 */
data class Charger(
    val id: Int,
    val name: String,
    val connector: ConnectorType,
    val powerKw: Int,
    val pricePerKwh: Int,
    val status: ChargerStatus
) {
    val isAvailable: Boolean get() = status == ChargerStatus.AVAILABLE

    /** Se considera carga rápida a partir de 50 kW (corriente continua). */
    val isFastCharger: Boolean get() = powerKw >= FAST_CHARGE_MIN_KW

    companion object {
        const val FAST_CHARGE_MIN_KW = 50
    }
}

/**
 * Información principal del negocio: una estación de carga con sus cargadores.
 * Las propiedades calculadas evitan repetir esa lógica en cada pantalla.
 */
data class Station(
    val id: Int,
    val name: String,
    val city: String,
    val address: String,
    val openingHours: String,
    val chargers: List<Charger>
) {
    val totalChargers: Int get() = chargers.size
    val availableCount: Int get() = chargers.count { it.isAvailable }
    val maxPowerKw: Int get() = chargers.maxOfOrNull { it.powerKw } ?: 0
    val connectorTypes: List<ConnectorType> get() = chargers.map { it.connector }.distinct()

    fun supports(connector: ConnectorType): Boolean = chargers.any { it.connector == connector }
}
