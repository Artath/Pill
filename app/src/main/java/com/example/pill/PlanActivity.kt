package com.example.pill

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_plan.*

class PlanActivity : AppCompatActivity() {

    var notificationDatabaseHelper = NotificationDatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        populate()
    }

    fun populate(){
        var data = notificationDatabaseHelper.data
        var planList = ArrayList<NotificationInfo>()
        var historyList = ArrayList<NotificationInfo>()

        while (data.moveToNext()){
            var id = data.getInt(data.getColumnIndex("ID"))
            var name = data.getString(data.getColumnIndex("name"))
            var dose = data.getString(data.getColumnIndex("dose"))
            var hour = data.getInt(data.getColumnIndex("hour"))
            var min = data.getInt(data.getColumnIndex("min"))
            var isRepeat = data.getInt(data.getColumnIndex("isRepeat"))
            var beforeEat = data.getInt(data.getColumnIndex("beforeEat"))
            var afterEat =  data.getInt(data.getColumnIndex("afterEat"))
            var isStored = data.getInt(data.getColumnIndex("isStored"))
            var notification = NotificationInfo( id, name, dose, min, hour, isRepeat, isStored, beforeEat, afterEat)
            if(isStored == 0){
                planList.add(notification)
            }else{
                historyList.add(notification)
            }
        }
        data.close()

        planRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        planRecyclerView.adapter = NotificationAdapter(planList, this)
        planRecyclerView.adapter.notifyDataSetChanged()

        historyRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        historyRecyclerView.adapter = NotificationAdapter(historyList, this)
        historyRecyclerView.adapter.notifyDataSetChanged()

    }

}
