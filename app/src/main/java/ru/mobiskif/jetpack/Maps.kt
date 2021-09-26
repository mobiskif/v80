package ru.mobiskif.jetpack

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val mapcallback = OnMapReadyCallback { map->
    map.uiSettings.isZoomControlsEnabled = true
}

fun getCoord(address: String, context: Context): LatLng {
    val location = Geocoder(context).getFromLocationName(address, 5)[0]
    return LatLng(location.latitude, location.longitude)
}

@Composable
fun Mymap(lpu: Lpu) {
    val context = LocalContext.current
    if (!lpu.address.isNullOrBlank()) {
        val v = rememberMapViewWithLifecycle()
        AndroidView({ v }) {
            CoroutineScope(Dispatchers.Main).launch {
                it.getMapAsync { map ->
                    val coord = getCoord(lpu.address!!, context)
                    val markerOptions = MarkerOptions()
                        .title("${lpu.phone}")
                        .position(coord)
                    map.addMarker(markerOptions)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 12f))
                }
            }
        }
    }
}


@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)//.apply { id = R.id.map }
    }

    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }

    mapView.getMapAsync(mapcallback)
    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

