package com.sebs.fitnessapp.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.application.LegendRFApp
import com.sebs.fitnessapp.data.LegendRepository
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.FragmentLegendListBinding
import com.sebs.fitnessapp.ui.adapters.CarouselAdapter
import com.sebs.fitnessapp.ui.adapters.LegendAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LegendListFragment : Fragment() {

    private var _binding: FragmentLegendListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LegendRepository
    private var mediaPlayer: MediaPlayer? = null // Instancia para manejar el audio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLegendListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del carrusel
        val imageResources = listOf(
            R.drawable.feature1,
            R.drawable.feature2,
            R.drawable.feature4
        )

        val carouselAdapter = CarouselAdapter(imageResources)
        binding.vpCarousel.adapter = carouselAdapter

        // Configuración de la lista de leyendas
        repository = (requireActivity().application as LegendRFApp).repository

        val call: Call<MutableList<LegendDto>> = repository.getLegendsApiary("legend/legends_list")

        call.enqueue(object : Callback<MutableList<LegendDto>> {
            override fun onResponse(
                p0: Call<MutableList<LegendDto>>,
                response: Response<MutableList<LegendDto>>
            ) {
                binding.pbLoading.visibility = View.GONE
                response.body()?.let { legendList ->
                    binding.rvGames.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = LegendAdapter(legendList, { legend ->
                            legend.id?.let { id ->
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, LegendDetailFragment.newInstance(id))
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }, requireContext())
                    }
                }
            }

            override fun onFailure(p0: Call<MutableList<LegendDto>>, p1: Throwable) {
                Toast.makeText(requireContext(), "Error: no hay conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}