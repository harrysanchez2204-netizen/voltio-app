package edu.utp.evchargemanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.utp.evchargemanager.data.SampleData
import edu.utp.evchargemanager.data.model.ConnectorType
import edu.utp.evchargemanager.navigation.Routes
import edu.utp.evchargemanager.navigation.bottomNavItems
import edu.utp.evchargemanager.ui.components.BoltLogo
import edu.utp.evchargemanager.ui.screens.FavoritesScreen
import edu.utp.evchargemanager.ui.screens.HomeScreen
import edu.utp.evchargemanager.ui.screens.PlannerScreen
import edu.utp.evchargemanager.ui.screens.StationDetailScreen
import edu.utp.evchargemanager.ui.screens.StationsScreen
import kotlinx.coroutines.launch

/**
 * Raíz de la aplicación: guarda el estado compartido entre pantallas (búsqueda, filtros y
 * favoritas), dibuja la barra superior e inferior y define el NavHost con las rutas.
 *
 * El estado se "eleva" aquí (state hoisting): las pantallas reciben valores y callbacks,
 * así que el favorito marcado en una pantalla se ve igual en todas las demás.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvChargeManagerApp() {
    val stations = SampleData.stations

    // Estado de búsqueda y filtros (rememberSaveable lo conserva al rotar el dispositivo).
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var connectorFilter by rememberSaveable { mutableStateOf<ConnectorType?>(null) }
    var onlyAvailable by rememberSaveable { mutableStateOf(false) }

    // Ids de estaciones favoritas. listSaver convierte la lista observable en una lista
    // simple para poder guardarla y restaurarla.
    val favoriteIds = rememberSaveable(
        saver = listSaver<SnapshotStateList<Int>, Int>(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) { mutableStateListOf<Int>() }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Alterna el favorito y confirma la acción con un mensaje breve.
    fun toggleFavorite(stationId: Int) {
        val name = SampleData.findStation(stationId)?.name ?: "La estación"
        val message = if (stationId in favoriteIds) {
            favoriteIds.remove(stationId)
            "$name se quitó de favoritas"
        } else {
            favoriteIds.add(stationId)
            "$name se agregó a favoritas"
        }
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (currentRoute == Routes.DETAIL) {
                        Text("Detalle de estación")
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            BoltLogo(logoSize = 32.dp)
                            Text("Voltio")
                        }
                    }
                },
                navigationIcon = {
                    if (currentRoute == Routes.DETAIL) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route || currentRoute in item.alsoSelectedOn,
                        onClick = { navigateToTab(navController, item.route, item.destination) },
                        icon = {
                            if (item.route == Routes.FAVORITES && favoriteIds.isNotEmpty()) {
                                // Insignia con la cantidad de favoritas guardadas.
                                BadgedBox(badge = { Badge { Text(favoriteIds.size.toString()) } }) {
                                    Icon(item.icon, contentDescription = null)
                                }
                            } else {
                                Icon(item.icon, contentDescription = null)
                            }
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    stations = stations,
                    favoriteIds = favoriteIds,
                    onStationClick = { navController.navigate(Routes.detail(it)) },
                    onToggleFavorite = ::toggleFavorite,
                    onGoToStations = { navigateToTab(navController, Routes.STATIONS, Routes.STATIONS) },
                    onGoToPlanner = { navigateToTab(navController, Routes.PLANNER, Routes.planner()) },
                    onGoToFavorites = { navigateToTab(navController, Routes.FAVORITES, Routes.FAVORITES) }
                )
            }

            composable(Routes.STATIONS) {
                StationsScreen(
                    stations = stations,
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    connectorFilter = connectorFilter,
                    onConnectorFilterChange = { connectorFilter = it },
                    onlyAvailable = onlyAvailable,
                    onOnlyAvailableChange = { onlyAvailable = it },
                    favoriteIds = favoriteIds,
                    onStationClick = { navController.navigate(Routes.detail(it)) },
                    onToggleFavorite = ::toggleFavorite
                )
            }

            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    favoriteStations = stations.filter { it.id in favoriteIds },
                    onStationClick = { navController.navigate(Routes.detail(it)) },
                    onToggleFavorite = ::toggleFavorite,
                    onExploreStations = { navigateToTab(navController, Routes.STATIONS, Routes.STATIONS) }
                )
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument(Routes.STATION_ID_ARG) { type = NavType.IntType })
            ) { entry ->
                val stationId = entry.arguments?.getInt(Routes.STATION_ID_ARG)
                StationDetailScreen(
                    station = SampleData.findStation(stationId),
                    isFavorite = stationId != null && stationId in favoriteIds,
                    onToggleFavorite = { stationId?.let { toggleFavorite(it) } },
                    onPlanCharge = { navController.navigate(Routes.planner(stationId ?: Routes.NO_STATION)) }
                )
            }

            composable(
                route = Routes.PLANNER,
                arguments = listOf(
                    navArgument(Routes.STATION_ID_ARG) {
                        type = NavType.IntType
                        defaultValue = Routes.NO_STATION
                    }
                )
            ) { entry ->
                PlannerScreen(
                    stations = stations,
                    initialStationId = entry.arguments?.getInt(Routes.STATION_ID_ARG) ?: Routes.NO_STATION
                )
            }
        }
    }
}

/**
 * Navega a una pestaña de la barra inferior.
 * Si la pestaña ya está en la pila (por ejemplo, al volver desde un detalle) se regresa a ella;
 * si no, se navega con lanzamiento único para no apilar copias de la misma pantalla.
 */
private fun navigateToTab(navController: NavHostController, route: String, destination: String) {
    if (navController.popBackStack(route, inclusive = false)) return
    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
