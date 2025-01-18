package com.sebs.fitnessapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.databinding.FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val gyms = listOf(
        GymData("Gym Independencia", LatLng(19.432608, -99.133209), "Arnold Schwarzenegger"),
        GymData("Fitness Pro", LatLng(19.434688, -99.140652), "Ronnie Coleman"),
        GymData("Miguel Gym", LatLng(19.436348, -99.142679), "Phil Heath"),
        GymData("Fit Gym", LatLng(19.430289, -99.135403), "Dorian Yates"),
        GymData("Power Gym", LatLng(19.431998, -99.128978), "Jay Cutler")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        setupSpinner()
    }

    private fun setupSpinner() {
        val spinner: Spinner = binding.gymSelector
        val gymNames = gyms.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gymNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGym = gyms[position]
                moveCameraToGym(selectedGym)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }
    }

    private fun moveCameraToGym(gym: GymData) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gym.location, 16f))
        Toast.makeText(requireContext(), "Ubicando: ${gym.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Aplicar estilo personalizado
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_minimal)
        )
        if (!success) {
            Toast.makeText(requireContext(), "Error al cargar el estilo del mapa", Toast.LENGTH_SHORT).show()
        }

        addMarkers()
    }

    private fun addMarkers() {
        gyms.forEach { gym ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(gym.location)
                    .title(gym.name)
                    .snippet("Líder: ${gym.leader}")
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class GymData(
        val name: String,
        val location: LatLng,
        val leader: String
    )
}