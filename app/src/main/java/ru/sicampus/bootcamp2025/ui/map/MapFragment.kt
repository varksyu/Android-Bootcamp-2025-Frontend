package ru.sicampus.bootcamp2025.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2025.R
import ru.sicampus.bootcamp2025.databinding.FragmentMapBinding
import ru.sicampus.bootcamp2025.domain.center.CenterEntity
import ru.sicampus.bootcamp2025.ui.centerCard.CenterCardFragment
import ru.sicampus.bootcamp2025.ui.profile.ProfileViewModel
import ru.sicampus.bootcamp2025.utils.collectWithLifecycle

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private var _viewBinding: FragmentMapBinding? = null
    private val viewBinding get() = _viewBinding!!


    private var cachedCenters: List<CenterEntity>? = null

    private lateinit var googleMap: GoogleMap
    private val viewModel: MapViewModel by viewModels {MapViewModel.Factory}

    private val markers = mutableMapOf<String, Marker>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentMapBinding.bind(view)

        initializeMap()
        viewModel.state.collectWithLifecycle(this) { state ->

            viewBinding.error.visibility = if (state is MapViewModel.State.Error) View.VISIBLE else View.GONE
            viewBinding.loading.visibility = if (state is MapViewModel.State.Loading) View.VISIBLE else View.GONE
            viewBinding.content.visibility = if (state is MapViewModel.State.Show) View.VISIBLE else View.GONE

            when(state) {
                is MapViewModel.State.Loading -> Unit
                is MapViewModel.State.Show -> {
                    cachedCenters = state.items
                    if (::googleMap.isInitialized) {
                        addMarkersToMap(state.items)
                    }
                }
                is MapViewModel.State.Error -> {
                    viewBinding.errorText.text = state.text
                }
            }

        }

    }

    private fun initializeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMarkerClickListener { marker ->
            //Toast.makeText(requireContext(), "Загрузка центра", Toast.LENGTH_LONG).show()
            val centerId = marker.tag as? Long
            val centerName = marker.title as? String
            if (centerId != null && centerName != null) {
                val fragment = CenterCardFragment().apply {
                    arguments = Bundle().apply {
                        putLong("centerId", centerId)
                        putString("centerName", centerName)
                    }
                }
                fragment.show(parentFragmentManager, "CenterCardFragment")

            }
            true


        }

        cachedCenters?.let { centers ->
            addMarkersToMap(centers)
        }
    }
    private fun addMarkersToMap(centers: List<CenterEntity>) {
        markers.clear()
        googleMap.clear()
        centers.forEach { center ->
            val latLng = LatLng(center.lat ?: 0.0, center.lng ?: 0.0)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(center.name)
            )
            //marker?.showInfoWindow()

            marker?.tag = center.id
            marker?.let { markers[center.id.toString()] = it }

        }
        if (centers.isNotEmpty()) {
            val firstCenter = centers.first()
            val firstLatLng = LatLng(firstCenter.lat ?: 0.0, firstCenter.lng ?: 0.0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 14f))
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}