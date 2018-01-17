package com.example.pill

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reminder.*
import android.view.View
import java.util.*
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


class ReminderActivity : AppCompatActivity() {

    private var name = ""
    private var dose: String = ""
    var notificationDatabaseHelper = NotificationDatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        name = intent.getStringExtra("name")
        textView.text = "Reminder to take " + name

        beforeSwitch.setOnClickListener(View.OnClickListener {afterSwitch.isChecked = false })
        afterSwitch.setOnClickListener(View.OnClickListener {beforeSwitch.isChecked = false })

        setBtn.setOnClickListener(View.OnClickListener {
            if(dosageEdit.length() != 0 && receptionHourEdit.length() != 0 && receptionMinEdit.length() != 0){

                var dose = dosageEdit.text.toString()
                var hour = receptionHourEdit.text.toString().toInt()
                var min = receptionMinEdit.text.toString().toInt()
                var isRepeat = 0
                var isStored = 0
                var beforeEat = 0
                var afterEat = 0

                if(beforeSwitch.isChecked){
                    if(beforeEdit.length() != 0){
                        beforeEat = beforeEdit.text.toString().toInt()
                        writeDbAndMakeNotification(dose, min, hour, isRepeat, isStored, beforeEat, afterEat)
                    }else{
                        Toast.makeText(applicationContext, "Enter time before", Toast.LENGTH_SHORT).show()
                    }
                }else if(afterSwitch.isChecked) {
                    if (afterEdit.length() != 0) {
                        afterEat = afterEdit.text.toString().toInt()
                        writeDbAndMakeNotification(dose, min, hour, isRepeat, isStored, beforeEat, afterEat)
                    }else{
                        Toast.makeText(applicationContext, "Enter time after", Toast.LENGTH_SHORT).show()
                    }
                }else if(!beforeSwitch.isChecked && !afterSwitch.isChecked)
                    writeDbAndMakeNotification(dose, min, hour, isRepeat, isStored, beforeEat, afterEat)


            }else{
                Toast.makeText(applicationContext, "Enter time and dosage", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun writeDbAndMakeNotification(dose:String, min:Int, hour:Int, isRepeat:Int, isStored:Int, beforeEat:Int, afterEat:Int){
        var id  = notificationDatabaseHelper.addData(name, dose, min, hour, isRepeat, isStored, beforeEat, afterEat)
        Toast.makeText(applicationContext, "Write in db id : " + id, Toast.LENGTH_SHORT).show()
        var intent = Intent("MY_ACTION")
        intent.putExtra("KEY", "NEW_NOT")
        intent.putExtra("ID", "" + id)
        sendBroadcast(intent)
        var backIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(backIntent)
    }


    private fun scheduleNotification(notification: Notification) {

        var hour = receptionHourEdit.text.toString().toInt()
        var min = receptionMinEdit.text.toString().toInt()

        val notificationIntent = Intent(this, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 0)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance()
        val setCalendar = Calendar.getInstance()
        setCalendar.set(Calendar.HOUR_OF_DAY, hour)
        setCalendar.set(Calendar.MINUTE, min)
        setCalendar.set(Calendar.SECOND, 0)
        if (setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE, 1)
        var time = setCalendar.timeInMillis

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(repeatSwitch.isChecked) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, periodicityEdit.text.toString().toLong()*60*60*100, pendingIntent)
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }


}
