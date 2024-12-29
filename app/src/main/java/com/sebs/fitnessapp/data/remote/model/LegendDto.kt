package com.sebs.fitnessapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class LegendDto(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("thumbnail")
    var thumbnail: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("PR_bench_press")
    var prBenchPress: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("occupation")
    var occupation: String? = null,

    @SerializedName("latitude")
    var latitude: Double? = null,

    @SerializedName("longitude")
    var longitude: Double? = null,

    @SerializedName("category")
    var category: String? = null
)




