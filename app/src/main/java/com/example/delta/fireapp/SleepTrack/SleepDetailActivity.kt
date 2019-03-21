package com.example.delta.fireapp.SleepTrack

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.delta.fireapp.DataModel.SleepData
import com.example.delta.fireapp.R
import com.hsalf.smilerating.SmileRating
import kotlinx.android.synthetic.main.activity_sleep_detail.*
import kotlinx.android.synthetic.main.activity_sleep_track.*
import kotlinx.android.synthetic.main.detail_row.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class SleepDetailActivity : AppCompatActivity() {

    lateinit var sleepDataArray: ArrayList<SleepData>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_detail)

        var bundleObject: Bundle = intent.extras
        sleepDataArray = bundleObject.getSerializable("sleepData") as ArrayList<SleepData>

        recyclerView_detail.layoutManager = LinearLayoutManager(this)
        recyclerView_detail.adapter = SleepDetailAdapter(sleepDataArray, this)

    }

}
