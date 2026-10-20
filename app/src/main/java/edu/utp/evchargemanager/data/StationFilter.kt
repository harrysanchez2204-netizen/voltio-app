package edu.utp.evchargemanager.data

import edu.utp.evchargemanager.data.model.ConnectorType
import edu.utp.evchargemanager.data.model.Station

/**
 * Lógica de búsqueda y filtrado de estaciones (funcionalidad trabajada en clase,
 * adaptada al negocio). Está fuera de la interfaz para poder probarla con tests unitarios.
 *
 * @param query texto buscado en nombre, ciudad o dirección (sin distinguir mayúsculas).
 * @param connector si no es null, deja solo estaciones que tengan ese tipo de conector.
 * @param onlyAvailable si es true, deja solo estaciones con al menos un cargador disponible.
 */
fun filterStations(
    stations: List<Station>,
    query: String,
    connector: ConnectorType?,
    onlyAvailable: Boolean
): List<Station> {
    val text = query.trim()
    return stations.filter { station ->
        val matchesText = text.isEmpty() ||
            station.name.contains(text, ignoreCase = true) ||
            station.city.contains(text, ignoreCase = true) ||
            station.address.contains(text, ignoreCase = true)
        val matchesConnector = connector == null || station.supports(connector)
        val matchesAvailability = !onlyAvailable || station.availableCount > 0
        matchesText && matchesConnector && matchesAvailability
    }
}
