package ru.geekbrains.kotlin_lesson1.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log
import ru.geekbrains.kotlin_lesson1.utlis.*
import java.lang.Thread.sleep

class MainService(serviceName: String = "Same_Service") : IntentService(serviceName) {
    override fun onHandleIntent(intent: Intent?) {
        Log.d(LOG_KEY,"MainService on")

        intent?.let {
            val stringExtra = it.getStringExtra(MAIN_ACTIVITY_KEY)
            Log.d(LOG_KEY,"MainActivity говорит: $stringExtra")
            sleep(MAIN_SERVICE_HARD_WORK_TIME)
            val message = Intent(BROADCAST_RECEIVER_CHANNEL_KEY)
            message.putExtra(MAIN_SERVICE_KEY,"Hello MainService!")
            sendBroadcast(message)
            //LocalBroadcastManager.getInstance(this).sendBroadcast(message)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_KEY,"MainService off")
    }
}