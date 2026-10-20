package edu.utp.evchargemanager

import edu.utp.evchargemanager.data.ChargeCalculator
import edu.utp.evchargemanager.data.model.Charger
import edu.utp.evchargemanager.data.model.ChargerStatus
import edu.utp.evchargemanager.data.model.ConnectorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/** Pruebas del cálculo y las validaciones del planificador de carga. */
class ChargeCalculatorTest {

    private val charger50kw = Charger(
        id = 1,
        name = "Cargador de prueba",
        connector = ConnectorType.CCS2,
        powerKw = 50,
        pricePerKwh = 2000,
        status = ChargerStatus.AVAILABLE
    )

    @Test
    fun estimate_calculatesEnergyTimeAndCost() {
        // Batería de 60 kWh de 20 % a 80 % = 36 kWh.
        val result = ChargeCalculator.estimate(60.0, 20, 80, charger50kw)

        assertEquals(36.0, result.energyKwh, 0.001)
        // 36 kWh / (50 kW * 0,9) = 0,8 h = 48 min.
        assertEquals(48, result.durationMinutes)
        // 36 kWh * 2.000 COP = 72.000 COP.
        assertEquals(72000L, result.totalCostCop)
    }

    @Test
    fun validateBatteryCapacity_acceptsValidValuesWithDotOrComma() {
        assertNull(ChargeCalculator.validateBatteryCapacity("60"))
        assertNull(ChargeCalculator.validateBatteryCapacity("40,5"))
        assertNull(ChargeCalculator.validateBatteryCapacity(" 77.4 "))
    }

    @Test
    fun validateBatteryCapacity_rejectsTextAndOutOfRangeValues() {
        assertNotNull(ChargeCalculator.validateBatteryCapacity("abc"))
        assertNotNull(ChargeCalculator.validateBatteryCapacity(""))
        assertNotNull(ChargeCalculator.validateBatteryCapacity("5"))
        assertNotNull(ChargeCalculator.validateBatteryCapacity("500"))
    }

    @Test
    fun validatePercentages_requiresTargetGreaterThanStart() {
        assertNull(ChargeCalculator.validatePercentages(20, 80))
        assertNotNull(ChargeCalculator.validatePercentages(80, 80))
        assertNotNull(ChargeCalculator.validatePercentages(90, 30))
    }
}
