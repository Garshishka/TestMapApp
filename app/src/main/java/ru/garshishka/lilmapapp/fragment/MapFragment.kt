package ru.garshishka.lilmapapp.fragment

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.GpsStatus
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.garshishka.lilmapapp.R
import ru.garshishka.lilmapapp.databinding.FragmentMapBinding

class MapFragment : Fragment(), MapListener, GpsStatus.Listener {
    //Permission part
    private var locationPermission = false
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                locationPermission = true
            } else {
                showPermissionSnackbar()
            }
        }

    private val binding: FragmentMapBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private var focusOn = false

    lateinit var mMap: MapView
    lateinit var controller: IMapController
    lateinit var mMyLocationOverlay: MyLocationNewOverlay
    lateinit var mainMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkMapPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance().load(
            context,
            context?.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )

        setUpMap()

        setUpTaps()

        setUpButtons()

        return binding.root
    }

    private fun setUpTaps() {
        val tapOverlay = MapEventsOverlay(object : MapEventsReceiver{
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    if (!::mainMarker.isInitialized){
                        mainMarker = Marker(mMap)
                        mainMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        mainMarker.icon = requireContext().getDrawable(R.drawable.baseline_center_24)
                        mMap.overlays.add(mainMarker)
                    }
                    mainMarker.position = GeoPoint(p.latitude, p.longitude)
                    mMap.invalidate()
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                Log.d("singleTapConfirmedHelper", "${p?.latitude} - ${p?.longitude}")
                return true
            }

        })

        mMap.overlays.add(tapOverlay)
    }

    private fun setUpMap() {
        mMap = binding.osmmap
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mMap)
        controller = mMap.controller

        mMyLocationOverlay.enableMyLocation()
        //mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true

        controller.setZoom(17.5)

        val mapPoint = GeoPoint(55.748225, 37.625184)
        controller.animateTo(mapPoint)

        mMap.overlays.add(mMyLocationOverlay)

        mMap.addMapListener(this)
    }

    private fun setUpButtons() {
        binding.apply {
            centerButton.setOnClickListener {
                if (::mainMarker.isInitialized){
                    controller.animateTo(mainMarker.position)
                } else{
                    Toast.makeText(requireContext(),R.string.no_main_point,Toast.LENGTH_LONG).show()
                }
            }
            focusButton.setOnClickListener {
                it.setBackgroundColor(
                    if (focusOn) requireContext().getColor(R.color.focus_on) else requireContext().getColor(
                        R.color.focus_off
                    )
                )
                focusOn = !focusOn
            }
        }
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        Log.i("TAG", "onCreate:la ${event?.source?.getMapCenter()?.latitude}")
        Log.i("TAG", "onCreate:lo ${event?.source?.getMapCenter()?.longitude}")
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {

        Log.i("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        return false
    }

    override fun onGpsStatusChanged(event: Int) {
        TODO("Not yet implemented")
    }

    private fun checkMapPermission() {
        when {
            context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED -> {
                locationPermission = true
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showPermissionSnackbar()
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun showPermissionSnackbar() {
        Snackbar.make(binding.osmmap, getString(R.string.need_geolocation), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.permission)) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .show()
    }
}