package com.sebs.fitnessapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
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

class LegendDatailFragment : Fragment() {

    private var legendId: String? = null
    private var _binding: FragmentLegendDatailBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LegendRepository
    private lateinit var wvVideo: WebView

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
    ): View? {
        _binding = FragmentLegendDatailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instancia del repo
        repository = (requireActivity().application as LegendRFApp).repository

        // Inicializar WebView
        wvVideo = binding.wvVideo
        wvVideo.settings.javaScriptEnabled = true
        wvVideo.settings.loadWithOverviewMode = true
        wvVideo.settings.useWideViewPort = true
        wvVideo.webViewClient = WebViewClient()

        legendId?.let { id ->
            val call: Call<LegendDetailsDto> = repository.getLegendDatailApiary(id)
            call.enqueue(object : Callback<LegendDetailsDto> {
                override fun onResponse(p0: Call<LegendDetailsDto>, response: Response<LegendDetailsDto>) {

                    // Éxito
                    binding.apply {
                        pbLoading.visibility = View.GONE
                        val legend = response.body()

                        tvName.text = legend?.name
                        tvAlias.text = legend?.alias ?: "Alias not available"
                        tvBirthdate.text = legend?.birthdate ?: "Birthdate not available"
                        tvOccupation.text = legend?.occupation ?: "Occupation not available"
                        tvPRBenchPress.text = legend?.prBenchPress ?: "PR not available"
                        tvPRSquat.text = legend?.prSquat ?: "PR not available"
                        tvPRDeadlift.text = legend?.prDeadlift ?: "PR not available"
                        tvLongDesc.text = legend?.description ?: "Description not available"

                        Glide.with(requireActivity())
                            .load(legend?.image)
                            .into(ivImage)

                        // Configurar y cargar el video
                        legend?.videoUrl?.let { url ->
                            val videoEmbedUrl = "https://www.youtube.com/embed/${extractVideoId(url)}"
                            wvVideo.loadUrl(videoEmbedUrl)
                        } ?: run {
                            wvVideo.loadUrl("about:blank")
                        }
                    }
                }

                override fun onFailure(p0: Call<LegendDetailsDto>, p1: Throwable) {
                    // Manejo de errores
                }
            })
        }
    }

    private fun extractVideoId(youtubeUrl: String): String {
        val regex = "v=([^&]+)".toRegex()
        val matchResult = regex.find(youtubeUrl)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(legendId: String) =
            LegendDatailFragment().apply {
                arguments = Bundle().apply {
                    putString(LEGEND_ID, legendId)
                }
            }
    }
}