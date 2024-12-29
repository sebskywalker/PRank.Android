package com.sebs.fitnessapp.data

import com.sebs.fitnessapp.data.remote.LegendAPI
import com.sebs.fitnessapp.data.remote.model.LegendDetailsDto
import com.sebs.fitnessapp.data.remote.model.LegendDto
import retrofit2.Call
import retrofit2.Retrofit

class LegendRepository(

    private val retrofit: Retrofit
) {

    private val legendAPI: LegendAPI = retrofit.create(LegendAPI::class.java)

    // fun getLegend(url: String?): Call<MutableList<LegendDto>> =
    //  legendAPI.getLegend(url)

    // fun getLegendDetail(id: String?): Call<LegendDetailsDto> =
    //  legendAPI.getLegends(id)

    //Para Apiary

    fun getLegendsApiary(url: Any?): Call<MutableList<LegendDto>> = legendAPI.getLegendsApiary()

    fun getLegendDatailApiary(id: String?): Call<LegendDetailsDto> = legendAPI.getLegendDetailApiary(id)



}
