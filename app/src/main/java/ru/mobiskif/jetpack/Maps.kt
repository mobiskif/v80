package ru.mobiskif.jetpack

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
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val mapcallback = OnMapReadyCallback { map->
    map.uiSettings.isZoomControlsEnabled = true

    val opera =  LatLng(-35.016, 143.321)
    val hubert = LatLng(-32.491, 147.309)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(hubert,5.8f))

    val markerOptions = MarkerOptions()
        .title("Sydney Opera House")
        .position(opera)
    map.addMarker(markerOptions)

    val markerOptionsDestination = MarkerOptions()
        .title("Restaurant Hubert")
        .position(hubert)
    map.addMarker(markerOptionsDestination)

    map.addPolyline(
        PolylineOptions().add( opera,
            LatLng(-34.747, 145.592),
            LatLng(-34.364, 147.891),
            LatLng(-33.501, 150.217),
            LatLng(-32.306, 149.248),
            hubert))
}

fun getCoord(address: String): LatLng {
    return LatLng(-35.016, 143.321)
}

@Composable
fun mymap(address: String?) {
    if (!address.isNullOrBlank()) {
        val v = rememberMapViewWithLifecycle()
        AndroidView({ v }) {
            CoroutineScope(Dispatchers.Main).launch {
                it.getMapAsync { map ->
                    val opera = getCoord(address)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(opera, 7f))
                }
            }
        }
    }
}


@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
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

