package com.example.pill

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent.getIntExtra("ID", 0)
        var notificationDatabaseHelper = NotificationDatabaseHelper(context)
        notificationDatabaseHelper.updateIsStored(id, 2)
        Toast.makeText(context, "id to notif manager: $id", Toast.LENGTH_SHORT).show()
        notificationManager.notify(id, notification)

    }

    companion object {
        var NOTIFICATION_ID = "notification-id"
        var NOTIFICATION = "notification"
    }
}