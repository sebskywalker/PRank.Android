package com.sebs.fitnessapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        // Configurar el botón "Home"
        binding.btnHome.setOnClickListener {
            // Regresar a la pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerrar la actividad actual
        }

        // Configurar el botón "Cerrar sesión"
        binding.btnCerrarSesion.setOnClickListener {
            // Limpiar los datos de SharedPreferences si es necesario
            sharedPreferences.edit().clear().apply()

            // Redirigir al LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finalizar la actividad actual
        }

        // Configurar el botón "Editar"
        binding.btnEditar.setOnClickListener {
            // Abrir la actividad de edición (ProfileActivity)
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}