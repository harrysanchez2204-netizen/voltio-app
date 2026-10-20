package edu.utp.evchargemanager.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import edu.utp.evchargemanager.R
import edu.utp.evchargemanager.data.model.Station

/**
 * Devuelve la ilustración (res/drawable) que corresponde a cada estación.
 * Se resuelve aquí, en la capa de UI, para que el modelo de datos no dependa de Android.
 */
@DrawableRes
fun stationImageRes(stationId: Int): Int = when (stationId) {
    1 -> R.drawable.estacion_1
    2 -> R.drawable.estacion_2
    3 -> R.drawable.estacion_3
    4 -> R.drawable.estacion_4
    5 -> R.drawable.estacion_5
    6 -> R.drawable.estacion_6
    7 -> R.drawable.estacion_7
    else -> R.drawable.estacion_8
}

/** Imagen de portada de una estación, recortada a proporción 16:9. */
@Composable
fun StationImage(station: Station, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(stationImageRes(station.id)),
        contentDescription = "Ilustración de la estación ${station.name}",
        contentScale = ContentScale.Crop,
        modifier = modifier.aspectRatio(16f / 9f)
    )
}
