package com.sebs.fitnessapp.data

import com.sebs.fitnessapp.data.remote.LegendAPI
import com.sebs.fitnessapp.data.remote.model.LegendDetailsDto
import com.sebs.fitnessapp.data.remote.model.LegendDto
import com.sebs.fitnessapp.data.remote.model.RankingDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LegendRepository(private val retrofit: Retrofit) {

    private val legendAPI: LegendAPI = retrofit.create(LegendAPI::class.java)
    private val userLegends: MutableList<LegendDto> = mutableListOf()

    fun getLegendsApiary(): Call<MutableList<LegendDto>> = legendAPI.getLegendsApiary()

    fun getLegendDatailApiary(id: String?): Call<LegendDetailsDto> = legendAPI.getLegendDetailApiary(id)

    fun addUserLegend(legend: LegendDto) {
        legend.assignCategory()  // Asigna categoría
        userLegends.add(legend)
    }

    fun getAllLegends(): MutableList<LegendDto> {
        val legends = mutableListOf<LegendDto>()
        legends.addAll(userLegends)
        return legends
    }

    fun getBenchPressRankings(callback: Callback<MutableList<RankingDto>>) {
        legendAPI.getLegendsApiary().enqueue(object : Callback<MutableList<LegendDto>> {
            override fun onResponse(
                call: Call<MutableList<LegendDto>>,
                response: Response<MutableList<LegendDto>>
            ) {
                if (response.isSuccessful) {
                    val rankings = response.body()?.map {
                        RankingDto(
                            name = it.title ?: "Desconocido",
                            imageUrl = it.thumbnail ?: "",
                            score = it.prBenchPress?.toIntOrNull() ?: 0
                        )
                    }?.sortedByDescending { it.score }?.toMutableList() ?: mutableListOf()

                    callback.onResponse(call as Call<MutableList<RankingDto>>, Response.success(rankings))
                } else {
                    callback.onFailure(
                        call as Call<MutableList<RankingDto>>,
                        Throwable("❌ Error al cargar los rankings")
                    )
                }
            }

            override fun onFailure(call: Call<MutableList<LegendDto>>, t: Throwable) {
                callback.onFailure(call as Call<MutableList<RankingDto>>, t)
            }
        })
    }
}
