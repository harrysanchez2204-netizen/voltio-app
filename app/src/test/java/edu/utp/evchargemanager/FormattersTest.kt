package edu.utp.evchargemanager

import edu.utp.evchargemanager.util.formatCop
import edu.utp.evchargemanager.util.formatDuration
import org.junit.Assert.assertEquals
import org.junit.Test

/** Pruebas de los formateadores de pesos colombianos y de duración. */
class FormattersTest {

    @Test
    fun formatCop_usesDotAsThousandsSeparator() {
        assertEquals("$0", formatCop(0))
        assertEquals("$950", formatCop(950))
        assertEquals("$45.000", formatCop(45000))
        assertEquals("$1.234.567", formatCop(1234567))
    }

    @Test
    fun formatDuration_showsHoursAndMinutes() {
        assertEquals("40 min", formatDuration(40))
        assertEquals("2 h", formatDuration(120))
        assertEquals("1 h 25 min", formatDuration(85))
    }
}
