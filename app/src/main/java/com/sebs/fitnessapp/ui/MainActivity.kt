package com.sebs.fitnessapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sebs.fitnessapp.R
import com.sebs.fitnessapp.data.LegendRepository
import com.sebs.fitnessapp.data.remote.RetrofitHelper
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.databinding.ActivityMainBinding

import com.sebs.fitnessapp.ui.fragments.LegendListFragment
import com.sebs.fitnessapp.utilis.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Mostrar fragment inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LegendListFragment())
                .commit()
        }

        // Configurar botón para cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut() // Cerrar la sesión del usuario
            startActivity(Intent(this, LoginActivity::class.java)) // Redirigir a LoginActivity
            Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
            finish() // Finalizar actividad actual
        }
    }

    /*
    //obteniendo la instancia de retrofit
    private lateinit var repository: LegendRepository
    private lateinit var retrofit: Retrofit

    fun click(view: View) {
        retrofit = RetrofitHelper().getRetrofit()
        repository = LegendRepository(retrofit)

        val call: Call<MutableList<LegendDto>> = repository.getLegendsApiary("legend/legends_list")

        call.enqueue(object : Callback<MutableList<LegendDto>> {
            override fun onResponse(
                call: Call<MutableList<LegendDto>>,
                response: Response<MutableList<LegendDto>>
            ) {
                Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.body()}")
            }

            override fun onFailure(p0: Call<MutableList<LegendDto>>, p1: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: No hay conexión",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    */
}