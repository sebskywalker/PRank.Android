package com.sebs.fitnessapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.databinding.ActivityMainBinding
import com.sebs.fitnessapp.ui.fragments.LegendListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("LegendPrefs", MODE_PRIVATE)

        // Actualizar el texto del botón dinámicamente
        updateEditButton()

        // Mostrar fragment inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LegendListFragment())
                .commit()
        }

        // Configurar botón para cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Configurar funcionalidad dinámica para el botón
        binding.btnEditarPerfil.setOnClickListener {
            val isLegendCreated = sharedPreferences.getBoolean("isLegendCreated", false)
            if (isLegendCreated) {
                // Abrir vista de la leyenda creada
                val intent = Intent(this, UserLegendActivity::class.java)
                startActivity(intent)
            } else {
                // Abrir formulario para crear leyenda
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateEditButton() {
        val isLegendCreated = sharedPreferences.getBoolean("isLegendCreated", false)
        binding.btnEditarPerfil.text = if (isLegendCreated) "Ver Leyenda" else "Editar Perfil"
    }

    override fun onResume() {
        super.onResume()
        // Actualizar el botón cada vez que la actividad vuelva a ser visible
        updateEditButton()
    }
}