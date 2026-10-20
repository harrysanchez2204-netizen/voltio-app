package edu.utp.evchargemanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

/** Rutas de Navigation Compose. Centralizarlas evita escribir textos sueltos por la app. */
object Routes {
    const val HOME = "inicio"
    const val STATIONS = "estaciones"
    const val FAVORITES = "favoritas"

    const val STATION_ID_ARG = "stationId"
    const val NO_STATION = -1

    /** Patrón de la ruta de detalle: "estacion/{stationId}". */
    const val DETAIL = "estacion/{$STATION_ID_ARG}"

    /** Patrón del planificador; el argumento es opcional (-1 = sin estación elegida). */
    const val PLANNER = "planificador?$STATION_ID_ARG={$STATION_ID_ARG}"

    fun detail(stationId: Int) = "estacion/$stationId"

    fun planner(stationId: Int = NO_STATION) = "planificador?$STATION_ID_ARG=$stationId"
}

/**
 * Elemento de la barra de navegación inferior.
 *
 * @property route patrón de ruta usado para saber si la pestaña está seleccionada.
 * @property destination ruta concreta a la que se navega al tocarla.
 * @property alsoSelectedOn otras rutas que mantienen resaltada esta pestaña
 *   (por ejemplo, el detalle de una estación pertenece a la pestaña Estaciones).
 */
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val destination: String = route,
    val alsoSelectedOn: Set<String> = emptySet()
)

val bottomNavItems = listOf(
    BottomNavItem("Inicio", Icons.Filled.Home, Routes.HOME),
    BottomNavItem(
        label = "Estaciones",
        icon = Icons.Filled.Place,
        route = Routes.STATIONS,
        alsoSelectedOn = setOf(Routes.DETAIL)
    ),
    BottomNavItem("Favoritas", Icons.Filled.Favorite, Routes.FAVORITES),
    BottomNavItem(
        label = "Planificar",
        icon = Icons.Filled.Build,
        route = Routes.PLANNER,
        destination = Routes.planner()
    )
)
