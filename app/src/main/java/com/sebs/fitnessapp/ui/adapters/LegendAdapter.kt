package com.sebs.fitnessapp.ui.adapters

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.LegendElementBinding

class LegendAdapter(
    private val legend: MutableList<LegendDto>,
    private val onLegendDto: (LegendDto) -> Unit,
    private val context: Context // Se agrega el contexto para usar MediaPlayer
) : RecyclerView.Adapter<LegendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendViewHolder {
        val binding = LegendElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LegendViewHolder(binding)
    }

    override fun getItemCount(): Int = legend.size

    override fun onBindViewHolder(holder: LegendViewHolder, position: Int) {

        val legend = legend[position]

        holder.bind(legend)

        holder.itemView.setOnClickListener {
            // Para los clicks en las leyendas
            onLegendDto(legend)

            // Reproducir el audio desde la carpeta raw
            val mediaPlayer = MediaPlayer.create(context, R.raw.yeahbuddy)
            mediaPlayer.start()

            // Liberar recursos después de la reproducción
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
        }
    }
}

