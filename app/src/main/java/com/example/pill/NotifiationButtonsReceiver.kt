package com.example.pill

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import com.example.pill.R.id.*
import kotlinx.android.synthetic.main.activity_reminder.*
import java.sql.Time
import java.util.*


class NotificationButtonsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var sp = PreferenceManager.getDefaultSharedPreferences(context)
        var postponeTime = sp.getString("postponeTime", "15").toInt()
        var receivedValue = intent.getStringExtra("KEY")
        var notificationDatabaseHelper = NotificationDatabaseHelper(context)

        fun scheduleNotification(notification: Notification, isRepeat: Int, hour: Int, min: Int, id:Int) {
            val notificationIntent = Intent(context, NotificationPublisher::class.java)
            notificationIntent.putExtra("ID", id)
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
            val pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val calendar = Calendar.getInstance()
            val setCalendar = Calendar.getInstance()
            setCalendar.set(Calendar.HOUR_OF_DAY, hour)
            setCalendar.set(Calendar.MINUTE, min)
            setCalendar.set(Calendar.SECOND, 0)
            if (setCalendar.before(calendar))
                setCalendar.add(Calendar.DATE, 1)
            var time = setCalendar.timeInMillis
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if(isRepeat == 0) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }else{
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, isRepeat.toLong()*60*60*100, pendingIntent)
            }
        }

        fun getNotification(id:Int, delayTime: Int, currentName: String, type: Int, dose:String, rc1:Int, rc2:Int, rc3:Int): Notification {
            var title: String = ""
            var text: String = ""
            var key1 = ""
            var key2 = ""
            var key3 = ""

            when(type){
                0 -> {
                    title = "Time to take " + currentName
                    text = "Dosage: " + dose
                    key1 = "CANCEL"
                    key2 = "ACCEPT"
                    key3 = "POSTPONE"
                }
                1 -> {
                    title = "Time to take " + currentName
                    text = "You should to take the $currentName $delayTime minutes before eating\n Dosage: $dose"
                    key1 = "CANCEL_BEFORE"
                    key2 = "ACCEPT_BEFORE"
                    key3 = "POSTPONE_BEFORE"
                }
                2 -> {
                    title = "Time to eat!"
                    text = "Now you need to eat, then take the $currentName in $delayTime minutes"
                    key1 = "CANCEL_AFTER"
                    key2 = "ACCEPT_AFTER"
                    key3 = "POSTPONE_AFTER"
                }
            }

            var intentCancel = Intent("MY_ACTION")
            intentCancel.putExtra("KEY", key1)
            intentCancel.putExtra("ID", id)
            var cancelPendingIntent = PendingIntent.getBroadcast(context, rc1, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT)

            var intentAccept = Intent("MY_ACTION")
            intentAccept.putExtra("KEY", key2)
            intentAccept.putExtra("ID", id)
            var acceptPendingIntent = PendingIntent.getBroadcast(context, rc2, intentAccept, PendingIntent.FLAG_UPDATE_CURRENT)

            var intentPostpone  = Intent("MY_ACTION")
            intentPostpone.putExtra("KEY", key3)
            intentPostpone.putExtra("ID", id)
            var postponePendingIntent = PendingIntent.getBroadcast(context, rc3, intentPostpone, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = Notification.Builder(context)
            builder.setContentTitle(title)
            builder.setContentText(text)
            builder.setSmallIcon(R.drawable.ic_launcher)
            builder.addAction(R.drawable.ic_not_cancel, "Cancel", cancelPendingIntent)
            builder.addAction(R.drawable.ic_not_postpone, "Postpone", postponePendingIntent)
            builder.addAction(R.drawable.ic_not_done, "Accept", acceptPendingIntent)

            return builder.build()
        }

        when (receivedValue) {
            "CANCEL" -> {
                var id = intent.getIntExtra("ID", 0)
                notificationDatabaseHelper.updateIsStored(id, 1)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "CANCEL id: $id", Toast.LENGTH_SHORT).show()
            }
            "POSTPONE" -> {
                var id = intent.getIntExtra("ID", 0)
                var data = notificationDatabaseHelper.data
                data.move(id)
                var dose = data.getString(data.getColumnIndex("dose"))
                var name = data.getString(data.getColumnIndex("name"))
                var hour = Time(System.currentTimeMillis()).hours
                var min = Time(System.currentTimeMillis()).minutes + postponeTime
                scheduleNotification(getNotification(id, 0, name, 0, dose, 0, 1, 2), 0, hour, min, id)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "pospone id: $id", Toast.LENGTH_SHORT).show()
            }
            "ACCEPT" -> {
                var id = intent.getIntExtra("ID", 0)
                notificationDatabaseHelper.updateIsStored(id, 3)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "ACCEPT id: $id", Toast.LENGTH_SHORT).show()
            }

            "CANCEL_BEFORE" -> {
                var id = intent.getIntExtra("ID", 0)
                notificationDatabaseHelper.updateIsStored(id, 1)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "CANCEL_BEFORE id: $id", Toast.LENGTH_SHORT).show()
            }
            "POSTPONE_BEFORE" -> {
                var id = intent.getIntExtra("ID", 0)
                var data = notificationDatabaseHelper.data
                data.move(id)
                var dose = data.getString(data.getColumnIndex("dose"))
                var name = data.getString(data.getColumnIndex("name"))
                var beforeEat = data.getInt(data.getColumnIndex("beforeEat"))
                var hour = Time(System.currentTimeMillis()).hours
                var min = Time(System.currentTimeMillis()).minutes + postponeTime
                scheduleNotification(getNotification(id, beforeEat, name, 0, dose, 3, 4, 5), 0, hour, min, id)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "POSTPONE_BEFORE id: $id", Toast.LENGTH_SHORT).show()
            }
            "ACCEPT_BEFORE" -> {
                var id = intent.getIntExtra("ID", 0)
                notificationDatabaseHelper.updateIsStored(id, 3)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "ACCEPT_BEFORE id: $id", Toast.LENGTH_SHORT).show()
            }
            "CANCEL_AFTER" -> {
                var id = intent.getIntExtra("ID", 0)
                notificationDatabaseHelper.updateIsStored(id, 1)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "CANCEL_AFTER id: $id", Toast.LENGTH_SHORT).show()
            }
            "POSTPONE_AFTER" -> {
                var id = intent.getIntExtra("ID", 0)
                var data = notificationDatabaseHelper.data
                data.move(id)
                var dose = data.getString(data.getColumnIndex("dose"))
                var name = data.getString(data.getColumnIndex("name"))
                var afterEat = data.getInt(data.getColumnIndex("afterEat"))
                var hour = Time(System.currentTimeMillis()).hours
                var min = Time(System.currentTimeMillis()).minutes + postponeTime
                scheduleNotification(getNotification(id, afterEat, name, 0, dose, 6, 7, 8), 0, hour, min, id)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "POSTPONE_AFTER id: $id", Toast.LENGTH_SHORT).show()
            }
            "ACCEPT_AFTER" -> {
                var id = intent.getIntExtra("ID", 0)
                notificationDatabaseHelper.updateIsStored(id, 3)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                //Toast.makeText(context, "ACCEPT_AFTER id: $id", Toast.LENGTH_SHORT).show()
            }

            "NEW_NOT"->{

                var id = intent.getStringExtra("ID")
                var currentId: Int = id.toInt()
                var data = notificationDatabaseHelper.data
                data.move(id.toInt())
                var dose = data.getString(data.getColumnIndex("dose"))
                var name = data.getString(data.getColumnIndex("name"))
                var isRepeat = data.getInt(data.getColumnIndex("isRepeat"))
                var hour = data.getInt(data.getColumnIndex("hour"))
                var min = data.getInt(data.getColumnIndex("min"))
                var beforeEat = data.getInt(data.getColumnIndex("beforeEat"))
                var afterEat = data.getInt(data.getColumnIndex("afterEat"))

                //Toast.makeText(context, "dose:$dose id:$currentId name:$name", Toast.LENGTH_SHORT).show()

                if(beforeEat == 0 && afterEat == 0){
                    scheduleNotification(getNotification(currentId, 0, name, 0, dose, 0, 1, 2), isRepeat, hour, min, currentId)
                }else if (beforeEat > 0){
                    scheduleNotification(getNotification(currentId, beforeEat, name, 1, dose, 3, 4, 5), isRepeat, hour, min, currentId)
                }else if(afterEat > 0){
                    scheduleNotification(getNotification(currentId, afterEat, name, 2, dose, 6, 7, 8), isRepeat, hour, min, currentId)
                }

            }
        }
    }
}