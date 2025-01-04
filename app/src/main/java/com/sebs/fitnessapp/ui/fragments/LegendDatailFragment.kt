package com.sebs.fitnessapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.application.LegendRFApp
import com.sebs.fitnessapp.data.LegendRepository
import com.sebs.fitnessapp.data.remote.model.LegendDetailsDto
import com.sebs.fitnessapp.databinding.FragmentLegendDatailBinding
import com.sebs.fitnessapp.utilis.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val LEGEND_ID = "legend_id"

class LegendDetailFragment : Fragment(), OnMapReadyCallback {

    private var legendId: String? = null
    private var _binding: FragmentLegendDatailBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LegendRepository
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            legendId = args.getString(LEGEND_ID)
            Log.d(Constants.LOGTAG, "ID recibido $legendId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLegendDatailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as LegendRFApp).repository

        // Configurar el mapa
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Determinar si es la leyenda del usuario o una de la API
        if (legendId == "user_legend") {
            loadUserLegendDetails()
        } else {
            loadLegendDetailsFromAPI()
        }
    }

    private fun loadUserLegendDetails() {
        binding.pbLoading.visibility = View.GONE
        binding.apply {
            tvName.text = "Sebastian Verastegui"
            tvAlias.text = "El Papu"
            tvBirthdate.text = "02/07/1999"
            tvOccupation.text = "Fitness Influencer"
            tvPRBenchPress.text = "110 kg"
            tvPRSquat.text = "100 kg"
            tvPRDeadlift.text = "100 kg"
            tvLongDesc.text = "Conocido como el peligroso, destacado por su pasión en el gimnasio."

            Glide.with(requireActivity())
                .load("https://example.com/user_thumbnail.jpg") // URL de ejemplo o imagen local
                .placeholder(R.drawable.person) // Imagen genérica
                .into(ivImage)

            wvVideo.loadUrl("about:blank") // No hay video para la leyenda creada

            val defaultLocation = LatLng(19.4326, -99.1332) // Coordenadas de ejemplo (Ciudad de México)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
            addCustomMarker(defaultLocation)
        }
    }

    private fun loadLegendDetailsFromAPI() {
        legendId?.let { id ->
            val call: Call<LegendDetailsDto> = repository.getLegendDatailApiary(id)
            call.enqueue(object : Callback<LegendDetailsDto> {
                override fun onResponse(p0: Call<LegendDetailsDto>, response: Response<LegendDetailsDto>) {
                    binding.pbLoading.visibility = View.GONE
                    val legend = response.body()
                    Log.d(Constants.LOGTAG, "Respuesta de la API: $legend") // Log para depuración

                    binding.apply {
                        tvName.text = legend?.name ?: "No disponible"
                        tvAlias.text = legend?.alias ?: "Alias no disponible"
                        tvBirthdate.text = legend?.birthdate ?: "Fecha no disponible"
                        tvOccupation.text = legend?.occupation ?: "Ocupación no disponible"
                        tvPRBenchPress.text = legend?.prBenchPress ?: "PR no disponible"
                        tvPRSquat.text = legend?.prSquat ?: "PR no disponible"
                        tvPRDeadlift.text = legend?.prDeadlift ?: "PR no disponible"
                        tvLongDesc.text = legend?.description ?: "Descripción no disponible"

                        Glide.with(requireActivity())
                            .load(legend?.image)
                            .into(ivImage)

                        legend?.videoUrl?.let { url ->
                            val videoEmbedUrl = "https://www.youtube.com/embed/${extractVideoId(url)}"
                            wvVideo.apply {
                                settings.javaScriptEnabled = true
                                settings.loadWithOverviewMode = true
                                settings.useWideViewPort = true
                                webViewClient = WebViewClient()
                                loadUrl(videoEmbedUrl)
                            }
                        } ?: run {
                            wvVideo.loadUrl("about:blank")
                        }

                        legend?.coordinates?.let {
                            val location = LatLng(it.latitude, it.longitude)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
                            addCustomMarker(location)
                        } ?: run {
                            Log.e(Constants.LOGTAG, "Coordenadas no disponibles para esta leyenda")
                        }
                    }
                }

                override fun onFailure(p0: Call<LegendDetailsDto>, p1: Throwable) {
                    Log.e(Constants.LOGTAG, "Error al cargar detalles: ${p1.localizedMessage}")
                }
            })
        }
    }

    private fun extractVideoId(youtubeUrl: String): String {
        val regex = "v=([^&]+)".toRegex()
        val matchResult = regex.find(youtubeUrl)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    private fun addCustomMarker(location: LatLng) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title("Ubicación de la leyenda")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.person)) // Reemplaza con tu imagen personalizada
        map.addMarker(markerOptions)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Configuración inicial del mapa
        val defaultLocation = LatLng(0.0, 0.0)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 1f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(legendId: String) =
            LegendDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(LEGEND_ID, legendId)
                }
            }
    }
}