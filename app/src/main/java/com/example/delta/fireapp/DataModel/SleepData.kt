package com.example.delta.fireapp.DataModel

/**
 * Created by Delta on 19.03.2019.
 */
data class SleepData (
        val startSleep: Long = 0,
        val endSleep:Long =0,
        val rating:String? = "",
        val isSleeping:Boolean = false,
        val userId:String? = ""

)