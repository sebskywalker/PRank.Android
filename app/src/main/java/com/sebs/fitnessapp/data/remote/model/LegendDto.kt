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
    var category: String? = null,

    var isUserCreated: Boolean = false  // Indica si fue creada por el usuario
) {
    /**
     * Asigna automáticamente la categoría según el PR de Bench Press.
     */
    fun assignCategory() {
        val benchPress = prBenchPress?.toIntOrNull() ?: 0
        category = when {
            benchPress >= 100 -> "Leyenda"
            benchPress in 50..99 -> "Top Global"
            else -> "Profesional"
        }
    }
}