package com.sebs.fitnessapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sebs.fitnessapp.databinding.ActivityMainBinding
import com.sebs.fitnessapp.ui.fragments.LegendListFragment
import com.google.firebase.auth.FirebaseAuth
import com.sebs.fitnessapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences y FirebaseAuth
        sharedPreferences = getSharedPreferences("LegendPrefs", MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()

        // Mostrar fragment inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LegendListFragment())
                .commit()
        }

        // Configurar botones
        setupButtons()
    }

    private fun setupButtons() {
        // Configurar botón para cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            handleLogout()
        }

        // Configurar botón para editar perfil o ver leyenda
        binding.btnEditarPerfil.setOnClickListener {
            val isLegendCreated = sharedPreferences.getBoolean("isLegendCreated", false)
            val intent = if (isLegendCreated) {
                // Abrir vista de la leyenda creada
                Intent(this, UserLegendActivity::class.java)
            } else {
                // Abrir formulario para crear leyenda
                Intent(this, ProfileActivity::class.java)
            }
            startActivity(intent)
        }

        // Configurar botón Home (opcional)
        binding.btnHome.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LegendListFragment())
                .commit()
        }
    }

    private fun handleLogout() {
        // Guardar los datos de la leyenda personalizada
        val legendData = sharedPreferences.all.filterKeys { it.startsWith("legend") }

        // Limpiar SharedPreferences
        sharedPreferences.edit().clear().apply()

        // Restaurar los datos de la leyenda personalizada
        val editor = sharedPreferences.edit()
        for ((key, value) in legendData) {
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Float -> editor.putFloat(key, value)
                is Long -> editor.putLong(key, value)
            }
        }
        editor.apply()

        // Cerrar sesión en Firebase
        firebaseAuth.signOut()

        // Mostrar mensaje de confirmación
        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()

        // Redirigir al LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finalizar la actividad actual
    }
}