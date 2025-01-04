package com.sebs.fitnessapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sebs.fitnessapp.databinding.ActivityUserLegendBinding

class UserLegendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLegendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewBinding
        binding = ActivityUserLegendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener los datos de SharedPreferences
        val sharedPreferences = getSharedPreferences("LegendPrefs", MODE_PRIVATE)
        binding.tvLegendName.text = sharedPreferences.getString("legendName", "N/A")
        binding.tvLegendAlias.text = sharedPreferences.getString("legendAlias", "N/A")
        binding.tvLegendDescription.text = sharedPreferences.getString("legendDescription", "N/A")
        binding.tvLegendBirthdate.text = sharedPreferences.getString("legendBirthdate", "N/A")
        binding.tvLegendOccupation.text = sharedPreferences.getString("legendOccupation", "N/A")
        binding.tvLegendBenchPressPR.text = sharedPreferences.getString("legendBenchPress", "N/A")
        binding.tvLegendSquatPR.text = sharedPreferences.getString("legendSquat", "N/A")
        binding.tvLegendDeadliftPR.text = sharedPreferences.getString("legendDeadlift", "N/A")

        // Cargar la imagen seleccionada
        val imageUriString = sharedPreferences.getString("legendImageUri", null)
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            Glide.with(this)
                .load(imageUri)
                .into(binding.ivLegendImage) // Cambia "ivLegendImage" al ID de tu ImageView
        }

        // Configurar el botón "Home"
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configurar el botón "Cerrar sesión"
        binding.btnCerrarSesion.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configurar el botón "Editar"
        binding.btnEditar.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}