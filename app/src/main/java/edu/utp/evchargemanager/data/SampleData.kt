package edu.utp.evchargemanager.data

import edu.utp.evchargemanager.data.model.Charger
import edu.utp.evchargemanager.data.model.ChargerStatus
import edu.utp.evchargemanager.data.model.ChargerStatus.AVAILABLE
import edu.utp.evchargemanager.data.model.ChargerStatus.MAINTENANCE
import edu.utp.evchargemanager.data.model.ChargerStatus.OCCUPIED
import edu.utp.evchargemanager.data.model.ChargerStatus.OUT_OF_SERVICE
import edu.utp.evchargemanager.data.model.ConnectorType
import edu.utp.evchargemanager.data.model.ConnectorType.CCS2
import edu.utp.evchargemanager.data.model.ConnectorType.CHADEMO
import edu.utp.evchargemanager.data.model.ConnectorType.TYPE_2
import edu.utp.evchargemanager.data.model.Station

/**
 * Datos locales de ejemplo (el alcance del primer corte no incluye API ni base de datos).
 *
 * IMPORTANTE: son datos ficticios. Los nombres, direcciones, tarifas y estados NO
 * representan estaciones reales ni disponibilidad en tiempo real.
 */
object SampleData {

    // Función auxiliar para que cada cargador ocupe una sola línea y el listado sea legible.
    private fun charger(
        id: Int,
        name: String,
        connector: ConnectorType,
        powerKw: Int,
        pricePerKwh: Int,
        status: ChargerStatus
    ) = Charger(id, name, connector, powerKw, pricePerKwh, status)

    val stations: List<Station> = listOf(
        Station(
            id = 1,
            name = "Hub Centro Cívico",
            city = "Pereira",
            address = "Carrera 7 con calle 19 (ejemplo)",
            openingHours = "Todos los días, 24 horas",
            chargers = listOf(
                charger(11, "Cargador A1", CCS2, 120, 2100, AVAILABLE),
                charger(12, "Cargador A2", CCS2, 120, 2100, OCCUPIED),
                charger(13, "Cargador B1", TYPE_2, 22, 1300, AVAILABLE),
                charger(14, "Cargador B2", TYPE_2, 22, 1300, AVAILABLE)
            )
        ),
        Station(
            id = 2,
            name = "Parque Industrial Norte",
            city = "Dosquebradas",
            address = "Avenida Simón Bolívar, km 2 (ejemplo)",
            openingHours = "Lunes a sábado, 6:00 a. m. - 10:00 p. m.",
            chargers = listOf(
                charger(21, "Cargador A1", CCS2, 60, 1900, OCCUPIED),
                charger(22, "Cargador A2", CHADEMO, 50, 1900, OCCUPIED),
                charger(23, "Cargador B1", TYPE_2, 11, 1200, MAINTENANCE)
            )
        ),
        Station(
            id = 3,
            name = "Centro Comercial Los Álamos",
            city = "Pereira",
            address = "Calle 14 #12-30, parqueadero nivel 2 (ejemplo)",
            openingHours = "Todos los días, 8:00 a. m. - 9:00 p. m.",
            chargers = listOf(
                charger(31, "Cargador A1", TYPE_2, 22, 1400, AVAILABLE),
                charger(32, "Cargador A2", TYPE_2, 22, 1400, AVAILABLE),
                charger(33, "Cargador A3", TYPE_2, 7, 1100, OCCUPIED),
                charger(34, "Cargador A4", TYPE_2, 7, 1100, AVAILABLE),
                charger(35, "Cargador A5", TYPE_2, 7, 1100, OUT_OF_SERVICE)
            )
        ),
        Station(
            id = 4,
            name = "Terminal de Transportes",
            city = "Pereira",
            address = "Avenida del Ferrocarril (ejemplo)",
            openingHours = "Todos los días, 24 horas",
            chargers = listOf(
                charger(41, "Cargador A1", CCS2, 90, 2000, AVAILABLE),
                charger(42, "Cargador A2", CHADEMO, 50, 1900, OUT_OF_SERVICE)
            )
        ),
        Station(
            id = 5,
            name = "Eje Cafetero Express",
            city = "Santa Rosa de Cabal",
            address = "Vía a Chinchiná, km 1 (ejemplo)",
            openingHours = "Todos los días, 6:00 a. m. - 8:00 p. m.",
            chargers = listOf(
                charger(51, "Cargador A1", CCS2, 150, 2200, AVAILABLE),
                charger(52, "Cargador A2", CCS2, 150, 2200, AVAILABLE),
                charger(53, "Cargador A3", CCS2, 150, 2200, OCCUPIED)
            )
        ),
        Station(
            id = 6,
            name = "Universidad Campus Verde",
            city = "Pereira",
            address = "Vereda La Julita, portería 2 (ejemplo)",
            openingHours = "Lunes a viernes, 7:00 a. m. - 6:00 p. m.",
            chargers = listOf(
                charger(61, "Cargador A1", TYPE_2, 11, 900, AVAILABLE),
                charger(62, "Cargador A2", TYPE_2, 11, 900, OCCUPIED),
                charger(63, "Cargador A3", TYPE_2, 11, 900, OCCUPIED)
            )
        ),
        Station(
            id = 7,
            name = "Hotel Mirador del Café",
            city = "Manizales",
            address = "Carrera 23 #64-10 (ejemplo)",
            openingHours = "Atención para huéspedes y visitantes, 24 horas",
            chargers = listOf(
                charger(71, "Cargador A1", TYPE_2, 22, 1500, MAINTENANCE),
                charger(72, "Cargador A2", TYPE_2, 22, 1500, MAINTENANCE)
            )
        ),
        Station(
            id = 8,
            name = "Plaza del Valle",
            city = "Armenia",
            address = "Avenida Bolívar #20-45 (ejemplo)",
            openingHours = "Todos los días, 9:00 a. m. - 8:00 p. m.",
            chargers = listOf(
                charger(81, "Cargador A1", CCS2, 60, 1950, AVAILABLE),
                charger(82, "Cargador A2", CHADEMO, 50, 1950, AVAILABLE),
                charger(83, "Cargador B1", TYPE_2, 22, 1350, OCCUPIED),
                charger(84, "Cargador B2", TYPE_2, 22, 1350, AVAILABLE)
            )
        )
    )

    fun findStation(id: Int?): Station? = stations.firstOrNull { it.id == id }
}
