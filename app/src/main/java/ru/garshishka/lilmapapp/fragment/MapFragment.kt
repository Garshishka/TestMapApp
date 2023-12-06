package ru.garshishka.lilmapapp.fragment

import android.content.Context.MODE_PRIVATE
import android.graphics.Rect
import android.location.GpsStatus
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
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
import org.osmdroid.views.overlay.Polyline
import ru.garshishka.lilmapapp.R
import ru.garshishka.lilmapapp.databinding.FragmentMapBinding

class MapFragment : Fragment(), MapListener, GpsStatus.Listener {
    private val binding: FragmentMapBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private var focusOn = false

    lateinit var mMap: MapView
    lateinit var controller: IMapController
    lateinit var mainMarker: Marker
    lateinit var secondMarker: Marker
    lateinit var line: Polyline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val tapOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    if (focusOn) {
                        if (!::secondMarker.isInitialized) {
                            secondMarker = Marker(mMap)
                            secondMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                            secondMarker.icon =
                                requireContext().getDrawable(R.drawable.baseline_second_location_24)
                            mMap.overlays.add(secondMarker)
                            line = Polyline(mMap)
                            line.setPoints(listOf(mainMarker.position, secondMarker.position))
                            mMap.overlays.add(line);
                        }
                        secondMarker.position = GeoPoint(p.latitude, p.longitude)
                        line.setPoints(listOf(mainMarker.position, secondMarker.position))
                    } else {
                        if (!::mainMarker.isInitialized) {
                            mainMarker = Marker(mMap)
                            mainMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                            mainMarker.icon =
                                requireContext().getDrawable(R.drawable.baseline_location_24)
                            mMap.overlays.add(mainMarker)
                        }
                        mainMarker.position = GeoPoint(p.latitude, p.longitude)
                        if (::secondMarker.isInitialized && ::line.isInitialized) {
                            line.setPoints(listOf(mainMarker.position, secondMarker.position))
                        }
                    }
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

        controller = mMap.controller

        controller.setZoom(17.5)

        val mapPoint = GeoPoint(55.748225, 37.625184)
        controller.animateTo(mapPoint)

        mMap.addMapListener(this)

        if (::mainMarker.isInitialized){
            val pos = mainMarker.position.latitude
            mainMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            mainMarker.icon =
                requireContext().getDrawable(R.drawable.baseline_location_24)
            mMap.overlays.add(mainMarker)
        }
        if (::secondMarker.isInitialized){
            secondMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            secondMarker.icon =
                requireContext().getDrawable(R.drawable.baseline_second_location_24)
            mMap.overlays.add(secondMarker)
            line = Polyline(mMap)
            line.setPoints(listOf(mainMarker.position, secondMarker.position))
            mMap.overlays.add(line);
        }
        mMap.invalidate()
    }

    private fun setUpButtons() {
        binding.apply {
            centerButton.setOnClickListener {
                if (::mainMarker.isInitialized) {
                    controller.animateTo(mainMarker.position)
                } else {
                    Toast.makeText(requireContext(), R.string.no_main_point, Toast.LENGTH_LONG)
                        .show()
                }
            }
            focusButton.setBackgroundColor(
                if (!focusOn) requireContext().getColor(R.color.focus_on) else requireContext().getColor(
                    R.color.focus_off
                )
            )
            focusButton.setOnClickListener {
                if (::mainMarker.isInitialized) {
                    focusOn = !focusOn
                    it.setBackgroundColor(
                        if (!focusOn) requireContext().getColor(R.color.focus_on) else requireContext().getColor(
                            R.color.focus_off
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), R.string.no_main_point, Toast.LENGTH_LONG)
                        .show()
                }
            }
            menuButton.setOnClickListener {
                findNavController().navigate(R.id.action_mapFragment_to_menuFragment)
            }
        }
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }

    override fun onGpsStatusChanged(event: Int) {
    }
}