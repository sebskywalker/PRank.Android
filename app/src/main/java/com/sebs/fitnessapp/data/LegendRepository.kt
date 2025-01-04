package com.sebs.fitnessapp.data

import com.sebs.fitnessapp.data.remote.LegendAPI
import com.sebs.fitnessapp.data.remote.model.LegendDetailsDto
import com.sebs.fitnessapp.data.remote.model.LegendDto
import retrofit2.Call
import retrofit2.Retrofit

class LegendRepository(private val retrofit: Retrofit) {

    private val legendAPI: LegendAPI = retrofit.create(LegendAPI::class.java)

    private val userLegends: MutableList<LegendDto> = mutableListOf()

    fun getLegendsApiary(): Call<MutableList<LegendDto>> = legendAPI.getLegendsApiary()

    fun getLegendDatailApiary(id: String?): Call<LegendDetailsDto> = legendAPI.getLegendDetailApiary(id)

    fun addUserLegend(legend: LegendDto) {
        userLegends.add(legend)
    }

    fun getAllLegends(): MutableList<LegendDto> {
        val legends = mutableListOf<LegendDto>()
        legends.addAll(userLegends)
        return legends
    }
}
