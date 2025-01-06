package com.sebs.fitnessapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.application.LegendRFApp
import com.sebs.fitnessapp.data.LegendRepository
import com.sebs.fitnessapp.data.remote.model.LegendCategory
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.FragmentLegendListBinding
import com.sebs.fitnessapp.ui.UserLegendActivity
import com.sebs.fitnessapp.ui.adapters.CarouselAdapter
import com.sebs.fitnessapp.ui.adapters.LegendCategoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LegendListFragment : Fragment() {

    private var _binding: FragmentLegendListBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LegendRepository
    private lateinit var adapter: LegendCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLegendListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as LegendRFApp).repository

        val sharedPreferences = requireContext().getSharedPreferences("LegendPrefs", Context.MODE_PRIVATE)
        val userLegendImageUri = sharedPreferences.getString("legendImageUri", null)
        val userLegendName = sharedPreferences.getString("legendName", "N/A")

        val call: Call<MutableList<LegendDto>> = repository.getLegendsApiary()
        call.enqueue(object : Callback<MutableList<LegendDto>> {
            override fun onResponse(call: Call<MutableList<LegendDto>>, response: Response<MutableList<LegendDto>>) {
                binding.pbLoading.visibility = View.GONE
                response.body()?.let { legendList ->
                    // Crear leyenda del usuario y agregarla a la lista
                    val userLegend = LegendDto(
                        id = "user_legend",
                        title = userLegendName,
                        thumbnail = userLegendImageUri,
                        description = "Es conocido como el peligroso",
                        category = "Top Global"
                    )
                    legendList.add(userLegend)

                    // Agrupar por categorías
                    val groupedCategories = legendList.groupBy { it.category ?: "Sin categoría" }
                        .map { (category, legends) -> LegendCategory(category, legends) }

                    // Configurar RecyclerView
                    adapter = LegendCategoryAdapter(groupedCategories) { legend ->
                        if (legend.id == "user_legend") {
                            val intent = Intent(requireContext(), UserLegendActivity::class.java)
                            startActivity(intent)
                        } else {
                            legend.id?.let { id ->
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, LegendDetailFragment.newInstance(id))
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }
                    }

                    binding.rvGames.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = this@LegendListFragment.adapter
                    }

                    // Configurar carrusel
                    setupCarousel()
                }
            }

            override fun onFailure(call: Call<MutableList<LegendDto>>, t: Throwable) {
                binding.pbLoading.visibility = View.GONE
                Toast.makeText(requireContext(), "Error al cargar leyendas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupCarousel() {
        val imageResources = listOf(
            R.drawable.feature1,
            R.drawable.feature2,
            R.drawable.feature4
        )
        val carouselAdapter = CarouselAdapter(imageResources)
        binding.vpCarousel.adapter = carouselAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}