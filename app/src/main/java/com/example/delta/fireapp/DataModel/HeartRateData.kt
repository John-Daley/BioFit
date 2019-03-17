package com.example.delta.fireapp.DataModel

/**
 * Created by Delta on 17.03.2019.
 */
data class HeartRateData (

        val BPM: Int = 0,
        val dateTime: MutableMap<String, String> ,
        val userID: String? = ""

)