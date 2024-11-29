package com.sebs.fitnessapp.application

import android.app.Application
import com.sebs.fitnessapp.data.LegendRepository
import com.sebs.fitnessapp.data.remote.RetrofitHelper

class LegendRFApp: Application() {

//END
    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        LegendRepository(retrofit)
    }

}




