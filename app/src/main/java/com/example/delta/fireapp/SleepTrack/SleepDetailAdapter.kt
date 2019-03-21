package com.example.delta.fireapp.SleepTrack

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.delta.fireapp.DataModel.SleepData
import com.example.delta.fireapp.R
import kotlinx.android.synthetic.main.detail_row.*
import kotlinx.android.synthetic.main.detail_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SleepDetailAdapter(val sleepDataArray: ArrayList<SleepData>, val parentActivity: SleepDetailActivity): RecyclerView.Adapter<CustomViewHolder>() {

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
        when (sleepDataArray[position].rating){
            "TERRIBLE" -> holder?.itemView?.imageView_smiley?.setImageResource(R.drawable.terrible_smiley)
            "BAD" -> holder?.itemView?.imageView_smiley?.setImageResource(R.drawable.bad_smiley)
            "OKAY" -> holder?.itemView?.imageView_smiley?.setImageResource(R.drawable.okay_smiley)
            "GOOD" -> holder?.itemView?.imageView_smiley?.setImageResource(R.drawable.good_smiley)
            "GREAT" -> holder?.itemView?.imageView_smiley?.setImageResource(R.drawable.great_smiley)
        }
    }

    fun timeRange(holder: CustomViewHolder?, position: Int){
        val startDate = Date(sleepDataArray[position].start)
        val endDate = Date(sleepDataArray[position].end)
        val format = SimpleDateFormat("HH:mm")
        val output: String = "You slept from " + format.format(startDate) + " - " + format.format(endDate)
        holder?.itemView?.textView_timeRange?.text = output

    }

    fun date(holder: CustomViewHolder?, position: Int){

        holder?.itemView?.textView_Date?.text = calculateDate(position)
    }

    fun timeSpanText(holder: CustomViewHolder?, position: Int){
        var start: Long = sleepDataArray[position].start
        var end: Long = sleepDataArray[position].end
        var timeSlept = end-start
        var outPut = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeSlept),
                TimeUnit.MILLISECONDS.toMinutes(timeSlept) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeSlept)),
                TimeUnit.MILLISECONDS.toSeconds(timeSlept) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeSlept)))
        holder?.itemView?.textView_timeSpan?.text = "You slept for " + outPut
    }

    fun calculateDate(position: Int): String{
        val date = Date(sleepDataArray[position].start)
        //var end: Long = sleepDataArray[position].end
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }
}

class CustomViewHolder(view: View): RecyclerView.ViewHolder(view){


}