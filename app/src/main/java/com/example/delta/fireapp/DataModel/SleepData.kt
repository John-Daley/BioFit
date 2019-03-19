package com.example.delta.fireapp.DataModel

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Delta on 19.03.2019.
 */
data class SleepData (
        val start: Long = 0,
        var end:Long =0,
        var rating:String? = "",
        val userId:String? = ""


){
    override fun toString():String{
        return "Sleep Sesh: " + convertLongToTime(start)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }
}


