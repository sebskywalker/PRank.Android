package com.sebs.fitnessapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.sebs.fitnessapp.databinding.FragmentRankingBinding

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private val legends = listOf(
        LegendData("Arnold", 300, 320, 350, "Legend"),
        LegendData("Ron Coleman", 290, 315, 340, "Legend"),
        LegendData("Silvester Stallone", 280, 310, 330, "Legend"),
        LegendData("Dorian Yates", 285, 300, 320, "Top Global"),
        LegendData("Franco Columbo", 280, 290, 310, "Top Global"),
        LegendData("Phil Heldt", 275, 280, 300, "Professional"),
        LegendData("Jay Cutler", 270, 275, 295, "Professional"),
        LegendData("Kay Green", 250, 265, 290, "Professional")
    )

    private val userPRs = LegendData("Mi Usuario", 235, 245, 255, "User")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showChart("Benchpress")

        binding.btnBenchpress.setOnClickListener { showChart("Benchpress") }
        binding.btnSquat.setOnClickListener { showChart("Squat") }
        binding.btnDeadlift.setOnClickListener { showChart("Deadlift") }
    }

    private fun showChart(type: String) {
        val entries = legends.mapIndexed { index, legend ->
            val value = when (type) {
                "Benchpress" -> legend.benchpress
                "Squat" -> legend.squat
                "Deadlift" -> legend.deadlift
                else -> 0
            }
            BarEntry(index.toFloat(), value.toFloat())
        }.toMutableList()

        // Agregar barra del usuario
        val userValue = when (type) {
            "Benchpress" -> userPRs.benchpress
            "Squat" -> userPRs.squat
            "Deadlift" -> userPRs.deadlift
            else -> 0
        }
        entries.add(BarEntry(entries.size.toFloat(), userValue.toFloat()))

        val colors = legends.map {
            when (it.category) {
                "Legend" -> Color.RED
                "Top Global" -> Color.YELLOW
                "Professional" -> Color.GREEN
                else -> Color.GRAY
            }
        }.toMutableList()
        colors.add(Color.MAGENTA)

        val dataSet = BarDataSet(entries, "$type PRs").apply {
            this.colors = colors
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }

        val barData = BarData(dataSet)
        binding.barChart.apply {
            data = barData
            description = Description().apply { text = "$type Rankings" }
            setFitBars(true)
            setDrawValueAboveBar(true)
            setScaleEnabled(false)
            animateY(1000)
            invalidate()
        }

        // Configurar etiquetas
        binding.labelLeyenda.text = formatLabel("Leyenda:", "Rojo", Color.RED)
        binding.labelTopGlobal.text = formatLabel("Top Global:", "Amarillo", Color.YELLOW)
        binding.labelProfesional.text = formatLabel("Profesional:", "Verde", Color.GREEN)
        binding.labelUsuario.text = formatLabel("Usuario:", "Magenta", Color.MAGENTA)
    }

    private fun formatLabel(baseText: String, colorText: String, color: Int): Spannable {
        val spannable = SpannableString("$baseText $colorText")
        val colorStart = baseText.length + 1
        spannable.setSpan(
            ForegroundColorSpan(color),
            colorStart,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class LegendData(
        val name: String,
        val benchpress: Int,
        val squat: Int,
        val deadlift: Int,
        val category: String
    )
}