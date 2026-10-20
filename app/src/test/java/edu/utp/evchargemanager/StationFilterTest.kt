package edu.utp.evchargemanager

import edu.utp.evchargemanager.data.SampleData
import edu.utp.evchargemanager.data.filterStations
import edu.utp.evchargemanager.data.model.ConnectorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pruebas de búsqueda y filtrado, y de la consistencia de los datos de ejemplo. */
class StationFilterTest {

    private val stations = SampleData.stations

    @Test
    fun sampleData_hasAtLeastSixStationsWithUniqueIds() {
        assertTrue(stations.size >= 6)
        assertEquals(stations.size, stations.map { it.id }.distinct().size)
        val chargerIds = stations.flatMap { it.chargers }.map { it.id }
        assertEquals(chargerIds.size, chargerIds.distinct().size)
    }

    @Test
    fun filter_withoutCriteria_returnsEverything() {
        assertEquals(stations, filterStations(stations, "", null, false))
    }

    @Test
    fun filter_byTextIgnoresCaseAndMatchesNameOrCity() {
        val byCity = filterStations(stations, "  pereira ", null, false)
        assertTrue(byCity.isNotEmpty())
        assertTrue(byCity.all { it.city.equals("Pereira", ignoreCase = true) || it.name.contains("pereira", true) || it.address.contains("pereira", true) })

        val byName = filterStations(stations, "HUB centro", null, false)
        assertEquals(listOf(1), byName.map { it.id })
    }

    @Test
    fun filter_byConnector_keepsOnlyStationsThatSupportIt() {
        val chademo = filterStations(stations, "", ConnectorType.CHADEMO, false)
        assertTrue(chademo.isNotEmpty())
        assertTrue(chademo.all { it.supports(ConnectorType.CHADEMO) })
    }

    @Test
    fun filter_onlyAvailable_excludesStationsWithoutFreeChargers() {
        val result = filterStations(stations, "", null, true)
        assertTrue(result.all { it.availableCount > 0 })
        // La estación 7 está toda en mantenimiento: no debe aparecer.
        assertTrue(result.none { it.id == 7 })
    }

    @Test
    fun filter_withNoMatches_returnsEmptyList() {
        assertTrue(filterStations(stations, "zzz-no-existe", null, false).isEmpty())
    }
}
