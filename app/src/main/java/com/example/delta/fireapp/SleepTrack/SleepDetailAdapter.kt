package com.example.delta.fireapp.SleepTrack

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.delta.fireapp.DataModel.SleepData
import com.example.delta.fireapp.R
import kotlinx.android.synthetic.main.detail_row.*
import kotlinx.android.synthetic.main.detail_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SleepDetailAdapter(val sleepDataArray: ArrayList<SleepData>): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return sleepDataArray.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.detail_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        timeSpanText(holder, position)
        timeRating(holder, position)
        date(holder, position)
        timeRange(holder, position)
    }

    fun timeRating(holder: CustomViewHolder?, position: Int){
        holder?.itemView?.textView_rating?.text = sleepDataArray[position].rating

    }

    fun timeRange(holder: CustomViewHolder?, position: Int){
        val startDate = Date(sleepDataArray[position].start)
        val endDate = Date(sleepDataArray[position].end)
        val format = SimpleDateFormat("HH:mm")
        val output: String = format.format(startDate) + " - " + format.format(endDate)
        holder?.itemView?.textView_timeRange?.text = output

    }

    fun date(holder: CustomViewHolder?, position: Int){
        val date = Date(sleepDataArray[position].start)
        //var end: Long = sleepDataArray[position].end
        val format = SimpleDateFormat("yyyy.MM.dd")
        holder?.itemView?.textView_Date?.text = format.format(date)
    }

    fun timeSpanText(holder: CustomViewHolder?, position: Int){
        var start: Long = sleepDataArray[position].start
        var end: Long = sleepDataArray[position].end
        var timeSlept = end-start
        var outPut = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeSlept),
                TimeUnit.MILLISECONDS.toMinutes(timeSlept) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeSlept)),
                TimeUnit.MILLISECONDS.toSeconds(timeSlept) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeSlept)))
        holder?.itemView?.textView_timeSpan?.text = outPut
    }

}

class CustomViewHolder(view: View): RecyclerView.ViewHolder(view){


}