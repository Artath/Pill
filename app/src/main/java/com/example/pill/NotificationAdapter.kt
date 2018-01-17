package com.example.pill

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item2.view.*

/**
 * Created by Дом on 16.01.2018.
 */
class NotificationAdapter(var data:ArrayList<NotificationInfo>, var context: Context): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var notificationDatabaseHelper = NotificationDatabaseHelper(context)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
            NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item2, parent, false))

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.time.text =  data[position].hour.toString() + " : " + data[position].min
        holder.dose.text = "Dosage: " + data[position].dose
        if(data[position].isRepeat == 0) {
            holder.repeatTxt.text = ""
        }else{
            holder.repeatTxt.text = "Repeat every " + data[position].isRepeat + " hours"
        }
        if(data[position].beforeEat == 0) {
            holder.beforeTxt.text = ""
        }else{
            holder.beforeTxt.text = " | Take " + data[position].beforeEat + " minutes before eating"
        }
        if(data[position].afterEat == 0) {
            holder.afterTxt.text = ""
        }else{
            holder.afterTxt.text = " | Take " + data[position].beforeEat + " minutes aftet eating"
        }
        when {
            data[position].isStored == 1 -> holder.img.setImageResource(R.drawable.ic_canceled)
            data[position].isStored == 2 -> holder.img.setImageResource(R.drawable.ic_unknown)
            data[position].isStored == 3 -> holder.img.setImageResource(R.drawable.ic_done)
        }
        if(data[position].isStored == 0){
            holder.itemView.setOnClickListener(View.OnClickListener {
                val dialog = AlertDialog.Builder(context)
                        .setTitle("Cancel notification?")
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                            notificationDatabaseHelper.updateIsStored(data[position].id, 1)
                            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                            val notificationIntent = Intent(context, NotificationPublisher::class.java)
                            val pendingIntent = PendingIntent.getBroadcast(context, data[position].id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                            am.cancel(pendingIntent)
                            var intent = Intent(context, PlanActivity::class.java)
                            startActivity(context, intent, Bundle.EMPTY)
                        })
                        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                        .create()
                dialog.show()
            })
        }else{
            holder.itemView.setOnClickListener(View.OnClickListener {
                val dialog = AlertDialog.Builder(context)
                        .setTitle("Change status")
                        .setPositiveButton("Done", DialogInterface.OnClickListener { _, _ ->
                            notificationDatabaseHelper.updateIsStored(data[position].id, 3)
                            var intent = Intent(context, PlanActivity::class.java)
                            startActivity(context, intent, Bundle.EMPTY)
                        })
                        .setNeutralButton("Unknown", DialogInterface.OnClickListener { _, _ ->
                            notificationDatabaseHelper.updateIsStored(data[position].id, 2)
                            var intent = Intent(context, PlanActivity::class.java)
                            startActivity(context, intent, Bundle.EMPTY)
                        })
                        .setNegativeButton("Canceled", DialogInterface.OnClickListener { dialog, which ->
                            notificationDatabaseHelper.updateIsStored(data[position].id, 1)
                            var intent = Intent(context, PlanActivity::class.java)
                            startActivity(context, intent, Bundle.EMPTY)
                        })
                        .create()
                dialog.show()
            })
        }

    }

    class NotificationViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name = view.nameTxt
        var time = view.timeTxt
        var dose = view.doseTxt
        var repeatTxt = view.repeatTxt
        var beforeTxt = view.beforeTxt
        var afterTxt = view.afterTxt
        var img = view.isStoredImg
    }


}