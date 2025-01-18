package com.sebs.fitnessapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.sebs.fitnessapp.data.remote.model.LegendCategory
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.FragmentLegendListBinding
import com.sebs.fitnessapp.ui.adapters.CarouselAdapter
import com.sebs.fitnessapp.ui.adapters.LegendCategoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LegendListFragment : Fragment() {

    private var _binding: FragmentLegendListBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LegendRepository
    private lateinit var sharedPreferences: SharedPreferences
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

        sharedPreferences = requireContext().getSharedPreferences("LegendPrefs", Context.MODE_PRIVATE)
        repository = (requireActivity().application as LegendRFApp).repository

        loadLegends()
    }

    private fun loadLegends() {
        val call: Call<MutableList<LegendDto>> = repository.getLegendsApiary()
        call.enqueue(object : Callback<MutableList<LegendDto>> {
            override fun onResponse(call: Call<MutableList<LegendDto>>, response: Response<MutableList<LegendDto>>) {
                binding.pbLoading.visibility = View.GONE
                response.body()?.let { legendList ->
                    val userLegend = loadUserLegendFromPreferences()
                    if (userLegend != null) {
                        legendList.add(userLegend)  // Agregar la leyenda personalizada
                    }

                    val groupedCategories = legendList.groupBy { it.category ?: "Sin categoría" }
                        .map { (category, legends) -> LegendCategory(category, legends) }
                        .toMutableList()  // **Conversión a MutableList**

                    adapter = LegendCategoryAdapter(groupedCategories) { legend ->
                        legend.id?.let { id ->
                            if (legend.isUserCreated || id == "user_legend") {
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, UserLegendFragment())
                                    .addToBackStack(null)
                                    .commit()
                            } else {
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
        val imageResources = listOf(R.drawable.feature1, R.drawable.feature2, R.drawable.feature4)
        val carouselAdapter = CarouselAdapter(imageResources)
        binding.vpCarousel.adapter = carouselAdapter
    }

    private fun loadUserLegendFromPreferences(): LegendDto? {
        val name = sharedPreferences.getString("legendName", null) ?: return null
        val alias = sharedPreferences.getString("legendAlias", null) ?: return null
        val description = sharedPreferences.getString("legendDescription", null) ?: return null
        val benchPress = sharedPreferences.getString("legendBenchPress", "0") ?: "0"
        val squat = sharedPreferences.getString("legendSquat", "0") ?: "0"
        val deadlift = sharedPreferences.getString("legendDeadlift", "0") ?: "0"
        val imageUri = sharedPreferences.getString("legendImageUri", null)

        val category = when (benchPress.toIntOrNull() ?: 0) {
            in 100..Int.MAX_VALUE -> "Leyenda"
            in 50..99 -> "Top Global"
            else -> "Profesional"
        }

        return LegendDto(
            id = "user_${name}_${alias}",
            title = "$name $alias",
            thumbnail = imageUri,
            description = description,
            prBenchPress = benchPress,
            category = category,
            isUserCreated = true
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}