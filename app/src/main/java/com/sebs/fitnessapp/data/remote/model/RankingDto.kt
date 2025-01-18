package com.sebs.fitnessapp.data.remote.model

data class RankingDto(
    val name: String,
    val imageUrl: String,
    val score: Int // ✅ Se agregó la propiedad 'score'
)