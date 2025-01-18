package com.sebs.fitnessapp.data.remote

import com.sebs.fitnessapp.data.remote.model.LegendDetailsDto
import com.sebs.fitnessapp.data.remote.model.LegendDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LegendAPI {

    // ✅ Obtener la lista de leyendas desde Apiary
    @GET("legend/legends_list")
    fun getLegendsApiary(): Call<MutableList<LegendDto>>

    // ✅ Obtener detalles de una leyenda
    @GET("legend/legend_detail/{id}")
    fun getLegendDetailApiary(
        @Path("id") id: String?
    ): Call<LegendDetailsDto>
}